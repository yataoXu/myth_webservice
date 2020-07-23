package com.zdmoney.service;

import java.util.Map;

import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.financePlan.BusiOrderSub;
import com.zdmoney.models.order.BusiOrderIntegral;
import com.zdmoney.models.order.BusiOrderTemp;

public interface BusiOrderIntegralService {
	int insert(BusiOrderIntegral record);
    BusiOrderIntegral selectByCondition(Map<String,String> map);
    int updateByCondition(BusiOrderIntegral record);
    void refundAppointment(BusiOrderIntegral busiOrderIntegral, CustomerMainInfo mainInfo, BusiOrderSub busiOrder);
    void refundVoucher(String cumNumber, BusiOrderSub busiOrder);
}
