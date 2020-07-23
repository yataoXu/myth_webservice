package com.zdmoney.base;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import websvc.utils.SpringContextHelper;


@RunWith(JUnit4ClassRunner.class)
@ContextConfiguration(locations = {


        "classpath:conf/spring.xml",
        "classpath:conf/spring-mybatis.xml",
        "classpath:conf/spring-redis.xml",
        "classpath:conf/spring-mvc.xml",
        "classpath:conf/spring-hessian.xml",
        "classpath:conf/spring-rocketMq.xml"

})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = false)
public  class BaseTest extends AbstractTransactionalJUnit4SpringContextTests implements ApplicationContextAware {
    @Autowired
    protected ApplicationContext ctx;

    @Before
    public void init(){
        SpringContextHelper.context = ctx;
//        SysSwitchHelper.setSwitchService(sysSwitchService);
    }
}

