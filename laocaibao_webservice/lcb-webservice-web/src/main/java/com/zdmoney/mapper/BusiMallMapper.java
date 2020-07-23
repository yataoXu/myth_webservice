package com.zdmoney.mapper;

import com.zdmoney.common.mybatis.mapper.JdMapper;
import com.zdmoney.models.BusiMall;

import java.util.List;
import java.util.Map;

public interface BusiMallMapper extends JdMapper<BusiMall, Long> {

    List<BusiMall> getProductList();

    int updateBusiMall(BusiMall busiMall);

    List<BusiMall> getProductListByType(Map<String, Object> map);
}