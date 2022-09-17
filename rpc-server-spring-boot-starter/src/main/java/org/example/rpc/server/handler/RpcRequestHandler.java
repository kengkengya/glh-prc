package org.example.rpc.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.example.rpc.core.common.RpcRequest;
import org.example.rpc.core.common.RpcResponse;
import org.example.rpc.core.protocol.MessageHeader;
import org.example.rpc.core.protocol.MessageProtocol;
import org.example.rpc.core.protocol.MsgStatus;
import org.example.rpc.core.protocol.MsgType;
import org.example.rpc.server.store.LocalServerCache;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 对rpc 请求进行处理
 *
 * @author guolonghang
 * @date 2022年09月17日13:29:22
 */
@Slf4j
public class RpcRequestHandler extends SimpleChannelInboundHandler<MessageProtocol<RpcRequest>> {
    /**
     * 使用线程池对rpc请求进行处理
     */
    private final ThreadPoolExecutor threadPoolExecutor =
            new ThreadPoolExecutor(10, 20, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10000));


    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, MessageProtocol<RpcRequest> rpcRequestMessageProtocol) throws Exception {
        //多线程处理多个rpc请求
        threadPoolExecutor.submit(() -> {
            //构建response响应
            MessageProtocol<RpcResponse> resProtocol = new MessageProtocol<>();
            RpcResponse rpcResponse = new RpcResponse();
            MessageHeader header = rpcRequestMessageProtocol.getHeader();

            //设置消息体状态
            header.setMsgType(MsgType.RESPONSE.getType());
            try {
                //解析请求体
                Object result = handler(rpcRequestMessageProtocol.getBody());
                //将处理后的消息体放入data中
                rpcResponse.setData(result);
                //修改header状态为成功
                header.setStatus(MsgStatus.SUCCESS.getCode());
                //封装响应消息协议
                resProtocol.setHeader(header);
                resProtocol.setBody(rpcResponse);
            } catch (Throwable throwable) {
                header.setStatus(MsgStatus.FAIL.getCode());
                rpcResponse.setMessage(throwable.toString());
                log.error("process request {} error", header.getRequestId(), throwable);
            }

            //通过netty的channel上下文将res进行刷新,进行写回
            channelHandlerContext.writeAndFlush(resProtocol);


        })
    }


    /**
     * 通过反射处理数据
     *
     * @param request
     * @return
     */
    private Object handler(RpcRequest request) {

        Object bean = LocalServerCache.get(request.getServiceName());
        if (null == bean) {
            throw new RuntimeException(String.format("service not exist: %s !", request.getServiceName()));
        }
        try {
            Class<?> clazz = bean.getClass();
            //解析请求，获取需要执行的method已经对应的参数
            Method method = clazz.getMethod(request.getMethod(), request.getParameterTypes());
            return method.invoke(bean, request.getParameters());
        } catch (Exception e) {
            log.error(ExceptionUtils.getStackTrace(e));
            throw new RuntimeException(e);
        }

    }
}
