package com.zdmoney.helper;

import com.zdmoney.enm.OrderGenerateType;
import org.joda.time.DateTime;

/**
 * 序号生成器
 *
 * @author: Leon
 * @time: 2018/9/22 10:30
 * @desc:
 */
public class SerialNumberGenerator {

    /**
     * 序号生成器
     *
     * @param orderGenerateType
     * @param customerId
     * @return
     */
    public static String generatorOrderNum(OrderGenerateType orderGenerateType, long customerId) {
        String SerialNumber = orderGenerateType.getType()
                + orderGenerateType.getCode() + customerId
                + DateTime.now().toString("yyyyMMddHHmmssSSS")
                + (int) (Math.random() * (100));
        return SerialNumber;
    }

    public static void main(String[] args) {
        System.out.println(generatorOrderNum(OrderGenerateType.MAIN_ORDER, 1));

    }

}