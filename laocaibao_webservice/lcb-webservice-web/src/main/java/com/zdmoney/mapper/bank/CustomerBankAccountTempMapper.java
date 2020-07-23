package com.zdmoney.mapper.bank;

import com.zdmoney.models.bank.CustomerBankAccountTemp;

public interface CustomerBankAccountTempMapper {
    int deleteByPrimaryKey(Long id);

    int insert(CustomerBankAccountTemp record);

    int insertSelective(CustomerBankAccountTemp record);

    CustomerBankAccountTemp selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(CustomerBankAccountTemp record);

    int updateByPrimaryKey(CustomerBankAccountTemp record);
}