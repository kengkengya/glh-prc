package org.example.rpc.client.proxy;


import org.example.rpc.client.config.RpcClientProperties;
import org.example.rpc.core.discovery.DiscoveryService;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * @author guolonghang
 * @date 2022年09月17日14:38:26
 */
public class ClientStubProxyFactory {

    private Map<Class<?>, Object> objectCache = new HashMap<>();

    /**
     * 获取代理对象
     *
     * @param clazz   接口
     * @param version 服务版本
     * @param <T>
     * @return 代理对象
     */
    public <T> T getProxy(Class<T> clazz, String version, DiscoveryService discoveryService, RpcClientProperties properties) {
        return (T) objectCache.computeIfAbsent(clazz, clz ->
                Proxy.newProxyInstance(clz.getClassLoader(), new Class[]{clz},
                        new ClientStubInvocationHandler(discoveryService, properties, clz, version))
        );
    }
}
