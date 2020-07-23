package com.zdmoney.controller;

import com.zdmoney.service.wdzj.LoanInfoService;
import com.zdmoney.webservice.api.dto.wdzj.LoanInfoDto;
import com.zdmoney.webservice.api.facade.ICustomerInfoFacadeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@Controller
@RequestMapping("/wdzj")
public class WdzjController {

    @Autowired
    private LoanInfoService loanInfoService;

    @Autowired
    private ICustomerInfoFacadeService customerInfoFacadeService;

    @RequestMapping("/loans")
    @ResponseBody
    public String getLoanInfo(LoanInfoDto loanInfoDto){
        return loanInfoService.getLoanInfo(loanInfoDto);
    }

    @RequestMapping("/gainBorrowInfo")
    @ResponseBody
    public String gainBorrowInfo(String projectId,String planId,String status){
        String result = "";
        try {
            result = loanInfoService.gainBorrowInfo(projectId,Long.parseLong(planId),status);
        }
        catch (Exception e){
            log.error("网贷之家获取借款数据异常:{}",e.getMessage(),e);
        }
        return result;
    }

    @RequestMapping("/withdrawThawJob")
    @ResponseBody
    public void withdrawThawJob(){
        try {
            customerInfoFacadeService.withdrawThawJob();
        }
        catch (Exception e){
            e.printStackTrace();
            log.error("网贷之家获取借款数据异常:{}",e.getMessage(),e);
        }
    }
}
