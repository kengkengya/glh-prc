package org.example.rpc.client.transport;

import lombok.extern.slf4j.Slf4j;

/**
 * 创建一个netty网络请求工厂
 * @author guolonghang
 * @date 2022年09月17日14:49:57
 */
@Slf4j
public class NetClientTransportFactory {

    public static NetClientTransport getNetClientTransport(){
        return new NettyNetClientTransport();
    }


}