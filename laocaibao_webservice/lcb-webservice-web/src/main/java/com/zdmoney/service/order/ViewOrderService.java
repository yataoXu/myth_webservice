package com.zdmoney.service.order;


import com.zdmoney.webservice.api.dto.ym.ViewOrderDto;
import com.zdmoney.webservice.api.dto.ym.vo.ViewOrderVo;

import java.util.List;

/**
 * Created by pc05 on 2017/11/22.
 */
public interface ViewOrderService {
    List<ViewOrderVo> getOrderInfo(ViewOrderDto viewOrderDto);
}
