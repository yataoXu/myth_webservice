package com.zdmoney.mapper.customer;

import com.zdmoney.models.customer.CustRatingUpPresent;
import java.util.List;
import java.util.Map;

/**
 * @date 2019-01-02 23:46:51
 */
public interface CustRatingUpPresentMapper {

    List<CustRatingUpPresent> queryCustRatingUpPresent(Map<String, Object> paramsMap);

    int saveCustRatingUpPresent(CustRatingUpPresent custRatingUpPresent);

    int savePresents(List<CustRatingUpPresent> list);

    int updateByMap(Map<String, Object> paramsMap);
}
