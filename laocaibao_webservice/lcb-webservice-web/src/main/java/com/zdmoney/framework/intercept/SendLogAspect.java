package com.zdmoney.framework.intercept;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;

import websvc.utils.SysLogUtil;

/**
 * Created by 00225181 on 2016/3/2.
 */
@Aspect
@Slf4j
public class SendLogAspect {

    @Pointcut("@annotation(com.zdmoney.framework.annotation.SaveSendLog)")
    public void sendLog(){}

    @AfterReturning(value="sendLog()", returning="result")
    public void doAfter(JoinPoint joinPoint, Object result){
        Object[] arguments = joinPoint.getArgs();//取被注解方法的参数
        if(arguments.length > 0){
            String groupName = (String)arguments[0];
            String key = (String)arguments[1];
            SysLogUtil.save(groupName+","+key+","+result,"Marketing-MQ-Result",joinPoint.getTarget().getClass().getName());
        }

    }

    @Before("sendLog()")
    public void before(JoinPoint joinPoint)throws  Exception{
        Object[] arguments = joinPoint.getArgs();//取被注解方法的参数
        if(arguments.length > 0){
            String groupName = (String)arguments[0];
            String key = (String)arguments[1];
            String body = (String)arguments[2];
            SysLogUtil.save(groupName+","+key+","+body,"Marketing-MQ-Send",joinPoint.getTarget().getClass().getName());
        }
    }
}
