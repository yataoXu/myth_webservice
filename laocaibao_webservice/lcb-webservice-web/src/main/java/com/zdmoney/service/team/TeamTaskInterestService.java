package com.zdmoney.service.team;

import com.zdmoney.common.service.BaseService;
import com.zdmoney.mapper.team.TeamTaskInterestMapper;
import com.zdmoney.models.team.TeamTaskInterest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by wu.hg on 2016/4/6.
 */
@Service
@Slf4j
public class TeamTaskInterestService extends BaseService<TeamTaskInterest, Long> {

    private TeamTaskInterestMapper getTeamTaskInterestMapper() {
        return (TeamTaskInterestMapper) baseMapper;
    }


}
