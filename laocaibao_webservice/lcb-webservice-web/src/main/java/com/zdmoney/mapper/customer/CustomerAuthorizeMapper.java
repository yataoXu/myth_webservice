package com.zdmoney.mapper.customer;

import com.zdmoney.models.customer.CustomerAuthorizeInfo;

import java.util.Map;

public interface CustomerAuthorizeMapper {

    CustomerAuthorizeInfo queryCustomerAuthorizeInfo(long customerId);

    long saveCustomerAuthorizeInfo(CustomerAuthorizeInfo customerAuthorizeInfo);

    long updateCustomerAuthorizeInfo(Map<String, Object> map);

}
