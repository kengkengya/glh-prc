package org.example.rpc.client.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.example.rpc.client.cache.LocalRpcResponseCache;
import org.example.rpc.core.common.RpcResponse;
import org.example.rpc.core.protocol.MessageProtocol;

/**
 * rpc响应处理器
 *
 * @author guolonghang
 * @date 2022年09月17日14:44:20
 */
@Slf4j
public class RpcResponseHandler extends SimpleChannelInboundHandler<MessageProtocol<RpcResponse>> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageProtocol<RpcResponse> rpcResponseMessageProtocol) throws Exception {
        String requestId = rpcResponseMessageProtocol.getHeader().getRequestId();
        // 收到响应 设置响应数据
        LocalRpcResponseCache.fillResponse(requestId, rpcResponseMessageProtocol);
    }
}