package com.zdmoney.mapper.order;

import java.util.Map;

import com.zdmoney.models.order.BusiOrderIntegral;

public interface BusiOrderIntegralMapper {
    int insert(BusiOrderIntegral record);
    BusiOrderIntegral selectByCondition(Map<String,String> map);
    int updateByCondition(BusiOrderIntegral record);
}