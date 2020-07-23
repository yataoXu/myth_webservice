package com.zdmoney.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class TransferRateDTO {
    /*转让产品利率*/
    private BigDecimal rate;

    /*提示信息*/
    private String tips;

    /*提示信息*/
    private String type;

    /*转让手续费*/
    private BigDecimal transferFee;

    /*转让手续费字符串*/
    private String transferFeeRateStr;

    /*预计到账金额*/
    private BigDecimal exceptReceivedAmount;

    /*折扣*/
    private BigDecimal discount;

    //折让比例
    private String discountRate;

    /**
     * 转让后年利率
     */
    private String transferRate;

}
