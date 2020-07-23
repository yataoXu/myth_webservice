package com.zdmoney.mapper;

import com.zdmoney.common.mybatis.mapper.JdMapper;
import com.zdmoney.models.BusiProductToPay;

public interface BusiProductToPayMapper{

    /**
     * 根据产品id查询提前结清产品信息
     * @param productId
     * @return
     */
    BusiProductToPay getBusiProductToPay(Long productId);

}