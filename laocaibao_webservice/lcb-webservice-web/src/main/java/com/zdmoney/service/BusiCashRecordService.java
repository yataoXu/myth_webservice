package com.zdmoney.service;

import com.zdmoney.models.BusiCashRecord;
import java.util.List;
import java.util.Map;

/**
 * @date 2018-11-30 15:39:13
 */
public interface BusiCashRecordService {

    List<BusiCashRecord> queryBusiCashRecord(Map<String, Object> paramsMap);

    int saveBusiCashRecord(BusiCashRecord busiCashRecord);

    int updateBusiCashRecord(BusiCashRecord busiCashRecord);

    int removeBusiCashRecordById(long id);

}

