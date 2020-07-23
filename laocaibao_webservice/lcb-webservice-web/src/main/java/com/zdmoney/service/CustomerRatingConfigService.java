package com.zdmoney.service;

import com.zdmoney.models.CustomerRatingConfig;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
/**
 * Created by user on 2019/1/2.
 */
public interface CustomerRatingConfigService {
    List<CustomerRatingConfig> queryCustomerRatingConfig(Map<String, Object> paramsMap);

    int updateCustomerRatingConfig(CustomerRatingConfig customerRatingConfig);

    CustomerRatingConfig findByRatingNum(Integer ratingNum);

    CustomerRatingConfig findRatingByInvestingAmt(BigDecimal investingAmt);

    List<CustomerRatingConfig> findConfigsBetweenTwoRatings(Integer minRatingNum, Integer maxRatingNum);

    List<CustomerRatingConfig> findInclusiveConfigsBetweenTwoRatings(Integer minRatingNum, Integer maxRatingNum);

    List<CustomerRatingConfig> findAll();

    List<CustomerRatingConfig> findAllNoCache();
}
