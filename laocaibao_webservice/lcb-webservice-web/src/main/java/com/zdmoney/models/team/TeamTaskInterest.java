package com.zdmoney.models.team;

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

@Table(name = "TEAM_TASK_INTEREST")
@Getter
@Setter
public class TeamTaskInterest extends AbstractEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select SEQ_TEAM_TASK_INTEREST.nextval from dual")
    private Long id;

    private String interestName;

    private BigDecimal interestRate;

    private Long interestDay;

    private Long validDay;

    private Long investPeriod;

    private BigDecimal investLowerAmt;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}