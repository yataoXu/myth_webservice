package com.zdmoney.models.financePlan;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by gaol on 2017/6/6
 **/
@Data
public class BusiDebtDetail implements Serializable {

    private Long id;

    /**
     * 债权编号
     */
    private String debtNo;

    /**
     * 原标的编号
     */
    private String initSubjectNo;

    /**
     * 理财计划ID
     */
    private Long productId;

    /**
     * 剩余期限
     */
    private Long restDays;

    /**
     * 下一个还款日
     */
    private Date nextPayDate;

    /**
     * 最近发起时间
     */
    private Date launchDate;

    /**
     * 最近撮合时间
     */
    private Date matchDate;

    /**
     * 类型 1-新标的 2-退出转让
     */
    private String debtType;

    /**
     * 状态 1-待撮合 2-暂停撮合 3-已撮合 4-提前结清 5-异常 6-结标
     */
    private String status;

    /**
     * 优先级
     */
    private String priority;

    /**
     * 债权价值
     */
    private BigDecimal debtPrice;

    /**
     * 原始债权利率
     */
    private double initRate;

    /**
     * 原始还款方式
     */
    private String initPay;

    /**
     * 当前持有人
     */
    private String currHolder;

    /**
     * 原持有人
     */
    private String initHolder;

    /**
     * 借款人编号
     */
    private String borrowerNumber;

    /**
     * 借款人姓名
     */
    private String borrowerName;

    /**
     * 借款人姓名
     */
    private Date borrowerDate;

    /**
     * 是否特殊理财人转让
     */
    private String isSpecialTransfer;

    /**
     * 备注
     */
    private String remark;
}
