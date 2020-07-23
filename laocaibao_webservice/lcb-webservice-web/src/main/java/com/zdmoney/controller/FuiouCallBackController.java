package com.zdmoney.controller;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSON;
import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.constant.InterfaceCode;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.mapper.trade.BusiTradeFlowMapper;
import com.zdmoney.models.sys.SysRequestLog;
import com.zdmoney.models.trade.BusiTradeFlow;
import com.zdmoney.models.zdpay.UserRechargeBO;
import com.zdmoney.service.AsyncService;
import com.zdmoney.service.SerialNoGeneratorService;
import com.zdmoney.service.ZdPayService;
import com.zdmoney.utils.MailUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;

/**
 * 收银台回调
 *
 * Author: gosling
 * Date: 2018年8月15日 16:44:05
 */
@Slf4j
@RestController
@RequestMapping("/zdpay")
public class FuiouCallBackController {

    @Autowired
    private ConfigParamBean configParamBean;

    @Autowired
    private AsyncService asyncService;

    @Autowired
    private BusiTradeFlowMapper busiTradeFlowMapper;

    /**
     * 回调入口
     * @param request
     * @return
     */
    @RequestMapping("/backNotify")
    public String backNotify(HttpServletRequest request){
        String code = request.getParameter("code"); // 返回状态
        String msg = request.getParameter("msg"); // 返回信息
        String interfaceCode = request.getParameter("interfaceCode"); // 接口编号
        String data = request.getParameter("data"); // 回调数据对象
        String methodName = "zdpay" + interfaceCode;

        SysRequestLog reqLog = new SysRequestLog();
        Date startTime = new Date();
        reqLog.setTransNo(SerialNoGeneratorService.generateTransNoForReq());
        reqLog.setMethodCode(methodName);
        reqLog.setReqParams(data);
        if (!"0000".equals(code)&&!configParamBean.getBypassedCode().contains(interfaceCode)) {
            log.error("收银台处理失败，接口号:{},失败原因:{}", interfaceCode, msg);
            if (InterfaceCode.USER_RECHARGE.getCode().equals(interfaceCode)) {
                UserRechargeBO rechargeBO = JSON.parseObject(data, UserRechargeBO.class);
                BusiTradeFlow tradeFlow = busiTradeFlowMapper.queryTradeFlow(rechargeBO.getChannelOrderNo());
                if (tradeFlow == null) {
                    throw new BusinessException("backNotify充值流水不存在！");
                }
                BusiTradeFlow temp = new BusiTradeFlow();
                temp.setId(tradeFlow.getId());
                temp.setStatus(AppConstants.TradeStatusContants.PROCESS_FAIL);
                busiTradeFlowMapper.updateByPrimaryKeySelective(temp);
            }
            reqLog.setRspTime(new Date().getTime() - startTime.getTime());
            reqLog.setStatus(AppConstants.REQ_LOG.REQ_STATUS_4);
            reqLog.setRspResult(msg);
            asyncService.saveReqLog(reqLog);
            return "success";
        }
        if (!InterfaceCode.codeMap.containsKey(interfaceCode)) {
            log.error("收银台处理失败，接口号:{},失败原因:{}", interfaceCode, "不存在该接口号!");
            return "failure";
        }
        if (StringUtils.isEmpty(data)) {
            log.error("收银台处理失败，接口号:{},失败原因:{}", interfaceCode, "参数为空!");
            return "failure";
        }
        log.info("收银台回调, 接口号:{}, 参数:{}", interfaceCode, data);
        String result = "success";
        try {
            WebApplicationContext web = ContextLoader.getCurrentWebApplicationContext();
            Object serviceBean = web.getBean(ZdPayService.class);
            Method method = null;
            //该方式获取方法，对于重写方法有可能会取错
            Method[] methods = ZdPayService.class.getDeclaredMethods();
            for (Method methodTmp : methods){
               if (methodName.equals(methodTmp.getName())){
                   method = methodTmp;
                   break;
               }
            }
            LocalVariableTableParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();
            String[] paramName = parameterNameDiscoverer.getParameterNames(method);

            Object obj = null;
            if (paramName.length == 1){
                obj = ReflectionUtils.invokeMethod(method, serviceBean, data);
            } else if (paramName.length == 4){
                obj = ReflectionUtils.invokeMethod(method, serviceBean, data, code, msg, interfaceCode);
            }
            reqLog.setRspTime(new Date().getTime() - startTime.getTime());
            reqLog.setStatus(AppConstants.REQ_LOG.REQ_STATUS_3);
            reqLog.setRspResult(Convert.toStr(obj));
        } catch (Exception e) {
            log.info("收银台回调处理异常, 接口号:{},异常信息:{}", interfaceCode, e);
            reqLog.setRspTime(new Date().getTime() - startTime.getTime());
            reqLog.setStatus(AppConstants.REQ_LOG.REQ_STATUS_4);
            reqLog.setRspResult("收银台回调处理异常:" + e);
            MailUtil.sendMail("收银台回调处理异常", "接口号:" + interfaceCode + ",异常信息:" + e);
            result = "failure";
        }
        asyncService.saveReqLog(reqLog);
        return result;
    }
}
