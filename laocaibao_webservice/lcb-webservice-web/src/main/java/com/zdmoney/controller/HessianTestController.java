package com.zdmoney.controller;

import com.zdmoney.webservice.api.common.dto.ResultDto;
import com.zdmoney.webservice.api.facade.ICreditFacadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 标的充值提现接口（hessian）
 * Created by 46186 on 2018/10/7.
 */
@Controller
@RequestMapping("/huarui")
public class HessianTestController {
    @Autowired
    private ICreditFacadeService iCreditFacadeService;

    @RequestMapping("/withdraw")
    @ResponseBody
    public String withdraw(String customerNo, String payAmt, String pageUrl) throws Exception {

        ResultDto dto = iCreditFacadeService.withdraw(customerNo,payAmt, pageUrl);
        if(dto.getData() == null){
            System.out.println(dto.getMsg());
            return dto.getMsg();
        }
        System.out.println(dto.getData().toString());

        return dto.getData().toString();
    }

    @RequestMapping("/recharge")
    @ResponseBody
    public String recharge(String customerNo, Integer channelType, String payAmt, String pageUrl) throws Exception {

        ResultDto dto = iCreditFacadeService.recharge(customerNo, channelType, payAmt, pageUrl);
        if(dto.getData() == null){
            System.out.println(dto.getMsg());
            return dto.getMsg();
        }
        System.out.println(dto.getData().toString());

        return dto.getData().toString();
    }

}
