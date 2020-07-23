package com.zdmoney.webservice.api.dto.plan;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * Created by gaol on 2017/6/17
 **/
@Data
public class MatchSucResult implements Serializable {

    /**
     * 资金编号
     */
    private String capitalCode;

    /**
     * 资金金额
     */
    private BigDecimal capitalAmount;

    /**
     * 资金类型
     */
    private String capitalType;

    /**
     * 用户编号
     */
    private String ledgerId;

    /**
     * 为撮合债权ID为数目
     */
    private int financeNum;
    /**
     * 转让 一转多的数目
     */
    private int transferNum;

    /**
     * 优先级
     */
    private String priority;

    /**
     * 撮合债权Id
     */
    private String financeId;

    /**
     * 撮合状态
     */
    private String status;

    /**
     * 期限(天)
     */
    private String totalTerm;

    /**
     * 撮合时间
     */
    private Date createTime;

    /**
     * 预期收益率
     */
    private BigDecimal earningsRate;

    /**
     * 理财计划ID
     */
    private String productCode;

    /**
     * 标的编号
     */
    private String subjectNo;


    /**
     * 标的总额
     */
    private String subjectAmt;

    /**
     * 借款人编号
     */
    private String loanCustomerNo;

    /**
     * 借款人姓名
     */
    private String loanCustomerName;
    /**
     * 债权类型 1-新出借 2-转让
     */
    private String debtType;

    /**
     * 出让订单编号
     */
    private String initOrderNum;

    /**
     * 撮合订单编号
     */
    private String matchOrderCode;

    /**
     * 主债权编号
     */
    private String manFinanceId;

    /**
     * 借款时间
     */
    private Date borrowerDate;

    /**
     * 债权价值
     */
    private BigDecimal debtWorth;

    /**
     * 利息
     */
    private BigDecimal interest;
}
