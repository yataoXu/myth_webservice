package com.zdmoney.webservice.api.dto.finance;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @ Author : Evan.
 * @ Description :
 * @ Date : Crreate in 2018/11/28 14:25
 * @Mail : xuyt@zendaimoney.com
 */
@Data
public class BusiOrderSumDto implements Serializable {

    private String productName;

    private BigDecimal totalOrderAmt;

    private BigDecimal totalPriInterest;

    private BigDecimal totalPaymentCash;

    private BigDecimal totalCustInterest;

    private BigDecimal totalComProfit;

    private BigDecimal totalComNProfit;

    private BigDecimal raiseRateIncome;

    private BigDecimal transferAmt;//转让金额

    private BigDecimal productAmt1;//优选产品金额

    private BigDecimal productAmt2;//岚枫赛殷金额

    private BigDecimal productAmt3;//个贷产品金额

    private String productType;//产品分类：   100-转让产品

    private BigDecimal totalFee;

    private BigDecimal totalComPriInterest;

    private BigDecimal totalIntegral;

    private BigDecimal totalCoupon;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date interestStartDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date interestEndDate;

    private String payDate;

    private String orderDate;

    private Long totalCount;

    private String closeDays;

    private BigDecimal bvStat;//BV

}
