package com.zdmoney.mapper.order;

import com.zdmoney.models.order.BusiOrderTemp;
import com.zdmoney.vo.CustomerInvestVO;

import java.util.List;
import java.util.Map;

public interface BusiOrderTempMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BusiOrderTemp record);

    BusiOrderTemp selectByPrimaryKey(Long id);
    
    BusiOrderTemp selectViewByPrimaryKey(Long id);

    BusiOrderTemp selectViewByOrderNo(String orderId);

    int updateByPrimaryKeySelective(BusiOrderTemp record);

    int updateByPrimaryKey(BusiOrderTemp record);

    int updateByOrderYearRateStr(BusiOrderTemp record);

    List<CustomerInvestVO> selectCustomerInvest4Task(Map<String,Object> map);

    int updatePayStatus(Map<String,Object> map);

    int updateOrderSubPayStatus(Map<String,Object> map);

    int updateOrderSub(Map<String,Object> map);
}