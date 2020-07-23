package com.zdmoney.webservice.api.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 2018/3/12.
 */
@AllArgsConstructor
@Getter
public enum CustomerAccountType{
    LENDER("出借人","lender", "1"),
    BORROWER("借款人","borrower", "2"),
    MARKETING("营销户","marketing","3"),
    SERVICE_CHARGE_ACCOUNT("手续费户", "service_charge_account", "4"),
    COMPENSATORY_ACCOUNT("代偿户", "compensatory_account", "5"),
    CONSUME_ACCOUNT("消费金融商家", "consume_account", "6"),
    PLATFORM_ACCOUNT("平台自有资金账户", "platform_account", "7"),
    GUARANTEE_ACCOUNT("担保方手续费户", "guarantee_account", "9");

    private String descr;
    private String value;
    private String code;

    private volatile static Map<String,CustomerAccountType> codeToTypes;

    public static CustomerAccountType getTypeByCode(String code){
        if(codeToTypes == null) {
            synchronized (CustomerAccountType.class) {
                if (codeToTypes == null) {
                    codeToTypes = new HashMap<>();
                    for (CustomerAccountType accountType : CustomerAccountType.values()) {
                        codeToTypes.put(accountType.getCode(), accountType);
                    }
                }
            }
        }
        return codeToTypes.get(code);
    }
}
