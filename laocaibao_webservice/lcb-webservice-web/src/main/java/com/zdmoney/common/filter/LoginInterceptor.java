package com.zdmoney.common.filter;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** 
 * Spring MVC拦截器 
 * @author gary 
 * 
 */
@Slf4j
public class LoginInterceptor extends HandlerInterceptorAdapter  {

	private String[] ignoreCusts;

    public void setIgnoreCusts(String[] ignoreCusts) {
        this.ignoreCusts = ignoreCusts;
    }

    /**
     * 在Controller方法前进行拦截 
     * 如果返回false 
     *      从当前拦截器往回执行所有拦截器的afterCompletion方法,再退出拦截器链. 
     * 如果返回true 
     *      执行下一个拦截器,直到所有拦截器都执行完毕. 
     *      再运行被拦截的Controller. 
     *      然后进入拦截器链,从最后一个拦截器往回运行所有拦截器的postHandle方法. 
     *      接着依旧是从最后一个拦截器往回执行所有拦截器的afterCompletion方法. 
     */  
    @Override  
    public boolean preHandle(HttpServletRequest request,  
            HttpServletResponse response, Object handler) throws Exception {  
    	//在preHandle中，可以进行编码、安全控制等处理
    	log.debug("=====preHandle====");
        //业务逻辑
    	String code = request.getParameter("arg0");
    	if("400002".equals(code)){
            String json = request.getParameter("arg1");
            String cellphone = JSON.parseObject(json).getJSONObject("reqParam").getString("cmCellphone");
    	    if(ignoreCusts!=null){
    	        for(String cmNumber : ignoreCusts){
    	            if(cmNumber.equals(cellphone)){
                        log.error("【{}】该用户在限制登陆名单中，不允许登陆",cellphone);
                        return false;
                    }
                }
            }
            log.info("用户【】登陆",cellphone);
        }
        return true;  
    }

}
