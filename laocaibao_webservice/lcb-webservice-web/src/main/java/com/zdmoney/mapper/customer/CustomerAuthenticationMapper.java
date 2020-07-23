package com.zdmoney.mapper.customer;

import com.zdmoney.models.customer.CustomerAuthentication;

public interface CustomerAuthenticationMapper {
    int insert(CustomerAuthentication record);

    int insertSelective(CustomerAuthentication record);
}