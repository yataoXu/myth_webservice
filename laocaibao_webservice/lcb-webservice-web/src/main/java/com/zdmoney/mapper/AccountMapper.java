package com.zdmoney.mapper;

import java.util.List;
import java.util.Map;

import com.zdmoney.models.CompanyBalance;
import com.zdmoney.models.CompanyLedger;
import com.zdmoney.models.CompanyRecord;
import com.zdmoney.models.customer.CustomerBalance;
import com.zdmoney.models.GeneralLedger;
import com.zdmoney.models.RecordDetail;

/**
 * 账户管理
 * @author yangp
 *
 */
public interface AccountMapper {

    /**
     * 生成流水明细
     * @param record
     * @return
     */
    int insertRecordDetail(RecordDetail record);
    
    /**
     * 产生用户余额
     * @param record
     * @return
     */
    int insertCustomerBalance(CustomerBalance record);
    
    /**
     * 生成总账记录
     * @param record
     * @return
     */
    int insertGeneralLedger(GeneralLedger record);

    /**
     * 查询总账记录
     * @param map
     * @return
     */
    List<GeneralLedger> selectGeneralLedger(Map<String,Object> map);
    
    /**
     * 查询客户最近总账记录
     * @param map
     * @return
     */
    List<GeneralLedger> selectLastLedger(Map<String,Object> map);
    
    /**
     * 查询用户余额
     * @param map
     * @return
     */
    CustomerBalance selectCustomerBalance(Map<String,Object> map);
    
    /**
     * 是否已操作过客户账户
     * @param map
     * @return
     */
    int exsitsCuOrder(Map<String,Object> map);
    
    
    /**
     * 解冻金额是否等于冻结金额
     * @param map
     * @return
     */
    String getBalance(Map<String,Object> map);
    
    /**
     * 是否已操作过公司账户
     * @param map
     * @return
     */
    int exsitsCoOrder(Map<String,Object> map);

    /**
     * 更新总账记录
     * @param record
     * @return
     */
    int updateGeneralLedger(GeneralLedger record);
    
    /**
     * 更新客户余额
     * @param record
     * @return
     */
    int updateCustomerBalance(CustomerBalance record);
    /**
     * 根据客户编号更新客户余额
     * @param record
     * @return
     */
    int updateCustomerBalanceByCustomerId(CustomerBalance record);
    
    List<RecordDetail> selectRecordDetail(Map<String,Object>  map);
    
    /**
     * 生成公司流水明细
     * @param record
     * @return
     */
    int insertCompanyRecord(CompanyRecord record);
    
    /**
     * 产生公司余额
     * @param record
     * @return
     */
    int insertCompanyBalance(CompanyBalance record);
    
    /**
     * 生成公司总账记录
     * @param record
     * @return
     */
    int insertCompanyLedger(CompanyLedger record);
    
    /**
     * 查询公司总账记录
     * @param map
     * @return
     */
    CompanyLedger selectCompanyLedger(Map<String,Object> map);
    
    /**
     * 查询公司最近总账记录
     * @param map
     * @return
     */
    CompanyLedger selectCompanyLastLedger(Map<String,Object> map);
    
    /**
     * 查询公司余额
     * @param map
     * @return
     */
    CompanyBalance selectCompanyBalance(Map<String,Object> map);
    
    /**
     * 更新公司总账记录
     * @param record
     * @return
     */
    int updateCompanyLedger(CompanyLedger record);
    
    /**
     * 更新公司余额
     * @param record
     * @return
     */
    int updateCompanyBalance(CompanyBalance record);
    
}