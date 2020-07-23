package com.zdmoney.service.impl;

import com.zdmoney.mapper.BusiCashRecordMapper;
import com.zdmoney.models.BusiCashRecord;
import com.zdmoney.service.BusiCashRecordService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * @date 2018-11-30 15:39:13
 */
public class BusiCashRecordServiceImpl implements BusiCashRecordService {

    @Autowired
    private BusiCashRecordMapper busiCashRecordMapper;

    public List<BusiCashRecord> queryBusiCashRecord(Map<String, Object> paramsMap){
        return busiCashRecordMapper.queryBusiCashRecord(paramsMap);
    }

    public int updateBusiCashRecord(BusiCashRecord busiCashRecord){
        return busiCashRecordMapper.updateBusiCashRecord(busiCashRecord);
    }

    public int saveBusiCashRecord(BusiCashRecord busiCashRecord){
        return busiCashRecordMapper.saveBusiCashRecord(busiCashRecord);
    }

    public int removeBusiCashRecordById(long id){
        return busiCashRecordMapper.removeBusiCashRecordById(id);
    }
}
