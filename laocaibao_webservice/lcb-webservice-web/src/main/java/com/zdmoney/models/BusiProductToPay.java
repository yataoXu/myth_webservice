package com.zdmoney.models;

import com.zdmoney.common.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "BUSI_PRODUCT_TO_PAY")
@Getter
@Setter
public class BusiProductToPay{

    private Long productId;//产品id

    private String productName;//产品名称

    private Date endDate;//到期日

    private Date yearRate;//利率

    private Long leftDays;//剩余期限

    private Long buyerNum;//购买人数

    private Long authorizedCustomerNum;//授权人数

    private BigDecimal amtToInvest;//授权升级金额

    private String allSettled;//是否已结清

    private Date dateToSettle;//交割日

    private Date authorizationDeadline;//授权截止日

    private Long relevantNewProduct;//对应理财计划















}