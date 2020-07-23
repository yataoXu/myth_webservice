package com.zdmoney.mapper.customer;

import com.zdmoney.common.mybatis.mapper.JdMapper;
import com.zdmoney.models.customer.CustomerSign;
import org.apache.ibatis.annotations.Param;

public interface CustomerSignMapper extends JdMapper<CustomerSign, Long> {

    int countSign(CustomerSign sign);

    CustomerSign findOneByCmNumberOrderBySignDate(@Param("cmNumber") String cmNumber);
}