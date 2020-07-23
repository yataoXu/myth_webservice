package com.zdmoney.mapper;

import com.zdmoney.models.CustomerRatingConfig;
import java.util.List;
import java.util.Map;

/**
 * @date 2019-01-07 19:19:06
 */
public interface CustomerRatingConfigMapper {

    List<CustomerRatingConfig> queryCustomerRatingConfig(Map<String, Object> paramsMap);

    int saveCustomerRatingConfig(CustomerRatingConfig customerRatingConfig);

    int updateCustomerRatingConfig(CustomerRatingConfig customerRatingConfig);

    int removeCustomerRatingConfigById(long id);

    /**
     * 根据用户等级查询对应等级配置
     *
     * @param ratingNum
     * @return
     */
    CustomerRatingConfig queryRatingConfigByRatingNum(Long ratingNum);

    /**
     * 查询用户等级配置信息
     * @return
     */
    List<CustomerRatingConfig> queryRatingConfigs();
}
