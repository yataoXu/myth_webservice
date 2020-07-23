package com.zdmoney.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Maps;
import com.zdmoney.conf.OpenConfig;
import com.zdmoney.service.WdzjService;
import com.zdmoney.trace.utils.TraceGenerator;
import com.zdmoney.webservice.api.dto.wdzj.LoanInfoDto;
import com.zdmoney.webservice.api.facade.ILcbGatewayFacade;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * 网贷之家接口
 */
@RestController
@RequestMapping("/openapi/wdzj")
@Slf4j
public class WdzjController {

    @Autowired
    private ILcbGatewayFacade lcbGatewayFacade;

    @Autowired
    private WdzjService wdzjService;

    @Autowired
    private OpenConfig openConfig;

    @RequestMapping("/loans")
    public Object getLoanInfo(LoanInfoDto loanInfoDto){
        Map<String, Object> params = Maps.newHashMap();
        params.put("date",loanInfoDto.getDate());
        params.put("page",loanInfoDto.getPage());
        params.put("pageSize",loanInfoDto.getPageSize());
        log.info("请求URL:" + openConfig.getWebserviceRemoteUrl() + "/wdzj/loans");
        String body = HttpUtil.createPost(openConfig.getWebserviceRemoteUrl() + "/wdzj/loans").header("traceId", TraceGenerator.generatorId()).form(params).execute().body();
//        ResultDto loanInfo = lcbGatewayFacade.getLoanInfo(loanInfoDto);
        JSONObject jsonObject = new JSONObject();
        if (StringUtils.isNotBlank(body)){
            jsonObject = JSONUtil.parseObj(body);
        }
        return  jsonObject;
    }

    @RequestMapping("/splitData")
    @ResponseBody
    public Object getSplitData(){
        wdzjService.getSplitData();
        return  "success!";
    }

    /**
     * 提前结清数据
     * @return
     */
    @RequestMapping("/earlyRepayment")
    @ResponseBody
    public Object earlyRepaymentData(String date,String page,String pageSize){
        Map<String,Object> params = Maps.newHashMap();
        params.put("startDate", StrUtil.isBlank(date)? DateUtil.format(DateUtil.offsetDay(DateUtil.date(),-1),"yyyy-MM-dd"):date);
        params.put("page",StrUtil.isBlank(page)?"1":page);
        params.put("pageSize",StrUtil.isBlank(pageSize)?"500":pageSize);
        String content = lcbGatewayFacade.earlyRepaymentData(params);
        return JSONUtil.parseObj(content);
    }

    @RequestMapping("/planLoan")
    public Object getPlanLoan(String projectId,String planId,String status){
        Map<String, Object> params = Maps.newHashMap();
        params.put("projectId",projectId);
        params.put("planId",planId);
        params.put("status",status);
        log.info("请求URL:"+openConfig.getWebserviceRemoteUrl()+"/wdzj/gainBorrowInfo");
        String body = HttpUtil.createPost(openConfig.getWebserviceRemoteUrl()+"/wdzj/gainBorrowInfo").header("traceId", TraceGenerator.generatorId()).form(params).execute().body();
        JSONObject jsonObject = new JSONObject();
        if (StringUtils.isNotBlank(body)){
            jsonObject = JSONUtil.parseObj(body);
        }
        return  jsonObject;
    }

    public static void main(String[] args) {
        String url = "http://localhost:8080/wdzj/loans";
        Map<String, Object> params = Maps.newHashMap();
        params.put("date","2018-06-07");
        params.put("page","1");
        params.put("pageSize","500");
        String body = HttpUtil.createPost(url).form(params).execute().body();
        System.out.println(body);
    }
}
