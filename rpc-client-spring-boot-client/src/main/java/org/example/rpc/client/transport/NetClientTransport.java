package org.example.rpc.client.transport;

import org.example.rpc.core.common.RpcResponse;
import org.example.rpc.core.protocol.MessageProtocol;

/**
 * 网络传输层
 * @author guolonghang
 */
public interface NetClientTransport {

    /**
     *  发送数据
     * @param metadata
     * @return
     * @throws Exception
     */
    MessageProtocol<RpcResponse> sendRequest(RequestMetadata metadata) throws Exception;

}