package org.example.rpc.core.register;

import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;
import org.example.rpc.core.common.ServiceInfo;

import java.io.IOException;

/**
 * zk 注册服务
 * @author guolonghang
 * @date 2022年09月17日14:11:55
 */
@Slf4j
public class ZookeeperRegistryService implements RegistryService {

    public static final int BASE_SLEEP_TIME_MS = 1000;
    public static final int MAX_RETRIES = 5;
    public static final String ZK_BASE_PATH = "/demo_rpc";

    private ServiceDiscovery<ServiceInfo> serviceDiscovery;

    public ZookeeperRegistryService(String registryAddr) {
        try {
            //新建zk客户端
            CuratorFramework client = CuratorFrameworkFactory.newClient(registryAddr, new ExponentialBackoffRetry(BASE_SLEEP_TIME_MS, MAX_RETRIES));
            //启动zk客户端
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

    @Override
    public void register(ServiceInfo serviceInfo) throws Exception {
        ServiceInstance<ServiceInfo> serviceInstance = ServiceInstance.<ServiceInfo>builder()
                .name(serviceInfo.getServiceName())
                .address(serviceInfo.getAddress())
                .port(serviceInfo.getPort())
                .payload(serviceInfo)
                .build();
        serviceDiscovery.registerService(serviceInstance);
    }

    @Override
    public void unRegister(ServiceInfo serviceInfo) throws Exception {
        ServiceInstance<ServiceInfo> serviceInstance = ServiceInstance
                .<ServiceInfo>builder()
                .name(serviceInfo.getServiceName())
                .address(serviceInfo.getAddress())
                .port(serviceInfo.getPort())
                .payload(serviceInfo)
                .build();
        serviceDiscovery.unregisterService(serviceInstance);
    }

    @Override
    public void destroy() throws IOException {
        serviceDiscovery.close();
    }

}