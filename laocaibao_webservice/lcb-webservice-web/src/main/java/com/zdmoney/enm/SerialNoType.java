package com.zdmoney.enm;

import com.zdmoney.service.SerialNoGeneratorService;
import com.zdmoney.utils.DateUtil;

import java.util.Date;

/**
 * 请求流水号类型
 * Author: silence.cheng
 * Date: 2018/12/4 14:48
 */
public enum SerialNoType {

    REPAY_CASH("回款送现金券","REPAY_CASH"),
    REGIST_CASH("注册送现金券","REGIST_CASH"),
    GAIN_CASH("领取现金券","GAIN_CASH"),
    TASK_CASH("活动送现金券","TASK_CASH"),
    MONTH_CASH("月度礼包送现金券","MONTH_CASH"),;

    private String desc;
    private String type;

    public String getDesc() {
        return desc;
    }


    public String getType() {
        return type;
    }


    SerialNoType(String desc, String type) {
        this.desc = desc;
        this.type = type;
    }

    public static void main(String[] args) {
        System.out.println(SerialNoType.MONTH_CASH.getType());
        System.out.println(SerialNoGeneratorService.commonGenerateTransNo(SerialNoType.MONTH_CASH, "888888",DateUtil.timeFormat(new Date(),DateUtil.YYYYMM)));
    }
}
