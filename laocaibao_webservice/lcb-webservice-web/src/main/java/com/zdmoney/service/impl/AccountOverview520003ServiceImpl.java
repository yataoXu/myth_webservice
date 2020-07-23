package com.zdmoney.service.impl;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.common.Result;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.integral.api.common.dto.PageResultDto;
import com.zdmoney.integral.api.common.dto.ResultDto;
import com.zdmoney.integral.api.dto.IntegralAccountDto;
import com.zdmoney.integral.api.dto.coupon.CouponDto;
import com.zdmoney.integral.api.dto.coupon.CouponSearchDto;
import com.zdmoney.integral.api.dto.coupon.enm.CouponStatus;
import com.zdmoney.integral.api.dto.lcbaccount.*;
import com.zdmoney.integral.api.dto.product.IntegralProductDto;
import com.zdmoney.integral.api.facade.*;
import com.zdmoney.mapper.customer.CustomerMainInfoMapper;
import com.zdmoney.mapper.order.BusiOrderMapper;
import com.zdmoney.mapper.order.BusiOrderTempMapper;
import com.zdmoney.mapper.payment.PaymentCalendarMapper;
import com.zdmoney.mapper.trade.BusiTradeFlowMapper;
import com.zdmoney.message.api.common.dto.MessageResultDto;
import com.zdmoney.message.api.facade.IMsgMessageFacadeService;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.order.BusiOrder;
import com.zdmoney.models.order.BusiOrderTemp;
import com.zdmoney.models.payment.PaymentPlan;
import com.zdmoney.models.team.TeamMemberInfo;
import com.zdmoney.service.AccountOverview520003Service;
import com.zdmoney.service.BankCardService;
import com.zdmoney.service.CustomerMainInfoService;
import com.zdmoney.service.UserAssetService;
import com.zdmoney.service.payment.PaymentPlanService;
import com.zdmoney.service.team.TeamMemberInfoService;
import com.zdmoney.session.RedisSessionManager;
import com.zdmoney.utils.CoreUtil;
import com.zdmoney.utils.DateUtil;
import com.zdmoney.vo.*;
import com.zdmoney.web.dto.AccountBalanceDTO;
import com.zdmoney.web.dto.AccountInfoDTO;
import com.zdmoney.web.dto.AccountOverviewDTO;
import com.zdmoney.web.dto.AccountRecordDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import websvc.models.Model_520003;
import websvc.models.Model_520005;
import websvc.req.ReqMain;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by 00225181 on 2015/12/2.
 * 2.0账户概览接口
 */
@Service
@Slf4j
public class AccountOverview520003ServiceImpl implements AccountOverview520003Service {

    @Autowired
    CustomerMainInfoMapper customerMainInfoMapper;

    @Autowired
    ICouponFacadeService couponFacadeService;

    @Autowired
    IIntegralAccountFacadeService integralAccountFacadeService;

    @Autowired
    IIntegralProductFacadeService integralProductFacadeService;

    @Autowired
    BusiOrderTempMapper busiOrderTempMapper;

    @Autowired
    BusiOrderMapper busiOrderMapper;

    @Autowired
    IAccountFacadeService accountFacadeService;

    @Autowired
    BusiTradeFlowMapper tradeFlowMapper;

    @Autowired
    ConfigParamBean configParamBean;

    @Autowired
    TeamMemberInfoService teamMemberInfoService;

    @Autowired
    IVoucherFacadeService voucherFacadeService;

    @Autowired
    PaymentPlanService paymentPlanService;

    @Autowired
    private IAppointmentFacadeService appointmentFacadeService;
    @Autowired
    private BankCardService bankCardService;

    @Autowired
    private UserAssetService userAssetService;

    @Autowired
    private PaymentCalendarMapper paymentCalendarMapper;

    @Autowired
    private CustomerMainInfoService customerMainInfoService;

    @Autowired
    private RedisSessionManager redisSessionManager;

    @Autowired
    private IMsgMessageFacadeService iMsgMessageFacadeService;

    @Override
    public Result getOverview(ReqMain reqMain) throws Exception {
        AccountOverviewDTO dto = new AccountOverviewDTO();
        Model_520003 model_520003 = (Model_520003) reqMain.getReqParam();
        String customerId = model_520003.getCustomerId();
        if (!StringUtils.isEmpty(customerId)) {
            CustomerMainInfo customerMainInfo = customerMainInfoMapper.selectByPrimaryKey(Long.parseLong(customerId));
            if (customerMainInfo != null) {
                AccountAssetOverviewDto accountAssetOverviewDto = new AccountAssetOverviewDto();
                accountAssetOverviewDto.setAccountNo(customerMainInfo.getCmNumber());
                ResultDto<AccountAssetOverviewResultDto> accountResultDto = accountFacadeService.accountAssetOverview(accountAssetOverviewDto);
                if (accountResultDto != null && accountResultDto.isSuccess() && accountResultDto.getData() != null) {
                    //积分余额
                    BigDecimal integralBalance = accountResultDto.getData().getIntegralAmount();
                    dto.setIntegralAmount(integralBalance.toString());
                    //账户余额
                    BigDecimal accountBalance = accountResultDto.getData().getBalanceAmount();
                    dto.setAvailableAmount(CoreUtil.BigDecimalAccurate(accountBalance));
                    //持有资产
                    UserUnReceiveAsset unReceiveAsset = getHoldAsset(customerMainInfo);
                    BigDecimal holdAsset = unReceiveAsset.getUnReceivePrinciple();
                    dto.setHoldAmount(CoreUtil.BigDecimalAccurate(holdAsset));
                    //昨日收益
                    BigDecimal yesterdayIncome = getYesterdayIncome(customerMainInfo);
                    dto.setYesterdayIncome(CoreUtil.BigDecimalAccurate(yesterdayIncome));

                    //累计收益
                    //BigDecimal totalIncome = getTotalIncome(customerMainInfo);

                    //v4.1 累计收益=已收利息+用户历史资产红包积分累计收益
                    UserUnReceiveAsset userUnReceiveAsset = getTotalCouponAndIntegralAmt(customerMainInfo);
                    //历史资产红包积分累计收益
                    BigDecimal couponAndIntergralAmt = userUnReceiveAsset.getTotalCouponAndIntegralAmt();
                    //用户已收利息
                    BigDecimal totalInterest = userAssetService.computeHistoryInterest(customerMainInfo.getId());

                    BigDecimal totalIncome = couponAndIntergralAmt.add(totalInterest);
                    dto.setTotalIncome(CoreUtil.BigDecimalAccurate(totalIncome));

                    dto.setMallHomeUrl(configParamBean.getMallHomeUrl());
                    dto.setTaskCenterUrl(configParamBean.getTaskCenterUrl());
                    dto.setLifeServiceUrl(configParamBean.getLifeServiceUrl());
                    dto.setRiskTestUrl(configParamBean.getRiskTestUrl());
                    //dto.setConsultingUrl(configParamBean.getConsultingUrl());//资讯中心url

                    //v4.1总资产:用户资产+余额
                    BigDecimal totalAsset = new BigDecimal(0);
                    List<AssetInfo> assetInfoList = paymentCalendarMapper.queryAssetDetail(Long.parseLong(customerId));
                    if (CollectionUtils.isNotEmpty(assetInfoList)) {
                        for (AssetInfo asset : assetInfoList) {
                            totalAsset = totalAsset.add(asset.getInterset()).add(asset.getPrincipal());
                        }
                    }
                    dto.setTotalAmount(CoreUtil.BigDecimalAccurate(totalAsset.add(accountBalance)));
                    dto.setMemberLevel(customerMainInfo.getMemberLevel());

                    TeamMemberInfo memberInfo = teamMemberInfoService.getTeamMemberInfoByCustomerId(customerMainInfo.getId());
                    if (memberInfo != null) {
                        dto.setTeamHomeUrl(configParamBean.getTouchWebappHomeUrl() + "/commonDispatchRequest?method=820008&resultPage=front/team/myTeam&sessionToken=");
                    } else {
                        dto.setTeamHomeUrl(configParamBean.getTouchWebappHomeUrl() + "/investment?sessionToken=");
                    }
                    dto.setInviteFriendUrl(configParamBean.getInviteFriendUrl());
                    //获取预约券数量
                    Integer bespeakNum = accountResultDto.getData().getAppointmentNum();
                    dto.setBespeakNum(bespeakNum);
                    //获取现金券数量
                    Integer cashNum = accountResultDto.getData().getCashNum();
                    dto.setCashNum(cashNum);

                    boolean bindFlag = bankCardService.checkBindCard(customerMainInfo);
                    dto.setBindCard(bindFlag ? 1 : 0);
                    if ("A".equals(customerMainInfo.getRiskTestType()))
                        dto.setRiskTestType(AppConstants.riskType.RISK_TYPE_A);
                    else if ("B".equals(customerMainInfo.getRiskTestType()))
                        dto.setRiskTestType(AppConstants.riskType.RISK_TYPE_B);
                    else if ("C".equals(customerMainInfo.getRiskTestType()))
                        dto.setRiskTestType(AppConstants.riskType.RISK_TYPE_C);
                    else if ("D".equals(customerMainInfo.getRiskTestType()))
                        dto.setRiskTestType(AppConstants.riskType.RISK_TYPE_D);
                    else if ("E".equals(customerMainInfo.getRiskTestType()))
                        dto.setRiskTestType(AppConstants.riskType.RISK_TYPE_E);
                    else dto.setRiskTestType(AppConstants.riskType.RISK_TYPE_NO);

                    //当前时间范围段
                    dto.setTimeStatus(getTimeStatus());
                    dto.setTimeTips(getTimeTips(customerMainInfo));

                    // 待回款
                    Map paramsMap = new HashMap<>();
                    String repayDate = new SimpleDateFormat("MM月").format(new Date());
                    dto.setRepayDate(repayDate);

                    String currentDate = new SimpleDateFormat("yyyyMM").format(new Date());
                    paramsMap.put("repayDate", currentDate);
                    paramsMap.put("customerId", customerId);
                    List<AssetCalendar> waitReceiveList = paymentCalendarMapper.queryAssetCalendarByWaitReceive(paramsMap);
                    BigDecimal totalWaitReceiveAmt = new BigDecimal(0);
                    if (CollectionUtils.isNotEmpty(waitReceiveList)) {
                        for (AssetCalendar asset : waitReceiveList) {
                            totalWaitReceiveAmt = totalWaitReceiveAmt.add(asset.getPrincipalInterest());
                        }
                    }
                    dto.setRepayAmt(totalWaitReceiveAmt);
                    if (totalWaitReceiveAmt.compareTo(new BigDecimal(0)) == 1) {
                        dto.setShowRepayDateAndAmt("1");//显示
                    }

                    //结息日是否在活动期间内
                    if (dto.getDisplay().equals("0")) {
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        if (DateTime.now().toDate().compareTo(sdf.parse(configParamBean.getPromotionStartDate())) >= 0 && DateTime.now().toDate().compareTo(sdf.parse(configParamBean.getPromotionEndDate())) <= 0) {
                            Map map = Maps.newTreeMap();
                            map.put("customerId", model_520003.getCustomerId());
                            map.put("startDate", configParamBean.getRangeInterestStartDate());
                            map.put("endDate", configParamBean.getRangeInterestEndDate());
                            if ("1".equals(customerMainInfo.getUserLabel())) {
                                map.put("regulations", configParamBean.getReinvestAccountantRegulations());
                            } else {
                                map.put("regulations", configParamBean.getReinvestNetworkuserRegulations());
                            }
                            int busiOrders = busiOrderMapper.selectContinuedorder(map);
                            if (busiOrders > 0) {
                                //是否显示banner标志
                                dto.setDisplay("1");
                                dto.setImgUrl(configParamBean.getBannerImgUrl());
                                dto.setRediUrl(configParamBean.getRediUrl());
                            }
                        }
                    }
                }
            }
        }


        return Result.success(dto);
    }

    @Override
    public Result getAccountBalance(ReqMain reqMain) throws Exception {
        Model_520005 model_520005 = (Model_520005) reqMain.getReqParam();
        if (StringUtils.isEmpty(model_520005.getCustomerId())) {
            throw new BusinessException("用户编号不能为空！");
        } else {
            CustomerMainInfo mainInfo = customerMainInfoMapper.selectByPrimaryKey(Long.parseLong(model_520005.getCustomerId()));
            if (mainInfo != null) {
                AccountBalanceDTO dto = new AccountBalanceDTO();
                dto.setAccountBalance(getAccountBalance(mainInfo));
                AccountRecordSearchDto arsdto = new AccountRecordSearchDto();
                arsdto.setAccountNo(mainInfo.getCmNumber());
                arsdto.setPageNo(Integer.parseInt(model_520005.getPageNo()));
                arsdto.setPageSize(Integer.parseInt(model_520005.getPageSize()));
                arsdto.setRecordType(model_520005.getRecordType());
                PageResultDto<AccountRecordDto> resultDto = accountFacadeService.getRecordList(arsdto);
                if (resultDto.isSuccess()) {
                    List<AccountRecordDto> list = resultDto.getDataList();
                    for (AccountRecordDto ardto : list) {
                        AccountRecordDTO accountRecordDTO = new AccountRecordDTO();
                        accountRecordDTO.setAmount(ardto.getAmount());
                        accountRecordDTO.setCreateDate(ardto.getCreateDate());
                        accountRecordDTO.setRecordNum(ardto.getRecordNum());
                        accountRecordDTO.setDirection(ardto.getDirection());
                        accountRecordDTO.setRecordTypeDesc(ardto.getRecordType());
                        dto.getAccountRecordList().add(accountRecordDTO);
                    }
                    dto.setPageNo(resultDto.getPageNo());
                    dto.setTotalPage(resultDto.getTotalPage());
                    dto.setTotalRecord(resultDto.getTotalSize());
                }
                BigDecimal withdrawBalance = tradeFlowMapper.selectWithdrawBalance(mainInfo.getId());
                dto.setWithdrawBalance(withdrawBalance == null ? new BigDecimal("0") : withdrawBalance);
                return Result.success(dto);
            } else {
                throw new BusinessException("用户信息不存在！");
            }
        }
    }

    /*查询账户余额*/
    @Override
    public BigDecimal getAccountBalance(CustomerMainInfo customerMainInfo) throws Exception {
        ResultDto<AccountDto> resultDto = accountFacadeService.getAccountBalance(customerMainInfo.getCmNumber());
        if (resultDto.isSuccess()) {
            AccountDto dto = resultDto.getData();
            return dto.getBalance();
        } else {
            return new BigDecimal(0);
        }
    }

    /*计算有效红包总额*/
    public Integer getRedPacketBalance(CustomerMainInfo mainInfo) {
        Integer balance = 0;
        CouponSearchDto searchDto = new CouponSearchDto();
        searchDto.setAccountNo(mainInfo.getCmNumber());
        searchDto.setStatus(CouponStatus.AVAIL.name());
        ResultDto<List<CouponDto>> resultDto = couponFacadeService.getCouponsByAccountNo(searchDto);
        if (resultDto.isSuccess()) {
            for (CouponDto couponDto : resultDto.getData()) {
                balance += couponDto.getAmount();
            }
        }
        return balance;
    }

    /*统计用户持有资产及待收利息*/
    @Override
    public UserUnReceiveAsset getHoldAsset(CustomerMainInfo customerMainInfo) throws Exception {
        UserUnReceiveAsset asset = new UserUnReceiveAsset();
        Map<String, Object> map = Maps.newTreeMap();
        map.put("customerId", customerMainInfo.getId());
        String[] status = new StringBuilder(AppConstants.BusiOrderStatus.BUSIORDER_STATUS_0)
                .append(",").append(AppConstants.BusiOrderStatus.BUSIORDER_STATUS_9)
                .append(",").append(AppConstants.BusiOrderStatus.BUSIORDER_STATUS_17)
                .append(",").append(AppConstants.BusiOrderStatus.BUSIORDER_STATUS_18)
                .toString().split(",");
        map.put("status", status);
        UserAssetIntstAndTtlAmtVo vo = busiOrderMapper.selectEffectiveOrderAmtByCustomerID(map);
        UserRepaymentVo repayVo = busiOrderMapper.selectRepaymentByCustomerID(map);
        //计算持有资产
        BigDecimal holdAsset = vo.getTotalInvestAmt().add(repayVo.getUnreceivedPrincipal());
        //计算待收利息
        BigDecimal unReceiveInterest = vo.getTotalInterest().add(repayVo.getUnreceivedInterest());
        asset.setUnReceivePrinciple(holdAsset);
        asset.setUnReceiveInterest(unReceiveInterest);
        return asset;
    }

    /*用户单笔(标的订单或转让订单)持有本金及待收利息v4.1*/
    @Override
    public UserUnReceiveAsset getHoldAssets(CustomerMainInfo customerMainInfo, BusiOrderTemp busiOrderTemp) throws Exception {
        UserUnReceiveAsset asset = new UserUnReceiveAsset();
        Map<String, Object> map = Maps.newTreeMap();
        map.put("customerId", customerMainInfo.getId());
        String[] status = new StringBuilder(AppConstants.BusiOrderStatus.BUSIORDER_STATUS_0)
                .append(",").append(AppConstants.BusiOrderStatus.BUSIORDER_STATUS_9)
                .append(",").append(AppConstants.BusiOrderStatus.BUSIORDER_STATUS_17)
                .append(",").append(AppConstants.BusiOrderStatus.BUSIORDER_STATUS_18)
                .toString().split(",");
        map.put("status", status);
        if (busiOrderTemp != null) { //单笔订单待收
            map.put("orderNum", busiOrderTemp.getOrderId());
        }
        UserRepaymentVo repayVo = busiOrderMapper.selectRepaymentByCustomerIdAndOrderNum(map);
        //计算持有本金
        BigDecimal holdAsset = repayVo.getUnreceivedPrincipal();
        //计算待收利息
        BigDecimal unReceiveInterest = repayVo.getUnreceivedInterest();
        asset.setUnReceivePrinciple(holdAsset);
        asset.setUnReceiveInterest(unReceiveInterest);
        return asset;
    }

    /*查询积分余额*/
    public Integer getIntegralBalance(CustomerMainInfo mainInfo) throws Exception {
        ResultDto<IntegralAccountDto> integralAccountDto = integralAccountFacadeService.getIntegralAccount(mainInfo.getCmNumber().toString());
        if (integralAccountDto != null && integralAccountDto.isSuccess()) {
            return integralAccountDto.getData().getIntegral();
        } else {
            return 0;
        }
    }

    /*查询积分产品信息*/
    public IntegralProductDto getIntegralProductInfo() {
        //查询积分兑换比例
        ResultDto<IntegralProductDto> resultDto = integralProductFacadeService.getIntegralProductByNo("00001");
        if (resultDto.isSuccess()) {
            return resultDto.getData();
        } else {
            return null;
        }
    }

    @Override
    public BigDecimal getYesterdayIncome(CustomerMainInfo customerMainInfo) throws Exception {
        Map<String, Object> map = Maps.newTreeMap();
        map.put("customerId", customerMainInfo.getId());
        String status = new StringBuilder(AppConstants.BusiOrderStatus.BUSIORDER_STATUS_0)
                .append(",").append(AppConstants.BusiOrderStatus.BUSIORDER_STATUS_9)
                .append(",").append(AppConstants.BusiOrderStatus.BUSIORDER_STATUS_10)
                .append(",").append(AppConstants.BusiOrderStatus.BUSIORDER_STATUS_14).toString();
        map.put("status", status.split(","));
        map.put("nowDate", new Date());
        BigDecimal yesIncome = busiOrderMapper.selectYesterdayIncome(map);
        if (yesIncome == null) {
            yesIncome = new BigDecimal(0);
        }


        //v4.7增加智投宝-退出中状态的昨日收益
        Map<String, Object> finPlanMap = Maps.newTreeMap();
        finPlanMap.put("nowDate", new Date());
        finPlanMap.put("customerId", customerMainInfo.getId());
        BigDecimal finPlanYesIncome = busiOrderMapper.yesterdayFinPlanIncome(finPlanMap);
        if (finPlanYesIncome == null) {
            finPlanYesIncome = new BigDecimal(0);
        }

        yesIncome = yesIncome.add(finPlanYesIncome);
        return yesIncome;
    }

    @Override
    public BigDecimal getTotalIncome(CustomerMainInfo customerMainInfo) throws Exception {
        Map<String, Object> map = Maps.newTreeMap();
        map.put("customerId", customerMainInfo.getId());
        String status = new StringBuilder(AppConstants.BusiOrderStatus.BUSIORDER_STATUS_0)
                .append(",").append(AppConstants.BusiOrderStatus.BUSIORDER_STATUS_9).append(",").
                        append(AppConstants.BusiOrderStatus.BUSIORDER_STATUS_17).append(",").
                        append(AppConstants.BusiOrderStatus.BUSIORDER_STATUS_18).toString();
        map.put("status", status.split(","));
        map.put("nowDate", new Date());
        BigDecimal totalIncome = busiOrderMapper.selectTotalIncomeUnLocked(map);
//        map.remove("status");
//        map.put("status", AppConstants.BusiOrderStatus.BUSIORDER_STATUS_0 + "," + AppConstants.BusiOrderStatus.BUSIORDER_STATUS_9);
        List<BusiOrder> lockOrders = busiOrderMapper.selectLockedOrders(map);
        if (totalIncome == null) {
            totalIncome = new BigDecimal(0);
        }
        for (int i = 0; i < lockOrders.size(); i++) {
            BusiOrder orderTemp = lockOrders.get(i);
            BigDecimal orderIncome = new BigDecimal(0);
            try {
                BigDecimal investDay = new BigDecimal(DateUtil.getIntervalDays2(new Date(), orderTemp.getInterestStartDate()));//投资天数
                if (orderTemp.getRaiseDays() == null || orderTemp.getRaiseDays().intValue() == 0) {//未加息
                    orderIncome = orderTemp.getDayProfit().multiply(investDay);//每日利息*投资天数
                } else {
                    BigDecimal raiseDays = new BigDecimal(orderTemp.getRaiseDays());//加息天数
                    int compareResult = investDay.compareTo(raiseDays);
                    if (compareResult == 1) {//投资天数大于加息天数
                        orderIncome = orderTemp.getRaiseDayProfit().multiply(raiseDays).add(orderTemp.getDayProfit().multiply(investDay.subtract(raiseDays)));//加息后的每日收益*加息天数+未加息的每日收益*（投资天数-加息天数）
                    } else if (compareResult == 0 || compareResult == -1) {//投资天数小于等于加息天数
                        orderIncome = orderTemp.getRaiseDayProfit().multiply(investDay);//加息后的每日收益*加息天数
                    }
                }
            } catch (Exception e) {
                log.error("************计算累计收益相关参数有误************");
                log.error(e.getMessage());
                e.printStackTrace();
            }
            totalIncome = totalIncome.add(orderIncome);
        }
        BigDecimal subjectInterest = this.getExpectPayPlan(customerMainInfo);
        totalIncome = totalIncome.add(subjectInterest);
        return totalIncome == null ? new BigDecimal(0) : totalIncome;
    }

    @Override
    public BigDecimal extractAllBalance(String cmNumber) throws Exception {
        BigDecimal allBalance = new BigDecimal(0);
        ResultDto<AccountDto> resultDto = accountFacadeService.getWithdrawAmount(cmNumber);
        if (resultDto.isSuccess()) {
            AccountDto dto = resultDto.getData();
            if (dto.getBalance().compareTo(dto.getUnsettleAmt()) >= 0) {
                allBalance = dto.getBalance().subtract(dto.getUnsettleAmt());
            }
        }
        return allBalance.compareTo(new BigDecimal(0)) < 0 ? new BigDecimal(0) : allBalance;
    }

    @Override
    public AccountInfoDTO getAccountInfo(String userId) throws Exception {
        Long customerId = Convert.toLong(userId);
        CustomerMainInfo customerMainInfo = customerMainInfoService.checkCustomerId(customerId);
        AccountInfoDTO accountInfo = new AccountInfoDTO();
        String key = "account:info:" + userId;
        String val = redisSessionManager.get(key);
        if (StringUtils.isNotEmpty(val)) {
            accountInfo = JSON.parseObject(val, AccountInfoDTO.class);
        } else {
            accountInfo.setInviteFriendUrl(configParamBean.getInviteFriendUrl());
            //昨日收益
            BigDecimal yesterdayIncome = getYesterdayIncome(customerMainInfo);
            accountInfo.setYesterdayIncome(CoreUtil.BigDecimalAccurate(yesterdayIncome));
            //v4.1 累计收益=已收利息+用户历史资产红包积分累计收益
            UserUnReceiveAsset userUnReceiveAsset = getTotalCouponAndIntegralAmt(customerMainInfo);
            //历史资产红包积分累计收益
            BigDecimal couponAndIntergralAmt = userUnReceiveAsset.getTotalCouponAndIntegralAmt();
            //用户已收利息
            BigDecimal totalInterest = userAssetService.computeHistoryInterest(customerMainInfo.getId());
            BigDecimal totalIncome = couponAndIntergralAmt.add(totalInterest);
            // 累计收益
            accountInfo.setTotalIncome(CoreUtil.BigDecimalAccurate(totalIncome));
            redisSessionManager.put(key, JSON.toJSONString(accountInfo), 1, TimeUnit.DAYS);
        }

        String repayDate = new SimpleDateFormat("MM月").format(new Date());
        String currentDate = new SimpleDateFormat("yyyyMM").format(new Date());
        String repayKey = "repay:" + currentDate + userId;
        String repayVal = redisSessionManager.get(repayKey);
        if (StringUtils.isNotEmpty(repayVal)) {
            accountInfo = JSON.parseObject(repayVal, AccountInfoDTO.class);
        } else {
            //结息日是否在活动期间内
            if (accountInfo.getDisplay().equals("0")) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                if (DateTime.now().toDate().compareTo(sdf.parse(configParamBean.getPromotionStartDate())) >= 0 && DateTime.now().toDate().compareTo(sdf.parse(configParamBean.getPromotionEndDate())) <= 0) {
                    Map map = Maps.newTreeMap();
                    map.put("customerId", userId);
                    map.put("startDate", configParamBean.getRangeInterestStartDate());
                    map.put("endDate", configParamBean.getRangeInterestEndDate());
                    if ("1".equals(customerMainInfo.getUserLabel())) {
                        map.put("regulations", configParamBean.getReinvestAccountantRegulations());
                    } else {
                        map.put("regulations", configParamBean.getReinvestNetworkuserRegulations());
                    }
                    int busiOrders = busiOrderMapper.selectContinuedorder(map);
                    if (busiOrders > 0) {
                        //是否显示banner标志
                        accountInfo.setDisplay("1");
                        accountInfo.setImgUrl(configParamBean.getBannerImgUrl());
                        accountInfo.setRediUrl(configParamBean.getRediUrl());
                    }
                }
            }
            // 待回款
            Map paramsMap = new HashMap<>();
            accountInfo.setRepayDate(repayDate);
            paramsMap.put("repayDate", currentDate);
            paramsMap.put("customerId", customerId);
            List<AssetCalendar> waitReceiveList = paymentCalendarMapper.queryAssetCalendarByWaitReceive(paramsMap);
            BigDecimal totalWaitReceiveAmt = new BigDecimal(0);
            if (CollectionUtils.isNotEmpty(waitReceiveList)) {
                totalWaitReceiveAmt = waitReceiveList.stream().map(AssetCalendar::getPrincipalInterest).reduce(BigDecimal.ZERO, BigDecimal::add);
            }
            accountInfo.setRepayAmt(totalWaitReceiveAmt);
            if (totalWaitReceiveAmt.compareTo(new BigDecimal(0)) == 1) {
                accountInfo.setShowRepayDateAndAmt("1");//显示
            }
            redisSessionManager.put(repayKey, JSON.toJSONString(accountInfo), 5, TimeUnit.MINUTES);
        }
        AccountAssetOverviewDto accountAssetOverviewDto = new AccountAssetOverviewDto();
        accountAssetOverviewDto.setAccountNo(customerMainInfo.getCmNumber());
        ResultDto<AccountAssetOverviewResultDto> accountResultDto = accountFacadeService.accountAssetOverview(accountAssetOverviewDto);
        if (accountResultDto != null || accountResultDto.getData() != null || accountResultDto.isSuccess()) {
            //账户余额
            BigDecimal accountBalance = accountResultDto.getData().getBalanceAmount();
            accountInfo.setAvailableAmount(CoreUtil.BigDecimalAccurate(accountBalance));
            //持有资产
            UserUnReceiveAsset unReceiveAsset = getHoldAsset(customerMainInfo);
            accountInfo.setHoldAmount(CoreUtil.BigDecimalAccurate(unReceiveAsset.getUnReceivePrinciple()));
            //v4.1总资产:用户资产+余额
            BigDecimal totalAsset = new BigDecimal(0);
            List<AssetInfo> assetInfoList = paymentCalendarMapper.queryAssetDetail(customerId);
            if (CollectionUtils.isNotEmpty(assetInfoList)) {
                totalAsset = assetInfoList.stream().map(asset -> asset.getInterset().add(asset.getPrincipal())).reduce(BigDecimal.ZERO, BigDecimal::add);
            }
            //总资产
            accountInfo.setTotalAmount(CoreUtil.BigDecimalAccurate(totalAsset.add(accountBalance)));
            //获取预约券数量
            Integer bespeakNum = accountResultDto.getData().getAppointmentNum();
            //获取现金券数量
            Integer cashNum = accountResultDto.getData().getCashNum();
            // 福利小红点
            if (bespeakNum > 0 || cashNum > 0) {
                accountInfo.setShowWelfareDot(true);
            }
            // 消息小红点
            MessageResultDto<Integer> msgResult = iMsgMessageFacadeService.unReadCount(userId);
            if (msgResult.isSuccess()) {
                accountInfo.setShowMsgDot(msgResult.getData() > 0 ? true : false);
            }
        }
        accountInfo.setMemberLevel(customerMainInfo.getMemberLevel());
        return accountInfo;
    }

    /**
     * 计算标的预计累计
     *
     * @param customerMainInfo
     * @return
     */
    public BigDecimal getExpectPayPlan(CustomerMainInfo customerMainInfo) {
        BigDecimal expectPayPlan = BigDecimal.ZERO;
        Map<String, Object> map = Maps.newTreeMap();
        map.put("customerId", customerMainInfo.getId());
        map.put("status", AppConstants.BusiOrderStatus.BUSIORDER_STATUS_14);
        List<BusiOrder> lockOrders = busiOrderMapper.selectOrdersByProperties(map);
        Date curDate = new Date();
        if (!lockOrders.isEmpty()) {
            for (BusiOrder order : lockOrders) {
                Example example = new Example(PaymentPlan.class);
                example.createCriteria().andEqualTo("orderNum", order.getOrderId());
                example.setOrderByClause("curr_term asc");
                List<PaymentPlan> paymentPlans = paymentPlanService.findByExample(example);
                //当前日期在锁定期后，取所有未回款完成的利息
                if (DateUtil.compareStringDate(curDate, order.getInterestEndDate()) > 0) {
                    for (PaymentPlan plan : paymentPlans) {
                        if (plan.getRepayStatus().equals(AppConstants.PaymentPlanStatus.UNRETURN) || plan.getRepayStatus().equals(AppConstants.PaymentPlanStatus.RETURNING)) {
                            expectPayPlan = expectPayPlan.add(plan.getInterest());
                        }
                    }
                }
                //下一期还款日期
                Date lastDate = order.getInterestStartDate();
                if (DateUtil.compareStringDate(curDate, order.getInterestStartDate()) > 0 && DateUtil.compareStringDate(curDate, order.getInterestEndDate()) <= 0) {
                    for (PaymentPlan plan : paymentPlans) {
                        if (AppConstants.PaymentPlanStatus.RETURNED.equals(plan.getRepayStatus())
                                || AppConstants.PaymentPlanStatus.RETURNED_AHEAD.equals(plan.getRepayStatus())
                                || AppConstants.PaymentPlanStatus.RETURNED_TRANSFER.equals(plan.getRepayStatus())
                                || AppConstants.PaymentPlanStatus.RETURNED_TRANSFER_UP.equals(plan.getRepayStatus())
                                || AppConstants.PaymentPlanStatus.RETURNING_TRANSFER_UP.equals(plan.getRepayStatus())) {
                            lastDate = plan.getRepayDay();
                            continue;
                        }
                        if (plan.getRepayStatus().equals(AppConstants.PaymentPlanStatus.UNRETURN) || plan.getRepayStatus().equals(AppConstants.PaymentPlanStatus.RETURNING)) {
                            if (DateUtil.compareStringDate(curDate, plan.getRepayDay()) > 0) {
                                expectPayPlan = expectPayPlan.add(plan.getInterest());
                                lastDate = plan.getRepayDay();
                            } else {
                                expectPayPlan = expectPayPlan.add(order.getDayProfit().multiply(new BigDecimal(DateUtil.getIntervalDays2(curDate, lastDate))));
                                break;
                            }
                        }
                    }
                }
            }
        }
        return expectPayPlan;
    }

    /**
     * 获取当前时间段属于上下午或晚上
     */
    private int getTimeStatus() {
        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        if (hour >= 6 && hour < 12) {
            return 0;
        } else if (hour >= 12 && hour < 18) {
            return 1;
        } else {
            return 2;
        }
    }

    private String getTimeTips(CustomerMainInfo customerMainInfo) {
        if (getTimeStatus() == 0) {
            return "上午好," + customerMainInfo.getCmRealName();
        }
        if (getTimeStatus() == 1) {
            return "下午好," + customerMainInfo.getCmRealName();
        }
        if (getTimeStatus() == 2) {
            return "晚上好," + customerMainInfo.getCmRealName();
        }
        return "你好," + customerMainInfo.getCmRealName();
    }

    /*用户历史资产红包积分累计收益v4.1*/
    public UserUnReceiveAsset getTotalCouponAndIntegralAmt(CustomerMainInfo customerMainInfo) throws Exception {
        Map<String, Object> map = Maps.newTreeMap();
        map.put("customerId", customerMainInfo.getId());
        UserUnReceiveAsset userUnReceiveAsset = busiOrderMapper.getTotalCouponAndIntegralAmt(map);
        return userUnReceiveAsset;
    }

    /*用户待收加息累计收益v4.1*/
    public UserUnReceiveAsset getNoRecieveAmt(CustomerMainInfo customerMainInfo) throws Exception {
        Map<String, Object> map = Maps.newTreeMap();
        map.put("customerId", customerMainInfo.getId());
        UserUnReceiveAsset userUnReceiveAsset = busiOrderMapper.getNoRecieveAmt(map);
        return userUnReceiveAsset;
    }

    public static void main(String[] args) throws Exception {
        BigDecimal b = BigDecimal.ZERO.add(new BigDecimal("0.54"));
        // System.out.println(b);
    }


}
