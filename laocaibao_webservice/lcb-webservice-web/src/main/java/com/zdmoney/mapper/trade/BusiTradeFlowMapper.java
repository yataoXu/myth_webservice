package com.zdmoney.mapper.trade;

import com.zdmoney.models.trade.BusiOperationDataStatistics;
import com.zdmoney.models.trade.BusiTradeConcentration;
import com.zdmoney.models.trade.BusiTradeFlow;
import com.zdmoney.webservice.api.dto.Asset.BusiTradeFlowDTO;
import com.zdmoney.webservice.api.dto.channel.WithdrawFlowsDto;
import com.zdmoney.webservice.api.dto.credit.wacai.WithdrawDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface BusiTradeFlowMapper {
    int insert(BusiTradeFlow record);

    BusiTradeFlow selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BusiTradeFlow record);

    List<BusiTradeFlow> selectByCondition(BusiTradeFlow tradeFlow);

    BigDecimal selectWithdrawBalance(Long customerId);

    int selectWithdrawTimesByCustomerId(Long customerId);

    String getRechargeStatusByFlowNum(String flowNum);

    BusiOperationDataStatistics findOperationDataStatistics();

    List<BusiTradeConcentration> findTradeConcentrationList(Long id);

    int expireRechargeRequest(Map<String,Object> map);

    BusiTradeFlow queryTradeFlow(String flowNum);

    BusiTradeFlow findTradeConcentrationByFlownum(String flowNum);

    List<BusiTradeFlowDTO> selectRecentTradeFlow(Map<String,Object> map);

    List<WithdrawFlowsDto> selectRecentTradeFlows(Long customerId);

    List<WithdrawDto> selectRecentTradeFlowsForWacai(Long customerId);

    List<BusiTradeFlow> selectByStatusTrddate(Map<String,Object> map);

    int updateStatusOfBusiTradeFlow(Map<String,Object> map);

}