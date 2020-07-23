package com.zdmoney.service;

import com.zdmoney.service.sys.AppSysInitService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by 00225181 on 2016/1/4.
 */
public class InitTest{

    @Autowired
    AppSysInitService appSysInitService;

    @Test
    public void test(){
        try {
//             appSysInitService.init("");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
