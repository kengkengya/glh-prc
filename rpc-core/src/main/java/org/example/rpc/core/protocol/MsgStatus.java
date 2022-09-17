package org.example.rpc.core.protocol;

import lombok.Getter;

/**
 * 消息状态
 * @Date 2022年09月17日01:36:02
 * @author guolonghang
 */
public enum MsgStatus {
    SUCCESS((byte)0),
    FAIL((byte)1);

    @Getter
    private final byte code;

    MsgStatus(byte code) {
        this.code = code;
    }

    public static boolean isSuccess(byte code){
        return MsgStatus.SUCCESS.code == code;
    }

}
