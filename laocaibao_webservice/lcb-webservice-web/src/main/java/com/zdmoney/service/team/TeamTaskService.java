package com.zdmoney.service.team;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.zdmoney.common.service.BaseService;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.mapper.team.TeamTaskMapper;
import com.zdmoney.marketing.api.dto.ResultDto;
import com.zdmoney.marketing.api.dto.RewardDto;
import com.zdmoney.marketing.api.facade.IMarketingFacadeService;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.team.*;
import com.zdmoney.service.CustomerMainInfoService;
import com.zdmoney.vo.ResultVo;
import com.zdmoney.web.dto.team.TeamDailyTaskDTO;
import com.zdmoney.web.dto.team.TeamMonthlyTaskDTO;
import com.zdmoney.web.dto.team.TeamNewHandTaskDTO;
import com.zdmoney.web.dto.team.TeamTaskDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by wu.hg on 2016/4/6.
 */
@Service
@Slf4j
public class TeamTaskService extends BaseService<TeamTask, Long> {

    @Autowired
    private CustomerMainInfoService customerMainInfoService;

    @Autowired
    private TeamTaskFlowService teamTaskFlowService;

    @Autowired
    private TeamMemberInfoService teamMemberInfoService;

    @Autowired
    private IMarketingFacadeService marketingFacadeService;

    @Autowired
    private TeamMainInfoService teamMainInfoService;

    @Autowired
    private TeamMemberMonthInvestFlowService teamMemberMonthInvestFlowService;

    @Autowired
    private TeamTaskInterestService teamTaskInterestService;

    @Autowired
    private TeamTaskCommonFlowService teamTaskCommonFlowService;

    private TeamTaskMapper getTeamTaskMapper() {
        return (TeamTaskMapper) baseMapper;
    }


    /**
     * 队员任务列表展示
     */
    public TeamTaskDTO getTaskList(Long customerId) {
        TeamTaskDTO dto = new TeamTaskDTO();
        CustomerMainInfo mainInfo = customerMainInfoService.findOne(customerId);
        if (mainInfo == null) {
            throw new BusinessException("customer.not.exist");
        }
        Map map = new HashMap();
        map.put("memberId", customerId);
        map.put("memberStatus", AppConstants.TeamMemberStatus.TEAM_MEMBER_NORMAL);
        List<TeamMemberInfo> memberList = teamMemberInfoService.findTeamMemberInfo(map);
        if (memberList.isEmpty()) {
            throw new BusinessException("没有有效的队伍信息");
        }
        TeamMemberInfo memberInfo = memberList.get(0);
        TeamMainInfo teamMainInfo = teamMainInfoService.findOne(memberInfo.getTeamId());
        if (teamMainInfo == null) {
            throw new BusinessException("队伍信息不存在");
        }
        if (teamMainInfo.getTeamStatus().shortValue() == AppConstants.TeamStatus.TEAM_STATUS_CLOSE.shortValue()) {
            throw new BusinessException("当前队伍不可用");
        }
        dto.setTeamNum(teamMainInfo.getTeamNum());
        Long fulfilNum = getTeamTaskMapper().getFulfilNum();
        if(fulfilNum !=null){
            if (teamMainInfo.getTeamNum().longValue() >= fulfilNum.longValue()) {
                dto.setIsFulfil(1L);
            }
        }
        if(teamMainInfo.getCaptainId().equals(memberInfo.getMemberId())){
            dto.setIsCaptain(1L);
        }

        //队伍成员的头像编号
        List<Long> headList = teamMemberInfoService.getTeamMemberHeadNum(teamMainInfo.getId());
        dto.setTeamMemberHeadNum(headList);
        //队伍名称
        dto.setTeamName(teamMainInfo.getTeamName());
        //队伍口号
        dto.setTeamSlogan(teamMainInfo.getSlogan());
        //小队编号
        dto.setTeamId(teamMainInfo.getId());

        //查询小队成员列表

        //所有有效的任务
        List<TeamTask> taskList = getTeamTaskMapper().selectEnableTeamTask(new Date());
        //团队已完成任务
        List<TeamTaskCommonFlow> taskCommonFlows = teamTaskCommonFlowService.getTeamTaskFlow(memberInfo.getTeamId());
        // 个人已完成任务
        List<TeamTaskFlow> taskFlows = teamTaskFlowService.getMemberTaskFlow(mainInfo);
        if (!taskList.isEmpty()) {
            List<TeamNewHandTaskDTO> newHandTaskDTO = Lists.newArrayList();
            List<TeamNewHandTaskDTO> newHandTaskDTODone = Lists.newArrayList();
            List<TeamMonthlyTaskDTO> monthlyTaskDTO = Lists.newArrayList();
            List<TeamDailyTaskDTO> dailyTaskDTO = Lists.newArrayList();
            List<TeamTaskFlow> newHandTasks = Lists.newArrayList();
            List<TeamTaskFlow> monthlyTasks = Lists.newArrayList();
            List<TeamTaskFlow> dailyTasks = Lists.newArrayList();
            if (!taskFlows.isEmpty()) {
                taskFlowSort(taskList, taskFlows, newHandTasks, monthlyTasks, dailyTasks);
            }
            for (TeamTask task : taskList) {
                String taskType = task.getTaskType();
                switch (taskType) {
                    case AppConstants.TeamTaskStatus.TASK_TYPE_NEW_HAND://新手任务
                        setNewHandTask(taskFlows, taskCommonFlows, task, newHandTaskDTO, newHandTaskDTODone);
                        continue;
                    case AppConstants.TeamTaskStatus.TASK_TYPE_MONTHLY://每月任务
//                        setMonthlyTask(monthlyTasks, task, monthlyTaskDTO);
                        continue;
                    case AppConstants.TeamTaskStatus.TASK_TYPE_NOT_LIMIT://日常任务（不限次）
                        setDailyTask(dailyTasks, taskCommonFlows, task, dailyTaskDTO);
                }
            }
            TeamMonthlyTaskDTO monthTaskDTO = setMonthTask(memberInfo.getTeamId(), taskCommonFlows, monthlyTasks, taskList);
            dto.setTeamNewHandTask(newHandTaskDTO);
            dto.setTeamNewHandTaskDone(newHandTaskDTODone);
            if (monthTaskDTO != null) {
                monthlyTaskDTO.add(monthTaskDTO);
                dto.setTeamMonthlyTask(monthlyTaskDTO);
            }
            dto.setTeamDailyTask(dailyTaskDTO);
        }
        return dto;
    }

    private void taskFlowSort(List<TeamTask> tasks, List<TeamTaskFlow> taskFlows,
                              List<TeamTaskFlow> newHandTasks,
                              List<TeamTaskFlow> monthlyTasks,
                              List<TeamTaskFlow> dailyTasks
    ) {
        Set<Long> monthlyTaskIds = Sets.newHashSet();
        Set<Long> dailyTaskIds = Sets.newHashSet();
        Set<Long> newHandsTaskIds = Sets.newHashSet();
        for (TeamTask task : tasks) {
            if (AppConstants.TeamTaskStatus.TASK_TYPE_NEW_HAND.equals(task.getTaskType())) {
                newHandsTaskIds.add(task.getId());
            }
            if (AppConstants.TeamTaskStatus.TASK_TYPE_MONTHLY.equals(task.getTaskType())) {
                monthlyTaskIds.add(task.getId());
            }
            if (AppConstants.TeamTaskStatus.TASK_TYPE_NOT_LIMIT.equals(task.getTaskType())) {
                dailyTaskIds.add(task.getId());
            }
        }

        for (TeamTaskFlow taskFlow : taskFlows) {
            if (newHandsTaskIds.contains(taskFlow.getTaskId())) {
                newHandTasks.add(taskFlow);
            }
            if (monthlyTaskIds.contains(taskFlow.getTaskId())) {
                monthlyTasks.add(taskFlow);
            }
            if (dailyTaskIds.contains(taskFlow.getTaskId())) {
                dailyTasks.add(taskFlow);
            }

        }
    }

    /**
     * 处理新手任务
     */
    public void setNewHandTask(List<TeamTaskFlow> newHandTaskFlows, List<TeamTaskCommonFlow> teamTaskCommonFlows, TeamTask task, List<TeamNewHandTaskDTO> newHandTaskDTOs, List<TeamNewHandTaskDTO> newHandTaskDTODones) {
        TeamNewHandTaskDTO newHandTaskDTO = new TeamNewHandTaskDTO();
        newHandTaskDTO.setTaskName(task.getTaskName());
        newHandTaskDTO.setTaskType(task.getTaskType());
        newHandTaskDTO.setMemberNum(task.getTeamNum().toString());
        if(task.getLcbAmt()!=null){
            newHandTaskDTO.setCoinNum(task.getLcbAmt().toString());
        }
        if (task.getRewardType().equals(AppConstants.TeamTaskStatus.TASK_REWARD_VOUCHER)) {
            if (StringUtils.isNotBlank(task.getAddInterestCouponId())) {
                TeamTaskInterest teamTaskInterest = teamTaskInterestService.findOne(Long.valueOf(task.getAddInterestCouponId()));
                if (teamTaskInterest != null) {
                    newHandTaskDTO.setDays(teamTaskInterest.getValidDay());
                    newHandTaskDTO.setRate(teamTaskInterest.getInterestRate());
                }
            }
        }

        newHandTaskDTO.setAwardType(task.getRewardType());
        TeamTaskCommonFlow teamTaskCommonFlow = null;
        List<TeamTaskCommonFlow> tempLists = getCommonFlowList(teamTaskCommonFlows, task);
        if (!CollectionUtils.isEmpty(tempLists)) {
            teamTaskCommonFlow = tempLists.get(0);
            teamTaskCommonFlows.remove(teamTaskCommonFlow);
        }
        //团队任务未完成
        if (teamTaskCommonFlow == null) {
            if (task.getEndTime() == null || task.getEndTime().after(new Date())) {
                newHandTaskDTO.setActionType(AppConstants.TeamTaskStatus.ACTION_STATUS_INVITE);
                newHandTaskDTOs.add(newHandTaskDTO);
            }
        }
        //团队任务已完成
        else {
            if (task.getShowTime() == null || task.getShowTime().after(new Date())) {
                TeamTaskFlow teamTaskFlow = null;
                if (!newHandTaskFlows.isEmpty()) {
                    for (TeamTaskFlow flow : newHandTaskFlows) {
                        if (flow.getTaskCommonFlowId().equals(teamTaskCommonFlow.getId())) {
                            teamTaskFlow = flow;
                            newHandTaskFlows.remove(flow);
                            break;
                        }
                    }
                }
                if (teamTaskFlow == null) {
                    newHandTaskDTO.setActionType(AppConstants.TeamTaskStatus.ACTION_STATUS_RECEIVE);
                    newHandTaskDTO.setFlowId(teamTaskCommonFlow.getId().toString());
                    newHandTaskDTOs.add(newHandTaskDTO);
                } else {
                    if (task.getEndTime() == null || task.getEndTime().after(new Date())) {
                        newHandTaskDTO.setActionType(AppConstants.TeamTaskStatus.ACTION_STATUS_FINISHED);
                        newHandTaskDTODones.add(newHandTaskDTO);
                    }
                }
            }
        }
    }


    private TeamMonthlyTaskDTO setMonthTask(Long teamId, List<TeamTaskCommonFlow> teamTaskCommonFlows, List<TeamTaskFlow> monthTasks, List<TeamTask> taskList) {
        TeamMonthlyTaskDTO monthTaskDTO = null;
        //可用任务
        List<TeamTask> tempTaskList = Lists.newArrayList();
        //已完成的团队流水
        List<TeamTaskCommonFlow> tempCommonFlowList = Lists.newArrayList();
        //已完成的个人流水
        List<TeamTaskFlow> tempFlowList = Lists.newArrayList();
        //初始化任务，团队流水，个人流水
        for (TeamTask task : taskList) {
            if (AppConstants.TeamTaskStatus.TASK_TYPE_MONTHLY.equals(task.getTaskType())) {
                tempTaskList.add(task);
            }
        }
        if (!tempTaskList.isEmpty()) {
            for (TeamTask teamTask : tempTaskList) {
                for (TeamTaskCommonFlow teamTaskCommonFlow : teamTaskCommonFlows) {
                    if (teamTask.getId().equals(teamTaskCommonFlow.getTaskId())) {
                        tempCommonFlowList.add(teamTaskCommonFlow);
                        break;
                    }
                }
            }
            for (TeamTaskCommonFlow teamTaskCommonFlow : teamTaskCommonFlows) {
                for (TeamTaskFlow teamTaskFlow : monthTasks) {
                    if (teamTaskFlow.getTaskCommonFlowId().equals(teamTaskCommonFlow.getId())) {
                        tempFlowList.add(teamTaskFlow);
                        break;
                    }
                }
            }
            BigDecimal curInvestAmount = teamMemberMonthInvestFlowService.getCurMonthlyInvestAmt(teamId);
            Date curDate = new Date();
            ResultVo result = new ResultVo();
            result.setStatus(0);
            for (TeamTask task : tempTaskList) {
                result = checkTask(task, tempCommonFlowList, tempFlowList);
                //任务未领取
                if (result.getStatus() == 1) {
                    monthTaskDTO = assembleTeamMonthlyTaskObject(task, curInvestAmount, AppConstants.TeamTaskStatus.ACTION_STATUS_RECEIVE);
                    monthTaskDTO.setFlowId(result.getFlowId().toString());
                    break;
                }
                //去投资
                if (result.getStatus() == 2) {
                    if (task.getEndTime() != null && curDate.before(task.getEndTime())) {
                        monthTaskDTO = assembleTeamMonthlyTaskObject(task, curInvestAmount, AppConstants.TeamTaskStatus.ACTION_STATUS_INVEST);
                        break;
                    }
                }
            }
            TeamTask lastTask = tempTaskList.get(tempTaskList.size() - 1);
            if (result.getStatus() == 0) {
                if (lastTask.getEndTime() != null && curDate.before(lastTask.getEndTime())) {
                    monthTaskDTO = assembleTeamMonthlyTaskObject(lastTask, curInvestAmount, AppConstants.TeamTaskStatus.ACTION_STATUS_FINISHED);
                }
            }
        }
        return monthTaskDTO;
    }

    /**
     * @return 0-已领取 1-未领取
     */
    private ResultVo checkTeamTaskCommonFlow(TeamTaskCommonFlow teamTaskCommonFlow, List<TeamTaskFlow> teamTaskFlows) {
        ResultVo resultVo = new ResultVo();
        resultVo.setStatus(1);
        resultVo.setFlowId(teamTaskCommonFlow.getId());
        if (teamTaskFlows != null) {
            for (TeamTaskFlow teamTaskFlow : teamTaskFlows) {
                if (teamTaskCommonFlow.getId().equals(teamTaskFlow.getTaskCommonFlowId())) {
                    resultVo.setStatus(0);
                    break;
                }
            }
        }
        return resultVo;
    }

    /**
     * @return 0-任务已领取 1-任务未领取 2- 去投资
     */
    private ResultVo checkTask(TeamTask teamTask, List<TeamTaskCommonFlow> teamTaskCommonFlows, List<TeamTaskFlow> teamTaskFlows) {
        ResultVo resultVo = new ResultVo();
        resultVo.setStatus(2);
        if (teamTaskFlows != null) {
            for (TeamTaskCommonFlow teamTaskCommonFlow : teamTaskCommonFlows) {
                if (teamTaskCommonFlow.getTaskId().equals(teamTask.getId())) {
                    resultVo = checkTeamTaskCommonFlow(teamTaskCommonFlow, teamTaskFlows);
                    break;
                }
            }
        }
        return resultVo;
    }

    /**
     * 封装每月任务对象
     */
    private TeamMonthlyTaskDTO assembleTeamMonthlyTaskObject(TeamTask task, BigDecimal curInvestAmount, String actionType) {
        TeamMonthlyTaskDTO teamMonthlyTaskDTO = new TeamMonthlyTaskDTO();
        if (task != null) {
            teamMonthlyTaskDTO = new TeamMonthlyTaskDTO();
            teamMonthlyTaskDTO.setTaskType(task.getTaskType());
            teamMonthlyTaskDTO.setTaskName(task.getTaskName());
            teamMonthlyTaskDTO.setTaskType(AppConstants.TeamTaskStatus.TASK_TYPE_MONTHLY);
            teamMonthlyTaskDTO.setActionType(actionType);
            teamMonthlyTaskDTO.setCurrentAmount(curInvestAmount);
            teamMonthlyTaskDTO.setInvestAmount(task.getInvestAmt());
            teamMonthlyTaskDTO.setAwardType(task.getRewardType());
            teamMonthlyTaskDTO.setCoinNum(task.getLcbAmt());
            if (task.getRewardType().equals(AppConstants.TeamTaskStatus.TASK_REWARD_VOUCHER)) {
                if (StringUtils.isNotBlank(task.getAddInterestCouponId())) {
                    TeamTaskInterest teamTaskInterest = teamTaskInterestService.findOne(Long.valueOf(task.getAddInterestCouponId()));
                    if (teamTaskInterest != null) {
                        teamMonthlyTaskDTO.setDays(teamTaskInterest.getValidDay());
                        teamMonthlyTaskDTO.setRate(teamTaskInterest.getInterestRate());
                    }
                }
            }
        }
        return teamMonthlyTaskDTO;
    }


    /**
     * 处理日常任务
     */
    public void setDailyTask(List<TeamTaskFlow> dailyTaskFlows, List<TeamTaskCommonFlow> teamTaskCommonFlows, TeamTask task, List<TeamDailyTaskDTO> dailyTaskDTOs) {
        TeamDailyTaskDTO teamDailyTaskDTO = new TeamDailyTaskDTO();
        teamDailyTaskDTO.setTaskType(task.getTaskType());
        teamDailyTaskDTO.setTaskName(task.getTaskName());
        teamDailyTaskDTO.setCoinNum(task.getLcbAmt());
        if(task.getRewardType().equals(AppConstants.TeamTaskStatus.TASK_REWARD_VOUCHER)){
            if (StringUtils.isNotBlank(task.getAddInterestCouponId())) {
                TeamTaskInterest teamTaskInterest = teamTaskInterestService.findOne(Long.valueOf(task.getAddInterestCouponId()));
                if (teamTaskInterest != null) {
                    teamDailyTaskDTO.setDays(teamTaskInterest.getValidDay());
                    teamDailyTaskDTO.setRate(teamTaskInterest.getInterestRate());
                }
            }
        }
        teamDailyTaskDTO.setInvestAmount(task.getInvestAmt());
        teamDailyTaskDTO.setActionType(AppConstants.TeamTaskStatus.ACTION_STATUS_INVEST);
        teamDailyTaskDTO.setAwardType(task.getRewardType());
        List<TeamTaskCommonFlow> lists = getCommonFlowList(teamTaskCommonFlows, task);
        List<TeamTaskCommonFlow> matchTaskCommonFlows = Lists.newArrayList();
        List<TeamTaskFlow> matchTaskFlows = Lists.newArrayList();

        //未完成任务
        if (!CollectionUtils.isEmpty(lists)) {
            //没有个人流水，去领取
            if (CollectionUtils.isEmpty(dailyTaskFlows)) {
                teamDailyTaskDTO.setActionType(AppConstants.TeamTaskStatus.ACTION_STATUS_RECEIVE);
                teamDailyTaskDTO.setFlowId(lists.get(0).getId().toString());
            }
            //有个人完成流水
            else {
                //匹配领取流水
                for (TeamTaskCommonFlow teamTaskCommonFlow : lists) {
                    for (TeamTaskFlow teamTaskFlow : dailyTaskFlows) {
                        if (teamTaskFlow.getTaskCommonFlowId().equals(teamTaskCommonFlow.getId())) {
                            matchTaskCommonFlows.add(teamTaskCommonFlow);
                            matchTaskFlows.add(teamTaskFlow);
                            break;
                        }
                    }
                }
                lists.removeAll(matchTaskCommonFlows);
                dailyTaskFlows.removeAll(matchTaskFlows);
                teamTaskCommonFlows.removeAll(matchTaskCommonFlows);
                //有未领取的团队流水
                if (!CollectionUtils.isEmpty(lists)) {
                    teamDailyTaskDTO.setActionType(AppConstants.TeamTaskStatus.ACTION_STATUS_RECEIVE);
                    teamDailyTaskDTO.setFlowId(lists.get(0).getId().toString());
                }
            }
        }

        if (teamDailyTaskDTO.getActionType().equals(AppConstants.TeamTaskStatus.ACTION_STATUS_INVEST)) {
            if (task.getEndTime() != null && task.getEndTime().before(new Date())) {
                teamDailyTaskDTO = null;
            }
        }
        dailyTaskDTOs.add(teamDailyTaskDTO);
    }


    /**
     * 根据任务，查找团队流水
     */
    private List<TeamTaskCommonFlow> getCommonFlowList(List<TeamTaskCommonFlow> teamTaskCommonFlows, TeamTask task) {
        List<TeamTaskCommonFlow> flows = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(teamTaskCommonFlows)) {
            for (TeamTaskCommonFlow teamTaskCommonFlow : teamTaskCommonFlows) {
                if (task.getId().equals(teamTaskCommonFlow.getTaskId())) {
                    flows.add(teamTaskCommonFlow);
                }
            }
        }
        return flows;
    }

    /**
     * 领取奖励
     */
    public Map<String, String> receiveTaskReward(Long flowId, Long customerId) {
        Map<String, String> result = Maps.newTreeMap();
        CustomerMainInfo mainInfo = customerMainInfoService.findOne(customerId);
        if (mainInfo == null) {
            throw new BusinessException("customer.not.exist");
        }
        TeamMemberInfo queryMember = new TeamMemberInfo();
        queryMember.setMemberId(customerId);
        queryMember.setMemberStatus(AppConstants.TeamMemberStatus.TEAM_MEMBER_NORMAL);
        List<TeamMemberInfo> memberList = teamMemberInfoService.findByEntity(queryMember);
        if (memberList.isEmpty()) {
            throw new BusinessException("没有有效的队伍信息");
        }
        TeamMemberInfo memberInfo = memberList.get(0);
        TeamMainInfo teamMainInfo = teamMainInfoService.findOne(memberInfo.getTeamId());
        if (teamMainInfo == null) {
            throw new BusinessException("队伍信息不存在");
        }
        if (teamMainInfo.getTeamStatus().shortValue() == AppConstants.TeamStatus.TEAM_STATUS_CLOSE.shortValue()) {
            throw new BusinessException("当前队伍不可用");
        }
        ResultDto<RewardDto> resultDto = marketingFacadeService.finishTeamTask(flowId, mainInfo.getCmNumber());
        if (resultDto.isSuccess()) {
            result.put("receiveStatus", "success");
            String name = resultDto.getData().getName();
            long amount = resultDto.getData().getAmount();
            if(resultDto.getData().getRewardType().equals(AppConstants.TeamTaskStatus.TASK_REWARD_COIN)){
                teamMemberMonthInvestFlowService.updateLCBAmt(customerId,teamMainInfo.getId(),amount);
            }
            if(resultDto.getData().getRewardType().equals(AppConstants.TeamTaskStatus.TASK_REWARD_VOUCHER)){
                teamMemberMonthInvestFlowService.updateVoucherNum(customerId,teamMainInfo.getId());
            }
            result.put("name", name);
            result.put("rewardType",resultDto.getData().getRewardType());
            result.put("amount", String.valueOf(amount));

        } else {
            result.put("receiveStatus", "fail");
            result.put("message", resultDto.getMsg());
        }
        return result;
    }


}
