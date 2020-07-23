package com.zdmoney.mapper.payment;

import com.zdmoney.common.mybatis.mapper.JdMapper;
import com.zdmoney.models.payment.PaymentPlan;
import com.zdmoney.vo.UserRepaymentVo;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface PaymentPlanMapper extends JdMapper<PaymentPlan, Long> {

    List<PaymentPlan> selectPaymentPlanById(String orderId);

    List<PaymentPlan> selectRepayInfoByCustomerId(String customerId);

    List<PaymentPlan> selectOrderRepayByCustomerId(Map map);

    List<PaymentPlan> selectPaymentPlansById(String orderId);

    BigDecimal  selectHistoryInterestByCustomerId(Long customerId);

    /*用户单笔(标的订单或转让订单)历史收益利息v4.1*/
    UserRepaymentVo selectHistoryOrderInterestByCustomerId(Map<String,Object> map);

    int updateByMap(Map<String, Object> map);

    BigDecimal getTotalPrincipal(@Param("originOrderNo")String orderId);

    /**
     * 查询当前时间之前未还款信息
     * @param orderNum
     * @return
     */
    List<PaymentPlan> selectPayPlansByCurrentTime(String orderNum, String repayDay);

    /**
     * 根据时间，订单号查还款信息
     * @return
     */
    List<PaymentPlan> selectPayPlansByDate(Map map) ;

    /**
     * 查询提前结清的回款计划
     * @param orderId
     * @return
     */
    List<PaymentPlan> selectEarlyPayPlan(String orderId);

    /**
     * 查询还款期数
     * @param subjectNo
     * @return
     */
    int queryPayPlanCount(String subjectNo);
}
