/*
* Copyright (c) 2016 zendaimoney.com. All Rights Reserved.
*/
package com.zdmoney.common.json;

import com.alibaba.fastjson.serializer.ValueFilter;
import com.google.common.collect.Sets;
import com.zdmoney.utils.JSONUtils;
import com.zdmoney.vo.UserAssetVo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Set;

/**
 * DefaultNumberFilter
 * <p/>
 * Author: Hao Chen
 * Date: 2016-04-13 16:38
 * Mail: haoc@zendaimoney.com
 */
public class DefaultNumberFilter implements ValueFilter {

    private static Set<String> oneScaleFieldNames = Sets.newHashSet(
            "yearRate", "yearRateInit", "addInterest",
            "totalAddRate", "actionRate" , "inviteRate"
    );


    @Override
    public Object process(Object object, String name, Object value) {
        int scale = oneScaleFieldNames.contains(name) ? 1 : 2;
        if (value instanceof BigDecimal) {
            return ((BigDecimal) value).setScale(scale, RoundingMode.DOWN).toString();
        } else if (value instanceof Double) {
            return new BigDecimal((Double) value).setScale(scale, RoundingMode.DOWN).toString();
        }
        return value;
    }

    public static void main(String[] args) {
        System.out.println(JSONUtils.toJSON(new UserAssetVo()));
    }
}