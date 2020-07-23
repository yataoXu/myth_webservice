package com.zdmoney.service;

import com.zdmoney.models.customer.CustRatingUpPresent;
import java.util.List;
import java.util.Map;
/**
 * Created by user on 2019/1/2.
 */
public interface CustRatingUpPresentService {

    List<CustRatingUpPresent> queryCustRatingUpPresent(Map<String, Object> paramsMap);

    int saveCustRatingUpPresent(CustRatingUpPresent custRatingUpPresent);

    int savePresents(List<CustRatingUpPresent> presents);

    void sendRatingUpPresents();
}
