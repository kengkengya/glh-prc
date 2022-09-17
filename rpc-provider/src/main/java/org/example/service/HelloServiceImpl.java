package org.example.service;

import org.example.HelloService;

/**
 * @author guolonghang
 * @Date 2022年09月17日00:10:11
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        return String.format("您好，%s 调用rpc成功",name);
    }
}
