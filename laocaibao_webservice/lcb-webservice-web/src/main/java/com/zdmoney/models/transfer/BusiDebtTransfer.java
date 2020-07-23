package com.zdmoney.models.transfer;

import com.zdmoney.common.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "BUSI_DEBT_TRANSFER")
@Getter
@Setter
public class BusiDebtTransfer extends AbstractEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select SEQ_BUSI_DEBT_TRANSFER.nextval from dual")
    private Long id;

    private String transferNo;

    private Long transferId;

    private String initOrderNo;

    private String newOrderNo;

    private Long initProductId;

    private Date pubDate;

    private String transferStatus;

    private BigDecimal transferPrice;

    private BigDecimal transferCharge;

    private Long buyId;

    private Date transferDate;

    private Date realTransferDate;

    private Date tradeDate;

    private Integer leftDay;

    private BigDecimal transferRate;

    private String remark;

    private String isSettle;

    private Long productId;

    private Date updateDate;

    private String originOrderNo;

    private BigDecimal serviceRate;

    private Long originProductId;

    private String outFeeSerial;

    private String inFeeSerial;

    private String transferSerial;


    private String productType; //产品类型 1-普通产品 2-标的产品 3-个贷产品 4-理财计划产品

    private BigDecimal initPrincipal;//上家用户待收本金

    private BigDecimal initInterest;//上家用户待收利息

}