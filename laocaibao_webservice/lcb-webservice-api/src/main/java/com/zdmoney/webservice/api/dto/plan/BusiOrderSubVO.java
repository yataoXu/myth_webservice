package com.zdmoney.webservice.api.dto.plan;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by gaol on 2017/6/6
 **/
@Data
public class BusiOrderSubVO implements Serializable {

    private Long id;

    /**
     * 理财计划订单号
     */
    private String orderNum;

    /**
     * 订单号
     */
    private String orderNumSub;

    /**
     * 债权转让编号
     */
    private String transferNo;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 用户编号
     */
    private String cmNumber;

    /**
     * 用户姓名
     */
    private String userName;

    /**
     * 订单金额
     */
    private BigDecimal orderAmt;

    /**
     * 订单状态
     */
    private String orderStatus;

    /**
     * 订单日期
     */
    private Date orderDate;

    /**
     * 标的编号
     */
    private String subjectNo;

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
     * 债权编号
     */
    private String debtNo;

    /**
     * 用户编号
     */
    private String customerId;
}
