package com.xiaojiesir.rpc.provider.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface RpcService {

    /**
     * 服务类型
     * @return
     */
    Class<?> serviceInterface() default Object.class;

    /**
     * 服务版本
     * @return
     */
    String serviceVersion() default "1.0";
}
