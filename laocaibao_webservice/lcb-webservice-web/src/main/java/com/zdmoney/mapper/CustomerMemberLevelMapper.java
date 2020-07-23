package com.zdmoney.mapper;

import com.zdmoney.models.customer.CustomerMemberLevel;

import java.util.List;
import java.util.Map;

/**
 * @date 2018-10-20 09:25:06
 */
public interface CustomerMemberLevelMapper {

    List<CustomerMemberLevel> queryCustomerMemberLevel(Map<String, Object> paramsMap);

    int saveCustomerMemberLevel(CustomerMemberLevel customerMemberLevel);

    int updateCustomerMemberLevel(CustomerMemberLevel customerMemberLevel);

    int removeCustomerMemberLevelById(long id);

    List<String> getCustomerIdByOrder();
}
