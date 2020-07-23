package com.zdmoney.service;

import com.zdmoney.base.BaseTest;
import com.zdmoney.mapper.report.ReportDailyFirstInvestMapper;
import com.zdmoney.models.report.ReportDailyFirstInvest;
import org.joda.time.DateTime;
import org.junit.*;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

import java.math.BigDecimal;

/**
 * Created by 00225181 on 2016/3/17.
 */
public class ReportDailyFirstInvestTest  extends BaseTest{
    @Autowired
    ReportDailyFirstInvestMapper reportDailyFirstInvestMapper;

    @Test
    @Rollback(false)
    public  void test(){
        ReportDailyFirstInvest invest = new ReportDailyFirstInvest();
        DateTime now = DateTime.now();
        String date = now.toString("yyyy-MM-dd");
        invest.setReportDate(date);
        invest.setChannel("zendaimoney");
//        invest.setId(4L);
//        invest.setInvestNum(1);
        invest.setInvestAmt(new BigDecimal(10000));
        reportDailyFirstInvestMapper.insert(invest);
        System.out.println(invest.getId());
//        invest = reportDailyFirstInvestMapper.selectOne(invest);
//        invest.setInvestNum(4);
//        invest.setInvestAmt(new BigDecimal(60000));
//        reportDailyFirstInvestMapper.updateByPrimaryKey(invest);
//        reportDailyFirstInvestMapper.updateByPrimaryKeySelective(invest);
    }
}
