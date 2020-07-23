package com.zdmoney.service.team;

import com.zdmoney.common.service.BaseService;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.mapper.team.TeamMainInfoMapper;
import com.zdmoney.models.team.TeamMainInfo;
import com.zdmoney.models.team.TeamMemberInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * Created by wu.hg on 2016/4/6.
 */
@Service
@Slf4j
public class TeamMainInfoService extends BaseService<TeamMainInfo, Long> {

    @Autowired
    TeamMemberInfoService teamMemberInfoService;

    private TeamMainInfoMapper getTeamMainInfoMapper() {
        return (TeamMainInfoMapper) baseMapper;
    }


    @Transactional
    public void updateTeamWealth(Long customerId,BigDecimal investAmt){
        try {
            TeamMemberInfo memberInfo = teamMemberInfoService.getTeamMemberInfoByCustomerId(customerId);
            if (memberInfo != null) {
                Long teamId = memberInfo.getTeamId();
                TeamMainInfo teamMainInfo = this.findOne(teamId);
                if (teamMainInfo != null) {
                    if(AppConstants.TeamStatus.TEAM_STATUS_OPEN.equals(teamMainInfo.getTeamStatus())){
                        teamMainInfo.setWealthValue(teamMainInfo.getWealthValue().add(investAmt));
                        this.updateNotNull(teamMainInfo);
                        log.info("更新队伍财富值成功!teamId="+teamMainInfo.getId());
                    }
                }
            }
        }catch (Exception e){
            log.error("更新队伍财富值失败，customerId="+customerId+"，错误信息："+e.getMessage());
        }
    }


}
