package org.example.rpc.provider.service;

import org.example.HelloService;
import org.example.rpc.server.annotation.RpcService;

/**
 * @author guolonghang
 * @Date 2022年09月17日00:10:11
 */
@RpcService(interfaceType = HelloService.class, version = "1.0")
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        return String.format("您好，%s 调用rpc成功",name);
    }
}
