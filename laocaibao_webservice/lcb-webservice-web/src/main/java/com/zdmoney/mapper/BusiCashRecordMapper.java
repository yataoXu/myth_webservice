package com.zdmoney.mapper;

import com.zdmoney.models.BusiCashRecord;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @date 2018-11-30 15:39:13
 */
public interface BusiCashRecordMapper {

    List<BusiCashRecord> queryBusiCashRecord(Map<String, Object> paramsMap);

    int saveBusiCashRecord(BusiCashRecord busiCashRecord);

    int updateBusiCashRecord(BusiCashRecord busiCashRecord);

    int removeBusiCashRecordById(long id);

    /**
     * 查询5天之内的回款红包信息
     */
    BusiCashRecord  queryCashRecordByRepayDate(String cmNumber , Date expireDate);

    /**
     * 查询当月用户领取会员礼包信息
     */
    BusiCashRecord  queryMonthCashRecord(String cmNumber);


}