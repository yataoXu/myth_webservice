/*
* Copyright (c) 2015 zendaimoney.com. All Rights Reserved.
*/  
package com.zdmoney.service.impl;

import com.github.pagehelper.PageHelper;
import com.zdmoney.mapper.BusiRebateMapper;
import com.zdmoney.service.BusiRebateService;
import com.zdmoney.vo.BusiRebateVo;
import com.zdmoney.webservice.api.dto.ym.BusiRebateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class BusiRebateServiceImpl implements BusiRebateService{
	
	@Autowired 
	BusiRebateMapper busiRebateMapper;

    /**
     * 查询客户某月的返利记录
     * @param map  key=customerId(Long)
     *                 yearMonth(String) example:"201506"
     * @return map中封装的是用户ID及返利金额
     */
    public List<BusiRebateVo> selectMonRebatesDetails(Map<String,Object>  map){
		return busiRebateMapper.selectMonRebatesDetails(map);
	}

	@Override
	public List<BusiRebateVo> getRebateInfo(BusiRebateDto busiRebateDto) {
		PageHelper.startPage(busiRebateDto.getPageNo(),busiRebateDto.getPageSize());
		return busiRebateMapper.getRebateInfo(busiRebateDto);
	}
}
 