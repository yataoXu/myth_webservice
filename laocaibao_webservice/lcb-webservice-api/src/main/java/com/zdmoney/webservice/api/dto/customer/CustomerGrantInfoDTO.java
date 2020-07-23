package com.zdmoney.webservice.api.dto.customer;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by user on 2018/10/9.
 */
@Getter
@Setter
public class CustomerGrantInfoDTO implements Serializable {

    private String cmNumber;

    private String customerName;

    private String cellphone;

    private String idNum;

    private String grantStatus;//0:未授权 1：授权充足 2：授权不足
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

    public String getVeiledPhoneNo(){
        return StringUtils.isBlank(cellphone)? "" : cellphone.substring(0,3) + "****" + cellphone.substring(7);
    }

    public String getVeiledIdNo(){
        if(StringUtils.isBlank(idNum)) return null;
        int len = idNum.length();
        char[] arr = new char[len-8];
        Arrays.fill(arr,'*');
        return idNum.substring(0,4) + new String(arr) + idNum.substring(len - 4);
    }
}
