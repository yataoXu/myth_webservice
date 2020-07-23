package com.zdmoney.facade;

import com.zdmoney.assets.api.utils.BeanUtil;
import com.zdmoney.service.BusiOrderService;
import com.zdmoney.utils.ValidatorUtils;
import com.zdmoney.webservice.api.common.dto.PageResultDto;
import com.zdmoney.webservice.api.common.dto.ResultDto;
import com.zdmoney.webservice.api.dto.finance.BusiOrderDto;
import com.zdmoney.webservice.api.dto.finance.BusiOrderSumDto;
import com.zdmoney.webservice.api.dto.plan.*;
import com.zdmoney.webservice.api.facade.IDataOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 数据系统--订单
 */
@Slf4j
@Service("dataOrderService")
public class DataOrderFacadeService implements IDataOrderService {

    @Autowired
    private BusiOrderService orderService;

    @Override
    public PageResultDto<DataOrderDTO> getTransferOrderData(DataOrderReqDTO orderReqDTO) {
        ValidatorUtils.validate(orderReqDTO);
        Map<String, Object> map = BeanUtil.transBean2Map(orderReqDTO);
        return orderService.getTransferOrderData(map);
    }

    @Override
    public PageResultDto<DataRepayplanDTO> getRepayPlanList(DataOrderReqDTO orderReqDTO) {
        ValidatorUtils.validate(orderReqDTO);
        Map<String, Object> map = BeanUtil.transBean2Map(orderReqDTO);
        return orderService.getRepayPlanList(map);
    }

    @Override
    public PageResultDto<DataRepayOrderDTO> getRepayCompleteOrderList(DataOrderReqDTO orderReqDTO) {
        ValidatorUtils.validate(orderReqDTO);
        Map<String, Object> map = BeanUtil.transBean2Map(orderReqDTO);
        return orderService.getRepayCompleteOrderList(map);
    }

    @Override
    public PageResultDto<DataOrderInfoDTO> getInvestOverList(DataOrderReqDTO orderReqDTO) {
        ValidatorUtils.validate(orderReqDTO);
        Map<String, Object> map = BeanUtil.transBean2Map(orderReqDTO);
        return orderService.getInvestOverList(map);
    }

    @Override
    public PageResultDto<BusiOrderDto> getReinvestOrderList(Map<String, Object> paramsMap) {
            return orderService.getReinvestOrderList(paramsMap);
    }

    @Override
    public PageResultDto<BusiOrderSumDto> getReinvestOrderSum(Map<String, Object> paramsMap) {
        return  orderService.getReinvestOrderSum(paramsMap);
    }

    @Override
    public ResultDto<BusiOrderSumDto> getReinvestOrderTotal(Map<String, Object> paramsMap) {
        ResultDto<BusiOrderSumDto> resultDto = new ResultDto<>();
        try {
            BusiOrderSumDto orderTotal = orderService.getReinvestOrderTotal(paramsMap);
            resultDto.setData(orderTotal);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultDto.setCode(ResultDto.ERROR_CODE);
            resultDto.setMsg(e.getMessage());
        }
        return resultDto;
    }

    @Override
    public ResultDto<BusiOrderSumDto> getReinvestOrderSumTotal(Map<String, Object> paramsMap) {
        ResultDto<BusiOrderSumDto> resultDto = new ResultDto<>();
        try {
            BusiOrderSumDto orderTotal = orderService.getReinvestOrderSumTotal(paramsMap);
            resultDto.setData(orderTotal);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultDto.setCode(ResultDto.ERROR_CODE);
            resultDto.setMsg(e.getMessage());
        }
        return resultDto;
    }
}
