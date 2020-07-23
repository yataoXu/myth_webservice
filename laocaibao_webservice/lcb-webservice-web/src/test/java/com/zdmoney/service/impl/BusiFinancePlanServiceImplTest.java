package com.zdmoney.service.impl;

import com.zdmoney.base.BaseTest;
import com.zdmoney.service.BusiFinancePlanService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

/**
 * Created by 00232384 on 2017/7/13.
 */
public class BusiFinancePlanServiceImplTest extends BaseTest{

    @Autowired
    private BusiFinancePlanService busiFinancePlanService;

    @Test
    public void buyBack() throws Exception {
        busiFinancePlanService.buyBack(null);
    }

}