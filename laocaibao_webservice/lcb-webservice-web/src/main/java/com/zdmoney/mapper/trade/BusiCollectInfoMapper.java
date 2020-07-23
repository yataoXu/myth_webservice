package com.zdmoney.mapper.trade;

import com.zdmoney.models.trade.BusiCollectInfo;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface BusiCollectInfoMapper extends Mapper<BusiCollectInfo> {


    int saveBusiCollectInfo(BusiCollectInfo record);

    List<BusiCollectInfo> selectBusiCollectInfoByParms(Map record);

}