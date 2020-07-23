package com.zdmoney.models.order;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class BusiOrderTemp {
    private Long id;

    private String orderId;

    private Long customerId;

    private BigDecimal orderAmt;

    private Long productId;

    private Date orderTime;

    private String status;

    private BigDecimal yearRate;

    private Date interestStartDate;

    private Date interestEndDate;

    private BigDecimal principalinterest;

    private String customerName;

    private String inviteCode;

    private Date confirmPaymentDate;

    private String paySerNum;

    private Long bankAccountId;

    private String cmOpenMechanism;

    private String cmOpenPlatform;

    private String cmTogatherType;

    private String cmOpenChannel;

    private String cmRegisterVersion;

    private Long isFirstOrder;

    private BigDecimal originalRate;

    private BigDecimal inviteRate;//首次填写邀请码加息

    private BigDecimal actionRate;//活动加息

    private String yearRateStr; //订单利率中文显示

    private BigDecimal dayProfit;   //每日收益

    private BigDecimal raiseDayProfit;  //加息后每日收益

    private Long raiseDays;              //加息天数

    private Long closeDays;              //封闭天数

    private Integer productType;           //产品类型

    private String subjectStatus;         //标的通知状态

    private String payType;          //还款方式

    private String transferType;    //转让类型

    private Date modifyDate; //修改日期

    private BigDecimal raiseRateIncome;//加息收益

    private BigDecimal couponAmount;//红包抵扣金额

    private BigDecimal integralAmount;//积分抵扣金额

    private BigDecimal cashAmount;//现金支付金额

    private String debtorNum;//借款人编号

    private String debtorName;//借款人姓名

    private Integer exitCheckStatus; // 提前退出审核状态 0：默认不提前退出 1：提前退出

    private Date exitCheckDate;//提前退出审核时间

    private Date exitClickDate;//提前退出申请时间（app，pc）

    private BigDecimal exitChargeFee;//提前退出申请手续费

    private String holderType;//持有人类型 0-普通用户 1-特殊理财人

    private String productName;//产品名称

    private Integer reinvestCount;//续投次数


    private String userLabel;//用户标签 0：互联网用户 1：理财师用户

    private String userLevel;//用户层级 0：理财师 1：理财师1级客户 2：理财师2级客户 3:互联网客户 4:互联网员工

    private String memberLevel;//会员等级 1-铁象 3-铜象 5-银象 7-金象 9-白金象 11-钻石象 13-无极象
}