package com.zdmoney.service.impl;

import com.zdmoney.common.BusinessOperation;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.mapper.OperStateRecordMapper;
import com.zdmoney.models.OperStateRecord;
import com.zdmoney.service.IOperStateRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 2018/8/21.
 */
@Service
public class OperStateRecordServiceImpl implements IOperStateRecordService {

    @Autowired
    private OperStateRecordMapper operStateRecordMapper;

    @Override
    public int save(OperStateRecord record) {
        return operStateRecordMapper.save(record);
    }

    @Override
    public int updateByCondition(OperStateRecord record) {
        return operStateRecordMapper.updateByCondition(record);
    }

    @Override
    public OperStateRecord selectByTypeAndKeyword(String type, String keyword) {
        Map<String,Object> map = new HashMap<>();
        map.put("operType",type);
        map.put("keyword",keyword);
        return operStateRecordMapper.selectByTypeAndKeyword(map);
    }

    @Override
    public void saveOperFlow(String type, String keyword) {
        OperStateRecord record = new OperStateRecord();
        record.setOperType(BusinessOperation.SEND_RATING_UP_CREDIT.getOperType());
        record.setKeyword(keyword);
        try{
            operStateRecordMapper.save(record);
        }catch (Exception e){
            throw new BusinessException("wbs插入流水失败"+record);
        }
    }
}
