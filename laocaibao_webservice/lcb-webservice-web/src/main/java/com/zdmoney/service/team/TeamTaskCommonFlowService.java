package com.zdmoney.service.team;

import com.zdmoney.common.service.BaseService;
import com.zdmoney.mapper.team.TeamTaskCommonFlowMapper;
import com.zdmoney.models.team.TeamTaskCommonFlow;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * Created by wu.hg on 2016/4/6.
 */
@Service
@Slf4j
public class TeamTaskCommonFlowService extends BaseService<TeamTaskCommonFlow, Long> {

    private TeamTaskCommonFlowMapper getTeamTaskCommonMapper() {
        return (TeamTaskCommonFlowMapper) baseMapper;
    }

    /*查询团队已完成任务*/
    public List<TeamTaskCommonFlow> getTeamTaskFlow(Long teamId) {
        Example example = new Example(TeamTaskCommonFlow.class);
        example.createCriteria().andEqualTo("teamId", teamId);
        example.setOrderByClause("finish_time asc");
        return getTeamTaskCommonMapper().selectByExample(example);
    }
}
