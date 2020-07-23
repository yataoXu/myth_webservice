package com.zdmoney.mapper;

import com.zdmoney.webservice.api.dto.busi.BusiBrandDto;

import java.util.List;
import java.util.Map;

/**
 * @date 2019-03-15 17:24:07
 */
public interface BusiBrandMapper {

    BusiBrandDto getBusiBrand(String displayDate);

    List<BusiBrandDto> queryBusiBrand(Map<String, Object> paramsMap);

    int saveBusiBrand(BusiBrandDto busiBrand);

    int updateBusiBrand(BusiBrandDto busiBrand);

    int removeBusiBrandById(long id);

    BusiBrandDto selectById(Long id);
}
