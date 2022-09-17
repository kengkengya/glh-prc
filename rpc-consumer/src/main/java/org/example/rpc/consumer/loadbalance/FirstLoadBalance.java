package org.example.rpc.consumer.loadbalance;

import lombok.extern.slf4j.Slf4j;
import org.example.rpc.core.balancer.LoadBalance;
import org.example.rpc.core.common.ServiceInfo;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author guolonghang
 * @date 2022年09月17日15:01:30
 * 自定义负载均衡
 */
@Slf4j
//@Component
public class FirstLoadBalance implements LoadBalance {

    @Override
    public ServiceInfo chooseOne(List<ServiceInfo> services) {
        log.info("---------FirstLoadBalance-----------------");
        return services.get(0);
    }
}