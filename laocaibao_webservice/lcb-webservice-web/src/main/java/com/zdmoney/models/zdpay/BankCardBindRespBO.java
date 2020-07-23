package com.zdmoney.models.zdpay;


import lombok.Data;


/**
 * @author lwf
 * @date 2018/8/1
 * use: 绑卡回调
 */
@Data
public class BankCardBindRespBO extends BaseBO {

    /**
     * 手机号
     */
    private String mobileNo;

    /**
     * 卡银行编号
     */
    private String parentBankId;

    /**
     * 卡银行名
     */
    private String parentBankName;

    /**
     * 对公账户/卡号
     */
    private String cardNo;

    /**
     * 卡支行名
     */
    private String bankNm;
}
