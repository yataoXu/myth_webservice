package com.zdmoney.mapper.team;

import com.zdmoney.common.mybatis.mapper.JdMapper;
import com.zdmoney.models.team.TeamMemberApplyInfo;
import com.zdmoney.web.dto.team.TeamApplyDTO;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TeamMemberApplyInfoMapper extends JdMapper<TeamMemberApplyInfo, Long> {
    List<TeamApplyDTO> getApplyList(Long teamId);
}