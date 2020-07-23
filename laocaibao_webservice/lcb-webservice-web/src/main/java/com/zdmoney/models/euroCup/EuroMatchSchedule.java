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

@Table(name = "EURO_MATCH_SCHEDULE")
@Getter
@Setter
public class EuroMatchSchedule extends AbstractEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select SEQ_MATCH_SCHEDULE.nextval from dual")
    private Long id;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date matchDate;

    private String matchType;

    private String homeTeam;

    private String awayTeam;

    private String pinHome;

    private String pinAway;

    private String matchReward;

    private String matchResult;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;

}