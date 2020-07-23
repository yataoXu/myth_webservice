package com.zdmoney.service;

import com.zdmoney.service.sys.SysSwitchService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by jb sun on 2016/3/8.
 */

public class TradeServiceTest{

    @Autowired
    private TradeService tradeService;

    @Autowired
    private SysSwitchService sysSwitchService;


    @Test
    public void testOrderPay()
    {
//       SysSwitchHelper sysSwitchHelper = new SysSwitchHelper();

//        ReqMain reqMain = new ReqMain();
//        Model_520001 model520001 = new Model_520001();
//        model520001.setCustomerId("81");
//        model520001.setOrderId("1788");
//        reqMain.setReqParam(model520001);
//        String result = "";
//        try{
//            tradeService.pay(reqMain);
//            System.out.println(result);
//        }
//        catch(Exception e){
//            e.printStackTrace();;
//        }

    }


}
