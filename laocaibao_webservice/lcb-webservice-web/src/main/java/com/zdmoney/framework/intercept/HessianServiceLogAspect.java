/*
* Copyright (c) 2016 zendaimoney.com. All Rights Reserved.
*/
package com.zdmoney.framework.intercept;

import com.google.common.collect.Sets;
import com.zdmoney.integral.api.common.dto.ResultDto;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import websvc.utils.SysLogUtil;

import java.util.Set;

/**
 * HessianServiceLogAspect
 * <p/>
 * Author: Hao Chen
 * Date: 2016-03-28 16:17
 * Mail: haoc@zendaimoney.com
 */
@Aspect
@Deprecated
public class HessianServiceLogAspect {

    public static final String SUCCESS = "SUCCESS";
    public static final String FAILED = "FAILED";
    private static Set<String> handlerMethodSet = Sets.newHashSet();

    static {
        handlerMethodSet.add("registGetIntegral");
        handlerMethodSet.add("authGetIntegral");
        handlerMethodSet.add("signGetIntegral");
        handlerMethodSet.add("invitationRegistGetIntegral");
        handlerMethodSet.add("firstOrderGetIntegral");
        handlerMethodSet.add("firstOrderDeductionIntegral");
        handlerMethodSet.add("investmentIncomeGetIntegral");
        handlerMethodSet.add("rebateGetIntegral");
        handlerMethodSet.add("giveGetIntegral");
        handlerMethodSet.add("invitationAuthGetIntegral");
        handlerMethodSet.add("invitationInvestmentGetIntegral");

        handlerMethodSet.add("exchange");
        handlerMethodSet.add("consume");
        handlerMethodSet.add("refund");
        handlerMethodSet.add("lifeExchange");

        handlerMethodSet.add("publishCouponBySource");
        handlerMethodSet.add("consumeCoupon");
        handlerMethodSet.add("refundCoupon");
        handlerMethodSet.add("authAccount");
        handlerMethodSet.add("activeCoupon");
        handlerMethodSet.add("gainCoupon");
        handlerMethodSet.add("extendCouponDay");

        handlerMethodSet.add("recharge");
        handlerMethodSet.add("withdraw");
        handlerMethodSet.add("refund");
        handlerMethodSet.add("receive");
        handlerMethodSet.add("purchase");
        handlerMethodSet.add("withdrawRefund");

        handlerMethodSet.add("addLotteryQualification");

        handlerMethodSet.add("publishVoucher");
        handlerMethodSet.add("consumeVoucher");
        handlerMethodSet.add("refundVoucher");
        handlerMethodSet.add("extendVoucherDay");

        handlerMethodSet.add("gainCoin");
        handlerMethodSet.add("consumeCoin");
        handlerMethodSet.add("refund");
        handlerMethodSet.add("executeToZero");
        handlerMethodSet.add("batchGainCoin");

        handlerMethodSet.add("finishTask");

        handlerMethodSet.add("sendTemplateMsg");
    }


    @Pointcut("execution(* com.zdmoney..api.facade.*Service.*(..)) || execution(* com.zendaimoney..api.facade.*Service.*(..)) ")
    public void hessianServiceMethod() {
    }

    @Before("hessianServiceMethod()")
    public void before(JoinPoint joinPoint) throws Exception {
        String methodName = joinPoint.getSignature().getName();
        if (handlerMethodSet.contains(methodName)) {
            String className = joinPoint.getThis().toString();
            Object[] arguments = joinPoint.getArgs();
            SysLogUtil.save(arguments, methodName + "-BEFORE", className);
        }
    }


    @AfterReturning(value = "hessianServiceMethod()", returning = "result")
    public void doAfter(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        if (handlerMethodSet.contains(methodName)) {
            String className = joinPoint.getThis().toString();
            String logType = "";
            if (result instanceof ResultDto) {
                ResultDto resultDto = (ResultDto) result;
                logType = methodName + "-" + (resultDto.isSuccess() ? SUCCESS : FAILED);
            } else if (result instanceof com.zdmoney.marketing.api.dto.ResultDto) {
                com.zdmoney.marketing.api.dto.ResultDto resultDto = (com.zdmoney.marketing.api.dto.ResultDto) result;
                logType = methodName + "-" + (resultDto.isSuccess() ? SUCCESS : FAILED);
            } else if (result instanceof com.zendaimoney.laocaibao.wx.api.dto.ResultDto) {
                com.zendaimoney.laocaibao.wx.api.dto.ResultDto resultDto = (com.zendaimoney.laocaibao.wx.api.dto.ResultDto) result;
                logType = methodName + "-" + (resultDto.isSuccess() ? SUCCESS : FAILED);
            }
            SysLogUtil.save(result, logType, className);
        }
    }
}