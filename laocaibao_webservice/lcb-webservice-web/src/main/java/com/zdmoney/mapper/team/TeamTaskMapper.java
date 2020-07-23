package com.zdmoney.mapper.team;

import com.zdmoney.common.mybatis.mapper.JdMapper;
import com.zdmoney.models.team.TeamTask;

import java.util.Date;
import java.util.List;

public interface TeamTaskMapper extends JdMapper<TeamTask, Long> {

    List<TeamTask> selectEnableTeamTask(Date curDate);

    Long getFulfilNum();
}