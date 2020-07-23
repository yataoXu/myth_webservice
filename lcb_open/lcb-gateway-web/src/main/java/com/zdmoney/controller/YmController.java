package com.zdmoney.controller;/**
 * Created by pc05 on 2017/11/24.
 */

import cn.hutool.http.HttpRequest;
import com.zdmoney.annotation.SignAuth;
import com.zdmoney.service.YmService;
import com.zdmoney.utils.SignUtils;
import com.zdmoney.webservice.api.common.dto.ResultDto;
import com.zdmoney.webservice.api.dto.ym.BusiProductDto;
import com.zdmoney.webservice.api.dto.ym.BusiRebateDto;
import com.zdmoney.webservice.api.dto.ym.CustomerMainInfoDto;
import com.zdmoney.webservice.api.dto.ym.ViewOrderDto;
import com.zdmoney.webservice.api.facade.ILcbGatewayFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.TreeMap;

/**
 * 描述 : 大拇指助手接口
 *
 * @author : huangcy
 * @create : 2017-11-24 14:53
 * @email : huangcy01@zendaimoney.com
 **/
@RestController
@RequestMapping("/openapi")
public class YmController {

    @Autowired
    private ILcbGatewayFacade lcbGatewayFacade;

    @Autowired
    private YmService ymService;

    @RequestMapping(value = "/products", method = {RequestMethod.GET}, produces = {"application/json"})
    @ResponseBody
    @SignAuth
    public ResultDto getProductInfo(BusiProductDto busiProductDto) {
        return lcbGatewayFacade.getProductInfo(busiProductDto);
    }


    @RequestMapping(value = "/members/rebates", method = {RequestMethod.GET}, produces = {"application/json"})
    @ResponseBody
    @SignAuth
    public ResultDto getRebateInfo(BusiRebateDto busiRebateDto) {
        return lcbGatewayFacade.getRebateInfo(busiRebateDto);
    }

    @RequestMapping(value = "/mumbers", method = {RequestMethod.GET}, produces = {"application/json"})
    @ResponseBody
    @SignAuth
    public ResultDto getCustomerInfo(CustomerMainInfoDto customerMainInfoDto) {
        return lcbGatewayFacade.getCustomerInfo(customerMainInfoDto);
    }

    @RequestMapping(value = "/orders", method = {RequestMethod.GET}, produces = {"application/json"})
    @ResponseBody
    @SignAuth
    public ResultDto getOrderInfo(ViewOrderDto viewOrderDto) {
        return lcbGatewayFacade.getOrderInfo(viewOrderDto);
    }

    @RequestMapping(value = "/entry", method = {RequestMethod.POST})
    @ResponseBody
//    @SignAuth
    public Object entry(String username, String idNum, String entryTime,String isStaff) {
        return ymService.entry(username,idNum,entryTime,isStaff);
    }

    @RequestMapping(value = "/quit", method = {RequestMethod.POST})
    @ResponseBody
//    @SignAuth
    public Object quit(String username,String idNum,String quitTime) {
        return ymService.quit(username,idNum,quitTime);
    }

    public static void main(String[] args) {
        Map<String, Object> reqParams = new TreeMap<>();
        reqParams.put("username","季向忠");
        reqParams.put("idNum","320625197003203177");
        reqParams.put("quitTime","2019-03-11");
//        reqParams.put("startTime", "2014-05-23 16:25:11");
//        reqParams.put("endTime", "2018-05-23 16:50:11");
//        reqParams.put("pageNo", "1");
//        reqParams.put("pageSize", "9999");
        reqParams.put("sign", SignUtils.getSign(reqParams, "bGNiLW9wZW4="));
//        reqParams.put("sign","emVuZGFpbW9uZXktbGNiLW9wZW4=");
        String url = "http://172.17.34.10:8084/lcb-open/openapi/mumbers?startTime=2016-11-01 09:33:11&endTime=2017-11-29 09:33:11&pageNo=1&pageSize=10&sign=987576E2620E335D86803D357E399591";
        url = "http://localhost:8083/openapi/entry";
        url="http://localhost:8099/lcb_open/openapi/quit";
//        String body = HttpRequest.post(url)
//                .header(Header.USER_AGENT, "Hutool http")
//                .form(reqParams)
//                .execute().body();
        String s = HttpRequest.post(url).form(reqParams).execute().body();
        System.out.println(s);
    }

}
