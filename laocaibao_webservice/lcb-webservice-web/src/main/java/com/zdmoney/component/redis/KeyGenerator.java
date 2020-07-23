package com.zdmoney.component.redis;

import cn.hutool.core.util.RandomUtil;
import org.joda.time.DateTime;

import java.util.concurrent.TimeUnit;

/**
 * Created by 00232384 on 2017/6/28.
 */
public enum KeyGenerator {

        PRODUCT_LIST("PRD_LST","产品列表",1,TimeUnit.MINUTES),

        USER_SIGN("USR_SGN","用户签到",1,TimeUnit.MINUTES),

        USER_SIGN_DAY("USR_SIGN_DAY" + DateTime.now().toString("yyyyMMdd"),"当天签到",1,TimeUnit.DAYS),

        HISTORY_SALE("HIS_SAL"+ DateTime.now().toString("yyyyMMdd"),"历史销售统计",1,TimeUnit.DAYS),

        PRODUCT_NOTICE("PRD_NOTICE", "产品列表公告", 1, TimeUnit.HOURS),

        USER_STATUS("US_", "430011前置判断", RandomUtil.randomInt(12, 24), TimeUnit.HOURS),

        USER_SESSION("USR_SESS","用户SESSION", 2 * 24 * 60 * 60 * 1000,TimeUnit.MILLISECONDS);


    private String key;

    private String desc;

    private long time;

    private TimeUnit timeUnit;



    public String getKey() {
        return key;
    }

    public long getTime() {
        return time;
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public String getDesc() {
        return desc;
    }

    KeyGenerator(String key, String desc, long time, TimeUnit timeUnit) {
        this.key = key;
        this.desc = desc;
        this.time = time;
        this.timeUnit = timeUnit;
    }
}
