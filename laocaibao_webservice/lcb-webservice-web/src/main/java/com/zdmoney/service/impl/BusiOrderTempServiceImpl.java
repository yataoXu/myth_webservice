package com.zdmoney.service.impl;

import com.zdmoney.constant.AppConstants;
import com.google.common.collect.Maps;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zdmoney.mapper.order.BusiOrderTempMapper;
import com.zdmoney.models.order.BusiOrderTemp;
import com.zdmoney.service.BusiOrderTempService;

import java.util.Date;
import java.util.Map;

@Service
@Slf4j
public class BusiOrderTempServiceImpl implements BusiOrderTempService {

	@Autowired
	BusiOrderTempMapper busiOrderTempMapper;
	
	public int insert(BusiOrderTemp record){
		return busiOrderTempMapper.insert(record);
	}
	
	public BusiOrderTemp selectByPrimaryKey(Long id){
		return busiOrderTempMapper.selectByPrimaryKey(id);
	}
	public BusiOrderTemp selectViewByPrimaryKey(Long id){
		return busiOrderTempMapper.selectViewByPrimaryKey(id);
	}
	
	public int updateByPrimaryKeySelective(BusiOrderTemp record){
		return busiOrderTempMapper.updateByPrimaryKeySelective(record);
	}



	public void updateOrderStatus(Long orderId, String originStatus, String needStatus) {
		log.info(">>>>>>>>>>更新订单状态开始，订单号：【{}】，原订单状态：【{}】", orderId, originStatus);
		Map map = Maps.newTreeMap();
		map.put("originStatus", originStatus);
		map.put("needStatus", needStatus);
		map.put("id", orderId);
		if(AppConstants.BusiOrderStatus.BUSIORDER_STATUS_0.equals(needStatus)){
			map.put("confirmDate", new Date());
		}
		int num = busiOrderTempMapper.updatePayStatus(map);
		int n = busiOrderTempMapper.updateOrderSubPayStatus(map);
		if (num == 1 && n == 1) {
			log.info(">>>>>>>>>>更新订单状态结束，订单号：【{}】，初始状态：【{}】，变更后状态【{}】", orderId, originStatus, needStatus);
		} else {
			log.info(">>>>>>>>>>更新订单状态失败，订单号：【{}】，原订单状态不为：【{}】", orderId, originStatus);
			throw new BusinessException("更新订单状态失败");
		}
	}

	public void updateOrderSub(Map map) {
		busiOrderTempMapper.updateOrderSub(map);
	}


	public void updateOrderStatus(Integer orderType,Long orderId, String originStatus, String needStatus) {
		log.info(">>>>>>>>>>更新订单状态开始，订单号：【{}】，原订单状态：【{}】", orderId, originStatus);
		Map map = Maps.newTreeMap();
		map.put("originStatus", originStatus);
		map.put("needStatus", needStatus);
		map.put("id", orderId);
		map.put("confirmDate", new Date());
		//锁定主订单
		if(orderType != AppConstants.OrderProductType.FINANCE_PLAN_SUB){
			int n1 = busiOrderTempMapper.updatePayStatus(map);
			if (n1 == 1) {
				log.info(">>>>>>>>>>更新订单状态结束，订单号：【{}】，初始状态：【{}】，变更后状态【{}】", orderId, originStatus, needStatus);
			} else {
				log.info(">>>>>>>>>>更新订单状态失败，订单号：【{}】，原订单状态不为：【{}】", orderId, originStatus);
				throw new BusinessException("更新订单状态失败");
			}
		}
		//锁定子订单
		int n2 = busiOrderTempMapper.updateOrderSubPayStatus(map);
		if (n2 ==1) {
			log.info(">>>>>>>>>>更新子订单状态结束，订单号：【{}】，初始状态：【{}】，变更后状态【{}】", orderId, originStatus, needStatus);
		} else {
			log.info(">>>>>>>>>>更新子订单状态失败，订单号：【{}】，原订单状态不为：【{}】", orderId, originStatus);
			throw new BusinessException("更新订单状态失败");
		}
	}
}
