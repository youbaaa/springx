package com.youbaaa.spring.annotation;

import java.lang.annotation.*;

/**
 * @author huang.zhangh
 * @version ControllerX.java, v 0.1 2020-09-08 14:38
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ControllerX {
    String value() default "";
}