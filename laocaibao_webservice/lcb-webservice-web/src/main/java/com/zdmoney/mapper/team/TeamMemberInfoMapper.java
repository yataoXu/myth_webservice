package com.zdmoney.mapper.team;

import com.zdmoney.common.mybatis.mapper.JdMapper;
import com.zdmoney.models.team.TeamMemberInfo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface TeamMemberInfoMapper  extends JdMapper<TeamMemberInfo, Long> {

    int updateCellphoneByMemberId(TeamMemberInfo record);

    List<TeamMemberInfo> findTeamMemberInfo(Map map);
}