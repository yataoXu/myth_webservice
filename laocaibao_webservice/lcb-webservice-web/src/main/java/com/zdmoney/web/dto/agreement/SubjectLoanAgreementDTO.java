package com.zdmoney.web.dto.agreement;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by 00225181 on 2016/4/5.
 */
@Getter
@Setter
public class SubjectLoanAgreementDTO {
    private String orderNum;//订单号
    private String borrowerName;//借款人姓名
    private String idNo;//借款人身份证号
    private String customerName;//投资人姓名
    private String cmNumber;//客户代码
    private String orderAmt;//订单金额
    private String loanDays;//出借天数
    private String principalInterest;//订单本息
    private String productPrincipalInterest;//产品本息
    private String borrowPurpose;//借款目的
    private String totalInvestAmt;//产品募集金额
    private String lastExpire;//还款日期
    private String payEndTime;//还款截止时间
    private String bankCardNo;//银行卡号
    private String bankName;//开户行

}
