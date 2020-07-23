package com.zdmoney.controller;

import com.alibaba.fastjson.JSONObject;
import com.zdmoney.common.Result;
import com.zdmoney.match.api.IMatchApi;
import com.zdmoney.match.dto.MatchApiResult;
import com.zdmoney.match.dto.ResourcePart;
import com.zdmoney.match.dto.ResourcePool;
import com.zdmoney.service.CustRatingChangingRecordService;
import com.zdmoney.service.CustRatingUpPresentService;
import com.zdmoney.service.CustomerMainInfoService;
import com.zdmoney.utils.ApplicationEventSupport;
import com.zdmoney.utils.StringUtil;
import com.zdmoney.webservice.api.common.dto.ResultDto;
import com.zdmoney.webservice.api.dto.Asset.UnpaidDebtToTransferDto;
import com.zdmoney.webservice.api.facade.IFinancePlanFacadeService;
import com.zdmoney.webservice.api.facade.IManagerFacadeService;
import com.zdmoney.webservice.api.facade.IManagerTaskFacadeService;
import com.zdmoney.webservice.api.facade.ISpecialOperFacadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import websvc.req.ReqHeadParam;
import websvc.req.ReqMain;
import websvc.utils.JsonException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
 * Created by 00235374 on 2015/11/12.
 */
@Controller
@RequestMapping("/ApiTest")
public class HttpTestController {

    private static final String PKG_NAME = "websvc.servant";
    private static final String PKG_NAME_MODEL = "websvc.models";

    @Autowired
    CustomerMainInfoService customerMainInfoService;

    @Autowired
    private ISpecialOperFacadeService specialOperFacadeService;

    @Autowired
    private IFinancePlanFacadeService financePlanFacadeService;

    @RequestMapping(value = "/transmitRequest")
    //@ResponseBody
    public String TransmitRequest(HttpServletRequest request, HttpServletResponse response,
                                    RedirectAttributes attr) {
        try {
            //取出method函数
            String methodStr = request.getParameter("method");

            if (StringUtil.isEmpty(methodStr))
                return "";

            //取出相关接口类
//            Map<String, Object> paramObjects = request.getParameterMap();
//            paramObjects.remove("method");

            Map map = new HashMap();
            Enumeration paramNames = request.getParameterNames();
            while (paramNames.hasMoreElements()) {
                String paramName = (String) paramNames.nextElement();
                        String[] paramValues = request.getParameterValues(paramName);
                        if (paramValues.length == 1) {
                            String paramValue = paramValues[0];
                            if (paramValue.length() != 0) {
                                //System.out.println("参数：" + paramName + "=" + paramValue);
                                map.put(paramName, paramValue);
                    }
                }
            }
            map.remove("method");

            //拼接报文体
            //ReqParam reqParam = JackJsonUtil.strToObj(map.toString(), ReqParam.class);

            ReqMain reqMain = createReqMain();

            //拼装报文体
            String params = JSONObject.toJSONString(reqMain);
            //添加参数
            JSONObject jsonObject = JSONObject.parseObject(params);
            jsonObject.remove("reqParam");

            jsonObject.put("reqParam", map);//JSONObject.toJSONString(map)

            params = jsonObject.toJSONString();

            String requestStr = "/Api/requestDeal?arg0=" + methodStr + "&arg1=" + params;
            System.out.println(requestStr);

            //return "redirect:"+requestStr;
            attr.addAttribute("arg0", methodStr);
            attr.addAttribute("arg1", params);
            return "redirect:/Api/requestDeal";
            //return new ModelAndView(requestStr);
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    private ReqMain createReqMain( ) {
        ReqMain reqMain = new ReqMain();
        ReqHeadParam reqHeadParam = new ReqHeadParam();
        reqHeadParam.setUserAgent("捞财宝 5.0.0(iOS;8.3;zh_CN)");
        reqHeadParam.setSessionToken("na/2Zldqn6gfMzznMW7wW/9Bxjgo8Hz1fRI8KodwI1I+eSg2AaDO737p6DzLmV3i");
        reqHeadParam.setToken("na/2Zldqn6gfMzznMW7wW/9Bxjgo8Hz1fRI8KodwI1I+eSg2AaDO737p6DzLmV3i");
        reqHeadParam.setVersion("5.0.0");
        reqHeadParam.setMechanism("证大");
        reqHeadParam.setPlatform("App");
        reqHeadParam.setTogatherType("证大无线");
        reqHeadParam.setOpenchannel("AppStore");
        reqMain.setReqHeadParam(reqHeadParam);
        reqMain.setProjectNo("Lc_WS2015");
        reqMain.setReqTimestamp("");
        reqMain.setReqUrl("");
        reqMain.setSign("126e8f8b81ad99ac0a33b7b2c78aa29f");
        reqMain.setSn("Lc_WS2015-20150701143755-75763");

        return reqMain;
    }

    @RequestMapping(value = "/testRequest")
    @ResponseBody
    public String TransmitRequest(HttpServletRequest request, HttpServletResponse response) {
        return "test success";
    }

    @RequestMapping(value = "/initPwd")
    @ResponseBody
    public String InitUserPwd(HttpServletRequest request,
                              HttpServletResponse response,
                              @RequestParam(value ="customerId" ,required = false) String customerId ) {

        int updateNum = customerMainInfoService.initCustomerData(customerId);
        return "修改数据：" + updateNum;
    }

    @RequestMapping(value = "/testSpecialTransfer")
    @ResponseBody
    public String testSpecialTransfer(){
        UnpaidDebtToTransferDto dto = new UnpaidDebtToTransferDto();
        dto.setDebtWorth(BigDecimal.valueOf(2552.3));
        dto.setOrderAmt(BigDecimal.valueOf(2552.3));
        dto.setOrderNo("C8888602702201710121442097280");
        dto.setPeriods(Arrays.asList(2,3));
        specialOperFacadeService.transferUnpaidDebt(dto);
        return "success";
    }

    @RequestMapping(value = "/testCorrectFailedOpers")
    @ResponseBody
    public String testCorrectFailedOpers(){
        UnpaidDebtToTransferDto dto = new UnpaidDebtToTransferDto();
        dto.setDebtWorth(BigDecimal.valueOf(2552.3));
        dto.setOrderAmt(BigDecimal.valueOf(2552.3));
        dto.setOrderNo("C8888602702201710121442097280");
        dto.setPeriods(Arrays.asList(2,3));
        specialOperFacadeService.correctFailedOpers(dto);
        return "success";
    }

    @RequestMapping(value = "/checkOperResult")
    @ResponseBody
    public String checkOperResult(String initOrderNo){
        ResultDto resultDto = specialOperFacadeService.checkOperResult(initOrderNo);
        return resultDto.getCode()+"-"+resultDto.getMsg()+"-"+resultDto.getData();
    }

    @Autowired
    private IManagerTaskFacadeService managerTaskFacadeService;

    @RequestMapping(value = "/testTransferCreditTask")
    @ResponseBody
    public String testTransferCreditTask(){
        try {
            managerTaskFacadeService.finishCreditTransfer();
        }catch (Exception e){
            e.printStackTrace();
            return "failed";
        }
        return "success";
    }

    @Autowired
    private CustRatingChangingRecordService custRatingChangingRecordService;

    @RequestMapping(value = "/testUpdateCustomersRating")
    @ResponseBody
    public void testUpdateCustomersRating(String ids){
        custRatingChangingRecordService.checkAndUpdateCustomerRating(ids);
    }

    @Autowired
    private CustRatingUpPresentService custRatingUpPresentService;
    @RequestMapping(value = "/testSendRatingUpPresents")
    @ResponseBody
    public void testSendRatingUpPresents(){
        custRatingUpPresentService.sendRatingUpPresents();
    }

    @RequestMapping(value = "/testSendRatingChangedMsg")
    @ResponseBody
    public void testSendRatingChangedMsg(){
        custRatingChangingRecordService.sendCustomerRatingChangingMsg();
    }

    @RequestMapping(value = "/testBuyingRemainingOfWacaiProduct")
    @ResponseBody
    public void testBuyingRemainingOfWacaiProduct(){
        managerTaskFacadeService.buyRemainingPartOfWacaiProduct();
    }

    @RequestMapping(value = "/testOrderSucceeded")
    @ResponseBody
    public void testOrderSucceeded(String orderNum){
        ApplicationEventSupport.publishApplicationEvent(new ApplicationEventSupport.NewOrderEvent(orderNum));
    }

    @Autowired
    private IManagerFacadeService managerFacadeService;

    @RequestMapping(value = "/rematch")
    @ResponseBody
    public void rematch(String orderNum){
        managerFacadeService.rematchOrderAndCredit(orderNum);
    }

    @Autowired
    private IMatchApi matchApi;
    @RequestMapping(value = "/putMatchCredit")
    @ResponseBody
    public void putMatchCredit(){
        ResourcePool rp = new ResourcePool();
        rp.setAppId(System.getProperty("app.id"));
        rp.setPoolId("66266");
        rp.setPoolName(rp.getAppId());
        MatchApiResult matchApiResult = matchApi.initResourcePool(rp);

        /*ResourcePart resourcePart = new ResourcePart();
        resourcePart.setSourceId("wn_test1");
        resourcePart.setAppId(rp.getAppId());
        resourcePart.setPoolId(rp.getPoolId());
        resourcePart.setSourceValue(BigDecimal.valueOf(2000));
        resourcePart.setCreateTime(new Date());
        MatchApiResult matchApiResult1 = matchApi.putResourcePart(resourcePart);*/

        ResourcePart resourcePart1 = new ResourcePart();
        resourcePart1.setSourceId("wn_test7");
        resourcePart1.setAppId(rp.getAppId());
        resourcePart1.setPoolId(rp.getPoolId());
        resourcePart1.setSourceValue(BigDecimal.valueOf(2000));
        resourcePart1.setCreateTime(new Date());
        MatchApiResult matchApiResult2 = matchApi.putResourcePart(resourcePart1);

    }

    @RequestMapping(value = "/testTenderSucceededListener")
    @ResponseBody
    public void testTenderSucceededListener(String orderNum){
        ApplicationEventSupport.publishApplicationEvent(new ApplicationEventSupport.TenderSucceededEvent(orderNum));
    }


    @RequestMapping(value = "/buidWacaiProduct")
    @ResponseBody
    public String buidWacaiProduct(){
        try {
            financePlanFacadeService.generateProductTask();
            return JsonException.toJsonStr(Result.success("定时任务创建挖财产品完成！"));
        }catch (Exception e){
            e.printStackTrace();
            return JsonException.toJsonStr(Result.fail("定时任务创建挖财产品失败！"));
        }
    }

}
