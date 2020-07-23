package com.zdmoney.controller;

import com.alibaba.fastjson.JSON;
import com.zdmoney.common.BussErrorCode;
import com.zdmoney.common.Result;
import com.zdmoney.common.listener.InstantiationTracingBeanPostListener;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.exception.HessianRpcException;
import com.zdmoney.message.api.common.ReqType;
import com.zdmoney.message.api.common.ResultStatus;
import com.zdmoney.message.api.dto.log.LogDataReqDto;
import com.zdmoney.models.sys.SysParameter;
import com.zdmoney.models.sys.SysRequestLog;
import com.zdmoney.service.AsyncService;
import com.zdmoney.service.SerialNoGeneratorService;
import com.zdmoney.service.SysParameterService;
import com.zdmoney.session.CustomerSessionService;
import com.zdmoney.trace.common.Constant;
import com.zdmoney.utils.MD5;
import com.zdmoney.web.dto.CustomerDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import websvc.req.ReqHeadParam;
import websvc.req.ReqMain;
import websvc.req.ReqParam;
import websvc.servant.base.FunctionService;
import websvc.utils.JsonException;

import javax.annotation.Resource;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by 00235374 on 2015/11/6.
 * HTTPController方法
 */

@Controller
@RequestMapping("/Api")
@Slf4j
public class HttpSvcController {

    private static final String PKG_NAME_MODEL = "websvc.models";

    @Autowired
    private CustomerSessionService customerSessionService;

    @Resource
    private Set<String> longLogFilterCodes;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private SysParameterService sysParameterService;

    @Autowired
    private AsyncService asyncService;

    private static Validator validator;

    /**
     * 接口接受类
     *
     * @return
     */
    @RequestMapping(value = "/requestDeal")
    @ResponseBody
    public String requestDeal(@RequestParam("arg0") String code, @RequestParam("arg1") String json) {
        SysRequestLog reqLog = new SysRequestLog();
        Date startTime = new Date();
        try {
            log.info("功能号: {} 请求: {}", code, json);
            reqLog.setTransNo(SerialNoGeneratorService.generateTransNoForReq());
            reqLog.setMethodCode(code);
            reqLog.setReqParams(json);
            reqLog.setStatus(AppConstants.REQ_LOG.REQ_STATUS_3);

            Class<?> paramClass = Class.forName(PKG_NAME_MODEL + ".Model_" + code);
            ReqMain reqMain = JSON.parseObject(json, ReqMain.class);
            String reqParamJson = JSON.parseObject(json).getString("reqParam");
            reqMain.setReqParam((ReqParam) JSON.parseObject(reqParamJson, paramClass));
            //校验参数
            Set<ConstraintViolation<ReqMain>> constraintViolations = checkParam(reqMain);
            if (constraintViolations.size() > 0) {
                String errrMsg = "";
                for (ConstraintViolation<ReqMain> constraintViolation : constraintViolations) {
                    errrMsg += constraintViolation.getMessage() + ",";
                }
                reqLog.setRspTime(System.currentTimeMillis() - startTime.getTime());
                reqLog.setStatus(AppConstants.REQ_LOG.REQ_STATUS_4);
                reqLog.setRspResult(errrMsg);
                asyncService.saveReqLog(reqLog);
                log.error("功能号: {} 错误: {}", code, errrMsg.substring(0, errrMsg.length() - 1));
                return JsonException.toJsonStr(BussErrorCode.ERROR_CODE_0103.getErrorcode(), AppConstants.Status.EXCEPTION, "", errrMsg.substring(0, errrMsg.length() - 1), null);
            }

            Object[] objects = InstantiationTracingBeanPostListener.methodCacheMap.get(code);
            FunctionService services = (FunctionService) objects[0];
            Method method = (Method) objects[1];
            Result result;
            try {
                result = (Result) method.invoke(services, reqMain);
            } catch (InvocationTargetException e) {
                Throwable te = e.getTargetException();
                if (te instanceof BusinessException) {
                    BusinessException be = ((BusinessException) te);
                    String message = getMessage(te.getMessage(), be.getArgs());
                    result = Result.fail(message);
                    log.error("调用功能时业务异常: {}", message);
                    log.error("调用接口【{}】时业务异常: {}，参数信息:{}", code ,message, json);
                } else if (te instanceof HessianRpcException) {
                    HessianRpcException he = ((HessianRpcException) te);
                    String model = getMessage(he.getModel(), null);
                    result = Result.fail(he.getMessage());
                    log.error("调用功能时hessian返回失败: 模块: {}, 错误信息: {} ", model, he.getMessage());
                    log.error("调用接口【{}】 hessian模块{} 异常: {}，参数信息:{}", code, model ,he.getMessage(), json);
                } else {
                    result = Result.fail();
                    log.error("调用功能时系统异常: ", te);
                    log.error("调用接口【{}】时系统异常: {}，参数信息:{}", code , te, json);
                }
                reqLog.setRspTime(System.currentTimeMillis() - startTime.getTime());
                reqLog.setStatus(AppConstants.REQ_LOG.REQ_STATUS_4);
                reqLog.setRspResult("调用功能时业务异常:" + e);
            }
            if (result.getMessage().matches("[\\w\\.]+")) {
                result.setMessage(getMessage(result.getMessage(), null));
            }
            String out = JsonException.toJsonStr(result);
            if(longLogFilterCodes.contains(code)){
                log.info("功能号: {} ,正常响应", code);
            }else {
                log.info("功能号: {} 响应: {}", code, out);
            }
            reqLog.setRspTime(System.currentTimeMillis() - startTime.getTime());
            reqLog.setRspResult(out);
            asyncService.saveReqLog(reqLog);

            Date endTime = new Date();
            long process = endTime.getTime() - startTime.getTime();
            LogDataReqDto reqDto = new LogDataReqDto();
            reqDto.setSystemName("LCB_WebService");
            reqDto.setMethodName("功能号:" + code);
            reqDto.setStartTime(startTime);
            reqDto.setEndTime(endTime);
            reqDto.setResParameter(out.length() > 1000 ? out.substring(0, 1000) : out);
            reqDto.setReqParameter(json);
            reqDto.setReqType(ReqType.HTTP);
            reqDto.setResultStatus(ResultStatus.SUCCEED);
            reqDto.setTraceId(MDC.get(Constant.TRACE_ID));
            reqDto.setAccessMsg("处理该请求共耗时:" + process + "毫秒");
            log.info(">>>接口耗时统计<<<【" + reqDto.getMethodName() + "】==耗时：" + process + "毫秒");
            //asyncService.saveAccessLog(reqDto);
            return out;
        } catch (Exception e) {
            log.error("", e);
            return JsonException.toJson(BussErrorCode.ERROR_CODE_0102);
        }
    }

    private String getMessage(String code, Object[] args) {
        if (StringUtils.isEmpty(code)) {
            return "未知";
        }
        return messageSource.getMessage(code, args, null);
    }

    private Set<ConstraintViolation<ReqMain>> checkParam(ReqMain reqMain) {
        if (validator == null) {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            validator = factory.getValidator();
        }
        return validator.validate(reqMain);
    }

    /**
     * @return Boolean    返回类型
     * @throws
     * @Title: check
     * @Description: 验签
     * @author Sam.J
     * @date 2015-6-24 上午09:57:25
     */
    private Boolean check(ReqMain reqMain, String key) {
        String projectNo = reqMain.getProjectNo();//项目代码
        String sn = reqMain.getSn(); //流水号
        String sign = reqMain.getSign();//传送来的验签结果
        String str = projectNo + "|" + sn + "|" + key;//加密key
        String code = MD5.MD5Encode(str);
        log.info("传入验签值  " + sign + " 本地计算验签   " + code);
        return code.equals(sign);
    }

    /*
     * 校验app版本
     */
    private String checkAppVersion(ReqHeadParam headParam) {
        String queryString = "appVersion";
        if("credit".equals(headParam.getSystem())){
            queryString = "appCreditVersion";
        }
        List<SysParameter> params = sysParameterService.findByPrType(queryString);
        String userAgent = headParam.getUserAgent();
        String deviceType = "";
        if (userAgent == null) {
            return "";
        }
        if (userAgent.toUpperCase().indexOf("IOS") > 0) {
            deviceType = "ios";
        } else if (userAgent.toUpperCase().indexOf("ANDROID") > 0) {
            deviceType = "android";
        } else {
            return "";
        }
        for (SysParameter param : params) {
            if (deviceType.equals(param.getPrName())) {
                String prValue = param.getPrValue();
                String prvs[] = prValue.split("\\.");
                String vers[] = headParam.getVersion().split("\\.");
                if (Integer.parseInt(prvs[0]) > Integer.parseInt(vers[0])) {
                    return JsonException.toJson(BussErrorCode.ERROR_CODE_2100);
                } else if (Integer.parseInt(prvs[0]) == Integer.parseInt(vers[0])) {
                    if (Integer.parseInt(prvs[1]) > Integer.parseInt(vers[1])) {
                        return JsonException.toJson(BussErrorCode.ERROR_CODE_2100);
                    } else if (Integer.parseInt(prvs[1]) == Integer.parseInt(vers[1])) {
                        if (Integer.parseInt(prvs[2]) == Integer.parseInt(vers[2])) {
                            return "";
                        } else if (Integer.parseInt(prvs[2]) < Integer.parseInt(vers[2])) {
                            return "";
                        } else {
                            return JsonException.toJson(BussErrorCode.ERROR_CODE_2100);
                        }
                    } else {
                        return "";
                    }
                } else {
                    return "";
                }
            }
        }
        return JsonException.toJson(BussErrorCode.ERROR_CODE_2101);
    }


    /**
     * 检测登录状态
     *
     * @param reqHeadParam
     * @return 0 正常 -1 未登录  -2已在另一设备登录 -3 会话失效
     */
    private int checkSession(ReqHeadParam reqHeadParam, String loginType, ReqParam reqParam) {
        log.info("entry method checkSession begin");
        String sessionToken = reqHeadParam.getSessionToken();
        log.info(">>>>>>>>>>>>>sessionToken:" + sessionToken);
        if (StringUtils.isBlank(sessionToken)) {
            return -1;
        }
        CustomerDTO customer = null;
        try {
            if (customerSessionService.getEdgeInfo(sessionToken) != null) {
                return -2;
            }
            customer = customerSessionService.getCustomer(loginType, sessionToken, true);
            if (customer.getIsSetPwd() == 0) {
                return -3;
            }
            if (reqParam != null && customer != null) {
                Map<String, Object> map = JSON.parseObject(JSON.toJSONString(reqParam));
                String customerId = String.valueOf(map.get("customerId"));
                String cmCustomerId = String.valueOf(map.get("cmCustomerId"));
                String cmCustmonerId = String.valueOf(map.get("cmCustmonerId"));

                if (!"null".equals(customerId) & !customer.getCmCustomerId().equals(customerId)) return -4;
                if (!"null".equals(cmCustomerId) & !customer.getCmCustomerId().equals(cmCustomerId)) return -4;
                if (!"null".equals(cmCustmonerId) & !customer.getCmCustomerId().equals(cmCustmonerId)) return -4;
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("checkSession系统异常:" + e);
            return -3;
        }
        log.info("entry method checkSession end");
        return customer == null ? -3 : 0;
    }

}
