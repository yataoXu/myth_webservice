package com.zdmoney.utils;

import com.alibaba.fastjson.JSON;
import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.mapper.customer.CustomerAuthenticationMapper;
import com.zdmoney.models.customer.CustomerAuthentication;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.ContextLoader;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * 安融--身份认证
 * Created by gaol on 2017/1/12
 **/
public class AnRongAuthenticationUtil {

    private static final Log log = LogFactory.getLog(AnRongAuthenticationUtil.class);

    private static CustomerAuthenticationMapper customerAuthenticationMapper;

    private static ConfigParamBean configParamBean = ContextLoader.getCurrentWebApplicationContext().getBean(ConfigParamBean.class);

    /**
     * 身份认证
     * @param name 姓名
     * @param cid 身份证号
     * @return
     */
    public static Boolean authentication(String name, String cid) {
        log.info("************************************安融-身份认证*****************************************");
        log.info("验证身份证号:" + cid + ",验证人姓名:" + name );
        customerAuthenticationMapper = ContextLoader.getCurrentWebApplicationContext().getBean(CustomerAuthenticationMapper.class);

        String signStr = new StringBuilder(name).append(cid)
                .append(configParamBean.getAr_member())
                .append(configParamBean.getAr_member_pass()).toString();
        String sign = MD5Utils.MD5Encode(signStr).toUpperCase();

        Map<String, String> params = new TreeMap<>();
        params.put("name", name);
        params.put("cid", cid);
        params.put("member", configParamBean.getAr_member());
        params.put("sign",sign);
        params.put("SSL", configParamBean.getAr_ssl());
        params.put("path", configParamBean.getAr_keyStore_path());
        params.put("pwd", configParamBean.getAr_keyStore_pass());

        String data = null;
        try {
            data = HttpClientUtil.get(configParamBean.getAr_authUrl(), params);
            log.info("安融接口返回结果:" + data);
            if(StringUtils.isBlank(data)) return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        Map<String, Object> dataMap = JSON.parseObject(data, Map.class);
        String message = String.valueOf(dataMap.get("message"));
        String result = String.valueOf(dataMap.get("result"));

        CustomerAuthentication auth=new CustomerAuthentication();
        auth.setCmIdnum(cid);
        auth.setRealName(name);
        auth.setCmInputDate(new Date());
        auth.setPlatform("APP");
        auth.setOperMan("System");
        auth.setChannelId(2);
        if("0".equals(result)){
            auth.setAuStatus(Short.valueOf("1"));
            auth.setAuMsg("安融认证:不一致");
        }else if("1".equals(result)){
            auth.setAuStatus(Short.parseShort("0"));
            auth.setAuMsg("安融认证:一致");
        }else if("2".equals(result)){
            auth.setAuStatus(Short.parseShort("1"));
            auth.setAuMsg("安融认证:未知");
        }else{
            auth.setAuStatus(Short.parseShort("1"));
            auth.setAuMsg("安融认证:未知");
        }
        customerAuthenticationMapper.insert(auth);

        if("0001".equals(message)) log.info("认证失败原因:必填参数不能为空");
        else if("0001_wrong".equals(message)) log.info("认证失败原因:必填参数格式错误");
        else if("0002".equals(message)) log.info("认证失败原因:授权失败");
        else if("0003_sfrz".equals(message)) log.info("认证失败原因:身份认证查询接口欠费");
        else if("0004".equals(message)) log.info("认证失败原因:系统错误");
        else if("0006".equals(message)) log.info("认证失败原因:未查到相关数据，此笔查询不扣费");
        else if("0008".equals(message)) log.info("认证失败原因:身份证号格式错误");

        if("0".equals(result)) log.info("认证失败原因:身份证号和姓名不一致");
        if("2".equals(result)) log.info("认证失败原因:未知");
        log.info("************************************安融-身份认证*****************************************");
        return ("0000".equals(message) & "1".equals(result)) ? true : false;
    }

    /**
     * 身份认证
     * @param name 姓名
     * @param cid 身份证号
     * @return Map<String, Object>
     */
    public static Map<String, Object> anRongAuth(String name, String cid) {
        log.info("************************************安融身份认证*****************************************");
        log.info("验证身份证号:" + cid + ",验证人姓名:" + name );
        Map<String, Object> resMap = new HashMap<>();
        String msg = "";

        String signStr = new StringBuilder(name).append(cid)
                .append(configParamBean.getAr_member())
                .append(configParamBean.getAr_member_pass()).toString();
        String sign = MD5Utils.MD5Encode(signStr).toUpperCase();

        Map<String, String> params = new TreeMap<>();
        params.put("name", name);
        params.put("cid", cid);
        params.put("member", configParamBean.getAr_member());
        params.put("sign",sign);
        params.put("SSL", configParamBean.getAr_ssl());
        params.put("path", configParamBean.getAr_keyStore_path());
        params.put("pass", configParamBean.getAr_keyStore_pass());

        String data = null;
        try {
            data = HttpClientUtil.get(configParamBean.getAr_authUrl(), params);
            log.info("安融返回结果:" + data);
        } catch (IOException e) {
            resMap.put("isSuccess", false);
            resMap.put("msg", "安融接口调用失败:" + e.getMessage());
            e.printStackTrace();
        }

        Map<String, Object> dataMap = JSON.parseObject(data, Map.class);
        String message = String.valueOf(dataMap.get("message"));
        String result = String.valueOf(dataMap.get("result"));

        if("0001".equals(message)) msg = "安融认证结果:必填参数不能为空";
        else if("0001_wrong".equals(message)) msg = "安融认证结果:必填参数格式错误";
        else if("0002".equals(message)) msg = "安融认证结果:授权失败";
        else if("0003_sfrz".equals(message)) msg = "安融认证结果:身份认证查询接口欠费";
        else if("0004".equals(message)) msg = "安融认证结果:系统错误";
        else if("0006".equals(message)) msg = "安融认证结果:未查到相关数据，此笔查询不扣费";
        else if("0008".equals(message)) msg = "安融认证结果:身份证号格式错误";
        else if("0000".equals(message)) msg = "安融认证结果:提交成功";

        if("0".equals(result)) msg += ",身份证号和姓名不一致";
        if("2".equals(result)) msg += ",未知";
        if("1".equals(result)) msg += ",认证成功";

        if ("0000".equals(message) & "1".equals(result)) {
            resMap.put("isSuccess", true);
            resMap.put("msg", msg);
        } else{
            resMap.put("isSuccess", false);
            resMap.put("msg", msg);
        }
        log.info("************************************安融身份认证*****************************************");
        return resMap;
    }

}
