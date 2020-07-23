package com.zdmoney.service.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Maps;
import com.zdmoney.common.Result;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.constant.BusiConstants;
import com.zdmoney.constant.OrderConstants;
import com.zdmoney.enm.OrderGenerateType;
import com.zdmoney.enm.OrderType;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.facade.InnerEmployeeService;
import com.zdmoney.helper.SerialNumberGenerator;
import com.zdmoney.integral.api.common.dto.ResultDto;
import com.zdmoney.integral.api.dto.appointment.AppointmentNumDto;
import com.zdmoney.integral.api.facade.IAppointmentFacadeService;
import com.zdmoney.mapper.BusiMallMapper;
import com.zdmoney.mapper.bank.CustomerBankAccountMapper;
import com.zdmoney.mapper.customer.CustomerMainInfoMapper;
import com.zdmoney.mapper.financePlan.BusiOrderSubMapper;
import com.zdmoney.mapper.product.BusiProductContractMapper;
import com.zdmoney.mapper.product.BusiProductLimitMapper;
import com.zdmoney.mapper.product.BusiProductMapper;
import com.zdmoney.mapper.product.BusiProductSubMapper;
import com.zdmoney.models.BusiMall;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.financePlan.BusiOrderSub;
import com.zdmoney.models.order.BusiOrderTemp;
import com.zdmoney.models.product.BusiProductContract;
import com.zdmoney.models.product.BusiProductLimit;
import com.zdmoney.models.product.BusiProductSub;
import com.zdmoney.models.sys.SysParameter;
import com.zdmoney.models.transfer.BusiDebtTransfer;
import com.zdmoney.service.*;
import com.zdmoney.service.order.OrderService;
import com.zdmoney.service.sys.SysStaffInfoService;
import com.zdmoney.service.transfer.BusiDebtTransferService;
import com.zdmoney.utils.*;
import com.zdmoney.vo.BusiBeforeOrderVo;
import com.zdmoney.vo.BusiProductRuleVo;
import com.zdmoney.vo.OrderVo;
import com.zdmoney.webservice.api.common.CustomerAccountType;
import com.zdmoney.webservice.api.dto.plan.MatchSucResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import websvc.req.ReqMain;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by 00225181 on 2016/3/16.
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private CustomerMainInfoMapper customerMainInfoMapper;
    @Autowired
    private BusiProductMapper busiProductMapper;
    @Autowired
    private SysStaffInfoService sysStaffInfoService;
    @Autowired
    private BusiOrderService busiOrderService;
    @Autowired
    private CustomerMainInfoService customerMainInfoService;
    @Autowired
    private CustomerBankAccountMapper customerBankAccountMapper;
    @Autowired
    private BusiOrderTempService busiOrderTempService;
    @Autowired
    private BusiProductLimitMapper busiProductLimitMapper;
    @Autowired
    private SysParameterService sysParameterService;
    @Autowired
    private BusiDebtTransferService busiDebtTransferService;
    @Autowired
    private BusiMallMapper busiMallMapper;
    @Autowired
    private BusiMallService busiMallService;
    @Autowired
    private IAppointmentFacadeService appointmentFacadeService;
    @Autowired
    private BusiProductContractMapper busiProductContractMapper;
    @Autowired
    private BusiOrderSubMapper busiOrderSubMapper;
    @Autowired
    private BusiProductSubMapper busiProductSubMapper;
    @Autowired
    private BusiFinancePlanService busiFinancePlanService;
    @Autowired
    private InnerEmployeeService innerEmployeeService;



    private void checkBespeakProduct(CustomerMainInfo customerMainInfo, BusiProductSub busiProduct, BigDecimal orderAmt) {
        //是否预约产品
        if (busiProduct.getLimitType() != null && busiProduct.getLimitType() == AppConstants.ProductLimitType.BESPEAK) {
            //是否在正常预约券使用时间内
            if (new Date().getTime() <= busiProduct.getReservatTime().getTime()) {
                Integer availNum = null;
                Long days;
                if (AppConstants.OrderProductType.FINANCE_PLAN.toString().equals(busiProduct.getSubjectType())){
                    days = busiProduct.getCloseDay().longValue();
                }else{
                     days = (busiProduct.getInterestEndDate().getTime() - busiProduct.getInterestStartDate().getTime()) / (24 * 60 * 60 * 1000);
                }
                try {
                    ResultDto<AppointmentNumDto> resultDto = appointmentFacadeService.getAppointmentNum(customerMainInfo.getCmNumber(), orderAmt, days.intValue());
                    if (resultDto != null) {
                        if (resultDto.isSuccess()) {
                            AppointmentNumDto appointmentNumDto = resultDto.getData();
                            availNum = appointmentNumDto.getAvailNum();
                        } else {
                            throw new BusinessException("bespeak.record");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("预约券检查接口获取可用预约券数量失败" + e.getMessage());
                    throw new BusinessException("bespeak.record");
                }
                if (availNum == 0) {
                    throw new BusinessException("没有可用预约券,请前往个人中心-捞财币商城兑换预约券或预约时间结束后再购买!");
                }
            }
        }
    }

    //封装订单
    private BusiOrderSub setOrder(OrderVo orderVo,CustomerMainInfo customerMainInfo, BusiProductContract busiProductContract, BigDecimal orderAmt ){
        BusiProductSub busiProduct = orderVo.getBusiProductSub();
        BusiOrderSub busiOrder = new BusiOrderSub();
        Date  now = new Date();
        busiOrder.setProductName(busiProduct.getProductName());
        //普通订单
        if(OrderType.COMMON ==orderVo.getOrderType()){
            busiOrder.setCustomerId(customerMainInfo.getId());
            busiOrder.setCustomerName(customerMainInfo.getCmRealName());
            busiOrder.setStatus(AppConstants.BusiOrderStatus.BUSIORDER_STATUS_1);
            busiOrder.setOrderAmt(orderAmt);
            busiOrder.setOrderTime(new Date());
            busiOrder.setHolderType(orderVo.getHoldType());
            String orderId = SerialNumberGenerator.generatorOrderNum(OrderGenerateType.MAIN_ORDER,customerMainInfo.getId());
            busiOrder.setOrderId(orderId);
            busiOrder.setModifyDate(new Date());
            busiOrder.setProductId(busiProduct.getId());
            busiOrder.setYearRate(busiProduct.getYearRate());
            busiOrder.setOriginalRate(busiProduct.getYearRate());
            busiOrder.setInterestStartDate(busiProduct.getInterestStartDate()==null?null:busiProduct.getInterestStartDate());
            busiOrder.setInterestEndDate(busiProduct.getInterestEndDate()==null?null:busiProduct.getInterestEndDate());
            ReqMain reqMain = orderVo.getReqMain();
            if(reqMain != null){
                busiOrder.setCmOpenMechanism(reqMain.getReqHeadParam().getMechanism());
                busiOrder.setCmOpenChannel(reqMain.getReqHeadParam().getOpenchannel());
                busiOrder.setCmOpenPlatform(AppConstants.getAppType(reqMain.getReqHeadParam().getUserAgent()));
                busiOrder.setCmTogatherType(reqMain.getReqHeadParam().getTogatherType());
                busiOrder.setCmRegisterVersion(reqMain.getReqHeadParam().getVersion());
            }
            busiOrder.setOriginalRate(busiProduct.getYearRateInit());
            busiOrder.setIsFirstOrder((long) 0);
            busiOrder.setSubjectStatus(AppConstants.SubjectStatus.NOTIFY_FAILURE);
            busiOrder.setProductType(Integer.valueOf(busiProduct.getSubjectType()));
            if (AppConstants.ProductTransferStatus.COMMON_PRODUCT.equals(busiProduct.getIsTransfer())) {
                busiOrder.setTransferType(AppConstants.OrderTransferStatus.ORDER_NORMAL);
            } else {
                busiOrder.setTransferType(AppConstants.OrderTransferStatus.ORDER_TRANSFER);
            }
            busiOrder.setPayType(busiProduct.getRepayType());
            if (busiProductContract != null) {
                busiOrder.setDebtorNum(busiProductContract.getCmNumber());
                busiOrder.setDebtorName(busiProductContract.getBorrowerName());
            }
            busiOrder.setCouponAmount(BigDecimal.ZERO);
            busiOrder.setIntegralAmount(BigDecimal.ZERO);
            busiOrder.setRaiseRateIncome(BigDecimal.ZERO);
            busiOrder.setCashAmount(BigDecimal.ZERO);
            String addRate = "0";
            busiOrder.setInviteCode(customerMainInfo.getCmIntroduceCode());
            //计算订单本利 (公式：本息=订单金额+订单金额/项目本金*(项目利息+项目利息*加息利率))   2.5版本重新计算本息
            BigDecimal principalInterest = busiOrderService.computePrincipleAndInterest(busiProduct, orderAmt, new BigDecimal(addRate));
            busiOrder.setPrincipalinterest(principalInterest);
            //修改订单年利率中文显示字符串，由于下单接口不存在使用加息券，帮直接 更新成现在的年利率
            String yearRete = NumberUtil.fortmatBigDecimalForOne(busiOrder.getYearRate().multiply(new BigDecimal(100))) + "%";
            busiOrder.setYearRateStr(yearRete);
            //更新每日收益
            BigDecimal dayInterest = busiOrderService.computeDailyInterest(busiProduct, orderAmt, new BigDecimal(addRate));
            busiOrder.setDayProfit(dayInterest);
            //加息天数为0，加息每日收益为日收益
            busiOrder.setRaiseDays(0L); //默认加息天数为0
            busiOrder.setRaiseDayProfit(busiOrder.getDayProfit());
            //产品活动加息
            busiOrder.setActionRate(busiProduct.getAddInterest());
            if(OrderType.COMMON.equals(orderVo.getOrderType())){
                int closeDays = 0;
                if(AppConstants.ProductSubjectType.FINANCE_PLAN.equals(busiProduct.getSubjectType())){
                    closeDays =  busiProduct.getCloseDay();
                }
                else {
                    closeDays = DateUtil.getIntervalDays2(busiOrder.getInterestEndDate(), busiOrder.getInterestStartDate()) + 1;
                }
                busiOrder.setCloseDays((long) closeDays);
            }
            //v4.9.1
            busiOrder.setUserLabel(customerMainInfo.getUserLabel());
            busiOrder.setUserLevel(customerMainInfo.getUserLevel());
            busiOrder.setMemberLevel(customerMainInfo.getMemberLevel().toString());

        }
        //理财计划子订单
        else if (OrderType.SUBORDER ==orderVo.getOrderType()){
            if("0".equals(orderVo.getHoldType())){
                CopyUtil.copyProperties(busiOrder,orderVo.getPlanOrder());
                busiOrder.setParentId(orderVo.getPlanOrder().getId());
                busiOrder.setParentNo(orderVo.getPlanOrder().getOrderId());
            }
            MatchSucResult matchSucResult  = orderVo.getMatchSucResult();
            busiOrder.setCustomerId(customerMainInfo.getId());
            busiOrder.setCustomerName(customerMainInfo.getCmRealName());
            busiOrder.setStatus(AppConstants.BusiOrderStatus.BUSIORDER_STATUS_1);
            busiOrder.setOrderAmt(orderAmt);
            busiOrder.setOrderTime(new Date());
            busiOrder.setHolderType(orderVo.getHoldType());
            String orderId = SerialNumberGenerator.generatorOrderNum(OrderGenerateType.SUB_ORDER,customerMainInfo.getId());
            busiOrder.setOrderId(orderId);
            BigDecimal interest = matchSucResult.getCapitalAmount().multiply(new BigDecimal(busiProduct.getCloseDay())).multiply(busiProduct.getYearRate()).divide(new BigDecimal(365), 2, 1);
            busiOrder.setOrderAmt(matchSucResult.getCapitalAmount());
            busiOrder.setSubOrderStatus(AppConstants.ORDER_SUB_STATUS.ORDER_STATUS_1);
            busiOrder.setCmNumber(customerMainInfo.getCmNumber());

            busiOrder.setPrincipalinterest(matchSucResult.getCapitalAmount().add(interest));
            busiOrder.setSubjectNo(matchSucResult.getSubjectNo());
            busiOrder.setDebtNo(matchSucResult.getManFinanceId());
            busiOrder.setDebtType(matchSucResult.getDebtType());
            busiOrder.setCashAmount(matchSucResult.getCapitalAmount());
            busiOrder.setOrderTime(new Date());
            busiOrder.setProductType(AppConstants.OrderProductType.FINANCE_PLAN_SUB);
            busiOrder.setOrderId(orderId);
            busiOrder.setDebtorNum(matchSucResult.getLoanCustomerNo());
            busiOrder.setDebtorName(matchSucResult.getLoanCustomerName());
            busiOrder.setRaiseRateIncome(BigDecimal.ZERO);
            if(AppConstants.FinancePlan.DEBT_TYPE1.equals(matchSucResult.getDebtType())){
                busiOrder.setProductId(busiProduct.getId());
            }else{
                busiOrder.setProductId(0L);
            }
            busiOrder.setSubOrderStatus(AppConstants.ORDER_SUB_STATUS.ORDER_STATUS_1);
            busiOrder.setCloseDays(Long.valueOf(busiProduct.getCloseDay()));
            busiOrder.setYearRate(busiProduct.getYearRate());
            busiOrder.setInterestStartDate(busiProduct.getInterestStartDate());
            busiOrder.setInterestEndDate(busiProduct.getInterestEndDate());

            busiOrder.setUserLabel(customerMainInfo.getUserLabel());
            busiOrder.setUserLevel(customerMainInfo.getUserLevel());
            busiOrder.setMemberLevel(customerMainInfo.getMemberLevel().toString());
        }
        //理财计划转让订单
        else {
            MatchSucResult matchSucResult  = orderVo.getMatchSucResult();
            busiOrder = orderVo.getOriginOrder();
            busiOrder.setCustomerId(customerMainInfo.getId());
            busiOrder.setCustomerName(customerMainInfo.getCmRealName());
            busiOrder.setStatus(AppConstants.BusiOrderStatus.BUSIORDER_STATUS_1);
            busiOrder.setOrderAmt(orderAmt);
            busiOrder.setOrderTime(new Date());
            busiOrder.setHolderType(orderVo.getHoldType());
            String orderId = SerialNumberGenerator.generatorOrderNum(OrderGenerateType.SUB_ORDER,customerMainInfo.getId());
            BigDecimal interest = orderAmt.multiply(new BigDecimal(busiOrder.getCloseDays())).multiply(busiOrder.getYearRate()).divide(new BigDecimal(365), 2, 1);
            busiOrder.setPrincipalinterest(orderAmt.add(interest));
            busiOrder.setOrderId(orderId);
            busiOrder.setInterestStartDate(now);
            busiOrder.setProductId(0L);
            if (matchSucResult != null) {
                busiOrder.setDebtNo(matchSucResult.getFinanceId());
            }
            busiOrder.setTransferType(AppConstants.OrderTransferStatus.ORDER_TRANSFER);

            busiOrder.setUserLabel(customerMainInfo.getUserLabel());
            busiOrder.setUserLevel(customerMainInfo.getUserLevel());
            busiOrder.setMemberLevel(customerMainInfo.getMemberLevel().toString());
        }
        return busiOrder;
    }



    public void checkLimitProduct(CustomerMainInfo mainInfo, Long limitType) {
        Long lockLimitType = mainInfo.getLimitType();
        if (lockLimitType != null) {
            BusiProductLimit lockBusiProductLimit = busiProductLimitMapper.selectByPrimaryKey(lockLimitType);
            if (lockBusiProductLimit != null) {//购买过限购产品
                Date nowDate = new Date();
                Date limitDate = mainInfo.getLimitLastDate();
                if (limitDate != null && nowDate.before(limitDate)) {//没过限购期
                    Long limitTypeId = limitType;//产品的限购类型
                    if (limitTypeId != null) {
                        if (lockLimitType.equals(limitTypeId)) {//再次购买同一个限购产品
                            throw new BusinessException("您已购买过此限购产品，限购期内不能再次购买！");
                        } else {
                            BusiProductLimit busiProductLimit = busiProductLimitMapper.selectByPrimaryKey(limitTypeId);
                            if (busiProductLimit != null) {
                                if (AppConstants.ProductLimitType.LIMIT.equals(busiProductLimit.getType())
                                        && AppConstants.ProductLimitType.LIMIT.equals(lockBusiProductLimit.getType())
                                        ) {//购买同类产品
                                    throw new BusinessException("您已购买过此类限购产品，限购期内不能再次购买！");
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public Result checkBespeakTicket(String loginType, Long customerId, Long productId, BigDecimal orderAmt) {
        CustomerMainInfo mainInfo = customerMainInfoService.checkCustomerId(customerId);
        BusiProductRuleVo productRuleVo = busiProductMapper.selectProductWithRule(productId);
        if (productRuleVo == null || productRuleVo.getLimitType() == null) {
            throw new BusinessException(AppConstants.LoginBusiOrder.BUSIORDER_ERROR_8);
        }
        BusiBeforeOrderVo beforeOrderVo = new BusiBeforeOrderVo();
        //校验新手专享产品
        if (productRuleVo.getLimitType() == AppConstants.ProductLimitType.NEW_HAND && mainInfo.getIsConsumed() == 1L) {
            throw new BusinessException(AppConstants.LoginBusiOrder.BUSIORDER_ERROR_19);
        }
        //校验微信专享产品
        if (productRuleVo.getLimitType() == AppConstants.ProductLimitType.WECHAT) {
            if (AppConstants.BuyWechatStatus.BUY.equals(mainInfo.getBuyWechat())) {
                beforeOrderVo.setErrorCode(1);
                beforeOrderVo.setErrorMsg(AppConstants.LoginBusiOrder.BUSIORDER_ERROR_23);
                return Result.fail(AppConstants.LoginBusiOrder.BUSIORDER_ERROR_23, beforeOrderVo);
            } else if (StringUtils.isEmpty(mainInfo.getOpenId())) {
                beforeOrderVo.setErrorCode(2);
                beforeOrderVo.setErrorMsg(AppConstants.LoginBusiOrder.BUSIORDER_ERROR_24);
                return Result.fail(AppConstants.LoginBusiOrder.BUSIORDER_ERROR_24, beforeOrderVo);
            }
        }
        //校验限购
        checkLimitProduct(mainInfo, productRuleVo.getLimitType());
        //校验平台
        if (StringUtils.isNotBlank(productRuleVo.getPlatform())) {
            String platformType;
            String platformName;
            if (BusiConstants.LOGIN_TYPE_WEB.equals(loginType)) {
                platformType = "1";
                platformName = "PC";
            } else if (BusiConstants.LOGIN_TYPE_APP.equals(loginType)) {
                platformType = "2";
                platformName = "APP";
            } else if (BusiConstants.LOGIN_TYPE_WECHAT.equals(loginType)) {
                platformType = "3";
                platformName = "微信";
            } else {
                platformType = "4";
                platformName = "WAP";
            }
            String[] platforms = productRuleVo.getPlatform().split(",");
            boolean flag = true;
            for (String platform : platforms) {
                if (platformType.equals(platform)) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                //为false,则说明需要在另一平台购买
                throw new BusinessException("该产品不可在" + platformName + "上购买");
            }
        }
        //检验会员
        if (StringUtils.isNotBlank(productRuleVo.getMemberType())) {
            String[] memberTypes = productRuleVo.getMemberType().split(",");
            boolean flag = true;
            for (String memberType : memberTypes) {
                if (mainInfo.getMemberType() == Integer.valueOf(memberType)) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                //为false,则说明需要在另一平台购买
                throw new BusinessException("该产品可购买的会员类型与您不符,请选择购买其他产品");
            }
        }
        //检验渠道
        if (StringUtils.isNotBlank(productRuleVo.getChannel())) {
            String[] channels = productRuleVo.getChannel().split(",");
            boolean flag = true;
            for (String channel : channels) {
                if (channel.equals(mainInfo.getChannelCode())) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                //为false,则说明需要在另一平台购买
                throw new BusinessException("该产品可购买的渠道类型与您不符,请选择购买其他产品");
            }
        }
        //是否预约产品
        if (AppConstants.ProductLimitType.BESPEAK == productRuleVo.getLimitType()) {
            //是否在正常预约券使用时间内
            if (new Date().getTime() < productRuleVo.getReservatTime().getTime()) {
                Integer availNum = null;
                Long days = 0L;
                if (AppConstants.OrderProductType.FINANCE_PLAN.toString().equals(productRuleVo.getSubjectType())){
                    days = productRuleVo.getCloseDay().longValue();
                }else{
                    if (productRuleVo.getInterestEndDate() != null && productRuleVo.getInterestStartDate() != null){
                        days = (productRuleVo.getInterestEndDate().getTime() - productRuleVo.getInterestStartDate().getTime()) / (24 * 60 * 60 * 1000);
                    }
                }
                try {
                    ResultDto<AppointmentNumDto> resultDto = appointmentFacadeService.getAppointmentNum(mainInfo.getCmNumber(), orderAmt, days.intValue());
                    if (resultDto != null) {
                        if (resultDto.isSuccess()) {
                            AppointmentNumDto appointmentNumDto = resultDto.getData();
                            availNum = appointmentNumDto.getAvailNum();
                        } else {
                            throw new BusinessException("bespeak.record");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("预约券检查接口获取可用预约券数量失败" + e.getMessage());
                    throw new BusinessException("bespeak.record");
                }
                if (availNum > 0) {
                    beforeOrderVo.setIsHasBespeakTicket(true);
                    beforeOrderVo.setIsUseBespeakTicket(true);
                } else {
                    beforeOrderVo.setIsHasBespeakTicket(false);
                    beforeOrderVo.setIsUseBespeakTicket(true);
                    try {
                        beforeOrderVo.setCoinBalance(busiMallService.getCoinBalance(mainInfo.getCmNumber()));
                        beforeOrderVo.setLastCoinBalance(busiMallService.getCoinBalanceLast(mainInfo.getCmNumber()));
                    } catch (Exception e) {
                        e.printStackTrace();
                        log.error("预约券检查接口获取捞财币失败" + e.getMessage());
                        throw new BusinessException("coin.record");
                    }
                    Map<String, Object> paramsMap = new HashMap<>();
                    paramsMap.put("merchandiseType", AppConstants.PRODUCT_TYPE.TYPE_3);
                    paramsMap.put("investPeriod", days);
                    paramsMap.put("investAmt", orderAmt);
                    List<BusiMall> mallList = busiMallMapper.getProductListByType(paramsMap);
                    if (mallList != null && mallList.size() > 0) {
                        BusiMall mall = mallList.get(0);
                        beforeOrderVo.setTicketId(mall.getId());
                        beforeOrderVo.setBuyAmt(mall.getBuyAmt().longValue());
                    }
                    beforeOrderVo.setBespeakTimeOver(timeForrmat(productRuleVo.getReservatTime()));
                }
            } else {
                beforeOrderVo.setIsHasBespeakTicket(false);
                beforeOrderVo.setIsUseBespeakTicket(false);
            }
        } else {
            beforeOrderVo.setIsHasBespeakTicket(false);
            beforeOrderVo.setIsUseBespeakTicket(false);
        }
        return Result.success(beforeOrderVo);
    }

    /**
     * 检验订单信息
     *
     * @param loginType
     * @param productId
     * @param orderAmt
     */
    public void checkOrder(String loginType, CustomerMainInfo mainInfo, Long productId, BigDecimal orderAmt,BusiProductSub busiProduct) {
        BusiProductRuleVo productRuleVo = busiProductMapper.selectProductWithRule(productId);
        if (productRuleVo == null || productRuleVo.getLimitType() == null) {
            throw new BusinessException(AppConstants.LoginBusiOrder.BUSIORDER_ERROR_8);
        }
        //校验新手专享产品
        if (productRuleVo.getLimitType() == AppConstants.ProductLimitType.NEW_HAND && mainInfo.getIsConsumed() == 1L) {
            throw new BusinessException(AppConstants.LoginBusiOrder.BUSIORDER_ERROR_19);
        }
        //校验微信专享产品
        if (productRuleVo.getLimitType() == AppConstants.ProductLimitType.WECHAT) {
            if (AppConstants.BuyWechatStatus.BUY.equals(mainInfo.getBuyWechat())) {
                throw new BusinessException(AppConstants.LoginBusiOrder.BUSIORDER_ERROR_23);
            } else if (StringUtils.isEmpty(mainInfo.getOpenId())) {
                throw new BusinessException(AppConstants.LoginBusiOrder.BUSIORDER_ERROR_24);
            }
        }
        //校验限购
        checkLimitProduct(mainInfo, productRuleVo.getLimitType());
        //校验平台
        if (StringUtils.isNotBlank(productRuleVo.getPlatform())) {
            String platformType;
            String platformName;
            if (BusiConstants.LOGIN_TYPE_WEB.equals(loginType)) {
                platformType = "1";
                platformName = "PC";
            } else if (BusiConstants.LOGIN_TYPE_APP.equals(loginType)) {
                platformType = "2";
                platformName = "APP";
            } else if (BusiConstants.LOGIN_TYPE_WECHAT.equals(loginType)) {
                platformType = "3";
                platformName = "微信";
            } else {
                platformType = "4";
                platformName = "WAP";
            }
            String[] platforms = productRuleVo.getPlatform().split(",");
            boolean flag = true;
            for (String platform : platforms) {
                if (platformType.equals(platform)) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                //为false,则说明需要在另一平台购买
                throw new BusinessException("该产品不可在" + platformName + "上购买");
            }
        }
        //检验会员
        if (StringUtils.isNotBlank(productRuleVo.getMemberType())) {
            String[] memberTypes = productRuleVo.getMemberType().split(",");
            boolean flag = true;
            for (String memberType : memberTypes) {
                if (mainInfo.getMemberType() == Integer.valueOf(memberType)) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                //为false,则说明需要在另一平台购买
                throw new BusinessException("该产品可购买的会员类型与您不符,请选择购买其他产品");
            }
        }
        //检验渠道
        if (StringUtils.isNotBlank(productRuleVo.getChannel())) {
            String[] channels = productRuleVo.getChannel().split(",");
            boolean flag = true;
            for (String channel : channels) {
                if (channel.equals(mainInfo.getChannelCode())) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                //为false,则说明需要在另一平台购买
                throw new BusinessException("该产品可购买的渠道类型与您不符,请选择购买其他产品");
            }
        }
        //是否预约产品
        if (AppConstants.ProductLimitType.BESPEAK == productRuleVo.getLimitType()) {
            //是否在正常预约券使用时间内
            if (new Date().getTime() < productRuleVo.getReservatTime().getTime()) {
                Integer availNum = null;
                Long days = 0L;
                if (productRuleVo.getInterestEndDate() != null && productRuleVo.getInterestStartDate() != null) {
                    days = (productRuleVo.getInterestEndDate().getTime() - productRuleVo.getInterestStartDate().getTime()) / (24 * 60 * 60 * 1000);
                }
                try {
                    ResultDto<AppointmentNumDto> resultDto = appointmentFacadeService.getAppointmentNum(mainInfo.getCmNumber(), orderAmt, days.intValue());
                    if (resultDto != null) {
                        if (resultDto.isSuccess()) {
                            AppointmentNumDto appointmentNumDto = resultDto.getData();
                            availNum = appointmentNumDto.getAvailNum();
                        } else {
                            throw new BusinessException("bespeak.record");
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("预约券检查接口获取可用预约券数量失败" + e.getMessage());
                    throw new BusinessException("bespeak.record");
                }
            }
        }

        //未测试用户先提示测试
        if (mainInfo.getRiskTestType()==null){
            throw new BusinessException("未进行风险测试、不能购买");
        }
        String purchaseCondition = busiProduct.getPurchaseCondition();
        if (purchaseCondition!=null){
            String[] conditions = purchaseCondition.split(";");
            if (!Arrays.asList(conditions).contains(mainInfo.getRiskTestType())){
                throw new BusinessException("风险测试等级和产品购买条件不符合、不能购买");
            }
        }
        //风险测试已过期
        if (mainInfo.getRiskExpireTime()!=null && DateUtil.getIntervalDays2(new Date(),mainInfo.getRiskExpireTime())>0){
            throw new BusinessException("风险测试已过期、重新测评后才能购买");
        }
        //检验是否签约法大大
        if (mainInfo.getSignContract()==null || mainInfo.getSignContract().intValue()==0){
            throw new BusinessException("该用户没有签约法大大、签约后才能购买");
        }
    }

    public Integer checkWechatLimitProduct(CustomerMainInfo mainInfo, BusiProductSub product) {
        Long limitType = product.getLimitType();
        if (limitType != null) {
            BusiProductLimit productLimit = busiProductLimitMapper.selectByPrimaryKey(limitType);
            if (productLimit.getType().equals(AppConstants.ProductLimitType.WECHAT)) {
                //购买的是微信产品
                if (AppConstants.BuyWechatStatus.BUY.equals(mainInfo.getBuyWechat())) {
                    return 1;
                } else if (StringUtils.isEmpty(mainInfo.getOpenId())) {
                    return 2;
                }
            }
        }
        return null;
    }

    public void checkChannel(CustomerMainInfo mainInfo, BusiProductSub product) {
        if (StringUtils.isNotEmpty(product.getProductChannel())) {
            if (!product.getProductChannel().equals(mainInfo.getChannelCode())) {
                throw new BusinessException("您不能购买此产品！");
            }
        }
    }

    public void checkCanBuyTransferProduct(CustomerMainInfo mainInfo, BusiProductSub product) {
        BusiDebtTransfer transfer = busiDebtTransferService.getByProductIdAndCmId(mainInfo.getId(), product.getId());
        if (transfer != null) {
            throw new BusinessException("您不能购买自己转让过的产品");
        }
    }

    public String timeForrmat(Date bespeakDate) {
        long time = bespeakDate.getTime() - new Date().getTime();
        long minutes = time / 1000 / 60;
        long seconds = time % (1000 * 60) / 1000;
        StringBuffer timeString = new StringBuffer();
        if (minutes > 59) {
            long hour = minutes / 60;
            minutes = minutes % 60;
            timeString.append(hour == 0 ? "" : hour + "小时");
        }
        timeString.append(minutes == 0 ? "" : minutes + "分");
        timeString.append(seconds == 0 ? "" : seconds + "秒");
        System.out.println(timeString.toString());
        return timeString.toString();
    }


    /**
     * 理财计划版本 重构下单接口
     *
     * @param customerId
     * @param productId
     * @param orderAmt
     * @param reqMain
     * @return
     * @throws Exception
     */
    public Result order(Long customerId, Long productId, BigDecimal orderAmt,String inviteCode, ReqMain reqMain) throws Exception {
        OrderVo orderVo = new OrderVo();
        orderVo.setCustomerId(customerId);
        orderVo.setProductId(productId);
        orderVo.setOrderAmt(orderAmt);
        orderVo.setReqMain(reqMain);
        BusiOrderSub busiOrder = this.order(orderVo);
        if (busiOrder.getId() != null) {
            Map<String, Object> valiCodeMap = Maps.newTreeMap();
            valiCodeMap.put("orderId", busiOrder.getId());
            valiCodeMap.put("cbAccount", "");
            valiCodeMap.put("errorCode", "");
            valiCodeMap.put("errorMsg", "");
            return Result.success("下单成功", valiCodeMap);
        } else {
            return Result.fail("下单失败!");
        }
    }


    /**
     * 订单基础校验
     */
    public BusiProductSub checkBasic(Long productId, BigDecimal orderAmt ,String holdType,Long customerId) {
        if (orderAmt.compareTo(new BigDecimal(0)) <= 0) {
            throw new BusinessException("订单金额不合法");
        }
        BusiProductSub busiProduct = busiProductSubMapper.selectByPrimaryKey(productId);
        if (busiProduct == null) {
            throw new BusinessException(AppConstants.LoginBusiOrder.BUSIORDER_ERROR_1);
        }
        if(!OrderConstants.OrderHoldType.HOLD_SPECIAL.equals(holdType)){
            //产品已售罄
            BigDecimal totalAmt = busiProduct.getTotalInvestAmt() != null ? busiProduct.getTotalInvestAmt() : new BigDecimal(0);
            if (totalAmt.add(orderAmt).compareTo(busiProduct.getProductPrincipal()) > 0) {//售罄
                throw new BusinessException(AppConstants.LoginBusiOrder.BUSIORDER_ERROR_13);
            }
            // 订单金额不能大于产品的起投上限金额
            if (orderAmt.compareTo(busiProduct.getInvestUpper()) > 0) {
                throw new BusinessException("购买金额不能大于产品的上限金额！");
            }
            // 该产品剩余金额校验
            BigDecimal totalInvestAmt = new BigDecimal(0);
            if (busiProduct.getTotalInvestAmt() != null) totalInvestAmt = busiProduct.getTotalInvestAmt();
            BigDecimal residueAmt = busiProduct.getProductPrincipal().subtract(totalInvestAmt);
            if (orderAmt.compareTo(busiProduct.getInvestLower()) < 0) {
                if (residueAmt.compareTo(busiProduct.getInvestLower()) >= 0) {
                    throw new BusinessException("购买金额不能小于起投金额！");
                } else {
                    if (orderAmt.compareTo(residueAmt) != 0) {
                        throw new BusinessException("购买金额不符合条件！");
                    }
                }
            }
            //产品已下架
            if (!AppConstants.ProductUpLowStatus.PRODUCT_UP.equals(busiProduct.getUpLowFlag())) {//下架
                throw new BusinessException(AppConstants.LoginBusiOrder.BUSIORDER_ERROR_14);
            }
            //产品未起售
            Date date = new Date();
            if (date.before(busiProduct.getSaleStartDate())) {
                throw new BusinessException("产品暂未起售");
            }
            //判断授权额度是否充足
            Map userAuth= busiOrderService.userAuthJudge(customerId,productId,orderAmt);
            String grantFlag= userAuth.get("grantFlag").toString();
            if ("1".equals(grantFlag)){
                throw new BusinessException("授权额度不足、下单失败");
            }
        }
        return busiProduct;
    }

    /**
     * 重构下单接口
     *
     * @return
     * @throws Exception
     */
    public BusiOrderSub order(OrderVo vo) throws Exception {
        Long customerId = vo.getCustomerId();
        Long productId = vo.getProductId();
        BigDecimal orderAmt = vo.getOrderAmt();
        ReqMain reqMain = vo.getReqMain();
        OrderType orderType = vo.getOrderType();
        CustomerMainInfo customerMainInfo = customerMainInfoService.findOpenCustomerById(customerId);

        if(StringUtils.isNotEmpty(customerMainInfo.getAccountType()) && !CustomerAccountType.LENDER.getValue().equals(customerMainInfo.getAccountType())){
            throw new BusinessException(AppConstants.LoginBusiOrder.BUSIORDER_ERROR_BUYING_NOT_PERMITTED);
        }
        if(OrderType.COMMON.equals(vo.getOrderType())){
            String cmNumber = busiFinancePlanService.gainDefaultSpecialLender();
            if(customerMainInfo.getCmNumber().equals(cmNumber)){
                vo.setHoldType(OrderConstants.OrderHoldType.HOLD_SPECIAL);
            }
        }
        BusiProductSub busiProduct = null;
        BusiProductContract contract = null;
        //理财计划子订单转让 不校验
        if(OrderType.COMMON.equals(orderType)){
            //校验订单信息
            busiProduct = checkBasic(productId, orderAmt , vo.getHoldType(),customerId);

            String loginType = reqMain ==null ? BusiConstants.LOGIN_TYPE_MANAGE : LaocaiUtil.getLoginType(reqMain.getReqHeadParam());
            //Manage发起和理财计划子产品，不校验以下信息
            if (!(BusiConstants.LOGIN_TYPE_MANAGE.equalsIgnoreCase(loginType)
                    || AppConstants.ProductSubjectType.FINANCE_PLAN_SUB.equals(busiProduct.getSubjectType()))) {
                if (isInsufficient(customerMainInfo, orderAmt)) {
                    throw new BusinessException("剩余可出借金额不足");
                }
                //专区产品校验
                Boolean productLimitType = innerEmployeeService.isProductLimitType(busiProduct.getLimitType()+"");
                if (productLimitType) {
                    Result staffAuth = innerEmployeeService.staffAuth(customerId,reqMain.getReqHeadParam().getIp());
                    if (!(staffAuth.getSuccess()) ||  StrUtil.equals(staffAuth.getData().toString(),"0")) {
                        throw new BusinessException(staffAuth.getMessage());
                    }
                    // 订单金额必须是10的倍数
                    if (orderAmt.intValue() % 10 !=0){
                        throw new BusinessException("订单金额必须是10的倍数!");
                    }
                }

                checkOrder(LaocaiUtil.getLoginType(reqMain.getReqHeadParam()), customerMainInfo, productId, orderAmt,busiProduct);
                //检查预约券
                checkBespeakProduct(customerMainInfo, busiProduct, orderAmt);
                //结售
                Date date = new Date();
                if (date.after(busiProduct.getSaleEndDate())) {
                    throw new BusinessException(AppConstants.LoginBusiOrder.BUSIORDER_ERROR_15);
                }
                //不在产品显示时间段内
                if (busiProduct.getShowStartDate() != null) {
                    if (date.before(busiProduct.getShowStartDate())) {
                        throw new BusinessException(AppConstants.LoginBusiOrder.BUSIORDER_ERROR_14);
                    }
                }
                if (busiProduct.getShowEndDate() != null) {
                    if (date.after(busiProduct.getShowEndDate())) {
                        throw new BusinessException(AppConstants.LoginBusiOrder.BUSIORDER_ERROR_14);
                    }
                }
            }
            //自己转让的产品不能购买
            if (AppConstants.ProductTransferStatus.TRANSFER_PRODUCT.equals(busiProduct.getIsTransfer())) {
                checkCanBuyTransferProduct(customerMainInfo, busiProduct);
                if (orderAmt.compareTo(busiProduct.getProductPrincipal()) != 0) {
                    throw new BusinessException("订单金额与转让产品金额不一致");
                }
            }
            if (!AppConstants.ProductSubjectType.FINANCE_PLAN.equals(busiProduct.getSubjectType())) {
                contract = busiProductContractMapper.selectByPrimaryKey(busiProduct.getContractId());
                if (contract == null) {
                    throw new BusinessException("标的信息不存在");
                }
            }
            //不能购买自己发布的个贷产品
            if (AppConstants.ProductSubjectType.SUBJECT_GD.equals(busiProduct.getPersonLoan())
                    ||AppConstants.ProductSubjectType.FINANCE_PLAN_SUB.equals(busiProduct.getPersonLoan())) {
                if (null != contract) {
                    if (customerMainInfo.getCmNumber().equals(contract.getCmNumber())) {
                        throw new BusinessException("不能购买自己发布的个贷产品");
                    }
                }
            }
            //判断购买产品和用户关系是否一致
            if (StringUtils.isNotBlank(customerMainInfo.getUserLabel()) &&  StringUtils.isNotBlank(busiProduct.getTargetConsumer())){
                // 获取默认特殊理财人编号
                String cmNumber = busiFinancePlanService.gainDefaultSpecialLender();
                if (!"2".equals(busiProduct.getTargetConsumer()) &&
                        !customerMainInfo.getUserLabel().equals(busiProduct.getTargetConsumer()) &&
                        !cmNumber.equals(customerMainInfo.getCmNumber())){
                    throw new BusinessException("当前购买的产品和用户类型不一致（下单）");
                }
            }

            //判断购买产品对应的会员等级和用户会员等级是否一致
            if (customerMainInfo.getMemberLevel() != null && busiProduct.getMemberLevel() != null) {
                if (customerMainInfo.getMemberLevel().intValue() < busiProduct.getMemberLevel().intValue()) {
                    throw new BusinessException("当前购买产品所需会员等级,与用户会员等级不符合（下单）");
                }
            }

            vo.setBusiProductSub(busiProduct);
        }
        //生成子订单信息
        BusiOrderSub busiOrderSub = setOrder(vo,customerMainInfo, contract, orderAmt);
        if (OrderType.COMMON == vo.getOrderType() ) {
            //生成主订单
            BusiOrderTemp busiOrder = PropertiesUtils.copy(BusiOrderTemp.class, busiOrderSub);
            busiOrderTempService.insert(busiOrder);
            busiOrderSub.setId(busiOrder.getId());
            busiOrderSubMapper.insertWithId(busiOrderSub);
        } else {
            busiOrderSubMapper.insert(busiOrderSub);
        }
        return busiOrderSub;
    }

    /**
     * 判断下单或支付剩余出借额度
     * @param mainInfo
     * @param orderAmt
     * @return
     */
    public Boolean isInsufficient(CustomerMainInfo mainInfo, BigDecimal orderAmt){
        boolean flag = false;
        Map<String, Object> paramsMap = new HashMap<>();
        paramsMap.put("prType", "investing_limit");
        paramsMap.put("prName", mainInfo.getRiskTestType());
        SysParameter sysParameter = sysParameterService.getSysParameterPrs(paramsMap);
        if (sysParameter == null) {
            throw new BusinessException("请先测评风险等级");
        }
        BigDecimal amt = Convert.toBigDecimal(sysParameter.getPrValue()).multiply(new BigDecimal(10000));
        // 在投总金额
        BigDecimal totalAmt = busiOrderService.statisticsOrderAmt(mainInfo.getId()).add(orderAmt);
        BigDecimal surplusAmt = amt.subtract(totalAmt);
        if (surplusAmt.compareTo(BigDecimal.ZERO) < 0) {
            flag = true;
        }
        return flag;
    }
}
