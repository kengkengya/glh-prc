package org.example.rpc.core.balancer;

import org.example.rpc.core.common.ServiceInfo;

import java.util.List;
import java.util.Random;

/**
 * 随机算法
 * @author guolonghang
 * @date 2022年09月17日14:06:11
 */
public class RandomBalance implements LoadBalance{

    private static Random random = new Random();

    @Override
    public ServiceInfo chooseOne(List<ServiceInfo> services) {
        return services.get(random.nextInt(services.size()));
    }
}