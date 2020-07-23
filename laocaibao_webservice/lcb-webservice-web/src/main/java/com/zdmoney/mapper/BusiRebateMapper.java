/*
* Copyright (c) 2015 zendaimoney.com. All Rights Reserved.
*/  
package com.zdmoney.mapper;

import com.zdmoney.vo.BusiRebateVo;
import com.zdmoney.webservice.api.dto.ym.BusiRebateDto;

import java.util.List;
import java.util.Map;

public interface BusiRebateMapper {
	
    List<BusiRebateVo> selectMonRebatesDetails(Map<String,Object> map);

    /**
     * 查询返利信息
     * @return
     */
    List<BusiRebateVo> getRebateInfo(BusiRebateDto busiRebateDto);
}
 