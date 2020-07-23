package com.zdmoney.service;

import com.zdmoney.models.customer.CustomerMainInfo;

import java.util.Map;

/**
 * Created by 46186 on 2018/10/20.
 */
public interface CustomerMemberLevelService {
    Map<String,String> queryMemberLevel(CustomerMainInfo mainInfo) throws Exception;
}
