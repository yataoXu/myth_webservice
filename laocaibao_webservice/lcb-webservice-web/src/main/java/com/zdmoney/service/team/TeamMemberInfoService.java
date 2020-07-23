package com.zdmoney.service.team;

import com.google.common.collect.Lists;
import com.zdmoney.common.service.BaseService;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.mapper.team.TeamMemberInfoMapper;
import com.zdmoney.models.team.TeamMemberInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

/**
 * Created by wu.hg on 2016/4/6.
 */
@Service
@Slf4j
public class TeamMemberInfoService extends BaseService<TeamMemberInfo, Long> {

    private TeamMemberInfoMapper getTeamMemberInfoMapper() {
        return (TeamMemberInfoMapper) baseMapper;
    }

    /**
     * 查询队员所在小组的人数
     * @param memberId
     * @return
     */
    public int getMemberCount(Long memberId){
        TeamMemberInfo queryTeam = new TeamMemberInfo();
        queryTeam.setMemberStatus(AppConstants.TeamMemberStatus.TEAM_MEMBER_NORMAL );
        queryTeam.setMemberId(memberId);
        TeamMemberInfo info = getTeamMemberInfoMapper().selectOne(queryTeam);
        if(info == null){
            throw new BusinessException("无组队信息");
        }
        TeamMemberInfo query = new TeamMemberInfo();
        query.setTeamId(info.getTeamId());
        query.setMemberStatus(AppConstants.TeamMemberStatus.TEAM_MEMBER_NORMAL);
        return getTeamMemberInfoMapper().selectCount(query);
    }

    /**
     * 查询用户所在团队信息
     * @param customerId
     * @return
     */
    public TeamMemberInfo getTeamMemberInfoByCustomerId(Long customerId){
        TeamMemberInfo info = new TeamMemberInfo();
        info.setMemberId(customerId);
        info.setMemberStatus(AppConstants.TeamMemberStatus.TEAM_MEMBER_NORMAL);
        info = baseMapper.selectOne(info);
        return info;
    }

    /**
     * 查询小队的所有成员
     * @param teamId
     * @return
     */
    public List<TeamMemberInfo> getTeamMemberInfoList(Long teamId)
    {
        TeamMemberInfo info = new TeamMemberInfo();
        info.setTeamId(teamId);
        return baseMapper.select(info);
    }

    /**
     * 根据队伍编号获取成员的头像编号
     * @param teamId
     * @return
     */
    public List<Long> getTeamMemberHeadNum(Long teamId)
    {
        List<Long> headList = Lists.newArrayList();
        List<TeamMemberInfo> infoList = getTeamMemberInfoList(teamId);

        for (TeamMemberInfo info : infoList)
        {
            headList.add(info.getMemberId() % 10);
        }
        return headList;
    }

    /**
     * 查询该用户是否已加入小队
     * @param customerId
     * @return
     */
    public boolean checkIsTeamMember(Long customerId){
        //用户是否已加入过队伍
        TeamMemberInfo memberInfo = new TeamMemberInfo();
        memberInfo.setMemberId(customerId);
        memberInfo.setMemberStatus(AppConstants.TeamMemberStatus.TEAM_MEMBER_NORMAL);
        List<TeamMemberInfo> memberInfos = getTeamMemberInfoMapper().select(memberInfo);
        int memberNum = memberInfos.size();
        if(memberNum!=0){
            return true;
        }else{
            return false;
        }
    }

    public List<TeamMemberInfo> findTeamMemberInfo(Map map) {
        return getTeamMemberInfoMapper().findTeamMemberInfo(map);
    }
}
