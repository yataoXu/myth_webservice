package com.zdmoney.web.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.math.BigInteger;

@Data
public class BankCardDTO {

    /*银行名称*/
    private String bankName;

    /*银行卡号*/
    private String bankCard;

    /*银行编号*/
    private String bankCode;

    /*限额描述*/
    private String limitDesc;

    /*限额*/
    private String limitAmt;

    /*提现描述*/
    private String withdrawDesc;

    /*可用余额*/
    private String accountBalance;

    /*是否支持连连充值 0：是 1：否*/
    private String isLockPay;

    /**
     * 快速提现描述
     */
    private String fastWithdrawDesc;

    /**
     * 提现利率
     */
    private String withdrawRate;

    /**
     * 提现冻结中金额
     */
    private BigDecimal freezeAmount = new BigDecimal(BigInteger.ZERO);

}
