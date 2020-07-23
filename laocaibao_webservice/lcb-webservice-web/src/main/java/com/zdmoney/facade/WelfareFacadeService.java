package com.zdmoney.facade;


import cn.hutool.core.util.ArrayUtil;
import com.google.common.collect.Maps;
import com.zdmoney.mapper.BusiCashTicketConfigMapper;
import com.zdmoney.mapper.welfare.BusiWelfareRuleMapper;
import com.zdmoney.webservice.api.common.dto.ResultDto;
import com.zdmoney.webservice.api.dto.welfare.BusiCashTicketConfigDto;
import com.zdmoney.webservice.api.dto.welfare.BusiWelfareRuleDto;
import com.zdmoney.webservice.api.facade.IWelfareFacadeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @ Author : Evan.
 * @ Description :
 * @ Date : Crreate in 2018/12/4 18:17
 * @Mail : xuyt@zendaimoney.com
 */

@Component
@Slf4j
public class WelfareFacadeService implements IWelfareFacadeService {

    @Autowired
    private BusiWelfareRuleMapper busiWelfareMapper;

    @Autowired
    private BusiCashTicketConfigMapper busiCashTicketConfigMapper;

    @Autowired
    private MessageSource messageSource;

    @Override
    public ResultDto<List<BusiWelfareRuleDto>> getCashRuleList(Map paramsMap) {
        ResultDto<List<BusiWelfareRuleDto>> resultDto = new ResultDto();
        List<BusiWelfareRuleDto> welfareRuleList;
        try {
            welfareRuleList = busiWelfareMapper.getWelfareRuleList(paramsMap);
            resultDto.setData(welfareRuleList);
            resultDto.setCode("0000");
        } catch (Exception e) {
            String msg = messageSource.getMessage(e.getMessage(), new Object[]{}, null);
            log.error("WelfareFacadeService获取现金福利规则失败:" + msg);
            resultDto.setMsg("获取现金福利规则失败:" + msg);
            resultDto.setCode("1111");
        }
        return resultDto;
    }

    @Override
    public ResultDto insertCashRule(BusiWelfareRuleDto busiWelfareRuleDto) {
        ResultDto resultDto = new ResultDto();
        int flag;
        try {
            flag = busiWelfareMapper.insert(busiWelfareRuleDto);
            if (flag != 0) {
                resultDto.setMsg("增加现金福利规则成功");
                resultDto.setCode("0000");
                return resultDto;
            }
        } catch (Exception e) {
            String msg = messageSource.getMessage(e.getMessage(), new Object[]{}, null);
            log.error("WelfareFacadeService增加现金福利规则失败:" + msg);
        }
        resultDto.setMsg("增加现金福利规则失败");
        resultDto.setCode("1111");
        return resultDto;
    }

    @Override
    public ResultDto updateCashRule(BusiWelfareRuleDto busiWelfareRuleDto) {
        ResultDto resultDto = new ResultDto();
        int flag;
        try {
            flag = busiWelfareMapper.updateByPrimaryKeySelective(busiWelfareRuleDto);
            if (flag != 0) {
                resultDto.setMsg("修改现金福利规则成功");
                resultDto.setCode("0000");
                return resultDto;
            }
        } catch (Exception e) {
            String msg = messageSource.getMessage(e.getMessage(), new Object[]{}, null);
            log.error("WelfareFacadeService修改现金福利规则失败:" + msg);
        }
        resultDto.setMsg("修改现金福利规则失败");
        resultDto.setCode("1111");
        return resultDto;

    }

    @Override
    public ResultDto getCashRuleById(Long id) {
        ResultDto<BusiWelfareRuleDto> resultDto = new ResultDto();
        BusiWelfareRuleDto busiWelfareRuleDto;
        try {
            busiWelfareRuleDto = busiWelfareMapper.selectByPrimaryKey(id);
            resultDto.setData(busiWelfareRuleDto);
            resultDto.setCode("0000");
        } catch (Exception e) {
            String msg = messageSource.getMessage(e.getMessage(), new Object[]{}, null);
            log.error("WelfareFacadeService获取现金福利规则失败:" + msg);
            resultDto.setMsg("获取现金福利规则失败");
            resultDto.setCode("1111");
        }
        return resultDto;
    }

    /**
     * 修改现金券状态
     *
     * @param
     * @return
     */
    @Override
    public ResultDto updateCashTicketStatus(BusiCashTicketConfigDto cashTicketConfig) {
        ResultDto resultDto = new ResultDto();
        int flag;
        try {
            flag = busiCashTicketConfigMapper.updateCashTicketStatus(cashTicketConfig);
            if (flag != 0) {
                resultDto.setMsg("更改现金福利状态成功");
                resultDto.setCode("0000");
                return resultDto;
            }
        } catch (Exception e) {
            String msg = messageSource.getMessage(e.getMessage(), new Object[]{}, null);
            log.error("WelfareFacadeService修改现金福利失败:" + msg);
        }
        resultDto.setMsg("更改现金福利状态成功");
        resultDto.setCode("1111");
        return resultDto;
    }

    @Override
    public ResultDto updateCashTicket(BusiCashTicketConfigDto cashTicketConfig) {
        ResultDto resultDto = new ResultDto();
        int flag;
        try {
            flag = busiCashTicketConfigMapper.updateByPrimaryKeySelective(cashTicketConfig);
            if (flag != 0) {
                resultDto.setMsg("修改现金福利成功");
                resultDto.setCode("0000");
                return resultDto;
            }
        } catch (Exception e) {
            String msg = messageSource.getMessage(e.getMessage(), new Object[]{}, null);
            log.error("WelfareFacadeService修改现金福利失败:" + msg);
        }
        resultDto.setMsg("修改现金福利失败");
        resultDto.setCode("1111");
        return resultDto;
    }

    @Override
    public ResultDto<BusiCashTicketConfigDto> getCashTicketById(Long id) {
        ResultDto<BusiCashTicketConfigDto> resultDto = new ResultDto();
        BusiCashTicketConfigDto cashTicketConfig;
        try {
            cashTicketConfig = busiCashTicketConfigMapper.selectByPrimaryKey(id);
            resultDto.setData(cashTicketConfig);
            resultDto.setCode("0000");
        } catch (Exception e) {
            String msg = messageSource.getMessage(e.getMessage(), new Object[]{}, null);
            log.error("WelfareFacadeService获取现金福利失败:" + msg);
            resultDto.setMsg("获取现金福利失败");
            resultDto.setCode("1111");
        }
        return resultDto;
    }

    @Override
    public ResultDto getWelfareByIds(String ids) {
        ResultDto resultDto = new ResultDto();
        if (StringUtils.isEmpty(ids)){
            resultDto.setMsg("获取现金福利失败");
            resultDto.setCode("1111");
            return resultDto;
        }

        int flag;
        try {
            Map<String, Object> paramsMap = Maps.newTreeMap();
            List<String> welfaceIdList = Arrays.asList(ids.split(","));
            paramsMap.put("welfaceIds",welfaceIdList);
            flag =  busiWelfareMapper.getWelfareByIds(paramsMap).size();
            if (flag == welfaceIdList.size()) {
                resultDto.setMsg("获取现金福利成功");
                resultDto.setCode("0000");
                return resultDto;
            }else{
                resultDto.setMsg("现金福利ID不存在");
                resultDto.setCode("1111");
                return resultDto;
            }
        } catch (Exception e) {
            String msg = messageSource.getMessage(e.getMessage(), new Object[]{}, null);
            log.error("WelfareFacadeService获取现金福利失败:" + msg);
            resultDto.setMsg("获取现金福利失败");
            resultDto.setCode("1111");
        }
        return resultDto;
    }

    /**
     * 获得所有的现金券
     *
     * @param paramsMap
     * @return
     */
    @Override
    public ResultDto<List<BusiCashTicketConfigDto>> getCashTicketList(Map paramsMap) {

        ResultDto<List<BusiCashTicketConfigDto>> resultDto = new ResultDto();
        List<BusiCashTicketConfigDto> cashTicketConfigList;
        try {
            cashTicketConfigList = busiCashTicketConfigMapper.getCashTicketList(paramsMap);
            resultDto.setData(cashTicketConfigList);
            resultDto.setCode("0000");
        } catch (Exception e) {
            String msg = messageSource.getMessage(e.getMessage(), new Object[]{}, null);
            log.error("WelfareFacadeService获取现金福利失败:" + msg);
            resultDto.setMsg("获取现金福利失败");
            resultDto.setCode("1111");
        }
        return resultDto;
    }

    @Override
    public ResultDto insertCashTicket(BusiCashTicketConfigDto cashTicketConfig) {
        ResultDto resultDto = new ResultDto();
        int flag;
        try {
            flag = busiCashTicketConfigMapper.insert(cashTicketConfig);
            if (flag != 0) {
                resultDto.setMsg("增加现金福利成功");
                resultDto.setCode("0000");
                return resultDto;
            }
        } catch (Exception e) {
            String msg = messageSource.getMessage(e.getMessage(), new Object[]{}, null);
            log.error("WelfareFacadeService增加现金福利失败:" + msg);
            resultDto.setMsg("增加现金福利失败");
            resultDto.setCode("1111");
        }
        return resultDto;
    }
}
