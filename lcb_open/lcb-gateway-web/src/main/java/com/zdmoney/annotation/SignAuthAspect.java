package com.zdmoney.annotation;/**
 * Created by pc05 on 2017/11/21.
 */


import cn.hutool.json.JSONUtil;
import com.zdmoney.conf.OpenConfig;
import com.zdmoney.trace.utils.JsonUtils;
import com.zdmoney.utils.SignUtils;
import com.zdmoney.utils.WebUtils;
import com.zdmoney.webservice.api.common.dto.ResultDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

/**
 * 描述 :
 *
 * @author : huangcy
 * @create : 2017-11-21 14:26
 * @email : huangcy01@zendaimoney.com
 **/
@Aspect
@Slf4j
@Component
public class SignAuthAspect {

    @Autowired
    private OpenConfig openConfig;

    @Pointcut(value="@annotation(com.zdmoney.annotation.SignAuth)")
    public void signAuthService(){}

    @Around("signAuthService()")
    public Object around(ProceedingJoinPoint point) throws Throwable{
        String targetName = this.getTargetName(point);
        String params = this.getParams(point);
        log.info("REQ: method=[{}], params=[{}]", targetName, params);
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        boolean ipValid = checkIpWhite(request);
        if (!ipValid) {
            return ResultDto.FAIL("ip不在白名单之内!");
        }

        boolean signValid = checkSign(request, WebUtils.getRequestMap(request));
        if (!signValid) {
            return ResultDto.FAIL("验签失败!");
        }
        Object proceed = point.proceed();
        log.info("RES: method=[{}], params=[{}], result=[{}]", targetName, params, JSONUtil.parseObj(proceed==null?"":proceed,false));
        return proceed;
    }

    private String getTargetName(JoinPoint point) {
        return point.getSignature().getDeclaringTypeName() + "." + point.getSignature().getName();
    }

    private String getParams(JoinPoint point) {
        return Arrays.toString(point.getArgs());
    }

    /**
     * 验证ip白名单
     *
     * @param request
     * @return
     * @throws Exception
     */
    private boolean checkIpWhite(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        log.info("X-Real-IP={}", ip);
        if (StringUtils.isBlank(ip)) {
            ip = request.getRemoteAddr();
        }
        log.info("visit ip=[{}] whiteIpList=[{}]", ip, openConfig.getWhiteIpList());
        return StringUtils.contains(openConfig.getWhiteIpList(), ip);
    }

    /**
     * 验证签名
     *
     * @param request
     * @return
     * @throws Exception
     */
    private boolean checkSign(HttpServletRequest request, Map<String, String> paramMap) {
        String paramSign = request.getParameter("sign");
        TreeMap<String, Object> signMap = new TreeMap();
        signMap.putAll(paramMap);
        log.info(JsonUtils.toJson(signMap));
        String md5Sign = SignUtils.getSign(signMap, openConfig.getCreditReqKey());
        return StringUtils.equals(paramSign, md5Sign);
    }
}
