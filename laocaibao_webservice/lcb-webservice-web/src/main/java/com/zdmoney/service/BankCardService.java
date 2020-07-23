package com.zdmoney.service;

import com.zdmoney.common.Result;
import com.zdmoney.models.bank.BusiBankLimit;
import com.zdmoney.models.bank.CustomerBankAccount;
import com.zdmoney.models.customer.CustomerMainInfo;

import com.zdmoney.webservice.api.common.dto.PageResultDto;
import com.zdmoney.webservice.api.dto.customer.BankCardBindNotifyDto;
import com.zdmoney.webservice.api.dto.customer.BusiUnbindRecordDto;
import com.zdmoney.webservice.api.dto.customer.BusiUnbindRecordVO;
import com.zdmoney.webservice.api.dto.customer.RecordDto;
import websvc.req.ReqMain;

import java.util.Map;

/**
 * Created by 00225181 on 2015/12/3.
 * 2.0银行卡接口
 */
public interface BankCardService {

    /*充值绑卡3.0*/
    Result rechargeAmt(ReqMain reqMain) throws Exception;

    void unBindPhone(CustomerMainInfo mainInfo);

    /* 银行代码转换*/
    String changeBankCode(String channelCode, String bankCode);


    boolean checkBindCard(CustomerMainInfo customerMainInfo);

    BusiBankLimit getPayBank(String bankCode, String channelCode);

    /**
     * 查询解绑记录
     *
     * @param map
     * @return
     * @throws Exception
     */
    PageResultDto<BusiUnbindRecordVO> selectUnbindRecord(Map<String, Object> map);

    /**
     * 查询绑卡记录
     *
     * @param map
     * @return
     * @throws Exception
     */
    PageResultDto<BankCardBindNotifyDto> selectBindCardRecord(Map<String, Object> map);


    /**
     * 更新用户绑定银行卡表
     * 更新用户表
     *
     * @param bankAccount
     * @param mainInfo
     */
    void updateBankAccountAndCustomerInfo(CustomerBankAccount bankAccount, CustomerMainInfo mainInfo);

    /**
     * 存管改造-调用账户系统（华瑞）解绑银行卡接口
     *
     * @param bankAccount
     * @param mainInfo
     */
    void callHRServcie(CustomerBankAccount bankAccount, CustomerMainInfo mainInfo);

    /**
     * 保存解绑操作记录
     *
     * @param bankAccount
     * @param mainInfo
     * @param isStaffOperating
     */
    void persistUnbindingRecord(CustomerBankAccount bankAccount, CustomerMainInfo mainInfo, boolean isStaffOperating);

    /**
     * 查询解绑操作记录
     *
     * @param map
     * @return
     * @throws Exception
     */
    PageResultDto<BusiUnbindRecordDto> queryBankUnbindRecord(Map<String, Object> map);
    /**
     * 保存更改银行预留手机号操作记录
     * @param bankAccount
     * @param mainInfo
     */
    void persistUnbindBankPhoneRecord(CustomerBankAccount bankAccount,CustomerMainInfo mainInfo,String oldBindPhone);


}
