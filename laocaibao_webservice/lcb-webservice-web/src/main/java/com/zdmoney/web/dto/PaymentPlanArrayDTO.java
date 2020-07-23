package com.zdmoney.web.dto;

import com.google.common.collect.Lists;
import com.zdmoney.models.payment.PaymentPlan;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
public class PaymentPlanArrayDTO {

    private List<PaymentPlanDTO> notPaymentList = Lists.newArrayList();//待还

    private List<PaymentPlanDTO> paymentList = Lists.newArrayList();//已还

    private BigDecimal noPayTotalAmt = new BigDecimal(0);//待回本金总额

    private BigDecimal noPayPrincipalInterest = new BigDecimal(0);//待回本息和

    private BigDecimal noPayTotalInterest = new BigDecimal(0);//待回利息


    private BigDecimal payTotalAmt = new BigDecimal(0);//已回本金总额

    private BigDecimal payPrincipalInterest = new BigDecimal(0);//已回本息和

    private BigDecimal payTotalInterest = new BigDecimal(0);//已回利息和
}
