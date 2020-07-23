package com.zdmoney.base;

import com.zdmoney.common.LcbWebApplicationContext;
import com.zdmoney.utils.test.base.BaseDbunitSpringTest;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;
import org.springframework.web.servlet.mvc.annotation.DefaultAnnotationHandlerMapping;

/**
 * @ Author : Evan.
 * @ Description :
 * @ Date : Crreate in 2019/4/10 14:30
 * @Mail : xuyt@zendaimoney.com
 */
public class GenericBaseTest extends BaseDbunitSpringTest {

    static {
        System.setProperty("dubbo.registry.address","172.17.34.208:2181,172.17.34.210:2181,172.17.34.211:2181");
        System.setProperty("consumer.assets.version","4.0.1");
        System.setProperty("env","dev");
        System.setProperty("dev_meta","http://172.17.34.16:8080");
        System.setProperty("app.id","webservice");
        System.setProperty("file.encoding","UTF-8");
    }

    private static HandlerMapping handlerMapping;
    private static HandlerAdapter handlerAdapter;
    public static XmlWebApplicationContext context ;

    /**
     * 读取配置文件
     */
    @BeforeClass
    public static void setUpx() {
        if (handlerMapping == null) {
            String[] configs = {
                    "classpath:conf/spring-test.xml"
//                    ,
//                    "classpath:conf/spring.xml",
//                    "classpath:conf/spring-mybatis.xml",
//                    "classpath:conf/spring-redis.xml",
//                    "classpath:conf/spring-mvc.xml",
//                    "classpath:conf/spring-hessian.xml",
//                    "classpath:conf/spring-rocketMq.xml"
            };
            context = new LcbWebApplicationContext();
            context.setConfigLocations(configs);
            MockServletContext msc = new MockServletContext();
            context.setServletContext(msc);
            context.refresh();
            msc.setAttribute(
                    WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE,
                    context);// TODO
            handlerMapping = (HandlerMapping) context
                    .getBean(DefaultAnnotationHandlerMapping.class);

            handlerAdapter = (HandlerAdapter) context
                    .getBean(context
                            .getBeanNamesForType(AnnotationMethodHandlerAdapter.class)[0]);

        }
    }
}
