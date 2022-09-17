package org.example.rpc.core.common;

import lombok.Data;

import java.io.Serializable;

/**
 * rpc response
 * @author guolonghang
 */
@Data
public class RpcResponse implements Serializable {

    private Object data;
    private String message;

}