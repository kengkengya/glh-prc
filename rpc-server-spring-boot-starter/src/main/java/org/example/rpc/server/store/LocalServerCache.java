package org.example.rpc.server.store;

import java.util.HashMap;
import java.util.Map;

/**
 * 在处理rpc请求时，可以通过本地cache进行获取，
 * 将暴露出的服务缓存到本地，避免反射实例化服务开销
 * @author guolonghang
 * @Date 2022年09月17日00:35:08
 */
public final class LocalServerCache {
    /**
     * 服务缓存列表
     */
    public static final Map<String,Object> SERVER_CACHE_MAP = new HashMap<>();

    public static void store(String serverName, Object server) {
        /**
         * 如果给定的key不存在，它就变成了put(key, value)。但是，如果key已经存在一些值，我们 remappingFunction 可以选择合并的方式。
         * 这个功能是完美契机上面的场景：
         *
         * 只需返回新值即可覆盖旧值： (old, new) -> new
         * 只需返回旧值即可保留旧值： (old, new) -> old
         * 以某种方式合并两者，例如： (old, new) -> old + new
         * 甚至删除旧值： (old, new) -> null
         */
        SERVER_CACHE_MAP.merge(serverName,server,(Object oldValue,Object newValue)->newValue);
    }

    public static Object get(String serverName) {
        return SERVER_CACHE_MAP.get(serverName);
    }

    public static Map<String, Object> getAll(){
        return null;
    }

}
