package org.example.rpc.core.balancer;

import org.example.rpc.core.common.ServiceInfo;

import java.util.List;

/**
 * 负载均衡算法接口
 */
public interface LoadBalance {
    /**
     * 负载均衡选择
     * @param services
     * @return
     */
    ServiceInfo chooseOne(List<ServiceInfo> services);
}