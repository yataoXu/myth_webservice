package com.zdmoney.service;

import com.zdmoney.models.order.BusiOrderTemp;

import java.util.Map;

public interface BusiOrderTempService {
	int insert(BusiOrderTemp record);
	BusiOrderTemp selectByPrimaryKey(Long id);
	BusiOrderTemp selectViewByPrimaryKey(Long id);
	int updateByPrimaryKeySelective(BusiOrderTemp record);
    void updateOrderStatus(Long orderId, String originStatus, String needStatus);
	void updateOrderStatus(Integer OrderType,Long orderId, String originStatus, String needStatus);

	void updateOrderSub(Map map);

}
