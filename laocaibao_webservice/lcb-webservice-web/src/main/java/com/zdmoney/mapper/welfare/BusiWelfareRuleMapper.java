package com.zdmoney.mapper.welfare;

import com.zdmoney.webservice.api.dto.welfare.BusiWelfareRuleDto;

import java.util.List;
import java.util.Map;

/**
 * @ Author : Evan.
 * @ Description :
 * @ Date : Crreate in 2018/12/5 9:10
 * @Mail : xuyt@zendaimoney.com
 */
public interface BusiWelfareRuleMapper {

    List<BusiWelfareRuleDto> getWelfareRuleList(Map<String, Object> map);

    int insert(BusiWelfareRuleDto cashTicketConfig);

    int updateByPrimaryKeySelective(BusiWelfareRuleDto cashTicketConfig);

    BusiWelfareRuleDto selectByPrimaryKey(Long id);

    List<BusiWelfareRuleDto> getWelfareByIds(Map<String, Object> map);
}

