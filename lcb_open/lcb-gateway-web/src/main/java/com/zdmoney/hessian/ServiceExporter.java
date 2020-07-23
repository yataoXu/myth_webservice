package com.zdmoney.hessian;/**
 * Created by pc05 on 2017/11/22.
 */

import com.zdmoney.conf.OpenConfig;
import com.zdmoney.facade.FacadeOpenService;
import com.zdmoney.message.api.facade.IMsgEmailFacadeService;
import com.zdmoney.trace.hessian.client.common.TraceHessianProxyFactory;
import com.zdmoney.trace.hessian.client.common.TraceHessianProxyFactoryBean;
import com.zdmoney.trace.hessian.service.common.TraceHessianServiceExporter;
import com.zdmoney.webservice.api.facade.IFacadeOpenService;
import com.zdmoney.webservice.api.facade.ILcbGatewayFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * 描述 :
 *
 * @author : huangcy
 * @create : 2017-11-22 14:36
 * @email : huangcy01@zendaimoney.com
 **/
@Component
public class ServiceExporter {

    @Autowired
    private OpenConfig openConfig;

    @Autowired
    private FacadeOpenService facadeOpenService;

    @Autowired
    private TraceHessianProxyFactory traceHessianProxyFactory;

    @Bean
    public TraceHessianProxyFactoryBean msgEmailFacade() {
        TraceHessianProxyFactoryBean msgEmailFacadeService = new TraceHessianProxyFactoryBean();
        msgEmailFacadeService.setServiceUrl(openConfig.getMsgEmailRemoteUrl() + "/msgEmailFacadeService");
        msgEmailFacadeService.setServiceInterface(IMsgEmailFacadeService.class);
        msgEmailFacadeService.setProxyFactory(traceHessianProxyFactory);
        return msgEmailFacadeService;
    }

    @Bean
    public TraceHessianProxyFactoryBean lcbGatewayFacade() {
        TraceHessianProxyFactoryBean lcbGatewayFacade = new TraceHessianProxyFactoryBean();
        lcbGatewayFacade.setServiceUrl(openConfig.getWebserviceRemoteUrl() + "/lcbGatewayFacade");
        lcbGatewayFacade.setServiceInterface(ILcbGatewayFacade.class);
        lcbGatewayFacade.setProxyFactory(traceHessianProxyFactory);
        return lcbGatewayFacade;
    }


    @Bean
    public TraceHessianProxyFactory proxyFactory(){
        TraceHessianProxyFactory traceHessianProxyFactory = new TraceHessianProxyFactory();
        traceHessianProxyFactory.setChannel("LCB");
        return traceHessianProxyFactory;
    }

    @Bean(name = "/facadeOpenService")
    public TraceHessianServiceExporter facadeOpenService() {
        TraceHessianServiceExporter traceHessianServiceExporter = new TraceHessianServiceExporter();
        traceHessianServiceExporter.setService(facadeOpenService);
        traceHessianServiceExporter.setServiceInterface(IFacadeOpenService.class);
        return traceHessianServiceExporter;
    }
}
