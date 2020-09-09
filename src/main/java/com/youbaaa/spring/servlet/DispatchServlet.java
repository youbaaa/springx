package com.youbaaa.spring.servlet;

import com.youbaaa.spring.annotation.AutowiredX;
import com.youbaaa.spring.annotation.ControllerX;
import com.youbaaa.spring.annotation.RequestMappingX;
import com.youbaaa.spring.annotation.ServiceX;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;

/**
 * @author huang.zhangh
 * @version DispatchServlet.java, v 0.1 2020-09-08 14:09
 */
public class DispatchServlet extends HttpServlet {
    private static final String LOCATION = "contextConfigLocation";
    private static final String PACKAGE_SCAN = "scanPackage";
    private Properties properties = new Properties();
    private List<String> className = new ArrayList<>();
    private Map<String, Object> ioc = new HashMap<>();
    private Map<String, Method> handMapping = new HashMap<>();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {

        //1，加载配置文件;doLoadConfig()方法的实现，将文件读取到Properties对象中：
        doLoadConfig(config.getInitParameter(LOCATION));
        //2，扫描相关的类;doScanner()方法，递归扫描出所有的Class文件
        doScanner(properties.getProperty(PACKAGE_SCAN));
        //3，初始化IOC;doInstance()方法，初始化所有相关的类，并放入到IOC容器之中。
        // IOC容器的key默认是类名首字母小写，如果是自己设置类名，则优先使用自定义的。因此，要先写一个针对类名首字母处理的工具方法
        doInstance();
        //4，DI;doAutowired()方法,将初始化到IOC容器中的类，需要赋值的字段进行赋值
        doAutowired();
        //5，初始化HandleMapping;initHandlerMapping()方法，将RequestMappingX中配置的信息和Method进行关联，并保存这些关系。
        initHandlerMapping();
    }

    private void initHandlerMapping() {
        if (ioc.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            Class<?> aClass = entry.getValue().getClass();
            if (!aClass.isAnnotationPresent(ControllerX.class)) {
                continue;
            }
            String baseUrl = "";
            if (aClass.isAnnotationPresent(RequestMappingX.class)) {
                RequestMappingX requestMappingX = aClass.getAnnotation(RequestMappingX.class);
                baseUrl = requestMappingX.value().trim();
            }
            Method[] methods = aClass.getMethods();
            for (Method method : methods) {
                if (!method.isAnnotationPresent(RequestMappingX.class)) {
                    continue;
                }
                RequestMappingX requestMappingX = aClass.getAnnotation(RequestMappingX.class);
                String url = ("/" + baseUrl + requestMappingX.value().trim()).replace("/+", "/");
                handMapping.put(url, method);
            }
        }
    }

    private void doAutowired() {
        if (ioc.isEmpty()) {
            return;
        }
        for (Map.Entry<String, Object> entry : ioc.entrySet()) {
            Field[] declaredFields = entry.getValue().getClass().getDeclaredFields();
            for (Field field : declaredFields) {
                if (!field.isAnnotationPresent(AutowiredX.class)) {
                    continue;
                }
                AutowiredX autowiredX = field.getAnnotation(AutowiredX.class);
                String beanName = autowiredX.value().trim();
                if ("".equals(beanName)) {
                    beanName = field.getType().getName();
                }
                field.setAccessible(true);
                try {
                    field.set(entry.getValue(), ioc.get(beanName));
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }


        }

    }

    private void doInstance() {
        if (className.size() == 0) {
            return;
        }
        for (String className : className) {
            try {
                Class<?> aClass = Class.forName(className);
                if (aClass.isAnnotationPresent(ControllerX.class)) {
                    String firstCase = lowerFirstCase(aClass.getSimpleName());
                    ioc.put(firstCase, aClass.newInstance());
                } else if (aClass.isAnnotationPresent(ServiceX.class)) {
                    ServiceX serviceX = aClass.getAnnotation(ServiceX.class);
                    String serverName = serviceX.value();
                    if (!"".equals(serverName.trim())) {
                        ioc.put(serverName, aClass.newInstance());
                        continue;
                    }
                    Class<?>[] interfaces = aClass.getInterfaces();
                    for (Class<?> i : interfaces) {
                        ioc.put(i.getName(), aClass.newInstance());
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void doScanner(String packageName) {
        URL resource = this.getClass().getClassLoader().getResource("/" + packageName.replace("\\.", "/"));
        assert resource != null;
        File dir = new File(resource.getFile());
        for (File file : Objects.requireNonNull(dir.listFiles())) {
            if (file.isDirectory()) {
                doScanner(packageName + file.getName());
            } else {
                className.add(packageName + "." + file.getName().replace(".class", "").trim());
            }
        }
    }

    private void doLoadConfig(String location) {
        InputStream is = null;
        is = this.getClass().getClassLoader().getResourceAsStream(location);
        try {
            properties.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 字符穿首字母小写
     *
     * @param str
     * @return
     */
    private String lowerFirstCase(String str) {
        char[] chars = str.toCharArray();
        chars[0] += 32;
        return String.valueOf(chars);
    }
}