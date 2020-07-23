package com.zdmoney.web.dto.team;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created by wu.hg on 2016/4/7.
 */
@Data
public class TeamNewHandTaskDTO extends TeamTaskCommonDTO{

    private String memberNum;

    private String coinNum;

    /** 加息券比列 */
    private BigDecimal rate = new BigDecimal(0);

    /** 加息天数 */
    private Long days = 0L;
}
