package com.zdmoney.models.team;

import com.alibaba.fastjson.annotation.JSONField;
import com.zdmoney.common.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "TEAM_TASK")
@Setter
@Getter
public class TeamTask extends AbstractEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select SEQ_TEAM_TASK.nextval from dual")
    private Long id;

    private String taskName;

    private String taskDesc;

    private Long investTerm;

    private Long investAmt;

    private Long teamNum;

    private String taskType;

    private String investType;

    private String rewardType;

    private String addInterestCouponId;

    private Long lcbAmt;

    private Short taskStatus;

    private Short showStatus;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;

    private Short investTaskType;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private Date showTime;

}