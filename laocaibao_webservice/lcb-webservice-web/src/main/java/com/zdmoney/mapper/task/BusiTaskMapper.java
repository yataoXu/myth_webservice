package com.zdmoney.mapper.task;

import com.zdmoney.common.mybatis.mapper.JdMapper;
import com.zdmoney.integral.api.dto.cash.CashDto;
import com.zdmoney.models.task.BusiTask;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface BusiTaskMapper extends JdMapper<BusiTask, Long> {

    BigDecimal getMaxTaskInvestAmt();

    List<BusiTask> selectEnableTask(Date sysDate);

    List<BusiTask> selectValidListByActionType(Map map);

    int countDailyTaskByDate(Map<String,Object> params);

    CashDto getCashDtoByTaskId(Long taskId);

    BusiTask getBusiTaskByRiskTest();
}