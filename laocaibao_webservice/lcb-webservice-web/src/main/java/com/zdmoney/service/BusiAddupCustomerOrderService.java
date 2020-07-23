package com.zdmoney.service;

import com.zdmoney.models.BusiAddupCustomerOrder;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.financePlan.BusiOrderSub;
import com.zdmoney.models.order.BusiOrderTemp;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by jb sun on 2016/2/23.
 */
public interface BusiAddupCustomerOrderService {
    List<BusiAddupCustomerOrder> selctBusiAddupCustomerOrder(Map<String,String> map);

    int insert(BusiAddupCustomerOrder record);

    BusiAddupCustomerOrder updateAddupCustomerOrderAmt(String customerNo,String yearMonth,BigDecimal orderAmt,BigDecimal orderNum);

    int updateInviteUserCount(Map<String,String> map);

    void setMonthInvestTotalAmt(CustomerMainInfo customerMainInfo, BusiOrderSub order);

    BusiAddupCustomerOrder findByCustomerNo(String cmNumber, String dateStr);
}
