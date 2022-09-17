package org.example.rpc.server.annotation;


import org.springframework.stereotype.Service;

import java.lang.annotation.*;

/**
 * rpc注解，用于暴露服务
 *
 * @author guolonghang
 * @Date 2022年09月17日00:25:41
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Service
public @interface RpcService {

    /**
     * 暴露服务接口类型
     *
     * @return
     */
    Class<?> interfaceType() default Object.class;

    /**
     * 服务版本
     *
     * @return
     */
    String version() default "1.0";

}
