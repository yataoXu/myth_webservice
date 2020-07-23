package com.zdmoney.utils;

import com.google.common.base.Charsets;
import com.zdmoney.constant.BusiConstants;
import com.zdmoney.mapper.customer.CustomerMainInfoMapper;
import com.zdmoney.models.customer.CustomerValidateCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import org.springframework.web.context.ContextLoader;
import websvc.req.ReqHeadParam;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 项目名称：laocaibao_webservice
 *
 * @author Sam.J
 * @ClassName: JsonBackUtil
 * @Description: 转换返回msg信息类
 * @date 2015-6-17 下午02:38:10
 */
@Slf4j
public class LaocaiUtil {

    public static String makeUserToken(String key, String cmNumber) throws Exception {
        String user = Des2.encode(key, cmNumber.getBytes());
        return new String(org.apache.commons.codec.binary.Base64.encodeBase64URLSafe(user.getBytes()), Charsets.UTF_8);
    }

    /**
     * @return String 返回类型
     * @throws
     * @Title: buildCustNum
     * @Description: 构建客户编号
     * @author Sam.J
     * @date 2015-6-17 下午02:49:46
     */
    public static String buildCustNum(String comWay) {
        String custNum = "";
        custNum += comWay; // 来源
        String timeStrFormat = new SimpleDateFormat("yyMMdd")
                .format(new Date());
        custNum += timeStrFormat;
        Random rnd = new Random();
        String radNum = 100 + rnd.nextInt(900) + "";// 生成3位随机码拼 以免重复
        CustomerMainInfoMapper customerMainInfoMap = ContextLoader.getCurrentWebApplicationContext().getBean(CustomerMainInfoMapper.class);
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("cmInputDate", timeStrFormat);
        String custTimeNum = customerMainInfoMap.getCustMainCountByParam(param)
                + 1 + "";
        for (int i = custTimeNum.length(); i < 6; i++) {
            custTimeNum = "0" + custTimeNum;
        }// 当天新建的客户数 最大长度为6位 呵呵，呵呵呵,呵呵呵呵
        custNum = custNum + custTimeNum + radNum;
        return custNum;
    }

    /**
     * @return boolean 返回类型
     * @throws
     * @Title: checkValidaterCode
     * @Description: 验证短信验证码是否有效
     * @author Sam.J
     * @date 2015-6-17 上午11:51:35
     */
    public static boolean checkValidaterCode(CustomerValidateCode customerValidateCode) {
        if (customerValidateCode == null)
            return false;
        Date codeEndTime = new Date();
        Date codeStartTime = customerValidateCode.getCvCreateTime();
        return codeEndTime.getTime() - codeStartTime.getTime() <= 10 * 60 * 1000;
    }


    public static String sessionToken2CmNumber(String sessionToken, String tokenKey){
        sessionToken = StringUtils.trim(sessionToken);
        String a = new String(org.apache.commons.codec.binary.Base64.decodeBase64(sessionToken.getBytes(Charsets.UTF_8)), Charsets.UTF_8);
        return StringUtils.substring(Des2.decodeValue(tokenKey, a), 0, 17);
    }

    public static <E> Page<E> convertPage(com.github.pagehelper.Page<E> page){
        Page<E> resultPage = new Page<>();
        setPageProp(page, resultPage);
        resultPage.setResults(page.getResult());
        return resultPage;
    }

    public static <E> Page<E> convertPage(com.github.pagehelper.Page page, List<E> list){
        Page<E> resultPage = new Page<>();
        setPageProp(page, resultPage);
        resultPage.setResults(list);
        return resultPage;
    }

    private static void setPageProp(com.github.pagehelper.Page page, Page resultPage) {
        resultPage.setPageNo(page.getPageNum());
        resultPage.setPageSize(page.getPageSize());
        resultPage.setTotalPage(page.getPages());
        resultPage.setTotalRecord(new Long(page.getTotal()).intValue());
    }

    /*
     * 返回长度为【strLength】的随机数
     */
    public static String getFixLenthString(int strLength) {
        Random rm = new Random();
        // 获得随机数
        double pross = (1 + rm.nextDouble()) * Math.pow(10, strLength);
        // 将获得的获得随机数转化为字符串
        String fixLenthString = String.valueOf(pross);
        // 返回固定的长度的随机数
        return fixLenthString.substring(1, strLength + 1);
    }

    public static String getLoginType(ReqHeadParam reqHeadParam){
        String loginType = BusiConstants.LOGIN_TYPE_APP;
        if(BusiConstants.LOGIN_TYPE_WECHAT.equalsIgnoreCase(reqHeadParam.getPlatform())){
            loginType = BusiConstants.LOGIN_TYPE_WECHAT;
        }else if(BusiConstants.LOGIN_TYPE_WEB.equalsIgnoreCase(reqHeadParam.getPlatform())){
            loginType = BusiConstants.LOGIN_TYPE_WEB;
        }else if(BusiConstants.LOGIN_TYPE_WAP.equalsIgnoreCase(reqHeadParam.getPlatform())){
            loginType = BusiConstants.LOGIN_TYPE_WAP;
        }else if(BusiConstants.LOGIN_TYPE_MANAGE.equalsIgnoreCase(reqHeadParam.getPlatform())){
            loginType = BusiConstants.LOGIN_TYPE_MANAGE;
        }else if(BusiConstants.LOGIN_TYPE_CREDIT.equalsIgnoreCase(reqHeadParam.getPlatform())){
            loginType = BusiConstants.LOGIN_TYPE_CREDIT;
        }
        return loginType;
    }

    public static void main(String[] args) throws Exception {
        //生成sessionToken
        String key = "65S458DF7486SDF5F1S5DF4E";
        String cmNumber = "01180214000011106";
        String user = Des2.encode(key, cmNumber.getBytes());
        String sessionToken =  new String(org.apache.commons.codec.binary.Base64.encodeBase64URLSafe(user.getBytes()), Charsets.UTF_8);
        System.out.println(sessionToken);

        //解码sessionToken
        String a = new String(org.apache.commons.codec.binary.Base64.decodeBase64(sessionToken.getBytes(Charsets.UTF_8)), Charsets.UTF_8);
        String cmNum = StringUtils.substring(Des2.decodeValue(key, a), 0, 17);
        System.out.println(cmNum);

    }

}
