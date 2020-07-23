package com.zdmoney.mapper;

import com.zdmoney.models.BusiCashTicketConfig;
import com.zdmoney.webservice.api.dto.welfare.BusiCashTicketConfigDto;

import java.util.List;
import java.util.Map;

/**
 * @date 2018-12-05 09:57:02
 */
public interface BusiCashTicketConfigMapper {

    List<BusiCashTicketConfig> queryBusiCashTicketConfig(Map<String, Object> paramsMap);

    int saveBusiCashTicketConfig(BusiCashTicketConfig busiCashTicketConfig);

    int updateBusiCashTicketConfig(BusiCashTicketConfig busiCashTicketConfig);

    int removeBusiCashTicketConfigById(long id);

    /**
     * 根据类型查询现金券
     */
    List<BusiCashTicketConfig> queryBusiCashTickets(String ticketType);

    int updateCashTicketStatus(BusiCashTicketConfigDto cashTicketConfig);

    List<BusiCashTicketConfigDto> getCashTicketList(Map paramsMap);

    int updateByPrimaryKeySelective(BusiCashTicketConfigDto cashTicketConfig);

    int insert(BusiCashTicketConfigDto cashTicketConfig);

    BusiCashTicketConfigDto selectByPrimaryKey(Long id);
}
