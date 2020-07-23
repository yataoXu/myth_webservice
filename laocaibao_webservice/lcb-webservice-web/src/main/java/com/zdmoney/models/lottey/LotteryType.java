package com.zdmoney.models.lottey;

import com.zdmoney.common.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "T_LOT_LOTTERY_TYPE")
@Setter
@Getter
public class LotteryType extends AbstractEntity<Long> {

    @Id
    private Long id;

    private String typeNo;

    private String name;

    private String imgUrl;

    private String visitUrl;

    private Short status;

    private String createBy;

    private String modifyBy;

    private Date createDate;

    private Date modifyDate;

    private Date endTime;

    private String activityType;

    private Date startTime;

    private Short hotStatus;

}