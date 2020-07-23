/*
* Copyright (c) 2015 zendaimoney.com. All Rights Reserved.
*/  
package com.zdmoney.service;

import com.zdmoney.vo.BusiRebateVo;
import com.zdmoney.webservice.api.dto.ym.BusiRebateDto;

import java.util.List;
import java.util.Map;

public interface BusiRebateService {
    /**
     * 查询客户某月的返利记录
     * @param map  key=customerId(Long)
     *                 yearMonth(String) example:"201506"
     * @return map中封装的是用户ID及返利金额
     */
    List<BusiRebateVo> selectMonRebatesDetails(Map<String,Object>  map);

    List<BusiRebateVo> getRebateInfo(BusiRebateDto busiRebateDto);
}
 