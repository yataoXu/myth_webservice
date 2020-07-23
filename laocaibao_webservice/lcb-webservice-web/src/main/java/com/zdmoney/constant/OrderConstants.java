package com.zdmoney.constant;

import com.google.common.collect.Maps;
import com.zdmoney.utils.PropertiesUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


/**
 * 订单常量

 * @author Leon
 */
public class OrderConstants {

    /**
     * 订单持有类型
     */
    public static class OrderHoldType {
        public static final String  HOLD_COMMON = "0"; // 普通用户-理财计划
        public static final String  HOLD_SPECIAL = "1"; // 特殊理财人-理财计划
        public static final String  HOLD_SPECIAL_EXCEPTION="2";//特殊理财人-异常回购
        public static final String  FORT_HOLDER="9";//堡垒户
    }
}
