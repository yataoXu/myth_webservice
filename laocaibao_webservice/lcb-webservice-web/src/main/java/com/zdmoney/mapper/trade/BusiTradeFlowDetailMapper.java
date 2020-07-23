package com.zdmoney.mapper.trade;

import com.zdmoney.models.trade.BusiTradeFlowDetail;

public interface BusiTradeFlowDetailMapper {
    int insert(BusiTradeFlowDetail record);
    BusiTradeFlowDetail selectByCondition(BusiTradeFlowDetail detail);
    int updateById(BusiTradeFlowDetail detail);
}