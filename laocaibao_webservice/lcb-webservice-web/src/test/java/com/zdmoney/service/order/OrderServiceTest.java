package com.zdmoney.service.order;

import com.zdmoney.base.BaseTest;
import com.zdmoney.models.financePlan.BusiOrderSub;
import com.zdmoney.vo.OrderVo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import websvc.req.ReqHeadParam;
import websvc.req.ReqMain;

import java.math.BigDecimal;

/**
 * Created by 00232384 on 2017/7/13.
 */
public class OrderServiceTest extends BaseTest {
    @Autowired
    private OrderService orderService;

    private ReqMain reqMain;

    @Before
    public void setUp() {
        reqMain = new ReqMain();
        ReqHeadParam reqHeadParam = new ReqHeadParam();
        reqHeadParam.setUserAgent("捞财宝 5.0.0(iOS;8.3;zh_CN)");
        reqHeadParam.setSessionToken("");
        reqHeadParam.setToken("46716ad41ba20b7dcf68262ac28adb824d15da9cb3b7bab10a6da1471ecb1fe3");
        reqHeadParam.setVersion("5.0.0");
        reqHeadParam.setMechanism("证大");
        reqHeadParam.setPlatform("App");
        reqHeadParam.setTogatherType("证大无线");
        reqHeadParam.setOpenchannel("AppStore");
        reqMain.setReqHeadParam(reqHeadParam);
        reqMain.setProjectNo("Lc_WS2015");
        reqMain.setReqTimestamp("");
        reqMain.setReqUrl("");
        reqMain.setSign("126e8f8b81ad99ac0a33b7b2c78aa29f");
        reqMain.setSn("Lc_WS2015-20150701143755-75763");
    }

    @Test
    public void orderCommon() throws Exception {
        OrderVo vo = new OrderVo();
        vo.setProductId(9444L);
        vo.setCustomerId(888888L);
        vo.setOrderAmt(new BigDecimal("1000"));
        vo.setHoldType("0");
        vo.setReqMain(reqMain);
        BusiOrderSub orderSub = orderService.order(vo);
        Assert.assertNotNull(orderSub);
    }

    @Test
    public void orderPlan() throws Exception {
        orderService.order(null);
    }

}