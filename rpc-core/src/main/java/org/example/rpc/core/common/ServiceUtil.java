package org.example.rpc.core.common;

/**
 * @author guolonghang
 * @Date 2022年09月17日00:46:49
 */
public class ServiceUtil {

    /**
     * @param serviceName
     * @param version
     * @return
     */
    public static String serviceKey(String serviceName, String version) {
        //实现逻辑是通过成员+分隔符连接，然后在结果截掉最后一个分隔符。
        return String.join("-", serviceName, version);
    }

}