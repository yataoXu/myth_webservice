package com.zdmoney.service;

import com.alibaba.fastjson.JSON;
import com.zdmoney.base.JUnitActionBase;
import com.zdmoney.models.zdpay.UserRechargeBO;
import org.junit.Test;

import java.math.BigDecimal;

/**
 * @program: laocaibao_webservice
 * @description:
 * @author: WeiNian
 * <p>
 * wein@zendaimoney.com
 * @create: 2019-04-16 10:31
 **/
public class TestZdPayService extends JUnitActionBase {

    private ZdPayService zdPayService = JUnitActionBase.context.getBean(ZdPayService.class);

    @Test
    public void wacaiRepaymentFreeze(){
        zdPayService.wacaiRepaymentFreeze("1111111",new BigDecimal("1"),"01190325000020936");
    }


    @Test
    public void zdpay1008(){
        UserRechargeBO userRechargeBO =new UserRechargeBO();
        userRechargeBO.setChannelOrderNo("R8888840856201903270929234005");
        userRechargeBO.setLoginId("15121122201");
        userRechargeBO.setPayAmt("1000000");
        userRechargeBO.setMchntTxnSsn("R888884085620190327092923400511");
        zdPayService.zdpay1008(JSON.toJSONString(userRechargeBO));
    }


}
