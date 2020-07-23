package com.zdmoney.base;

import com.zdmoney.common.LcbWebApplicationContext;
import org.junit.BeforeClass;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.HandlerAdapter;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.annotation.AnnotationMethodHandlerAdapter;
import org.springframework.web.servlet.mvc.annotation.DefaultAnnotationHandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 描述 :JUnit测试action时使用的基类
 *
 * @author : weiNian
 * @create : 2019-03-05 10:57
 * @Mail: wein@zendaimoney.com
 **/
public class JUnitActionBase {

    static{
        System.setProperty("env","dev");
        System.setProperty("dev_meta","http://172.17.34.75:8080");
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
    public static void setUp() {
        if (handlerMapping == null) {
            String[] configs = {
                    "classpath:conf/spring-test.xml",
                    "classpath:conf/spring.xml",
                    "classpath:conf/spring-mybatis.xml",
                    "classpath:conf/spring-redis.xml",
                    "classpath:conf/spring-mvc.xml",
                    "classpath:conf/spring-hessian.xml",
                    "classpath:conf/spring-rocketMq.xml" };
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

    /**
     * 执行request请求的action
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    public ModelAndView excuteAction(HttpServletRequest request,
                                     HttpServletResponse response) throws Exception {
        // 这里需要声明request的实际类型，否则会报错
        request.setAttribute(HandlerMapping.INTROSPECT_TYPE_LEVEL_MAPPING, true);
        HandlerExecutionChain chain = handlerMapping.getHandler(request);
        ModelAndView model = null;
        try {
            model = handlerAdapter
                    .handle(request, response, chain.getHandler());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return model;
    }
}