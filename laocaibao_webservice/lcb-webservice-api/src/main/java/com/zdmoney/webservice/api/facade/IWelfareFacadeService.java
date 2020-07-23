package com.zdmoney.webservice.api.facade;

import com.zdmoney.webservice.api.common.dto.ResultDto;
import com.zdmoney.webservice.api.dto.welfare.BusiCashTicketConfigDto;
import com.zdmoney.webservice.api.dto.welfare.BusiWelfareRuleDto;


import java.util.List;
import java.util.Map;

/**
 * @ Author : Evan.
 * @ Description : 福利相关业务接口
 * @ Date : Crreate in 2018/12/4 17:37
 * @Mail : xuyt@zendaimoney.com
 */
public interface IWelfareFacadeService {

    /**
     * 获得现金券
     * @param paramsMap
     * @return
     */
    ResultDto<List<BusiWelfareRuleDto>> getCashRuleList(Map paramsMap);

    ResultDto insertCashRule(BusiWelfareRuleDto busiWelfareRuleDto);

    ResultDto updateCashRule(BusiWelfareRuleDto busiWelfareRuleDto);

    ResultDto<BusiWelfareRuleDto> getCashRuleById(Long id);

    ResultDto<List<BusiCashTicketConfigDto>> getCashTicketList(Map map);

    ResultDto insertCashTicket(BusiCashTicketConfigDto cashTicketConfig);

    ResultDto updateCashTicketStatus(BusiCashTicketConfigDto  cashTicketConfig);

    ResultDto updateCashTicket(BusiCashTicketConfigDto cashTicketConfig);

    ResultDto<BusiCashTicketConfigDto> getCashTicketById(Long id);

    ResultDto getWelfareByIds (String ids);









}
