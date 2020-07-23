package com.zdmoney.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Created by 00225181 on 2015/12/2.
 */

@Getter
@Setter
public class AccountOverviewDTO {

    /*总资产*/
    private String totalAmount;

    /*持有资产*/
    private String holdAmount;

    /*可用余额*/
    private String availableAmount;

    /*积分余额*/
    private String integralAmount;

    /*红包余额*/
    private String redPacktAmount;

    /*昨日收益*/
    private String yesterdayIncome;

    /*累计收益*/
    private String totalIncome;

    /**
     * 商城首页URL
     */
    private String mallHomeUrl;

    /**
     * 任务中心URL
     */
    private String taskCenterUrl;

    private String lifeServiceUrl;

    /*
     *组队系统url
     */
    private String teamHomeUrl;

    /**
     * 邀请好友
     */
    private String inviteFriendUrl;

    /**
     * 资讯中心url
     */
    private String consultingUrl;

    /**
     * 用户加息券数量
     */
    private int voucherNum;
    /**
     * 用户预约券数量
     */
    private int bespeakNum;

    /**
     * 0-未绑卡 1-绑卡
     */
    private int bindCard=0;

    /**
     * 用户风险测评类型
     */
    private String riskTestType;

    /**
     * 风险测评URL
     */
    private String riskTestUrl;

    /**
     * 时间状态： 0：上午 1：下午 2：晚上
     */
    private int timeStatus;

    /**
     * 时间提示
     */
    private String timeTips;

    /**
     * 待回款日期
     */
    private String repayDate;

    /**
     * 待回款金额
     */
    private BigDecimal repayAmt;

    /**
     * 是否显示回款日期状态
     * 0:不显示 1：显示
     */
    private String showRepayDateAndAmt="0";


    /*是否显示 0 不现实 1显示*/
    private String display = "0";

    /*图片url*/
    private String imgUrl;
    /*跳转的Url*/
    private String rediUrl;

    /**
     * 用户现金券数量
     */
    private int cashNum;

    /**
     * 会员等级
     */
    private int memberLevel;
}