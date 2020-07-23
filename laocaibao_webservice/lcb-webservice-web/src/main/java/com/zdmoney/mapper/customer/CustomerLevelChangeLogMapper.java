package com.zdmoney.mapper.customer;

import com.zdmoney.webservice.api.dto.customer.CustomerLevelChangeLogDto;

/**
 * Created by pc05 on 2018/2/5.
 */
public interface CustomerLevelChangeLogMapper {

    int saveCustomerLevelChangeLog(CustomerLevelChangeLogDto customerLevelChangeLogDto);
}
