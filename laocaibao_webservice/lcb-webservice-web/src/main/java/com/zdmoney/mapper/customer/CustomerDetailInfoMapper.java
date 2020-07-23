package com.zdmoney.mapper.customer;

import com.zdmoney.models.customer.CustomerDetailInfo;

public interface CustomerDetailInfoMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CustomerDetailInfo record);

    int insertSelective(CustomerDetailInfo record);

    CustomerDetailInfo selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CustomerDetailInfo record);

    int updateByPrimaryKey(CustomerDetailInfo record);
}