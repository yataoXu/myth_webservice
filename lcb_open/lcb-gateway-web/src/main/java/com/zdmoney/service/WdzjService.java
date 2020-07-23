package com.zdmoney.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zdmoney.utils.MailUtil;
import com.zdmoney.webservice.api.common.dto.ResultDto;
import com.zdmoney.webservice.api.dto.wdzj.LoanInfoData;
import com.zdmoney.webservice.api.dto.wdzj.LoanSplitDto;
import com.zdmoney.webservice.api.dto.wdzj.LoanUserVo;
import com.zdmoney.webservice.api.facade.ILcbGatewayFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 网贷之家 数据源
 */
@Service
@Slf4j
public class WdzjService {

    @Autowired
    private ILcbGatewayFacade lcbGatewayFacade;

    /**
     * 校验网贷之家数据
     */
    public void checkWdzjData() {
		BigDecimal big_10000 = new BigDecimal(10000);
		//获取网贷之家 捞财宝昨日成交额
        BigDecimal wdzjMoney = this.getWdzjData();
		//查询wdzj_loan_info 昨日起息的总金额
        String lastMoney = lcbGatewayFacade.getLastMoney();
		BigDecimal bigDecimal = new BigDecimal(StrUtil.isBlank(lastMoney) ? "0" : lastMoney);
        //转换成万元并且4舍五入
		BigDecimal lcbMoney = bigDecimal.divide(big_10000, 2, BigDecimal.ROUND_HALF_UP);
		//查询busi_order_sub 昨日起息的总金额
		String orderLastMoney = lcbGatewayFacade.getOrderLastMoney();
		BigDecimal orderLastMoneyBigDecimal = new BigDecimal(StrUtil.isBlank(orderLastMoney) ? "0": orderLastMoney);
		//转换成万元并且4舍五入
		BigDecimal orderLastMoneyChange = orderLastMoneyBigDecimal.divide(big_10000, 2, BigDecimal.ROUND_HALF_UP);
		log.info("网贷之家数据和捞财宝数据对比>>>>>>>>>>>对比时间[{}],wdzjMoney[{}],wdzj_loan_info[{}],busi_order_sub[{}]",
				DateUtil.now(), wdzjMoney.toString(), lcbMoney.toString(), orderLastMoneyChange.toString());
		if (lcbMoney.compareTo(wdzjMoney) != 0 || lcbMoney.compareTo(orderLastMoneyChange) != 0) {
			log.info("网贷之家数据和捞财宝数据对比出现偏差>>>>>>>>>>>对比时间[{}],wdzjMoney[{}],wdzj_loan_info[{}],busi_order_sub[{}]",
					DateUtil.now(), wdzjMoney.toString(), lcbMoney.toString(), orderLastMoneyChange.toString());
			MailUtil.sendMail("网贷之家数据对比",
					"网贷之家数据和捞财宝数据对比出现偏差>>>>>>>>>>>对比时间:[" + DateUtil.now() + "],网贷之家昨日起息金额:[" + wdzjMoney.toString()
							+ "],捞财宝昨日临时表起息金额:[" + lcbMoney.toString() + "],捞财宝昨日真实起息金额:[" + orderLastMoneyChange
							.toString() + "]", null, false);
        }
    }

    /**
     * 返回网贷之家 捞财宝昨日成交额
     * @return
     */
    private BigDecimal getWdzjData(){
        String url = "https://shuju.wdzj.com/plat-info-target.html";
        Map params = Maps.newHashMap();
        params.put("wdzjPlatId",84);
        params.put("type",1);
        params.put("target1",1);
        params.put("target2",0);
        String body = HttpUtil.createPost(url).form(params).execute().body();
        Map<String,String> wdzjVolume = Maps.newHashMap();
        try {
            JSONObject jsonObject = JSONUtil.parseObj(body);
            JSONArray date = JSONUtil.parseArray(jsonObject.get("date"));
            JSONArray data1 = JSONUtil.parseArray(jsonObject.get("data1"));
            if (date.size() != data1.size()){
                log.error("网贷之家接口数据有误:content[{}]",body);
            }else {
                for(int i=0;i<date.size();i++){
                    wdzjVolume.put(date.get(i).toString(),data1.get(i).toString());
                }
            }
        }catch (Exception e){
            log.error("解析网贷之家接口报错:content[{}]",body,e);
        }
        String lastDay = DateUtil.format(DateUtil.offsetDay(DateUtil.date(), -1), "yyyy-MM-dd");
        String lastMoney = wdzjVolume.get(lastDay);
         return new BigDecimal(lastMoney==null?"0":lastMoney);
    }

    /**
     * 更新网贷之家数据
     */
    public void updateWdzjData(){
        int count = lcbGatewayFacade.countWdzjData();
        if (count == 0){
            getNoSplitInfoData();
            getNoSplitUserData();
        }else{
            log.info("时间[{}],updateWdzjData已经更新[{}]条,不执行!", DateUtil.now(),count);
        }
    }

    /**
     * 获取需要拆分的数据
     * @return
     */
    public void getSplitData(){
        int pageNo = 1;
        LoanSplitDto loanSplitDto = new LoanSplitDto();
        BigDecimal amtInit = new BigDecimal(83896.57);
        /*首次拉取标信息*/
        if(lcbGatewayFacade.countSplitUser(5) > 0){
            loanSplitDto.setIsFirst(0);
        }
        while(true){
            log.info("更新优选,蓝枫赛殷投标数据开始>>>>>>>pageNo[{}] , isFirst[{}]",pageNo,loanSplitDto.getIsFirst());
            List<LoanUserVo> res = Lists.newArrayList();
            loanSplitDto.setPageNo(pageNo);
            loanSplitDto.setPageSize(500);
            loanSplitDto.setIsSplit(0);
            ResultDto loanInfoData = lcbGatewayFacade.getLoanUserData(loanSplitDto);
            if(loanInfoData.isSuccess() && loanInfoData.getData()!=null){
                res.addAll(JSONUtil.parseArray(loanInfoData.getData()).toList(LoanUserVo.class));
            }
            if(res.size() == 0){
                log.info("更新优选,蓝枫赛殷投标数据已经结束>>>>>>>pageNo[{}] , isFirst[{}]",pageNo,loanSplitDto.getIsFirst());
                break;
            }else {
                List<LoanUserVo> loanUser = Lists.newArrayList();
                List<LoanInfoData> loanInfo = Lists.newArrayList();
                for (LoanUserVo re : res) {
                    Integer productType = re.getProductType();
                    String productId = re.getProjectId();
                    LoanInfoData loanInfoVo = new LoanInfoData();
                    if(productType == 1){
                        loanInfoVo = lcbGatewayFacade.getBusiProductInfo(productId);
                    }else if(productType == 2 ){
                        loanInfoVo = lcbGatewayFacade.getBusiProductSubInfo(productId);
                    }
                    if(loanInfoVo == null){
                        continue;
                    }
                    BigDecimal amount = new BigDecimal(re.getAmount());
                    int divide = amount.divide(amtInit, 0, BigDecimal.ROUND_DOWN).intValue();
                    int num = 0;
                    for(int i=0;i<divide;i++){
                        LoanUserVo loanUserVo = new LoanUserVo();
                        LoanInfoData loanInfoVo1 = new LoanInfoData();
                        BeanUtil.copyProperties(re,loanUserVo);
                        String projectId = loanUserVo.getProjectId();
                        String productIdSub = projectId + "_" + num + "_" + RandomUtil.randomString(6);
                        loanUserVo.setProjectId(productIdSub);
                        loanUserVo.setAmount(amtInit.doubleValue()); //设置拆分之后的金额
                        loanUserVo.setValidAmount(amtInit.doubleValue());
                        loanUserVo.setDataType(5);
                        loanUser.add(loanUserVo);

                        BeanUtil.copyProperties(loanInfoVo,loanInfoVo1);
                        loanInfoVo1.setAmount(amtInit.doubleValue());
                        loanInfoVo1.setTitle(loanInfoVo.getTitle()+"_"+num+"_"+ RandomUtil.randomString(6));
                        loanInfoVo1.setDataType(5);
                        loanInfoVo1.setProjectId(productIdSub);
                        loanInfoVo1.setPlanId(projectId);
                        loanInfoVo1.setRepaymentType(1); //优选,蓝枫赛殷 -> 一次性还本付息
                        loanInfoVo1.setUserName("BP_"+loanInfoVo1.getPlanId()+"_"+RandomUtil.randomString(6));
                        loanInfo.add(loanInfoVo1);
                        num += 1;
                    }
                    BigDecimal subtract = amount.subtract(amtInit.multiply(new BigDecimal(divide)));
                    if (subtract.compareTo(new BigDecimal(0))==1){
                        num+=1;
                        LoanUserVo loanUserVo = new LoanUserVo();
                        LoanInfoData loanInfoVo1 = new LoanInfoData();
                        BeanUtil.copyProperties(re,loanUserVo);
                        String projectId = loanUserVo.getProjectId();
                        String productIdSub = projectId+"_"+num+"_"+RandomUtil.randomString(6);
                        loanUserVo.setAmount(subtract.doubleValue()); //设置拆分之后的金额
                        loanUserVo.setValidAmount(subtract.doubleValue());
                        loanUserVo.setDataType(5);
                        loanUserVo.setProjectId(productIdSub);
                        loanUser.add(loanUserVo);

                        BeanUtil.copyProperties(loanInfoVo,loanInfoVo1);
                        loanInfoVo1.setAmount(subtract.doubleValue());
                        loanInfoVo1.setTitle(loanInfoVo.getTitle()+"_"+num+"_"+RandomUtil.randomString(6));
                        loanInfoVo1.setDataType(5);
                        loanInfoVo1.setProjectId(productIdSub);
                        loanInfoVo1.setPlanId(projectId);
                        loanInfoVo1.setRepaymentType(1); //优选,蓝枫赛殷 -> 一次性还本付息
                        loanInfoVo1.setUserName("BP_"+loanInfoVo1.getPlanId()+"_"+RandomUtil.randomString(6));
                        loanInfo.add(loanInfoVo1);
                    }

                }
                int i1 = lcbGatewayFacade.saveLoanInfo(loanInfo);
                int i = lcbGatewayFacade.saveLoanUser(loanUser);
                log.info("拆分优选,蓝枫赛殷标信息,共[{}]条,更新[{}]条",loanInfo.size(),i1);
                log.info("拆分优选,蓝枫赛殷投标信息,共[{}]条,更新[{}]条",loanUser.size(),i);
            }
            pageNo+=1;
        }
    }

    /**
     * 获取不需要拆分的数据
     * @return
     */
    private void getNoSplitInfoData(){
        int pageNo = 1;
        LoanSplitDto loanSplitDto = new LoanSplitDto();
        while(true){
            log.info("更新个贷,理财计划标数据开始>>>>>>>pageNo[{}] , isFirst[{}]",pageNo,loanSplitDto.getIsFirst());
            List<LoanInfoData> res = Lists.newArrayList();
            loanSplitDto.setPageNo(pageNo);
            loanSplitDto.setPageSize(1000);
            ResultDto loanInfoData = lcbGatewayFacade.getLoanInfoData(loanSplitDto);
            if(loanInfoData.isSuccess() && loanInfoData.getData()!=null){
                res.addAll(JSONUtil.parseArray(loanInfoData.getData()).toList(LoanInfoData.class));
            }
            if(res.size() == 0){
                break;
            }else {
                int i = lcbGatewayFacade.saveLoanInfo(res);
                log.info("更新个贷,理财计划标数据完成,共[{}]条,更新[{}]条,pageNo[{}],isFirst[{}]",res.size(),i,pageNo,loanSplitDto.getIsFirst());
            }
            pageNo+=1;
        }
    }

    /**
     * 获取不需要拆分的数据
     * @return
     */
    private void getNoSplitUserData(){
        int pageNo = 1;
        LoanSplitDto loanSplitDto = new LoanSplitDto();
        loanSplitDto.setIsFirst(0);
        while(true){
            log.info("更新个贷,理财计划投标数据开始>>>>>>>pageNo[{}] , isFirst[{}]",pageNo,loanSplitDto.getIsFirst());
            List<LoanUserVo> res = Lists.newArrayList();
            loanSplitDto.setPageNo(pageNo);
            loanSplitDto.setPageSize(1000);
            loanSplitDto.setIsSplit(1);
            ResultDto loanUserData = lcbGatewayFacade.getLoanUserData(loanSplitDto);
            if(loanUserData.isSuccess() && loanUserData.getData()!=null){
                res.addAll(JSONUtil.parseArray(loanUserData.getData()).toList(LoanUserVo.class));
            }
            if(res.size() == 0){
                break;
            }else {
                int i = lcbGatewayFacade.saveLoanUser(res);
                log.info("更新个贷,理财计划投标数据完成,共[{}]条,更新[{}]条,pageNo[{}],isFirst[{}]",res.size(),i,pageNo,loanSplitDto.getIsFirst());
            }
            pageNo+=1;
        }
    }
}
