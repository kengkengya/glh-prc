package org.example.rpc.core.discovery;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.example.rpc.core.balancer.LoadBalance;
import org.example.rpc.core.common.ServiceInfo;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * zk发现服务
 * @author guolonghang
 * @date 2022年09月17日14:01:53
 */
@Slf4j
public class ZookeeperDiscoveryService implements DiscoveryService {

    public static final int BASE_SLEEP_TIME_MS = 1000;
    public static final int MAX_RETRIES = 10;
    public static final String ZK_BASE_PATH = "/demo_rpc";

    private ServiceDiscovery<ServiceInfo> serviceDiscovery;

    private LoadBalance loadBalance;

    public ZookeeperDiscoveryService(String registryAddr, LoadBalance loadBalance) {
        this.loadBalance = loadBalance;
        try {
            //创建一个zk客户端进行连接zk
            CuratorFramework client = CuratorFrameworkFactory.newClient(registryAddr, new ExponentialBackoffRetry(BASE_SLEEP_TIME_MS, MAX_RETRIES));
            //启动zk
            client.start();
            JsonInstanceSerializer<ServiceInfo> serializer = new JsonInstanceSerializer<>(ServiceInfo.class);
            this.serviceDiscovery = ServiceDiscoveryBuilder.builder(ServiceInfo.class)
                    .client(client)
                    .serializer(serializer)
                    .basePath(ZK_BASE_PATH)
                    .build();
            this.serviceDiscovery.start();
        } catch (Exception e) {
            log.error("serviceDiscovery start error :{}", e);
        }
    }


    /**
     *  服务发现
     * @param serviceName
     * @return
     * @throws Exception
     */
    @Override
    public ServiceInfo discovery(String serviceName) throws Exception {
        Collection<ServiceInstance<ServiceInfo>> serviceInstances = serviceDiscovery.queryForInstances(serviceName);
        return CollectionUtils.isEmpty(serviceInstances) ? null
                : loadBalance.chooseOne(serviceInstances.stream().map(ServiceInstance::getPayload).collect(Collectors.toList()));
    }

}
