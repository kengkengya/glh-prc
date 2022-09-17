package org.example.rpc.core.protocol;

import lombok.Data;
import org.example.rpc.core.protocol.MessageHeader;

import java.io.Serializable;

/**
 * @author guolonghang
 * @Classname MessageProtocol
 * @Description 消息协议
 */
@Data
public class MessageProtocol<T> implements Serializable {

    /**
     *  消息头
     */
    private MessageHeader header;

    /**
     *  消息体
     */
    private T body;

}
