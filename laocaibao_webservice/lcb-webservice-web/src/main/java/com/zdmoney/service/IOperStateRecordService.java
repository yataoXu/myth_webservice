package com.zdmoney.service;

import com.zdmoney.models.OperStateRecord;

/**
 * Created by user on 2018/8/21.
 */
public interface IOperStateRecordService {
    int save(OperStateRecord record);

    int updateByCondition(OperStateRecord record);

    OperStateRecord selectByTypeAndKeyword(String type,String keyword);

    void saveOperFlow(String type,String keyword);
}
