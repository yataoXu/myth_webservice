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

@Table(name = "TEAM_TASK_COMMON_FLOW")
@Getter
@Setter
public class TeamTaskCommonFlow extends AbstractEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select SEQ_TEAM_TASK_COMMON_FLOW.nextval from dual")
    private Long id;

    private Long teamId;

    private Long taskId;

    private Date finishTime;

    private Long rewardType;

    private Long rewardAmt;

    private Date lastReceiveTime;

    private Long interestId;
}