package com.zdmoney.service;

import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.webservice.api.common.dto.PageResultDto;
import com.zdmoney.webservice.api.common.dto.ResultDto;
import com.zdmoney.webservice.api.dto.customer.CustomerBankAccountVO;
import com.zdmoney.webservice.api.dto.customer.CustomerInfoVO;
import com.zdmoney.webservice.api.dto.customer.ExpandedCustomerInfoVO;

import java.util.Map;

/**
 * Created by user on 2017/9/5.
 */
public interface ICustomerInfoService {
    PageResultDto<CustomerInfoVO> getCustomerByParams(Map<String,Object> map);

    ResultDto<Integer> updateByCustomerId(CustomerMainInfo model);

    PageResultDto<ExpandedCustomerInfoVO> searchCustomersWithPlannerInfo(Map<String,Object> map);

    ResultDto<Integer> modifyCustomerMemberType(Long id);

    PageResultDto<ExpandedCustomerInfoVO> searchCustomersBankAccountInfo(Map<String,Object> map);

    ResultDto<ExpandedCustomerInfoVO> searchBankAccountDetail(Long id);

    ResultDto<Integer> updateCustomerBankAccount(CustomerBankAccountVO model);

    ResultDto<ExpandedCustomerInfoVO> selectOneCustomerBankAccount(Map<String,Object> map);

    ResultDto<CustomerInfoVO> selectOneCustomerInfo(String cmNumber);

    PageResultDto<CustomerInfoVO> getCustomerByNameAndPhone(Map<String,Object> map);

}
