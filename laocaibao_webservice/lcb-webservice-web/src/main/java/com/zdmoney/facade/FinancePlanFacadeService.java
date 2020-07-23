package com.zdmoney.facade;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.zdmoney.assets.api.common.dto.AssetsResultDto;
import com.zdmoney.assets.api.dto.match.enums.DebtType;
import com.zdmoney.assets.api.facade.subject.ILCBSubjectFacadeService;
import com.zdmoney.common.Result;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.life.api.utils.JsonUtils;
import com.zdmoney.mapper.financePlan.BusiOrderSubMapper;
import com.zdmoney.mapper.financePlan.DebtDetailMapper;
import com.zdmoney.mapper.product.*;
import com.zdmoney.match.api.DefaultMatchApi;
import com.zdmoney.match.dto.MatchApiResult;
import com.zdmoney.match.dto.ResourcePart;
import com.zdmoney.match.dto.ResourcePool;
import com.zdmoney.models.BusiMatchResultInfo;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.financePlan.BusiOrderSub;
import com.zdmoney.models.product.BusiProduct;
import com.zdmoney.models.product.BusiProductContract;
import com.zdmoney.models.product.BusiProductSub;
import com.zdmoney.models.product.BusiProductTemplate;
import com.zdmoney.models.trade.BusiCollectInfo;
import com.zdmoney.models.transfer.BusiDebtTransfer;
import com.zdmoney.service.BusiFinancePlanService;
import com.zdmoney.service.CustomerMainInfoService;
import com.zdmoney.service.TradeService;
import com.zdmoney.service.transfer.BusiDebtTransferService;
import com.zdmoney.utils.CommonHelper;
import com.zdmoney.utils.CopyUtil;
import com.zdmoney.utils.MailUtil;
import com.zdmoney.utils.StringUtil;
import com.zdmoney.webservice.api.common.dto.PageResultDto;
import com.zdmoney.webservice.api.common.dto.ResultDto;
import com.zdmoney.webservice.api.dto.plan.*;
import com.zdmoney.webservice.api.facade.IFinancePlanFacadeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import websvc.exception.BusinessException;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by 00232384 on 2017/6/14.
 */
@Component
@Slf4j
public class FinancePlanFacadeService implements IFinancePlanFacadeService {

    @Autowired
    private BusiFinancePlanService busiFinancePlanService;

    @Autowired
    private TradeService tradeService;

    @Autowired
    private BusiProductTemplateMapper busiProductTemplateMapper;

    @Autowired
    private BusiProductMapper busiProductMapper;

    @Autowired
    private BusiSubjectInfoMapper busiSubjectInfoMapper;

    @Autowired
    private DefaultMatchApi defaultMatchApi;

    @Autowired
    private DebtDetailMapper debtDetailMapper;

    @Autowired
    private CustomerMainInfoService customerMainInfoService;

    @Autowired
    private BusiProductSubMapper busiProductSubMapper;

    @Autowired
    private BusiProductContractMapper busiProductContractMapper;

    @Autowired
    private BusiDebtTransferService debtTransferService;

    @Autowired
    private ILCBSubjectFacadeService lcbSubjectFacadeService;




    @Override
    public ResultDto<SpecialFinancialPlannerVO> gainSpecialFinancialPlannerInfo() {
        SpecialFinancialPlannerVO specialFinancialPlanner = busiFinancePlanService.querySpecialFinancePeopleCode();
        if (specialFinancialPlanner != null) {
            return new ResultDto(specialFinancialPlanner);
        } else {
            return new ResultDto<>("未配置理财人信息",false);
        }
    }

    @Override
    public ResultDto insertDebtDetail(BusiDebtDetailVO busiDebtDetail) {
        int n = busiFinancePlanService.insertDebtDetail(busiDebtDetail);
        if (n > 0) {
            return new ResultDto("插入债权详情成功",true);
        }
        return new ResultDto("插入债权详情失败",false);
    }

    @Override
    public ResultDto sendProduct(SendProductReqDTO sendProductDTO) {
        return busiFinancePlanService.sendProduct(sendProductDTO);
    }

    @Override
    public ResultDto<BuyBackDTO> buyBack(String subjectNo) {
        return busiFinancePlanService.buyBack(subjectNo);
    }

    @Override
    public PageResultDto<BusiDebtDetailVO> queryDeptProductDetail(DebtDetailReqDTO debtDetailDTO) {
        PageResultDto<BusiDebtDetailVO> pageResultDto = busiFinancePlanService.queryDeptProductDetail(debtDetailDTO);
        if (pageResultDto.isSuccess()) {
            return pageResultDto;
        }
        return new PageResultDto();
    }

    @Override
    public PageResultDto<BusiFundDetailVO> queryFundDetail(FundDetailReqDTO fundDetailDTO) {
        PageResultDto<BusiFundDetailVO> pageResultDto = busiFinancePlanService.queryFundDetail(fundDetailDTO);
        if (pageResultDto.isSuccess()) {
            return pageResultDto;
        }
        return new PageResultDto();
    }

    @Override
    public ResultDto sendRevokeProduct(String productCode) {
        return busiFinancePlanService.sendRevokeProduct(productCode);
    }


    @Override
    public ResultDto debtMatch(DebtMatchReqDTO debtMatchReqDTO) {
        Boolean flag = busiFinancePlanService.debtMatch(debtMatchReqDTO);
        if (flag) {
            return new ResultDto("债权匹配成功",true);
        }
        return new ResultDto("债权匹配失败",false);
    }

    @Override
    public ResultDto debtMatchNums(Long productId) {
        long matchNums;
        if (productId == null) {
            throw new BusinessException("理财计划Id不能为空！");
        }
        try{
             matchNums= busiFinancePlanService.debtMatchNums(productId);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return new ResultDto("查询理财计划匹配数量失败",false);
        }
        return new ResultDto("查询理财计划匹配数量成功",matchNums,true);
    }

    @Override
    public ResultDto matchResultOrder(MatchResultDto matchResultDto) {
       checkMatchResultOrder(matchResultDto);
        try {
            long masterId = busiFinancePlanService.saveMatchResult(matchResultDto);
//            busiFinancePlanService.matchResultOrder(matchResultDto,masterId);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            MailUtil.sendMail("撮合调用失败", JsonUtils.toJson(matchResultDto)+"\n异常原因:" + e);
            return new ResultDto("撮合调用失败!", false);
        }
        return new ResultDto("撮合调用成功!", true);
    }


    @Override
    public ResultDto matchResultOrderTask() {
        try {
            return busiFinancePlanService.matchResultOrderTask();
        }catch (Exception e){
            log.error(e.getMessage(),e);
            MailUtil.sendMail("撮合定时任务调用失败", JsonUtils.toJson("")+"\n异常原因:" + e);
            return new ResultDto("撮合定时任务调用失败!", false);
        }
    }


    @Override
    public ResultDto bidTenderRepeal(String orderNo,String tenderNo) {
        if (StringUtil.isEmpty(orderNo)||StringUtil.isEmpty(tenderNo)) {
            throw new BusinessException("参数不能为空！");
        }
        return busiFinancePlanService.bidTenderRepeal(orderNo, tenderNo);
    }

    @Override
    @Async
    public ResultDto repairMatchOrder(String financeIds) {
        try {
            Map<Long,Object> matchMap=busiFinancePlanService.repairMatchOrder(financeIds);
            Set<Long> keySet = matchMap.keySet();
            for(Long masterId:keySet){
                try {
                    busiFinancePlanService.matchResultOrder((BusiMatchResultInfo)matchMap.get(masterId));
                    Thread.sleep(10000);
                }catch (Exception e){
                    log.error(e.getMessage(),e);
                    MailUtil.sendMail("补单失败", masterId+"\n异常原因:" + e);
                }
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return new ResultDto("补单失败!", false);
        }
        return new ResultDto("补单完成!", true);
    }

    @Override
    public ResultDto bidTransferRepeal(String newOrderNo,String initOrderNo,String flowNum) {
        if (StringUtil.isEmpty(initOrderNo)) {
            throw new BusinessException("转让订单号不能为空！");
        }
        if (StringUtil.isEmpty(newOrderNo)) {
            throw new BusinessException("受让订单号不能为空！");
        }
        if (StringUtil.isEmpty(flowNum)) {
            throw new BusinessException("华瑞转让流水号不能为空！");
        }
        return busiFinancePlanService.bidTransferRepeal(newOrderNo,initOrderNo,flowNum);
    }


    @Override
    public ResultDto<MatchSpecialResultDto> matchSpecialResultOrder(MatchSpecialReqDto matchSpecialReqDto){
        return busiFinancePlanService.matchSpecialResultOrder(matchSpecialReqDto);
    }

    @Override
    public ResultDto sendDebtInfo(List<DebtQueueDTO> debtQueueDTO) {
        int n = busiFinancePlanService.sendDebtInfo(debtQueueDTO);
        if (n > 0) {
            return new ResultDto("推送债权信息成功",true);
        }
        return new ResultDto("推送债权信息失败",false);
    }

    @Override
    public ResultDto specialTransfer(List<SpecialTransferDebtDTO> specialTransferDebtList) {
        return busiFinancePlanService.specialTransfer(specialTransferDebtList);
    }

    @Override
    public ResultDto specialBuyBackOrder(MatchResultDto matchResultDto) {
        return busiFinancePlanService.specialBuyBackOrder(matchResultDto);
    }

    @Override
    public ResultDto earlyOut() {
        return busiFinancePlanService.earlyOut();
    }

    @Override
    public PageResultDto<BusiMatchExceptionVO> queryMatchException(MatchExceptionDTO matchExceptionDTO) {
        return busiFinancePlanService.queryMatchException(matchExceptionDTO);
    }


    @Override
    public PageResultDto<MatchFlowVO> queryMatchFlow(MatchFlowDTO matchFlowDTO) {
        return busiFinancePlanService.queryMatchFlow(matchFlowDTO);
    }

    @Override
    public ResultDto updateMatchExceptionByMap(Map<String, Object> map) {
        return busiFinancePlanService.updateMatchExceptionByMap(map);
    }

    @Override
    public ResultDto<List<SpecialFinancialPlannerVO>> gainSpecialFinancialPlannerInfoList() {
        List<SpecialFinancialPlannerVO> specialFinancialPlanners = busiFinancePlanService.querySpecialFinancePeopleList();
        if (CollectionUtils.isNotEmpty(specialFinancialPlanners)) {
            return new ResultDto(specialFinancialPlanners);
        } else {
            return new ResultDto<>("未配置理财人信息",false);
        }
    }


    @Override
    public ResultDto bidTenderCollect(HRbidTenderCollectDto hRbidTenderCollectDto){
        //校验参数
        String checkParamStr = CommonHelper.checkParam(hRbidTenderCollectDto);
        if (!"".equals(checkParamStr)){
            log.error(checkParamStr);
            throw new BusinessException(checkParamStr);
        }

        boolean result = tradeService.bidTenderCollect(hRbidTenderCollectDto);
        if (result == true){
            return new ResultDto("华瑞投标归集(放款初审)成功", result);
        }else {
            return new ResultDto("华瑞投标归集(放款初审)失败", result);
        }

    }


    @Override
    public void bidTenderCollectCallback(CollectOrderInfoDTO record){
        tradeService.bidTenderCollectCallback(record);
    }


    @Override
    public ResultDto timeTenderCollect(LoanReviewDTO loanReviewDTO){
        //校验参数
        String checkParamStr = CommonHelper.checkParam(loanReviewDTO);
        if (!"".equals(checkParamStr)){
            log.error(checkParamStr);
            throw new BusinessException(checkParamStr);
        }
        Result result = tradeService.timeTenderCollect(loanReviewDTO);
        String resultStr =result.getData()== null?"":result.getData().toString();

        if (result.getSuccess()){
            return new ResultDto("0000", resultStr, resultStr);
        }else {
            return new ResultDto("1111", resultStr, resultStr);
        }

    }

    @Override
    public void timeTenderCollectCallback(CollectOrderInfoDTO record){
        tradeService.timeTenderCollectCallback(record);
    }

    private MatchResultDto checkMatchResultOrder(MatchResultDto matchResultDto) {
        log.info("MatchResultDto异常参数:" + JsonUtils.toJson(matchResultDto));
        if (matchResultDto == null) {
            throw new BusinessException("MatchResultDto结果集不能为空！");
        }
        List<MatchSucResult> sucResultList = matchResultDto.getMatchSucResultList();
        if (CollectionUtils.isEmpty(sucResultList)) {
            throw new BusinessException("子撮合信息结果集不能为空！");
        }
        for (MatchSucResult matchSucResult : sucResultList) {
            if (org.apache.commons.lang3.StringUtils.isBlank(matchSucResult.getCapitalCode())) {
                throw new BusinessException("未查询到" + matchSucResult.getCapitalCode() + "对应的资金详情");
            }
            if (AppConstants.FinancePlan.DEBT_TYPE2.equals(matchSucResult.getDebtType())) {
                String initOrderNum = matchSucResult.getInitOrderNum();
                if (org.apache.commons.lang3.StringUtils.isBlank(initOrderNum)) {
                    throw new BusinessException("退出转让的上家订单号不能为空");
                }
                if (matchSucResult.getDebtWorth() == null) {
                    throw new BusinessException("债权价值不能为空");
                }
            }
        }
        return matchResultDto;
    }

    @Override
    public ResultDto queryCollectionReviewResult(CollectOrderInfoDTO record){
        log.info("查询放款审查流水参数：{}", JSON.toJSONString(record));
        //校验参数
        String checkParamStr = CommonHelper.checkParam(record);
        if (!"".equals(checkParamStr)){
            log.error(checkParamStr);
            throw new BusinessException(checkParamStr);
        }


        Map result =new HashMap();
        BusiCollectInfo busiCollectInfo = tradeService.queryCollectOrderInfo(record);
        if (busiCollectInfo != null){
            if ("0000".equals(busiCollectInfo.getCode())){
                result.put("status","1");
                result.put("msg","审核通过");
            }else {
                result.put("status","2");
                result.put("msg","审核不通过");
            }
        }
        else {
            result.put("status","0");
            result.put("msg","审核中");
        }
        return new ResultDto("查询成功",result);
    }


    @Override
    public ResultDto bidTenderRetryCollect(BidTenderRetryCollectDto record){
        log.info("重试放款审查流水参数：{}", JSON.toJSONString(record));
        //校验参数
        String checkParamStr = CommonHelper.checkParam(record);
        if (!"".equals(checkParamStr)){
            log.error(checkParamStr);
            return new ResultDto("1111", checkParamStr);
        }
        //放款初审重试
        if (record.getType() == 0){

            if (StringUtil.isEmpty(record.getPersonAccount())){
                return new ResultDto("1111", "用户编号不能为空");
            }
            HRbidTenderCollectDto hRbidTenderCollectDto = new HRbidTenderCollectDto();
            hRbidTenderCollectDto.setBidNo(record.getBidNo());
            hRbidTenderCollectDto.setPersonAccount(record.getPersonAccount());
            boolean result=tradeService.bidTenderCollect(hRbidTenderCollectDto,true);
            if (result){
                return new ResultDto("放款重试成功！");
            }else {
                return ResultDto.FAIL("放款重试失败！");
            }

        }
        //放款复审重试
        else if (record.getType() == 1){
            LoanReviewDTO loanReviewDTO =new LoanReviewDTO();
            loanReviewDTO.setBidNo(record.getBidNo());
            Result result = tradeService.timeTenderCollect(loanReviewDTO,true);
            return new ResultDto(result.getMessage(), result.getData(),result.getSuccess());
        }else {
            return new ResultDto("1111", "归集类型错误，0：华瑞投标归集(放款初审)；1：放款归集(放款复审)");
        }

    }



    @Override
    public void generateProductTask(){
        /*
        1、从BusiProductTemplate模板生成挖财BusiProduct信息
        2、撮合资源池初始化接口
        3、生成子产品信息productSub信息
        4、标的信息推送到撮合系统
        5、添加债权详情
         */

        try {

            Map param =new HashMap();
            List packedStatus =new ArrayList();
            packedStatus.add(0);
            packedStatus.add(2);
            param.put("packedStatus",packedStatus);
            List<BusiProductContract> busiProductContracts=  busiProductContractMapper.selectBusiProductContractByPackedStatus(param);
            //有可建产品标的信息 ，则生成产品
            if (busiProductContracts.size()>0){
                // 1、从模板中生成product信息
                BusiProduct busiProduct =new BusiProduct();
                BusiProductTemplate busiProductTemplate =new BusiProductTemplate();
                //获取挖财产品模板
                Long productType = busiProductTemplateMapper.queryWacaiProductType();
                busiProductTemplate.setProductType(productType);

                busiProductTemplate = busiProductTemplateMapper.selectOne(busiProductTemplate);
                CopyUtil.copyProperties(busiProduct, busiProductTemplate);

                String productName =busiProductMapper.queryProductNameForWacai();
                busiProduct.setProductName(productName);
                busiProduct.setCreditSource("WACAI");
                busiProduct.setYearRate(busiProductTemplate.getInitYearRate());
                try {
                    Integer raiseDay = busiProductTemplate.getRaiseDay();
                    //开售期
                    busiProduct.setSaleStartDate(new Date());
                    busiProduct.setSaleEndDate(DateUtil.offsetDay(new Date(),raiseDay));
                    //展示期
                    busiProduct.setShowStartDate(busiProduct.getSaleStartDate());
                    busiProduct.setShowEndDate(busiProduct.getSaleEndDate());
                    //起息 CLOSE_DAY
                    busiProduct.setInterestStartDate(DateUtil.parseDate(DateUtil.format(DateUtil.offsetDay(new Date(),1),"yyyy-MM-dd"))  );
                    busiProduct.setInterestEndDate(DateUtil.parseDate(DateUtil.format(DateUtil.offsetDay(busiProduct.getInterestStartDate(),busiProductTemplate.getCloseDay().intValue()-1),"yyyy-MM-dd")));
                }catch (Exception e){
                    e.printStackTrace();
                }

                busiProduct.setIncrementAmount(busiProduct.getIncrementAmount()==null?BigDecimal.ZERO:busiProduct.getIncrementAmount());
                busiProduct.setTopFlag(StringUtil.isEmpty(busiProduct.getTopFlag())?"0":busiProduct.getTopFlag());
                busiProduct.setHotSellFlag(StringUtil.isEmpty(busiProduct.getHotSellFlag())?"0":busiProduct.getHotSellFlag());
                busiProduct.setPcTop(StringUtil.isEmpty(busiProduct.getPcTop())?"0":busiProduct.getPcTop());
                busiProduct.setPcSort(busiProduct.getPcSort()==null?0:busiProduct.getPcSort());
                busiProduct.setIsRecommend(StringUtil.isEmpty(busiProduct.getIsRecommend())?"0":busiProduct.getIsRecommend());
                busiProduct.setTransferStatus(StringUtil.isEmpty(busiProduct.getTransferStatus())?"0":busiProduct.getTransferStatus());
                busiProduct.setIsArea(StringUtil.isEmpty(busiProduct.getIsArea())?"0":busiProduct.getIsArea());
                busiProduct.setPersonLoan(busiProduct.getPersonLoan()==null?0:busiProduct.getPersonLoan());
                busiProduct.setIsNewHand("2".equals(busiProduct.getLimitType().toString())?"1":"0");

                busiProduct.setYearRate(busiProductTemplate.getInitYearRate().add(busiProductTemplate.getAddInterest()));
                busiProduct.setInvestLower(busiProductTemplate.getInvestLower());
                busiProduct.setInvestUpper(busiProductTemplate.getInvestUpper());
                busiProduct.setRepayType(busiProductTemplate.getRepayType());
                busiProduct.setYearRateInit(busiProductTemplate.getInitYearRate());
                busiProduct.setCloseDay(busiProductTemplate.getCloseDay());
                busiProduct.setRuleId(busiProductTemplate.getRuleId());
                busiProduct.setRemark(busiProductTemplate.getRemark());
                busiProduct.setProductFeature(busiProductTemplate.getProductFeature());
                busiProduct.setMarketing(busiProductTemplate.getMarketing());
                busiProduct.setCooperativeDesc(busiProductTemplate.getCooperativeDesc());
                busiProduct.setProductDesc(busiProductTemplate.getProductDesc());
                busiProduct.setRiskMeasures(busiProductTemplate.getRiskMeasures());
                busiProduct.setScurityMode(busiProductTemplate.getScurityMode());
                //设置所有用户可购买
                busiProduct.setTargetConsumer(busiProductTemplate.getUserType().toString());
                busiProduct.setSubjectType(AppConstants.ProductSubjectType.FINANCE_PLAN);
                busiProduct.setPlanStatus(AppConstants.PRODUCT_PLAN_STATUS.PRODUCT_STATUS_12);
                busiProduct.setVmTotalInvestAmt(BigDecimal.ZERO);
                busiProduct.setVmTotalInvestPersonNumber(0L);
                busiProduct.setTotalInvestAmt(BigDecimal.ZERO);
                busiProduct.setProductPrincipal(BigDecimal.ZERO);
                busiProduct.setTotalInvestPerson(0);
                busiProduct.setIsTransfer(AppConstants.OrderTransferStatus.ORDER_NORMAL);
                busiProduct.setAuditFlag(AppConstants.ProductAuditStatus.PRODUCT_AUDIT_PASS);

                busiProductMapper.insertSelective(busiProduct);

                //将产品数据添加到子产品表
                BusiProductSub busiMainProductSub =new BusiProductSub();
                CopyUtil.copyProperties(busiMainProductSub, busiProduct);
                busiProductSubMapper.saveProductSubForWacai(busiMainProductSub);



                //2、资源池初始化接口
                ResourcePool resourcePool =new ResourcePool();
                resourcePool.setPoolId(busiProduct.getId().toString() );
                resourcePool.setPoolName(busiProduct.getProductName());
                resourcePool.setAppId(System.getProperty("app.id"));
                resourcePool.setCreateTime(new Date());
                MatchApiResult matchApiResult= defaultMatchApi.initResourcePool(resourcePool);
                if (!"000000".equals(matchApiResult.getCode())){
                    throw new BusinessException("资源池初始化失败，原因："+matchApiResult.getMessage());
                }

                //产品金额
                BigDecimal productPrincipal =BigDecimal.ZERO;
                //负载比金额
                BigDecimal liabilitiesRatePrincipal =BigDecimal.ZERO;
                //遍历未打包到产品的标的
                for (BusiProductContract busiProductContract : busiProductContracts){
                    param.put("oldPackedStatus",packedStatus);
                    param.put("subjectNo",busiProductContract.getSubjectNo());
                    //默认失败
                    param.put("packedStatus",2);
                    try {

                        //3、生成子产品信息productSub信息
                        tradeService.bidBuildProductForWacai(busiProductContract,busiProduct);

                        //4、标的信息推送到撮合系统
                        ResourcePart resourcePart = new ResourcePart();
                        resourcePart.setPoolId(resourcePool.getPoolId());
                        resourcePart.setAppId(resourcePool.getAppId());
                        resourcePart.setSourceId(busiProductContract.getSubjectNo());
                        resourcePart.setSourceValue(busiProductContract.getCollectAmount());
                        resourcePart.setCreateTime(new Date());
                        MatchApiResult matchApiResult1 = defaultMatchApi.putResourcePart(resourcePart);
                        if (!"000000".equals(matchApiResult.getCode())){
                            throw new BusinessException("资源方添加失败，原因："+matchApiResult1.getMessage());
                        }
                        //5、添加债权详情
                        BusiDebtDetailVO busiDebtDetailVO =new BusiDebtDetailVO();
                        busiDebtDetailVO.setInitSubjectNo(busiProductContract.getSubjectNo());
                        busiDebtDetailVO.setProductId(busiProduct.getId());
                        busiDebtDetailVO.setMatchDate(new Date());
                        busiDebtDetailVO.setLaunchDate(new Date());
                        //设置为1-新标的
                        busiDebtDetailVO.setDebtType(DebtType.getNewSubjectNo());
                        //设置为3-已撮合
                        busiDebtDetailVO.setStatus("3");
                        //标的募集金额 设置为 债权价值
                        busiDebtDetailVO.setDebtPrice(busiProductContract.getCollectAmount());
                        busiDebtDetailVO.setInitRate(busiProductContract.getYearRate().doubleValue());
                        busiDebtDetailVO.setPriority("1");
                        busiDebtDetailVO.setCurrHolder(busiProductContract.getCmNumber());
                        busiDebtDetailVO.setBorrowerNumber(busiProductContract.getCmNumber());
                        busiDebtDetailVO.setBorrowerName(busiProductContract.getBorrowerName());
                        busiDebtDetailVO.setBorrowerDate(busiProductContract.getCreateDate());
                        debtDetailMapper.insertDebtDetail(busiDebtDetailVO);
                        productPrincipal = productPrincipal.add(busiProductContract.getCollectAmount());
                        liabilitiesRatePrincipal =busiProductContract.getCollectAmount().multiply(busiProductContract.getLiabilitiesRate()).add(liabilitiesRatePrincipal);
                        param.put("packedStatus",3);
                        log.info("标的号：{} 对应子产品信息生成成功",busiProductContract.getSubjectNo());
                    }catch (Exception e){
                        log.error("标的号：{}生成对应子产品信息异常",busiProductContract.getSubjectNo(),e);
                    }finally {
                        busiSubjectInfoMapper.updatePackedForSubjectInfo(param);
                    }
                }

                AssetsResultDto<Integer> assetsResultDto = lcbSubjectFacadeService.getBorrowerRatingByDebtRatio(liabilitiesRatePrincipal.divide(productPrincipal,4,BigDecimal.ROUND_HALF_UP));
                if (assetsResultDto.isSuccess()){
                    String purchaseCondition = busiFinancePlanService.rankTransfer(new Long(assetsResultDto.getData()));
                    busiProduct.setProductRank(assetsResultDto.getData());
                    busiProduct.setPurchaseCondition(purchaseCondition);
                    busiMainProductSub.setProductRank(assetsResultDto.getData());
                    busiMainProductSub.setPurchaseCondition(purchaseCondition);
                }else {
                    log.error("挖财产品:{} 调用标的获取风险等级失败:{}",busiProduct.getProductName(),JSON.toJSONString(assetsResultDto));
                }

                busiProduct.setProductPrincipal(productPrincipal);
                BigDecimal productInterest = productPrincipal.multiply(busiProduct.getYearRate().multiply(new BigDecimal(busiProduct.getCloseDay()))).divide(new BigDecimal(365), 2, 1);
                busiProduct.setProductInterest(productInterest);
                busiProduct.setAuditFlag(AppConstants.ProductAuditStatus.PRODUCT_AUDIT_PASS);
                busiProduct.setUpLowFlag(AppConstants.ProductUpLowStatus.PRODUCT_UP);
                busiProduct.setPlanStatus(4);
                busiProductMapper.updateByPrimaryKey(busiProduct);
                busiMainProductSub.setProductInterest(productInterest);
                busiMainProductSub.setUpLowFlag(AppConstants.ProductUpLowStatus.PRODUCT_UP);
                busiMainProductSub.setIsNotify("1");
                //修改子产品备份数据金额
                busiMainProductSub.setProductPrincipal(productPrincipal);
                busiProductSubMapper.updateByPrimaryKey(busiMainProductSub);
                log.info("挖财产品：{} 创建成功！",busiProduct.getProductName());
            }


        }catch (Exception e){
            log.error("定时生成产品异常",e);
            MailUtil.sendMail("定时生成挖财产品异常", "定时生成挖财产品异常,失败原因:" + e.getMessage());
        }


    }

    @Autowired
    private BusiOrderSubMapper busiOrderSubMapper;

    public ResultDto<BidOrderInfoDto> getOrderDetail(String orderNo){
        ResultDto resultDto = new ResultDto();
        BidOrderInfoDto orderInfoDto = new BidOrderInfoDto();
        BusiOrderSub orderSub = busiOrderSubMapper.queryBusiOrderSubInfo(orderNo);
        if(orderSub == null) return new ResultDto("订单不存在", false);
        try {
            orderInfoDto.setPartnerNo(AppConstants.PARTNER_NO);
            fillInDtoWithOrderInfo(orderInfoDto, orderSub);
            fillInDtoWithTransferInfo(orderInfoDto, orderSub);
            resultDto.setData(orderInfoDto);
        }catch (Exception e){
            log.error(e.getMessage(), e);
            resultDto = new ResultDto("发生异常："+e.getMessage(), false);
        }
        return resultDto;
    }

    private void fillInDtoWithOrderInfo(BidOrderInfoDto orderInfoDto, BusiOrderSub orderSub){
        Long productId = orderSub.getProductId();
        BidOrderInfoDto.OrderType orderType = BidOrderInfoDto.OrderType.BID;
        if(orderSub.getProductType().equals(AppConstants.OrderProductType.FINANCE_PLAN_SUB)){
            productId = orderSub.getPlanId();
            orderType = BidOrderInfoDto.OrderType.FINANCIAL_PLAN_SUB;
        }else if(orderSub.getProductType().equals(AppConstants.OrderProductType.FINANCE_PLAN)){
            orderType = BidOrderInfoDto.OrderType.FINANCIAL_PLAN;
        }
        CustomerMainInfo customer = customerMainInfoService.findOne(orderSub.getCustomerId());
        BusiProductSub product = busiProductSubMapper.selectByPrimaryKey(productId);

        String subjectNo = StringUtils.isEmpty(orderSub.getSubjectNo()) ? product.getSubjectNo() : orderSub.getSubjectNo();
        orderInfoDto.setCustomerNo(customer.getCmNumber());
        orderInfoDto.setCustomerName(customer.getCmRealName());
        orderInfoDto.setCustomerIdNo(customer.getCmIdnum());
        orderInfoDto.setPhoneNo(customer.getCmCellphone());

        orderInfoDto.setDebtNo(orderSub.getDebtNo());
        orderInfoDto.setDebtType(orderSub.getDebtType());
        orderInfoDto.setOrderNo(orderSub.getOrderId());
        orderInfoDto.setSubjectNo(subjectNo);
        orderInfoDto.setOrderType(orderType);
        orderInfoDto.setOrderAmt(orderSub.getOrderAmt());
        orderInfoDto.setOrderTime(orderSub.getOrderTime());
        orderInfoDto.setYearRate(orderSub.getYearRate());
        orderInfoDto.setInterest(orderSub.getPrincipalinterest().subtract(orderSub.getOrderAmt()));
        orderInfoDto.setInterestStartDate(orderSub.getInterestStartDate());
        orderInfoDto.setInterestEndDate(orderSub.getInterestEndDate());
        orderInfoDto.setPlanId(orderSub.getPlanId());
        orderInfoDto.setProductName(product.getProductName());
        orderInfoDto.setProductId(orderSub.getProductId());
    }

    private void fillInDtoWithTransferInfo(BidOrderInfoDto orderInfoDto, BusiOrderSub orderSub){
        if(AppConstants.FinancePlan.DEBT_TYPE2.equals(orderSub.getDebtType())
                || orderSub.getTransferType().equals(AppConstants.OrderTransferStatus.ORDER_TRANSFER)){

            BusiDebtTransfer busiDebtTransfer = debtTransferService.getSuccessBusiDebtTransfer(
                    orderSub.getCustomerId(), orderSub.getOrderId(),AppConstants.OrderTransferDirect.ORDER_TRANSFER_TRANSFEREE);
            CustomerMainInfo transferor = customerMainInfoService.findOne(busiDebtTransfer.getTransferId());
            BusiOrderSub transferorOrder = busiOrderSubMapper.queryBusiOrderSubInfo(busiDebtTransfer.getInitOrderNo());

            orderInfoDto.setTransfered(Boolean.TRUE);
            orderInfoDto.setTransferNo(busiDebtTransfer.getTransferNo());
            orderInfoDto.setTransferDate(busiDebtTransfer.getUpdateDate());
            orderInfoDto.setTransferorNo(transferor.getCmNumber());
            orderInfoDto.setTransferorName(transferor.getCmRealName());
            orderInfoDto.setTransferorIdNo(transferor.getCmIdnum());
            orderInfoDto.setTransferorPhoneNo(transferor.getCmCellphone());
            orderInfoDto.setTransferorOrderNo(transferorOrder.getOrderId());
            orderInfoDto.setTransferorDebtNo(transferorOrder.getDebtNo());
        }
    }

}
