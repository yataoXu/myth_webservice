package com.zdmoney.service;

import com.zdmoney.common.service.BaseService;
import com.zdmoney.mapper.MerchantRegisterRecordMapper;
import com.zdmoney.models.MerchantRegisterRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
* MerchantRegisterRecordService
* <p/>
* Author: jb sun
* Date: 2016-04-29 15:34:58
* Mail: sjb0223@hotmail.com
*/
@Service
public class MerchantRegisterRecordService extends BaseService<MerchantRegisterRecord, Long> {

    private MerchantRegisterRecordMapper getMerchantRegisterRecordMapper() {
        return (MerchantRegisterRecordMapper) baseMapper;
    }

    public boolean checkUniqueMerchantValidCode(String merchantNo,String validCode)
    {
        MerchantRegisterRecord condition = new MerchantRegisterRecord();
        condition.setMerchantNo(merchantNo);
        condition.setValidCode(validCode);
        List<MerchantRegisterRecord> registerRecords = findByEntity(condition);
//        Example example = new Example(MerchantRegisterRecord.class);
//        example.createCriteria().andCondition("MERCHANT_NO=",merchantNo).andCondition("VALID_CODE=",validCode);
        return registerRecords.size() > 0 ? true : false;
    }


}