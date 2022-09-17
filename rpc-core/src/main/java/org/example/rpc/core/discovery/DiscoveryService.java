package org.example.rpc.core.discovery;

import org.example.rpc.core.common.ServiceInfo;

/**
 * 发现服务
 * @author guolonghang
 * @date 2022年09月17日14:01:17
 */
public interface DiscoveryService {

    /**
     *  发现
     * @param serviceName
     * @return
     * @throws Exception
     */
    ServiceInfo discovery(String serviceName) throws Exception;

}
