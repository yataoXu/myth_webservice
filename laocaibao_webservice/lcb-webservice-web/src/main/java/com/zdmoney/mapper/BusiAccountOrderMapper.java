package com.zdmoney.mapper;

import java.util.List;
import java.util.Map;

import com.zdmoney.models.BusiAccountOrder;

public interface BusiAccountOrderMapper {
    int insert(BusiAccountOrder record);

    int insertSelective(BusiAccountOrder record);

    BusiAccountOrder selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BusiAccountOrder record);

    int updateByPrimaryKey(BusiAccountOrder record);
    
    List<BusiAccountOrder> selectBusiAccountOrder(Map<String,Object> map);
}