package com.zdmoney.models.order;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;
@Table(name = "BUSI_ORDER_REINVEST_LOG")
@Data
public class BusiOrderReinvestLog {
    /**
     * 主键
     */
    private Long id;

    /**
     * 订单ID
     */
    private BigDecimal orderId;

    /**
     * 客户ID
     */
    @Column(name = "CUSTOMER_ID")
    private BigDecimal customerId;

    /**
     * 原利率
     */
    private BigDecimal sourceRate;

    /**
     * 现利率
     */
    private BigDecimal currentRate;

    /**
     * 原结息日
     */
    private Date sourceEndDate;

    /**
     * 现结息日
     */
    private Date currentEndDate;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date modifyTime;

    /**
     * 续投类型
     */
    private String reinvestType;
    /**
     * 续投利率
     */
    private BigDecimal reinvestRate;
    /**
     * 续投天数
     */
    private long reinvestDays;
    /**
     * 续投本金
     */
    private BigDecimal reinvestAmt;

}