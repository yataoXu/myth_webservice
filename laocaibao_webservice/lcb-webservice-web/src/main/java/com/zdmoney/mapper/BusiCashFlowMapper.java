package com.zdmoney.mapper;

import com.zdmoney.models.BusiCashFlow;
import java.util.List;
import java.util.Map;

/**
 * @date 2018-12-05 16:09:12
 */
public interface BusiCashFlowMapper {

    List<BusiCashFlow> queryBusiCashFlow(Map<String, Object> paramsMap);

    int saveBusiCashFlow(BusiCashFlow busiCashFlow);

    int updateBusiCashFlow(BusiCashFlow busiCashFlow);

    int removeBusiCashFlowById(long id);
}
