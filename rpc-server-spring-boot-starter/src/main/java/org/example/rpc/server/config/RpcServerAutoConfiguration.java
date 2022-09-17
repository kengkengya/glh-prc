package org.example.rpc.server.config;


import org.example.rpc.core.register.RegistryService;
import org.example.rpc.core.register.ZookeeperRegistryService;
import org.example.rpc.server.RpcServerProvider;
import org.example.rpc.server.transport.NettyRpcServer;
import org.example.rpc.server.transport.RpcServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自动装配rpc server
 * rpc server端依赖 1、rpc zk注册服务 bean 2、netty解析请求-响应bean 3、rpcServer provider bean
 *
 * @author guolonghang
 * @Date 2022年09月17日01:10:04
 */
@Configuration
@EnableConfigurationProperties(RpcServerProperties.class)
public class RpcServerAutoConfiguration {

    @Autowired
    private RpcServerProperties properties;

    @Bean
    @ConditionalOnMissingBean
    public RegistryService registryService() {
        return new ZookeeperRegistryService(properties.getRegistryAddr());
    }

    @Bean
    @ConditionalOnMissingBean(RpcServer.class)
    RpcServer RpcServer() {
        return new NettyRpcServer();
    }

    @Bean
    @ConditionalOnMissingBean(RpcServerProvider.class)
    RpcServerProvider rpcServerProvider(@Autowired RegistryService registryService,
                                        @Autowired RpcServer rpcServer,
                                        @Autowired RpcServerProperties rpcServerProperties) {
        return new RpcServerProvider(rpcServerProperties, registryService, rpcServer);
    }
}
