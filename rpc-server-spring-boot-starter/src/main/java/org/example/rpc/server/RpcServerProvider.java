package org.example.rpc.server;

import lombok.extern.slf4j.Slf4j;
import org.example.rpc.core.common.ServiceInfo;
import org.example.rpc.core.common.ServiceUtil;
import org.example.rpc.core.register.RegistryService;
import org.example.rpc.server.annotation.RpcService;
import org.example.rpc.server.config.RpcServerProperties;
import org.example.rpc.server.store.LocalServerCache;
import org.example.rpc.server.transport.RpcServer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.CommandLineRunner;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.net.InetAddress;


/**
 * @author guolonghang
 * @Date 2022年09月17日00:22:57
 */
@Slf4j
public class RpcServerProvider implements BeanPostProcessor, CommandLineRunner {

    private RpcServerProperties properties;

    private RegistryService registryService;

    private RpcServer rpcServer;

    public RpcServerProvider(RpcServerProperties properties, RegistryService registryService, RpcServer rpcServer) {
        this.properties = properties;
        this.registryService = registryService;
        this.rpcServer = rpcServer;
    }

    /**
     * CommandLineRunner 项目启动之后，启动rpc服务 处理请求
     *
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) throws Exception {
        new Thread(() -> rpcServer.start(properties.getPort())).start();
        log.info(" rpc server :{} start, appName :{} , port :{}", rpcServer, properties.getAppName(), properties.getPort());
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                // 关闭之后把服务从ZK上清除
                registryService.destroy();
            } catch (Exception ex) {
                log.error("", ExceptionUtils.getMessage(ex));
            }

        }));
    }

    /**
     * 所有bean注册完成后进行处理
     * <p>
     * 暴露服务注册到注册中心
     * <p>
     * 容器启动后开启netty服务处理请求
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        RpcService rpcService = bean.getClass().getAnnotation(RpcService.class);

        if (rpcService != null) {
            //服务注册
            try {
                //获取当前bean的服务类型
                String serviceName = rpcService.interfaceType().getName();
                String version = rpcService.version();
                //将rpc缓存到本地
                LocalServerCache.store(ServiceUtil.serviceKey(serviceName, version), bean);

                //实例化bean
                ServiceInfo serviceInfo = new ServiceInfo();
                serviceInfo.setServiceName(serviceName);
                serviceInfo.setAddress(InetAddress.getLocalHost().getHostAddress());
                serviceInfo.setPort(properties.getPort());
                serviceInfo.setVersion(version);
                serviceInfo.setAppName(properties.getAppName());

                //注册到zk中
                registryService.register(serviceInfo);
            } catch (Exception e) {
                log.error("服务出错：{}", ExceptionUtils.getMessage(e));
            }
        }
        return bean;
    }
}
