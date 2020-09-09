package com.youbaaa.spring.annotation;

import java.lang.annotation.*;

/**
 * @author huang.zhangh
 * @version RequestMappingX.java, v 0.1 2020-09-08 14:39
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMappingX {
    String value() default "";
}