package com.zdmoney.service.customer;

import com.alibaba.fastjson.JSON;
import com.zdmoney.base.JUnitActionBase;
import com.zdmoney.common.Result;
import org.junit.Test;
import websvc.models.Model_550006;
import websvc.req.ReqMain;

/**
 * @program: laocaibao_webservice
 * @description:
 * @author: WeiNian
 * <p>
 * wein@zendaimoney.com
 * @create: 2019-03-14 15:07
 **/
public class CustomerAddressServiceTest extends JUnitActionBase {

    private CustomerAddressService customerAddressService = JUnitActionBase.context.getBean(CustomerAddressService.class);

    @Test
    public void queryBorrowerInfo(){
        try {
            ReqMain reqMain = new ReqMain();
            Model_550006 model_550006 =new Model_550006();
            model_550006.setStatus(2);
            model_550006.setId("10000002536");
            reqMain.setReqParam(model_550006);
            Result result = customerAddressService.queryBorrowerInfo(reqMain);
            System.out.println(JSON.toJSONString(result));
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
