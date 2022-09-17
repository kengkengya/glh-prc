package org.example.rpc.server.transport;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.example.rpc.core.codec.RpcDecoder;
import org.example.rpc.core.codec.RpcEncoder;
import org.example.rpc.server.handler.RpcRequestHandler;

import java.net.InetAddress;


/**
 * 使用netty来处理rpc 请求
 *
 * @author guolonghang
 * @Date 2022年09月17日01:14:16
 */
@Slf4j
public class NettyRpcServer implements RpcServer {

    @Override
    public void start(int port) {
        ////创建两个线程组 bossGroup、workerGroup
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup worker = new NioEventLoopGroup();

        try {
            String serverAddress = InetAddress.getLocalHost().getHostAddress();
            //创建服务端的启动对象，设置参数
            ServerBootstrap bootstrap = new ServerBootstrap();
            //将线程组加入进去
            bootstrap.group(boss, worker)
                    //设置服务端通道实现类型
                    .channel(NioServerSocketChannel.class)
                    //初始化通道对象
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            //给pipeline管道设置处理器
                            socketChannel.pipeline()
                                    // 自定义协议编码
                                    .addLast(new RpcEncoder())
                                    // 自定义协议解码
                                    .addLast(new RpcDecoder())
                                    // 自定义请求处理器
                                    .addLast(new RpcRequestHandler());
                        }
                    })
                    //设置保持活动连接状态
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    //设置线程队列得到连接个数
                    .option(ChannelOption.SO_BACKLOG, 128);
            //绑定端口号，启动服务端
            ChannelFuture channelFuture = bootstrap.bind(serverAddress, port).sync();
            log.info("server addr {} started on port {}", serverAddress, port);
            //关闭通道监听
            channelFuture.channel().closeFuture().sync();
        } catch (Exception e) {
            log.info("", ExceptionUtils.getStackTrace(e));
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }
}
