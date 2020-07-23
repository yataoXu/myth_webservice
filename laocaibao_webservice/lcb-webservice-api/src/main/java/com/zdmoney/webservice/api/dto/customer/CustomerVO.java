package com.zdmoney.webservice.api.dto.customer;

import lombok.Data;

import java.io.Serializable;

@Data
public class CustomerVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 会员姓名
     */
    private String cmRealName = "";

    private String cmNumber;

    /**
     * 会员ID
     */
    private String cmCustomerId = "";

    /**
     * 身份证号码
     */
    private String cmIdnum = "";
    /**
     * 手机号码
     */
    private String cmCellphone = "";

    private String cmStatus = "";


    /**
     * 是否是员工
     * 0-否  1-是
     */
    private String cmEmployee = "";

    /**
     * 邀请码
     */
    private String CmInviteCode = "";

    /**
     * 介绍人码
     */
    private String CmIntroduceCode = "";

    /*
    * 会话令牌
    */
    private String sessionToken = "";

    /*
     * 推送设备号
     */
    private String token = "";

    /*
    * 设备号
    */
    private String deviceId = "";

    /*
     *分享红包url
     */
    private String shareRedUrl = "";

    private String qrCode = "";

    private int integral;

    private String cmNumToken = "";

    /*
     *是否设置交易密码
     */
    private int isSetPwd;//0:否，1:是

    /*
     *是否实名认证
     */
    private int isAuth;//0:否，1:是

    private String cmAccount;

    //商户验证码
    private String validaCode;

    //是否开户
    private int isOpen;//0否，1是

    //是否绑卡
    private int isBindCard;//0否，1是

    //是否存管版本 0否 1是
    private int isDepositVersion;

    private int buyingPermitted;//1:允许购买产品 0：不允许购买

    private String customerType;//客户类型 0:个人 ，1：组织机构

    /**
     * 返回给前端页面的提示语
     */
    private String msg;

    private String pwdTips;

    private String userLevel;
}
