package com.zdmoney.models.payment;

import com.zdmoney.common.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Table(name = "BUSI_ORDER_PAY_PLAN")
public class PaymentPlan extends AbstractEntity<Long> {
    @Id
    private Long id;

    private String no;

    private String subjectNo;

    private String orderNum;

    private BigDecimal principal;

    private BigDecimal interest;

    private BigDecimal principalInterest;

    private Integer term;

    private Integer borrowingDays;

    private BigDecimal dayInterest;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date repayDay;

    private String repayStatus;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date CreateTime;

    private String remark;

    private Integer currTerm;

    @DateTimeFormat( pattern = "yyyy-MM-dd" )
    private Date realTime;

}