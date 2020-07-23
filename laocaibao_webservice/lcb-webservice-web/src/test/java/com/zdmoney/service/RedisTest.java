package com.zdmoney.service;

import com.alibaba.fastjson.JSON;
import com.zdmoney.base.BaseTest;
import com.zdmoney.service.product.ProductService;
import com.zdmoney.session.RedisSessionManager;
import com.zdmoney.utils.DateUtil;
import com.zdmoney.utils.Page;
import com.zdmoney.vo.BusiProductVO;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created by gaol on 2017/6/13
 **/
public class RedisTest extends BaseTest {

    @Autowired
    RedisSessionManager redisSessionManager;

    @Autowired
    ProductService productService;

    @Test
    public void redisTest(){
        String listProductKey = "LCB_WS_LIST_PRODUCT_" + DateUtil.getDateFormatString(new Date(),"yyyyMMddhhmm");
        String productDTO = redisSessionManager.get(listProductKey);

        System.out.println(productDTO);
    }

    @Test
    public void productTest(){
        Page<BusiProductVO> page = null;
        try {
            page = productService.getProductDetail(null,null,"","0", 1,20,"1");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(">>>>>>>>" + JSON.toJSONString(page));
    }

}
