package com.zdmoney.mapper.order;

import com.zdmoney.models.order.BusiOrderReinvestLog;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BusiOrderReinvestLogMapper{
    int insert(BusiOrderReinvestLog busiOrderReinvestLog);

    List<BusiOrderReinvestLog> selectBusiOrderReinvestLogByCustomerId(Long orderId);
}