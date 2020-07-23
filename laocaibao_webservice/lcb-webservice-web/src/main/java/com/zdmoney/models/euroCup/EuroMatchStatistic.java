package com.zdmoney.models.euroCup;

import com.zdmoney.common.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "EURO_MATCH_STATISTIC")
@Getter
@Setter
public class EuroMatchStatistic extends AbstractEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select SEQ_MATCH_STATISTIC.nextval from dual")
    private Long id;

    private Long matchId;

    private Long customerId;

    private String guessResult;

    private String isGuessRight;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateDate;

    private String customerMobile;

    private Integer isGrant;
}