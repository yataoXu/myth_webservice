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

@Table(name = "CUSTOMER_BORROW_INFO")
@Getter
@Setter
public class CustomerBorrowInfo extends AbstractEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select SEQ_CUSTOMER_BORROW_INFO.nextval from dual")
    private Long id;

    private Long cmId;//用户ID

    private String cmNumber;//用户编号

    private BigDecimal borrowAmt;//借款意向-借款金额

    private String borrowPurpose;//借款意向-借款用途

    private String borrowPeriod;//借款意向-借款期限

    private Long overdueNum;//本平台逾期次数

    private BigDecimal overdueAmt;//本平台逾期金额

    private Long overdueCreditStatus;//截止借款前6个月征信报告中的逾期状况 0-无 1-有

    private Long thdFlag;//其他网贷平台借款情况 0-无 1-有

    private Date createTime;

    private Date modifyTime;

    private String thdInfo;//其他借款平台借款详情



}