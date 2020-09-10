/*
 * bianque.com
 * Copyright (C) 2013-2020 All Rights Reserved.
 */
package com.youbaaa.demo.controller;

import com.youbaaa.demo.server.DemoServer;
import com.youbaaa.spring.annotation.AutowiredX;
import com.youbaaa.spring.annotation.ControllerX;
import com.youbaaa.spring.annotation.RequestMappingX;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author huang.zhangh
 * @version DemoController.java, v 0.1 2020-09-09 20:13
 */
@ControllerX
@RequestMappingX("/demo")
public class DemoController {
    @AutowiredX
    DemoServer demoServer;

    @RequestMappingX("query")
    public void query(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.getWriter().write("hello");
    }
}