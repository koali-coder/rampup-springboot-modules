package com.demo.rampup.common.annotation;

import java.lang.annotation.*;

/**
 * @author zhouyw
 * @date 2022-05-07
 * @describe com.demo.rampup.common.annotation
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuditLog {

    // 方法释义
    String value() default "";

}
