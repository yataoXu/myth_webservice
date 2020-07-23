package com.zdmoney.mapper.financePlan;


import com.zdmoney.models.financePlan.BusiOrderSub;
import com.zdmoney.models.order.BusiOrder;
import com.zdmoney.models.order.BusiOrderSubVo;
import com.zdmoney.models.order.BusiOrderTemp;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BusiOrderSubMapper {

    int insert(BusiOrderSub record);

    int insertWithId(BusiOrderSub record);

    BusiOrderSub selectByPrimaryKey(Long id);

    /**
     * 查询子订单信息
     * @param map
     * @return
     */
    List<BusiOrderSub> queryOrderSubInfo(Map<String,Object> map);

    /**
     * 根据订单号查询子单详情
     * @param orderNo
     * @return
     */
    BusiOrderSub queryBusiOrderSubInfo(@Param("orderNo") String orderNo);



    int updateByPrimaryKeySelective(BusiOrderSub record);

    int updateByOrderYearRateStr(BusiOrderSub orderTemp);

    /**
     * 根据子订单id查询子订单信息
     * @param parentId
     * @return
     */
    List<BusiOrderSub> queryOrderByParentId(Long parentId);

    /**
     * 批量更新子单状态
     * @param orderList
     * @return
     */
    int batchUpdateOrderSubStatus(List<Long> orderList);

    List<BusiOrderSub> selectByMap(Map<String,Object> map);

    int updateOrderByIdAndStatus(Map<String, Object> paramsMap);

    int batchUpdateOrderStatus(List<Long> orderList);

    /**
     * 查询正在撮合中的订单
     * @param mainOrderId
     * @return
     */
    long queryFundByParentId(Long mainOrderId);


    List<BusiOrderSub> queryOrderSubInfoByBidnoAndStatus(Map<String, Object> paramsMap);

    /**
     * 修改订单放款状态
     * @param paramsMap
     * @return
     */
    int updateOrderLoanStatusByIdAndStatus(Map<String, Object> paramsMap);


    /**
     * 根据标的号查询借款人存管ID
     * @param bidNo
     * @return
     */
    String queryLoanLoginIdByBidNo(String bidNo);

    int updateByPrimaryKeySelectiveByOrderNum(BusiOrder record);


    List<BusiOrderSubVo> selectRepayplanDetail(String orderNum);


    int insertOrders(List<BusiOrderSub> list);

    List<BusiOrderSub> queryByConditions(Map<String, Object> conditions);

    List<BusiOrderSub> queryBusiOrderSubInfoBySubjectNoStatus(Map<String, Object> paramsMap);




}