/*
* Copyright (c) 2016 zendaimoney.com. All Rights Reserved.
*/
package com.zdmoney.service;

import com.zdmoney.base.BaseTest;
import com.zdmoney.marketing.api.dto.ResultDto;
import com.zdmoney.marketing.api.facade.IMarketingFacadeService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * IMarketingFacadeServiceTest
 * <p/>
 * Author: Hao Chen
 * Date: 2016-05-03 16:45
 * Mail: haoc@zendaimoney.com
 */
public class IMarketingFacadeServiceTest extends BaseTest {

    @Autowired
    private IMarketingFacadeService marketingFacadeService;

    /**
     * 查询账户积分明细
     * @throws Exception
     */
    @org.junit.Test
    public void testSign() throws Exception {
        ResultDto sign = marketingFacadeService.sign("01160329000004357");
        System.out.println(sign);
    }

}