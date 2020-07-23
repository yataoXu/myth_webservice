package com.zdmoney.web.dto.team;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Created by wu.hg on 2016/4/7.
 */
@Getter
@Setter
public class TeamDailyTaskDTO extends TeamTaskCommonDTO {

    private Long coinNum;

    private Long investAmount;

    /** 加息券比列 */
    private BigDecimal rate = new BigDecimal(0);

    /** 加息天数 */
    private Long days = 0L;






}
