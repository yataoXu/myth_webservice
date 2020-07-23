package com.zdmoney.mapper.bank;

import com.zdmoney.models.bank.CustomerBankAccount;
import com.zdmoney.webservice.api.dto.customer.BankCardBindNotifyDto;

import java.util.List;
import java.util.Map;

public interface CustomerBankAccountMapper {

    CustomerBankAccount selectByPrimaryKey(Long id);

    String selectAccountByCustomerId(Long customerId);
    
    int updateByCustomerId(CustomerBankAccount record);

    int updateByPrimaryKey(CustomerBankAccount record);

    int insert(CustomerBankAccount account);

    List<CustomerBankAccount> listCustBankAccount(Long customerId);

    CustomerBankAccount selectBankAccountInfo(Long customerId,String cbAccount);

    CustomerBankAccount selectBankAccountByBankCode(String bankCard);

    List<BankCardBindNotifyDto> selectBindCardRecord(Map<String,Object> map);

    CustomerBankAccount selectBankAccountByBankCodeIgnoreDeletion(String bankCard);

}