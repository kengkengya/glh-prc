package org.example.rpc.client.transport;


import lombok.Builder;
import lombok.Data;
import org.example.rpc.core.common.RpcRequest;
import org.example.rpc.core.protocol.MessageProtocol;

import java.io.Serializable;

/**
 * @author guolonghang
 * @Classname RequestMetadata
 * @Description 请求元数据
 */
@Data
@Builder
public class RequestMetadata implements Serializable {

    /**
     *  协议
     */
    private MessageProtocol<RpcRequest> protocol;

    /**
     *  地址
     */
    private String address;

    /**
     *  端口
     */
    private Integer port;

    /**
     *  服务调用超时
     */
    private Integer timeout;

}
