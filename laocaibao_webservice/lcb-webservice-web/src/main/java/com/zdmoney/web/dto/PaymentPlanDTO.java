package com.zdmoney.web.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PaymentPlanDTO {

    private String currTerm;//当前期数

    private String term;//总期数

    private String repayDay;//回款日

    private String principal;//本金

    private String interest;//利息

    private String principalInterest;//本息和

    private String status;

    private String title;

    private String pcTitle;
}
