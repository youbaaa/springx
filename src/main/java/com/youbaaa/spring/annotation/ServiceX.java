package com.youbaaa.spring.annotation;

import java.lang.annotation.*;

/**
 * @author huang.zhangh
 * @version ServiceX.java, v 0.1 2020-09-08 14:39
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ServiceX {
    String value() default "";
}