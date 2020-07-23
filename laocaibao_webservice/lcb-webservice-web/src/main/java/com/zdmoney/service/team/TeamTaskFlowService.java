package com.zdmoney.service.team;

import com.zdmoney.common.service.BaseService;
import com.zdmoney.mapper.team.TeamTaskFlowMapper;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.team.TeamTaskFlow;
import com.zdmoney.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * Created by wu.hg on 2016/4/6.
 */
@Service
@Slf4j
public class TeamTaskFlowService extends BaseService<TeamTaskFlow, Long> {

    private TeamTaskFlowMapper getTeamTaskFlowMapper() {
        return (TeamTaskFlowMapper) baseMapper;
    }

    /*查询队员已完成的任务*/
    public List<TeamTaskFlow> getMemberTaskFlow(CustomerMainInfo mainInfo) {
        Example example = new Example(TeamTaskFlow.class);
        example.createCriteria().andEqualTo("cmNumber", mainInfo.getCmNumber());
        example.setOrderByClause("id asc");
        return getTeamTaskFlowMapper().selectByExample(example);
    }
}
