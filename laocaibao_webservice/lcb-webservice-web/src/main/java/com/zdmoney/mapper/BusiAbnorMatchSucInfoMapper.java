package com.zdmoney.mapper;

import com.zdmoney.models.BusiAbnorMatchSucInfo;
import com.zdmoney.models.BusiMatchSucInfo;

import java.util.List;
import java.util.Map;

public interface BusiAbnorMatchSucInfoMapper {

    List<BusiAbnorMatchSucInfo> selectByMap(Map<String,Object> map);

    void insert(BusiAbnorMatchSucInfo busiAbnorMatchSucInfo);

    List<BusiAbnorMatchSucInfo> selectByMasterId(Long masterId);

}
