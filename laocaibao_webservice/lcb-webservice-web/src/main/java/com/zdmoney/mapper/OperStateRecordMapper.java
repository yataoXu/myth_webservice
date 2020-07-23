package com.zdmoney.mapper;

import com.zdmoney.models.OperStateRecord;

import java.util.Map;

/**
 * Created by user on 2018/8/21.
 */
public interface OperStateRecordMapper {

    int save(OperStateRecord record);

    int updateByCondition(OperStateRecord record);

    OperStateRecord selectByTypeAndKeyword(Map<String,Object> map);
}
