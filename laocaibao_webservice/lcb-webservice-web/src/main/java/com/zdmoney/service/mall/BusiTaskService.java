package com.zdmoney.service.mall;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.StrBuilder;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.constant.Constants;
import com.zdmoney.enm.SerialNoType;
import com.zdmoney.gamecenter.api.dto.GainFragmentDto;
import com.zdmoney.gamecenter.api.facade.IFragmentFacadeService;
import com.zdmoney.integral.api.dto.account.IntegralGiveDto;
import com.zdmoney.integral.api.dto.account.IntegralResDto;
import com.zdmoney.integral.api.dto.account.common.IntegralSourceNo;
import com.zdmoney.integral.api.dto.cash.CashDto;
import com.zdmoney.integral.api.dto.cash.enm.CashPublishSource;
import com.zdmoney.integral.api.dto.coin.CoinOprDto;
import com.zdmoney.integral.api.dto.coin.CoinOprResultDto;
import com.zdmoney.integral.api.facade.ICashFacadeService;
import com.zdmoney.integral.api.facade.ICoinFacadeService;
import com.zdmoney.integral.api.facade.IIntegralAccountFacadeService;
import com.zdmoney.mapper.BusiAddupCustomerOrderMapper;
import com.zdmoney.mapper.BusiCashRecordMapper;
import com.zdmoney.mapper.BusiCashTicketConfigMapper;
import com.zdmoney.mapper.task.BusiTaskFlowMapper;
import com.zdmoney.mapper.task.BusiTaskMapper;
import com.zdmoney.marketing.api.dto.ResultDto;
import com.zdmoney.marketing.api.facade.IMarketingFacadeService;
import com.zdmoney.models.BusiAddupCustomerOrder;
import com.zdmoney.models.BusiCashRecord;
import com.zdmoney.models.BusiCashTicketConfig;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.task.BusiTask;
import com.zdmoney.models.task.BusiTaskFlow;
import com.zdmoney.service.*;
import com.zdmoney.service.base.BaseBusinessService;
import com.zdmoney.service.welfare.WelfareService;
import com.zdmoney.utils.DateUtil;
import com.zdmoney.utils.LaocaiUtil;
import com.zdmoney.web.dto.mall.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by 00225181 on 2016/2/26.
 */
@Service
@Slf4j
public class BusiTaskService extends BaseBusinessService<BusiTask, Long> {

    private BusiTaskMapper getBusiTaskMapper() {
        return (BusiTaskMapper) baseMapper;
    }

    @Autowired
    private ConfigParamBean configParamBean;

    @Autowired
    private BusiTaskFlowMapper busiTaskFlowMapper;

    @Autowired
    private BusiAddupCustomerOrderMapper addupCustomerOrderMapper;

    @Autowired
    BusiMallService busiMallService;

    @Autowired
    private BusiRiskAssessService riskAssessService;

    @Autowired
    private BusiTaskFlowService busiTaskFlowService;

    @Autowired
    private IFragmentFacadeService fragmentFacadeService;

    @Autowired
    private WelfareService welfareService;

	@Autowired
	private ICashFacadeService cashFacadeService;

	@Autowired
	private BusiCashTicketConfigMapper cashTicketConfigMapper;

	@Autowired
	private BusiCashRecordMapper cashRecordMapper;

	@Autowired
	private IIntegralAccountFacadeService integralAccountFacadeService;


    public MallTaskDTO  getTask(String cmToken) {
        MallTaskDTO dto = new MallTaskDTO();
        dto.setTouchAppUrl(configParamBean.getTouchAppUrl());
        String cmNumber = LaocaiUtil.sessionToken2CmNumber(cmToken, configParamBean.getUserTokenKey());
        CustomerMainInfo mainInfo = checkExistByCmNumber(cmNumber);
        Example example = new Example(BusiTask.class);
        example.createCriteria().andEqualTo("status", "1").andEqualTo("showStatus", "1");
        example.setOrderByClause("limit_type asc,task_start_time asc");
        List<BusiTask> taskList = getBusiTaskMapper().selectEnableTask(new Date());//查询所有任务
        List<BusiTaskFlow> taskFlows = getUserTaskFlow(mainInfo);//查询用户所有有效任务
        long testCount = riskAssessService.queryRiskTest(mainInfo.getId());

        if (!taskList.isEmpty()) {
            List<HandNewTaskDTO> handNewTaskDTOs = Lists.newArrayList();
            List<HandNewTaskDTO> handNewTaskDTODones = Lists.newArrayList();
            List<LimitTimeTaskDTO> limitTimeTaskDTOs = Lists.newArrayList();
            List<LimitTimeTaskDTO> limitTimeTaskDTODones = Lists.newArrayList();
            List<DailyTaskDTO> dailyTaskDTOs = Lists.newArrayList();
            List<DailyTaskDTO> investDTOs = Lists.newArrayList();
            List<DailyTaskDTO> dailyTaskDTODones = Lists.newArrayList();
            List<BusiTaskFlow> handNewTasks = Lists.newArrayList();
            List<BusiTaskFlow> monthTasks = Lists.newArrayList();
            List<BusiTaskFlow> dailyTasks = Lists.newArrayList();
            List<BusiTaskFlow> limitTimeTasks = Lists.newArrayList();
            int num = 0;
            if (!taskFlows.isEmpty()) {
                taskFlowSort(taskList, taskFlows, handNewTasks, monthTasks, dailyTasks, limitTimeTasks,num);
            }
            for (BusiTask task : taskList) {
                TaskCommonDTO commonDTO;
                String taskType = task.getTaskType();
                switch (taskType) {
                    case AppConstants.MallTaskStatus.TASK_TYPE_1://新手任务
                        commonDTO = new HandNewTaskDTO();
                        setCommonDTO(commonDTO, task);
                        setHandNewTask(commonDTO, handNewTasks, task, mainInfo, handNewTaskDTOs, handNewTaskDTODones);
                        continue;
                    case AppConstants.MallTaskStatus.TASK_TYPE_2://限时任务
                        setLimitTask(limitTimeTasks, task, limitTimeTaskDTOs, limitTimeTaskDTODones);
                        continue;
                    case AppConstants.MallTaskStatus.TASK_TYPE_3://多捞多得
                        continue;
                    case AppConstants.MallTaskStatus.TASK_TYPE_4://普通任务/每日任务/累计任务
                        commonDTO = new DailyTaskDTO();
                        setCommonDTO(commonDTO, task);
                        setDailyTask(commonDTO, dailyTasks, task, mainInfo, dailyTaskDTOs, dailyTaskDTODones);
                        continue;
                    case AppConstants.MallTaskStatus.TASK_TYPE_5://--邀请好友
                        commonDTO = new DailyTaskDTO();
                        setCommonDTO(commonDTO, task);
                        setDailyTask(commonDTO, dailyTasks, task, mainInfo, investDTOs, dailyTaskDTODones);
                        continue;
                    case AppConstants.MallTaskStatus.TASK_TYPE_10:
                        RiskTestDTO testDTO = new RiskTestDTO();
                        testDTO.setIsRiskTest(testCount == 0 ? 0 : 1);
                        testDTO.setLcbAmt(task.getLcAmt());
                        testDTO.setTaskName(task.getTaskName());
                        testDTO.setTaskType(AppConstants.MallTaskStatus.TASK_TYPE_10);
                        testDTO.setRiskTestUrl(configParamBean.getRiskTestUrl());
                        dto.setRiskTestDTO(testDTO);
                        continue;
                }
            }

            //处理多捞多得
            MonthTaskDTO monthTaskDTO = setMonthTask(monthTasks, taskList, mainInfo,dto);
            dto.setMonthTasks(getMonthTask());
            dto.setDailyTasks(dailyTaskDTOs);
            dto.setDailyTasksDone(dailyTaskDTODones);
            dto.setHandNewTasks(handNewTaskDTOs);
            dto.setHandNewTasksDone(handNewTaskDTODones);
            dto.setLimitTimeTasks(limitTimeTaskDTOs);
            dto.setLimitTimeTasksDone(limitTimeTaskDTODones);
            dto.setInvestTasks(investDTOs);
            if (monthTaskDTO != null) {
                dto.setDoneNum(num);
                //dto.setTotalNum(monthTasks.size());
                dto.setMonthTask(monthTaskDTO);
            }
        }
        Long coinBalance = busiMallService.getCoinBalance(mainInfo.getCmNumber());
        dto.setCoinBalance(Math.max(coinBalance, 0L));
        return dto;
    }

    private MonthTaskDTO setMonthTask(List<BusiTaskFlow> monthTasks, List<BusiTask> taskList, CustomerMainInfo main,MallTaskDTO dto) {
        MonthTaskDTO monthTaskDTO = null;
        List<BusiTask> taskSet = Lists.newArrayList();
        if (taskList.isEmpty()) {
            monthTaskDTO = new MonthTaskDTO();
            //没有任务
            return monthTaskDTO;
        } else {
            for (BusiTask task : taskList) {
                if (AppConstants.MallTaskStatus.TASK_TYPE_3.equals(task.getTaskType())) {//取出所有的多捞多得任务
                    taskSet.add(task);
                }
            }
        }
        dto.setTotalNum(taskSet.size());
        BusiAddupCustomerOrder addupCustomerOrder = getAddupCustomerOrder(main.getCmNumber());//查询当月投资总额
        for (BusiTask task : taskSet) {
            for (BusiTaskFlow taskFlow : monthTasks) {
                if (taskFlow.getTaskId().equals(task.getId())) {
                    if (taskFlow.getStatus().equals(AppConstants.MallTaskStatus.TASK_RECEIVE_STATUS)) {
                        if (monthTaskDTO == null) {
                            monthTaskDTO = setMonthTaskObject(task, addupCustomerOrder, AppConstants.MallTaskStatus.ACTION_STATUS_9);
                        }
                        if (!monthTaskDTO.getActionType().equals(AppConstants.MallTaskStatus.ACTION_STATUS_2) && !monthTaskDTO.getActionType().equals(AppConstants.MallTaskStatus.ACTION_STATUS_6)) {//没有需要投资的任务
                            if (monthTaskDTO.getTaskInvestAmt().compareTo(task.getInvestAmt()) == -1) {//去任务金额最大的一个任务，在已完成任务中展示
                                monthTaskDTO = setMonthTaskObject(task, addupCustomerOrder, AppConstants.MallTaskStatus.ACTION_STATUS_9);
                            }
                        }
                    } else {
                        if (task.getTaskEndTime().after(new Date()) && task.getTaskStartTime().before(new Date())) {
                            if (monthTaskDTO == null) {
                                monthTaskDTO = setMonthTaskObject(task, addupCustomerOrder, AppConstants.MallTaskStatus.ACTION_STATUS_6);
                                monthTaskDTO.setFlowId(taskFlow.getId().toString());
                            }
                            if (!monthTaskDTO.getActionType().equals(AppConstants.MallTaskStatus.ACTION_STATUS_2)) {//没有需要投资的任务
                                if (monthTaskDTO.getActionType().equals(AppConstants.MallTaskStatus.ACTION_STATUS_9)) {
                                    monthTaskDTO = setMonthTaskObject(task, addupCustomerOrder, AppConstants.MallTaskStatus.ACTION_STATUS_6);
                                    monthTaskDTO.setFlowId(taskFlow.getId().toString());
                                } else {
                                    if (monthTaskDTO.getTaskInvestAmt().compareTo(task.getInvestAmt()) != -1) {//取任务金额最小的一个任务，给用户领取
                                        monthTaskDTO = setMonthTaskObject(task, addupCustomerOrder, AppConstants.MallTaskStatus.ACTION_STATUS_6);
                                        monthTaskDTO.setFlowId(taskFlow.getId().toString());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (monthTaskDTO == null) {
            for (BusiTask task : taskSet) {
                if (task.getTaskEndTime().after(new Date()) && task.getTaskStartTime().before(new Date())) {
                    if (monthTaskDTO == null) {
                        monthTaskDTO = setMonthTaskObject(task, addupCustomerOrder, AppConstants.MallTaskStatus.ACTION_STATUS_2);
                    }
                    if (monthTaskDTO.getTaskInvestAmt().compareTo(task.getInvestAmt()) == 1) {
                        monthTaskDTO = setMonthTaskObject(task, addupCustomerOrder, AppConstants.MallTaskStatus.ACTION_STATUS_2);
                    }
                }
            }
        } else if (monthTaskDTO.getActionType().equals(AppConstants.MallTaskStatus.ACTION_STATUS_9)) {
            Set<BusiTask> doTasks = Sets.newHashSet();
            for (BusiTask task : taskSet) {
                for (BusiTaskFlow taskFlow : monthTasks) {
                    if (task.getId().equals(taskFlow.getTaskId())) {//开始的任务
                        doTasks.add(task);
                    }
                }
            }
            taskSet.removeAll(doTasks);//未开始做的任务
            BusiTask unDoTask = null;
            for (BusiTask task : taskSet) {
                if (task.getTaskEndTime().after(new Date()) && task.getTaskStartTime().before(new Date())) {
                    //未开始的任务
                    if (unDoTask == null) {
                        unDoTask = task;
                    } else {
                        if (unDoTask.getInvestAmt().compareTo(task.getInvestAmt()) == 1) {
                            unDoTask = task;
                        }
                    }
                }
            }
            if (unDoTask != null) {
                if (unDoTask.getTaskEndTime().after(new Date()) && unDoTask.getTaskStartTime().before(new Date())) {
                    monthTaskDTO = setMonthTaskObject(unDoTask, addupCustomerOrder, AppConstants.MallTaskStatus.ACTION_STATUS_2);
                }
            }
//            for(BusiTask task:taskSet){
//                for(BusiTaskFlow taskFlow : monthTasks){
//                    if(!task.getId().equals(taskFlow.getTaskId())){//未开始的任务
//                        if(monthTaskDTO.getActionType().equals(AppConstants.MallTaskStatus.ACTION_STATUS_9)) {
//                            monthTaskDTO = setMonthTaskObject(task, addupCustomerOrder, AppConstants.MallTaskStatus.ACTION_STATUS_2);
//                        }else if (monthTaskDTO.getTaskInvestAmt().compareTo(task.getInvestAmt()) == 1) {//取任务金额较小的一个任务
//                            monthTaskDTO = setMonthTaskObject(task, addupCustomerOrder, AppConstants.MallTaskStatus.ACTION_STATUS_2);
//                        }
//                    }
//                }
//            }
        }
        return monthTaskDTO;
    }

    public MonthTaskDTO setMonthTaskObject(BusiTask task, BusiAddupCustomerOrder addupCustomerOrder, String actionType) {
        TaskCommonDTO commonDto = new MonthTaskDTO();
        setCommonDTO(commonDto, task);
        MonthTaskDTO monthTaskDTO = (MonthTaskDTO) commonDto;
        monthTaskDTO.setTaskType(AppConstants.MallTaskStatus.TASK_ID_MONTH_INVEST.toString());
        monthTaskDTO.setInvestAmt(addupCustomerOrder == null ? new BigDecimal(0) : addupCustomerOrder.getOrderAmt());
        monthTaskDTO.setTaskInvestAmt(task.getInvestAmt());
        monthTaskDTO.setLcbAmt(task.getLcAmt());
        monthTaskDTO.setActionType(actionType);//已完成
        return monthTaskDTO;
    }

    //处理限时任务
    private void setLimitTask(List<BusiTaskFlow> limitTimeTasks,
                              BusiTask task, List<LimitTimeTaskDTO> limitTimeTaskDTOs, List<LimitTimeTaskDTO> limitTimeTaskDTODones) {
        boolean isDoTask = false;
        for (BusiTaskFlow taskFlow : limitTimeTasks) {
            if (task.getId().equals(taskFlow.getTaskId())) {
                LimitTimeTaskDTO limitTimeTask = setLimitTask(task);
                if (AppConstants.MallTaskStatus.TASK_UNRECEIVE_STATUS.equals(taskFlow.getStatus())) {
                    if (task.getTaskEndTime().after(new Date()) && task.getTaskStartTime().before(new Date())) {
                        limitTimeTask.setActionType(AppConstants.MallTaskStatus.ACTION_STATUS_6);//去领取
                        limitTimeTask.setFlowId(taskFlow.getId().toString());
                        limitTimeTask.setInvestUpperPeriod(task.getPeriodUpperLimit());
                        limitTimeTaskDTOs.add(limitTimeTask);
                    }
                } else if (AppConstants.MallTaskStatus.TASK_RECEIVE_STATUS.equals(taskFlow.getStatus())) {
                    limitTimeTask.setActionType(AppConstants.MallTaskStatus.ACTION_STATUS_9);//已完成
                    limitTimeTask.setInvestUpperPeriod(task.getPeriodUpperLimit());
                    limitTimeTaskDTODones.add(limitTimeTask);
                }
                isDoTask = true;
            }
        }
        if (!isDoTask) {//没有做过当前任务
            if (task.getTaskEndTime().after(new Date()) && task.getTaskStartTime().before(new Date())) {
                LimitTimeTaskDTO limitTimeTaskDTO = setLimitTask(task);
                limitTimeTaskDTO.setActionType(AppConstants.MallTaskStatus.ACTION_STATUS_2);//去投资
                limitTimeTaskDTO.setInvestUpperPeriod(task.getPeriodUpperLimit());
                limitTimeTaskDTOs.add(limitTimeTaskDTO);
            }
        }
    }

    private LimitTimeTaskDTO setLimitTask(BusiTask task) {
        TaskCommonDTO commonDTO = new LimitTimeTaskDTO();
        setCommonDTO(commonDTO, task);
        LimitTimeTaskDTO limitTimeTask = (LimitTimeTaskDTO) commonDTO;
        limitTimeTask.setTaskType(AppConstants.MallTaskStatus.TASK_ID_LIMIT_INVEST.toString());
        limitTimeTask.setTaskStartDate(task.getTaskStartTime());
        limitTimeTask.setTaskEndDate(task.getTaskEndTime());
        limitTimeTask.setInvestAmt(task.getInvestAmt());
        limitTimeTask.setInvestPeriod(task.getInvestPeriod());
        limitTimeTask.setLcbAmt(task.getLcAmt());
        limitTimeTask.setLimitType(StringUtils.isEmpty(task.getLimitType()) ? 1 : Integer.parseInt(task.getLimitType()));
        return limitTimeTask;
    }

    //处理每日任务
    public void setDailyTask(TaskCommonDTO commonDTO, List<BusiTaskFlow> dailyTaskFlows,
                             BusiTask task, CustomerMainInfo mainInfo,
                             List<DailyTaskDTO> dailyTaskDTOs, List<DailyTaskDTO> dailyTaskDTODones) {
        DailyTaskDTO dailyTaskDTO = (DailyTaskDTO) commonDTO;
        dailyTaskDTO.setLcbAmt(task.getLcAmt());

/*        //每日签到
        if (AppConstants.MallTaskStatus.TASK_ID_SIGN.equals(task.getId())) {
            dailyTaskDTO.setTaskType(AppConstants.MallTaskStatus.TASK_ID_SIGN.toString());
            for (BusiTaskFlow taskFlow : dailyTaskFlows) {
                if (AppConstants.MallTaskStatus.TASK_ID_SIGN.equals(taskFlow.getTaskId())) {
                    if (AppConstants.MallTaskStatus.TASK_UNRECEIVE_STATUS.equals(taskFlow.getStatus())) {//未领取
                        dailyTaskDTO.setActionType(AppConstants.MallTaskStatus.ACTION_STATUS_6);
                        dailyTaskDTO.setLcbAmt(task.getLcAmt());
                        dailyTaskDTO.setFlowId(taskFlow.getId().toString());
                        dailyTaskDTOs.add(dailyTaskDTO);
                        return;//先领取未领取的奖励
                    }
                }
            }
            CustomerSign customerSign = new CustomerSign();
            customerSign.setCmNumber(mainInfo.getCmNumber());
            customerSign.setSignDate(new Date());
            boolean signed = customerSignService.isSign(customerSign);
            if (signed) {//已签到
                //查询奖励是否已发放
                if (dailyTaskFlows.isEmpty()) {
                    log.error("用户已签到，但没有奖励流水，日期=" + DateUtil.timeFormat(new Date(), "yyyyMMdd") + "，cmNumber=" + mainInfo.getCmNumber());
                } else {
                    for (BusiTaskFlow taskFlow : dailyTaskFlows) {
                        if (AppConstants.MallTaskStatus.TASK_ID_SIGN.equals(taskFlow.getTaskId())) {
                            if (AppConstants.MallTaskStatus.TASK_RECEIVE_STATUS.equals(taskFlow.getStatus())) {//已领取
                                if (DateUtil.timeFormat(taskFlow.getCreateDate(), "yyyyMMdd").equals(DateUtil.timeFormat(new Date(), "yyyyMMdd"))) {//当日签到任务已领
                                    dailyTaskDTO.setActionType(AppConstants.MallTaskStatus.ACTION_STATUS_9);
                                    dailyTaskDTO.setLcbAmt(task.getLcAmt());
                                    dailyTaskDTODones.add(dailyTaskDTO);
                                    return;
                                }
                            }
                        }
                    }
                    if (dailyTaskDTODones.isEmpty()) {//当日签到奖励没领取
                        dailyTaskDTO.setActionType(AppConstants.MallTaskStatus.ACTION_STATUS_4);
                        dailyTaskDTOs.add(dailyTaskDTO);
                    }
                }
            } else {//未签到
                dailyTaskDTO.setActionType(AppConstants.MallTaskStatus.ACTION_STATUS_4);
                dailyTaskDTOs.add(dailyTaskDTO);
            }

//            if(!flag){
//                log.error("用户已签到，但没有奖励流水，日期="+DateUtil.timeFormat(new Date(),"yyyyMMdd")+"，cmNumber="+mainInfo.getCmNumber());
//            }
//            CustomerSign customerSign = new CustomerSign();
//            customerSign.setCmNumber(mainInfo.getCmNumber());
//            customerSign.setSignDate(new Date());
//            int num = customerSignService.countSign(customerSign);
//            if(num > 0) {//已签到
//                //查询奖励是否已发放
//                if(dailyTaskFlows.isEmpty()){
//                    log.error("用户已签到，但没有奖励流水，日期="+DateUtil.timeFormat(new Date(),"yyyyMMdd")+"，cmNumber="+mainInfo.getCmNumber());
//                }
//            }else{//未签到
//                dailyTaskDTO.setActionType(AppConstants.MallTaskStatus.ACTION_STATUS_4);
//                dailyTaskDTOs.add(dailyTaskDTO);
//            }
        }*/
        //}
        //抓公仔
        /*if (Long.valueOf(catchDollId).equals(task.getId())) {
            dailyTaskDTO.setTaskType("12");
            //查询邀请任务
            if (dailyTaskFlows.isEmpty()) {
                dailyTaskDTO.setActionType(AppConstants.MallTaskStatus.ACTION_STATUS_7);//去玩吧
            } else {
                Long totalUnRecevieLcbAmt = 0l;
                String flowId = "";
                for (BusiTaskFlow taskFlow : dailyTaskFlows) {
                    if (catchDollId.equals(taskFlow.getTaskId())) {
                        if (AppConstants.MallTaskStatus.TASK_UNRECEIVE_STATUS.equals(taskFlow.getStatus())) {
                            totalUnRecevieLcbAmt += taskFlow.getLcbAmt();
                            if (StringUtils.isEmpty(flowId)) {
                                flowId = taskFlow.getId().toString();
                            } else {
                                flowId += "," + taskFlow.getId().toString();
                            }
                        }
                    }
                }
                if (totalUnRecevieLcbAmt > 0l) {
                    dailyTaskDTO.setActionType(AppConstants.MallTaskStatus.ACTION_STATUS_6);//去领取
                    dailyTaskDTO.setUnRecevieLcbAmt(totalUnRecevieLcbAmt);
                    dailyTaskDTO.setFlowId(flowId);
                } else {
                    dailyTaskDTO.setActionType(AppConstants.MallTaskStatus.ACTION_STATUS_7);//去玩吧
                }
            }
            dailyTaskDTOs.add(dailyTaskDTO);
        }*/
        Map<String, Object> params = new HashMap<>();
        params.put("now",new Date());
        //邀请好友
        if (AppConstants.MallTaskStatus.TASK_ID_INVITATION.equals(task.getId())) {
            dailyTaskDTO.setTaskType(AppConstants.MallTaskStatus.TASK_ID_INVITATION.toString());
            //查询邀请任务
            if (dailyTaskFlows.isEmpty()) {
                dailyTaskDTO.setActionType(AppConstants.MallTaskStatus.ACTION_STATUS_5);//去邀请
            } else {
                Long totalUnRecevieLcbAmt = 0l;
                String flowId = "";
                for (BusiTaskFlow taskFlow : dailyTaskFlows) {
                    if (AppConstants.MallTaskStatus.TASK_ID_INVITATION.equals(taskFlow.getTaskId())) {
                        if (AppConstants.MallTaskStatus.TASK_UNRECEIVE_STATUS.equals(taskFlow.getStatus())) {
                            totalUnRecevieLcbAmt += taskFlow.getLcbAmt();
                            if (StringUtils.isEmpty(flowId)) {
                                flowId = taskFlow.getId().toString();
                            } else {
                                flowId += "," + taskFlow.getId().toString();
                            }
                        }
                    }
                }
                if (totalUnRecevieLcbAmt > 0l) {
                    dailyTaskDTO.setActionType(AppConstants.MallTaskStatus.ACTION_STATUS_6);//去领取
                    dailyTaskDTO.setUnRecevieLcbAmt(totalUnRecevieLcbAmt);
                    dailyTaskDTO.setFlowId(flowId);
                } else {
                    dailyTaskDTO.setActionType(AppConstants.MallTaskStatus.ACTION_STATUS_5);//去邀请
                }
            }
            dailyTaskDTOs.add(dailyTaskDTO);
        }else if(getBusiTaskMapper().countDailyTaskByDate(params)>0){
            //刮刮卡
            if (Long.valueOf(configParamBean.getScratchCardId()).equals(task.getId())) {
                dailyTaskDTO.setTaskType("11");
            }else if(Long.valueOf(configParamBean.getCatchDollId()).equals(task.getId())){
                dailyTaskDTO.setTaskType("12");
            }
            Date date = new Date();
            long startOfDay = cn.hutool.core.date.DateUtil.beginOfDay(date).getTime();//当天开始时间
            long endOfDay = cn.hutool.core.date.DateUtil.endOfDay(date).getTime();//当天结束时间
            //查询邀请任务
            if (dailyTaskFlows.isEmpty()) {
                dailyTaskDTO.setActionType(AppConstants.MallTaskStatus.ACTION_STATUS_7);//去玩吧
            } else {
                Long totalUnRecevieLcbAmt = 0l;
                String flowId = "";
                for (BusiTaskFlow taskFlow : dailyTaskFlows) {
                    if (task.getId().equals(taskFlow.getTaskId())) {//过滤当天之外的任务
                        long createTime = taskFlow.getCreateDate().getTime();//创建时间
                        if (AppConstants.MallTaskStatus.TASK_UNRECEIVE_STATUS.equals(taskFlow.getStatus()) && createTime >= startOfDay && createTime <= endOfDay) {
                            totalUnRecevieLcbAmt += taskFlow.getLcbAmt();
                            if (StringUtils.isEmpty(flowId)) {
                                flowId = taskFlow.getId().toString();
                            } else {
                                flowId += "," + taskFlow.getId().toString();
                            }
                        }
                    }
                }
                if (totalUnRecevieLcbAmt > 0l) {
                    dailyTaskDTO.setActionType(AppConstants.MallTaskStatus.ACTION_STATUS_6);//去领取
                    dailyTaskDTO.setUnRecevieLcbAmt(totalUnRecevieLcbAmt);
                    dailyTaskDTO.setFlowId(flowId);
                } else {
                    dailyTaskDTO.setActionType(AppConstants.MallTaskStatus.ACTION_STATUS_7);//去玩吧
                }
            }
            dailyTaskDTOs.add(dailyTaskDTO);
        }
    }

    //处理新手任务
    public void setHandNewTask(TaskCommonDTO commonDTO, List<BusiTaskFlow> handNewTaskFlows,
                               BusiTask task, CustomerMainInfo mainInfo,
                               List<HandNewTaskDTO> handNewTaskDTOs, List<HandNewTaskDTO> handNewTaskDTODones) {
        HandNewTaskDTO handNewTaskDTO = (HandNewTaskDTO) commonDTO;
        handNewTaskDTO.setLcbAmt(task.getLcAmt());
        //新手有礼--实名认证
        if (AppConstants.MallTaskStatus.TASK_ID_AUTH.equals(task.getId())) {
            handNewTaskDTO.setTaskType(AppConstants.MallTaskStatus.TASK_ID_AUTH.toString());
            if (3 == mainInfo.getCmStatus()) {//用户已实名认证
                if (handNewTaskFlows.isEmpty()) {//没有任务流水
                    log.error("用户已实名认证，但没有奖励流水，cmNumber=" + mainInfo.getCmNumber());
                } else {
                    for (BusiTaskFlow taskFlow : handNewTaskFlows) {
                        if (AppConstants.MallTaskStatus.TASK_ID_AUTH.equals(taskFlow.getTaskId())) {
                            if (AppConstants.MallTaskStatus.TASK_UNRECEIVE_STATUS.equals(taskFlow.getStatus())) {//未领取奖励
                                handNewTaskDTO.setActionType(AppConstants.MallTaskStatus.ACTION_STATUS_6);//去领取
                                handNewTaskDTO.setFlowId(taskFlow.getId().toString());
                                handNewTaskDTOs.add(handNewTaskDTO);
                            } else if (AppConstants.MallTaskStatus.TASK_RECEIVE_STATUS.equals(taskFlow.getStatus())) {
                                handNewTaskDTO.setActionType(AppConstants.MallTaskStatus.ACTION_STATUS_9);//已领取
                                handNewTaskDTODones.add(handNewTaskDTO);
                            }
                            handNewTaskFlows.remove(taskFlow);
                            break;
                        }
                    }
                }
            } else {//未实名认证
                handNewTaskDTO.setActionType(AppConstants.MallTaskStatus.ACTION_STATUS_1);//去认证
                handNewTaskDTOs.add(handNewTaskDTO);
            }
        }


        //小试牛刀--首次投资
        if (AppConstants.MallTaskStatus.TASK_ID_FIRST_INVEST.equals(task.getId())) {
            handNewTaskDTO.setTaskType(AppConstants.MallTaskStatus.TASK_ID_FIRST_INVEST.toString());
            if (handNewTaskFlows.isEmpty()) {
                handNewTaskDTO.setActionType(AppConstants.MallTaskStatus.ACTION_STATUS_2);//去投资
                handNewTaskDTOs.add(handNewTaskDTO);
            } else {
                boolean flag = false;
                for (BusiTaskFlow taskFlow : handNewTaskFlows) {
                    if (AppConstants.MallTaskStatus.TASK_ID_FIRST_INVEST.equals(taskFlow.getTaskId())) {
                        if (AppConstants.MallTaskStatus.TASK_RECEIVE_STATUS.equals(taskFlow.getStatus())) {//已完成
                            handNewTaskDTO.setActionType(AppConstants.MallTaskStatus.ACTION_STATUS_9);
                            handNewTaskDTODones.add(handNewTaskDTO);
                        } else if (AppConstants.MallTaskStatus.TASK_UNRECEIVE_STATUS.equals(taskFlow.getStatus())) {//去领取
                            handNewTaskDTO.setActionType(AppConstants.MallTaskStatus.ACTION_STATUS_6);
                            handNewTaskDTO.setFlowId(taskFlow.getId().toString());
                            handNewTaskDTOs.add(handNewTaskDTO);
                        }
                        handNewTaskFlows.remove(taskFlow);
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    handNewTaskDTO.setActionType(AppConstants.MallTaskStatus.ACTION_STATUS_2);//去投资
                    handNewTaskDTOs.add(handNewTaskDTO);
                }
            }
        }

        //小试牛刀--注册24小时内投资
        if (AppConstants.MallTaskStatus.TASK_ID_24H_INVEST.equals(task.getId())) {
            handNewTaskDTO.setTaskType(AppConstants.MallTaskStatus.TASK_ID_24H_INVEST.toString());
            if (handNewTaskFlows.isEmpty()) {
                Date registorDate = mainInfo.getCmInputDate();
                DateTime start = new DateTime(registorDate);
                DateTime end = DateTime.now();
                int hours = Hours.hoursBetween(start, end).getHours();
//               long minSecond = new Date().getTime() - registorDate.getTime();
//               int hours  = (int)(minSecond/60/60/1000);
                if (hours >= 24) {//注册超过24小时，任务已失效
                } else {//注册24小时内，任务没完成
                    handNewTaskDTO.setActionType(AppConstants.MallTaskStatus.ACTION_STATUS_2);//去投资
                    handNewTaskDTOs.add(handNewTaskDTO);
                }
            } else {
                boolean flag = false;
                for (BusiTaskFlow taskFlow : handNewTaskFlows) {
                    if (AppConstants.MallTaskStatus.TASK_ID_24H_INVEST.equals(taskFlow.getTaskId())) {
                        if (AppConstants.MallTaskStatus.TASK_UNRECEIVE_STATUS.equals(taskFlow.getStatus())) {//未领取
                            handNewTaskDTO.setActionType(AppConstants.MallTaskStatus.ACTION_STATUS_6);
                            handNewTaskDTO.setFlowId(taskFlow.getId().toString());
                            handNewTaskDTOs.add(handNewTaskDTO);
                        } else if (AppConstants.MallTaskStatus.TASK_RECEIVE_STATUS.equals(taskFlow.getStatus())) {//已领取
                            handNewTaskDTO.setActionType(AppConstants.MallTaskStatus.ACTION_STATUS_9);
                            handNewTaskDTODones.add(handNewTaskDTO);
                        }
                        handNewTaskFlows.remove(taskFlow);
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    Date registorDate = mainInfo.getCmInputDate();
                    DateTime start = new DateTime(registorDate);
                    DateTime end = DateTime.now();
                    int hours = Hours.hoursBetween(start, end).getHours();
//                   long minSecond = new Date().getTime() - registorDate.getTime();
//                   int hours  = (int)(minSecond/60/60/1000);
                    if (hours >= 24) {//注册超过24小时，任务已失效
                    } else {//注册24小时内，任务没完成
                        handNewTaskDTO.setActionType(AppConstants.MallTaskStatus.ACTION_STATUS_2);//去投资
                        handNewTaskDTOs.add(handNewTaskDTO);
                    }
                }
            }
        }

        //微信绑定
        if (AppConstants.MallTaskStatus.TASK_ID_BIND_WEICHAT.equals(task.getId())) {
            handNewTaskDTO.setTaskType(AppConstants.MallTaskStatus.TASK_ID_BIND_WEICHAT.toString());
            if (StringUtils.isEmpty(mainInfo.getOpenId())) {
                handNewTaskDTO.setActionType(AppConstants.MallTaskStatus.ACTION_STATUS_3);//去绑定
                handNewTaskDTOs.add(handNewTaskDTO);
            } else {
                if (handNewTaskFlows.isEmpty()) {
                    log.error("用户已绑定微信，但没有奖励流水，cmNumber=" + mainInfo.getCmNumber());
                } else {
                    for (BusiTaskFlow taskFlow : handNewTaskFlows) {
                        if (AppConstants.MallTaskStatus.TASK_UNRECEIVE_STATUS.equals(taskFlow.getStatus())) {//未领取
                            handNewTaskDTO.setActionType(AppConstants.MallTaskStatus.ACTION_STATUS_6);//去领取
                            handNewTaskDTO.setFlowId(taskFlow.getId().toString());
                            handNewTaskDTOs.add(handNewTaskDTO);
                        } else if (AppConstants.MallTaskStatus.TASK_RECEIVE_STATUS.equals(taskFlow.getStatus())) {//已领取
                            handNewTaskDTO.setActionType(AppConstants.MallTaskStatus.ACTION_STATUS_9);//已领取
                            handNewTaskDTODones.add(handNewTaskDTO);
                        }
                        handNewTaskFlows.remove(taskFlow);
                        break;
                    }
                }
            }
        }
    }

    private void taskFlowSort(List<BusiTask> tasks, List<BusiTaskFlow> taskFlows,
                              List<BusiTaskFlow> handNewTasks,
                              List<BusiTaskFlow> monthTasks,
                              List<BusiTaskFlow> dailyTasks,
                              List<BusiTaskFlow> limitTimeTasks,int num) {
        Set<Long> limitTaskIds = Sets.newHashSet();
        Set<Long> monthTaskIds = Sets.newHashSet();
        for (BusiTask task : tasks) {
            if (AppConstants.MallTaskStatus.TASK_TYPE_2.equals(task.getTaskType())) {
                limitTaskIds.add(task.getId());
            }
            if (AppConstants.MallTaskStatus.TASK_TYPE_3.equals(task.getTaskType())) {
                monthTaskIds.add(task.getId());
            }
        }
        for (BusiTaskFlow taskFlow : taskFlows) {
            if (taskFlow.getTaskId().equals(AppConstants.MallTaskStatus.TASK_ID_SIGN)) {//签到
                dailyTasks.add(taskFlow);
            } else if (taskFlow.getTaskId().equals(AppConstants.MallTaskStatus.TASK_ID_INVITATION)) {//邀请
                dailyTasks.add(taskFlow);
            } else if(taskFlow.getTaskId().equals(Long.parseLong(configParamBean.getScratchCardId()))){//刮刮卡
                dailyTasks.add(taskFlow);
            } else if(taskFlow.getTaskId().equals(Long.parseLong(configParamBean.getCatchDollId()))){//抓娃娃
                dailyTasks.add(taskFlow);
            } else if (taskFlow.getTaskId().equals(AppConstants.MallTaskStatus.TASK_ID_AUTH)) {//认证
                handNewTasks.add(taskFlow);
            } else if (taskFlow.getTaskId().equals(AppConstants.MallTaskStatus.TASK_ID_FIRST_INVEST)) {//小试牛刀--首笔投资
                handNewTasks.add(taskFlow);
            } else if (taskFlow.getTaskId().equals(AppConstants.MallTaskStatus.TASK_ID_24H_INVEST)) {//小试牛刀--24内投资
                handNewTasks.add(taskFlow);
            } else if (taskFlow.getTaskId().equals(AppConstants.MallTaskStatus.TASK_ID_BIND_WEICHAT)) {//绑定微信
                handNewTasks.add(taskFlow);
            } else if (limitTaskIds.contains(taskFlow.getTaskId())) {
                limitTimeTasks.add(taskFlow);
            } else if (monthTaskIds.contains(taskFlow.getTaskId())) {
                if(org.apache.commons.lang3.StringUtils.equals(taskFlow.getStatus()+"","1")){ //记录完成多捞多得任务进度
                    num += 1;
                }
                monthTasks.add(taskFlow);
            }
        }
    }

    public Map<String, Object> receiveTaskReward(String cmToken, String flowIds) {
        Map<String, Object> result = Maps.newTreeMap();
        String cmNumber = LaocaiUtil.sessionToken2CmNumber(cmToken, configParamBean.getUserTokenKey());
        checkExistByCmNumber(cmNumber);

        List<Long> flowIdList = Lists.newArrayList();
        Iterable<String> strings = Splitter.on(",").omitEmptyStrings().trimResults().split(flowIds);
        for (String flowIdStr : strings) {
            flowIdList.add(Long.parseLong(flowIdStr));
        }
        if (flowIdList.isEmpty()) {
            result.put("receiveStatus", "fail");
            result.put("message", "没有有效的任务ID");
        } else {
            ResultDto<Map<String,Object>> resultDto = finishTask(flowIdList, cmNumber);
            if (resultDto.isSuccess()) {
                result.put("receiveStatus", "success");
                result.put("lcbAmt", Integer.parseInt(String.valueOf(resultDto.getData().get("totalLcbAmt"))));
                result.put("totalIntegral", Integer.parseInt(String.valueOf(resultDto.getData().get("totalIntegral"))));
                result.put("totalCash", Integer.parseInt(String.valueOf(resultDto.getData().get("totalCash"))));
                result.put("fragments",resultDto.getData().get("fragments"));
            } else {
                result.put("receiveStatus", "fail");
                result.put("message", resultDto.getMsg());
            }
        }
        return result;
    }

    public List<MonthTaskListDTO> getMonthTask() {
        List<MonthTaskListDTO> monthTaskList = Lists.newArrayList();
        Example example = new Example(BusiTask.class);
        example.createCriteria().andEqualTo("showStatus", "1").andEqualTo("status", "1")
                .andEqualTo("taskType", AppConstants.MallTaskStatus.TASK_TYPE_3).andLessThanOrEqualTo("taskStartTime", new Date())
                .andGreaterThanOrEqualTo("taskEndTime", new Date());
        example.setOrderByClause("invest_amt asc");
        List<BusiTask> tasks = getBusiTaskMapper().selectByExample(example);
        if (!tasks.isEmpty()) {
            for (BusiTask task : tasks) {
                MonthTaskListDTO dto = new MonthTaskListDTO();
                if(org.apache.commons.lang3.StringUtils.contains(task.getAwardType(),"1")){//显示碎片奖励个数
                    dto.setChestNum(1);
                }else{
                    dto.setChestNum(0);
                }
                if(!org.apache.commons.lang3.StringUtils.equals(task.getAwardType(),"1")){
                    dto.setLcbAmt(task.getLcAmt());
                }else{
                    dto.setLcbAmt(0L);
                }
                dto.setInvestAmt(task.getInvestAmt().longValue());

                monthTaskList.add(dto);
            }
        }
        return monthTaskList;
    }

    /*查询用户所有有效奖励*/
    public List<BusiTaskFlow> getUserTaskFlow(CustomerMainInfo mainInfo) {
        Example example = new Example(BusiTaskFlow.class);
        example.createCriteria().andEqualTo("cmNumber", mainInfo.getCmNumber())
                .andCondition("(last_time is null or to_char(last_time,'yyyymmddhh24miss') >= " + DateUtil.timeFormat(new Date(), "yyyyMMddHHmmss") + ")");
        example.setOrderByClause("id asc");
        return busiTaskFlowMapper.selectByExample(example);
    }

    public void setCommonDTO(TaskCommonDTO commonDTO, BusiTask task) {
        commonDTO.setTaskType(task.getTaskType());
        commonDTO.setTaskName(task.getTaskName());
    }

    public BusiAddupCustomerOrder getAddupCustomerOrder(String cmNumber) {
        BusiAddupCustomerOrder addupCustomerOrder = new BusiAddupCustomerOrder();
        addupCustomerOrder.setCustomerNo(cmNumber);
        addupCustomerOrder.setYearMonth(DateUtil.timeFormat(new Date(), "yyyyMM"));
        addupCustomerOrder = addupCustomerOrderMapper.selctBusiAddupCustomerOrder(addupCustomerOrder);
        return addupCustomerOrder;
    }




    /**
     *  @Description: 领取任务奖励 (发送捞财币和碎片奖励)
     *  @author huangcy
     *  @date 2017/7/28
    */
    public ResultDto<Map<String,Object>> finishTask(List<Long> taskFlowIds, String cmNumber) {
        if (CollectionUtils.isEmpty(taskFlowIds)) {
            log.error("领取失败，任务ID为空，{}", taskFlowIds);
            return new ResultDto<Map<String,Object>>("领取失败，任务ID为空", false);
        }
		//所有任务list
        List<BusiTaskFlow> taskFlowList = busiTaskFlowService.findReceivableByIdIn(taskFlowIds, cmNumber);
        Long totalLcbAmt = 0L;
        BigDecimal totalIntegral = new BigDecimal(0);
        BigDecimal totalCash = new BigDecimal(0);
        Map<String,Object> resParams = Maps.newHashMap();
        List<GainFragmentDto> fragments = Lists.newArrayList(); //碎片任务
		List<CoinOprDto> coinOprDtoList = Lists.newArrayList(); //捞财币任务
        if (CollectionUtils.isEmpty(taskFlowList)) {
            log.error("领取失败，没有对应的任务记录，{}", taskFlowIds);
            return new ResultDto<Map<String,Object>>("领取失败，没有对应的任务记录", false);
        }

        for (BusiTaskFlow taskFlow : taskFlowList) {
            if(StrUtil.equals(taskFlow.getTaskType(),"1")){
            	//领取碎片奖励
				ResultDto<Map<String, Object>> mapResultDto = this.sendPiecesOfReward(fragments, taskFlow, cmNumber);
				if (!mapResultDto.isSuccess()) return mapResultDto;
			}else if (StrUtil.equals(taskFlow.getTaskType(),"0") && taskFlow.getLcbAmt().intValue() > 0){
            	//将任务捞财币累加之后统一赠送
                CoinOprDto oprDto = new CoinOprDto();
                oprDto.setAccountNo(taskFlow.getCmNumber());
                BusiTask busiTask = findOne(taskFlow.getTaskId());
                if (busiTask == null) {
                    log.error("没有对应的任务，跳过 {}", taskFlow.getTaskId());
                    continue;
                }
                oprDto.setCoin(taskFlow.getLcbAmt());
                totalLcbAmt += taskFlow.getLcbAmt();
                oprDto.setTips("完成任务: " + busiTask.getTaskName());
                String transNo = SerialNoGeneratorService.generateTransNoByOrderId(String.valueOf(taskFlow.getTaskId()),"TASK");
                oprDto.setTransNo(transNo);
                coinOprDtoList.add(oprDto);
            }else if(StrUtil.equals(taskFlow.getTaskType(),"2") && taskFlow.getLcbAmt().intValue() > 0){ //领取积分
				ResultDto resultDto = this.sendIntegral(taskFlow, cmNumber);
				if (!resultDto.isSuccess()) return resultDto;
                totalIntegral = totalIntegral.add(new BigDecimal(taskFlow.getLcbAmt()));
            }else if(StrUtil.equals(taskFlow.getTaskType(),"3")){ //领取现金券
				ResultDto resultDto = this.sendRCashCoupon(taskFlow, cmNumber);
				if (!resultDto.isSuccess()) return resultDto;
                totalCash = totalCash.add(new BigDecimal(resultDto.getData().toString()));
			}
        }

        //批量发送捞财币
        if (coinOprDtoList.size()>0){
            ResultDto<Map<String, Object>> mapResultDto = this.sendLcbCoin(coinOprDtoList, taskFlowList, cmNumber);
            if (mapResultDto != null && !mapResultDto.isSuccess()){
                return mapResultDto;
            }
        }

		resParams.put("fragments",fragments);
        resParams.put("totalLcbAmt",totalLcbAmt);
        resParams.put("totalIntegral",totalIntegral.intValue());
        resParams.put("totalCash",totalCash.intValue());
        return new ResultDto<Map<String,Object>>(resParams);
    }

	/**
	 * 领取积分
	 * @param taskFlow
	 * @param cmNumber
	 * @return
	 */
    private ResultDto sendIntegral(BusiTaskFlow taskFlow,String cmNumber){
    	if (StrUtil.equals(taskFlow.getTaskType(),"2")){
			taskFlow.setStatus(Short.parseShort(Constants.TASK_FLOW_STATUS_RECEIVING));
			busiTaskFlowService.update(taskFlow);

			//封装 IntegralGiveDto
			IntegralGiveDto integralGiveDto = new IntegralGiveDto();
			String transNo = this.createIntegralNo(taskFlow.getTaskId(), cmNumber);
			integralGiveDto.setTransNo(transNo);//流水号
			integralGiveDto.setAccountNo(cmNumber);//用户编号
			integralGiveDto.setIntegral(taskFlow.getLcbAmt().intValue());
			integralGiveDto.setChannel("lcb");
			integralGiveDto.setNo(IntegralSourceNo.TGL_GAIN_INTEGRAL_BY_ACTIVITY_ALL.getCode());
			integralGiveDto.setPlatform("all");
			//调用 送积分
			com.zdmoney.integral.api.common.dto.ResultDto<IntegralResDto> integralResDtoResultDto = integralAccountFacadeService
					.giveGetIntegral(integralGiveDto);
			if(!integralResDtoResultDto.isSuccess()){
				log.error("调用系统发送积分失败,RES[{}]", BeanUtil.beanToMap(integralResDtoResultDto));
				return new ResultDto<Map<String,Object>>(integralResDtoResultDto.getMsg(), false);
			}
			//更新任务流水 -> 任务已领取
			taskFlow.setStatus(Short.parseShort(Constants.TASK_FLOW_STATUS_OVER));
			taskFlow.setReceiveTime(new Date());
			taskFlow.setRecordNum(transNo);
			busiTaskFlowService.update(taskFlow);
		}
		return new ResultDto();
	}

	/**
	 * @description 生成积分流水
	 *		
	 * @params [method, taskId, cmNumber]
	 * @return java.lang.String
	 * @date: 2018/12/6 19:16
	 * @author: huangcy
	 */
	private String createIntegralNo(Long taskId,String cmNumber){
		return StrBuilder.create().append("TASK_").append(cmNumber).append("_").append(taskId).toString();
	}

    /**
     * @descriptio 领取现金券
     *		
     * @params []
     * @return com.zdmoney.marketing.api.dto.ResultDto
     * @date: 2018/12/6 16:46
     * @author: huangcy
     */
    private ResultDto sendRCashCoupon(BusiTaskFlow taskFlow,String cmNumber){
        int totalCash = 0;
    	if (StrUtil.equals(taskFlow.getTaskType(),"3")){
			taskFlow.setStatus(Short.parseShort(Constants.TASK_FLOW_STATUS_RECEIVING));
			busiTaskFlowService.update(taskFlow);
			CashDto cashDtoByTaskId = busiTaskFlowService.getCashDtoByTaskId(taskFlow.getTaskId());
			if (cashDtoByTaskId == null){
				log.info("任务对应的现金券未配置，请检查,taskFlow : [{}]",BeanUtil.beanToMap(taskFlow));
				return new ResultDto<Map<String,Object>>("任务对应的现金券未配置，请检查", false);
			}
			cashDtoByTaskId.setAccountNo(cmNumber);
			cashDtoByTaskId.setPublishSource(CashPublishSource.ACTIVITY);
			com.zdmoney.webservice.api.common.dto.ResultDto resultDto = sendRCashCoupon(cashDtoByTaskId,cmNumber, null);
			if (!resultDto.isSuccess()){
				log.error("调用系统发送现金券失败,RES[{}]", BeanUtil.beanToMap(resultDto));
				return new ResultDto<Map<String,Object>>(resultDto.getMsg(), false);
			}
            JSONObject res = JSONUtil.parseObj(resultDto.getData());
            totalCash = ((List)res.get("results")).size();
            taskFlow.setStatus(Short.parseShort(Constants.TASK_FLOW_STATUS_OVER));
			taskFlow.setReceiveTime(new Date());
			taskFlow.setRecordNum(res.get("transNo").toString());
			busiTaskFlowService.update(taskFlow);
		}
		return new ResultDto(totalCash);
	}

	/**
	 * 批量送捞财币
	 * @return
	 */
	private ResultDto<Map<String,Object>> sendLcbCoin(List<CoinOprDto> coinOprDtoList,List<BusiTaskFlow> taskFlowList,String cmNumber){
		if (coinOprDtoList.size() > 0) {
			try {
				for (BusiTaskFlow taskFlow : taskFlowList) {
					if(StrUtil.equals(taskFlow.getTaskType(),"0")){
						taskFlow.setStatus(Short.parseShort(Constants.TASK_FLOW_STATUS_RECEIVING));
						busiTaskFlowService.update(taskFlow);
					}
				}
				com.zdmoney.integral.api.common.dto.ResultDto<List<CoinOprResultDto>> listResultDto = welfareService.batchGainCoin( coinOprDtoList);
				if (!listResultDto.isSuccess()) {
					log.error("调用系统发送捞财币失败,RES[{}]",BeanUtil.beanToMap(listResultDto));
					return new ResultDto<Map<String,Object>>("调用系统发送捞财币失败", false);
				}
				List<CoinOprResultDto> data = listResultDto.getData();
				int i = 0;
				for (BusiTaskFlow taskFlow : taskFlowList) {
					if(StrUtil.equals(taskFlow.getTaskType(),"0")) {
						taskFlow.setStatus(Short.parseShort(Constants.TASK_FLOW_STATUS_OVER));
						taskFlow.setReceiveTime(new Date());
						taskFlow.setRecordNum(data.get(i).getRecordNum());
						busiTaskFlowService.update(taskFlow);
						i++;
					}
				}
			} catch (Exception e) {
				log.error("[cmNumber: {}] 发送捞财币出现异常，{}", cmNumber, e);
				return new ResultDto<Map<String,Object>>("发送捞财币出现异常", false);
			}
			return new ResultDto<>();
		}
		return new ResultDto("无任务领取",false);
	}

	/**
	 * 送碎片奖励
	 */
	private ResultDto<Map<String,Object>> sendPiecesOfReward(List<GainFragmentDto> fragments,BusiTaskFlow taskFlow,String cmNumber){
		taskFlow.setStatus(Short.parseShort(Constants.TASK_FLOW_STATUS_RECEIVING));
		busiTaskFlowService.update(taskFlow);
		com.zdmoney.gamecenter.api.common.dto.ResultDto<GainFragmentDto> gainFragmentDtoResultDto = fragmentFacadeService.gainFragment(cmNumber, getSource(taskFlow.getTaskId() + ""));
		if (!gainFragmentDtoResultDto.isSuccess()){
			log.error("调用系统发送碎片失败,RES[{}]", BeanUtil.beanToMap(gainFragmentDtoResultDto));
			return new ResultDto<Map<String,Object>>("调用系统发送碎片失败", false);
		}
		taskFlow.setStatus(Short.parseShort(Constants.TASK_FLOW_STATUS_OVER));
		taskFlow.setReceiveTime(new Date());
		taskFlow.setRecordNum(gainFragmentDtoResultDto.getData().getId()+"");
		busiTaskFlowService.update(taskFlow);
		fragments.add(gainFragmentDtoResultDto.getData());
		return new ResultDto();
	}

	/**
	 * 金象 or 银象
	 * @param taskId
	 * @return
	 */
	private String getSource(String taskId){
        if(StringUtils.equals(configParamBean.getScratchCardId(), taskId)){
            return "0";
        }else if(StringUtils.equals(configParamBean.getCatchDollId() ,taskId)){
            return "1";
        }
        return "2";
    }

	/**
	 * @description 发送现金券
	 *		
	 * @params [cashPublishSource, cmNumber, expireDate]
	 * @return com.zdmoney.webservice.api.common.dto.ResultDto
	 * @date: 2018/12/6 17:31
	 * @author: huangcy
	 */
	public com.zdmoney.webservice.api.common.dto.ResultDto sendRCashCoupon(CashDto cashDtoByTaskId,String cmNumber,Date expireDate){
		List<CashDto> cashDtos = new ArrayList<>();
		cashDtos.add(cashDtoByTaskId);
		//请求流水号
		String transNo = SerialNoGeneratorService
				.commonGenerateTransNo(SerialNoType.values()[(Integer.valueOf(cashDtoByTaskId.getPublishSource().getValue())-1)], cmNumber);
		com.zdmoney.integral.api.common.dto.ResultDto<List<String>> resultDto;
		try {
			resultDto = cashFacadeService.publishCash(cashDtos, transNo );
			if (!resultDto.isSuccess()) {
				log.error("现金券发放失败! 任务类型:{},用户编号为:{},错误原因:{}", cashDtoByTaskId.getPublishSource().getValue(), cmNumber, resultDto.getMsg());
				return new com.zdmoney.webservice.api.common.dto.ResultDto("现金券发放失败：", resultDto.getMsg(), Boolean.FALSE);
			}
		} catch (Exception e) {
			log.error("调用账户系统--->，现金券发放异常，cmNumber【{}】", cmNumber, e);
			return new com.zdmoney.webservice.api.common.dto.ResultDto("现金券发放异常", Boolean.FALSE);
		}

		BusiCashRecord record = new BusiCashRecord();
		cn.hutool.core.date.DateTime nowDate = cn.hutool.core.date.DateUtil.date();
		record.setCashCouponType(cashDtoByTaskId.getPublishSource().getValue());
		record.setCmNumber(cmNumber);
		record.setCreateDate(nowDate);
		record.setModifyDate(nowDate);
		record.setStatus("1");
		record.setCashCouponId(CollUtil.join(resultDto.getData(), ","));
		record.setExpireDate(expireDate==null? nowDate:expireDate);
		cashRecordMapper.saveBusiCashRecord(record);

        JSONObject data = JSONUtil.createObj();
        data.put("results",resultDto.getData());
        data.put("transNo",transNo);
        return new com.zdmoney.webservice.api.common.dto.ResultDto("现金券发放成功", data,Boolean.TRUE);
	}
}
