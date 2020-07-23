package com.zdmoney.webservice.api.dto.channel;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by 46186 on 2019/2/14.
 * 用户信息
 */
@Data
public class UserInfoDto implements Serializable {

    private String customerName;

    //是否实名0:否，1:是
    private String realNameStatus;

    //是否开户
    private String openAccountStatus;

    //是否授权
    private String authStatus;

    //是否绑卡
    private String bindCardStatus;

    //卡号后四位
    private String bankCard;

    //银行名称
    private String bankName;

    /**
     * 自动出借授权额度
     */
    private Long autoLendAmt = 0L;

    @Override
    public String toString() {
        return "UserInfoDto{" +
                "customerName='" + customerName + '\'' +
                ", realNameStatus='" + realNameStatus + '\'' +
                ", openAccountStatus='" + openAccountStatus + '\'' +
                ", authStatus='" + authStatus + '\'' +
                ", bindCardStatus='" + bindCardStatus + '\'' +
                ", bankCard='" + bankCard + '\'' +
                ", bankName='" + bankName + '\'' +
                ", autoLendAmt=" + autoLendAmt +
                '}';
    }
}
