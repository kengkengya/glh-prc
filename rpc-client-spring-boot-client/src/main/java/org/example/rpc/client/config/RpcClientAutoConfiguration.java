package org.example.rpc.client.config;


import org.example.rpc.core.balancer.FullRoundBalance;
import org.example.rpc.core.balancer.LoadBalance;
import org.example.rpc.core.balancer.RandomBalance;
import org.example.rpc.core.discovery.DiscoveryService;
import org.example.rpc.core.discovery.ZookeeperDiscoveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.bind.BindResult;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

/**
 * @author guolonghang
 * @Classname RpcClientAutoConfiguration
 * @Description
 * @date 2022年09月17日14:31:55
 */
@Configuration
public class RpcClientAutoConfiguration {

    /**
     * 将properties文件与RpcClientProperties绑定
     *
     * @param environment
     * @return
     */
    @Bean
    public RpcClientProperties rpcClientProperties(Environment environment) {
        BindResult<RpcClientProperties> result = Binder.get(environment).bind("rpc.client", RpcClientProperties.class);
        return result.get();
    }

    @Bean
    @ConditionalOnMissingBean
    public ClientStubProxyFactory clientStubProxyFactory() {
        return new ClientStubProxyFactory();
    }


    /**
     * 负载均衡策略----随机轮询bean
     *
     * @return
     */
    @Primary
    @Bean(name = "loadBalance")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "rpc.client", name = "balance", havingValue = "randomBalance", matchIfMissing = true)
    public LoadBalance randomBalance() {
        return new RandomBalance();
    }

    /**
     * 负载均衡策略----轮询bean
     *
     * @return
     */
    @Bean(name = "loadBalance")
    @ConditionalOnMissingBean
    @ConditionalOnProperty(prefix = "rpc.client", name = "balance", havingValue = "fullRoundBalance")
    public LoadBalance loadBalance() {
        return new FullRoundBalance();
    }

    /**
     * 服务发现bean，用于在zk集群发现服务
     *
     * @param properties
     * @param loadBalance
     * @return
     */
    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnBean({RpcClientProperties.class, LoadBalance.class})
    public DiscoveryService discoveryService(@Autowired RpcClientProperties properties,
                                             @Autowired LoadBalance loadBalance) {
        return new ZookeeperDiscoveryService(properties.getDiscoveryAddr(), loadBalance);
    }

    @Bean
    @ConditionalOnMissingBean
    public RpcClientProcessor rpcClientProcessor(@Autowired ClientStubProxyFactory clientStubProxyFactory,
                                                 @Autowired DiscoveryService discoveryService,
                                                 @Autowired RpcClientProperties properties) {
        return new RpcClientProcessor(clientStubProxyFactory, discoveryService, properties);
    }

}
