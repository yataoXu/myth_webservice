package com.zdmoney.mapper;

import com.zdmoney.models.BusiMatchResultInfo;

import java.util.List;
import java.util.Map;

public interface BusiMatchResultInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BusiMatchResultInfo record);

    int insertSelective(BusiMatchResultInfo record);

    BusiMatchResultInfo selectByPrimaryKey(Long id);

    List<BusiMatchResultInfo> selectByMap(Map<String,Object> map);

    List<BusiMatchResultInfo> selectBusiMatchUnionByMap(Map<String,Object> map);

    int updateByPrimaryKeySelective(BusiMatchResultInfo record);

    int updateByPrimaryKey(BusiMatchResultInfo record);

    int updateStatusByInitStatus(Map<String,Object> map);

}