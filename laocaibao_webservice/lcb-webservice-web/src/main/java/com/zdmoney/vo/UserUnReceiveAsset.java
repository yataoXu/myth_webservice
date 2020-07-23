package com.zdmoney.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Created by wu.hg on 2016/12/27.
 */
@Setter
@Getter
public class UserUnReceiveAsset {

    private BigDecimal unReceivePrinciple;

    private BigDecimal unReceiveInterest;

    //红包积分累计收益
    private BigDecimal totalCouponAndIntegralAmt;

    //已收加息
    private BigDecimal receiveInterest;

    //未收加息
    private BigDecimal noReceiveInterest;
}
