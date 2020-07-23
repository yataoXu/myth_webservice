/*
* Copyright (c) 2016 zendaimoney.com. All Rights Reserved.
*/
package com.zdmoney.common.listener;

import com.google.common.collect.Maps;
import com.zdmoney.common.anno.FunctionId;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import websvc.servant.base.FunctionService;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * InstantiationTracingBeanPostListener
 * <p/>
 * Author: Hao Chen
 * Date: 2016-03-25 11:26
 * Mail: haoc@zendaimoney.com
 */
public class InstantiationTracingBeanPostListener implements ApplicationListener<ContextRefreshedEvent> {

    public static Map<String, Object[]> methodCacheMap = Maps.newHashMap();

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext context = contextRefreshedEvent.getApplicationContext();
        if (context.getParent() == null) {
            Map<String, FunctionService> functionServiceMap = context.getBeansOfType(FunctionService.class);
            for (FunctionService functionService : functionServiceMap.values()) {
                Method[] declaredMethods = functionService.getClass().getDeclaredMethods();
                for (Method method : declaredMethods) {
                    FunctionId functionId = method.getAnnotation(FunctionId.class);
                    if (functionId != null) {
                        if (methodCacheMap.containsKey(functionId.value())) {
                            throw new RuntimeException("重复的功能号: " + functionId.value());
                        }
                        methodCacheMap.put(functionId.value(), new Object[]{functionService, method});
                    }
                }
            }
        }
    }
}