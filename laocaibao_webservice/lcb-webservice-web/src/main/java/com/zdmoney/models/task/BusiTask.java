package com.zdmoney.models.task;

import com.zdmoney.common.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "BUSI_TASK")
@Setter
@Getter
public class BusiTask extends AbstractEntity<Long> {

    @Id
    private Long id;

    private String taskName;

    private String taskDesc;

    private String taskType;

    private String limitType;

    private Long investPeriod;

    private Long periodUpperLimit;

    private BigDecimal investAmt;

    private Long lcAmt;

    private Date taskStartTime;

    private Date taskEndTime;

    private String status;

    private String showStatus;

    private String actionType;

    private String isLoop;

    private String awardType;
}