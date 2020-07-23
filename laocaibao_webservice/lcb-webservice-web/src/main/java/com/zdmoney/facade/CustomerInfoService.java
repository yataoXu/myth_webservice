package com.zdmoney.facade;

import com.zdmoney.assets.api.utils.BeanUtil;
import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.service.BankCardService;
import com.zdmoney.service.BusiRiskAssessService;
import com.zdmoney.service.CustomerMainInfoService;
import com.zdmoney.webservice.api.common.dto.PageResultDto;
import com.zdmoney.webservice.api.dto.customer.*;
import com.zdmoney.webservice.api.facade.ICustomerInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @ Author : Evan.
 * @ Description :
 * @ Date : Crreate in 2018/8/23 14:26
 * @Mail : xuyt@zendaimoney.com
 */

@Component
@Slf4j
public class CustomerInfoService implements ICustomerInfoService {

    @Autowired
    private CustomerMainInfoService customerMainInfoService;

    @Autowired
    private BankCardService bankCardService;

    @Autowired
    private BusiRiskAssessService busiRiskAssessService;

    @Autowired
    private ConfigParamBean configParamBean;

    @Override
    public PageResultDto<RegisterNotifyDto> getCustomerInfoByRegistNotify(CustomerReqDto customerReqDto) {
        Map<String, Object> map = BeanUtil.transBean2Map(customerReqDto);
        return customerMainInfoService.getCustomerInfoByRegistNotify(map);
    }

    @Override
    public PageResultDto<NameCertificateNotifyDto> getNameCertificateNotify(CustomerReqDto customerReqDto) {
        Map<String, Object> map = BeanUtil.transBean2Map(customerReqDto);
        return customerMainInfoService.getNameCertificateNotify(map);
    }

    @Override
    public PageResultDto<RiskEvaluateNotifyDto> getRiskEvaluateNotify(CustomerReqDto customerReqDto) {
        Map<String, Object> map = BeanUtil.transBean2Map(customerReqDto);
        return busiRiskAssessService.queryRiskEvaluateNotify(map);
    }

    @Override
    public PageResultDto<BankCardBindNotifyDto> getBindCardRecord(CustomerReqDto customerReqDto) {
        Map<String, Object> map = BeanUtil.transBean2Map(customerReqDto);
        return bankCardService.selectBindCardRecord(map);
    }


    @Override
    public PageResultDto<BusiUnbindRecordDto> getUnbindCardRecord(CustomerReqDto customerReqDto) {
        Map<String, Object> map = BeanUtil.transBean2Map(customerReqDto);
        return bankCardService.queryBankUnbindRecord(map);
    }

    @Override
    public PageResultDto<RecordDto> getCloseAccountInfo(CustomerReqDto customerReqDto) {
        Map<String, Object> map = BeanUtil.transBean2Map(customerReqDto);
        return customerMainInfoService.getCloseAccountInfo(map);
    }

    @Override
    public PageResultDto<BusiUnbindRecordDto> getPhoneBindModify(CustomerReqDto customerReqDto) {
        Map<String, Object> map = BeanUtil.transBean2Map(customerReqDto);
        return customerMainInfoService.queryPhoneBindModify(map);
    }

    @Override
    public PageResultDto<RecordDto> getWeChatBindNotify(CustomerReqDto customerReqDto) {
        Map<String, Object> map = BeanUtil.transBean2Map(customerReqDto);
        return customerMainInfoService.getWeChatBindNotify(map);
    }

}
