package com.zdmoney.service.mall;

import com.google.common.collect.Maps;
import com.zdmoney.common.service.BaseService;
import com.zdmoney.constant.Constants;
import com.zdmoney.integral.api.dto.cash.CashDto;
import com.zdmoney.mapper.customer.CustomerMainInfoMapper;
import com.zdmoney.mapper.task.BusiTaskFlowMapper;
import com.zdmoney.mapper.task.BusiTaskMapper;
import com.zdmoney.models.BusiAddupCustomerOrder;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.order.BusiOrderTemp;
import com.zdmoney.models.task.BusiTask;
import com.zdmoney.models.task.BusiTaskFlow;
import com.zdmoney.service.BusiAddupCustomerOrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
* BusiTaskFlowService
* <p/>
* Author: Hao Chen
* Date: 2016-02-27 11:04:02
* Mail: haoc@zendaimoney.com
*/
@Service
@Slf4j
public class BusiTaskFlowService extends BaseService<BusiTaskFlow, Long> {

    @Autowired
    private BusiAddupCustomerOrderService busiAddupCustomerOrderService;

    @Autowired
    private CustomerMainInfoMapper customerMainInfoMapper;

    @Autowired
    private BusiTaskMapper busiTaskMapper;

    private BusiTaskFlowMapper getBusiTaskFlowMapper() {
        return (BusiTaskFlowMapper) baseMapper;
    }

    public CashDto getCashDtoByTaskId(Long taskId){
		return busiTaskMapper.getCashDtoByTaskId(taskId);
	}

    public boolean isFinished(String cmNumber, Long taskId, String taskType) {
        Example example = new Example(getEntityClass());
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("cmNumber", cmNumber)
                .andEqualTo("taskId" , taskId);
        if(StringUtils.equals(Constants.TASK_TYPE_DAILY, taskType)){
            Date start = new DateTime(new Date()).secondOfDay().withMinimumValue().toDate();
            Date end = new DateTime(start).secondOfDay().withMaximumValue().toDate();
            criteria.andBetween("createDate", start, end);
        }else if(StringUtils.equals(Constants.TASK_TYPE_MONTHLY, taskType)){
            Date start = new DateTime(new Date()).dayOfMonth().withMinimumValue().secondOfDay().withMinimumValue().toDate() ;
            Date end = new DateTime(start).dayOfMonth().withMaximumValue().secondOfDay().withMaximumValue().toDate();
            criteria.andBetween("createDate", start, end);
        }else if(!StringUtils.equals(Constants.TASK_TYPE_ONCE, taskType) && !StringUtils.equals(Constants.TASK_TYPE_TIME_LIMITED, taskType)){
            log.warn("未知的任务类型: {}", taskType);
        }
        return baseMapper.selectCountByExample(example) > 0;
    }

    public boolean isRelated(String cmNumber, String relationCmNumber, Long taskId) {
        Example example = new Example(getEntityClass());
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("cmNumber", cmNumber)
                .andEqualTo("taskId" , taskId)
                .andEqualTo("relationCmNumber", relationCmNumber);
        return baseMapper.selectCountByExample(example) > 0;
    }

    public List<BusiTaskFlow> findReceivableByIdIn(List<Long> taskFlowIds, String cmNumber) {
        Example example = new Example(getEntityClass());
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("id", taskFlowIds);
        criteria.andEqualTo("cmNumber", cmNumber);
        criteria.andEqualTo("status", Constants.TASK_FLOW_STATUS_TO_OBTAIN);
        example.setOrderByClause("id desc");
        return findByExample(example);
    }


    /**
     *  @Description: 多捞多得 完成任务记录
     *  @author huangcy
     *  @date 2017/7/27
    */
    @Async
    public void saveBusiTaskFlow(BusiOrderTemp order, CustomerMainInfo customer){
        List<BusiTask> taskList = findValidListByActionType(Constants.TASK_ACTION_TYPE_ORDER);
        for (BusiTask busiTask : taskList) {
            // 先判断是否可以完成任务
            boolean canFinish = false;
            if (Constants.TASK_TYPE_UNLIMITED.equals(busiTask.getTaskType())) {
                canFinish = true;
            } else if (!isFinished(customer.getCmNumber(), busiTask.getId(), busiTask.getTaskType())) {
                canFinish = true;
            }
            if (!canFinish) {
                continue;
            }
            // 投资
            if (Constants.TASK_ACTION_TYPE_ORDER.equals(busiTask.getActionType())) {
                if (order.getOrderAmt() == null) {
                    log.warn("[{}] 下单金额为空, 忽略", customer.getCmNumber());
                    continue;
                }
                // 累计任务
                if (StringUtils.equals(Constants.TASK_LIMIT_TYPE_MULTIPLE, busiTask.getLimitType())) {
                    String dateStr = new SimpleDateFormat("yyyyMM").format(new Date());
                    BusiAddupCustomerOrder busiAddupCustomerOrder = busiAddupCustomerOrderService.findByCustomerNo(customer.getCmNumber(), dateStr);
                    if (busiAddupCustomerOrder == null) {
                        canFinish = false;
                    } else if (busiAddupCustomerOrder.getOrderAmt().compareTo(busiTask.getInvestAmt()) < 0) {
                        canFinish = false;
                    }
                    // 单次任务
                } else {
                    if (busiTask.getInvestPeriod() != null && busiTask.getInvestPeriod() > getInvestPeriod(order)) {
                        canFinish = false;
                    }
                    if (busiTask.getPeriodUpperLimit() != null && busiTask.getPeriodUpperLimit() < getInvestPeriod(order)) {
                        canFinish = false;
                    }
                    if (busiTask.getInvestAmt() != null && busiTask.getInvestAmt().compareTo(order.getOrderAmt()) > 0) {
                        canFinish = false;
                    }
                    if (Constants.TASK_ID_24H_INVEST.equals(busiTask.getId()) && !isWithin24Order(customer,order)) {
                        canFinish = false;
                    }
                }
                // 起息(邀请)
            } else if (Constants.TASK_ACTION_TYPE_INTEREST.equals(busiTask.getActionType())) {
                if (getBeInvitedCustomer(customer) == null || isRelated(customer.getCmNumber(), getBeInvitedCustomer(customer).getCmNumber(), busiTask.getId())) {
                    canFinish = false;
                }
            }
            if (canFinish) {
                BusiTaskFlow busiTaskFlow = new BusiTaskFlow();
                busiTaskFlow.setTaskId(busiTask.getId());
                busiTaskFlow.setCmNumber(customer.getCmNumber());
                Date now = new Date();
                busiTaskFlow.setCreateDate(now);
                busiTaskFlow.setStatus(Short.parseShort(Constants.TASK_FLOW_STATUS_TO_OBTAIN));
                Date lastTime = null;
                if (StringUtils.equals(Constants.TASK_TYPE_MONTHLY, busiTask.getTaskType())) {
                    lastTime = new DateTime(now).secondOfDay().withMinimumValue().dayOfMonth().withMaximumValue().secondOfDay().withMaximumValue().toDate();
                } else if (StringUtils.equals(Constants.TASK_TYPE_TIME_LIMITED, busiTask.getTaskType())) {
                    lastTime = busiTask.getTaskEndTime();
                }
                busiTaskFlow.setLastTime(lastTime);
                if (Constants.TASK_ACTION_TYPE_INTEREST.equals(busiTask.getActionType()) && getBeInvitedCustomer(customer) != null) {
                    busiTaskFlow.setRelationCmNumber(getBeInvitedCustomer(customer).getCmNumber());
                }
                if(StringUtils.contains(busiTask.getAwardType(),"1")){//发碎片
                    busiTaskFlow.setTaskType("1");
                    getBusiTaskFlowMapper().insert(busiTaskFlow);
                }
                if(!StringUtils.equals(busiTask.getAwardType(),"1")){//发捞财币
                    busiTaskFlow.setTaskType("0");
                    busiTaskFlow.setLcbAmt(busiTask.getLcAmt());
                    getBusiTaskFlowMapper().insert(busiTaskFlow);
                }

            }
        }
        //return busiTaskFlowList;
    }

    /**
     *  @Description: 以actionType查询任务
     *  @author huangcy
     *  @date 2017/7/27
     */
    public List<BusiTask> findValidListByActionType(String actionType) {
        Map<String, Object> map = Maps.newHashMap();
        map.put("actionType", actionType);
        map.put("now", new Date());
        return busiTaskMapper.selectValidListByActionType(map);
    }

    /**
     *  @Description: 查询邀请人信息
     *  @author huangcy
     *  @date 2017/7/27
    */
    private CustomerMainInfo getBeInvitedCustomer(CustomerMainInfo customerMainInfo){
        String introduceCode = customerMainInfo.getCmIntroduceCode();
        if (!org.apache.commons.lang.StringUtils.isEmpty(introduceCode)) {
            Map<String, Object> map = Maps.newTreeMap();
            map.put("cmInviteCode", introduceCode);
            List<CustomerMainInfo> mainInfos = customerMainInfoMapper.getCustomerBySearchParam(map);
            if (!mainInfos.isEmpty()) {
                return mainInfos.get(0);
            }
        }
        return null;
    }

    /**
     *  @Description: 投资时间是否小于24小时
     *  @author huangcy
     *  @date 2017/7/27
    */
    private boolean isWithin24Order(CustomerMainInfo customerMainInfo,BusiOrderTemp order){
        DateTime start = new DateTime(customerMainInfo.getCmInputDate());
        DateTime end = new DateTime(order.getOrderTime());
        int hours = Hours.hoursBetween(start, end).getHours();
        return hours < 24;
    }

    /**
     *  @Description: 获得理财期限
     *  @author huangcy
     *  @date 2017/7/27
    */
    public int getInvestPeriod(BusiOrderTemp orderTemp) {
        DateTime start = new DateTime(orderTemp.getInterestStartDate());
        DateTime end = new DateTime(orderTemp.getInterestEndDate());
//        long second = orderTemp.getInterestEndDate().getTime()-orderTemp.getInterestStartDate().getTime();
//        int days = (int)(second/60/60/1000/24) + 1;
        int days = Days.daysBetween(start, end).getDays() + 1;
        return days;
    }

    /**
     *  @Description: 查询任务流水记录
     *  @author huangcy
     *  @date 2017/10/11
    */
    public int findTaskFlow(String cmNumber,Long taskId){
        BusiTaskFlow busiTaskFlow = new BusiTaskFlow();
        busiTaskFlow.setTaskId(taskId);
        busiTaskFlow.setCmNumber(cmNumber);
        return getBusiTaskFlowMapper().selectCount(busiTaskFlow);
    }
    /**
     *  @Description: 查询投资任务流水记录
     *  @date 2017/10/11
     */
    public List<BusiTaskFlow> findOrderTaskFlow(List<Long> taskFlowIds, String cmNumber) {
        Example example = new Example(getEntityClass());
        Example.Criteria criteria = example.createCriteria();
        criteria.andIn("taskId", taskFlowIds);
        criteria.andEqualTo("cmNumber", cmNumber);
       // criteria.andEqualTo("status", Constants.TASK_FLOW_STATUS_TO_OBTAIN);
        example.setOrderByClause("id desc");
        return findByExample(example);
    }
    /**
     *  @Description: 查询限时任务流水记录
     *  @date 2017/10/11
     */
    public List<BusiTaskFlow> findLimitTimeTaskFlow(String cmNumber,Date lastTime,Long taskId) {
        Example example = new Example(getEntityClass());
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("cmNumber", cmNumber);
        criteria.andEqualTo("lastTime", lastTime);
        criteria.andEqualTo("taskId", taskId);
        example.setOrderByClause("id desc");
        return findByExample(example);
    }
}