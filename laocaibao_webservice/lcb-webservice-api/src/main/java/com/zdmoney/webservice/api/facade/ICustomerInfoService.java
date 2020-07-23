package com.zdmoney.webservice.api.facade;


import com.zdmoney.webservice.api.common.dto.PageResultDto;
import com.zdmoney.webservice.api.dto.customer.*;

/**
 * 用户信息接口
 */
public interface ICustomerInfoService {

    /**
     * 根据注册时间获得用户
     */
    PageResultDto<RegisterNotifyDto> getCustomerInfoByRegistNotify(CustomerReqDto customerReqDto);


    /**
     * 根据实名时间获得用户
     *
     * @return
     */
    PageResultDto<NameCertificateNotifyDto> getNameCertificateNotify(CustomerReqDto customerReqDto);


    /**
     * 根据风险评估时间获得用户
     *
     * @return
     */
    PageResultDto<RiskEvaluateNotifyDto> getRiskEvaluateNotify(CustomerReqDto customerReqDto);


    /**
     * 根据用户绑卡时间获得用户
     *
     * @return
     */
    PageResultDto<BankCardBindNotifyDto> getBindCardRecord(CustomerReqDto customerReqDto);

    /**
     * 根据解绑卡时间获得用户
     *
     * @return
     */
    PageResultDto<BusiUnbindRecordDto> getUnbindCardRecord(CustomerReqDto customerReqDto);


    /**
     * 根据用户注销时间获得用户
     *
     * @return
     */
    PageResultDto<RecordDto> getCloseAccountInfo(CustomerReqDto customerReqDto);


    /**
     * 根据用户变更手机号时间获得用户
     */
    PageResultDto<BusiUnbindRecordDto> getPhoneBindModify(CustomerReqDto customerReqDto);


    /**
     * 根据用户微信绑定时间获得用户
     */
    PageResultDto<RecordDto> getWeChatBindNotify(CustomerReqDto customerReqDto);

}
