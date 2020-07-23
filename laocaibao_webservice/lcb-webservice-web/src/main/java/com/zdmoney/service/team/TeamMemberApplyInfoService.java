package com.zdmoney.service.team;

import com.zdmoney.constant.AppConstants;
import com.zdmoney.enums.BusiTypeEnum;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.mapper.team.TeamMemberApplyInfoMapper;
import com.zdmoney.marketing.entity.JoinMessage;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.team.TeamMainInfo;
import com.zdmoney.models.team.TeamMemberApplyInfo;
import com.zdmoney.models.team.TeamMemberInfo;
import com.zdmoney.secure.utils.ThreeDesUtil;
import com.zdmoney.service.CustomerMainInfoService;
import com.zdmoney.service.base.BaseBusinessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;

/**
 * Created by wu.hg on 2016/4/6.
 */
@Service
@Slf4j
public class TeamMemberApplyInfoService extends BaseBusinessService<TeamMemberApplyInfo, Long> {

    @Autowired
    private TeamMainInfoService teamMainInfoService;

    @Autowired
    private TeamMemberInfoService teamMemberInfoService;

    @Autowired
    private CustomerMainInfoService customerMainInfoService;

    private TeamMemberApplyInfoMapper getTeamMemberApplyInfoMapper() {
        return (TeamMemberApplyInfoMapper) baseMapper;
    }


    /**
     * 队员审核
     *
     * @param captainId 队长ID
     * @param applyId   申请ID
     * @param type      审核类型（0-通过 1-拒绝）
     */
    public void captainAudit(Long captainId, Long applyId, Integer type) {
        TeamMemberInfo info = checkUser(captainId, applyId, type);
        if (info != null) {
            //调用入队MQ
            JoinMessage message = new JoinMessage();
            message.setCmNumber(info.getMemberNumber());
            message.setTeamId(info.getTeamId());
            message.setCustomerId(info.getMemberId());
            sendRocketMqMsg(BusiTypeEnum.JOIN, message);
        }
    }


    @Transactional
    public TeamMemberInfo checkUser(Long captainId, Long applyId, Integer type) {
        TeamMemberApplyInfo query = new TeamMemberApplyInfo();
        query.setId(applyId);
        TeamMemberApplyInfo applyInfo = getTeamMemberApplyInfoMapper().selectOne(query);
        if (applyInfo == null) {
            throw new BusinessException("队伍申请信息不存在");
        }
        if (!applyInfo.getMemberStatus().equals(AppConstants.TeamApplyStatus.TEAM_APPLY_AUDITING)) {
            throw new BusinessException("已存在审核操作，请勿重复发起");
        }
        TeamMainInfo teamMainInfo = teamMainInfoService.findOne(applyInfo.getTeamId());
        teamMainInfo.setCaptainCellphone(ThreeDesUtil.encryptMode(teamMainInfo.getCaptainCellphone()));//加密
        if (teamMainInfo == null) {
            throw new BusinessException("队伍信息不存在");
        }
        if (teamMainInfo.getTeamStatus().shortValue() == AppConstants.TeamStatus.TEAM_STATUS_CLOSE.shortValue()) {
            throw new BusinessException("当前队伍不可用");
        }
        teamMainInfo.getTeamStatus();
        if (teamMainInfo.getCaptainId().longValue() != captainId.longValue()) {
            throw new BusinessException("只有队长有权限审核申请");
        }
        CustomerMainInfo memberInfo = customerMainInfoService.findOne(applyInfo.getMemberId());
        if (memberInfo == null) {
            throw new BusinessException("队员信息不存在");
        }
        TeamMemberInfo info = null;
        //审核拒绝
        if (type == 1) {
            applyInfo.setMemberStatus(AppConstants.TeamApplyStatus.TEAM_APPLY_REJECT);
        }
        //审核通过
        if (type == 0) {
            //验证申请人是否已有队伍
            TeamMemberInfo queryList = new TeamMemberInfo();
            queryList.setMemberId(applyInfo.getMemberId());
            queryList.setMemberStatus(AppConstants.TeamMemberStatus.TEAM_MEMBER_NORMAL);
            List<TeamMemberInfo> list = teamMemberInfoService.findByEntity(queryList);
            if (!CollectionUtils.isEmpty(list)) {
                throw new BusinessException(memberInfo.getCmCellphone() + "已经加入其它队伍");
            }
            //验证本队人数是否超限
            TeamMemberInfo queryTeam = new TeamMemberInfo();
            queryTeam.setMemberStatus(AppConstants.TeamMemberStatus.TEAM_MEMBER_NORMAL);
            queryTeam.setTeamId(teamMainInfo.getId());
            List<TeamMemberInfo> teamList = teamMemberInfoService.findByEntity(queryTeam);
            if (!CollectionUtils.isEmpty(teamList) && teamList.size() >= 10) {
                throw new BusinessException("队伍已满");
            }
            applyInfo.setMemberStatus(AppConstants.TeamApplyStatus.TEAM_APPLY_PASS);
            //加入队员信息
            TeamMemberInfo teamMemberInfo = new TeamMemberInfo();
            teamMemberInfo.setMemberNumber(memberInfo.getCmNumber());
            teamMemberInfo.setTeamId(teamMainInfo.getId());
            teamMemberInfo.setMemberId(applyInfo.getMemberId());
            teamMemberInfo.setMemberStatus(AppConstants.TeamMemberStatus.TEAM_MEMBER_NORMAL);
            teamMemberInfo.setMemberName(applyInfo.getMemberName());
            teamMemberInfo.setMemberCellphone(applyInfo.getMemberCellphone());
            teamMemberInfo.setInviteId(applyInfo.getInviteId());
            teamMemberInfo.setEnqueueTime(new Date());
            teamMemberInfo.setRemark(applyInfo.getRemark());
            info = teamMemberInfoService.save(teamMemberInfo);
            //更新队伍表人员数量
            teamMainInfo.setTeamNum(teamMainInfo.getTeamNum() + 1);
            teamMainInfo.setCaptainCellphone(ThreeDesUtil.decryptMode(teamMainInfo.getCaptainCellphone()));//先解密一下
            teamMainInfoService.update(teamMainInfo);
        }
        int i = getTeamMemberApplyInfoMapper().updateByPrimaryKey(applyInfo);
        if(i<=0){
            throw new BusinessException("审核失败");
        }
        return info;
    }
}
