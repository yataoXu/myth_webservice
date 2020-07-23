package com.zdmoney.web.dto.team;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Created by wu.hg on 2016/4/7.
 */
@Getter
@Setter
public class TeamMonthlyTaskDTO extends  TeamTaskCommonDTO{
    /** 累计投资金额*/
    private Long investAmount =0L;

    /** 加息券比列 */
    private BigDecimal rate = new BigDecimal(0);

    private Long coinNum;

    /** 加息天数 */
    private Long days = 0L;

    /** 当前投资金额 */
    private BigDecimal currentAmount = new BigDecimal(0);
}
