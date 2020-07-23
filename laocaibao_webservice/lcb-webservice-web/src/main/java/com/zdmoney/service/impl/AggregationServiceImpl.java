package com.zdmoney.service.impl;

import com.google.common.collect.Maps;
import com.zdmoney.constant.Constants;
import com.zdmoney.mapper.order.BusiOrderMapper;
import com.zdmoney.mapper.trade.BusiTradeFlowMapper;
import com.zdmoney.models.product.BusiProduct;
import com.zdmoney.service.BusiProductService;
import com.zdmoney.service.IAggregationService;
import com.zdmoney.webservice.api.common.dto.ResultDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by user on 2017/9/6.
 */
@Service
@Slf4j
public class AggregationServiceImpl implements IAggregationService {

    @Autowired
    private BusiTradeFlowMapper busiTradeFlowMapper;

    @Autowired
    private BusiOrderMapper busiOrderMapper;

    @Autowired
    private BusiProductService busiProductService;

    @Override
    public ResultDto<Integer> expireRechargeRequest() {
        Map<String,Object> map =new HashMap<String,Object>();
        map.put("status", Constants.Account.STATUS_FAILED);
        map.put("tradeType", Constants.Account.TRD_TYPE_RECHARGE);
        map.put("currTime",new Date());
        ResultDto<Integer> resultDto = new ResultDto<>();
        try {
            int num = busiTradeFlowMapper.expireRechargeRequest(map);
            resultDto.setCode(ResultDto.SUCCESS_CODE);
            resultDto.setData(Integer.valueOf(num));
        }catch (Exception e){
            log.error(e.getMessage(),e);
            resultDto.setCode(ResultDto.ERROR_CODE);
            resultDto.setMsg(e.getMessage());
        }
        return resultDto;
    }

    @Override
    @Transactional
    public ResultDto<Integer> updateCanceledOrderStatus() {
        Map<String,Object> map = new HashMap<>();
        map.put("currTime",new Date());
        ResultDto<Integer> resultDto = new ResultDto<>();
        try {
            int num = busiOrderMapper.updateUnpaidOrderStatus(map);
            busiOrderMapper.updateUnpaidSubOrderStatus(map);
            resultDto.setCode(ResultDto.SUCCESS_CODE);
            resultDto.setData(Integer.valueOf(num));
        }catch(Exception e){
            log.error(e.getMessage(),e);
            throw new RuntimeException(e.getMessage(),e);
        }
        return resultDto;
    }

    @Override
    @Transactional
    public ResultDto<Integer> updateExpiredProduct() {
        ResultDto<Integer> resultDto = new ResultDto<>();
        try {
            List<BusiProduct> productList = busiProductService.queryExpiredProduct();
            if(CollectionUtils.isNotEmpty(productList)){
                for (BusiProduct busiProduct:productList){
                    Map<String,Object> productMap = Maps.newHashMap();
                    productMap.put("productId",busiProduct.getId());
                    productMap.put("sortFlag","0");
                    productMap.put("isArea","0");
                    productMap.put("isRecommend","0");
                    busiProductService.updateMainSub(productMap);
                }
                resultDto.setData(Integer.valueOf(productList.size()));
            }else{
                resultDto.setData(0);
            }
            resultDto.setCode(ResultDto.SUCCESS_CODE);

        }catch(Exception e){
            log.error(e.getMessage(),e);
            throw new RuntimeException(e.getMessage(),e);
        }
        return resultDto;
    }
}
