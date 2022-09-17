package org.example.rpc.server.transport;

/**
 * rpc服务端接口，底层使用netty实现
 * @author guolonghang
 * @Date 2022年09月17日00:58:34
 */
public interface RpcServer {

    /**
     * 开启服务
     */
    void start(int port);

}
