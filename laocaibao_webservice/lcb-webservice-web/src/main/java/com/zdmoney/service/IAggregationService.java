package com.zdmoney.service;

import com.zdmoney.webservice.api.common.dto.ResultDto;

import java.util.Map;

/**
 * Created by user on 2017/9/6.
 */
public interface IAggregationService {
    ResultDto<Integer> expireRechargeRequest();

    ResultDto<Integer> updateCanceledOrderStatus();

    ResultDto<Integer> updateExpiredProduct();
}
