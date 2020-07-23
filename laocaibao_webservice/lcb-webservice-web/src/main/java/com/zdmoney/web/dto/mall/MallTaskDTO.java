package com.zdmoney.web.dto.mall;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by 00225181 on 2016/2/26.
 */
@Getter
@Setter
public class MallTaskDTO {
    Long coinBalance=0L;
    List<HandNewTaskDTO> handNewTasksDone = Lists.newArrayList();//已完成新手任务
    List<LimitTimeTaskDTO> limitTimeTasksDone = Lists.newArrayList();//已未完成限时任务
    List<DailyTaskDTO> dailyTasksDone = Lists.newArrayList();//已未完成每日任务

    List<HandNewTaskDTO> handNewTasks = Lists.newArrayList();//未完成新手任务
    List<LimitTimeTaskDTO> limitTimeTasks = Lists.newArrayList();//未完成限时任务
    MonthTaskDTO monthTask;//未完成每月任务
    List<DailyTaskDTO> dailyTasks = Lists.newArrayList();//未完成每日任务
    List<DailyTaskDTO> investTasks = Lists.newArrayList();//未完成每日任务
    RiskTestDTO riskTestDTO;// 风险测评
    Integer doneNum;
    Integer totalNum;
    List<MonthTaskListDTO> monthTasks = Lists.newArrayList();//多捞多得任务列表
    String touchAppUrl;
}
