package com.zdmoney.webservice.api.dto.plan;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by user on 2019/3/13.
 */
@Data
public class BidOrderInfoDto implements Serializable{

    private String partnerNo;

    private String subjectNo;

    private String customerName;

    private String customerNo;//用户编号

    private String customerIdNo;

    private String phoneNo;

    private String orderNo;

    private BigDecimal orderAmt;

    private BigDecimal interest;

    private Date orderTime;

    private BigDecimal yearRate;

    private Date interestStartDate;

    private Date interestEndDate;

    private Long productId;

    private String productName;

    private OrderType orderType;

    private Long planId;

    private boolean isTransfered = false;//是否转让订单

    private String debtType;

    private String debtNo;

    private String transferNo;//转让编号

    private Date transferDate;//交割日

    private String transferorOrderNo;//上家订单号

    private String transferorDebtNo;//上家债权编号

    private String transferorName;//上家姓名

    private String transferorNo;//上家用户编号

    private String transferorIdNo;//上家身份证号

    private String transferorPhoneNo;//上家手机号

    public enum OrderType{
        FINANCIAL_PLAN("智投宝主单"),FINANCIAL_PLAN_SUB("智投宝子单"),BID("月月宝订单");
        private String descr;

        OrderType(String descr) {
            this.descr = descr;
        }
    }

}
