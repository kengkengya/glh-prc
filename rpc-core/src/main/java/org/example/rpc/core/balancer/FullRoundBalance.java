package org.example.rpc.core.balancer;

import org.example.rpc.core.common.ServiceInfo;

import java.util.List;

/**
 * 轮询算法
 * @author guolonghang
 * @date 2022年09月17日14:03:55
 */
public class FullRoundBalance implements LoadBalance {


    private int index;

    @Override
    public synchronized ServiceInfo chooseOne(List<ServiceInfo> services) {
        // 加锁防止多线程情况下，index超出services.size()
        if (index >= services.size()) {
            index = 0;
        }
        return services.get(index++);
    }
}
