package com.zdmoney.service;

import com.zdmoney.service.sys.SysHeadService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by 00225181 on 2016/1/4.
 */
public class HeadTest{
    @Autowired
    SysHeadService sysHeadService;

    @Test
    public void testGetAll(){
        sysHeadService.findHeadDtoByNewsType(null);
    }
}
