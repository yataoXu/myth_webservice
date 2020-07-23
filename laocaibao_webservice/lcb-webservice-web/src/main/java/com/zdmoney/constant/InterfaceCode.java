package com.zdmoney.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Getter
public enum InterfaceCode {

    USER_OPEN("1001", "user/accountOpen", "个人开户"),

    BANK_BIND("1002", "user/bankCardBind", "绑卡"),

    BANK_UN_BIND("1003", "user/bankCardUnBind", "解绑卡"),

    MOBILE_CHANGE("1004", "user/changeMobile", "更换手机号"),

    PASSWORD_MODIFY("1005", "user/resetPassword", "密码重置"),

    SMS_NOFITY("1006", "user/smsConfigInform", "短信通知配置"),

    USER_CANCEL("1007", "user/cancelUser", "销户"),

    USER_RECHARGE("1008", "user/recharge", "充值（快捷，收银台）"),

    USER_WITHDRAW("1009", "user/withdraw", "提现"),

    USER_GRANT("1010", "trade/userGrant", "用户授权"),

    USER_PWD_FREEZE("1011", "trade/passwordFreeze", "验密冻结"),

    USER_PWD_UN_FREEZE("1012", "trade/passwordUnfreeze", "验密解冻"),

    OFFLINE_RECHARGE("1013", "trade/passwordUnfreeze", "转账充值"),

    THIRD_SERIALNO("", "manager/getThirdSerialNo", "通过商户流水号获取第三方流水号");

    private String code;

    private String url;

    private String desc;

    public static final Map<String, String> codeMap = new HashMap<>();

    static {
        for (InterfaceCode code : EnumSet.allOf(InterfaceCode.class)) {
            codeMap.put(code.getCode(), code.getDesc());
        }
    }
}
