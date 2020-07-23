package com.zdmoney.webservice.api.facade;

import com.zdmoney.webservice.api.common.dto.PageResultDto;
import com.zdmoney.webservice.api.common.dto.ResultDto;
import com.zdmoney.webservice.api.dto.finance.BusiOrderDto;
import com.zdmoney.webservice.api.dto.finance.BusiOrderSumDto;
import com.zdmoney.webservice.api.dto.plan.*;

import java.util.List;
import java.util.Map;

public interface IDataOrderService {

    /**
     * 查询起息订单
     *
     * @param orderReqDTO
     * @return
     */
    PageResultDto<DataOrderDTO> getTransferOrderData(DataOrderReqDTO orderReqDTO);

    /**
     * 回款计划
     *
     * @param orderReqDTO
     * @return
     */
    PageResultDto<DataRepayplanDTO> getRepayPlanList(DataOrderReqDTO orderReqDTO);

    /**
     * 订单回款完成
     *
     * @param orderReqDTO
     * @return
     */
    PageResultDto<DataRepayOrderDTO> getRepayCompleteOrderList(DataOrderReqDTO orderReqDTO);

    /**
     * 订单完成
     *
     * @param orderReqDTO
     * @return
     */
    PageResultDto<DataOrderInfoDTO> getInvestOverList(DataOrderReqDTO orderReqDTO);


    /**
     * 获得续投订单
     * @param paramsMap
     * @return
     */
    PageResultDto<BusiOrderDto> getReinvestOrderList(Map<String,Object> paramsMap);

    /**
     * 查询续投订单汇总
     * @param paramsMap
     * @return
     */
    PageResultDto<BusiOrderSumDto> getReinvestOrderSum(Map<String,Object> paramsMap);

    /**
     * 获得续投订单的总计金额
     * @param paramsMap
     * @return
     */
    ResultDto<BusiOrderSumDto> getReinvestOrderTotal(Map<String,Object> paramsMap);

    /**
     *  续投订单汇总总资产
     * @param paramsMap
     * @return
     */
    ResultDto<BusiOrderSumDto> getReinvestOrderSumTotal(Map<String,Object> paramsMap);
}
