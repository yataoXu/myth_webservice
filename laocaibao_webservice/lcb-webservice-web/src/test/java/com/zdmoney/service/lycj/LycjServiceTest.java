package com.zdmoney.service.lycj;

import com.zdmoney.base.BaseTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;

/**
 * Created by Administrator on 2018/12/25 0025.
 */
public class LycjServiceTest extends BaseTest{

    @Autowired
    private LycjService lycjService;


    @Test
    public void sendLoanInfoData() throws UnsupportedEncodingException {
        lycjService.sendLoanInfoData("2018-01-14",1,10);
    }

}