package com.zdmoney.mapper.customer;

import com.zdmoney.models.customer.CustomerSignRule;
import com.zdmoney.models.customer.CustomerSignInfo;
import com.zdmoney.models.customer.CustomerSignLog;

import java.util.List;

/**
 * Created by Gosling on 2016/12/7.
 */
public interface CustomerSignInfoMapper{

    /**
     * 保存用户签到信息
     * @param signInfo
     * @return
     */
    Long insertSignInfo(CustomerSignInfo signInfo);

    /**
     * 查询用户签到明细
     * @param customerId
     * @return
     */
    CustomerSignLog querySignDetail(Long customerId);

    /**
     * 根据客户ID查询签到次数
     * @param customerId
     * @return
     */
    Long querySignCount(Long customerId);

    /**
     * 当签到满30天后,更改之前的签到状态
     * @param customerId
     * @return
     */
    Long updateSignStatus(Long customerId);

    /**
     * 查询当前用户当天是否有签到记录
     * @param customerId
     * @return
     */
    Long queryNowadaysSignCount(Long customerId);

    /**
     * 查询用户昨天签到数据
     * @param customerId
     * @return
     */
    Long queryYesterdaySignInfo(Long customerId);

    /**
     * 查询签到日期
     * @param customerId
     * @return
     */
    List<CustomerSignInfo> queryCustomerSignDate(Long customerId);

    /**
     * 插入签到奖励日志
     * @param signLog
     * @return
     */
    Long insertSignLog(CustomerSignLog signLog);

    /**
     * 根据签到天数查询发放礼品规则
     * @param signDays
     * @return
     */
    CustomerSignRule querySignRule(int signDays);
}

