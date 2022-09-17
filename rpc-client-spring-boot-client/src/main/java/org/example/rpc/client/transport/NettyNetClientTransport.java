package org.example.rpc.client.transport;



import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.example.rpc.client.cache.LocalRpcResponseCache;
import org.example.rpc.client.handler.RpcResponseHandler;
import org.example.rpc.core.codec.RpcDecoder;
import org.example.rpc.core.codec.RpcEncoder;
import org.example.rpc.core.common.RpcRequest;
import org.example.rpc.core.common.RpcResponse;
import org.example.rpc.core.protocol.MessageProtocol;

import java.util.concurrent.TimeUnit;

@Slf4j
public class NettyNetClientTransport implements NetClientTransport {

    private final Bootstrap bootstrap;
    private final EventLoopGroup eventLoopGroup;
    private final RpcResponseHandler handler;


    public NettyNetClientTransport() {
        bootstrap = new Bootstrap();
        //四线程非阻塞式IO
        eventLoopGroup = new NioEventLoopGroup(4);
        handler = new RpcResponseHandler();
        bootstrap.group(eventLoopGroup).channel(NioSocketChannel.class)
                //初始化通道
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline()
                                // 解码 是入站操作 将二进制解码成消息
                                .addLast(new RpcDecoder())
                                // 接收响应 入站操作
                                .addLast(handler)
                                // 编码 是出站操作 将消息编写二进制
                                .addLast(new RpcEncoder<>());
                    }
                });
    }

    /**
     * 发送请求时，将请求id与响应结果future缓存到本地
     * @param metadata
     * @return
     * @throws Exception
     */
    @Override
    public MessageProtocol<RpcResponse> sendRequest(RequestMetadata metadata) throws Exception {
        MessageProtocol<RpcRequest> protocol = metadata.getProtocol();
        RpcFuture<MessageProtocol<RpcResponse>> future = new RpcFuture<>();
        LocalRpcResponseCache.add(protocol.getHeader().getRequestId(), future);

        // TCP 连接
        ChannelFuture channelFuture = bootstrap.connect(metadata.getAddress(), metadata.getPort()).sync();
        channelFuture.addListener((ChannelFutureListener) arg0 -> {
            if (channelFuture.isSuccess()) {
                log.info("connect rpc server {} on port {} success.", metadata.getAddress(), metadata.getPort());
            } else {
                log.error("connect rpc server {} on port {} failed.", metadata.getAddress(), metadata.getPort());
                channelFuture.cause().printStackTrace();
                eventLoopGroup.shutdownGracefully();
            }
        });
        // 写入数据
        channelFuture.channel().writeAndFlush(protocol);
        //如果未超时，则进入异步等待结果返回。
        return metadata.getTimeout() != null ? future.get(metadata.getTimeout(), TimeUnit.MILLISECONDS) : future.get();
    }
}
