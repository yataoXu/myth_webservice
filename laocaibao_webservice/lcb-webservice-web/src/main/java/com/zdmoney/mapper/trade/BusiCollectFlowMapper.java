package com.zdmoney.mapper.trade;

import com.zdmoney.models.trade.BusiCollectFlow;
import com.zdmoney.models.trade.BusiTradeFlowDetail;
import com.zdmoney.webservice.api.dto.plan.CollectOrderDetailDTO;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BusiCollectFlowMapper extends Mapper<BusiCollectFlow> {


    int saveBusiCollectFlow(List<CollectOrderDetailDTO>  record);
}