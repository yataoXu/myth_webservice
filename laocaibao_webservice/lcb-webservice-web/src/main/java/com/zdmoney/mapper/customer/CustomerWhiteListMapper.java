package com.zdmoney.mapper.customer;

import com.zdmoney.models.customer.CustomerWhiteList;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CustomerWhiteListMapper extends Mapper<CustomerWhiteList> {

    List<CustomerWhiteList> selectByPhone(@Param("phone")String cellphone);

}