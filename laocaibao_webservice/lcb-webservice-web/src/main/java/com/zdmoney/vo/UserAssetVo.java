package com.zdmoney.vo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by 00225181 on 2015/12/2.
 */
@Getter
@Setter
public class UserAssetVo {

    /*产品id*/
    private Long productId;

    /*订单id*/
    private Long orderId;

    /*产品名*/
    private String productName;

    /*订单金额*/
    private BigDecimal orderAmt = new BigDecimal(0);

    /*结息日*/
    @JSONField(format = "yyyy-MM-dd")
    private Date interestEndDate;

    /*利息*/
    private BigDecimal interest = new BigDecimal(0);

    /*订单状态*/
    private String status;

    /*年华利率*/
    private BigDecimal bigDecimalRate = new BigDecimal(0);

    /*年化利率Str*/
    private String rate = "0";

    /*本息*/
    private BigDecimal principalInterest = new BigDecimal(0);

    /*下单时间*/
    @JSONField(format = "yyyy-MM-dd")
    private Date orderTime;

    /*起息日*/
    @JSONField(format = "yyyy-MM-dd")
    private Date interestStartDate;

    /*积分金额*/
    private BigDecimal integralAmount = new BigDecimal(0);

    /*红包金额*/
    private BigDecimal redAmount = new BigDecimal(0);

    /*余额支付金额*/
    private BigDecimal cashAmount = new BigDecimal(0);

    /*状态翻译*/
    private String statusDesc;

    /*订单利息*/
    private String yearRateStr;

    private BigDecimal inviteRate = new BigDecimal(0);

    private BigDecimal actionRate = new BigDecimal(0);

    private Integer isAddRate = 0;
    //协议URL
    private String agreementUrl = "";

    private BigDecimal totalAddRate = new BigDecimal(0);

    private String projectDetailUrl;

    /*还款方式*/
    private String repayType;

    /*还款计划*/
    private BigDecimal principal;//本金

    private BigDecimal payInterest;//利息

    private BigDecimal payPrincipalInterest = new BigDecimal(0);//本息和;

    private String payAndTerm;//已回/总期数

    private String type;

    private Integer hasTrans;

    private String  isTransferOrder;//是否可转让

    private String transferStatus;//转让状态 1:可转让 0:不可转让

    private String hasTransferStatus;//订单转让状态判断

    private Integer transferDay;//转让天数

    private int investPeriod; //投资期限

    private int surplusInvestPeriod; //剩余投资期限

    private int totalInvestPeriod;//购买日起的投资期限

    private String  isTransfer;//是否转让产品 0-固收产品 1-转让产品

    private String  orderTransferStatus;//订单转入状态

    private String  orderTransferDesc;//订单转入状态描述

    private BigDecimal transferPrice = new BigDecimal(0);//转让本金

    private BigDecimal transferRate = new BigDecimal(0);//转让利率

    private BigDecimal transferCharge = new BigDecimal(0);//转让服务费（元）

    private BigDecimal transferReceivedAmt  = new BigDecimal(0);//实收本息

    private BigDecimal transferPreReceivedAmt  = new BigDecimal(0);//预计到账金额

    /*转让申请日期*/
    @JSONField(format = "yyyy-MM-dd")
    private Date transferDate;

    /*转让成功日期*/
    @JSONField(format = "yyyy-MM-dd")
    private Date updateDate;

    private String isOneDay;//是否一天之内 0：是  1：不是

    private BigDecimal orderTransferInterest = new BigDecimal(0);//转让代收利息

    /*申请日期*/
    @JSONField(format = "yyyy-MM-dd")
    private Date pubDate;

    private String subjectType;//产品类型 1-优选产品 2-岚枫赛殷 3-个贷产品 4-理财计划产品
    /** 项目本金*/
    private BigDecimal productPrincipal;
    /** 到期利息*/
    private BigDecimal productInterest;

    private Integer closeDay;//封闭期

    private Integer exitCheckStatus; //提前退出审核状态 0：默认不提前退出 1：提前退出

    @JSONField(format = "yyyy-MM-dd")
    private Date exitCheckDate; //提前退出审核时间

    @JSONField(format = "yyyy-MM-dd")
    private Date exitClickDate; //提前退出申请时间（app，pc）

    private int showFinPlan=0;//显示债权信息入口  0：不显示 1：显示

    private int  exitApplyStatus;//提前退出申请状态 0，默认无退出申请 1 ，申请过

    private BigDecimal exitChargeFee = new BigDecimal(0);//退出服务费（元）

    private String creditorRightsUrl = "";//债权信息url

    private BigDecimal exitActualAmt = new BigDecimal(0);//退出实际到手金额（理财计划）

    private BigDecimal exitActualInterest  = new BigDecimal(0);//实收利息（理财计划）

    @JSONField(format = "yyyy-MM-dd")
    private Date exitActualDate ; //理财计划订单结束退出日期

    private String transferType;//0-非转让订单 1-转让订单

    @JSONField(format = "yyyy-MM-dd")
    private Date modifyDate; //订单更新时间

    private String borrowerUrl = "";//借款人信息url

    @JSONField(format = "yyyy-MM-dd")
    private Date earlyExitDate; //提前结清时间
    //续投标识
    private int reinvestCount;


    private String yearRateInit;

    /** 转让原始利率 */
    private String originYearRate;

    /** 转让后年利率 */
    private String yearRate;

}
