package com.zdmoney.models.payment;

import com.zdmoney.common.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Table(name = "BUSI_SUBJECT_PAY_PLAN")
public class BusiSubjectPayPlan extends AbstractEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select SEQ_BUSI_SUBJECT_PAY_PLAN.nextval from dual")
    private Long id;

    private String no;

    private String subjectNo;

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
    private Date createTime;

    private String remark;

    private Integer currTerm;

    private Long productId;
}