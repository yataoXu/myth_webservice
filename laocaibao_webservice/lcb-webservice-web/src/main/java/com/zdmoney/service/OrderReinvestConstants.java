package com.zdmoney.service;

import com.zdmoney.common.ConfigParamBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;

/**
 * OrderReinvestConstants
 *
 * @Author: wein
 * @Description:
 * @Date: Created in 2018/9/3 15:47
 * @Mail: wein@zendaimoney.com
 */
@Component
public class OrderReinvestConstants {

    @Autowired
    private ConfigParamBean configParamBean;

    private HashMap<String, OrderReinvestConstants> OrderReinvestConstantsMap = new HashMap<String, OrderReinvestConstants>();

    //枚举名称
    String reinvestName;
    //枚举code
    String type;
    //枚举利率
    BigDecimal rate;
    //续投天数
    int days;
    //
    String rateDiffer;

    public OrderReinvestConstants(String reinvestName, String type, BigDecimal rate, int days, String rateDiffer) {
        this.reinvestName = reinvestName;
        this.type = type;
        this.rate = rate;
        this.days = days;
        this.rateDiffer = rateDiffer;
    }

    public OrderReinvestConstants() {
    }

    public void setMapValue() {
        OrderReinvestConstantsMap.put("0", new OrderReinvestConstants("续投360天", "0", new BigDecimal(configParamBean.getReinvest360days()), 360, configParamBean.getRatediffer360days()));
        OrderReinvestConstantsMap.put("1", new OrderReinvestConstants("续投180天", "1", new BigDecimal(configParamBean.getReinvest180days()), 180, configParamBean.getRatediffer180days()));
    }


    public String getReinvestName() {
        return reinvestName;
    }

    public void setReinvestName(String reinvestName) {
        this.reinvestName = reinvestName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public String getRateDiffer() {
        return rateDiffer;
    }

    public void setRateDiffer(String rateDiffer) {
        this.rateDiffer = rateDiffer;
    }

    public OrderReinvestConstants getOrderReinvestTypeByType(String type) {
        setMapValue();
        OrderReinvestConstants orderReinvestConstants = OrderReinvestConstantsMap.get(type);
        return orderReinvestConstants;
    }
}
