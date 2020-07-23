package com.zdmoney.service;

import com.zdmoney.service.lifeService.LifeService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import websvc.models.Model_500019;
import websvc.models.Model_500020;
import websvc.models.Model_500021;
import websvc.req.ReqMain;

/**
 * LifeServiceImpl Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>���� 18, 2016</pre>
 */
public class LifeServiceImplTest{

    @Autowired
    private LifeService lifeService;

    /**
     * Method: getProductByPhone(ReqMain reqMain)
     */
    @Test
    public void testGetProductByPhone() throws Exception {

        ReqMain reqMain = new ReqMain();
        Model_500019 model520019 = new Model_500019();
        String result = "";
        //查询话费
        model520019.setCustomerId("11111");
        model520019.setMobilePhone("13795409101");
        model520019.setType("1");
        reqMain.setReqParam(model520019);
        result = "";
        try{
            lifeService.getProductByPhone(reqMain);
            System.out.println("话费："+result);
        }
        catch(Exception e){
            e.printStackTrace();
        }

        //查询流量
        model520019.setCustomerId("11111");
        model520019.setMobilePhone("13795409101");
        model520019.setType("0");
        reqMain.setReqParam(model520019);
        result = "";
        try{
            lifeService.getProductByPhone(reqMain);
            System.out.println("流量："+result);
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    /**
     * Method: rechargeToPhone(ReqMain reqMain)
     */
    @Test
    public void testRechargeToPhone() throws Exception {
        ReqMain reqMain = new ReqMain();
        Model_500020 model = new Model_500020();
        String result = "";

        //充话费
        model.setCustomerId("11111");
        model.setProductNo("1000000001");
        model.setMobilePhone("13795409101");
        reqMain.setReqParam(model);
        result = "";
        try{
            lifeService.rechargeToPhone(reqMain);
            System.out.println("话费："+result);
        }
        catch(Exception e){
            e.printStackTrace();;
        }

        //充流量
        model.setCustomerId("11111");
        model.setProductNo("2000000001");
        model.setMobilePhone("13795409101");
        reqMain.setReqParam(model);
        result = "";
        try{
            lifeService.rechargeToPhone(reqMain);
            System.out.println("流量："+result);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Method: getRechargeDetail(ReqMain reqMain)
     */
    @Test
    public void testGetRechargeDetail() throws Exception {

        ReqMain reqMain = new ReqMain();
        Model_500021 model = new Model_500021();
        String result = "";

        model.setCustomerId("11111");
        model.setPageNo(1);
        model.setPageSize(10);
        reqMain.setReqParam(model);
        result = "";
        try{
            lifeService.getRechargeDetail(reqMain);
            System.out.println("列表："+result);
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }


} 
