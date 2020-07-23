package com.zdmoney.service.team;

import com.google.common.collect.Maps;
import com.zdmoney.common.service.BaseService;
import com.zdmoney.mapper.team.TeamMemberMonthInvestFlowMapper;
import com.zdmoney.models.team.TeamMemberInfo;
import com.zdmoney.models.team.TeamMemberMonthInvestFlow;
import com.zdmoney.utils.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * Created by wu.hg on 2016/4/6.
 */
@Service
@Slf4j
public class TeamMemberMonthInvestFlowService extends BaseService<TeamMemberMonthInvestFlow, Long> {
    @Autowired
    TeamMemberInfoService teamMemberInfoService;

    private TeamMemberMonthInvestFlowMapper getTeamMemberMonthInvestFlowMapper() {
        return (TeamMemberMonthInvestFlowMapper) baseMapper;
    }

    /**
     * 更新队伍对元每月投资
     **/
    @Transactional
    public void updateFlow(long customerId, BigDecimal investAmt) {
        try {
            TeamMemberInfo info = teamMemberInfoService.getTeamMemberInfoByCustomerId(customerId);
            if (info == null) {
                log.info("用户没有队伍信息！customerId=" + customerId);
                return;
            }
            TeamMemberMonthInvestFlow investFlow = new TeamMemberMonthInvestFlow();
            investFlow.setInvestTime(DateTime.now().toString("yyyyMM"));
            investFlow.setMemberId(info.getId());
            investFlow.setTeamId(info.getTeamId());
            TeamMemberMonthInvestFlow teamMemberMonthInvestFlow = baseMapper.selectOne(investFlow);
            if (teamMemberMonthInvestFlow != null) {
                teamMemberMonthInvestFlow.setInvestAmt(investAmt.add(teamMemberMonthInvestFlow.getInvestAmt()));
                this.updateNotNull(teamMemberMonthInvestFlow);
            } else {
                investFlow.setInvestAmt(investAmt);
                investFlow.setReceiveAddInterestcouponNum(0L);
                investFlow.setReceiveLcbAmt(0L);
                this.save(investFlow);
            }
            log.info("更新队员当月投资记录成功!customerId=" + customerId);
        } catch (Exception e) {
            log.error("更新队员当月投资记录失败：" + e.getMessage());
        }
    }

    public BigDecimal getCurMonthlyInvestAmt(long teamId) {
        Map map = Maps.newTreeMap();
        map.put("teamId", teamId);
        map.put("curMonth", DateUtil.getDateFormatString(new Date(), "yyyyMM"));
        return getTeamMemberMonthInvestFlowMapper().selectTeamMonthInvestAmt(map);
    }

    /**
     * 更新捞财币
     **/
    @Transactional
    public void updateLCBAmt(Long customerId,Long teamId, Long lcbAmt) {
        TeamMemberMonthInvestFlow investFlow = new TeamMemberMonthInvestFlow();
        investFlow.setInvestTime(DateUtil.timeFormat(new Date(), "yyyyMM"));
        investFlow.setMemberId(customerId);
        investFlow.setTeamId(teamId);
        TeamMemberMonthInvestFlow teamMemberMonthInvestFlow = baseMapper.selectOne(investFlow);
        if (teamMemberMonthInvestFlow != null) {
            teamMemberMonthInvestFlow.setReceiveLcbAmt(teamMemberMonthInvestFlow.getReceiveLcbAmt()+lcbAmt);
            this.updateNotNull(teamMemberMonthInvestFlow);
        } else {
            investFlow.setInvestAmt(new BigDecimal(0));
            investFlow.setReceiveAddInterestcouponNum(0L);
            investFlow.setReceiveLcbAmt(lcbAmt);
            this.save(investFlow);
        }
    }

    /**
     * 更新加息券数量
     **/
    @Transactional
    public void updateVoucherNum(Long customerId,Long teamId) {
        TeamMemberMonthInvestFlow investFlow = new TeamMemberMonthInvestFlow();
        investFlow.setInvestTime(DateUtil.timeFormat(new Date(),"yyyyMM"));
        investFlow.setMemberId(customerId);
        investFlow.setTeamId(teamId);
        TeamMemberMonthInvestFlow teamMemberMonthInvestFlow = baseMapper.selectOne(investFlow);
        if (teamMemberMonthInvestFlow != null) {
            teamMemberMonthInvestFlow.setReceiveAddInterestcouponNum(teamMemberMonthInvestFlow.getReceiveAddInterestcouponNum()+1);
            this.updateNotNull(teamMemberMonthInvestFlow);
        } else {
            investFlow.setInvestAmt(new BigDecimal(0));
            investFlow.setReceiveAddInterestcouponNum(1L);
            investFlow.setReceiveLcbAmt(0L);
            this.save(investFlow);
        }
    }
}
