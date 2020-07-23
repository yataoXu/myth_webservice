package com.zdmoney.web.dto;

import lombok.Data;

/**
 * Created by 00225181 on 2015/12/3.
 */
@Data
public class BindCardInfoDTO {

    /*绑定状态*/
    private String bindStatus = ""; //0-用户有卡信息，正常 1-存量用户有卡信息，但自动绑定失败  2-用户未绑卡

    /*银行名称*/
    private String bankName = "";

    /*银行代码*/
    private String bankCode = "";

    private BankCardInfoDTO bankCardInfoDTO;

    /*银行卡号*/
    private String bankCard = "";
}
