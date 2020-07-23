package com.zdmoney.mapper;

import com.zdmoney.models.BusiMatchSucInfo;

import java.util.List;
import java.util.Map;

public interface BusiMatchSucInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(BusiMatchSucInfo record);

    BusiMatchSucInfo selectByPrimaryKey(Long id);

    List<BusiMatchSucInfo> selectByMap(Map<String,Object> map);

    int updateByPrimaryKey(BusiMatchSucInfo record);

    int updateByMap(Map<String,Object> map);

    List<BusiMatchSucInfo> selectByMasterId(Long masterId);

}