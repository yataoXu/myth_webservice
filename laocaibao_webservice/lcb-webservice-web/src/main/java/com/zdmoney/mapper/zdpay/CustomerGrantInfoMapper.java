package com.zdmoney.mapper.zdpay;

import com.zdmoney.models.zdpay.UserGrantBO;

public interface CustomerGrantInfoMapper {

    int saveCustomerGrantInfo(UserGrantBO userGrantBO);

    UserGrantBO queryUserGrant(long customerId);

    int updateCustomerGrantInfo(UserGrantBO userGrantBO);

}
