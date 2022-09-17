package org.example.rpc.client.cache;

import org.example.rpc.client.transport.RpcFuture;
import org.example.rpc.core.common.RpcResponse;
import org.example.rpc.core.protocol.MessageProtocol;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 请求和响应映射对象
 * @author guolonghang
 * @date 2022年09月17日14:22:48
 */
public class LocalRpcResponseCache {

    /**
     * 请求响应映射cache，使用并发map
     */
    private static Map<String, RpcFuture<MessageProtocol<RpcResponse>>> requestResponseCache = new ConcurrentHashMap<>();

    /**
     *  添加请求和响应的映射关系
     * @param reqId 消息体 id，请求rpc结果
     * @param future
     */
    public static void add(String reqId, RpcFuture<MessageProtocol<RpcResponse>> future){
        requestResponseCache.put(reqId, future);
    }

    /**
     *  设置响应数据
     * @param reqId
     * @param messageProtocol
     */
    public static void fillResponse(String reqId, MessageProtocol<RpcResponse> messageProtocol){
        // 获取缓存中的 future
        RpcFuture<MessageProtocol<RpcResponse>> future = requestResponseCache.get(reqId);

        // 设置数据
        future.setResponse(messageProtocol);

        // 移除缓存
        requestResponseCache.remove(reqId);
    }
}
