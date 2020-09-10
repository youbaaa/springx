/*
 * bianque.com
 * Copyright (C) 2013-2020 All Rights Reserved.
 */
package com.youbaaa.spring.annotation;

import java.lang.annotation.*;

/**
 * @author huang.zhangh
 * @version RequestParamX.java, v 0.1 2020-09-10 10:12
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParamX {
    String value() default "";
}