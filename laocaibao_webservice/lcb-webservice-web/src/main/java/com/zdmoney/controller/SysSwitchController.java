package com.zdmoney.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.zdmoney.assets.api.dto.subject.borrow.BorrowConfirmDto;
import com.zdmoney.common.Result;
import com.zdmoney.constant.BusiConstants;
import com.zdmoney.mapper.sys.SysSwitchMapper;
import com.zdmoney.marketing.common.RandomCharUtils;
import com.zdmoney.component.mq.ProducerService;
import com.zdmoney.service.customer.CustomerCenterService;
import com.zdmoney.service.transfer.BusiDebtTransferService;
import com.zdmoney.trace.utils.LcbTraceRunnable;
import com.zdmoney.webservice.api.common.MsgType;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import websvc.models.Model_500033;
import websvc.req.ReqHeadParam;
import websvc.req.ReqMain;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/sysSwitch")
public class SysSwitchController {

    @Autowired
    private ProducerService producerService;

    @Autowired
    private CustomerCenterService customerCenterService;

    @Autowired
    private BusiDebtTransferService busiDebtTransferService;

    @RequestMapping("/reloadParam")
    @ResponseBody
    public String reloadParam(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
        CacheManager cacheManager = CacheManager.getInstance();
        Cache cache = cacheManager.getCache(SysSwitchMapper.class.getCanonicalName());
        if (cache != null) {
            cacheManager.removeCache(SysSwitchMapper.class.getCanonicalName());
            return "重载成功！";
        } else {
            return "cache 不存在！";
        }
    }

    @RequestMapping("/test")
    @ResponseBody
    public String testSendingRegistrationMsg(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {
        JSONObject json = new JSONObject();
        json.put("cmNumber","00270011"+ RandomCharUtils.createRandomCharData(6));
        //json.put("redNo","hb00270011"+ RandomCharUtils.createRandomCharData(6));
        try {
            producerService.sendRegistrationMsg(json.toString());
        }catch (Exception e){
            e.printStackTrace();
        }
        return "success";
    }

    @RequestMapping("/testShare")
    @ResponseBody
    public String testShare(){
        customerCenterService.share("");//need to modify "share" method before testing, or you will encounter an error
        return "success";
    }

    @RequestMapping("/testSendWacaiInfoConfirmDataMsg")
    @ResponseBody
    public String testSendWacaiInfoConfirmDataMsg(){
        String str = "贷款";
        BorrowConfirmDto borrowConfirmDto = new BorrowConfirmDto();
        borrowConfirmDto.setPartnerNo("WACAI");
        borrowConfirmDto.setBorrowPurpose(str);
        borrowConfirmDto.setBorrowNo("LFFC2016060201");
        //注册实名完通知标的
        producerService.sendWacaiInfoConfirmDataMsg(JSON.toJSONString(borrowConfirmDto), MsgType.WACAI_INFO_CONFIRM_NOTICE);
        return "success";
    }

    @RequestMapping("/testSendCollectingDataMsg")
    @ResponseBody
    public String testSendCollectingDataMsg(){
        producerService.sendCollectingDataMsg("{\"id\":\"a1111\"}", MsgType.ACTIVATE_ORDER);
        return "success";
    }

    private ReqMain getReqMainFromManager(){
        ReqMain reqMain = new ReqMain();
        ReqHeadParam reqHeadParam = new ReqHeadParam();
        reqHeadParam.setPlatform(BusiConstants.LOGIN_TYPE_MANAGE);
        reqHeadParam.setUserAgent(BusiConstants.LOGIN_TYPE_MANAGE);
        reqMain.setReqHeadParam(reqHeadParam);
        return reqMain;
    }

    @RequestMapping("/testTransfer")
    public void testTransfer(HttpServletRequest request,HttpServletResponse respone){
        final StringBuilder sb = new StringBuilder();

       final ReqMain reqMain = getReqMainFromManager();
        Model_500033 cdtModel = new Model_500033();
        cdtModel.setCustomerId(Long.valueOf(133534));
        cdtModel.setOrderId(Long.valueOf(387741));
        cdtModel.setTransferDate(new Date());
        cdtModel.setTransferPrice(BigDecimal.valueOf(1500));
        reqMain.setReqParam(cdtModel);

        int tryTimes = 20;
        ExecutorService executorService = Executors.newFixedThreadPool(tryTimes);
        for(int i = 0;i < tryTimes;i++){
            final int n = i;
            executorService.execute(new LcbTraceRunnable() {
                @Override
                public void concreteRun() {
                    System.out.println("第" + n + "次：");
                    StringBuilder tmp = new StringBuilder();
                    tmp.append("第" + n + "次：");
                    try {
                        Result result = busiDebtTransferService.transfer(reqMain);
                        if(result == null || !result.getSuccess())
                            tmp.append(result == null ? "发生错误":result.getMessage());
                    } catch (Exception e) {
                        e.printStackTrace();
                        tmp.append(e.getMessage());
                    }
                    tmp.append("\r\n");
                    sb.append(tmp);
                }
            });
        }
        try {
            executorService.awaitTermination(10, TimeUnit.SECONDS);
            System.out.println(sb.toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        respone.setContentType("text/html; charset=utf-8");
        try {
            BufferedWriter bw = new BufferedWriter(respone.getWriter());
            bw.write( sb.toString());
            bw.flush();
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
