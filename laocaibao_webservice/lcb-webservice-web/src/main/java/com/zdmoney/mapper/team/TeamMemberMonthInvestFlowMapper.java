package com.zdmoney.mapper.team;

import com.zdmoney.common.mybatis.mapper.JdMapper;
import com.zdmoney.models.team.TeamMemberMonthInvestFlow;

import java.math.BigDecimal;
import java.util.Map;

public interface TeamMemberMonthInvestFlowMapper extends JdMapper<TeamMemberMonthInvestFlow, Long> {

    BigDecimal selectTeamMonthInvestAmt(Map map);
}