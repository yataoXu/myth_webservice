package com.zdmoney.models.team;

import com.zdmoney.common.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "TEAM_TASK_FLOW")
@Getter
@Setter
public class TeamTaskFlow extends AbstractEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select SEQ_TEAM_TASK_FLOW.nextval from dual")
    private Long id;

    private Long taskId;

    private Long taskCommonFlowId;

    private String cmNumber;

    private Date createTime;

    private String receiveStatus;

    private Date recevieTime;

    private String recevieSeri;
}