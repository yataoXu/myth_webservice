package com.zdmoney.service;

import com.zdmoney.utils.NumberUtil;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

/**
 * Created by jb sun on 2016/3/3.
 */
public class BigDemailTest {
    public static void main(String[] args) {
        BigDecimal orderAmt = new BigDecimal(2000);
        BigDecimal yearRate = new BigDecimal(0.22);
        BigDecimal voucherRate = new BigDecimal(0.3010);

        MathContext mc = new MathContext(3, RoundingMode.DOWN);
        BigDecimal dayInterest = orderAmt.multiply(yearRate).divide(new BigDecimal(365), mc).setScale(2, BigDecimal.ROUND_DOWN);
        System.out.println("dayInterest:"+dayInterest);

        dayInterest = dayInterest.setScale(2,BigDecimal.ROUND_DOWN);
        System.out.println("dayInterest1:"+dayInterest);

        System.out.println("dayInterest2:"+NumberUtil.fortmatBigDecimal(orderAmt.multiply(yearRate).divide(new BigDecimal(365),mc)));


        dayInterest = orderAmt.multiply(yearRate).divide(new BigDecimal(365), mc);
        System.out.println("dayInterest1:"+dayInterest);
        BigDecimal voucherDayInterest = orderAmt.multiply(yearRate.add(voucherRate)).divide(new BigDecimal(365, mc), 6);
        //System.out.println("dayInterest:"+dayInterest);
        System.out.println("voucherDayInterest:"+voucherDayInterest);

        System.out.println("dayInterest2:"+NumberUtil.fortmatBigDecimal(voucherDayInterest));


    }

}
