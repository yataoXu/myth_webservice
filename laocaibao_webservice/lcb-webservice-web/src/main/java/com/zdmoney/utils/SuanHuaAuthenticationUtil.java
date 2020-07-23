package com.zdmoney.utils;

import com.alibaba.fastjson.JSON;
import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.mapper.customer.CustomerAuthenticationMapper;
import com.zdmoney.models.customer.CustomerAuthentication;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.ContextLoader;
import java.io.IOException;
import java.util.*;

/**
 * 算话--身份认证
 * Created by gaol on 2017/1/13
 **/
@Slf4j
public class SuanHuaAuthenticationUtil {

    private static CustomerAuthenticationMapper customerAuthenticationMapper;

    private static ConfigParamBean configParamBean = ContextLoader.getCurrentWebApplicationContext().getBean(ConfigParamBean.class);

    public static Boolean authentication(String name, String cid) {
        log.info("***********************************算话身份认证开始***********************************");
        log.info("验证姓名:" + name + ",身份证号:" + cid);
        customerAuthenticationMapper = ContextLoader.getCurrentWebApplicationContext().getBean(CustomerAuthenticationMapper.class);

        Map<String, String> params = new TreeMap<>();
        params.put("orgcode", configParamBean.getSh_member_code());
        params.put("name", name);
        params.put("idCard", cid);
        params.put("hash",paramHash(params));
        log.info("请求参数:" + params.toString());
        // 发送认证请求
        String resultData = null;
        try {
            resultData = HttpClientUtil.post(configParamBean.getSh_authUrl(), params);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        log.info("认证结果:" + resultData);
        if(StringUtils.isBlank(resultData)) return false;

        Map<String, Object> dataMap = JSON.parseObject(resultData, Map.class);
        Boolean success = (Boolean) dataMap.get("success");
        if(!success){
            log.info("[算话认证]--验证失败原因:" + dataMap.get("errors").toString());
            return false;
        }
        String result = (String) dataMap.get("result");

        CustomerAuthentication auth=new CustomerAuthentication();
        auth.setCmIdnum(cid);
        auth.setRealName(name);
        auth.setCmInputDate(new Date());
        auth.setPlatform("APP");
        auth.setOperMan("System");
        auth.setChannelId(3);
        if("1".equals(result)){
            auth.setAuStatus(Short.valueOf("0"));
            auth.setAuMsg("算话认证:一致");
        }else if("2".equals(result)){
            auth.setAuStatus(Short.parseShort("1"));
            auth.setAuMsg("算话认证:不一致");
        }else if("3".equals(result)){
            auth.setAuStatus(Short.parseShort("1"));
            auth.setAuMsg("算话认证:无此号码");
        }
        customerAuthenticationMapper.insert(auth);
        log.info("***********************************算话身份认证结束***********************************");
        return success & "1".equals(result) ? true : false;
    }

    /**
     * 消息hash值
     * @param param
     * @return
     */
    private static String paramHash(Map<String, String> param) {
        StringBuilder sb = new StringBuilder();
        Iterator iterator = param.keySet().iterator();
        while (iterator.hasNext()) {
            sb.append(param.get(iterator.next()));
        }
        return MD5Utils.MD5Encode(sb.append(configParamBean.getSh_member_key()).toString()).toUpperCase();
    }

    /**
     * 算话认证
     * @param name
     * @param cid
     * @return Map<String, Object>
     */
    public static Map<String, Object> suanHuaAuth(String name, String cid) {
        log.info("***********************************算话身份认证开始***********************************");
        log.info("验证姓名:" + name + ",身份证号:" + cid);
        Map<String, Object> resMap = new HashMap<>();

        Map<String, String> params = new TreeMap<>();
        params.put("orgcode", configParamBean.getSh_member_code());
        params.put("name", name);
        params.put("idCard", cid);
        params.put("hash",paramHash(params));
        log.info("请求参数:" + params.toString());

        String resultData = null;
        try {
            // 发送认证请求
            resultData = HttpClientUtil.post(configParamBean.getSh_authUrl(), params);
        } catch (IOException e) {
            resMap.put("isSuccess", false);
            resMap.put("msg", "算话接口调用失败:" + e.getMessage());
            e.printStackTrace();
        }
        log.info("算话返回结果:" + resultData);

        Map<String, Object> dataMap = JSON.parseObject(resultData, Map.class);
        Boolean success = (Boolean) dataMap.get("success");
        String result = (String) dataMap.get("result");

        if (success & "1".equals(result)) {
            resMap.put("isSuccess", true);
            resMap.put("msg", "算话认证成功");
        } else if (!success) {
            resMap.put("isSuccess", false);
            resMap.put("msg", dataMap.get("errors").toString());
        } else if ("2".equals(result)) {
            resMap.put("isSuccess", false);
            resMap.put("msg", "算话认证:不一致");
        } else if ("3".equals(result)) {
            resMap.put("isSuccess", false);
            resMap.put("msg", "算话认证:无此号码");
        }
        return resMap;
    }

}
