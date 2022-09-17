package org.example.rpc.client.config;

import lombok.Data;


/**
 * @author guolonghang
 * rpc 客户端的属性填充
 */
@Data
public class RpcClientProperties {

    /**
     * 负载均衡
     */
    private String balance;

    /**
     * 序列化
     */
    private String serialization;

    /**
     * 服务发现地址
     */
    private String discoveryAddr = "0.0.0.0:2181";

    /**
     * 服务调用超时
     */
    private Integer timeout;

}
