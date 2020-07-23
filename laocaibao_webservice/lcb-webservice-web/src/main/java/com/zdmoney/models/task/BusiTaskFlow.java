package com.zdmoney.models.task;

import com.zdmoney.common.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "BUSI_TASK_FLOW")
@Setter
@Getter
public class BusiTaskFlow extends AbstractEntity<Long> {

    @Id
    private Long id;

    private Long taskId;

    private String cmNumber;

    private Long lcbAmt;

    private Date createDate;

    private Short status;

    private Date lastTime;

    /**
     * 领取时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date receiveTime;

    /**
     * 流水号
     */
    private String recordNum;

    private String taskType;

    /**
     * 关联用户编号
     */
    private String relationCmNumber;
}