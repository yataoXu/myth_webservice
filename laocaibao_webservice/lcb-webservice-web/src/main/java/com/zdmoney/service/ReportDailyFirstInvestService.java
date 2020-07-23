package com.zdmoney.service;

import com.zdmoney.mapper.order.BusiOrderTempMapper;
import com.zdmoney.mapper.report.ReportDailyFirstInvestMapper;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.financePlan.BusiOrderSub;
import com.zdmoney.models.order.BusiOrderTemp;
import com.zdmoney.models.report.ReportDailyFirstInvest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 00232384 on 2017/3/19.
 */
@Service
@Slf4j
public class ReportDailyFirstInvestService {

    @Autowired
    private ReportDailyFirstInvestMapper reportDailyFirstInvestMapper;

    @Autowired
    private BusiOrderTempMapper busiOrderTempMapper;

    @Autowired
    private BusiOrderService busiOrderService;

    @Async("payThreadExecutor")
    public void saveReport(BusiOrderSub order, CustomerMainInfo mainInfo, boolean isFirst) {
        if (!isFirst) {
            return;
        }
        log.info("更新用户首单标志，订单编号：【{}】,用户ID：【{}】", order.getId(), mainInfo.getId());
        BusiOrderTemp orderTemp = new BusiOrderTemp();
        orderTemp.setId(order.getId());
        orderTemp.setIsFirstOrder(1L);
        busiOrderTempMapper.updateByPrimaryKeySelective(orderTemp);
        log.info("用户首单统计开始，订单编号：【{}】,用户ID：【{}】", order.getId(), mainInfo.getId());
        if (!StringUtils.isEmpty(mainInfo.getCmOpenChannel())) {
            ReportDailyFirstInvest invest = new ReportDailyFirstInvest();
            DateTime now = DateTime.now();
            String date = now.toString("yyyy-MM-dd");
            invest.setReportDate(date);
            invest.setChannel(mainInfo.getCmOpenChannel());
            invest = reportDailyFirstInvestMapper.selectOne(invest);
            if (invest != null) {
                invest.setInvestNum(invest.getInvestNum() + 1);
                invest.setInvestAmt(invest.getInvestAmt().add(order.getOrderAmt()));
                reportDailyFirstInvestMapper.updateByPrimaryKeySelective(invest);
            } else {
                invest = new ReportDailyFirstInvest();
                invest.setInvestAmt(order.getOrderAmt());
                invest.setInvestNum(1);
                invest.setReportDate(date);
                invest.setChannel(mainInfo.getCmOpenChannel());
                reportDailyFirstInvestMapper.insert(invest);
            }
        }
        log.info("用户首单统计结束，订单编号：【{}】,用户ID：【{}】", order.getId(), mainInfo.getId());
    }

    @Async("payThreadExecutor")
    public void statisticsSalesVolume(Long customerId) {
        try {
            Long res = busiOrderService.findUpdateCustomerCount(customerId);
            Map<String, Object> map = new HashMap<>();
            if (res == null) {
                map.put("customerId", customerId);
                map.put("count", 1);
                busiOrderService.insertCustomerCount(map);
            } else {
                map.put("customerId", customerId);
                map.put("count", res + 1);
                busiOrderService.updateCustomerCount(map);
            }
            log.info(customerId+"的购买数量由"+res+"更新为"+map.get("count"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
