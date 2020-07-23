package com.zdmoney.mapper.gameCenter;

import com.zdmoney.common.mybatis.mapper.JdMapper;
import com.zdmoney.models.gameCenter.DailyTaskRecord;

import java.util.Map;

public interface DailyTaskRecordMapper extends JdMapper<DailyTaskRecord, Long> {

    int countSameMonthSigns(Map<String, Object> params);

    int saveDailyTask(DailyTaskRecord dailyTaskRecord);
}