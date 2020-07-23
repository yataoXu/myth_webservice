package com.zdmoney.webservice.api.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 2017/11/1.
 *
 * you should add other enums when you need more
 */
public enum WelfareType {
    REGISTRATION("registrationCoupon","注册红包");

    private static volatile Map<String,WelfareType> prefixToEnum;

    public final static String splitor = "_";

    private String prefix;

    private String name;

    WelfareType(String prefix, String name){
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
    public static WelfareType getInstance(String prefix){
        if(prefixToEnum == null){
            synchronized (WelfareType.class){
                if(prefixToEnum == null){
                    prefixToEnum = new HashMap<String,WelfareType>();
                    for(WelfareType welfare : values()){
                        prefixToEnum.put(welfare.getPrefix(),welfare);
                    }
                }
            }
        }
        return prefixToEnum.get(prefix);
    }

    /**
     * The 2nd way,use static inner class :
     * when you use a class for first time, jvm will load this class and init it,
     * and the static block sentences will be executed,as while as this procession is thread-safety
     */
    /*public static WelfareType getInstance(String prefix){
        return Helper.getInstance(prefix);
    }

    private static class  Helper{
        static{
            prefixToEnum = new HashMap<>();
            for(WelfareType welfare : values()){
                prefixToEnum.put(welfare.getPrefix(),welfare);
            }
        }

        public static WelfareType getInstance(String prefix){
            return prefixToEnum.get(prefix);
        }
    }*/
}