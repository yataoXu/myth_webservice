package com.zdmoney.service;

import com.zdmoney.common.Result;
import com.zdmoney.common.ResultDto;
import com.zdmoney.models.financePlan.BusiOrderSub;
import com.zdmoney.models.order.BusiOrder;
import com.zdmoney.models.product.BusiProduct;
import com.zdmoney.models.product.BusiProductSub;
import com.zdmoney.models.zdpay.UserAuthDto;
import com.zdmoney.vo.*;
import com.zdmoney.webservice.api.dto.finance.QueryOrderReqDto;
import websvc.req.ReqMain;
import com.zdmoney.webservice.api.common.dto.PageResultDto;
import com.zdmoney.webservice.api.dto.finance.BusiOrderDto;
import com.zdmoney.webservice.api.dto.finance.BusiOrderSumDto;
import com.zdmoney.webservice.api.dto.plan.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface BusiOrderService {

    int insert(BusiOrder record);

    BusiOrder selectByPrimaryKey(Long id);

    BusiOrder selectByOrderNo(String orderNo);

    int updateByPrimaryKeySelective(BusiOrder record);

    List<BusiOrder> selectAllOrder(Map<String, Object> map);

    List<BusiOrder> selectOrderByProperty(Map<String, Object> map);

    List<BusiOrderProductVo> selectOrderProductByProperty(Map<String, Object> map);

    String buildCeNumber(String type, long custid);

    String buildCeNumber(String busiType, String type, long custid);

    BigDecimal calculationPrincipalinterest(BusiProduct busiProduct, BigDecimal orderAmt, BigDecimal addRate);

    BigDecimal calculationInterest(BigDecimal principal, BigDecimal rate, int numOfDays, BigDecimal addRate);

    List<OrderInterest> selectOrderTotalInterest(Map<String, Object> map);

    BigDecimal selectOrderNotStartInterest(Map<String, Object> map);

    DTO selectOrderEndInterest(Map<String, Object> map);

    List<BusiOrder> selectOrderByCustomerIdAndStatus(Map<String, Object> map);

    int selectInviteCustFirstInvestCount(String inviteCode, Long customerId);

    BigDecimal computePrincipleAndInterest(BusiProductSub busiProduct, BigDecimal orderAmt, BigDecimal addRate);

    BigDecimal computeDailyInterest(BusiProductSub busiProduct, BigDecimal orderAmt, BigDecimal addRate);

    HistorySaleVo selectHistorySale();

    BigDecimal selectYestodaySale(String datetime);

    List<BusiOrder> selectProductOrders(Map<String, Object> map);

    List<BusiOrder> selectOrderViewByProperty(Map<String, Object> map);

    /**
     * 查询某段时间的订单
     *
     * @param map
     * @return
     */
    List<BusiOrder> selectOrderPayByDate(Map<String, Object> map);

    /**
     * 查询用户某段时间的订单金额
     *
     * @param map
     * @return
     */
    List selectOrderPayTotalAmtByDate(Map<String, Object> map);

    /**
     * 查询用户给某段结息时间的订单
     *
     * @param map
     * @return
     */
    List<BusiOrder> selectOrderByCustomerIdAndDate(Map<String, Object> map);

    /**
     * 检查销量表是否存在该用户数据
     *
     * @param customerId
     * @return
     */
    Long findUpdateCustomerCount(Long customerId);

    void updateCustomerCount(Map<String, Object> map);

    void insertCustomerCount(Map<String, Object> map);

    int updateMainSub(Map<String, Object> map);

    /**
     * 查询理财计划产品订单
     *
     * @param paramsMap
     * @return
     */
    List<BusiOrder> queryFinancePlanOrderInfo(Map<String, Object> paramsMap);

    int updateOrderByIdAndStatus(Map<String, Object> paramsMap);

    /**
     * 查询用户订单关联产品是否提前结清
     *
     * @param paramsMap
     * @return
     */
    List<OrderProductExit> queryOrderProductExit(Map<String, Object> paramsMap);

    int updateMainAndSubOrderByCondition(BusiOrder record);
    /**
     * 数据系统---查询生效订单
     * 已交割的转让产品
     *
     * @param paramsMap
     * @return
     */
    PageResultDto<DataOrderDTO> getTransferOrderData(Map<String, Object> paramsMap);

    /**
     * 数据系统---查询生效订单
     * 个贷和理财计划产品
     *
     * @param paramsMap
     * @return
     */
    PageResultDto<DataRepayplanDTO> getRepayPlanList(Map<String, Object> paramsMap);

    /**
     * 订单回款完成
     *
     * @param paramsMap
     * @return
     */
    PageResultDto<DataRepayOrderDTO> getRepayCompleteOrderList(Map<String, Object> paramsMap);

    /**
     * 完成订单
     *
     * @param paramsMap
     * @return
     */
    PageResultDto<DataOrderInfoDTO> getInvestOverList(Map<String, Object> paramsMap);

    int updateMainAndSubOrderWithOrderNo(BusiOrder record);


    /**
     * 下单用户是否授权充足
     * @param customerId
     * @param productId
     * @param orderAmt
     * @return
     */
    Map userAuthJudge(Long customerId,Long productId,BigDecimal orderAmt);

    /**
     * 我的网贷,转让页面授权是否充足
     * authFlag :0 我的网贷  1：转让
     *
     */
    Map  userGrantFlag(Long customerId,int authFlag,BigDecimal transferFee);

    /**
     * 用户下单授权信息
     * @param customerId
     * @param productId
     * @param orderAmt
     * @return
     */
    UserAuthDto getOrderUserAuth(Long customerId, Long productId, BigDecimal orderAmt);



    /**
     * 获取用户在投散标、智投宝信息
     * @param customerId
     * @return
     */
    UserAuthOrderDto commonUserAuthOrder(Long customerId);

    /**
     * 获取标的借款用户待还信息
     * @param customerId
     * @return
     */
    UserAuthOrderDto borrowerAuthInfo(Long customerId, Date signDate, BigDecimal borrowAmt);

    /**
     * 统计再投订单金额
     * @param customerId
     * @return
     */
    BigDecimal statisticsOrderAmt(Long customerId);

    BusiOrderSumDto getReinvestOrderTotal(Map<String, Object> paramsMap);

    BusiOrderSumDto getReinvestOrderSumTotal(Map<String, Object> paramsMap);

    PageResultDto<BusiOrderSumDto> getReinvestOrderSum(Map<String, Object> paramsMap);

    PageResultDto<BusiOrderDto> getReinvestOrderList(Map<String, Object> paramsMap);

    void buyRemainingPartOfWacaiProduct();

    int saveSubOrders(List<BusiOrderSub> orders);

    PageResultDto<BusiOrderDto> queryMatchedOrders(QueryOrderReqDto reqDto);

    PageResultDto<BusiOrderDto> queryMainOrderInfo(QueryOrderReqDto reqDto);

    PageResultDto<BusiOrderDto> queryWacaiSubOrders(QueryOrderReqDto reqDto);

    void updateWacaiFundDetail();
}

