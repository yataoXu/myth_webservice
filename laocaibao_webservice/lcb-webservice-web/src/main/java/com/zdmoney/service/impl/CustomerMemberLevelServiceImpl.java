package com.zdmoney.service.impl;


import com.google.common.collect.Maps;
import com.zdmoney.constant.Constants;
import com.zdmoney.mapper.CustomerMemberLevelMapper;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.customer.CustomerMemberLevel;
import com.zdmoney.service.AccountOverview520003Service;
import com.zdmoney.service.CustomerMemberLevelService;
import com.zdmoney.utils.StringUtil;
import com.zdmoney.vo.UserUnReceiveAsset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @date 2018-10-20 09:25:06
 */
@Service("customerMemberLevelService")
public class CustomerMemberLevelServiceImpl implements CustomerMemberLevelService {

    @Autowired
    private AccountOverview520003Service accountOverview520003Service;

    @Autowired
    private CustomerMemberLevelMapper customerMemberLevelMapper;


    @Override
    public Map<String, String> queryMemberLevel(CustomerMainInfo mainInfo) throws Exception {
        Map<String, String> levelInfoMap = Maps.newHashMap();
        levelInfoMap.put("initAmt", "0.00");
        levelInfoMap.put("initLevel", "0");

        // 查询初始在投金额
        Map<String, Object> map = Maps.newHashMap();
        map.put("customerId", mainInfo.getId());
        List<CustomerMemberLevel> resultList = customerMemberLevelMapper.queryCustomerMemberLevel(map);
        CustomerMemberLevel leveInfo = new CustomerMemberLevel();
        if (resultList != null && !resultList.isEmpty()) {
            leveInfo = resultList.get(0);
            levelInfoMap.put("initAmt", leveInfo.getInitAmt().toString());
            levelInfoMap.put("initLevel", leveInfo.getInitLevel());
        }
        // 查询当前在投金额
        if (StringUtil.isEmpty(leveInfo.getEndAmt())) {
            UserUnReceiveAsset unReceiveAsset = accountOverview520003Service.getHoldAsset(mainInfo);
            BigDecimal holdAsset = unReceiveAsset.getUnReceivePrinciple();
            double doubleHoldAsset = holdAsset.setScale(2, BigDecimal.ROUND_DOWN).doubleValue();
            levelInfoMap.put("currentAmt", subStringByPoint(holdAsset));
            levelInfoMap.put("currentLevel", calculationLevel(doubleHoldAsset));
        } else {
            levelInfoMap.put("currentAmt", subStringByPoint(leveInfo.getEndAmt()));
            levelInfoMap.put("currentLevel", leveInfo.getEndLevel());
        }
        return levelInfoMap;
    }

    private String calculationLevel(double amt) {
        if (amt == 0) {
            return Constants.MemberLevel.LEVEL_0;
        } else if (amt > 0 && amt <= 50000) {
            return Constants.MemberLevel.LEVEL_1;
        } else if (amt > 50000 && amt <= 200000) {
            return Constants.MemberLevel.LEVEL_2;
        } else if (amt > 200000 && amt <= 500000) {
            return Constants.MemberLevel.LEVEL_3;
        } else if (amt > 500000 && amt <= 2000000) {
            return Constants.MemberLevel.LEVEL_4;
        } else if (amt > 2000000 && amt <= 10000000) {
            return Constants.MemberLevel.LEVEL_5;
        } else if (amt > 10000000) {
            return Constants.MemberLevel.LEVEL_6;
        }

        return Constants.MemberLevel.LEVEL_0;
    }

    private String subStringByPoint(BigDecimal input) {
        if (input.compareTo(new BigDecimal("0")) == 0) {
            return "0.00";
        }
        String str = input.toString();
        int num = str.indexOf(".");
        return str.substring(0, num + 3);

    }
}
