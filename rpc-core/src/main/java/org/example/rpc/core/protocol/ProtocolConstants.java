package org.example.rpc.core.protocol;


/**
 * @author guolonghang
 * @Date 2022年09月17日01:34:25
 */
public class ProtocolConstants {

    /**
     * 报文头部长度42位
     */
    public static final int HEADER_TOTAL_LEN = 42;

    /**
     * 魔数
     */
    public static final short MAGIC = 0x00;

    /**
     * 版本
     */
    public static final byte VERSION = 0x1;

    /**
     * 消息 id 32
     */
    public static final int REQ_LEN = 32;

}
