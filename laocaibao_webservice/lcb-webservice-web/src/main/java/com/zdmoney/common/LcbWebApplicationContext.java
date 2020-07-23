package com.zdmoney.common;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.web.context.support.XmlWebApplicationContext;

/**
 * 解决Spring循环引用问题
 * Created by gaol on 2017/6/19
         **/
public class LcbWebApplicationContext extends XmlWebApplicationContext {

    @Override
    protected DefaultListableBeanFactory createBeanFactory() {
        DefaultListableBeanFactory beanFactory =  super.createBeanFactory();
        beanFactory.setAllowRawInjectionDespiteWrapping(true);
        return beanFactory;
    }

}
