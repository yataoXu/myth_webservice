package com.zdmoney.mapper;

import com.zdmoney.common.mybatis.mapper.JdMapper;
import com.zdmoney.models.BusiOrderToPay;
import com.zdmoney.models.BusiProductToPay;

public interface BusiOrderToPayMapper extends JdMapper<BusiOrderToPay,Long>{

    /**
     * 根据订单id查询提前结清订单信息
     * @param orderNo
     * @return
     */
    BusiOrderToPay getBusiOrdertToPay(String orderNo);

}