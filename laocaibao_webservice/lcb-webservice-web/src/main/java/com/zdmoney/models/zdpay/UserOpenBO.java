package com.zdmoney.models.zdpay;

import lombok.Data;

/**
 * 开户, 授权, 绑卡信息
 */
@Data
public class UserOpenBO extends BaseBO {

    private String userId;

    private String reserved;

    private String custName;

    private String artifName;

    /**
     * 用户属性：1.出借人2.借款人3.营销户 4.手续费户5.代偿户6.消费金融商家7.平台自有资金账户 9.担保方手续费户
     */
    private String userAttr;

    private String certifTp;

    private String mobileNo;

    //////////////授权//////////////
    /**
     * 格式： 010000000000
     * 0-未授权、 1-已授权；
     * 权限数值占位说明：
     * 第 1 位：自动出借
     * 第 2 位： 自动还款
     * 第 3 位： 自动代偿还款
     * 第 4 位： 自动缴费
     * 第 5-12 位预留占位
     */
    private String authStatus;

    /**
     * 自动出借授权期限
     */
    private String autoLendTerm;

    /**
     * 自动出借授权额度
     */
    private Long autoLendAmt = 0L;

    /**
     * 已使用授权出借额度
     */
    private Long usedLendAmt = 0L;

    /**
     * 剩余授权出借额度
     */
    private Long leaveLendAmt = 0L;

    /**
     * 自动还款授权期限
     */
    private String autoRepayTerm;

    /**
     * 自动还款授权额度
     */
    private Long autoRepayAmt = 0L;

    /**
     * 自动代偿还款授权期限
     */
    private String autoCompenTerm;

    /**
     * 自动代偿还款授权额度
     */
    private Long autoCompenAmt = 0L;

    /**
     * 缴费授权期限
     */
    private String autoFeeTerm;

    /**
     * 缴费授权额度
     */
    private Long autoFeeAmt = 0L;

    //////////////银行卡//////////////

    /**
     * 卡银行编号
     */
    private String parentBankId;

    /**
     * 卡银行名
     */
    private String parentBankName;

    /**
     * 卡支行名
     */
    private String bankNm;

    /**
     * 银行卡号
     */
    private String cardNo;
}
