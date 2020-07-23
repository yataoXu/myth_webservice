package com.zdmoney.service;

import com.alibaba.fastjson.JSON;
import com.zdmoney.base.BaseTest;
import com.zdmoney.mapper.order.BusiOrderTempMapper;
import com.zdmoney.service.customer.CustomerCenterService;
import com.zdmoney.service.customer.CustomerValidateCodeService;
import com.zdmoney.service.sys.AppSysInitService;
import com.zdmoney.webservice.api.common.dto.ResultDto;
import com.zdmoney.webservice.api.facade.ICreditFacadeService;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class AccountTest extends BaseTest {

    @Autowired
    private CustomerValidateCodeService customerValidateCodeService;

    @Test
    public void senCodeTest(){
        customerValidateCodeService.sendValidateCode(1,"13671629914","", 0);
    }
}
