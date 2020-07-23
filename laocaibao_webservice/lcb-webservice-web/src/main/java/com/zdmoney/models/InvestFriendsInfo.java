package com.zdmoney.models;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by user on 2016/12/20.
 */
@Data
public class InvestFriendsInfo {

    /**
     * 投资人ID
     */
    private Long investId;

    /**
     * 被邀请好友手机号
     */
    private String invitedCellPhone;

    /**
     * 首次投资日期
     */
    private String firstInvestDate;

    /**
     * 投资总金额
     */
    private BigDecimal amount;

    /**
     * 投资人姓名
     */
    private String investName;

}
