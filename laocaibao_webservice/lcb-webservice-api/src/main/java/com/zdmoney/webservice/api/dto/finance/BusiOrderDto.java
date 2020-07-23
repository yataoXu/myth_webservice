package com.zdmoney.webservice.api.dto.finance;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @ Author : Evan.
 * @ Description :
 * @ Date : Crreate in 2018/11/28 14:37
 * @Mail : xuyt@zendaimoney.com
 */
@Data
public class BusiOrderDto implements Serializable {
    private Long id;//定单序列

    private String orderId;//订单号

    private Long customerId;

    private BigDecimal orderAmt;

    private Long productId;

    @DateTimeFormat( pattern = "yyyy-MM-dd HH:mm:ss" )
    private Date orderTime;

    private String status;

    private BigDecimal yearRate;//年化利率

    @DateTimeFormat( pattern = "yyyy-MM-dd" )
    private Date interestStartDate;

    @DateTimeFormat( pattern = "yyyy-MM-dd" )
    private Date interestEndDate;

    private BigDecimal principalinterest;//到期本息和

    private String customerName;

    private String accountSeriNo;

    private String inviteCode;

    @DateTimeFormat( pattern = "yyyy-MM-dd" )
    private  Date  confirmPaymentDate;//确认付款时间

    private String paySerNum;//付款流水

    private Long bankAccountId;//付款银行帐号ID


    private String cmTogatherType;//合作类型

    private String cmOpenMechanism;

    private String cmOpenPlatform;//开户平台 来源

    private String cmOpenChannel;

    private String cmRegisterVersion;

    private Long isFirstOrder;

    private String bankCard;//银行卡号

    private String bankBranchName;//银行卡分行

    private String orderNo; // 积分交易订单号

    private String productNo; // 积分商品编号

    private String productName;

    private String productSerialNo; // 积分商品编号

    private BigDecimal integral; // 积分涉及金额

    private String couponId; // 红包交易订单号

    private BigDecimal coupon; // 红包涉及金额

    private String couponSerial; // 红包流水号

    private BigDecimal paymentCash; //实付金额

    private BigDecimal originalRate;//产品利率

    private BigDecimal inviteRate;//邀请码加息

    private BigDecimal actionRate;//活动加息

    private String payChannel;//支付渠道

    private String voucherId;//加息券编号

    private String voucherSerialNo;//加息券消费流水号

    private String appointmnetSerialNo;//预约券消费流水号

    private Long raiseDays;//加息天数

    private Long closeDays;//封闭天数

    private BigDecimal dayProfit;//日加息利润

    private BigDecimal raiseDayProfilt;//加息天数

    private String productType;

    private boolean useWelfare;

    private BigDecimal addInterestCost;

    private BigDecimal cashAmt;

    private BigDecimal transferCharge;

    private String payType;//回款方式

    private String relAccountNo;//关联账户

    private String subjectStatus;//通知标的状态

    private Integer currTerm;//当前期

    private Integer term;//总期

    private String transferNo; //债权转让编号

    private String transferType; //订单转让类型 0-非转让订单 1-转让订单

    private String cmNumber; //用户编号

    private String memberType;

    private BigDecimal raiseRateIncome;

    private String ruleName;

    private String ruleId;

    /**
     * 提前退出审核状态
     */
    private Integer exitCheckStatus;

    /**
     * 提前退出审核时间
     */
    @DateTimeFormat( pattern = "yyyy-MM-dd HH:mm:ss" )
    private Date exitCheckDate;

    /**
     * 提前退出申请时间
     */
    @DateTimeFormat( pattern = "yyyy-MM-dd HH:mm:ss" )
    private Date exitClickDate;

    /**
     * 产品类型
     */
    private String subjectType;

    private BigDecimal exitFee;//退出手续费

    private BigDecimal exitActualAmt;//实际到手金额

    private BigDecimal exitActualInterest;//实收利息

    private Date exitActualDate;//实际退出时间

    /**
     * 状态描述
     */
    private String statusDescr;

    private String yearRateStr;//年化利率字符串类型

    private String cellPhoneNum;

    private String cardNum;//身份证号

    private int subOrderNum;

    private BigDecimal subOrderSum;//子订单金额合计

    private int failedSubOrderNum;

    private String parentNo;

    private String subjectNo;

}
