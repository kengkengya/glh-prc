package org.example.rpc.core.register;

import org.example.rpc.core.common.ServiceInfo;

import java.io.IOException;

/**
 * @author guolonghang
 */
public interface RegistryService {

    /**
     * 注册一个服务
     * @param serviceInfo
     * @throws Exception
     */
    void register(ServiceInfo serviceInfo) throws Exception;

    /**
     *
     * @param serviceInfo
     * @throws Exception
     */
    void unRegister(ServiceInfo serviceInfo) throws Exception;

    /**
     * @throws IOException
     */
    void destroy() throws IOException;

}