package com.zdmoney.mapper.order;

import com.zdmoney.models.order.BusiOrderExchange;
import com.zdmoney.web.dto.ExchangeRecordDTO;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BusiOrderExchangeMapper extends Mapper<BusiOrderExchange> {

    int insertBusiOrderExchange(BusiOrderExchange busiOrderExchange);

    int updateBusiOrderExchange(BusiOrderExchange busiOrderExchange);

    List<ExchangeRecordDTO> getExchangeRecords(Long customerId);

    BusiOrderExchange  getBusiOrderExchangeInfo(String orderNum);
}