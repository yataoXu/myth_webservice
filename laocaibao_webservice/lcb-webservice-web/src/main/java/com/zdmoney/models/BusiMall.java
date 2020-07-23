package com.zdmoney.models;

import com.zdmoney.common.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "BUSI_MALL")
@Getter
@Setter
public class BusiMall extends AbstractEntity<Long> {

    @Id
    private Long id;

    private String merchandiseName;

    private String merchandiseType;

    private BigDecimal couponAmt;

    private BigDecimal interestRate;

    private Long interestDay;

    private Long validDay;

    private Long investPeriod;

    private BigDecimal investAmt;

    private BigDecimal buyAmt;

    private String amtType;

    private Long merchandiseNum;

    private Long buyNum;

    private String status;

    private String showStatus;

    private Date createDate;

    private String remark;

    private Long periodUpperLimit;

    private BigDecimal amtUpperLimit;

}