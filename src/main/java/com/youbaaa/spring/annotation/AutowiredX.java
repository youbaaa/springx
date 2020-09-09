package com.youbaaa.spring.annotation;

import java.lang.annotation.*;

/**
 * @author huang.zhangh
 * @version AutowiredX.java, v 0.1 2020-09-08 14:35
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutowiredX {
    String value() default "";
}