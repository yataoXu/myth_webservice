package com.zdmoney.webservice.api.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 2018/1/25.
 */
public enum MsgType {
    REPAY("repay","回款"),
    REPAYPLAN("repayPlan","回款计划"),
    REPAY_COMPLETE("repay_complete","回款完成"),
    INVESTING_OVER("invest_over","订单完成"),
    //INVALID_ORDER("invalid_order","订单失效"),
    ACTIVATE_ORDER("activate_order","订单生效"),
    REGISTER("register","注册"),
    BANK_CARD_AFFAIR("bank_card_affair","绑卡/解绑"),
    AUTHENTICATE_REAL_NAME("authenticate_real_name","实名认证"),
    BIND_WECHAT("bind_wechat","微信绑定"),
    EVALUATE_RISK_TOLERANCE("evaluate_risk_tolerance","风险承受评测"),
    WACAI_INFO_CONFIRM_NOTICE("wacai_info_confirm_notice","挖财信息注册确认通知标的");

    private static volatile Map<String,MsgType> prefixToEnum;

    public final static String splitor = "#";

    private String prefix;

    private String name;

    MsgType(String prefix, String name){
        this.prefix = prefix;
        this.name = name;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getName() {
        return name;
    }

    /**
     * use "volatile" and "double check" to initialize prefixToEnum
     * being sure the processing is thread-safety
     * @param prefix
     * @return
     */
    public static MsgType getInstance(String prefix){
        if(prefixToEnum == null){
            synchronized (MsgType.class){
                if(prefixToEnum == null){
                    prefixToEnum = new HashMap<String,MsgType>();
                    for(MsgType msgType : values()){
                        prefixToEnum.put(msgType.getPrefix(),msgType);
                    }
                }
            }
        }
        return prefixToEnum.get(prefix);
    }


    public String toString(){
        return "type[name = " + name + ",prefix = " + prefix + "]";
    }
    /**
     * The 2nd way,use static inner class :
     * when you use a class for first time, jvm will load this class and init it,
     * and the static block sentences will be executed,as while as this procession is thread-safety
     */
    /*public static MsgType getInstance(String prefix){
        return Helper.getInstance(prefix);
    }

    private static class  Helper{
        static{
            prefixToEnum = new HashMap<>();
            for(MsgType msgType : values()){
                prefixToEnum.put(msgType.getPrefix(),msgType);
            }
        }

        public static MsgType getInstance(String prefix){
            return prefixToEnum.get(prefix);
        }
    }*/
}
