package com.zdmoney.mapper;

import java.util.Map;

import com.zdmoney.vo.BusiRebateStatisticsVo;

public interface BusiRebateStatisticsMapper {

	
	/**
	 * 根据客户ID查询上月累积返利和当月累积返利
	 * @param map
	 * @return
	 */
	public BusiRebateStatisticsVo selectTotalAmtById(Map<String,Object> map);
	
}
