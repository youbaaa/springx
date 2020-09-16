/*
 * bianque.com
 * Copyright (C) 2013-2020 All Rights Reserved.
 */
package com.youbaaa.demo.server;

import com.youbaaa.spring.annotation.ServiceX;

import java.util.HashMap;
import java.util.Map;

/**
 * @author huang.zhangh
 * @version DemoServer.java, v 0.1 2020-09-09 20:15
 */
@ServiceX("demoServer")
public class DemoServer {
    private ThreadLocal<Integer> currentUser = ThreadLocal.withInitial(() -> null);

    public void threadLocal() {
        //设置用户信息之前先查询一次ThreadLocal中的用户信息
        String before = Thread.currentThread().getName() + ":" + currentUser.get();
        //设置用户信息到ThreadLocal
        currentUser.set(1);
        //设置用户信息之后再查询一次ThreadLocal中的用户信息
        String after = Thread.currentThread().getName() + ":" + currentUser.get();
        //汇总输出两次查询结果
        Map result = new HashMap();
        result.put("before", before);
        result.put("after", after);
        System.out.println(result);
    }
}