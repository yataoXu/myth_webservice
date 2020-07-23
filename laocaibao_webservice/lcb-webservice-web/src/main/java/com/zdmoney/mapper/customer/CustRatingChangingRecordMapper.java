package com.zdmoney.mapper.customer;

import com.zdmoney.models.customer.CustRatingChangingRecord;
import com.zdmoney.vo.CustRatingChangingVo;

import java.util.List;
import java.util.Map;

/**
 * @date 2019-01-02 23:47:00
 */
public interface CustRatingChangingRecordMapper {

    List<CustRatingChangingRecord> queryCustRatingChangingRecord(Map<String, Object> paramsMap);

    List<CustRatingChangingVo> queryCustRatingChangingVo(Map<String, Object> paramsMap);

    int saveCustRatingChangingRecord(CustRatingChangingRecord custRatingChangingRecord);

    int saveRecords(List<CustRatingChangingRecord> list);

    int updateByMap(Map<String, Object> params);
}
