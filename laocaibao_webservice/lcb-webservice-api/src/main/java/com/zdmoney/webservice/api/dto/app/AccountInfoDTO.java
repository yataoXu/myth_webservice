package com.zdmoney.webservice.api.dto.app;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by 00225181 on 2015/12/2.
 */
@Getter
@Setter
public class AccountInfoDTO  implements Serializable {

    /*总资产*/
    private String totalAmount;

    /*持有资产*/
    private String holdAmount;

    /*可用余额*/
    private String availableAmount;

    /*昨日收益*/
    private String yesterdayIncome;

    /*累计收益*/
    private String totalIncome;

    /*是否显示 0 不显示 1 显示*/
    private String display = "0";

    /*图片url*/
    private String imgUrl;

    /*跳转的Url*/
    private String rediUrl;

    /**
     * 邀请好友
     */
    private String inviteFriendUrl;

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

    /**
     * 会员等级
     */
    private int memberLevel;

    /**
     * 是否显示福利小红点
     */
    private boolean showWelfareDot;

    /**
     * 是否显示消息小红点
     */
    private boolean showMsgDot;

}