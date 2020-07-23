package com.zdmoney.mapper.order;

import com.zdmoney.models.flw.Orders;
import com.zdmoney.models.order.BusiOrder;
import com.zdmoney.models.order.BusiOrderSubVo;
import com.zdmoney.vo.*;
import com.zdmoney.webservice.api.dto.finance.BusiOrderDto;
import com.zdmoney.webservice.api.dto.finance.BusiOrderSumDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface BusiOrderMapper {

    int insert(BusiOrder record);

    BusiOrder selectByPrimaryKey(Long id);

    BusiOrder selectByOrderNo(String orderNo);

    BusiOrderProductVo selectOrderProductByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BusiOrder record);

    List<BusiOrder> selectAllOrder(Map<String,Object> map);
    
    List<BusiOrder> selectOrderByProperty(Map<String,Object> map);
    
    List<BusiOrder> selectOrderViewByProperty(Map<String,Object> map);
    
    List<BusiOrder> selectOrderByCustomerId(Map<String,Object> map);

    List<BusiOrder> selectOrdersByProperties(Map<String,Object> map);
    
    List<BusiOrderProductVo> selectOrderProductByProperty(Map<String,Object> map);
    
    List<OrderInterest> selectOrderTotalInterest(Map<String,Object> map);
    
    BigDecimal selectOrderNotStartInterest(Map<String,Object> map);
    
    DTO selectOrderEndInterest(Map<String,Object> map);
    
     BankAccountInterest selectOrderBankAccount(Long customerId);
     
     int updateByPrimaryKeySelectiveByOrderNum(BusiOrder record);
     
     List<BusiOrder> selectOrderByCustomerIdAndStatus(Map<String,Object> map);
    
     List<BusiOrder> selectOrders(Map<String,Object> map);

     UserAssetIntstAndTtlAmtVo selectEffectiveOrderAmtByCustomerID(Map<String,Object> map);

     UserAssetIntstAndTtlAmtVo selectEffectiveOrderAmtByCustomerIdAndTime(Map<String,Object> map);

    BigDecimal selectYesterdayIncome(Map<String,Object> map);

    List<UserAssetVo> selelctAssetByCustomerIdAndStatus(Map<String,Object> map);

    BigDecimal selectTotalIncomeUnLocked(Map<String,Object> map);

    List<BusiOrder> selectLockedOrders(Map<String,Object> map);

    int selectInviteCustFirstInvestCount(Map<String,Object> map);

    UserRepaymentVo selectRepaymentByCustomerID(Map<String,Object> map );

    UserRepaymentVo selectRepaymentByCustomerIDAndTime(Map<String,Object> map );

    /*统计用户持有资产及待收利息v4.1*/
    UserRepaymentVo selectRepaymentByCustomerIdAndOrderNum(Map<String,Object> map );

    HistorySaleVo selectHistorySale();

    BigDecimal selectYestodaySale(String datetime);


    /**
     * 获取订单产品转让失败的记录（1天以内）
     */
    List<UserAssetVo> selectTransferFailRecord(Map<String,Object> map);

    List<BusiOrder>selectProductOrders(Map<String,Object> map);

    int selectProductBuyTimes(Map<String,Object> map);


    /**
     * 查询某段时间的订单(重复下单不记录)
     * @param map
     * @return
     */
    List<BusiOrder> selectOrderPayByDate(Map<String,Object> map);

    /**
     * 查询用户某段时间的订单金额
     * @param map
     * @return
     */
    List selectOrderPayTotalAmtByDate(Map<String,Object> map);

    /**
     * 查询用户给某段结息时间的订单
     * @param map
     * @return
     */
    List<BusiOrder> selectOrderByCustomerIdAndDate(Map<String, Object> map);

    /**
     * 检查销量表是否存在该用户数据
     * @param customerId
     * @return
     */
    Long findUpdateCustomerCount(Long customerId);

    void updateCustomerCount(Map<String, Object> map);

    void insertCustomerCount(Map<String, Object> map);

    int updateMainSub(Map<String, Object> map);

    /**
     * 查询理财计划产品订单
     * @param paramsMap
     * @return
     */
    List<BusiOrder> queryFinancePlanOrderInfo(Map<String, Object> paramsMap);

    int updateOrderByIdAndStatus(Map<String, Object> paramsMap);

    /**
     * 更新未付款的主订单状态
     * @param map
     * @return
     */
    int updateUnpaidSubOrderStatus(Map<String, Object> map);

    /**
     * 更新未付款的子订单状态
     * @param map
     * @return
     */
    int updateUnpaidOrderStatus(Map<String, Object> map);

    /*用户历史资产红包积分累计收益v4.1*/
    UserUnReceiveAsset getTotalCouponAndIntegralAmt(Map<String,Object> map);

    /*用户待收加息收益累计v4.1*/
    UserUnReceiveAsset getNoRecieveAmt(Map<String,Object> map);

    /**
     * 查询用户订单关联产品是否提前结清
     * @param paramsMap
     * @return
     */
    List<OrderProductExit> queryOrderProductExit(Map<String, Object> paramsMap);

    /**
     * 数据系统---查询生效订单
     * @param paramsMap
     * @return
     */
    List<BusiOrder> getActivateOrder(Map<String, Object> paramsMap);

    List<BusiOrder> getTransferOrderData(Map<String, Object> paramsMap);

    /**
     * 回款完成订单
     * @param paramsMap
     * @return
     */
    List<BusiOrderSubVo> getRepayCompleteOrderList(Map<String, Object> paramsMap);

    /**
     * 完成订单
     * @param paramsMap
     * @return
     */
    List<BusiOrder> getInvestOverList(Map<String, Object> paramsMap);


    /**
     * 理财计划昨日收益
     * @param map
     * @return
     */
    BigDecimal yesterdayFinPlanIncome(Map<String,Object> map);

    List<BusiOrderProductVo> getContinuedOrderList(Map<String, Object> paramsMap);

    int selectContinuedorder(Map<String,Object> map);

    /**
     * 根据产品id获取订单最大的结息日
     */
    BusiOrder getMaxInterestEndDate(Long  productId);

    BusiOrder getOrderInfo(String  orderId);

    /**
     * 返利网查询
     * @param paramsMap
     * @return
     */
    Orders getOrderListForFlw(Map<String, Object> paramsMap);

    BusiOrderSumDto selectReinvestOrderTotal(Map<String, Object> paramsMap);

    List<BusiOrderDto> selectReinvestOrderList(Map<String, Object> paramsMap);

    /**
     * 订单汇总总资产
     * @param map
     * @return
     */
    BusiOrderSumDto selectReinvestOrderSumTotal(Map<String, Object> map);

    /**
     * 查询续投订单汇总
     * @param map
     * @return
     */
    List<BusiOrderSumDto> selectReinvestOrderSum(Map<String, Object> map);

    List<BusiOrderDto> queryMatchedOrders(Map<String, Object> map);
}