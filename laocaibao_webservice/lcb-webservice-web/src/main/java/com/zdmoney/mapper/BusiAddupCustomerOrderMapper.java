package com.zdmoney.mapper;

import com.zdmoney.models.BusiAddupCustomerOrder;

import java.util.List;
import java.util.Map;

public interface BusiAddupCustomerOrderMapper {
    List<BusiAddupCustomerOrder> selctBusiAddupCustomerOrder(Map<String,String> map);

    int insert(BusiAddupCustomerOrder record);

    int insertSelective(BusiAddupCustomerOrder record);

    int updateBusiAddupCustomerOrder(BusiAddupCustomerOrder busiAddupCustomerOrderVo);

    BusiAddupCustomerOrder selctBusiAddupCustomerOrder(BusiAddupCustomerOrder busiAddupCustomerOrder);

    int updateInviteUserCount(Map<String,String> map);
}