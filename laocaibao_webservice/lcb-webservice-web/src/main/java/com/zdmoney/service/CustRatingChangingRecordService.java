package com.zdmoney.service;

import com.zdmoney.models.customer.CustRatingChangingRecord;
import java.util.List;
import java.util.Map;
/**
 * Created by user on 2019/1/2.
 */
public interface CustRatingChangingRecordService {

    List<CustRatingChangingRecord> queryCustRatingChangingRecord(Map<String, Object> paramsMap);

    int saveCustRatingChangingRecord(CustRatingChangingRecord custRatingChangingRecord);

    void checkAndUpdateCustomerRating(String ids);

    void updateCustomersAndSaveChangingRecords(List<Long> customerIds, Integer newRating, List<CustRatingChangingRecord> changingRecords);

    void sendCustomerRatingChangingMsg();
}
