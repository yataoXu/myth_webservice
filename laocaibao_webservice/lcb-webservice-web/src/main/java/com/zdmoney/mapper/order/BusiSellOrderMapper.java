package com.zdmoney.mapper.order;

import com.zdmoney.models.order.BusiSellOrder;

import java.util.List;

public interface BusiSellOrderMapper {

    List<BusiSellOrder> findSellOrderByOrderId(Long orderId, Long customerId);
}