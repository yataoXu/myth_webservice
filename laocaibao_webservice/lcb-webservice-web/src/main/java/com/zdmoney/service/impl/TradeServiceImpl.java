package com.zdmoney.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zdmoney.assets.api.common.dto.AssetsResultDto;
import com.zdmoney.assets.api.dto.agreement.AgreementNameDto;
import com.zdmoney.assets.api.dto.signature.ContinueInvestReqDto;
import com.zdmoney.assets.api.dto.subject.ChildSubjectPlanDto;
import com.zdmoney.assets.api.dto.subject.SubjectInfoObtainDto;
import com.zdmoney.assets.api.dto.transfer.TransferFailReqDto;
import com.zdmoney.assets.api.dto.transfer.TransferFailResDto;
import com.zdmoney.assets.api.dto.transfer.TransferSuccessReqDto;
import com.zdmoney.assets.api.dto.transfer.TransferSuccessResDto;
import com.zdmoney.assets.api.dto.subject.SubjectInvestCollectResultReqDto;
import com.zdmoney.assets.api.dto.subject.SubjectReAuditResultDto;
import com.zdmoney.assets.api.facade.signature.ISignatureFacadeService;
import com.zdmoney.assets.api.facade.subject.ILCBSubjectFacadeService;
import com.zdmoney.assets.api.facade.subject.ISubjectFacadeService;
import com.zdmoney.common.*;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.constant.BusiConstants;
import com.zdmoney.constant.OrderConstants;
import com.zdmoney.facade.InnerEmployeeService;
import com.zdmoney.integral.api.dto.lcbaccount.query.QueryUserInfoDto;
import com.zdmoney.integral.api.dto.lcbaccount.query.QueryUserInfoResultDto;
import com.zdmoney.mapper.trade.BusiCollectFlowMapper;
import com.zdmoney.mapper.trade.BusiCollectInfoMapper;
import com.zdmoney.mapper.zdpay.CustomerGrantInfoMapper;
import com.zdmoney.models.OperStateRecord;
import com.zdmoney.models.trade.BusiCollectInfo;
import com.zdmoney.models.transfer.DebtTransferDetail;
import com.zdmoney.models.zdpay.UserGrantBO;
import com.zdmoney.service.OrderReinvestConstants;
import com.zdmoney.enums.BusiTypeEnum;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.helper.SystemParameterHelper;
import com.zdmoney.integral.api.common.dto.ResultDto;
import com.zdmoney.integral.api.dto.appointment.AppointmentDto;
import com.zdmoney.integral.api.dto.appointment.AppointmentOprResDto;
import com.zdmoney.integral.api.dto.coupon.CouponDto;
import com.zdmoney.integral.api.dto.lcbaccount.*;
import com.zdmoney.integral.api.dto.lcbaccount.bid.*;
import com.zdmoney.integral.api.dto.lcbaccount.enm.*;
import com.zdmoney.integral.api.dto.order.IntegralExchangeDto;
import com.zdmoney.integral.api.dto.order.IntegralExchangeResDto;
import com.zdmoney.integral.api.dto.order.IntegralOrderRefundDto;
import com.zdmoney.integral.api.dto.product.IntegralProductDto;
import com.zdmoney.integral.api.dto.voucher.VoucherDto;
import com.zdmoney.integral.api.dto.voucher.VoucherOprResDto;
import com.zdmoney.integral.api.dto.voucher.VoucherSearchDto;
import com.zdmoney.integral.api.facade.*;
import com.zdmoney.life.api.utils.JsonUtils;
import com.zdmoney.mapper.SysOperateTaskMapper;
import com.zdmoney.mapper.bank.CustomerBankAccountMapper;
import com.zdmoney.mapper.customer.CustomerMainInfoMapper;
import com.zdmoney.mapper.financePlan.BusiOrderSubMapper;
import com.zdmoney.mapper.financePlan.FundDetailMapper;
import com.zdmoney.mapper.order.BusiOrderReinvestLogMapper;
import com.zdmoney.mapper.order.BusiOrderTempMapper;
import com.zdmoney.mapper.product.*;
import com.zdmoney.mapper.trade.BusiTradeFlowMapper;
import com.zdmoney.mapper.transfer.BusiDebtTransferMapper;
import com.zdmoney.marketing.entity.OrderMessage;
import com.zdmoney.marketing.utils.BusinessKeyGenerator;
import com.zdmoney.message.api.common.dto.MessageResultDto;
import com.zdmoney.message.api.dto.sm.SendSmNotifyReqDto;
import com.zdmoney.message.api.facade.ISmFacadeService;
import com.zdmoney.models.SysOperateTask;
import com.zdmoney.models.bank.CustomerBankAccount;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.financePlan.BusiFundDetail;
import com.zdmoney.models.financePlan.BusiOrderSub;
import com.zdmoney.models.order.BusiOrder;
import com.zdmoney.models.order.BusiOrderIntegral;
import com.zdmoney.models.order.BusiOrderReinvestLog;
import com.zdmoney.models.order.BusiOrderTemp;
import com.zdmoney.models.payment.PaymentPlan;
import com.zdmoney.models.product.*;
import com.zdmoney.models.sys.SysParameter;
import com.zdmoney.models.trade.BusiOperationDataStatistics;
import com.zdmoney.models.trade.BusiTradeFlow;
import com.zdmoney.models.transfer.BusiDebtTransfer;
import com.zdmoney.component.mq.Producer;
import com.zdmoney.mq.client.group.MqGroup;
import com.zdmoney.service.*;
import com.zdmoney.service.order.OrderService;
import com.zdmoney.service.payment.PaymentPlanService;
import com.zdmoney.service.subject.SubjectService;
import com.zdmoney.service.sys.SysSwitchService;
import com.zdmoney.service.transfer.BusiDebtTransferService;
import com.zdmoney.service.welfare.WelfareService;
import com.zdmoney.session.RedisSessionManager;
import com.zdmoney.utils.*;
import com.zdmoney.vo.BusiProductRuleVo;
import com.zdmoney.vo.OrderVo;
import com.zdmoney.vo.trade.TenderVo;
import com.zdmoney.vo.trade.TransferVo;
import com.zdmoney.web.TransferResultDTO;
import com.zdmoney.web.dto.BannerDTO;
import com.zdmoney.web.dto.Pay20DTO;
import com.zdmoney.web.dto.PayPasswordDTO;
import com.zdmoney.web.dto.WithdrawDTO;
import com.zdmoney.webservice.api.dto.plan.CollectOrderDetailDTO;
import com.zdmoney.webservice.api.dto.plan.CollectOrderInfoDTO;
import com.zdmoney.webservice.api.dto.plan.HRbidTenderCollectDto;
import com.zdmoney.webservice.api.dto.plan.LoanReviewDTO;
import com.zendaimoney.laocaibao.wx.api.dto.WeiChantDto;
import com.zendaimoney.laocaibao.wx.api.facade.IWechatFacadeService;
import com.zendaimoney.laocaibao.wx.common.Constants;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;
import websvc.models.*;
import websvc.req.ReqMain;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by 00225181 on 2015/12/1.
 * 2.0 交易相关接口
 */
@Service
@Slf4j
public class TradeServiceImpl implements TradeService {

    @Autowired
    IVoucherFacadeService voucherFacadeService;
    @Autowired
    OrderService orderService;
    @Autowired
    BusiProductLimitMapper busiProductLimitMapper;
    @Autowired
    CustomerMainInfoService customerMainInfoService;
    @Autowired
    private CustomerMainInfoMapper customerMainInfoMapper;
    @Autowired
    private BusiOrderTempMapper busiOrderTempMapper;
    @Autowired
    private BusiOrderService busiOrderService;
    @Autowired
    private IIntegralOrderFacadeService integralOrderFacadeService;
    @Autowired
    private ICouponFacadeService couponFacadeService;
    @Autowired
    private BusiOrderIntegralService busiOrderIntegralService;
    @Autowired
    private BusiProductMapper busiProductMapper;
    @Autowired
    private BusiTradeFlowMapper busiTradeFlowMapper;
    @Autowired
    private IAccountFacadeService accountFacadeService;
    @Autowired
    private AccountOverview520003Service accountOverview520003Service;
    @Autowired
    private BusiAddupCustomerOrderService busiAddupCustomerOrderService;
    @Autowired
    private Producer producer;
    @Autowired
    private BusiBannerService busiBannerService;

    @Autowired
    private SysSwitchService sysSwitchService;

    @Autowired
    private IWechatFacadeService wechatFacadeService;

    @Autowired
    private SysParameterService sysParameterService;

    @Autowired
    private BusiProductContractMapper busiProductContractMapper;

    @Autowired
    private SubjectService subjectService;
    @Autowired
    private ILCBSubjectFacadeService lcbSubjectFacadeService;

    @Autowired
    private BusiDebtTransferService busiDebtTransferService;

    @Autowired
    private BusiProductService busiProductService;

    @Autowired
    private IAppointmentFacadeService appointmentFacadeService;


    @Autowired
    private IBidFacadeService bidFacadeService;

    @Autowired
    private PayDepositService payDepositService;

    @Autowired
    private ReportDailyFirstInvestService reportDailyFirstInvestService;

    @Autowired
    RedisSessionManager redisSessionManager;

    @Autowired
    private SysOperateTaskMapper sysOperateTaskMapper;

    @Value(value = "${order.limit.time}")
    private String orderLimitTime;

    @Value(value = "${order.limit.desc}")
    private String orderLimitDesc;

    @Autowired
    private FundDetailMapper fundDetailMapper;

    @Autowired
    private PaymentPlanService paymentPlanService;


    @Autowired
    private BusiDebtTransferMapper debtTransferMapper;

    @Autowired
    private BusiProductSubMapper busiProductSubMapper;

    @Autowired
    private BusiOrderSubMapper busiOrderSubMapper;

    @Autowired
    private CustomerBankAccountMapper bankAccountMapper;

    @Autowired
    private ISmFacadeService iSmFacadeService;

    @Autowired
    private ConfigParamBean configParamBean;

    @Autowired
    BusiProductRuleMapper busiProductRuleMapper;

    @Autowired
    private BusiOrderReinvestLogMapper busiOrderReinvestLogMapper;

    @Autowired
    private OrderReinvestConstants orderReinvestConstants;

    @Autowired
    private BusiCollectFlowMapper busiCollectFlowMapper;

    @Autowired
    private BusiCollectInfoMapper busiCollectInfoMapper;

    @Autowired
    private ISubjectFacadeService iSubjectFacadeService;

    @Autowired
    private CustomerGrantInfoMapper customerGrantInfoMapper;

    @Autowired
    private IDepositFacadeService depositFacadeService;

    @Autowired
    private IOperStateRecordService operStateRecordService;

    @Autowired
    private ISignatureFacadeService signatureFacadeService;

    @Autowired
    private WelfareService welfareService;

    @Autowired
    private InnerEmployeeService innerEmployeeService;


    /*
     *2.0支付
     */
    @Override
    public Result pay(Long customerId, Long orderId, String integralAmt, String redId, String voucherId, String payPassword, boolean isNeedPayPwd, ReqMain reqMain) throws Exception {
        Pay20DTO pay20DTO = new Pay20DTO();
        String loginType = BusiConstants.LOGIN_TYPE_MANAGE;
        if (reqMain != null) {
            loginType = LaocaiUtil.getLoginType(reqMain.getReqHeadParam());
        }
        //校验-用户信息
        CustomerMainInfo customerMainInfo = customerMainInfoService.findOpenCustomerById(customerId);
        BusiOrderSub order = busiOrderSubMapper.selectByPrimaryKey(orderId);
        if (order == null) {
            throw new BusinessException(AppConstants.LoginBusiOrder.BUSIORDER_ERROR_10);
        }
        if (!order.getCustomerId().equals(customerId)) {
            throw new BusinessException("订单信息异常");
        }
        //manage兜底不校验支付密码
        if (BusiConstants.LOGIN_TYPE_MANAGE.equalsIgnoreCase(loginType)
                || OrderConstants.OrderHoldType.HOLD_SPECIAL.equals(order.getHolderType())) {
            isNeedPayPwd = false;
        } else {
            if (orderService.isInsufficient(customerMainInfo, order.getOrderAmt())) {
                throw new BusinessException("剩余可出借金额不足");
            }
        }

        //校验-订单信息
        String orderKey = "ORD_LCK_" + customerId + "_" + order.getProductId();
        String orderInfo = redisSessionManager.get(orderKey);
        if (StringUtils.isNotBlank(orderInfo)) {
            throw new BusinessException(orderLimitDesc);
        }

        if (!AppConstants.BusiOrderStatus.BUSIORDER_STATUS_1.equals(order.getStatus())) {
            throw new BusinessException(AppConstants.LoginBusiOrder.BUSIORDER_ERROR_16);
        }
        //判断授权额度是否充足
        Map userAuth = busiOrderService.userAuthJudge(customerId, order.getProductId(), order.getOrderAmt());
        String grantFlag = userAuth.get("grantFlag").toString();
        if ("1".equals(grantFlag)) {
            throw new BusinessException("授权额度不足、支付失败");
        }

        //校验-转让订单
        BusiDebtTransfer transfer = null;
        if (AppConstants.OrderTransferStatus.ORDER_TRANSFER.equals(order.getTransferType())) {
            transfer = busiDebtTransferService.getTransferByProductId(order.getProductId());
            //转让产品订单，只能支付转让初始状态的订单
            if (transfer != null) {
                if (!AppConstants.DebtTransferStatus.TRANSFER_INIT.equals(transfer.getTransferStatus())) {
                    throw new BusinessException(AppConstants.LoginBusiOrder.BUSIORDER_ERROR_13);
                }
                if (transfer.getTransferPrice().compareTo(order.getOrderAmt()) != 0) {
                    throw new BusinessException(AppConstants.LoginBusiOrder.BUSIORDER_ERROR_21);
                }
            }
        }

        BusiProductSub busiProductVO = busiProductSubMapper.selectByPrimaryKey(order.getProductId());

        pay20DTO = checkOrder(order, busiProductVO, loginType);
        //规则校验
        BusiProductRuleVo productRuleVo = checkRule(loginType, customerMainInfo, busiProductVO.getId());
        //检查福利是否可用
        checkCanUseWelfare(busiProductVO, productRuleVo, integralAmt, redId, voucherId, order);
        //验证-预约券
        String appointmentId = gainAppointmnetId(loginType, busiProductVO, customerMainInfo, order.getOrderAmt());
        log.info("*************获取用户预约券appointmentId： " + appointmentId);
        if (!AppConstants.PayStatusContants.FAIL.equals(pay20DTO.getPayResult())) {//订单有效
            Boolean isAmountEnough = checkAccountAmount(customerMainInfo, order, integralAmt, redId, busiProductVO, reqMain,loginType);
            if (isAmountEnough) { //用户账户余额充足
                boolean lock = false;
                String lockKey = "ORD_LCK_" + customerId + "_" + order.getProductId() + "_KEY";
                String lockValue = "ORD_LCK_" + customerId + "_" + order.getProductId() + "_LOCK";
                try {
                    // 获取锁
                    lock = redisSessionManager.setNX(lockKey, lockValue);
                    log.info(Thread.currentThread().getName() + "当前支付请求线程是否获取到锁:" + lock);
                    if (lock) {
                        // 1分钟后,key值失效,自动释放锁
                        redisSessionManager.expire(lockKey, 1, TimeUnit.MINUTES);
                        //账户系统消费
                        consumeNew(customerMainInfo, order, busiProductVO, integralAmt, redId, voucherId, reqMain, transfer, appointmentId);
                        //设置用户限购，如果购买的产品是限购产品
                        lockLimitCustomer(customerMainInfo, busiProductVO, productRuleVo);
                        //更新用户本地授权额度
                        updateUserGrantInfo(customerId, order.getProductId(), order.getOrderAmt());
                        pay20DTO.setPayResult(AppConstants.PayStatusContants.SUCCESS);
                        pay20DTO.setPayResultDesc(AppConstants.PayStatusContants.getPayStatusDesc(AppConstants.PayStatusContants.BUY_SUCCESS));
                        SimpleDateFormat format = new SimpleDateFormat("MM月dd日");

                        if (order.getProductType().intValue() == AppConstants.OrderProductType.FINANCE_PLAN.intValue()) {//理财计划
                            pay20DTO.setInterestDate("购买后3个工作日内开始起息");
                        } else {
                            pay20DTO.setInterestDate(format.format(order.getInterestStartDate()) + "开始计息");
                        }
                        pushWxOrSms(customerMainInfo, order, busiProductVO);

                        if (StringUtils.isNotBlank(orderLimitTime)) {
                            //单个用户下单时间限制
                            Long orderLimitTimes = getOrderLimitTime(orderLimitTime);
                            redisSessionManager.put(orderKey, order.getOrderId(), orderLimitTimes, TimeUnit.SECONDS);
                        }
                    } else {
                        log.info("用户支付请求没有获取到锁，不能进行支付。");
                        throw new BusinessException(orderLimitDesc);
                    }
                } finally {
                    if (lock) {// 如果获取了锁，则释放锁
                        redisSessionManager.remove(lockKey);
                        log.info(Thread.currentThread().getName() + "用户支付请求结束，释放锁!");
                    }
                }
            } else {
                //用户账户余额不足
                log.info("账户余额或积分不足，订单号：" + orderId + "，客户号：" + customerId);
                pay20DTO.setPayResult(AppConstants.PayStatusContants.FAIL);
                pay20DTO.setPayResultDesc(AppConstants.PayStatusContants.getPayStatusDesc(AppConstants.PayStatusContants.NOT_ENOUGH_AMOUNT));
            }
        }

        List<BannerDTO> dtos = getbugSuccessBanners();
        if (dtos.size() > 0) {
            pay20DTO.setBuySuccessBanner(dtos.get(0));
        }
        pay20DTO.setOrderId(order.getOrderId());
        pay20DTO.setOrderTime(DateUtil.timeFormat(order.getOrderTime(), "yyyyMMddHHmm"));
        return Result.success(pay20DTO);
    }
    private Long getOrderLimitTime(String orderLimitTime) {
        List<String> orderTimeResult = Arrays.asList(StringUtils.split(orderLimitTime, ","));
        Long minTime = Long.parseLong(orderTimeResult.get(0));
        Long maxTime = Long.parseLong(orderTimeResult.get(1));
        Long result = minTime + (int) (Math.random() * ((maxTime - minTime) + 1));
        return result;
    }
    /*
     *2.0提现
     */
    @Override
    @Transactional(noRollbackFor = BusinessException.class)
    public Result withDraw(ReqMain reqMain) throws Exception {
        WithdrawDTO withdrawdto = new WithdrawDTO();
        Model_520009 model_520009 = (Model_520009) reqMain.getReqParam();
        String customerId = model_520009.getCustomerId();
        log.info("提现开始，客户号：" + customerId + "，提现金额：" + model_520009.getWithDrawAmount());
        if (StringUtils.isEmpty(customerId)) {
            throw new BusinessException("用户编号不能为空！");
        }
        Long custId = Long.parseLong(customerId);
        CustomerMainInfo mainInfo = customerMainInfoService.findOpenCustomerById(custId);
        boolean isCheckPasswordPass = checkPayPassword(withdrawdto, mainInfo, model_520009.getPayPassword());

        if (!isCheckPasswordPass) {
            log.info("提现失败，校验密码失败，客户号：" + customerId + "，提现金额：" + model_520009.getWithDrawAmount() + "！");
            return new Result(false, "提现失败", withdrawdto);
        }

        if (StringUtils.isEmpty(model_520009.getWithDrawAmount())) {
            throw new BusinessException("提现金额不能为空！");
        }

        if (new BigDecimal(model_520009.getWithDrawAmount()).compareTo(new BigDecimal(0)) == -1) {
            throw new BusinessException("提现金额不合法");
        }

        SysParameter withdrawlimit = sysParameterService.findOneByPrType("withdrawlimit");
        if (withdrawlimit != null) {
            String value = withdrawlimit.getPrValue();
            if (new BigDecimal(value).compareTo(new BigDecimal(model_520009.getWithDrawAmount())) == -1) {
                log.info("提现，客户号：" + customerId + "，提现金额：" + model_520009.getWithDrawAmount() + ">每日提现限额" + value);
                throw new BusinessException("提现金额超过每日提现限额：" + value);
            }
        } else {
            log.error("提现，客户号：" + customerId + "，提现金额：" + model_520009.getWithDrawAmount() + "，系统没有配置提现限额！");
            throw new BusinessException("系统没有配置提现限额，提现失败，如有疑问请联系管理员！");
        }
        SysParameter withdrawtimes = sysParameterService.findOneByPrType("withdrawtimes");
        if (withdrawtimes != null) {
            String value = withdrawtimes.getPrValue();
            int cnt = busiTradeFlowMapper.selectWithdrawTimesByCustomerId(custId);
            if (Integer.parseInt(value) <= cnt) {
                log.info("提现，客户号：" + customerId + "，提现金额：" + model_520009.getWithDrawAmount() + "，今日提现次数" + cnt + "，超过系统设置的每日提现次数");
                withdrawdto.setIsLock(true);
                withdrawdto.setMsg("今日已提现" + cnt + "次，不能再次提现，请明日再试！");
                return new Result(false, "今日已提现" + cnt + "次，不能再次提现，请明日再试！", withdrawdto);
            }
        } else {
            log.error("提现，客户号：" + customerId + "，提现金额：" + model_520009.getWithDrawAmount() + "，系统没有配置单日提现次数！");
            throw new BusinessException("系统没有配置单日提现次数，提现失败，如有疑问请联系管理员！");
        }

        if (StringUtils.isEmpty(mainInfo.getBankAccountId())) {
            throw new BusinessException("没有绑定的银行卡，不能发起提现申请,请先绑定银行卡！");
        }
        CustomerBankAccount bankAccount = bankAccountMapper.selectByPrimaryKey(Long.parseLong(mainInfo.getBankAccountId()));
        if (bankAccount == null) {
            throw new BusinessException("用户没有绑定的银行信息！");
        }
        if (StringUtils.isEmpty(bankAccount.getCbSubBankCode())) {
            if (StringUtils.isEmpty(model_520009.getSubBankCode()) || StringUtils.isEmpty(model_520009.getSubBankName())) {
                log.info("提现，客户号：" + customerId + "，提现金额：" + model_520009.getWithDrawAmount() + "，支行为空！");
                throw new BusinessException("支行不能为空！");
            } else {
                //更新用户支行
                bankAccount.setCbSubBankCode(model_520009.getSubBankCode());
                bankAccount.setCbBranchName(model_520009.getSubBankName());
                int num = bankAccountMapper.updateByPrimaryKey(bankAccount);
                if (num != 1) {
                    log.error("提现，客户号：" + customerId + "，提现金额：" + model_520009.getWithDrawAmount() + "，更新支行失败！");
                    throw new BusinessException("更新支行失败！");
                }
            }
        }
        // 查询可提现金额
        BigDecimal accountBalance = accountOverview520003Service.extractAllBalance(mainInfo.getCmNumber());
        if (accountBalance.compareTo(new BigDecimal(model_520009.getWithDrawAmount())) == -1) {
            log.info("提现，客户号：" + customerId + "，提现金额：" + model_520009.getWithDrawAmount() + "，余额不足！");
            throw new BusinessException("账户余额不足，不能提现！");
        }
        String flowId = busiOrderService.buildCeNumber("W", "8888", custId);
        AccountOprDto dto = new AccountOprDto();
        dto.setAccountNo(mainInfo.getCmNumber());
        dto.setAmount(new BigDecimal(model_520009.getWithDrawAmount()));
        dto.setOrderNo(flowId);
        dto.setTransNo("WTX" + flowId);
        ResultDto<AccountOprResultDto> resultDto = accountFacadeService.withdraw(dto);
        if (resultDto.isSuccess()) {
            log.info("提现，客户号：" + customerId + "，提现金额：" + model_520009.getWithDrawAmount() + "，账户端扣款成功！");
        } else {
            log.error("提现，客户号：" + customerId + "，提现金额：" + model_520009.getWithDrawAmount() + "，账户端扣款失败，" + resultDto.getMsg());
            throw new BusinessException("提现失败，" + resultDto.getMsg());
        }
        //发起提现申请流水
        BusiTradeFlow tradeFlow = new BusiTradeFlow();
        tradeFlow.setFlowNum(flowId);
        tradeFlow.setTrdDate(new Date());
        tradeFlow.setTrdType(AppConstants.TradeStatusContants.WITHDRAWING);
        tradeFlow.setTrdAmt(new BigDecimal(model_520009.getWithDrawAmount()));
        tradeFlow.setCustomerId(custId);
        tradeFlow.setStatus(AppConstants.TradeStatusContants.INIT);
        tradeFlow.setAccountSeriNo(resultDto.getData().getRecordNum());
        int num = busiTradeFlowMapper.insert(tradeFlow);
        if (num == 1) {
            log.info("提现成功，客户号：" + customerId + "，提现金额：" + model_520009.getWithDrawAmount() + "，保存提现流水成功！");
            return Result.success("提现申请发送成功！", withdrawdto);
        } else {
            log.error("提现失败，客户号：" + customerId + "，提现金额：" + model_520009.getWithDrawAmount() + "，保存提现流水失败！");
            AccountOprDto accountOprDto = new AccountOprDto();
            accountOprDto.setAccountNo(mainInfo.getCmNumber());
            accountOprDto.setAmount(new BigDecimal(model_520009.getWithDrawAmount()));
            accountOprDto.setOrderNo(flowId);
            accountOprDto.setTransNo("WTXTK" + flowId);
            ResultDto<AccountOprResultDto> accountOprResultDto = accountFacadeService.refund(accountOprDto);
            if (accountOprResultDto.isSuccess()) {
                tradeFlow.setAccountSeriNo(accountOprResultDto.getData().getRecordNum());
                num = busiTradeFlowMapper.updateByPrimaryKeySelective(tradeFlow);
                if (num == 1) {
                    log.info("账户退款成功,flowNum=" + flowId);
                } else {
                    log.error("账户退款失败,flowNum=" + flowId);
                }
                throw new BusinessException("提现申请发送失败！");
            } else {
                throw new BusinessException("提现申请发送失败！");
            }
        }
    }

    private boolean checkPayPassword(WithdrawDTO dto, CustomerMainInfo mainInfo, String payPassword) {
        if (StringUtils.isEmpty(mainInfo.getPayPassword())) {
            throw new BusinessException("您没有设置交易密码,请先设置交易密码！");
        }
        boolean isCheckPass = false;
        String errorMsg = "";
        String errorTimes = sysParameterService.findOneByPrType("resetErrorTimes").getPrValue();
        String lockHours = sysParameterService.findOneByPrType("resetLockHour").getPrValue();
        boolean isLock = customerMainInfoService.isLocked(mainInfo.getPayLockTime(), Integer.parseInt(lockHours));
        if (isLock) {//用户密码已锁定
            errorMsg = PropertiesUtil.getDescrptionProperties().get("withdraw.pay_password.lock");
            errorMsg = MessageFormat.format(errorMsg, errorTimes, lockHours);
            dto.setIsLock(true);
            dto.setErrorTime(mainInfo.getPayErrorTime());
            dto.setMsg(errorMsg);
        } else {//未锁定
            String password = customerMainInfoService.genLoginPassword(payPassword.toUpperCase(), mainInfo.getCmSalt());
            if (mainInfo.getPayPassword().toUpperCase().equals(password)) {//密码校验通过
                isCheckPass = true;
                mainInfo.setPayLockTime(null);
                mainInfo.setPayErrorTime(0);
                mainInfo.setPayErrorLastTime(null);
            } else {//密码校验不通过
                Integer errorTime = mainInfo.getPayErrorTime();
                DateTime payErrorLastTime = new DateTime(mainInfo.getPayErrorLastTime() == null ? DateTime.now().toDate() : mainInfo.getPayErrorLastTime());
                Long payErrorLastTimeStr = Long.parseLong(payErrorLastTime.toString("yyyyMMdd"));
                Long now = Long.parseLong(DateTime.now().toString("yyyyMMdd"));
                if (now > payErrorLastTimeStr) {//清除隔天的密码错误记数
                    errorTime = 0;
                }
                errorTime = errorTime + 1;
                if (errorTime >= Short.parseShort(errorTimes)) {
                    DateTime date = DateTime.now();
                    mainInfo.setPayLockTime(date.toDate());//设置锁定账户时间
                    mainInfo.setPayErrorTime(0);
                    errorMsg = PropertiesUtil.getDescrptionProperties().get("withdraw.pay_password.lock");
                    errorMsg = MessageFormat.format(errorMsg, errorTimes, lockHours);
                    isLock = true;//账户锁定
                } else {
                    isLock = false;
                    errorMsg = PropertiesUtil.getDescrptionProperties().get("withdraw.pay_password.error");
                    errorMsg = MessageFormat.format(errorMsg, errorTime, errorTimes, lockHours);
                    mainInfo.setPayErrorTime(errorTime);
                }
                mainInfo.setPayErrorLastTime(DateTime.now().toDate());
                dto.setErrorTime(errorTime);
                dto.setIsLock(isLock);
                dto.setMsg(errorMsg);
            }
            customerMainInfoService.update(mainInfo);
        }
        return isCheckPass;
    }

    /*
     * 校验订单信息
     */
    public Pay20DTO checkOrder(BusiOrderSub order, BusiProductSub busiProductVO, String loginType) {
        log.info("校验订单状态开始，订单号：" + order.getId());
        String errorMsg = "";
        //未找到产品
        if (busiProductVO == null) {
            errorMsg = AppConstants.PayStatusContants.getPayStatusDesc(AppConstants.PayStatusContants.OFF_SHELF);
            return payResult(false, errorMsg);
        }

        if (!BusiConstants.LOGIN_TYPE_MANAGE.equalsIgnoreCase(loginType)) {
            //产品已过期
            if (busiProductVO.getSaleEndDate().getTime() < new Date().getTime()) {
                log.info("校验订单状态，产品过期，订单号：" + order.getId());
                errorMsg = AppConstants.PayStatusContants.getPayStatusDesc(AppConstants.PayStatusContants.OVERDUE);
                return payResult(false, errorMsg);
            }
            //判断是否第一次购买新手标产品
            CustomerMainInfo customerMainInfo = customerMainInfoMapper.selectByCustomerId(order.getCustomerId());
            if (customerMainInfo != null && "1".equals(busiProductVO.getIsNewHand()) && customerMainInfo.getIsConsumed() == 1) {
                log.info("校验订单状态，非新手购买新手标，订单号：" + order.getId());
                errorMsg = AppConstants.PayStatusContants.getPayStatusDesc(AppConstants.PayStatusContants.NOT_NEW_HAND);
                return payResult(false, errorMsg);
            }

            boolean isOverSold = sysSwitchService.getSwitchIsOn("isOverSold");
            if (!isOverSold) {//不可以超卖
                BigDecimal orderAmt = order.getOrderAmt();
                BigDecimal canBuyAmt = busiProductVO.getProductPrincipal().subtract(busiProductVO.getTotalInvestAmt()).subtract(busiProductVO.getVmTotalInvestAmt());
                //已售罄
                if (canBuyAmt.doubleValue() <= 0) {
                    log.info("校验订单状态，产品已售罄，订单号：" + order.getId());
                    errorMsg = AppConstants.PayStatusContants.getPayStatusDesc(AppConstants.PayStatusContants.SELL_OUT);
                    return payResult(false, errorMsg);
                } else if (canBuyAmt.subtract(orderAmt).doubleValue() < 0) {
                    //产品余额不足
                    log.info("校验订单状态，产品余额不足，订单号：" + order.getId());
                    errorMsg = AppConstants.PayStatusContants.getPayStatusDesc(AppConstants.PayStatusContants.BALANCE_UN_ENOUGH);
                    return payResult(false, errorMsg);
                }
            }

            Map<String, String> param = Maps.newTreeMap();
            param.put("orderNo", order.getId().toString());
            BusiOrderIntegral busiOrderIntegral = busiOrderIntegralService.selectByCondition(param);
            if (busiOrderIntegral != null) {
                errorMsg = AppConstants.PayStatusContants.getPayStatusDesc(AppConstants.PayStatusContants.EXSIST_ORDER_PAY_FLOW);
                return payResult(false, errorMsg);
            }
            try {
                orderService.checkLimitProduct(customerMainInfo, busiProductVO.getLimitType());
            } catch (Exception e) {
                if (e instanceof BusinessException) {
                    log.info("用户限购期购买限购订单：" + order.getId());
                    errorMsg = e.getMessage();
                    return payResult(false, errorMsg);
                }
            }
            //未测试用户先提示测试
            if (customerMainInfo.getRiskTestType() == null) {
                throw new BusinessException("未进行风险测试、不能购买");
            }
            String purchaseCondition = busiProductVO.getPurchaseCondition();
            if (purchaseCondition != null) {
                String[] conditions = purchaseCondition.split(";");
                if (!Arrays.asList(conditions).contains(customerMainInfo.getRiskTestType())) {
                    throw new BusinessException("风险测试等级和产品购买条件不符合、不能购买");
                }
            }
            //风险测试已过期
            if (customerMainInfo.getRiskExpireTime() != null && DateUtil.getIntervalDays2(new Date(), customerMainInfo.getRiskExpireTime()) > 0) {
                throw new BusinessException("风险测试已过期、重新测评后才能购买");
            }
            //检验是否签约法大大
            if (customerMainInfo.getSignContract() == null || customerMainInfo.getSignContract().intValue() == 0) {
                throw new BusinessException("该用户没有签约法大大、签约后才能购买");
            }
            //判断购买产品和用户关系是否一致
            if (StringUtils.isNotBlank(customerMainInfo.getUserLabel()) && org.apache.commons.lang3.StringUtils.isNotBlank(busiProductVO.getTargetConsumer())) {
                if (!"2".equals(busiProductVO.getTargetConsumer()) && !customerMainInfo.getUserLabel().equals(busiProductVO.getTargetConsumer())) {
                    throw new BusinessException("当前购买的产品和用户类型不一致(支付)");
                }
            }

            //判断购买产品对应的会员等级和用户会员等级是否一致
            if (customerMainInfo.getMemberLevel() != null && busiProductVO.getMemberLevel() != null) {
                if (customerMainInfo.getMemberLevel().intValue() < busiProductVO.getMemberLevel().intValue()) {
                    throw new BusinessException("当前购买产品所需会员等级,与用户会员等级不符合（支付）");
                }
            }
        }
        log.info("更新订单状态结束，订单号：" + order.getId());
        return payResult(true, errorMsg);
    }

    private Pay20DTO payResult(boolean isSuccess, String errorMsg) {
        Pay20DTO pay20DTO = new Pay20DTO();
        pay20DTO.setPayResultDesc(errorMsg);
        pay20DTO.setPayResult(isSuccess ? AppConstants.PayStatusContants.SUCCESS : AppConstants.PayStatusContants.FAIL);

        return pay20DTO;
    }

    @Transactional
    public boolean isFirst(CustomerMainInfo customerMainInfo) {
        Boolean isFirst = false;
        if (customerMainInfo.getIsConsumed() == 0) {
            customerMainInfo.setIsConsumed(1L);
            customerMainInfo.setFirstConsumeDate(new Date());
            customerMainInfo.setCmModifyDate(new Date());
            int num = customerMainInfoMapper.updateByPrimaryKey(customerMainInfo);
            if (num != 1) {
                log.error("更新用户新手状态失败！");
            }
            isFirst = true;
        }
        return isFirst;
    }

    @Async("payThreadExecutor")
    public void sendMsg(BusiOrderSub order, CustomerMainInfo customerMainInfo, Boolean isFirst, BusiProductSub product) {
        try {
            String key = BusinessKeyGenerator.getKey(BusiTypeEnum.ORDER.name());
            Integer lotteryCode = 0;
            SysParameter reLotteryCode = sysParameterService.findOneByPrType("reLotteryCode");
            if (reLotteryCode == null) {
                lotteryCode = 0;
            } else {
                lotteryCode = Integer.parseInt(reLotteryCode.getPrValue());
            }
            OrderMessage orderMessage = new OrderMessage();
            orderMessage.setCmNumber(customerMainInfo.getCmNumber());
            orderMessage.setCustomerId(customerMainInfo.getId());
            orderMessage.setFirstOrder(isFirst);
            orderMessage.setOrderAmt(order.getOrderAmt());
            orderMessage.setOrderNum(order.getOrderId());
            orderMessage.setPlatform(order.getCmOpenPlatform());
            orderMessage.setLotteryCode(lotteryCode);
            orderMessage.setInvestPeriod(getInvestPeriod(order, product));
            orderMessage.setOrderTime(order.getOrderTime());
            orderMessage.setIsTransfer(AppConstants.OrderTransferStatus.ORDER_TRANSFER.equals(order.getTransferType()));
            if (AppConstants.OrderProductType.COMMON.equals(order.getProductType())) {
                orderMessage.setSubject(false);
            } else {
                orderMessage.setSubject(true);
            }
            DateTime start = new DateTime(customerMainInfo.getCmInputDate());
            DateTime end = new DateTime(order.getOrderTime());
            int hours = Hours.hoursBetween(start, end).getHours();
            if (hours < 24) {
                orderMessage.setWithin24Order(true);
            } else {
                orderMessage.setWithin24Order(false);
            }
            String introduceCode = customerMainInfo.getCmIntroduceCode();
            if (!StringUtils.isEmpty(introduceCode)) {
                Map<String, Object> map = Maps.newTreeMap();
                map.put("cmInviteCode", introduceCode);
                List<CustomerMainInfo> mainInfos = customerMainInfoMapper.getCustomerBySearchParam(map);
                if (!mainInfos.isEmpty()) {
                    CustomerMainInfo introduceCustomer = mainInfos.get(0);
                    orderMessage.setInviteCmNumber(introduceCustomer.getCmNumber());
                }
            }
            log.info("order send mq:key=" + key + ",msg=" + JackJsonUtil.objToStr(orderMessage));
            producer.sendMsg(MqGroup.LCB_GROUP.name(), key, JackJsonUtil.objToStr(orderMessage));
        } catch (Exception e) {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    /*
     * 积分消费
     */
    private void consumIntegral(CustomerMainInfo customerMainInfo,
                                BusiOrderTemp busiOrderTemp, String integralAmt, String redId, ReqMain reqMain) {
        log.info("消费积分开始,订单号:" + busiOrderTemp.getId());
        IntegralExchangeDto exchangeDto = new IntegralExchangeDto();
        exchangeDto.setAccountNo(customerMainInfo.getCmNumber());
        exchangeDto.setChannelOrderNo(busiOrderTemp.getOrderId());
        exchangeDto.setProductNo("00001");
        exchangeDto.setIntegral(Integer.parseInt(integralAmt));
        exchangeDto.setChannel(AppConstants.APPLICATION_CONTEXT_NAME);
        exchangeDto.setPlatform(AppConstants.getAppType(reqMain.getReqHeadParam().getUserAgent()));
        ResultDto<IntegralExchangeResDto> resultDto = integralOrderFacadeService.exchange(exchangeDto);
        if (resultDto.isSuccess()) {
            log.info("消费积分成功,订单号:" + busiOrderTemp.getId());
            try {
                log.info("消费积分结束,订单号:" + busiOrderTemp.getId());
                consumeFlow(resultDto.getData(), busiOrderTemp, integralAmt, redId, "");
            } catch (Exception e) {
                log.error("", e);
                refundIntegral(busiOrderTemp);
                throw new BusinessException(e);
            }
        } else {
            String info = "消费积分失败,订单号:" + busiOrderTemp.getId() + "," + resultDto.getMsg();
            log.info(info);
            throw new BusinessException(info);
        }
    }

    /*
     * 红包消费
     */
    private void consumeCoupon(CustomerMainInfo customerMainInfo, BusiOrderTemp busiOrderTemp, String integralAmt, String redId) {
        log.info("消费红包,订单号：" + busiOrderTemp.getId());
        CouponDto couponDto = new CouponDto();
        couponDto.setAccountNo(customerMainInfo.getCmNumber());
        couponDto.setNo(redId);
        couponDto.setOrderNo(busiOrderTemp.getOrderId());
        couponDto.setName(customerMainInfo.getCmRealName());
        ResultDto<String> resultDto = couponFacadeService.consumeCoupon(couponDto);
        if (resultDto != null) {
            if (resultDto.isSuccess()) {
                log.info("消费红包成功,订单号：" + busiOrderTemp.getId());
                consumeFlow(new IntegralExchangeResDto(), busiOrderTemp, integralAmt, redId, resultDto.getData());
            } else {
                log.info("消费红包失败,订单号：" + busiOrderTemp.getId());
                throw new BusinessException(BussErrorCode.ERROR_CODE_0000.getErrorcode(), resultDto.getMsg());
            }
        } else {
            throw new BusinessException(BussErrorCode.ERROR_CODE_0000.getErrorcode(), "红包系统--使用红包失败，没有正确返回");
        }
    }

    /**
     * 加息劵消费
     */
    @Transactional
    private void consumeVoucher(CustomerMainInfo customerMainInfo, BusiOrderSub busiOrderTemp, String voucherId) {
        log.info("消费加息券开始,订单号：{},加息券编号:{}", busiOrderTemp.getOrderId(), voucherId);
        //查询加息券
        VoucherDto voucherDto = null;
        VoucherSearchDto dto = new VoucherSearchDto();
        dto.setNo(voucherId);
        dto.setAccountNo(customerMainInfo.getCmNumber());
        dto.setCurDate(new Date());
        dto.setActualInvestAmount(busiOrderTemp.getOrderAmt());
//        dto.setActualInvestPeriod(DateUtil.getIntervalDays(busiOrderTemp.getInterestEndDate(), busiOrderTemp.getInterestStartDate()) + 1);
        dto.setActualInvestPeriod(busiOrderTemp.getCloseDays().intValue());
        ResultDto<VoucherDto> resultDto = voucherFacadeService.getCanConsumeVoucher(dto);
        if (resultDto != null) {
            if (resultDto.isSuccess()) {
                voucherDto = resultDto.getData();
            } else {
                throw new BusinessException(BussErrorCode.ERROR_CODE_0000.getErrorcode(), resultDto.getMsg());
            }
        } else {
            throw new BusinessException(BussErrorCode.ERROR_CODE_0000.getErrorcode(), "查询可用加息券异常");
        }
        String consumeSerialNo = "";
        //核销加息券
        VoucherDto consumeDto = new VoucherDto();
        consumeDto.setAccountNo(customerMainInfo.getCmNumber());
        consumeDto.setNo(voucherId);
        consumeDto.setOrderNo(busiOrderTemp.getOrderId());
        ResultDto<VoucherOprResDto> consumeResultDto = voucherFacadeService.consumeVoucher(consumeDto);

        if (consumeResultDto != null) {
            if (consumeResultDto.isSuccess()) {
                log.info("消费加息券结束,订单号：{},加息券编号:{}", busiOrderTemp.getOrderId(), voucherId);
                consumeSerialNo = consumeResultDto.getData().getSerialNo();
                try {
                    //写入订单消费流水表
                    log.info("保存消费流水，订单号:" + busiOrderTemp.getId() + ",加息券id:" + voucherId + "，加息券消费流水:" + resultDto.getData().getSerialNo());
                    Map<String, String> map = Maps.newTreeMap();
                    map.put("orderNo", busiOrderTemp.getId().toString());
                    BusiOrderIntegral orderIntegral = busiOrderIntegralService.selectByCondition(map);
                    if (orderIntegral == null) {
                        orderIntegral = new BusiOrderIntegral();
                        orderIntegral.setOrderNo(busiOrderTemp.getId());
                        orderIntegral.setVoucherId(voucherId);
                        orderIntegral.setVoucherSerialNo(consumeSerialNo);
                        int num = busiOrderIntegralService.insert(orderIntegral);
                        if (num < 1) {
                            log.info("保存消费流水失败，订单号:" + busiOrderTemp.getId() + ",加息券id:" + voucherId + "，加息券消费流水:" + resultDto.getData().getSerialNo());
                            throw new BusinessException("生成积分流水失败！");
                        }
                        log.info("保存消费流水成功，订单号:" + busiOrderTemp.getId() + ",加息券id:" + voucherId + "，加息券消费流水:" + resultDto.getData().getSerialNo());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("保存支付信息出错，订单号：" + busiOrderTemp.getId());
                    busiOrderIntegralService.refundVoucher(customerMainInfo.getCmNumber(), busiOrderTemp);
                    throw new BusinessException(BussErrorCode.ERROR_CODE_0000.getErrorcode(), consumeResultDto.getMsg());
                }
                //重新计算订单到期本金和订单汇率值
                Integer voucherDay = voucherDto.getDays();

                Map<String, Object> map = new HashMap<>();
                map.put("productId", busiOrderTemp.getProductId());
                BusiProductSub busiProduct = busiProductSubMapper.findProductById(map);

                Long orderDays = 0L;
                if (AppConstants.ProductSubjectType.FINANCE_PLAN.equals(busiProduct.getSubjectType())) {
                    orderDays = busiProduct.getCloseDay().longValue();
                } else {
                    orderDays = DateUtil.getIntervalDays2(busiOrderTemp.getInterestEndDate(), busiOrderTemp.getInterestStartDate()) + 1L;
                }

                MathContext mc = new MathContext(8, RoundingMode.DOWN);
                BigDecimal dayInterest = busiOrderTemp.getOrderAmt().multiply(busiOrderTemp.getYearRate()).divide(new BigDecimal(365), mc).setScale(8, BigDecimal.ROUND_DOWN);
                BigDecimal voucherDayInterest = busiOrderTemp.getOrderAmt().multiply(busiOrderTemp.getYearRate().add(voucherDto.getRate())).divide(new BigDecimal(365), mc).setScale(8, BigDecimal.ROUND_DOWN);

                if (voucherDay == null || voucherDay == 0) {
                    busiOrderTemp.setYearRate(busiOrderTemp.getYearRate().add(voucherDto.getRate()));
                    busiOrderTemp.setYearRateStr(NumberUtil.fortmatBigDecimalForOne(busiOrderTemp.getYearRate().multiply(new BigDecimal(100))) + "%");

                    //计算利息
                    BigDecimal principalin = voucherDayInterest.multiply(new BigDecimal(orderDays));
                    String princi = CoreUtil.BigDecimalAccurate(principalin);
                    principalin = new BigDecimal(princi);
                    //更新本息
                    BigDecimal afterRatePI = busiOrderTemp.getOrderAmt().add(principalin).setScale(2, BigDecimal.ROUND_DOWN);
                    busiOrderTemp.setRaiseRateIncome(afterRatePI.subtract(busiOrderTemp.getPrincipalinterest()).setScale(2, BigDecimal.ROUND_DOWN));
                    busiOrderTemp.setRaiseDayProfit(voucherDayInterest);    //更新加息券使用后的日息
                    busiOrderTemp.setRaiseDays(busiOrderTemp.getCloseDays()); //加息天数为该产品的封闭期
                    busiOrderTemp.setPrincipalinterest(afterRatePI);

                } else {
                    if (orderDays - voucherDay > 0) {
                        busiOrderTemp.setYearRateStr(
                                NumberUtil.fortmatBigDecimalForOne(busiOrderTemp.getYearRate().add(voucherDto.getRate()).multiply(new BigDecimal(100))) +
                                        "%(前" + voucherDay + "天)" + "\n" +
                                        NumberUtil.fortmatBigDecimalForOne(busiOrderTemp.getYearRate().multiply(new BigDecimal(100))) +
                                        "%(后" + (orderDays - voucherDay) + "天)");
                    } else {
                        busiOrderTemp.setYearRateStr(NumberUtil.fortmatBigDecimalForOne(busiOrderTemp.getYearRate().add(voucherDto.getRate()).multiply(new BigDecimal(100))) + "%");
                    }

                    //计算不加息天数
                    Long noCloseDays = orderDays - voucherDay;
                    voucherDay = noCloseDays > 0 ? voucherDay : orderDays.intValue();
                    noCloseDays = noCloseDays > 0 ? noCloseDays : 0L;

                    //计算利息
                    BigDecimal principalin = voucherDayInterest.multiply(new BigDecimal(voucherDay)).
                            add(dayInterest.multiply(new BigDecimal(noCloseDays)));
                    //更新本息
                    BigDecimal afterRatePI = busiOrderTemp.getOrderAmt().add(principalin).setScale(2, BigDecimal.ROUND_DOWN);
                    busiOrderTemp.setRaiseRateIncome(afterRatePI.subtract(busiOrderTemp.getPrincipalinterest()).setScale(2, BigDecimal.ROUND_DOWN));
                    busiOrderTemp.setPrincipalinterest(afterRatePI);
                    busiOrderTemp.setRaiseDayProfit(voucherDayInterest);    //更新加息券使用后的日息
                    busiOrderTemp.setRaiseDays(Long.valueOf(voucherDay));
                }
                int n1 = busiOrderSubMapper.updateByOrderYearRateStr(busiOrderTemp);
                if (n1 != 1) {
                    throw new BusinessException("加息收益-更新子订单失败");
                }
                BusiOrderTemp order = new BusiOrderTemp();
                CopyUtil.copyProperties(order, busiOrderTemp);
                int n2 = busiOrderTempMapper.updateByOrderYearRateStr(order);
                if (n2 != 1) {
                    throw new BusinessException("加息收益-更新主订单失败");
                }
            } else {
                log.info("消费加息券失败,订单号：" + busiOrderTemp.getId());
                busiOrderIntegralService.refundVoucher(customerMainInfo.getCmNumber(), busiOrderTemp);
                throw new BusinessException(BussErrorCode.ERROR_CODE_0000.getErrorcode(), consumeResultDto.getMsg());
            }
        } else {
            throw new BusinessException(BussErrorCode.ERROR_CODE_0000.getErrorcode(), "加息券系统--投资使用加息券失败，没有正确返回");
        }
    }

    /*
     * 消费流水
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void consumeFlow(IntegralExchangeResDto dto, BusiOrderTemp busiOrderTemp, String integralAmt, String redId, String couponOrderNo) {
        //保存订单积分付款信息
        log.info("保存消费流水，订单号:" + busiOrderTemp.getId() + "，积分金额:" + integralAmt + ",红包id:" + redId + "，红包消费流水:" + couponOrderNo);
        Map<String, String> map = Maps.newTreeMap();
        map.put("orderNo", busiOrderTemp.getId().toString());
        BusiOrderIntegral ordreIntergral = busiOrderIntegralService.selectByCondition(map);
        if (ordreIntergral == null) {
            Integer redPacketAmout = 0;
            if (StringUtils.isNotEmpty(redId)) {
                ResultDto<CouponDto> couponDtoResultDto = couponFacadeService.getCouponByNo(redId);
                if (couponDtoResultDto.isSuccess()) {
                    CouponDto couponDto = couponDtoResultDto.getData();
                    redPacketAmout = couponDto.getAmount();
                }
            }
            if (dto == null) {
                dto = new IntegralExchangeResDto();
            }
            BusiOrderIntegral orderIntegral = new BusiOrderIntegral();
            orderIntegral.setIntegralProductSerialNo(dto.getProductSerialNo());
            orderIntegral.setIntegralOrderNo(dto.getOrderNo());
            orderIntegral.setOrderNo(busiOrderTemp.getId());
            orderIntegral.setIntegralAmount(StringUtils.isEmpty(integralAmt) ? "0" : integralAmt);
            //查询积分兑换比例
            IntegralProductDto productDto = accountOverview520003Service.getIntegralProductInfo();
            if (productDto != null) {
                float rate = productDto.getReturnValue() / productDto.getIntegral();
                orderIntegral.setIntegralRmbAmount(StringUtils.isEmpty(integralAmt) ? "0" : String.valueOf(rate * Float.parseFloat(integralAmt)));
            }
            orderIntegral.setCashAmount(busiOrderTemp.getOrderAmt().subtract(new BigDecimal(redPacketAmout)).subtract(new BigDecimal(dto.getValue() == null ? 0 : dto.getValue())).toString());
            orderIntegral.setCouponId(redId);
            orderIntegral.setCouponAmount(redPacketAmout.toString());
            orderIntegral.setCouponOrderNo(couponOrderNo);

            int num = busiOrderIntegralService.insert(orderIntegral);
            if (num < 1) {
                log.info("保存消费流水失败，订单号:" + busiOrderTemp.getId() + "，积分:" + integralAmt + ",红包:" + redId + "，红包流水:" + couponOrderNo);
                throw new BusinessException("生成积分流水失败！");
            }
            log.info("保存消费流水成功，订单号:" + busiOrderTemp.getId() + "，积分:" + integralAmt + ",红包:" + redId + "，红包流水:" + couponOrderNo);
        } else {
            log.info("保存消费流水失败，订单号:" + busiOrderTemp.getId() + "，订单已存在支付流水，不能重复支付！");
            throw new BusinessException("订单已存在支付流水，不能重复支付！");
        }
    }

    /*
     *账户消费
     */
    @Transactional(rollbackFor = Exception.class)
    private void consumeAccount(CustomerMainInfo mainInfo, BusiOrderTemp order) {
        log.info("账户消费开始，订单号：" + order.getId());
        BigDecimal accountPayAmount = order.getOrderAmt();
        AccountOprDto dto = new AccountOprDto();
        dto.setAccountNo(mainInfo.getCmNumber());
        Map<String, String> map = Maps.newTreeMap();
        map.put("orderNo", order.getId().toString());
        BusiOrderIntegral integral = busiOrderIntegralService.selectByCondition(map);
        if (integral != null) {//积分或红包支付
            String integralAmt = integral.getIntegralAmount();

            if (!StringUtils.isEmpty(integralAmt)) {
                //查询积分兑换比例
                IntegralProductDto productDto = accountOverview520003Service.getIntegralProductInfo();
                if (productDto != null) {
                    float rate = productDto.getReturnValue() / productDto.getIntegral();
                    accountPayAmount = accountPayAmount.subtract(new BigDecimal(rate * Float.parseFloat(integralAmt)));
                }
            }

            if (!StringUtils.isEmpty(integral.getCouponAmount())) {
                accountPayAmount = accountPayAmount.subtract(new BigDecimal(integral.getCouponAmount()));
            }
        }
        dto.setAmount(accountPayAmount);
        dto.setOrderNo(order.getOrderId());
        dto.setTransNo("PTZF" + order.getOrderId());
        ResultDto<AccountOprResultDto> resultDto = null;
        if (AppConstants.OrderTransferStatus.ORDER_TRANSFER.equals(order.getTransferType())) {
            resultDto = accountFacadeService.transferee(dto);
        } else {
            resultDto = accountFacadeService.purchase(dto);
        }
        if (resultDto == null) {
            log.error("账户消费失败，订单号：" + order.getId() + ",原因：账户系统调用失败");
            throw new BusinessException("支付失败：账户系统调用失败");
        }
        if (resultDto.isSuccess()) {
            log.info("账户消费，账户端成功，订单号：" + order.getId());
            if (integral == null) {
                integral = new BusiOrderIntegral();
                integral.setOrderNo(order.getId());
                integral.setCashAmount(accountPayAmount.toString());
                integral.setAccountSeriNo(resultDto.getData().getRecordNum());
                int num = busiOrderIntegralService.insert(integral);
                if (num == 1) {
                    log.info("账户消费，保存busi_order_integral成功，订单号：" + order.getId());
                } else {
                    log.info("账户消费，保存busi_order_integral失败，订单号：" + order.getId());
                    throw new BusinessException("保存支付流水失败！");
                }
            } else {
                integral.setAccountSeriNo(resultDto.getData().getRecordNum());
                int num = busiOrderIntegralService.updateByCondition(integral);
                if (num == 1) {
                    log.info("账户消费，更新busi_order_integral成功，订单号：" + order.getId());
                } else {
                    log.info("账户消费，更新busi_order_integral失败，订单号：" + order.getId());
                    throw new BusinessException("保存支付流水失败！");
                }
            }
        } else {
            log.error("账户消费失败，订单号：" + order.getId() + ",原因：" + resultDto.getMsg());
            throw new BusinessException("支付失败：" + resultDto.getMsg());
        }

    }

    /*
     *捞财宝消费
     *修改产品余额，订单状态
     */
    public void consumeProduct(BusiOrderTemp order) {
        log.info("捞财宝更新产品库存，订单号：" + order.getId());
        BigDecimal orderAmt = order.getOrderAmt();
        Map<String, Object> map = Maps.newTreeMap();
        map.put("amt", orderAmt);
        map.put("productId", order.getProductId());
        boolean isOverSold = sysSwitchService.getSwitchIsOn("isOverSold");
        if (!isOverSold) {//可以超卖
            map.put("isOverSold", "false");
        }
        int num = busiProductMapper.updateInvestAmt(map);
        if (num == 1) {
            log.info("捞财宝更新产品库存成功，订单号：" + order.getId());
            BusiProduct product = busiProductMapper.selectByPrimaryKey(order.getProductId());
            Long contractId = product.getContractId();
            if (product.getTotalInvestAmt().compareTo(product.getProductPrincipal()) >= 0) {
                BusiProductContract contract = new BusiProductContract();
                contract.setId(contractId);
                contract.setIsFinish("1");
                busiProductContractMapper.updateByPrimaryKeySelective(contract);
            }
        } else {
            log.info("捞财宝更新产品库存失败，订单号：" + order.getId());
            throw new BusinessException(AppConstants.PayStatusContants.getPayStatusDesc(AppConstants.PayStatusContants.SELL_OUT));
        }
    }

    /*
     *积分退款
     */
    @Transactional
    private void refundIntegral(BusiOrderTemp busiOrder) {
        log.info("退还积分开始，订单号：" + busiOrder.getId());
        Map<String, String> param = Maps.newTreeMap();
        param.put("orderNo", busiOrder.getId().toString());
        BusiOrderIntegral busiOrderIntegral = busiOrderIntegralService.selectByCondition(param);
        if (busiOrderIntegral == null) {
            log.info("退还积分，订单无积分消费，订单号：" + busiOrder.getId());
        } else if (!StringUtils.isEmpty(busiOrderIntegral.getIntegralAmount())) {
            if (!"0".equals(busiOrderIntegral.getIntegralAmount())) {//有需要退还的积分
                log.info("退还积分，订单有积分消费，订单号：" + busiOrder.getId());
                IntegralOrderRefundDto refundDto = new IntegralOrderRefundDto();
                refundDto.setChannelOrderNo(busiOrder.getOrderId());//订单号
                refundDto.setOrderNo(busiOrderIntegral.getIntegralOrderNo());//积分订单号
                String transNo = SerialNoGeneratorService.generateTransNoByOrderId(busiOrder.getOrderId(), "REFUND");
                refundDto.setTransNo(transNo);
                ResultDto result = integralOrderFacadeService.refund(refundDto);
                if (result.isSuccess()) {
                    log.info("退还积分成功，订单号：" + busiOrder.getId());
                    //正常退还积分
                } else {
                    log.info("退还积分失败，订单号：" + busiOrder.getId());
                    throw new BusinessException(result.getMsg());
                }
            }
        }
    }

    /*
     *红包退款
     */
    @Transactional
    private void refundCoupon(BusiOrderTemp busiOrder) {
        log.info("退还红包开始，订单号：" + busiOrder.getId());
        Map<String, String> param = Maps.newTreeMap();
        param.put("orderNo", busiOrder.getId().toString());
        BusiOrderIntegral busiOrderIntegral = busiOrderIntegralService.selectByCondition(param);
        if (busiOrderIntegral == null) {
            log.info("退还红包，订单无红包消费，订单号：" + busiOrder.getId());
        } else if (!StringUtils.isEmpty(busiOrderIntegral.getCouponAmount())) {
            if (!"0".equals(busiOrderIntegral.getCouponAmount()) && !StringUtils.isEmpty(busiOrderIntegral.getCouponOrderNo())) {//有需要退还的红包
                log.info("退还红包，订单有红包消费，订单号：" + busiOrder.getId());
                CouponDto dto = new CouponDto();
                dto.setNo(busiOrderIntegral.getCouponId());
                dto.setOrderNo(busiOrder.getOrderId());
                dto.setSerialNo(busiOrderIntegral.getCouponOrderNo());
                ResultDto<String> resultDto = couponFacadeService.refundCoupon(dto);
                if (resultDto.isSuccess()) {
                    log.info("退还红包成功，订单号：" + busiOrder.getId());
                    BusiOrderIntegral bointegral = new BusiOrderIntegral();
                    bointegral.setOrderNo(busiOrderIntegral.getOrderNo());
                    bointegral.setCouponProductSerialNo(resultDto.getData());
                    int num = busiOrderIntegralService.updateByCondition(bointegral);
                    if (num == 1) {
                    } else {
                        throw new BusinessException("退还红包失败，更新消费流水失败");
                    }
                } else {
                    log.info("退还红包失败，订单号：" + busiOrder.getId());
                    throw new BusinessException(resultDto.getMsg());
                }
            }
        }
    }

    /*
     *校验账户余额
     */
    @Transactional
    private Boolean checkAccountAmount(CustomerMainInfo customerMainInfo, BusiOrderSub orderTemp, String integralAmt, String redId, BusiProductSub busiProduct, ReqMain reqMain,String loginType) throws Exception {
        log.info("校验用户账户余额，订单号：" + orderTemp.getId() + "，用户编号：" + customerMainInfo.getId());
        //查询积分兑换比例
        IntegralProductDto productDto = accountOverview520003Service.getIntegralProductInfo();
        //专区产品积分校验( 排除兜底人和特殊理财人 )
        Boolean productLimitType = innerEmployeeService.isProductLimitType(String.valueOf(busiProduct.getLimitType()));
        if(productLimitType && !(BusiConstants.LOGIN_TYPE_MANAGE.equalsIgnoreCase(loginType)
                || OrderConstants.OrderHoldType.HOLD_SPECIAL.equals(orderTemp.getHolderType()))){
            if (productDto == null || productDto.getIntegral() == null){
                log.info("查询积分兑换比例失败!");
                return false;
            }
            BigDecimal integral = new BigDecimal(productDto.getIntegral());
            Result staffAuth = innerEmployeeService.staffAuth(customerMainInfo.getId(), reqMain.getReqHeadParam().getIp());
            if (!(staffAuth.getSuccess()) ||  StrUtil.equals(staffAuth.getData().toString(),"0")) {
                throw new BusinessException(staffAuth.getMessage());
            }
            // 订单金额必须是10的倍数
            if (orderTemp.getOrderAmt().intValue() % 10 !=0){
                throw new BusinessException("订单金额必须是10的倍数!");
            }
            //查询积分兑换比例
            String paymentRate = configParamBean.getStaffPaymentRate();
            //实际支付多少金额
            BigDecimal actualPayment = orderTemp.getOrderAmt().multiply(BigDecimal.ONE.subtract(new BigDecimal(paymentRate))).setScale(2, BigDecimal.ROUND_DOWN);
            //实际抵扣金额
            BigDecimal deductionAmount = orderTemp.getOrderAmt().subtract(actualPayment);
            if (deductionAmount.compareTo(new BigDecimal(integralAmt).divide(integral).setScale(2,BigDecimal.ROUND_DOWN)) != 0) {
                log.info("专区产品所使用积分抵扣金额比例须为9:1");
                return false;
            }
        }
        //使用积分抵扣金额
        BigDecimal orderAmt = orderTemp.getOrderAmt();
        if (!StringUtils.isEmpty(integralAmt)) {
            if (productDto != null) {
                float rate = productDto.getReturnValue() / productDto.getIntegral();
                orderAmt = orderAmt.subtract(new BigDecimal(rate * Float.parseFloat(integralAmt)));
            }
        }
        //使用红包抵扣金额
        if (!StringUtils.isEmpty(redId)) {
            ResultDto<CouponDto> couponDtoResultDto = couponFacadeService.getCouponByNo(redId);
            if (couponDtoResultDto.isSuccess()) {
                CouponDto couponDto = couponDtoResultDto.getData();
                Integer redAmt = couponDto.getAmount();
                orderAmt = orderAmt.subtract(new BigDecimal(redAmt));
            } else {
                log.info("校验用户账户余额，订单号：" + orderTemp.getId() + "，用户编号：" + customerMainInfo.getId() + "，红包编号：" + redId + "未查询到红包！");
            }
        }
        BigDecimal accountBalance = accountOverview520003Service.getAccountBalance(customerMainInfo);
        if (accountBalance.compareTo(orderAmt) >= 0) {
            log.info("校验用户账户余额结束，余额充足，订单号：" + orderTemp.getId() + "，用户编号：" + customerMainInfo.getId());
            return true;
        } else {
            log.info("校验用户账户余额结束，余额不足，订单号：" + orderTemp.getId() + "，用户编号：" + customerMainInfo.getId());
            return false;
        }
    }

    public int getInvestPeriod(BusiOrderSub orderTemp, BusiProductSub product) {
        DateTime start = new DateTime(orderTemp.getInterestStartDate());
        DateTime end = new DateTime(orderTemp.getInterestEndDate());
        int days;
        if (AppConstants.ProductSubjectType.FINANCE_PLAN.equals(product.getSubjectType())) {
            days = product.getCloseDay();
        } else {
            days = Days.daysBetween(start, end).getDays() + 1;
        }

        return days;
    }

    //设置限购时间
    @Transactional
    private void lockLimitCustomer(CustomerMainInfo mainInfo, BusiProductSub product, BusiProductRuleVo productRuleVo) {
        Long limitTypeId = product.getLimitType();
        BusiProductLimit busiProductLimit = busiProductLimitMapper.selectByPrimaryKey(limitTypeId);
        if (busiProductLimit != null && AppConstants.ProductLimitType.LIMIT.equals(busiProductLimit.getType())) {
            Date nowDate = new Date();
            if (nowDate.after(busiProductLimit.getStartDate()) && nowDate.before(busiProductLimit.getEndDate())) {
                //限购产品
                if (mainInfo.getLimitType() == null) {
                    mainInfo.setLimitType(limitTypeId);
                    mainInfo.setLimitLastDate(busiProductLimit.getEndDate());
                    mainInfo.setCmModifyDate(nowDate);
                    customerMainInfoMapper.updateByPrimaryKey(mainInfo);
                } else {
                    Date lastLockTime = mainInfo.getLimitLastDate();
                    if (lastLockTime != null && lastLockTime.before(nowDate)) {//限购期已过
                        mainInfo.setLimitType(limitTypeId);
                        mainInfo.setLimitLastDate(busiProductLimit.getEndDate());
                        mainInfo.setCmModifyDate(nowDate);
                        customerMainInfoMapper.updateByPrimaryKey(mainInfo);
                    }
                }
            }
        }
        //设置用户已购买过微信专享产品
        if ((busiProductLimit != null && AppConstants.ProductLimitType.WECHAT.equals(busiProductLimit.getType()))) {
            if (AppConstants.BuyWechatStatus.NOTBUY.equals(mainInfo.getBuyWechat())) {
                mainInfo.setBuyWechat(AppConstants.BuyWechatStatus.BUY);
                mainInfo.setCmModifyDate(new Date());
                customerMainInfoMapper.updateByPrimaryKey(mainInfo);
            }
        }
    }

    public void orderSendWechat(CustomerMainInfo mainInfo, BusiOrderTemp orderTemp, String productName) {
        Map<String, String> map = Maps.newTreeMap();
        DateTime dateTime = new DateTime(orderTemp.getOrderTime() == null ? DateTime.now() : orderTemp.getOrderTime());
        map.put("keyword1", dateTime.toString("yyyy-MM-dd HH:mm"));
        map.put("keyword2", productName);
        DateTime startTime = new DateTime(orderTemp.getInterestStartDate());
        map.put("keyword3", startTime.toString("yyyy-MM-dd"));
        NumberFormat nf = new DecimalFormat("###,###.##");
        map.put("keyword4", nf.format(orderTemp.getOrderAmt()) + "元");
        //到期利息
        map.put("keyword5", nf.format(orderTemp.getPrincipalinterest().subtract(orderTemp.getOrderAmt())) + "元");
        WeiChantDto weiChantDto = new WeiChantDto();
        weiChantDto.setOpenId(mainInfo.getOpenId());
        weiChantDto.setParam(map);
        weiChantDto.setTmlShortId(Constants.MSG_TPL_PURCHASE_SUCCESS);
        wechatFacadeService.sendTemplateMsg(weiChantDto);

    }

    protected void sendWxTemplateMsg(WeiChantDto weiChantDto) {
        try {
            com.zendaimoney.laocaibao.wx.api.dto.ResultDto resultDto = wechatFacadeService.sendTemplateMsg(weiChantDto);
            if (resultDto.isSuccess()) {
                log.info(resultDto.getMsg());
            } else {
                log.error("微信推送失败：{} | openId: {} | map: {}", new Object[]{resultDto.getMsg(), weiChantDto.getOpenId(), JSON.toJSONString(weiChantDto.getParam())});
            }
        } catch (Exception e) {
            log.error("微信推送异常：{} | openId: {} | map: {}", new Object[]{e.getMessage(), weiChantDto.getOpenId(), JSON.toJSONString(weiChantDto.getParam())});
        }
    }


    //获取申购成功banner
    private List<BannerDTO> getbugSuccessBanners() {
        //申购成功banner
        return busiBannerService.queryBannerDTOListByType("5");
    }


    private int getSmsChannel() {
        List<SysParameter> params = SystemParameterHelper.findParameterByPrType("messageChannel");
        SysParameter sysParameter = params.size() > 0 ? params.get(0) : null;
        int msgTag = 0;
        try {
            msgTag = sysParameter == null ? 0 : Integer.parseInt(sysParameter.getPrValue());
        } catch (Exception e) {
            msgTag = 0;
        }
        return msgTag;
    }

    private void pushWxOrSms(CustomerMainInfo customerMainInfo, BusiOrderSub order, BusiProductSub busiProductVO) {
        try {
            WeiChantDto weiChantDto = new WeiChantDto();
            //微信内容
            if (StringUtils.isNotEmpty(customerMainInfo.getOpenId())) {
                if (StringUtils.isNotBlank(busiProductVO.getSubjectType()) && AppConstants.ProductSubjectType.FINANCE_PLAN.equals(busiProductVO.getSubjectType())) {
                    Map<String, String> map = Maps.newTreeMap();
                    map.put("keyword1", busiProductVO.getProductName());
                    NumberFormat nf = new DecimalFormat("###,###.##");
                    map.put("keyword2", nf.format(order.getOrderAmt()) + "元");
                    DateTime dateTime = new DateTime(order.getOrderTime() == null ? DateTime.now() : order.getOrderTime());
                    map.put("keyword3", dateTime.toString("yyyy-MM-dd HH:mm"));
                    weiChantDto.setOpenId(customerMainInfo.getOpenId());
                    weiChantDto.setParam(map);
                    weiChantDto.setTmlShortId(Constants.MSG_TPL_PURCHASE_SUCCESS_NOTICE);
                } else {
                    Map<String, String> map = Maps.newTreeMap();
                    DateTime dateTime = new DateTime(order.getOrderTime() == null ? DateTime.now() : order.getOrderTime());
                    map.put("keyword1", dateTime.toString("yyyy-MM-dd HH:mm"));
                    map.put("keyword2", busiProductVO.getProductName());
                    DateTime startTime = new DateTime(order.getInterestStartDate());
                    map.put("keyword3", startTime.toString("yyyy-MM-dd"));
                    NumberFormat nf = new DecimalFormat("###,###.##");
                    map.put("keyword4", nf.format(order.getOrderAmt()) + "元");
                    //到期利息
                    map.put("keyword5", nf.format(order.getPrincipalinterest().subtract(order.getOrderAmt())) + "元");
                    weiChantDto.setOpenId(customerMainInfo.getOpenId());
                    weiChantDto.setParam(map);
                    weiChantDto.setTmlShortId(Constants.MSG_TPL_PURCHASE_SUCCESS);
                }
                com.zendaimoney.laocaibao.wx.api.dto.ResultDto resultDto = wechatFacadeService.sendTemplateMsg(weiChantDto);
                if (resultDto.isSuccess()) {
                    log.info("发送微信成功，订单号:{}", order.getId());
                } else {
                    SendSmNotifyReqDto reqDto = new SendSmNotifyReqDto();
                    reqDto.setMobile(customerMainInfo.getCmCellphone());

                    String msgContent = "尊敬的" + customerMainInfo.getCmRealName() + ",恭喜您成功向" + busiProductVO.getProductName() +
                            "出借" + String.valueOf(order.getOrderAmt()) + "元，祝您多捞多得！";
                    //理财计划短信内容 subjectType:4
                    if (StringUtils.isNotBlank(busiProductVO.getSubjectType()) && AppConstants.ProductSubjectType.FINANCE_PLAN.equals(busiProductVO.getSubjectType())) {
                        msgContent = "尊敬的" + customerMainInfo.getCmRealName() + ",恭喜您已成功加入智投宝:" + busiProductVO.getProductName() +
                                ",加入金额为" + String.valueOf(order.getOrderAmt()) + "元。更多详情前往‘我的-我的网贷’中查看。";
                    }
                    reqDto.setSendMsg(msgContent);
                    MessageResultDto rspDto = iSmFacadeService.sendNotifyMsg(reqDto);
                    if (rspDto.isSuccess()) {
                        log.info("手机号" + customerMainInfo.getCmCellphone() + ",发送通知短信成功!");
                    } else {
                        log.error("手机号" + customerMainInfo.getCmCellphone() + ",发送通知短信失败!失败码:" + rspDto.getCode() + ",失败原因:" + rspDto.getMsg());
                    }
                }
            } else {
                SendSmNotifyReqDto reqDto = new SendSmNotifyReqDto();
                //短信内容
                reqDto.setMobile(customerMainInfo.getCmCellphone());
                String msgContent = "尊敬的" + customerMainInfo.getCmRealName() + ",恭喜您成功向" + busiProductVO.getProductName() +
                        "出借" + String.valueOf(order.getOrderAmt()) + "元，祝您多捞多得！";
                //理财计划短信内容 subjectType:4
                if (StringUtils.isNotBlank(busiProductVO.getSubjectType()) && AppConstants.ProductSubjectType.FINANCE_PLAN.equals(busiProductVO.getSubjectType())) {
                    msgContent = "尊敬的" + customerMainInfo.getCmRealName() + ",恭喜您已成功加入智投宝:" + busiProductVO.getProductName() +
                            ",加入金额为" + String.valueOf(order.getOrderAmt()) + "元。更多详情前往‘我的-我的网贷’中查看。";
                }
                reqDto.setSendMsg(msgContent);
                // smsDto.setSmsChannel(String.valueOf(getSmsChannel()));
                MessageResultDto resultDto = iSmFacadeService.sendNotifyMsg(reqDto);
                if (resultDto.isSuccess()) {
                    log.info("发送短信成功，订单号:{}", order.getId());
                } else {
                    log.error("发送短信失败，订单号:{}", order.getId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("发送短信或微信异常,订单号:{},原因:{}", order.getId(), e.getMessage());
        }
    }

    private boolean checkPassword(PayPasswordDTO dto, CustomerMainInfo mainInfo, String payPassword) {
        if (StringUtils.isEmpty(mainInfo.getPayPassword())) {
            throw new BusinessException("您没有设置交易密码,请先设置交易密码！");
        }
        if (StringUtils.isBlank(payPassword)) {
            throw new BusinessException("交易密码不能为空！");
        }
        boolean isCheckPass = false;
        String errorMsg = "";
        String errorTimes = sysParameterService.findOneByPrType("resetErrorTimes").getPrValue();
        String lockHours = sysParameterService.findOneByPrType("resetLockHour").getPrValue();
        boolean isLock = customerMainInfoService.isLocked(mainInfo.getPayLockTime(), Integer.parseInt(lockHours));
        if (isLock) {//用户密码已锁定
            errorMsg = PropertiesUtil.getDescrptionProperties().get("pay.pay_password.lock");
            errorMsg = MessageFormat.format(errorMsg, errorTimes, lockHours);
            dto.setIsLock(true);
            dto.setErrorTime(mainInfo.getPayErrorTime());
            dto.setMsg(errorMsg);
        } else {//未锁定
            String password = customerMainInfoService.genLoginPassword(payPassword.toUpperCase(), mainInfo.getCmSalt());
            if (mainInfo.getPayPassword().toUpperCase().equals(password)) {//密码校验通过
                isCheckPass = true;
                mainInfo.setPayLockTime(null);
                mainInfo.setPayErrorTime(0);
                mainInfo.setPayErrorLastTime(null);
            } else {//密码校验不通过
                Integer errorTime = mainInfo.getPayErrorTime();
                DateTime payErrorLastTime = new DateTime(mainInfo.getPayErrorLastTime() == null ? DateTime.now().toDate() : mainInfo.getPayErrorLastTime());
                Long payErrorLastTimeStr = Long.parseLong(payErrorLastTime.toString("yyyyMMdd"));
                Long now = Long.parseLong(DateTime.now().toString("yyyyMMdd"));
                if (now > payErrorLastTimeStr) {//清除隔天的密码错误记数
                    errorTime = 0;
                }
                errorTime = errorTime + 1;
                if (errorTime >= Short.parseShort(errorTimes)) {
                    DateTime date = DateTime.now();
                    mainInfo.setPayLockTime(date.toDate());//设置锁定账户时间
                    mainInfo.setPayErrorTime(0);
                    errorMsg = PropertiesUtil.getDescrptionProperties().get("pay.pay_password.lock");
                    errorMsg = MessageFormat.format(errorMsg, errorTimes, lockHours);
                    isLock = true;//账户锁定
                } else {
                    isLock = false;
                    errorMsg = PropertiesUtil.getDescrptionProperties().get("pay.pay_password.error");
                    //errorMsg = MessageFormat.format(errorMsg, errorTime, errorTimes, lockHours);
                    errorMsg = MessageFormat.format(errorMsg, Integer.parseInt(errorTimes) - errorTime);
                    mainInfo.setPayErrorTime(errorTime);
                }
                mainInfo.setPayErrorLastTime(DateTime.now().toDate());
                dto.setErrorTime(errorTime);
                dto.setIsLock(isLock);
                dto.setMsg(errorMsg);
            }
            customerMainInfoService.update(mainInfo);
        }
        return isCheckPass;
    }

    /**
     * 规则校验
     */
    private BusiProductRuleVo checkRule(String loginType, CustomerMainInfo mainInfo, Long productId) {
        BusiProductRuleVo productRuleVo = busiProductMapper.selectProductWithRule(productId);
        if (productRuleVo == null || productRuleVo.getLimitType() == null) {
            throw new BusinessException(AppConstants.LoginBusiOrder.BUSIORDER_ERROR_8);
        }
        return productRuleVo;
    }

    /**
     * 预约券消费
     */
    @Transactional
    private void consumeAppointment(CustomerMainInfo customerMainInfo, BusiOrderSub busiOrderTemp, String appointmentId) {
        log.info(">>>>>订单号:{}，预约券编号:{},消费预约券开始：", appointmentId, busiOrderTemp.getId());
        AppointmentDto appointmentDto = new AppointmentDto();
        appointmentDto.setAccountNo(customerMainInfo.getCmNumber());
        appointmentDto.setNo(appointmentId);
        appointmentDto.setOrderNo(busiOrderTemp.getOrderId());
        appointmentDto.setTransNo(SerialNoGeneratorService.generateTransNoByOrderId(busiOrderTemp.getOrderId(), "CONSUMEPRE"));
        ResultDto<AppointmentOprResDto> resultDto = appointmentFacadeService.consume(appointmentDto);
        if (resultDto != null) {
            if (resultDto.isSuccess()) {
                log.info(">>>>>订单号:{}，预约券编号:{},消费预约券结束：", appointmentId, busiOrderTemp.getId());
                String serialNo = resultDto.getData().getSerialNo();
                try {
                    //写入订单消费流水表
                    log.info("保存消费流水，订单号:" + busiOrderTemp.getId() + "，预约券id:" + appointmentId + "，预约券消费流水:" + serialNo);
                    Map<String, String> map = Maps.newTreeMap();
                    map.put("orderNo", busiOrderTemp.getId().toString());
                    BusiOrderIntegral orderIntegral = busiOrderIntegralService.selectByCondition(map);
                    if (orderIntegral == null) {
                        orderIntegral = new BusiOrderIntegral();
                        orderIntegral.setOrderNo(busiOrderTemp.getId());
                        orderIntegral.setAppointmentId(appointmentId);
                        orderIntegral.setAppointmentSerialNo(serialNo);
                        int num = busiOrderIntegralService.insert(orderIntegral);
                        if (num < 1) {
                            log.info("保存消费流水失败，订单号:" + busiOrderTemp.getId() + "，预约券id:" + appointmentId + "，预约券消费流水:" + serialNo);
                            throw new BusinessException("生成预约券流水失败！");
                        }
                        log.info("保存消费流水成功，订单号:" + busiOrderTemp.getId() + "，预约券id:" + appointmentId + "，预约券消费流水:" + serialNo);
                    } else {
                        orderIntegral.setAppointmentId(appointmentId);
                        orderIntegral.setAppointmentSerialNo(serialNo);
                        int num = busiOrderIntegralService.updateByCondition(orderIntegral);
                        if (num != 1) {
                            log.info("更新消费流水失败，订单号:" + busiOrderTemp.getId() + "，预约券id:" + appointmentId + "，预约券消费流水:" + serialNo);
                            throw new BusinessException("更新预约券流水失败！");
                        }
                        log.info("更新消费流水成功，订单号:" + busiOrderTemp.getId() + "，预约券id:" + appointmentId + "，预约券消费流水:" + serialNo);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("保存支付信息出错，订单号：{},预约券编号", busiOrderTemp.getId(), appointmentId);
                    throw new BusinessException(BussErrorCode.ERROR_CODE_0000.getErrorcode(), resultDto.getMsg());
                }
            } else {
                log.info("消费预约券失败,订单号：" + busiOrderTemp.getId());
                throw new BusinessException(BussErrorCode.ERROR_CODE_0000.getErrorcode(), resultDto.getMsg());
            }
        } else {
            throw new BusinessException(BussErrorCode.ERROR_CODE_0000.getErrorcode(), "预约券系统--使用预约券失败，没有正确返回");
        }
    }

    private String gainAppointmnetId(String loginType, BusiProductSub busiProductVO, CustomerMainInfo customerMainInfo, BigDecimal orderAmt) {
        //是否预约产品
        String appointmentId = "";
        //manage兜底不校验预约券
        if (!(BusiConstants.LOGIN_TYPE_MANAGE.equals(loginType)
                && AppConstants.PersonalLoan.PERSONAL_LOAN.equals(busiProductVO.getPersonLoan())
                && AppConstants.ProductTransferStatus.COMMON_PRODUCT.equals(busiProductVO.getIsTransfer()))) {
            if (busiProductVO.getLimitType() != null && busiProductVO.getLimitType().equals(AppConstants.ProductLimitType.BESPEAK)) {
                //是否存在正常预约券使用时间内
                if (new Date().getTime() <= busiProductVO.getReservatTime().getTime()) {
                    Long days = 0L;
                    if (busiProductVO.getInterestEndDate() != null && busiProductVO.getInterestStartDate() != null) {
                        days = (busiProductVO.getInterestEndDate().getTime() - busiProductVO.getInterestStartDate().getTime()) / (24 * 60 * 60 * 1000);
                    }
                    ResultDto<AppointmentDto> resultDto = appointmentFacadeService.queryAvailAppointment(customerMainInfo.getCmNumber(), orderAmt, days.intValue());
                    log.info("***********获取用户有效的预约券结果********" + resultDto);
                    if (resultDto != null) {
                        if (resultDto.isSuccess()) {
                            AppointmentDto appointment = resultDto.getData();
                            if (appointment != null) {
                                appointmentId = appointment.getNo();
                            } else {
                                throw new BusinessException("没有可用预约券,请前往个人中心-捞财币商城兑换预约券或预约时间结束后再购买!");
                            }
                        } else {
                            throw new BusinessException("bespeak.record");
                        }
                    }
                }
            }
        }
        return appointmentId;
    }


    /**
     * 计算实付现金
     *
     * @param order
     * @param integralAmt
     * @param redId
     * @return
     */
    private BigDecimal gainCashAmount(BusiOrderSub order, String integralAmt, String redId) {
        log.info("计算实付现金开始，订单号：" + order.getId());
        BigDecimal accountPayAmount = order.getOrderAmt();
        if (StringUtils.isNotBlank(integralAmt)) {
            //查询积分兑换比例
            IntegralProductDto productDto = accountOverview520003Service.getIntegralProductInfo();
            if (productDto != null) {
                float rate = productDto.getReturnValue() / productDto.getIntegral();
                accountPayAmount = accountPayAmount.subtract(new BigDecimal(rate * Float.parseFloat(integralAmt)));
            }
        }
        if (StringUtils.isNotBlank(redId)) {
            ResultDto<CouponDto> couponDtoResultDto = couponFacadeService.getCouponByNo(redId);
            if (couponDtoResultDto.isSuccess()) {
                CouponDto couponDto = couponDtoResultDto.getData();
                accountPayAmount = accountPayAmount.subtract(new BigDecimal(couponDto.getAmount()));
            }
        }
        log.info("计算实付现金结束开始，订单号：" + order.getId());
        return accountPayAmount;
    }

    /*
     * 个贷产品支付回退
     */
    @Transactional
    public void refundPersonLoan(CustomerMainInfo customerMainInfo, BusiProduct busiProduct, BusiOrderTemp order, ReqMain reqMain) {
        log.info("个贷退款，订单号：" + order.getId());
        Map<String, String> map = Maps.newTreeMap();
        map.put("orderNo", order.getId().toString());
        BusiOrderIntegral ordreIntergral = busiOrderIntegralService.selectByCondition(map);
        if (ordreIntergral != null) {
            //查询公司收款户
            String accountCompany = SystemParameterHelper.findOneParameterByPrType("account_gssk");
            AccountReceiveCollectDto collect = new AccountReceiveCollectDto();
            collect.setTransNo("GDZFTK" + order.getOrderId());
            List<AccountReceiveCollectDto.AccountDetail> accounts = Lists.newArrayList();
            //个人现金账户-收入
            AccountReceiveCollectDto.AccountDetail accountPerson = new AccountReceiveCollectDto.AccountDetail();
            accountPerson.setAccountNo(customerMainInfo.getCmNumber());
            accountPerson.setAccountType(AccountWholeType.PERSONAL);
            accountPerson.setAccountOprDirection(AccountOprDirection.IN);
            accountPerson.setOrderNo(order.getOrderId());
            accountPerson.setRelAccountNo(accountCompany);
            accountPerson.setRelAccountType(AccountWholeType.COM_RECEIVE_COLLECT);
            accountPerson.setServiceCode(busiProduct.getSubjectNo());
            accountPerson.setServiceName(busiProduct.getProductName());

            //公司收款账户-支出
            AccountReceiveCollectDto.AccountDetail accountCompanyRCV = new AccountReceiveCollectDto.AccountDetail();
            accountCompanyRCV.setAccountNo(accountCompany);
            accountCompanyRCV.setAccountType(AccountWholeType.COM_RECEIVE_COLLECT);
            accountCompanyRCV.setAccountOprDirection(AccountOprDirection.OUT);
            accountCompanyRCV.setRelAccountNo(customerMainInfo.getCmNumber());
            accountCompanyRCV.setRelAccountType(AccountWholeType.PERSONAL);
            accountCompanyRCV.setServiceCode(busiProduct.getSubjectNo());
            accountCompanyRCV.setServiceName(busiProduct.getProductName());
            accountCompanyRCV.setOrderNo(order.getOrderId());

            //回退现金
            if (StringUtils.isNotBlank(ordreIntergral.getAccountSeriNo())) {
                accountPerson.setAccountOprType(AccountOprType.PURCHASE_BACK);
                accountPerson.setAmount(new BigDecimal(ordreIntergral.getCashAmount()));
                accountPerson.setOrderNo(order.getOrderId());
                accountCompanyRCV.setAccountOprType(AccountOprType.INVESTMENT_CASH_INCOME_BACK);
                accountCompanyRCV.setAmount(new BigDecimal(ordreIntergral.getCashAmount()));
                accounts.add(accountPerson);
                accounts.add(accountCompanyRCV);
            }
            //回退红包
            if (StringUtils.isNotBlank(ordreIntergral.getCouponOrderNo())) {
                AccountReceiveCollectDto.AccountDetail accountPersonCoupon = PropertiesUtils.copy(AccountReceiveCollectDto.AccountDetail.class, accountPerson);
                AccountReceiveCollectDto.AccountDetail accountCompanyRCVCoupon = PropertiesUtils.copy(AccountReceiveCollectDto.AccountDetail.class, accountCompanyRCV);
                accountPersonCoupon.setAccountOprType(AccountOprType.COUPON_APPLY_BACK);
                accountPersonCoupon.setAccountType(AccountWholeType.COUPON);
                accountPersonCoupon.setRelAccountType(AccountWholeType.COM_RECEIVE_COLLECT);
                accountCompanyRCVCoupon.setAccountOprType(AccountOprType.COUPON_APPLY_BACK);
                accountCompanyRCVCoupon.setRelAccountType(AccountWholeType.COUPON);
                AccountReceiveCollectDto.CouponDto couponDto = new AccountReceiveCollectDto.CouponDto();
                couponDto.setNo(ordreIntergral.getCouponId());
                couponDto.setOrderNo(order.getOrderId());
                couponDto.setName(customerMainInfo.getCmRealName());
                couponDto.setSerialNo(ordreIntergral.getCouponOrderNo());
                accountPersonCoupon.setCouponDto(couponDto);
                accountCompanyRCVCoupon.setCouponDto(couponDto);
                accounts.add(accountPersonCoupon);
                accounts.add(accountCompanyRCVCoupon);
            }
            //回退积分
            if (StringUtils.isNotBlank(ordreIntergral.getIntegralProductSerialNo())) {
                AccountReceiveCollectDto.AccountDetail accountPersonIntegral = PropertiesUtils.copy(AccountReceiveCollectDto.AccountDetail.class, accountPerson);
                AccountReceiveCollectDto.AccountDetail accountCompanyRCVCoupon = PropertiesUtils.copy(AccountReceiveCollectDto.AccountDetail.class, accountCompanyRCV);
                accountPersonIntegral.setAccountOprType(AccountOprType.INTEGRAL_APPLY_BACK);
                accountPersonIntegral.setAccountType(AccountWholeType.INTEGRAL);
                accountPersonIntegral.setRelAccountType(AccountWholeType.COM_RECEIVE_COLLECT);
                accountCompanyRCVCoupon.setAccountOprType(AccountOprType.INTEGRAL_APPLY_BACK);
                accountCompanyRCVCoupon.setRelAccountType(AccountWholeType.INTEGRAL);
                AccountReceiveCollectDto.IntegralDto integralDto = new AccountReceiveCollectDto.IntegralDto();
                integralDto.setChannel(AppConstants.APPLICATION_CONTEXT_NAME);
                integralDto.setChannelOrderNo(order.getOrderId());
                integralDto.setOrderNo(ordreIntergral.getIntegralOrderNo());
                integralDto.setIntegral(Integer.parseInt(ordreIntergral.getIntegralAmount()));
                integralDto.setProductNo("00001");
                integralDto.setPlatform(AppConstants.getAppType(reqMain.getReqHeadParam().getUserAgent()));
                accountPersonIntegral.setIntegralDto(integralDto);
                accountCompanyRCVCoupon.setIntegralDto(integralDto);
                accounts.add(accountPersonIntegral);
                accounts.add(accountCompanyRCVCoupon);
            }
            collect.setAccountDetails(accounts);
            try {
                ResultDto<List<ResultDto<AccountOprResultDto>>> resultDto = accountFacadeService.receiveCollect(collect);
                if (resultDto.isSuccess()) {
                    log.info("个贷账户退款，账户端完成，订单号：" + order.getId());
                    for (ResultDto<AccountOprResultDto> result : resultDto.getData()) {
                        AccountOprResultDto res = result.getData();
                        if (res != null && StringUtils.isNotBlank(res.getRecordNum())) {
                            //匹配红包流水
                            if (customerMainInfo.getCmNumber().equals(res.getAccountNo()) && AccountOprType.COUPON_APPLY_BACK.equals(res.getAccountOprType())) {
                                log.info("回退红包,订单号：" + order.getId());
                                ordreIntergral.setCouponProductSerialNo(res.getRecordNum());
                            }
                            //匹配积分流水
                            if (customerMainInfo.getCmNumber().equals(res.getAccountNo()) && AccountOprType.INTEGRAL_APPLY_BACK.equals(res.getAccountOprType())) {
                                log.info("回退积分,订单号：" + order.getId());
                                ordreIntergral.setIntegralProductSerialNo(res.getIntegralExchangeResDto().getProductSerialNo());
                            }
                            //匹配账户流水
                            if (customerMainInfo.getCmNumber().equals(res.getAccountNo()) && AccountOprType.REFUND.equals(res.getAccountOprType())) {
                                log.info("个贷账户退款,订单号：" + order.getId());
                                ordreIntergral.setAccountRefundSeriNo(res.getRecordNum());
                            }
                        }
                    }
                    int num = busiOrderIntegralService.updateByCondition(ordreIntergral);
                    if (num == 1) {
                        log.info("账户退款，前端完成，订单号：" + order.getId());
                    } else {
                        log.error("个贷账户退款失败，支付流水表失败，订单号：" + order.getId());
                        throw new BusinessException("个贷账户退款失败，更新支付流水表失败，订单号：" + order.getId());
                    }
                } else {
                    log.info("个贷账户退款失败，订单号：" + order.getId() + "，失败信息：" + resultDto.getMsg());
                    throw new BusinessException("个贷账户退款失败，订单号：" + order.getId() + "，" + resultDto.getMsg());
                }
            } catch (Exception e) {
                log.error("调用个贷账户退款发生异常:" + e.getMessage());
            }
        } else {
            log.info("个贷账户退款，订单号：" + order.getId() + "订单无消费流水！");
        }
    }


    //账户消费
    public void consumeNew(CustomerMainInfo customerMainInfo, BusiOrderSub order,
                           BusiProductSub product, String integralAmt, String redId,
                           String voucherId, ReqMain reqMain, BusiDebtTransfer transfer, String appointmentId) {
        log.info(">>>>>>>>>>支付开始，订单号：【】，订单类型：【】,转让状态【】", order.getId(), order.getProductType(), order.getTransferType());
        boolean isFirst = false;
        //锁定库存订单信息
        payDepositService.lockOrder(order, transfer, customerMainInfo);
        try {
            //消费加息券
            if (StringUtils.isNotBlank(voucherId)) {
                consumeVoucher(customerMainInfo, order, voucherId);
            }
            //消费预约券
            if (StringUtils.isNotBlank(appointmentId)) {
                consumeAppointment(customerMainInfo, order, appointmentId);
            }
            // 调用华瑞投标接口 华瑞-投标
            paySubjectToHR(customerMainInfo, reqMain, transfer, product, order, voucherId, integralAmt, redId);
            // 确定订单信息
            payDepositService.confirmOrder(order, transfer, customerMainInfo);

            // push 投标成功事件
            publishOrderEvent(order, product);
            //投标成功
            // 理财计划产品下单，生成一笔资金详情
            if (AppConstants.OrderProductType.FINANCE_PLAN == order.getProductType()) {
                saveFundDetail(product, customerMainInfo, order);
            }
            if (AppConstants.OrderProductType.FINANCE_PLAN_SUB != order.getProductType()) {
                //变更首单标志
                isFirst = isFirst(customerMainInfo);
                //异步-统计首笔订单信息
                reportDailyFirstInvestService.saveReport(order, customerMainInfo, isFirst);
                //同步统计用户每月投资（只统计普通订单，不包括转让订单）
                if (order.getTransferType().equals(AppConstants.OrderTransferStatus.ORDER_NORMAL)) {
                    busiAddupCustomerOrderService.setMonthInvestTotalAmt(customerMainInfo, order);
                }
                // 异步-统计用户购买数量
                reportDailyFirstInvestService.statisticsSalesVolume(customerMainInfo.getId());
                //异步-通知Marketing
                sendMsg(order, customerMainInfo, isFirst, product);
            }
            //异步-通知标的系统下单结果
            notifySubject(order, product, customerMainInfo, transfer);
            //异步-下单激活现金券
            welfareService.sendInvestCash(customerMainInfo.getCmNumber(), order);

        } catch (Exception e) {
            log.error(">>>>>>>>>>投标出现异常，进行回滚操作开始,订单号：【{}】", order.getId(), e);
            //执行回滚操作
            payDepositService.unLockOrder(order, transfer, customerMainInfo, appointmentId, voucherId, isFirst);
            log.error(">>>>>>>>>>投标出现异常，进行回滚操作结束,订单号：【{}】", order.getId(), e);
            throw new BusinessException("支付失败");
        }
        log.info(">>>>>>>>>>支付结束，订单号：" + order.getId());
    }

    private void publishOrderEvent(BusiOrderSub order, BusiProductSub product){
        if(AppConstants.CreditSource.WACAI.name().equals(product.getCreditSource()))
            ApplicationEventSupport.publishApplicationEvent(new ApplicationEventSupport.NewOrderEvent(order.getOrderId()));
    }

    /**
     * 插入资金详情
     *
     * @param product
     * @param customerMainInfo
     * @param order
     */
    private void saveFundDetail(BusiProductSub product, CustomerMainInfo customerMainInfo, BusiOrderSub order) {
        BusiFundDetail fundDetail = new BusiFundDetail();
        Date date = new Date();
        fundDetail.setProductId(product.getId());
        fundDetail.setCloseDay(Long.valueOf(product.getCloseDay()));
        fundDetail.setCmCellPhone(customerMainInfo.getCmCellphone());
        fundDetail.setCmNumber(customerMainInfo.getCmNumber());
        fundDetail.setOrderAmt(order.getOrderAmt());
        fundDetail.setFundType(Integer.valueOf(AppConstants.FinancePlan.FUND_TYPE0));
        fundDetail.setOrderId(order.getId());
        fundDetail.setPriority(1L);
        fundDetail.setStatus(Integer.valueOf(AppConstants.FinancePlan.FUND_STATUS_1));
        fundDetail.setYearRate(product.getYearRate().doubleValue());
        fundDetail.setCreateDate(date);
        fundDetail.setDiskNo("");
        fundDetail.setModifyDate(date);
        fundDetail.setMatchingAmt(order.getOrderAmt());
        fundDetailMapper.saveFundDetail(fundDetail);
    }

    private void notifySubject(BusiOrderSub order, BusiProductSub product, CustomerMainInfo customerMainInfo, BusiDebtTransfer transfer) {
        //非转让产品
        if (AppConstants.OrderTransferStatus.ORDER_NORMAL.equals(order.getTransferType())) {
            //优选产品
            if (AppConstants.OrderProductType.COMMON.equals(order.getProductType())) {
                subjectService.signProductAgreement(order, product, customerMainInfo);
            }
            if (AppConstants.OrderProductType.FINANCE_PLAN.equals(order.getProductType())) {
                subjectService.addMatchInvestOrder(order, product, customerMainInfo);
            }
            if (AppConstants.OrderProductType.CONTRACT.equals(order.getProductType())
                    || AppConstants.OrderProductType.PERSONAL.equals(order.getProductType())
                    || AppConstants.OrderProductType.FINANCE_PLAN_SUB.equals(order.getProductType())) {
                subjectService.notifyCommonProductSubject(order, customerMainInfo);
            }
        } else {//转让产品
            TransferVo transferVo = new TransferVo();
            transferVo.setCmIdnum(customerMainInfo.getCmIdnum());
            transferVo.setCustomerName(customerMainInfo.getCmRealName());
            transferVo.setCustomerNo(customerMainInfo.getCmNumber());
            transferVo.setOrderNo(order.getOrderId());
            transferVo.setSubjectNo(order.getSubjectNo());
            transferVo.setCustomerPhone(customerMainInfo.getCmCellphone());
            subjectService.notifyTransferProductSubject(transfer.getTransferNo(), order, product, customerMainInfo, null);
        }
    }

    /*
     * 华瑞-投标
     */
    @Transactional
    public void paySubjectToHR(CustomerMainInfo customerMainInfo, ReqMain reqMain, BusiDebtTransfer transfer, BusiProductSub busiProduct, BusiOrderSub order, String voucherNo, String integralAmt, String couponNo) {
        BorrowTenderDto tenderDto = this.assembleBorrowTenderDto(customerMainInfo, reqMain, busiProduct, order, voucherNo, integralAmt, couponNo, transfer);
        try {
            BorrowTenderResultDto tenderResult = commonBidTender(tenderDto);
            if (tenderResult == null) {
                log.error(">>>>>>>>>>调用华瑞投标失败,订单号:【{}】，产品类型：【{}】<<<<<<<<<<", order.getId(), order.getProductType());
                throw new BusinessException("投标失败");
            }

            Map<String, String> map = Maps.newTreeMap();
            map.put("orderNo", order.getId().toString());
            BusiOrderIntegral orderIntegral = busiOrderIntegralService.selectByCondition(map);
            if (orderIntegral == null) {
                orderIntegral = new BusiOrderIntegral();
            }
            //消费红包流水
            if (StringUtils.isNotBlank(tenderResult.getCouponSerialNo())) {
                BigDecimal couponAmount = tenderResult.getCouponAmount();
                orderIntegral.setCouponId(couponNo);
                orderIntegral.setCouponAmount(couponAmount.toString());
                orderIntegral.setCouponOrderNo(tenderResult.getCouponSerialNo());
                order.setCouponAmount(couponAmount);
            }
            //消费积分消费流水
            if (StringUtils.isNotBlank(tenderResult.getIntegralSerialNo())) {
                orderIntegral.setIntegralProductSerialNo(tenderResult.getIntegralSerialNo());
                orderIntegral.setIntegralOrderNo(tenderResult.getIntegralOrderNo());
                orderIntegral.setIntegralAmount(StringUtils.isEmpty(integralAmt) ? "0" : integralAmt);
                orderIntegral.setIntegralRmbAmount(tenderResult.getIntegralAmount().toString());
                order.setIntegralAmount(tenderResult.getIntegralAmount());
            }
            orderIntegral.setAccountSeriNo(tenderResult.getCashSerialNo());
            orderIntegral.setCashAmount(tenderResult.getCashAmount().toString());
            orderIntegral.setOrderNo(order.getId());
            //更新订单信息
            this.saveFlow(order, orderIntegral);

        } catch (Exception e) {
            log.error(">>>>>>>>>>投标失败,订单号:【{}】<<<<<<<<<<", order.getId(), e);
            throw new BusinessException("投标失败：" + e.getMessage());
        }
    }

    public BorrowTenderDto assembleBorrowTenderDto(CustomerMainInfo customerMainInfo, ReqMain reqMain, BusiProductSub busiProduct, BusiOrderSub order, String voucherNo, String integralAmt, String couponNo, BusiDebtTransfer transfer) {
        BigDecimal cashAmount = gainCashAmount(order, integralAmt, couponNo);
        BorrowTenderDto tenderDto = new BorrowTenderDto();

        tenderDto.setCustomerNo(customerMainInfo.getCmNumber());
        tenderDto.setCustomerName(customerMainInfo.getCmRealName());
        tenderDto.setLendLoginId(customerMainInfo.getFuiouLoginId());

        tenderDto.setOrderNo(order.getOrderId());
        tenderDto.setOrderAmount(order.getOrderAmt());
        tenderDto.setCashAmount(cashAmount);
        tenderDto.setProductId(busiProduct.getId().toString());
        tenderDto.setAccouontType(AccountWholeType.PERSONAL);
        tenderDto.setAccountOprType(AccountOprType.PURCHASE);
        if (AppConstants.OrderTransferStatus.ORDER_TRANSFER.equals(order.getTransferType())) {
            tenderDto.setAccountOprType(AccountOprType.TRANSFEREE);
        }
        tenderDto.setPayMethod(exchangePayMethod(order.getPayType()));
        //标的相关信息
        tenderDto.setLoanCustomerNo(order.getDebtorNum());
        tenderDto.setLoanCustomerName(order.getDebtorName());
        tenderDto.setBidNo(busiProduct.getId().toString());
        tenderDto.setBidAmount(busiProduct.getProductPrincipal());

        tenderDto.setOrderTime(new Date());
        tenderDto.setPrincipalInterest(order.getPrincipalinterest());
        Integer type = order.getProductType();
        ProductType productType = null;
        if (AppConstants.OrderProductType.COMMON.equals(type)) {
            productType = ProductType.COMMON;
        } else if (AppConstants.OrderProductType.FINANCE_PLAN.equals(type)) {
            productType = ProductType.FINANCE_PLAN;
            tenderDto.setPlanProductId(String.valueOf(busiProduct.getId()));//如果是理财计划则设置理财计划ID
        } else if (AppConstants.OrderProductType.FINANCE_PLAN_SUB.equals(type)) {
            productType = ProductType.FINANCE_BID;
            tenderDto.setPlanProductId(String.valueOf(order.getPlanId()));
            tenderDto.setOriginalPlanOrderNo(order.getParentNo());
            tenderDto.setBidNo(order.getSubjectNo());
        } else {
            productType = ProductType.BID;
            tenderDto.setBidNo(busiProduct.getSubjectNo());
        }
        //特殊理财人
        if ("1".equals(order.getHolderType()) && AppConstants.OrderProductType.FINANCE_PLAN_SUB.equals(type)) {
            productType = ProductType.BID;
        }
        tenderDto.setProductType(productType);
        tenderDto.setTransferType(AppConstants.OrderTransferStatus.ORDER_NORMAL.equals(order.getTransferType()) ? TransferType.NO_TRANSFER : TransferType.YES_TRANSFER);
        tenderDto.setTransNo(SerialNoGeneratorService.generateTenderSerialNo(order.getOrderId()));
        tenderDto.setActionRate(order.getActionRate());
        tenderDto.setCloseDays(order.getCloseDays().intValue());

        //封装加息参数
        tenderDto.setRaiseDays(order.getRaiseDays().intValue());
        if (StringUtils.isNotBlank(voucherNo)) {
            BorrowTenderDto.VoucherDto voucherDto = new BorrowTenderDto.VoucherDto();
            voucherDto.setVoucherAmount(order.getRaiseRateIncome());
            voucherDto.setVoucherNo(voucherNo);
            tenderDto.setVoucherDto(voucherDto);
        }
        //封装红包参数
        if (StringUtils.isNotBlank(couponNo)) {
            BorrowTenderDto.CouponDto couponDto = new BorrowTenderDto.CouponDto();
            couponDto.setCouponNo(couponNo);
            couponDto.setCouponOrderNo(order.getOrderId());
            Integer couponAmount = this.gainCouponAmount(couponNo);
            couponDto.setCouponAmount(new BigDecimal(couponAmount));
            tenderDto.setCouponDto(couponDto);
        }
        if (StringUtils.isNotBlank(integralAmt) && new BigDecimal(integralAmt).compareTo(new BigDecimal("0")) == 1) {
            BorrowTenderDto.IntegralDto integralDto = new BorrowTenderDto.IntegralDto();
            integralDto.setProductNo("00001");
            integralDto.setChannelOrderNo(order.getOrderId());
            integralDto.setChannel(AppConstants.APPLICATION_CONTEXT_NAME);
            integralDto.setIntegral(Integer.parseInt(integralAmt));
            integralDto.setPlatform(AppConstants.getAppType(reqMain.getReqHeadParam().getUserAgent()));
            tenderDto.setIntegralDto(integralDto);
        }
        //转让参数
        if ("1".equals(busiProduct.getIsTransfer())) {
            tenderDto.setTransferDto(getTranssferBidTendDto(transfer.getInitOrderNo()));
        }

        return tenderDto;
    }

    //获取上家转让人信息
    public BorrowTenderDto.TransferDto getTranssferBidTendDto(String orderNo) {
        log.info("投标获取上家订单编号:" + orderNo);
        BusiOrder order = busiOrderSubMapper.queryBusiOrderSubInfo(orderNo);
        CustomerMainInfo mainInfo = customerMainInfoService.findOneByCustomerId(order.getCustomerId());

        BorrowTenderDto.TransferDto bidTransfer = new BorrowTenderDto.TransferDto();
        bidTransfer.setCustomerTransferNo(mainInfo.getCmNumber());//上家编号
        bidTransfer.setTransferLoginId(mainInfo.getFuiouLoginId());//上家loginid
        bidTransfer.setCapitalAmount(busiDebtTransferService.calcuteCrediAmount(orderNo));//上家待还本金
        return bidTransfer;
    }

    /**
     * 检查是否可用福利
     *
     * @param product
     * @param ruleVo
     * @param integralAmt
     * @param redId
     * @param voucherId
     */
    private void checkCanUseWelfare(BusiProductSub product, BusiProductRuleVo ruleVo, String integralAmt, String redId, String voucherId, BusiOrderSub order) {
        boolean canUseIntegral = false;
        boolean canUseCoupon = false;
        boolean canUseVoucher = false;
        if (AppConstants.ProductTransferStatus.COMMON_PRODUCT.equals(product.getIsTransfer()) && ruleVo != null && StringUtils.isNotBlank(ruleVo.getWelfare())) {
            String[] types = ruleVo.getWelfare().split(",");
            for (int i = 0; i < types.length; i++) {
                if (types[i].equals(AppConstants.WelfareType.INTEGRAL)) {
                    canUseIntegral = true;
                }
                if (types[i].equals(AppConstants.WelfareType.COUPON)) {
                    canUseCoupon = true;
                }
                if (types[i].equals(AppConstants.WelfareType.VOUCHER)) {
                    canUseVoucher = true;
                }
            }
        }
        int temp = 0;
        //积分，红包，加息券校验
        if (StringUtils.isNotBlank(integralAmt) && new BigDecimal(integralAmt).compareTo(new BigDecimal("0")) == 1) {
            if (!canUseIntegral) {
                throw new BusinessException("该产品不能使用积分！");
            }
            temp++;
        }
        if (StringUtils.isNotBlank(redId)) {
            if (!canUseCoupon) {
                throw new BusinessException("该产品不能使用红包！");
            }
            temp++;
        }
        if (StringUtils.isNotBlank(voucherId)) {
            if (!canUseVoucher) {
                throw new BusinessException("该产品不能使用加息券！");
            }
            temp++;
        }
        if (temp > 1) {
            throw new BusinessException("该产品不能使用多种福利！");
        }
        //检验红包是否符合条件
        if (StringUtils.isNotBlank(redId)) {
            ResultDto<CouponDto> couponDtoResultDto = couponFacadeService.getCouponByNo(redId);
            if (couponDtoResultDto.isSuccess()) {
                CouponDto couponDto = couponDtoResultDto.getData();

                if (order.getOrderAmt().compareTo(new BigDecimal(couponDto.getInvestAmount())) < 0) {
                    throw new BusinessException("该订单金额不符合红包金额使用条件！");
                }
                if (order.getCloseDays().intValue() < couponDto.getInvestPeriod().intValue()) {
                    throw new BusinessException("该订单不能符合红包使用期限！");
                }
                if (!DateUtil.isBetweenTwoDate(couponDto.getStartTime(), couponDto.getEndTime())) {
                    throw new BusinessException("该红包不在有效范围内！");
                }
            }
        }

    }

    /**
     * 保存消费流水更新订单信息
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void saveFlow(BusiOrderSub order, BusiOrderIntegral orderIntegral) {
        if (orderIntegral.getId() == null) {
            int num = busiOrderIntegralService.insert(orderIntegral);
            if (num == 1) {
                log.info(">>>>>>>>>>保存投标流水成功，订单号:{}<<<<<<<<<<", order.getId());
            } else {
                log.info(">>>>>>>>>>保存投标流水失败，订单号:{}<<<<<<<<<<" + order.getId());
                throw new BusinessException("保存投标流水失败！");
            }
        } else {
            int num = busiOrderIntegralService.updateByCondition(orderIntegral);
            if (num == 1) {
                log.info(">>>>>>>>>>更新投标流水成功，订单号:{}<<<<<<<<<<", order.getId());
            } else {
                log.info(">>>>>>>>>>更新投标流水失败，订单号:{}<<<<<<<<<<" + order.getId());
                throw new BusinessException("更新投标流水失败！");
            }
        }
        BusiOrderTemp orderTemp = new BusiOrderTemp();
        orderTemp.setId(order.getId());
        orderTemp.setCouponAmount(StringUtils.isBlank(orderIntegral.getCouponAmount()) ? BigDecimal.ZERO : new BigDecimal(orderIntegral.getCouponAmount()));
        orderTemp.setIntegralAmount(order.getIntegralAmount());
        orderTemp.setCashAmount(new BigDecimal(orderIntegral.getCashAmount()));
        orderTemp.setPaySerNum(orderIntegral.getAccountSeriNo());
        busiOrderTempMapper.updateByPrimaryKeySelective(orderTemp);

        BusiOrderSub orderSub = new BusiOrderSub();
        orderSub.setId(order.getId());
        orderSub.setCouponAmount(StringUtils.isBlank(orderIntegral.getCouponAmount()) ? BigDecimal.ZERO : new BigDecimal(orderIntegral.getCouponAmount()));
        orderSub.setIntegralAmount(order.getIntegralAmount());
        orderSub.setCashAmount(new BigDecimal(orderIntegral.getCashAmount()));
        orderSub.setPaySerNum(orderIntegral.getAccountSeriNo());
        busiOrderSubMapper.updateByPrimaryKeySelective(orderSub);
    }


    private PayMethod exchangePayMethod(String payType) {
        PayMethod payMethod = PayMethod.OTHER;
        if (AppConstants.OrderPaymentType.ONE_TIME.equals(payType)) {
            payMethod = PayMethod.ALL;
        }
        if (AppConstants.OrderPaymentType.DEBX.equals(payType)) {
            payMethod = PayMethod.AVERAGE_CAPITAL_INTEREST;
        }
        if (AppConstants.OrderPaymentType.XXHB.equals(payType)) {
            payMethod = PayMethod.INTEREST_CAPITAL;
        }
        return payMethod;
    }

    private Integer gainCouponAmount(String couponNo) {
        Integer couponAmount = null;
        ResultDto<CouponDto> couponDtoResultDto = couponFacadeService.getCouponByNo(couponNo);
        if (couponDtoResultDto.isSuccess()) {
            CouponDto couponDto = couponDtoResultDto.getData();
            couponAmount = couponDto.getAmount();
        }
        return couponAmount;
    }

    @Override
    public Result findOperationDataStatistics(ReqMain reqMain) throws Exception {
        Map<String, Object> map = new HashMap<>();
        BusiOperationDataStatistics bods = busiTradeFlowMapper.findOperationDataStatistics();
        if (bods != null) {
            bods.setTradeConcentrations(busiTradeFlowMapper.findTradeConcentrationList(bods.getId()));
            List nameList = new ArrayList();
            nameList.add("16-20");
            nameList.add("21-30");
            nameList.add("31-40");
            nameList.add("41-50");
            nameList.add("51-60");
            nameList.add("60以上");
            List valueList = new ArrayList();
            valueList.add(bods.getTwenty());
            valueList.add(bods.getThirty());
            valueList.add(bods.getForty());
            valueList.add(bods.getFifty());
            valueList.add(bods.getSixty());
            valueList.add(bods.getOtherAge());
            map.put("name", nameList);
            map.put("value", valueList);
        }
        map.put("operationDataStatistics", bods);
        return Result.success(map);
    }

    /**
     * 债权转让
     *
     * @param initOrderSub 原出让订单编号
     * @param orderSub     购买订单
     * @param mainInfo     购买用户
     * @param debtAmount   债权价值
     * @param transferNum  一转多的数目
     * @return
     */
    @Transactional
    public BusiDebtTransfer planTransfer(BusiOrderSub initOrderSub, BusiOrderSub orderSub, CustomerMainInfo mainInfo, BigDecimal debtAmount, int transferNum) {
        BusiOrderSub originOrder = getOriginOrder(initOrderSub.getOrderId());
        if (originOrder == null) {
            originOrder = initOrderSub;
        }
        BigDecimal transferFee = BigDecimal.ZERO;
        BusiDebtTransfer debtTransfer = createDebtTransfer(orderSub, initOrderSub, originOrder, transferFee, debtAmount);// 生成转让记录
        Map<String, Object> checkMap = Maps.newHashMap();
        checkMap.put("initOrderNo", initOrderSub.getOrderId());
        long transNum = debtTransferMapper.getTransfersNumByInitOrderNum(checkMap);
        log.info("原始订单号：{} 查询对应转让订单数量：{} ", initOrderSub.getOrderId(), transNum);
        if (transNum == transferNum) {
            Map<String, Object> planMap = new HashMap<>();
            planMap.put("orderNum", initOrderSub.getOrderId());//订单编号
            planMap.put("status", AppConstants.PaymentPlanStatus.RETURNED_TRANSFER);//新状态
            planMap.put("initStatus", AppConstants.PaymentPlanStatus.UNRETURN);//原状态
            paymentPlanService.updateByMap(planMap);
            log.info("撮合下单转让更新上家回款计划成功：{}", JsonUtils.toJson(planMap));

            initOrderSub.setSubOrderStatus(AppConstants.ORDER_SUB_STATUS.ORDER_STATUS_2);
            initOrderSub.setStatus(AppConstants.BusiOrderStatus.BUSIORDER_STATUS_16);
            busiOrderSubMapper.updateByPrimaryKeySelective(initOrderSub);
            log.info("撮合下单转让更新上家订单状态成功：{}", JsonUtils.toJson(initOrderSub));
        }
        debtTransfer.setIsSettle(AppConstants.TransferDebtStatus.TRANSFER_SETTLE_YES);
        debtTransfer.setTransferStatus(AppConstants.DebtTransferStatus.TRANSFER_FINISH);
        debtTransferMapper.updateByPrimaryKeySelective(debtTransfer);


        /**
         * 存管改造前 转让交割：
         *String recordNum = bidTransfer(orderSub, initOrderSub, originOrder, mainInfo, transferFee, debtAmount);
         * 存管改造后 转让交割：
         */
        com.zdmoney.common.ResultDto<TransferResultDTO> resultDto = doTransferCredit(initOrderSub.getOrderId(), orderSub.getOrderId(), transferFee, debtAmount);
        if (resultDto == null || !resultDto.isSuccess() || resultDto.getData() == null) {
            log.error("转让交割调用账户操作失败,返回参数：{}", JSON.toJSONString(resultDto));
            throw new BusinessException("调用账户操作失败");
        }
        debtTransfer.setInFeeSerial(resultDto.getData().getFeeInSerialNo());
        debtTransfer.setOutFeeSerial(resultDto.getData().getFeeOutSerialNo());
        debtTransferMapper.updateByPrimaryKeySelective(debtTransfer);

        try {
            orderSub.setTransferNo(debtTransfer.getTransferNo());
            orderSub.setTransferType(AppConstants.OrderTransferStatus.ORDER_TRANSFER);
            orderSub.setTransferSerialNo(resultDto.getData().getTransferSerialNo());
            busiOrderSubMapper.updateByPrimaryKeySelective(orderSub);
        } catch (Exception ue) {
            log.error("转让成功，订单号" + orderSub.getOrderId() + "更新失败" + ue.getMessage(), ue);
            MailUtil.sendMail("转让成功，订单号" + orderSub.getOrderId() + "更新失败", ue);
        }
        return debtTransfer;
    }

    /**
     * 交割
     *
     * @param orderSub     新购买订单
     * @param initOrderSub 转让人订单
     * @param originOrder  原始转让人订单
     * @param mainInfo
     * @param transferFee
     */
    @Override
    @Transactional
    public String bidTransfer(BusiOrderSub orderSub, BusiOrderSub initOrderSub, BusiOrderSub originOrder, CustomerMainInfo mainInfo, BigDecimal transferFee, BigDecimal debtAmount) {
        BorrowTransferDto transferDto = commonGetTransferDto(orderSub, initOrderSub, originOrder, mainInfo, transferFee, debtAmount);
        log.info("转让参数:" + JsonUtils.toJson(transferDto));
        com.zdmoney.integral.api.common.dto.ResultDto<BorrowTransferResultDto> resultDto = bidFacadeService.bidTransfer(transferDto);
        if (resultDto != null) {
            String initOrderNum = initOrderSub.getOrderId();
            log.info(initOrderNum + ":" + resultDto.getMsg());
            if (resultDto.isSuccess()) {
                List<ResultDto<AccountOprResultDto>> accountResults = resultDto.getData().getAccountResults();
                if (!CollectionUtils.isEmpty(accountResults)) {
                    String recordNum = accountResults.get(0).getData().getRecordNum();
                    log.info(initOrderNum + ":" + recordNum);
                    return recordNum;
                }
            }
        }
        throw new com.zdmoney.exception.BusinessException("转让失败，订单号" + orderSub.getOrderId());
    }

    /**
     * 投标
     *
     * @param tenderVo
     * @param orderSub
     */
    @Transactional
    public String tender(TenderVo tenderVo, BusiOrderSub orderSub, BusiProductSub busiProductSub) {
        String cashSerial = null;
        try {
            BorrowTenderDto tenderDto = commonTenderDto(tenderVo, orderSub);
            BorrowTenderResultDto borrowTenderDto = this.commonBidTender(tenderDto);
            if (borrowTenderDto != null) {
                cashSerial = borrowTenderDto.getCashSerialNo();
            }
        } catch (Exception e) {
            log.error(">>>>>>>>>>调用账户系统投标失败,订单号:【{}】，产品类型：【{}】,错误原因:{}<<<<<<<<<<", orderSub.getOrderId(), orderSub.getProductType(), e);
            throw new BusinessException("投标失败");
        }
        orderSub.setStatus(AppConstants.BusiOrderStatus.BUSIORDER_STATUS_0);
        orderSub.setPaySerNum(cashSerial);
        try {
            busiOrderSubMapper.updateByPrimaryKeySelective(orderSub);
            //新标的，投标成功后再修改原子产品对象缓存
            if (busiProductSub.getId() != null) {
                String randomNumbers = RandomUtil.randomNumbers(15);
                String lockKey = "tenderBusiProductSub" + busiProductSub.getId();
                try {
                    /**
                     * 在多线程环境下，通过分布式锁同步
                     * 从数据库中获取最新产品信息
                     */
                    if (redisSessionManager.tryGetDistributedLock(lockKey, randomNumbers, 1000 * 60 * 30,   10)) {
                        busiProductSub = busiProductSubMapper.selectByPrimaryKey(busiProductSub.getId());
                        busiProductSub.setTotalInvestPerson(busiProductSub.getTotalInvestPerson() + 1);
                        busiProductSub.setTotalInvestAmt(busiProductSub.getTotalInvestAmt().add(orderSub.getOrderAmt()));
                        busiProductService.updateSubProduct(busiProductSub);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                    MailUtil.sendMail(orderSub.getOrderId() + "投标成功后续更新子产品信息,未获取到锁", e);
                } finally {
                    redisSessionManager.releaseDistributedLock(lockKey, randomNumbers);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            MailUtil.sendMail(orderSub.getOrderId() + "投标成功后续更新失败", e);
        }
        return cashSerial;
    }


    /**
     * 退款
     *
     * @param orderSub
     */
    @Transactional
    public void transferRefund(BusiOrderSub orderSub, BusiProductSub busiProductSub) {
        TransferRefundDto accountOprDto = new TransferRefundDto();
        accountOprDto.setTransNo("TK" + orderSub.getOrderId());
        accountOprDto.setBidNo(orderSub.getSubjectNo());
        accountOprDto.setAccountNo(orderSub.getCmNumber());
        accountOprDto.setOrderNo(orderSub.getOrderId());
        accountOprDto.setAmount(orderSub.getOrderAmt());
        accountOprDto.setSerialNo(orderSub.getPaySerNum());
        accountOprDto.setProductType(ProductType.FINANCE_BID);
        accountOprDto.setTransfeeOriginPlanOrderNo(orderSub.getParentNo());
        accountOprDto.setLendLoginId(orderSub.getLoginId());
        String recordNum = "";
        try {
            if (busiProductSub.getId() != null) {
                busiProductSub.setTotalInvestPerson(busiProductSub.getTotalInvestPerson() - 1);
                busiProductSub.setTotalInvestAmt(busiProductSub.getTotalInvestAmt().subtract(orderSub.getOrderAmt()));
                busiProductService.updateSubProduct(busiProductSub);
            }
            log.info("退款参数:" + JsonUtils.toJson(accountOprDto));
            com.zdmoney.integral.api.common.dto.ResultDto<TransferRefundResultDto> refund = accountFacadeService.transferRefund(accountOprDto);
            if (refund != null && refund.isSuccess()) {
                recordNum = refund.getData().getRecordNum();
                log.info("转让失败，订单号" + orderSub.getOrderId() + "退款成功流水号" + recordNum);
                Random random = new Random(System.currentTimeMillis());
                String f = "f" + random.nextInt(10000);
                orderSub.setDebtNo(orderSub.getDebtNo() + f);
                orderSub.setStatus(AppConstants.BusiOrderStatus.BUSIORDER_STATUS_4);
                orderSub.setRefundSerialNo(recordNum);
                busiOrderSubMapper.updateByPrimaryKeySelective(orderSub);
            } else if (refund != null) {
                log.info("转让失败，订单号" + orderSub.getOrderId() + "退款失败" + refund.getMsg());
                MailUtil.sendMail("转让失败，订单号" + orderSub.getOrderId() + "退款失败", refund.getMsg());
            }
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
            if ("".equals(recordNum)) {
                throw new com.zdmoney.exception.BusinessException("转让失败，订单号" + orderSub.getOrderId() + "退款失败");
            } else {
                MailUtil.sendMail(orderSub.getOrderId() + "退款成功后续更新失败", recordNum);
            }
        }
    }


    /**
     * 建产品建标
     *
     * @param productContract
     * @param busiProduct
     */
    @Override
    @Transactional
    public BusiProductSub bidBuildProduct(BusiProductContract productContract, BusiProduct busiProduct) {
        String subjectNo = productContract.getSubjectNo();
        Map<String, Object> subParams = new HashMap<>();
        subParams.put("subjectNo", subjectNo);
        List<BusiProductSub> subs = busiProductService.selectProductSub(subParams);
        if (!CollectionUtils.isEmpty(subs)) {
            return subs.get(0);
        }
        BusiProductSub busiProductSub = new BusiProductSub();

        String repayType = productContract.getRepayType();
        if ("AVERAGE_CAPITAL_INTEREST".equals(repayType)) {
            repayType = AppConstants.FinancePlan.REPAY_TYPE0;
        }
        if ("ONE_CAPTITAL_INTEREST".equals(repayType)) {
            repayType = AppConstants.FinancePlan.REPAY_TYPE1;
        }
        if ("BEFORE_INTEREST_AFTER_CAPTITAL".equals(repayType)) {
            repayType = AppConstants.FinancePlan.REPAY_TYPE2;
        }
        CopyUtil.copyProperties(busiProductSub, busiProduct);
        busiProductSub.setSubjectNo(subjectNo);
        busiProductSub.setTotalInvestAmt(BigDecimal.ZERO);
        busiProductSub.setTotalInvestPerson(0);
        busiProductSub.setProductPrincipal(productContract.getCollectAmount());
        busiProductSub.setSubjectType(AppConstants.ProductSubjectType.FINANCE_PLAN_SUB);
        busiProductSub.setPlanId(busiProduct.getId());
        busiProductSub.setPlanStatus(AppConstants.PRODUCT_PLAN_STATUS.PRODUCT_STATUS_12);
        busiProductSub.setInterestStartDate(productContract.getInterestStartDate());
        busiProductSub.setInterestEndDate(productContract.getInterestEndDate());
        busiProductSub.setYearRate(productContract.getYearRate());
        busiProductSub.setYearRateInit(productContract.getYearRate());
        busiProductSub.setRepayType(repayType);
        busiProductSub.setContractId(productContract.getId());
        busiProductSub.setSaleStartDate(productContract.getSaleStartDate());
        busiProductSub.setSaleEndDate(productContract.getSaleEndDate());
        busiProductSub.setYearRate(productContract.getYearRate());
        SubjectInfoObtainDto obtainDto = new SubjectInfoObtainDto(subjectNo, AppConstants.APPLICATION_CONTEXT_NAME);
        AssetsResultDto<List<ChildSubjectPlanDto>> subjectRepayPlan = lcbSubjectFacadeService.getSubjectRepayPlan(obtainDto);//获取标的回款计划
        if (!subjectRepayPlan.isSuccess()) {
            throw new websvc.exception.BusinessException("获取标的回款计划失败", subjectNo + subjectRepayPlan.getMsg());
        }
        BigDecimal interest = BigDecimal.ZERO;
        for (ChildSubjectPlanDto dto : subjectRepayPlan.getData()) {
            interest = interest.add(dto.getInterest());
        }
        busiProductSub.setProductInterest(interest);
        String itemInfo = "借款人工作收入稳定，借款用于【" + productContract.getBorrowUse() + "】。经审核，该借款人符合捞财宝借款人融资准入标准，准予为其提供线上融资服务并进行还款管理";
        busiProductSub.setItemInfo(itemInfo);
        busiProductSub.setBorrowUse(productContract.getBorrowUse());
        busiProductSub.setRepaymentGuarantee(AppConstants.REPAYMENTGUARANTEE);
        busiProductSub.setRepaySource(AppConstants.REPAYSOURCE);
        busiProductSub.setCooperativeDesc(AppConstants.COOPERATIVEDESC);
        busiProductSub.setLiabilitiesRate(productContract.getLiabilitiesRate());


        log.info("子产品:" + JsonUtils.toJson(busiProductSub));
        busiProductService.insertSubProduct(busiProductSub);
        Map<String, Object> contractMap = new HashMap<>();
        contractMap.put("initBuild", "0");
        contractMap.put("isBuild", "1");
        contractMap.put("subjectNo", subjectNo);
        int num = busiProductContractMapper.updateByMap(contractMap);
        if (num != 1) {
            throw new com.zdmoney.exception.BusinessException(subjectNo + "已经建过标的产品");
        }

        //WACAI（挖财）在标的推标时已向账户发标，故在此不再调用账户发标
        if (!"WACAI".equalsIgnoreCase(busiProduct.getCreditSource()) ){
            com.zdmoney.webservice.api.common.dto.ResultDto  resultDto = bidBuild(productContract, null);
            if (!resultDto.isSuccess()) {
                throw new com.zdmoney.exception.BusinessException("调用账户发标失败");
            }
        }

        return busiProductSub;
    }



    /**
     *
     * 挖财系列产品
     *
     * 建子产品流程
     *
     * @param productContract
     * @param busiProduct
     */
    @Override
    @Transactional
    public BusiProductSub bidBuildProductForWacai(BusiProductContract productContract, BusiProduct busiProduct) {
        String subjectNo = productContract.getSubjectNo();
        Map<String, Object> subParams = new HashMap<>();
        subParams.put("subjectNo", subjectNo);
        List<BusiProductSub> subs = busiProductService.selectProductSub(subParams);
        if (!CollectionUtils.isEmpty(subs)) {
            return subs.get(0);
        }
        BusiProductSub busiProductSub = new BusiProductSub();

        String repayType = productContract.getRepayType();
        if ("AVERAGE_CAPITAL_INTEREST".equals(repayType)) {
            repayType = AppConstants.FinancePlan.REPAY_TYPE0;
        }
        if ("ONE_CAPTITAL_INTEREST".equals(repayType)) {
            repayType = AppConstants.FinancePlan.REPAY_TYPE1;
        }
        if ("BEFORE_INTEREST_AFTER_CAPTITAL".equals(repayType)) {
            repayType = AppConstants.FinancePlan.REPAY_TYPE2;
        }
        CopyUtil.copyProperties(busiProductSub, busiProduct);
        busiProductSub.setSubjectNo(subjectNo);
        busiProductSub.setTotalInvestAmt(BigDecimal.ZERO);
        busiProductSub.setTotalInvestPerson(0);
        busiProductSub.setProductPrincipal(productContract.getCollectAmount());
        busiProductSub.setSubjectType(AppConstants.ProductSubjectType.FINANCE_PLAN_SUB);
        busiProductSub.setPlanId(busiProduct.getId());
        busiProductSub.setPlanStatus(AppConstants.PRODUCT_PLAN_STATUS.PRODUCT_STATUS_12);
        busiProductSub.setInterestStartDate(productContract.getInterestStartDate());
        busiProductSub.setInterestEndDate(productContract.getInterestEndDate());
        busiProductSub.setYearRate(productContract.getYearRate());
        busiProductSub.setYearRateInit(productContract.getYearRate());
        busiProductSub.setRepayType(repayType);
        busiProductSub.setContractId(productContract.getId());
        busiProductSub.setSaleStartDate(productContract.getSaleStartDate());
        busiProductSub.setSaleEndDate(productContract.getSaleEndDate());
        busiProductSub.setYearRate(productContract.getYearRate());

        busiProductSub.setProductInterest(productContract.getProductInterest());
        String itemInfo = "借款人工作收入稳定，借款用于【" + productContract.getBorrowUse() + "】。经审核，该借款人符合捞财宝借款人融资准入标准，准予为其提供线上融资服务并进行还款管理";
        busiProductSub.setItemInfo(itemInfo);
        busiProductSub.setBorrowUse(productContract.getBorrowUse());
        busiProductSub.setRepaymentGuarantee(AppConstants.REPAYMENTGUARANTEE);
        busiProductSub.setRepaySource(AppConstants.REPAYSOURCE);
        busiProductSub.setCooperativeDesc(AppConstants.COOPERATIVEDESC);
        busiProductSub.setLiabilitiesRate(productContract.getLiabilitiesRate());


        log.info("子产品:" + JsonUtils.toJson(busiProductSub));
        busiProductService.insertSubProduct(busiProductSub);
        Map<String, Object> contractMap = new HashMap<>();
        contractMap.put("initBuild", "0");
        contractMap.put("isBuild", "1");
        contractMap.put("subjectNo", subjectNo);
        int num = busiProductContractMapper.updateByMap(contractMap);
        if (num != 1) {
            throw new com.zdmoney.exception.BusinessException(subjectNo + "已经建过标的产品");
        }


        return busiProductSub;
    }




    private boolean bidBuild(BusiProductContract productContract) {
        String subjectNo = productContract.getSubjectNo();
        BidBuildDto buildDto = new BidBuildDto();
        buildDto.setTransNo("FB_" + subjectNo);
        buildDto.setAmount(productContract.getCollectAmount());
        buildDto.setBidNo(subjectNo);
        buildDto.setAnnualRate(productContract.getYearRate());
        buildDto.setBeginDate(productContract.getSaleStartDate());
        buildDto.setEndDate(productContract.getSaleEndDate());
        String repayType = productContract.getRepayType();
        if ("AVERAGE_CAPITAL_INTEREST".equals(repayType)) {
            buildDto.setPayMethod(PayMethod.AVERAGE_CAPITAL_INTEREST);
        } else if ("AVERAGE_CAPITAL".equals(repayType)) {
            buildDto.setPayMethod(PayMethod.AVERAGE_CAPITAL);
        } else if ("ONE_CAPTITAL_INTEREST".equals(repayType)) {
            buildDto.setPayMethod(PayMethod.ALL);
        } else if ("BEFORE_INTEREST_AFTER_CAPTITAL".equals(repayType)) {
            buildDto.setPayMethod(PayMethod.INTEREST_CAPITAL);
        } else {
            buildDto.setPayMethod(PayMethod.OTHER);
        }
        buildDto.setUserName(productContract.getBorrowerName());
        log.info("建标参数:" + JsonUtils.toJson(buildDto));
        com.zdmoney.integral.api.common.dto.ResultDto<BidBuildResultDto> bidBuildResult = bidFacadeService.bidBuild(buildDto);//调用账户发标
        if (bidBuildResult == null || !bidBuildResult.isSuccess()) {
            throw new com.zdmoney.exception.BusinessException("调用账户发标失败");
        }
        return true;
    }


    /**
     * 撮合与垫付调用(理财计划投标)
     *
     * @param tenderVo
     * @param orderSub
     * @return
     */
    public BorrowTenderDto commonTenderDto(TenderVo tenderVo, BusiOrderSub orderSub) {
        BorrowTenderDto tenderDto = new BorrowTenderDto();
        tenderDto.setTransferType(TransferType.NO_TRANSFER);
        tenderDto.setAccountOprType(AccountOprType.PURCHASE);
        if (AppConstants.FinancePlan.DEBT_TYPE2.equals(tenderVo.getDebtType())) {
            tenderDto.setTransferType(TransferType.YES_TRANSFER);
            tenderDto.setAccountOprType(AccountOprType.TRANSFEREE);
            BusiOrder order = busiOrderSubMapper.queryBusiOrderSubInfo(tenderVo.getInitOrderNum());
            CustomerMainInfo mainInfo = customerMainInfoService.findOneByCustomerId(order.getCustomerId());
            BorrowTenderDto.TransferDto bidTransfer = new BorrowTenderDto.TransferDto();
            bidTransfer.setCustomerTransferNo(mainInfo.getCmNumber());//上家编号
            bidTransfer.setTransferLoginId(mainInfo.getFuiouLoginId());//上家loginid
            bidTransfer.setCapitalAmount(tenderVo.getDebtWorth());//上家待还本金
            tenderDto.setTransferDto(bidTransfer);
        }
        tenderDto.setTransNo(SerialNoGeneratorService.generateTenderSerialNo(orderSub.getOrderId()));
        tenderDto.setBidNo(orderSub.getSubjectNo());
        tenderDto.setProductId(orderSub.getProductId() + "");
        Integer type = orderSub.getProductType();
        ProductType productType;
        if (AppConstants.OrderProductType.COMMON.equals(type)) {
            productType = ProductType.COMMON;
        } else if (AppConstants.OrderProductType.FINANCE_PLAN.equals(type)) {
            productType = ProductType.FINANCE_PLAN;
        } else if (AppConstants.OrderProductType.FINANCE_PLAN_SUB.equals(type)) {
            productType = ProductType.FINANCE_BID;
        } else {
            productType = ProductType.BID;
        }
        //特殊理财人
        if ("2".equals(tenderVo.getHolderType())) {
            productType = ProductType.BID;
        }
        tenderDto.setProductType(productType);
        tenderDto.setCloseDays(orderSub.getCloseDays().intValue());
        tenderDto.setBidAmount(tenderVo.getBidAmount());
        tenderDto.setOrderTime(new Date());
        tenderDto.setOrderAmount(orderSub.getOrderAmt());
        tenderDto.setOrderNo(orderSub.getOrderId());
        tenderDto.setCashAmount(orderSub.getCashAmount());
        tenderDto.setCustomerName(tenderVo.getCustomerMainInfo().getCmRealName());
        tenderDto.setCustomerNo(tenderVo.getCustomerMainInfo().getCmNumber());
        tenderDto.setLoanCustomerName(orderSub.getDebtorName());
        tenderDto.setLoanCustomerNo(orderSub.getDebtorNum());
        tenderDto.setAccouontType(AccountWholeType.PERSONAL);
        tenderDto.setPrincipalInterest(orderSub.getPrincipalinterest());
        tenderDto.setPlanProductId(String.valueOf(orderSub.getPlanId()));
        tenderDto.setOriginalPlanOrderNo(orderSub.getParentNo());
        tenderDto.setLendLoginId(tenderVo.getCustomerMainInfo().getFuiouLoginId());

        PayMethod payMethod = PayMethod.OTHER;
        if (AppConstants.OrderPaymentType.ONE_TIME.equals(orderSub.getPayType())) {
            payMethod = PayMethod.ALL;
        }
        if (AppConstants.OrderPaymentType.DEBX.equals(orderSub.getPayType())) {
            payMethod = PayMethod.AVERAGE_CAPITAL_INTEREST;
        }
        if (AppConstants.OrderPaymentType.XXHB.equals(orderSub.getPayType())) {
            payMethod = PayMethod.INTEREST_CAPITAL;
        }
        tenderDto.setPayMethod(payMethod);


        log.info("投标:" + JSONObject.fromObject(tenderDto));
        return tenderDto;
    }

    @Transactional
    public String financePlanSendCommission(String orderNum, String cmNumber, BigDecimal welfareAmt) {
        SysOperateTask operateTask = new SysOperateTask();
        operateTask.setSerialNo(orderNum);
        try {
            sysOperateTaskMapper.insert(operateTask);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            if (e.getMessage().contains("java.sql.SQLException: ORA-00001")) {
                BusiOrderSub orderSub = busiOrderSubMapper.queryBusiOrderSubInfo(orderNum);
                if (orderSub == null) {
                    throw new com.zdmoney.exception.BusinessException("订单" + orderNum + "不存在");
                }
                return orderSub.getSendSerialNo();
            }
        }

        SysParameter sysParameter = sysParameterService.findOneByPrType(AppConstants.ACCOUNT_GSHB);
        String accountGshb = (sysParameter == null) ? "" : sysParameter.getPrValue();
        CustomerMainInfo customerMainInfo = customerMainInfoMapper.selectBycmNumber(cmNumber);

        FinancePlanSendCommissionDto dto = new FinancePlanSendCommissionDto();
        String couponLoginId = CompanyAccounts.getCompanyAccounts().getGshbAccountFuiouId();
        dto.setTransNo(SerialNoGeneratorService.generateSendSerialNo(orderNum));
        dto.setAccountNo(cmNumber);
        dto.setAmount(welfareAmt);
        dto.setChannelOrderNo(orderNum);
        dto.setOriginalPlanOrderNo(orderNum);
        dto.setCouponAccountNo(accountGshb);
        dto.setCouponLoginId(couponLoginId);
        dto.setLendLoginId(customerMainInfo.getFuiouLoginId());

        log.info(JSONObject.fromObject(dto).toString());
        com.zdmoney.integral.api.common.dto.ResultDto<String> resultDto = bidFacadeService.financePlanSendCommission(dto);
        if (resultDto != null && resultDto.isSuccess()) {
            JSONObject json = JSONObject.fromObject(resultDto);
            log.info("订单" + orderNum + "佣金派送:" + json.toString());
            return resultDto.getData();
        } else {
            throw new com.zdmoney.exception.BusinessException("订单" + orderNum + "佣金派送结果不正确");
        }
    }

    @Transactional
    public boolean bidTenderRepeal(BusiOrderSub orderSub, String tenderNo) {
        String orderNo = orderSub.getOrderId();
        TenderRepealDto tenderRepealDto = new TenderRepealDto();
        BusiProductSub busiProductSub = new BusiProductSub();
        busiProductSub.setId(orderSub.getProductId());
        BusiProductSub productSub = busiProductSubMapper.selectOne(busiProductSub);
        if (productSub == null) {
            throw new BusinessException(orderNo + "对应产品信息不存在");
        }
        tenderRepealDto.setBidAmount(productSub.getProductPrincipal());
        tenderRepealDto.setBidBeginDate(productSub.getSaleStartDate());
        tenderRepealDto.setBidEndDate(productSub.getSaleEndDate());
        tenderRepealDto.setBidAnnualRate(productSub.getYearRate());
        tenderRepealDto.setBidNo(productSub.getId() + "");
        CustomerMainInfo mainInfo = customerMainInfoService.findOne(orderSub.getCustomerId());
        if (mainInfo == null) {
            throw new BusinessException(orderNo + "对应用户信息不存在");
        }
        if (AppConstants.ProductSubjectType.FINANCE_PLAN_SUB.equals(orderSub.getProductType() + "")) {
            Random random = new Random(System.currentTimeMillis());
            String f = "f" + random.nextInt(10000);
            orderSub.setDebtNo(orderSub.getDebtNo() + f);
            orderSub.setStatus(AppConstants.BusiOrderStatus.BUSIORDER_STATUS_4);
            busiOrderSubMapper.updateByPrimaryKeySelective(orderSub);
            tenderRepealDto.setBidNo(productSub.getSubjectNo());
        }
        tenderRepealDto.setTransNo("ZB" + tenderNo);
        tenderRepealDto.setTenderNo(tenderNo);
        tenderRepealDto.setCustomerName(mainInfo.getCmRealName());
        tenderRepealDto.setCustomerNo(mainInfo.getCmNumber());
        tenderRepealDto.setIdCardNo(mainInfo.getCmIdnum());
        tenderRepealDto.setOrderAmount(orderSub.getOrderAmt());
        tenderRepealDto.setFeeAmount(BigDecimal.ZERO);
        tenderRepealDto.setLoanCustomerNo(orderSub.getDebtorNum());
        PayMethod payMethod = PayMethod.OTHER;
        if (AppConstants.OrderPaymentType.ONE_TIME.equals(orderSub.getPayType())) {
            payMethod = PayMethod.ALL;
        }
        if (AppConstants.OrderPaymentType.DEBX.equals(orderSub.getPayType())) {
            payMethod = PayMethod.AVERAGE_CAPITAL_INTEREST;
        }
        if (AppConstants.OrderPaymentType.XXHB.equals(orderSub.getPayType())) {
            payMethod = PayMethod.INTEREST_CAPITAL;
        }
        tenderRepealDto.setPayMethod(payMethod);
        log.info("撤标参数:" + JSONObject.fromObject(tenderRepealDto).toString());
        try {
            ResultDto<String> resultDto = null;// bidFacadeService.bidTenderRepeal(tenderRepealDto);
            if (resultDto != null && resultDto.isSuccess()) {
                log.info(orderNo + "撤标结果:" + JSONObject.fromObject(resultDto));
                return true;
            }
        } catch (Exception e) {
            log.error(orderNo + e.getMessage(), e);
            throw new BusinessException(orderNo + "账户撤标不成功");
        }
        throw new BusinessException(orderNo + "账户撤标不成功");
    }

    @Transactional
    public boolean bidTransferRepeal(BusiOrderSub orderSub, String flowNum) {
        String orderNo = orderSub.getOrderId();
        BusiProductSub busiProductSub = new BusiProductSub();
        TenderTransferDto tenderTransferDto = new TenderTransferDto();
        if (orderSub.getProductId() != 0L) {
            busiProductSub.setId(orderSub.getProductId());
            busiProductSub = busiProductSubMapper.selectOne(busiProductSub);
            if (busiProductSub == null) {
                throw new BusinessException(orderNo + "对应产品信息不存在");
            }
            busiProductSub.setTotalInvestPerson(busiProductSub.getTotalInvestPerson() + 1);
            busiProductSub.setTotalInvestAmt(busiProductSub.getTotalInvestAmt().add(orderSub.getOrderAmt()));
            busiProductService.updateSubProduct(busiProductSub);
        }
        Map<String, Object> fundParams = Maps.newHashMap();
        fundParams.put("orderId", orderSub.getParentId());
        fundParams.put("status", AppConstants.FinancePlan.FUND_STATUS_3);
        fundParams.put("initStatus", AppConstants.FinancePlan.FUND_STATUS_2);
        fundParams.put("matchDate", new Date());
        fundDetailMapper.updateByMap(fundParams);
        Map map = new HashMap();
        map.put("subjectNo", orderSub.getSubjectNo());
        List<BusiProductContract> busiProductContracts = busiProductContractMapper.selectBusiProductContract(map);
        if (org.apache.commons.collections.CollectionUtils.isEmpty(busiProductContracts)) {
            throw new BusinessException(orderNo + "对应标的信息不存在");
        }
        BusiProductContract productContract = busiProductContracts.get(0);

        String debtNo = orderSub.getDebtNo();
        if (debtNo.indexOf("f") != -1) {
            orderSub.setDebtNo(debtNo.substring(0, debtNo.indexOf("f")));
        }
        orderSub.setStatus(AppConstants.BusiOrderStatus.BUSIORDER_STATUS_0);
        busiOrderSubMapper.updateByPrimaryKeySelective(orderSub);

        tenderTransferDto.setCustomerNo(orderSub.getCmNumber());
        tenderTransferDto.setProductId(orderSub.getProductId() + "");
        tenderTransferDto.setTransferNo(flowNum);
        tenderTransferDto.setTransNo("XFZR" + orderNo);
        tenderTransferDto.setOriginalPlanOrderNo(orderSub.getParentNo());
        tenderTransferDto.setBidNo(productContract.getSubjectNo());
        tenderTransferDto.setBidAmount(productContract.getCollectAmount());
        tenderTransferDto.setBidAnnualRate(productContract.getYearRate());
        tenderTransferDto.setBidBeginDate(productContract.getSaleStartDate());
        tenderTransferDto.setBidEndDate(productContract.getSaleEndDate());
        tenderTransferDto.setCashAmount(orderSub.getCashAmount());
        tenderTransferDto.setOrderTime(orderSub.getOrderTime());
        tenderTransferDto.setPrincipalInterest(orderSub.getPrincipalinterest());
        tenderTransferDto.setOrderNo(orderNo);
        tenderTransferDto.setPlanProductId(orderSub.getPlanId() + "");
        tenderTransferDto.setLoanCustomerName(orderSub.getDebtorName());
        tenderTransferDto.setLoanCustomerNo(orderSub.getDebtorNum());
        PayMethod payMethod = PayMethod.OTHER;
        if (AppConstants.OrderPaymentType.ONE_TIME.equals(orderSub.getPayType())) {
            payMethod = PayMethod.ALL;
        }
        if (AppConstants.OrderPaymentType.DEBX.equals(orderSub.getPayType())) {
            payMethod = PayMethod.AVERAGE_CAPITAL_INTEREST;
        }
        if (AppConstants.OrderPaymentType.XXHB.equals(orderSub.getPayType())) {
            payMethod = PayMethod.INTEREST_CAPITAL;
        }
        tenderTransferDto.setPayMethod(payMethod);
        Integer type = orderSub.getProductType();
        ProductType productType = null;
        if (AppConstants.OrderProductType.COMMON.equals(type)) {
            productType = ProductType.COMMON;
        } else if (AppConstants.OrderProductType.FINANCE_PLAN.equals(type)) {
            productType = ProductType.FINANCE_PLAN;
        } else if (AppConstants.OrderProductType.FINANCE_PLAN_SUB.equals(type)) {
            productType = ProductType.FINANCE_BID;
        } else {
            productType = ProductType.BID;
        }
        //特殊理财人
        if ("2".equals(orderSub.getHolderType())) {
            productType = ProductType.BID;
        }
        tenderTransferDto.setProductType(productType);
        tenderTransferDto.setTransferType(TransferType.YES_TRANSFER);
        tenderTransferDto.setAccountOprType(AccountOprType.TRANSFEREE);
        log.info("修复转让参数:" + JSONObject.fromObject(tenderTransferDto));
        throw new BusinessException(orderNo + "账户修复转让不成功");
    }

    public String anewBidTransfer(BusiOrderSub orderSub, BusiOrderSub initOrderSub, BusiOrderSub originOrder, CustomerMainInfo mainInfo) {
        BorrowTransferDto transferDto = commonGetTransferDto(orderSub, initOrderSub, originOrder, mainInfo, BigDecimal.ZERO, BigDecimal.ZERO);
        log.info("修复转让参数:" + JsonUtils.toJson(transferDto));
        throw new com.zdmoney.exception.BusinessException("修复转让失败，订单号" + orderSub.getOrderId());
    }


    /**
     * 获取最原始订单信息
     */
    private BusiOrderSub getOriginOrder(String orderId) {
        BusiOrderSub orderTemp = null;
        Example example = new Example(BusiDebtTransfer.class);
        example.createCriteria().andEqualTo("newOrderNo", orderId).andEqualTo("transferStatus", AppConstants.DebtTransferStatus.TRANSFER_FINISH);
        List<BusiDebtTransfer> list = debtTransferMapper.selectByExample(example);
        if (!org.springframework.util.CollectionUtils.isEmpty(list)) {
            BusiDebtTransfer transfer = list.get(0);
            if (StringUtils.isNotBlank(transfer.getOriginOrderNo())) {
                orderTemp = busiOrderSubMapper.queryBusiOrderSubInfo(transfer.getOriginOrderNo());
            }
        }
        return orderTemp;
    }


    /**
     * 生成转让单
     */
    private BusiDebtTransfer createDebtTransfer(BusiOrderSub busiOrderSub, BusiOrderSub initOrderSub, BusiOrderSub originOrder, BigDecimal transferFee, BigDecimal debtAmount) throws com.zdmoney.exception.BusinessException {
        BusiDebtTransfer transfer = new BusiDebtTransfer();
        Date date = new Date();
        transfer.setTransferNo(buildTransferNo(initOrderSub.getCustomerId()));
        transfer.setTransferId(initOrderSub.getCustomerId());
        transfer.setInitOrderNo(initOrderSub.getOrderId());
        transfer.setPubDate(date);
        transfer.setNewOrderNo(busiOrderSub.getOrderId());
        transfer.setTransferStatus(AppConstants.DebtTransferStatus.TRANSFER_SUCCESS);
        transfer.setTransferPrice(busiOrderSub.getOrderAmt());
        transfer.setTransferDate(new Date());
        transfer.setRealTransferDate(transfer.getTransferDate());
        transfer.setTransferCharge(transferFee);
        transfer.setIsSettle(AppConstants.TransferDebtStatus.TRANSFER_SETTLE_NO);
        transfer.setProductId(busiOrderSub.getProductId());
        transfer.setInitProductId(initOrderSub.getProductId());
        transfer.setUpdateDate(date);
        transfer.setProductType(AppConstants.ProductSubjectType.FINANCE_PLAN_SUB);
        transfer.setBuyId(busiOrderSub.getCustomerId());
        //存储最原始的订单号
        transfer.setOriginOrderNo(originOrder.getOrderId());
        transfer.setOriginProductId(originOrder.getProductId());
        //上家用户待收本金
        transfer.setInitPrincipal(debtAmount);
        long leftDay = DateUtil.getBetweenDays(busiOrderSub.getInterestEndDate(), busiOrderSub.getInterestStartDate()) + 1;
        transfer.setLeftDay(Integer.valueOf(leftDay + ""));
        transfer.setTradeDate(date);
        int i = debtTransferMapper.insert(transfer);
        if (i != 1) {
            throw new com.zdmoney.exception.BusinessException("生成转让单失败");
        }
        return transfer;
    }


    /**
     * 生成转让编号
     */
    private String buildTransferNo(Long customerId) {
        String transferNo = "T" + customerId;
        SimpleDateFormat timeStrFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        transferNo += timeStrFormat.format(new Date());
        transferNo += (int) (Math.random() * (100));
        return transferNo;
    }

    public BorrowTransferDto commonGetTransferDto(BusiOrderSub orderSub, BusiOrderSub initOrderSub, BusiOrderSub originOrder, CustomerMainInfo mainInfo, BigDecimal transferFee, BigDecimal debtAmount) {
        String initOrderNum = initOrderSub.getOrderId();
        Map<String, Object> planMap = new HashMap<>();
        planMap.put("orderNum", initOrderNum);//订单编号
        planMap.put("status", AppConstants.PaymentPlanStatus.RETURNED_TRANSFER);//新状态
        planMap.put("initStatus", AppConstants.PaymentPlanStatus.UNRETURN);//原状态
        paymentPlanService.updateByMap(planMap);

        BigDecimal transferPrice = orderSub.getOrderAmt();
        BorrowTransferDto tenderDto = new BorrowTransferDto();
        BorrowTransferDto.BidTransfer bidTransfer = new BorrowTransferDto.BidTransfer();
        tenderDto.setTransNo("ZR" + orderSub.getOrderId());
        bidTransfer.setCustomerNo(orderSub.getCmNumber());
        tenderDto.setFeesLoginId(mainInfo.getFuiouLoginId());

        bidTransfer.setOrderNo(orderSub.getOrderId());
        bidTransfer.setOriginalOrderNo(initOrderSub.getOrderId());
        bidTransfer.setOrderAmount(transferPrice);
        bidTransfer.setCashAmount(transferPrice);
        bidTransfer.setFeeAmount(transferFee);
        bidTransfer.setProductId(initOrderSub.getProductId() + "");
        bidTransfer.setBidNo(originOrder.getSubjectNo());
        bidTransfer.setCustomerTransferNo(initOrderSub.getCmNumber());
        bidTransfer.setCreditAmount(debtAmount);
        bidTransfer.setOriginalPlanOrderNo(initOrderSub.getParentNo());

        List<PaymentPlan> paymentPlans = paymentPlanService.selectPaymentPlans(initOrderNum, null);
        if (org.springframework.util.CollectionUtils.isEmpty(paymentPlans)) {
            throw new com.zdmoney.exception.BusinessException("找不到订单号" + initOrderNum + "对应回款计划");
        }
        BigDecimal principalInterest = BigDecimal.ZERO;
        BigDecimal principal = BigDecimal.ZERO;
        for (PaymentPlan paymentPlan : paymentPlans) {
            if (AppConstants.PaymentPlanStatus.RETURNING.equals(paymentPlan.getRepayStatus())
                    || AppConstants.PaymentPlanStatus.RETURNED.equals(paymentPlan.getRepayStatus())) {
                principalInterest = principalInterest.add(paymentPlan.getPrincipalInterest());
                principal = principal.add(paymentPlan.getPrincipal());
            }
        }
        bidTransfer.setProductType(ProductType.FINANCE_BID);
        List<BorrowTransferDto.AccountDetail> accounts = new ArrayList<>();
        BorrowTransferDto.AccountDetail personIn = new BorrowTransferDto.AccountDetail();
        personIn.setAccountNo(initOrderSub.getCmNumber());
        personIn.setAccountType(AccountWholeType.PERSONAL);
        personIn.setOrderNo(orderSub.getOrderId());
        personIn.setAccountOprType(AccountOprType.TRANSFER);
        personIn.setAmount(transferPrice);
        personIn.setRelAccountNo(orderSub.getCmNumber());
        personIn.setRelAccountType(AccountWholeType.PERSONAL);
        personIn.setAccountOprDirection(AccountOprDirection.IN);
        personIn.setServiceCode(orderSub.getProductId() + "");
        personIn.setServiceName(orderSub.getProductName());
        accounts.add(personIn);
        tenderDto.setAccountDetails(accounts);
        tenderDto.setBidTransfer(bidTransfer);
        return tenderDto;
    }

    /**
     * 收银台接口
     */
    public Result cashierPay(ReqMain reqMain) throws Exception {
        Model_521000 cdtModel = (Model_521000) reqMain.getReqParam();
        Long customerId = cdtModel.getCustomerId();
        Long productId = cdtModel.getProductId();
        String orderId = cdtModel.getOrderId();
        BigDecimal orderAmt = cdtModel.getOrderAmt();
        String integralAmt = cdtModel.getIntegralAmt();
        String redId = cdtModel.getRedId();
        String voucherId = cdtModel.getVoucherId();
        String payPassword = cdtModel.getPayPassword();
        //v4.8 不验密码
        boolean isNeedPassword = false;
        Result res;

        // 下单验签 (v4.9.1验签走编排系统)
//        if (!checkOrderSign(reqMain, configParamBean.getRechargeMd5Key(), cdtModel)) {
//            return Result.fail("下单验签失败");
//        }

        //系统自动下单
        if (!orderId.equals("")) {
            //支付
            res = this.pay(customerId, Long.parseLong(orderId), integralAmt, redId, voucherId, payPassword, isNeedPassword, reqMain);
            if (!res.getSuccess()) {
                return Result.fail(res.getMessage(), res.getData());
            }
        } else {
            //下单
            OrderVo orderVo = new OrderVo();
            orderVo.setCustomerId(customerId);
            orderVo.setProductId(productId);
            orderVo.setOrderAmt(orderAmt);
            orderVo.setReqMain(reqMain);
            BusiOrderSub busiOrder = orderService.order(orderVo);
            if (busiOrder == null) {
                throw new BusinessException("下单失败,用户编号：" + customerId);
            }
            //支付
            res = this.pay(customerId, busiOrder.getId(), integralAmt, redId, voucherId, payPassword, isNeedPassword, reqMain);
            if (!res.getSuccess()) {
                return Result.fail(res.getMessage(), res.getData());
            }
        }
        return Result.success(res.getData());
    }

    /**
     * 收银台初始化接口
     */
    public Result cashierPayInit(ReqMain reqMain) throws Exception {
        Model_521001 cdtModel = (Model_521001) reqMain.getReqParam();
        Long customerId = cdtModel.getCustomerId();
        Long productId = cdtModel.getProductId();
        //校验-用户信息
        CustomerMainInfo customerMainInfo = customerMainInfoService.findOpenCustomerById(customerId);
        if (customerMainInfo == null) {
            throw new BusinessException("该用户信息不存在。");
        }
        BusiProduct busiProduct = busiProductService.findOne(productId);
        if (busiProduct == null) {
            throw new BusinessException("该产品id不存在:" + productId);
        }
        BigDecimal accountBalance = accountOverview520003Service.getAccountBalance(customerMainInfo);
        Map cashierPayInitMap = Maps.newConcurrentMap();
        cashierPayInitMap.put("accountBalance", accountBalance);

        /*协议模板列表*/
        List<AgreementNameDto> agreementTempletes = subjectService.gainPayAgreementsTemplete(busiProduct);
        cashierPayInitMap.put("agreementTempletes", agreementTempletes);

        String useWelfare = "0";
        Long ruleId = busiProduct.getRuleId();
        if (ruleId != null) {
            BusiProductRule busiProductRule = busiProductRuleMapper.selectByPrimaryKey(ruleId);
            String welfare = busiProductRule.getWelfare();
            if (StringUtils.isBlank(welfare)) {
                useWelfare = "1";
            }
        } else {
            useWelfare = "1";
        }
        cashierPayInitMap.put("useWelfare", useWelfare); //是否可用福利  0：可用 1：不可用

        return Result.success(cashierPayInitMap);
    }

    /**
     * @return Boolean    返回类型
     * @throws
     * @Title: check
     * @Description: 验签 (customerId+productId+orderId+orderAmt+integralAmt+redId+voucherId)
     */
    private Boolean checkOrderSign(ReqMain reqMain, String key, Model_521000 cdtModel) {
        String projectNo = reqMain.getProjectNo();//项目代码
        String sn = reqMain.getSn(); //流水号
        String sign = reqMain.getSign();//传送来的验签结果
        //下单加密参数
        String orderSn = cdtModel.getCustomerId() + "&" + cdtModel.getProductId() + "&" + cdtModel.getOrderId() + "&" + cdtModel.getOrderAmt() + "&" + cdtModel.getIntegralAmt() + "&" + cdtModel.getRedId() + "&" + cdtModel.getVoucherId();
        String str = projectNo + "|" + orderSn + "|" + key;//加密key
        String code = MD5.MD5Encode(str);
        log.info(">>>>>>>>传入验签值:" + sign + " >>>>>>>>>本地计算验签:" + code);
        return code.equals(sign);
    }


    public Result reinvestment(Long customerId, Long orderId, String reinvestType) throws Exception {

        HashMap infosMap = new HashMap();
        //校验是否在活动期限内
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (DateTime.now().toDate().compareTo(sdf.parse(configParamBean.getPromotionStartDate())) < 0) {
            infosMap.put("reinvestmentStatus", "1");
            return Result.success("活动还未开始", infosMap);
        }

        if (DateTime.now().toDate().compareTo(sdf.parse(configParamBean.getPromotionEndDate())) > 0) {
            infosMap.put("reinvestmentStatus", "2");
            return Result.success("活动已经结束", infosMap);
        }

        //校验-用户信息
        CustomerMainInfo customerMainInfo = customerMainInfoMapper.selectByCustomerId(customerId);
        if (customerMainInfo == null) {
            throw new BusinessException("未查询到对应的用户信息");
        }


        BusiOrder busiOrder = busiOrderService.selectByPrimaryKey(orderId);
        if (busiOrder == null) {
            throw new BusinessException("未查询到对应的订单信息");
        }

        //判断授权额度是否充足
        Map userAuth = busiOrderService.userAuthJudge(customerId, busiOrder.getProductId(), busiOrder.getPrincipalinterest());
        String grantFlag = userAuth.get("grantFlag").toString();
        if ("1".equals(grantFlag)) {
            throw new BusinessException("授权额度不足、续投失败");
        }


        //校验是否已参加活动
        int busiOrderReinvestLogNum = busiOrderReinvestLogMapper.selectBusiOrderReinvestLogByCustomerId(orderId).size();
        if (busiOrderReinvestLogNum > 0) {
            throw new BusinessException("不可重复续投");
        }

        //购买产品
        BusiProduct busiProduct = busiProductMapper.selectByPrimaryKey(busiOrder.getProductId());

        OrderReinvestConstants orderReinvestType = orderReinvestConstants.getOrderReinvestTypeByType(reinvestType);
        if (orderReinvestType == null) {
            throw new BusinessException("选择的续投方式有误");
        }


        //检验是否签约法大大
        if (customerMainInfo.getSignContract() == null || customerMainInfo.getSignContract().intValue() == 0) {
            infosMap.put("reinvestmentStatus", "3");
            return Result.success("该用户没有签约法大大,签约后才能购买", infosMap);
        }


        //结息日
        Date interestEndDate = DateUtil.plusDay(busiOrder.getInterestEndDate(), orderReinvestType.getDays());
        //续投后封闭天数
        int closeDays = DateUtil.getIntervalDays2(interestEndDate, busiOrder.getInterestStartDate()) + 1;
        //续投每日收益 = 订单本息*续投利率/365
        BigDecimal dayProfit = busiOrder.getPrincipalinterest().multiply(orderReinvestType.getRate()).divide(new BigDecimal("365"), 18, BigDecimal.ROUND_HALF_DOWN);
        String dayProfitTemp = CoreUtil.BigDecimalAccurate(dayProfit);
        dayProfit = new BigDecimal(dayProfitTemp);
        //续投后本息 = 续投收益+在投订单本息
        BigDecimal principalinterest = busiOrderService.calculationInterest(busiOrder.getPrincipalinterest(), orderReinvestType.getRate(), orderReinvestType.getDays(), BigDecimal.ZERO).add(busiOrder.getPrincipalinterest());
        //续投后全程利率
        BigDecimal yearRateNew = principalinterest.subtract(busiOrder.getOrderAmt()).multiply(BigDecimal.valueOf(365))
                .divide(busiOrder.getOrderAmt().multiply(BigDecimal.valueOf(closeDays)), 18, BigDecimal.ROUND_HALF_DOWN);
        String yearRateTemp = CoreUtil.BigDecimalAccurate(yearRateNew, 4);
        yearRateNew = new BigDecimal(yearRateTemp);

        //年利率显示字符串
        String yearRateStr = NumberUtil.fortmatBigDecimalForOne(yearRateNew.multiply(new BigDecimal(100))) + "%";
        //修改时间
        Date modifyDate = new Date();
        String randomNumbers = RandomUtil.randomNumbers(15);
        String lockKey = "reinvestment" + orderId;
        if (redisSessionManager.tryGetDistributedLock(lockKey, randomNumbers, 1000 * 60 * 30, 10)) {
            try {

                //签约续投协议
                ContinueInvestReqDto continueInvestReqDto = new ContinueInvestReqDto();
                continueInvestReqDto.setContinueTerm(orderReinvestType.getDays());
                continueInvestReqDto.setProductName(busiProduct.getProductName());
                continueInvestReqDto.setMobile(customerMainInfo.getCmCellphone());
                continueInvestReqDto.setOrderAmount(busiOrder.getOrderAmt().toString());
                continueInvestReqDto.setOrderNo(busiOrder.getOrderId());
                continueInvestReqDto.setCustomerNo(customerMainInfo.getCmNumber());
                continueInvestReqDto.setCustomerName(customerMainInfo.getCmRealName());
                continueInvestReqDto.setIdNo(customerMainInfo.getCmIdnum());
                continueInvestReqDto.setCurrentTerm(DateUtil.getIntervalDays2(busiOrder.getInterestEndDate(), busiOrder.getInterestStartDate()) + 1);
                continueInvestReqDto.setSumTerm(closeDays);
                continueInvestReqDto.setCurrentRate(busiOrder.getYearRate());
                continueInvestReqDto.setContinueRate(orderReinvestType.getRate());
                continueInvestReqDto.setPartnerNo("LCB");
                continueInvestReqDto.setBusinessNo(busiOrder.getOrderId());
                log.info("签约续投协议对象信息：{}", JSON.toJSONString(continueInvestReqDto));
                signatureFacadeService.signContinueInvest(continueInvestReqDto);
            } catch (Exception e) {
                log.error("签约续投协议异常,{}", e);
                MailUtil.sendMail("签约续投协议失败，订单号：" + busiOrder.getOrderId(), e);
            }

            try {
                //添加续存流水
                BusiOrderReinvestLog busiOrderReinvestLog = new BusiOrderReinvestLog();
                busiOrderReinvestLog.setOrderId(BigDecimal.valueOf(orderId));
                busiOrderReinvestLog.setCurrentEndDate(interestEndDate);
                busiOrderReinvestLog.setCurrentRate(yearRateNew);
                busiOrderReinvestLog.setCustomerId(BigDecimal.valueOf(customerId));
                busiOrderReinvestLog.setSourceEndDate(busiOrder.getInterestEndDate());
                busiOrderReinvestLog.setSourceRate(busiOrder.getYearRate());
                busiOrderReinvestLog.setCreateTime(modifyDate);
                busiOrderReinvestLog.setModifyTime(modifyDate);
                busiOrderReinvestLog.setReinvestType(orderReinvestType.getType());
                busiOrderReinvestLog.setReinvestRate(orderReinvestType.getRate());
                busiOrderReinvestLog.setReinvestDays(orderReinvestType.getDays());
                busiOrderReinvestLog.setReinvestAmt(busiOrder.getPrincipalinterest());
                log.info("续投流水对象信息：{}", JSON.toJSONString(busiOrderReinvestLog));
                busiOrderReinvestLogMapper.insert(busiOrderReinvestLog);
            } catch (Exception e) {
                log.error("添加续存流水异常,{}", e);
                MailUtil.sendMail("添加续存流水失败，订单号：" + busiOrder.getOrderId(), e);
                return Result.fail("续投失败，请联系管理员");
            }

            try {
                //修改子订单信息
                BusiOrderSub busiOrderSub = busiOrderSubMapper.selectByPrimaryKey(orderId);
                log.info("续投前子订单对象信息：{}", JSON.toJSONString(busiOrderSub));
                busiOrderSub.setInterestEndDate(interestEndDate);
                busiOrderSub.setCloseDays(Long.valueOf(closeDays));
                busiOrderSub.setYearRate(yearRateNew);
                busiOrderSub.setPrincipalinterest(principalinterest);
                busiOrderSub.setYearRateStr(yearRateStr);
                busiOrderSub.setDayProfit(dayProfit);
                busiOrderSub.setModifyDate(modifyDate);
                busiOrderSub.setReInvestCount(busiOrderSub.getReInvestCount() + 1);
                log.info("续投后子订单对象信息：{}", JSON.toJSONString(busiOrderSub));
                busiOrderSubMapper.updateByPrimaryKeySelective(busiOrderSub);
            } catch (Exception e) {
                log.error("更新续投子订单信息异常,{}", e);
                MailUtil.sendMail("更新续投子订单信息失败，订单号：" + busiOrder.getOrderId(), e);
                return Result.fail("续投失败，请联系管理员");
            }

            try {
                //修改主订单信息
                log.info("续投前主订单对象信息：{}", JSON.toJSONString(busiOrder));
                busiOrder.setInterestEndDate(interestEndDate);
                busiOrder.setCloseDays(Long.valueOf(closeDays));
                busiOrder.setYearRate(yearRateNew);
                busiOrder.setPrincipalinterest(principalinterest);
                busiOrder.setYearRateStr(yearRateStr);
                busiOrder.setDayProfit(dayProfit);
                busiOrder.setModifyDate(modifyDate);
                busiOrder.setReInvestCount(busiOrder.getReInvestCount() + 1);
                log.info("续投后主订单对象信息：{}", JSON.toJSONString(busiOrder));
                busiOrderService.updateByPrimaryKeySelective(busiOrder);
            } catch (Exception e) {
                log.error("更新续投主订单信息异常,{}", e);
                MailUtil.sendMail("更新续投主订单信息失败，订单号：" + busiOrder.getOrderId(), e);
                return Result.fail("续投失败，请联系管理员");
            }
            infosMap.put("reinvestmentStatus", "0");
            return Result.success("续投成功", infosMap);
        } else {
            throw new BusinessException("正在续投中，请勿重复提交");
        }
    }

    /**
     * 华瑞投标
     *
     * @param tenderDto
     * @return
     */
    public BorrowTenderResultDto commonBidTender(BorrowTenderDto tenderDto) {
        BorrowTenderResultDto borrowResultDto;
        try {
            tenderDto.setTransNo(tenderDto.getOrderNo() + tenderDto.getCustomerNo());
            ResultDto<BorrowTenderResultDto> resultDto = bidFacadeService.bidTender(tenderDto);
            if (resultDto.isSuccess()) {
                log.info(">>>>>>>>>>投标成功,订单号:【{}】，产品类型：【{}】<<<<<<<<<<", tenderDto.getOrderNo(), tenderDto.getProductType());
                borrowResultDto = resultDto.getData();
            } else {
                log.error(">>>>>>>>>>调用账户系统投标失败,订单号:【{}】，产品类型：【{}】,错误原因:{}<<<<<<<<<<", tenderDto.getOrderNo(), tenderDto.getProductType(), resultDto.getMsg());
                throw new BusinessException("投标失败");
            }
            log.info(">>>>>>>>>>投标结束,订单号:【{}】<<<<<<<<<<", tenderDto.getOrderNo());
        } catch (Exception e) {
            log.error(">>>>>>>>>>调用账户系统投标异常,订单号:【{}】<<<<<<<<<<", tenderDto.getOrderNo(), e);
            throw new BusinessException("投标异常：" + e.getMessage());
        }
        return borrowResultDto;
    }


    /**
     * 华瑞投标归集(放款初审)
     * bidTenderCollect重载
     */
    public boolean bidTenderCollect(HRbidTenderCollectDto hRbidTenderCollectDto) {
        return bidTenderCollect(hRbidTenderCollectDto, false);
    }
    /**
     *
     */
    /**
     * 华瑞投标归集(放款初审)
     *
     * @param hRbidTenderCollectDto
     * @param isRetry               是否是重试
     * @return
     */
    public boolean bidTenderCollect(HRbidTenderCollectDto hRbidTenderCollectDto, boolean isRetry) {

        boolean resultBoolean = false;
        log.info(">>>>>>>>>>华瑞投标归集(放款初审)开始，参数信息：{}<<<<<<<<<<", JSON.toJSONString(hRbidTenderCollectDto));
        ResultDto<Boolean> depositTenderCollectResultDto;
        try {

            HashMap infosMap = new HashMap();
            infosMap.put("subjectNo", hRbidTenderCollectDto.getBidNo());
            List statusList = new ArrayList();
            statusList.add("0");//订单状态0付款成功
            infosMap.put("status", statusList);
            infosMap.put("debtorNum", hRbidTenderCollectDto.getPersonAccount());
            infosMap.put("loanStatus", com.zdmoney.constant.Constants.ORDER_LOAN_STATUS_0);
            //若是重试，则仅查询上次失败订单
            if (isRetry) {
                infosMap.put("collectionType", "0");
            }


            //获取该借款人对应的所有出借订单
            List<BusiOrderSub> busiOrderSubList = busiOrderSubMapper.queryOrderSubInfoByBidnoAndStatus(infosMap);
            if (busiOrderSubList.isEmpty()) {
                log.error("华瑞投标归集(放款初审)失败,标的号：{},未查到对应订单信息！", hRbidTenderCollectDto.getBidNo());
                resultBoolean = false;
            } else {

                //如果不是修复，则需要校验初审订单金额和标的金额是否一致
                if (!isRetry) {
                    BusiProductSub busiProductSub = busiProductSubMapper.findProductSubBySubjectNo(hRbidTenderCollectDto.getBidNo());
                    BigDecimal totalOrderAmt = BigDecimal.ZERO;
                    for (BusiOrderSub busiOrderSub : busiOrderSubList){
                        totalOrderAmt = totalOrderAmt.add(busiOrderSub.getOrderAmt());
                    }
                    if (busiProductSub.getTotalInvestAmt().compareTo(totalOrderAmt)!=0){
                        log.error("华瑞投标归集(放款初审)标的号{} 子产品TotalInvestAmt：{} 与对应子订单:{} 不一致 ", hRbidTenderCollectDto.getBidNo(),busiProductSub.getTotalInvestAmt(),totalOrderAmt);
                        return resultBoolean;
                    }
                }



                CustomerMainInfo customerMainInfo = customerMainInfoMapper.selectBycmNumber(hRbidTenderCollectDto.getPersonAccount());
                List<DepositTenderCollectDto.AccountDetail> accountDetails = new ArrayList<DepositTenderCollectDto.AccountDetail>();
                for (BusiOrderSub busiOrderSubItem : busiOrderSubList) {
                    DepositTenderCollectDto.AccountDetail accountDetail = new DepositTenderCollectDto.AccountDetail();
                    accountDetail.setOrderNo(busiOrderSubItem.getOrderId());
                    accountDetail.setOrderAmt(busiOrderSubItem.getOrderAmt());
                    //福利金额
                    BigDecimal welfareAmt = null;
                    if (busiOrderSubItem.getCouponAmount() != null && busiOrderSubItem.getIntegralAmount() != null) {
                        welfareAmt = busiOrderSubItem.getCouponAmount().add(busiOrderSubItem.getIntegralAmount());
                    } else if (busiOrderSubItem.getCouponAmount() != null) {
                        welfareAmt = busiOrderSubItem.getCouponAmount();
                    } else if (busiOrderSubItem.getIntegralAmount() != null) {
                        welfareAmt = busiOrderSubItem.getIntegralAmount();
                    }
                    accountDetail.setWelfareAmt(welfareAmt);
                    accountDetail.setLendLoginId(busiOrderSubItem.getLoginId());

                    accountDetails.add(accountDetail);
                }
                DepositTenderCollectDto depositTenderCollectDto = new DepositTenderCollectDto();
                //获取富友红包户id
                String couponLoginId = CompanyAccounts.getCompanyAccounts().getGshbAccountFuiouId();
                //放款初审流水号;若是重试，则换成重试流水号
                if (isRetry) {
                    depositTenderCollectDto.setTransNo("HRTBRETRY_" + hRbidTenderCollectDto.getBidNo());
                } else {
                    depositTenderCollectDto.setTransNo("HRTB" + hRbidTenderCollectDto.getBidNo());
                }
                depositTenderCollectDto.setLoanLoginId(customerMainInfo.getFuiouLoginId());
                depositTenderCollectDto.setBidNo(hRbidTenderCollectDto.getBidNo());
                depositTenderCollectDto.setCouponLoginId(couponLoginId);
                depositTenderCollectDto.setAccountDetails(accountDetails);
                depositTenderCollectDto.setCallBackUrl(configParamBean.getManagerUrl() + "/busi/matchResult/bidTenderCollectCallback");
                depositTenderCollectResultDto = bidFacadeService.bidTenderCollect(depositTenderCollectDto);

                if ("0000".equals(depositTenderCollectResultDto.getCode())) {
                    //修改订单放款状态 为放款初审中
                    busiOrderSubMapper.updateOrderLoanStatusByIdAndStatus(infosMap);
                    log.info(">>>>>>>>>>华瑞投标归集(放款初审)已发送到账户<<<<<<<<<<");
                    resultBoolean = true;
                } else {
                    log.error("华瑞投标归集(放款初审)失败,标的号：{},返回对象信息：{}", hRbidTenderCollectDto.getBidNo(), JSON.toJSONString(depositTenderCollectResultDto));
                    resultBoolean = false;
                }
            }
        } catch (Exception e) {
            //修改订单放款状态 为放款初审中
            log.error(">>>>>>>>>>华瑞投标归集(放款初审)异常<<<<<<<<<<", e);
            resultBoolean = false;
        }
        return resultBoolean;
    }


    public void bidTenderCollectCallback(CollectOrderInfoDTO record) {
        log.info("华瑞投标归集回调信息：{}", JSON.toJSONString(record));
        try {
            if (record != null && !StringUtil.isEmpty(record.getSubjectNo())) {
                BusiCollectInfo busiCollectInfo = JSON.parseObject(JSON.toJSONString(record), BusiCollectInfo.class);

                Map parms = new HashMap();
                parms.put("type", "0");
                parms.put("subjectNo", busiCollectInfo.getSubjectNo());
                //todo 紧急上线，暂时去掉
//                List<BusiCollectInfo>  busiCollectInfoList = busiCollectInfoMapper.selectBusiCollectInfoByParms(parms);
//                if (busiCollectInfoList != null && busiCollectInfoList.size() >0) {
//                    log.info("标的code：{} 华瑞投标归集重复回调，本次回调信息：{}，上次回调信息：{}",busiCollectInfo.getSubjectNo(),JSON.toJSONString(record),JSON.toJSONString(busiCollectInfoList));
//                }
//                else{
                //保存流水信息
                busiCollectInfoMapper.saveBusiCollectInfo(busiCollectInfo);
                List<CollectOrderDetailDTO> collectOrderDetailDTOS = record.getCollectOrderDetailDTOList();
                if (collectOrderDetailDTOS != null && collectOrderDetailDTOS.size() > 0) {
                    busiCollectFlowMapper.saveBusiCollectFlow(collectOrderDetailDTOS);

                    //更新订单放款状态
                    for (CollectOrderDetailDTO item : collectOrderDetailDTOS) {
                        try {
                            HashMap infosMap = new HashMap();
                            infosMap.put("subjectNo", busiCollectInfo.getSubjectNo());
                            List statusList = new ArrayList();
                            statusList.add("0");//订单状态0付款成功
                            statusList.add("14");//订单状态14分期回款中
                            infosMap.put("status", statusList);
                            infosMap.put("orderNum", item.getOrderNum());
                            if ("0000".equals(item.getCode())) {
                                infosMap.put("loanStatus", com.zdmoney.constant.Constants.ORDER_LOAN_STATUS_1);
                            } else {
                                infosMap.put("loanStatus", com.zdmoney.constant.Constants.ORDER_LOAN_STATUS_2);
                            }
                            busiOrderSubMapper.updateOrderLoanStatusByIdAndStatus(infosMap);

                        } catch (Exception e) {
                            log.error("华瑞投标归集(放款初审)回调更新订单:{}  异常：{}", JSON.toJSONString(item), e);
                        }
                    }
                }
                //回调通知标的系统
                SubjectInvestCollectResultReqDto subjectInvestCollectResultReqDto = new SubjectInvestCollectResultReqDto();
                subjectInvestCollectResultReqDto.setSubjectNo(busiCollectInfo.getSubjectNo());
                subjectInvestCollectResultReqDto.setPartnerNo("LCB");
                subjectInvestCollectResultReqDto.setResultCode(busiCollectInfo.getCode());
                subjectInvestCollectResultReqDto.setOperator("SYS");
                lcbSubjectFacadeService.subjectInvestCollectResult(subjectInvestCollectResultReqDto);
//               }
            }
        } catch (Exception e) {
            log.error(">>>>>>>>>>华瑞投标归集(放款初审)回调异常<<<<<<<<<<", e);
        }
        log.info("华瑞投标归集回调完成：{}", JSON.toJSONString(record));
    }


    /**
     * 放款归集(放款复审)
     *
     * @param loanReviewDTO
     * @return
     */
    public Result timeTenderCollect(LoanReviewDTO loanReviewDTO) {
        return timeTenderCollect(loanReviewDTO, false);
    }

    /**
     * 放款归集(放款复审)
     *
     * @param loanReviewDTO
     * @param isRetry       是否是重试
     * @return
     */
    public Result timeTenderCollect(LoanReviewDTO loanReviewDTO, boolean isRetry) {
        log.info(">>>>>>>>>>放款归集(放款复审)开始，参数信息：{}<<<<<<<<<<", JSON.toJSONString(loanReviewDTO));

        String subjectNo = loanReviewDTO.getBidNo();
        Map<String, Object> params = new HashMap<>();
        params.put("subjectNo", loanReviewDTO.getBidNo());
        List<BusiProductContract> subjectProduct = busiProductContractMapper.selectBusiProductContract(params);
        if (subjectProduct == null || subjectProduct.size() == 0) {
            log.error("不存在标的编号为" + subjectNo + "标的");
            MailUtil.sendMail("放款归集(放款复审)失败", "不存在标的编号为" + subjectNo + "标的");
            return Result.fail("放款归集(放款复审)失败", "不存在标的编号为" + subjectNo + "标的");
        }
        try {
            String callBack = configParamBean.getManagerUrl() + "/busi/matchResult/timeTenderCollectCallback";
            List productTypeList = new ArrayList();
            productTypeList.add("3");//3-个贷产品
            productTypeList.add("5");//5-理财计划子产品
            params.put("productTypeList", productTypeList);
            List statusList = new ArrayList();
            statusList.add("0");//订单状态0付款成功
            params.put("status", statusList);
            //若是重试，则仅查询上次失败订单
            if (isRetry) {
                params.put("collectionType", "1");
            }
            List<BusiOrderSub> orderList = busiOrderSubMapper.queryOrderSubInfoByBidnoAndStatus(params);
            if (orderList.isEmpty()) {
                log.error("放款归集(放款复审)失败,标的号：{},未查到对应订单信息！", loanReviewDTO.getBidNo());
                return Result.fail("放款归集(放款复审)失败,标的号：" + loanReviewDTO.getBidNo() + ",未查到对应订单信息！");
            } else {
                String loanLoginId = busiOrderSubMapper.queryLoanLoginIdByBidNo(loanReviewDTO.getBidNo());
                //若是重试;若是重试，则换成重试流水号
                String transNo = null;
                if (isRetry) {
                    transNo = "FKGJRETRY_" + loanReviewDTO.getBidNo();
                } else {
                    transNo = "FKGJ" + loanReviewDTO.getBidNo();
                }
                Result tenderCollectResult = tenderCollect(orderList, callBack, loanReviewDTO.getBidNo(), loanLoginId, transNo);
                if (tenderCollectResult.getSuccess()) {
                    //修改订单放款状态 为放款复审中
                    params.put("loanStatus", com.zdmoney.constant.Constants.ORDER_LOAN_STATUS_3);
                    busiOrderSubMapper.updateOrderLoanStatusByIdAndStatus(params);
                } else {
                    return tenderCollectResult;
                }

            }
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            MailUtil.sendMail(subjectNo + "放款归集(放款复审)失败", errorMsg);
            return Result.fail("放款归集(放款复审)失败", errorMsg);
        }
        return Result.success("放款归集(放款复审)成功");
    }


    public void timeTenderCollectCallback(CollectOrderInfoDTO record) {
        log.info("放款归集(放款复审)回调信息：{}", JSON.toJSONString(record));
        try {
            if (record != null && !StringUtils.isEmpty(record.getCode())) {
                Map parms = new HashMap();
                parms.put("type", "1");
                parms.put("subjectNo", record.getSubjectNo());
                //todo 紧急上线，暂时去掉
//                List<BusiCollectInfo>  busiCollectInfoList = busiCollectInfoMapper.selectBusiCollectInfoByParms(parms);
//                if (busiCollectInfoList != null && busiCollectInfoList.size() >0) {
//                    log.info("标的code：{} 放款归集(放款复审)重复回调，本次回调信息：{}，上次回调信息：{}",record.getSubjectNo(),JSON.toJSONString(record),JSON.toJSONString(busiCollectInfoList));
//                }
//                else {
                BusiCollectInfo busiCollectInfo = JSON.parseObject(JSON.toJSONString(record), BusiCollectInfo.class);

                //保存流水信息
                busiCollectInfoMapper.saveBusiCollectInfo(busiCollectInfo);
                List<CollectOrderDetailDTO> collectOrderDetailDTOS = record.getCollectOrderDetailDTOList();
                if (collectOrderDetailDTOS != null && collectOrderDetailDTOS.size() > 0) {
                    busiCollectFlowMapper.saveBusiCollectFlow(collectOrderDetailDTOS);

                    //更新订单放款状态
                    for (CollectOrderDetailDTO item : collectOrderDetailDTOS) {

                        try {
                            Map<String, Object> params = new HashMap<>();
                            params.put("subjectNo", busiCollectInfo.getSubjectNo());
                            List productTypeList = new ArrayList();
                            params.put("orderNum", item.getOrderNum());
                            if ("0000".equals(item.getCode())) {
                                params.put("loanStatus", com.zdmoney.constant.Constants.ORDER_LOAN_STATUS_4);
                            } else {
                                params.put("loanStatus", com.zdmoney.constant.Constants.ORDER_LOAN_STATUS_5);
                            }
                            log.info("更新订单放款状态{}", JSON.toJSONString(params));
                            busiOrderSubMapper.updateOrderLoanStatusByIdAndStatus(params);
                        } catch (Exception e) {
                            log.error("放款归集(放款复审)回调更新订单:{}  异常：{}", JSON.toJSONString(item), e);
                        }
                    }
                }
                //回调通知标的系统
                SubjectReAuditResultDto subjectReAuditResultDto = new SubjectReAuditResultDto();
                subjectReAuditResultDto.setCode(busiCollectInfo.getCode());
                subjectReAuditResultDto.setMsg(busiCollectInfo.getMsg());
                subjectReAuditResultDto.setBidNo(busiCollectInfo.getSubjectNo());
                iSubjectFacadeService.loanReAuditResult(subjectReAuditResultDto);

//                }
            }
        } catch (Exception e) {
            log.error(">>>>>>>>>>放款归集(放款复审)回调异常<<<<<<<<<<", e);
        }
        log.info("放款归集(放款复审)回调完成：{}", JSON.toJSONString(record));
    }


    public BusiCollectInfo queryCollectOrderInfo(CollectOrderInfoDTO record) {
        Map parms = new HashMap();
        parms.put("type", record.getType());
        parms.put("subjectNo", record.getSubjectNo());
        List<BusiCollectInfo> busiCollectInfoList = busiCollectInfoMapper.selectBusiCollectInfoByParms(parms);
        log.info("查询放款审查流水参数：{},查询结果：{}", JSON.toJSONString(record), JSON.toJSONString(busiCollectInfoList));
        if (busiCollectInfoList != null && busiCollectInfoList.size() > 0) {
            return busiCollectInfoList.get(0);
        } else {
            return null;
        }
    }

    /**
     * @param subList
     * @param callBack
     * @param bidNo
     * @param loanLoginId
     * @Author: weiNian
     * @Description: 组装账户接口的 放款复审流水参数
     * @Date: 2018/9/27 9:38
     */
    private Result tenderCollect(List<BusiOrderSub> subList, String callBack, String bidNo, String loanLoginId, String transNo) {


        //获取账户红包户id
        String couponLoginId = CompanyAccounts.getCompanyAccounts().getGshbAccount();
        String productId = "";


        Map<String, List<TimeTenderCollectDto.AccountDetail>> busiOrderAccountDetail = new HashMap<String, List<TimeTenderCollectDto.AccountDetail>>();

        for (BusiOrderSub busiOrder : subList) {
            List<TimeTenderCollectDto.AccountDetail> accounts = new ArrayList<>();
            productId = busiOrder.getProductId() + "";
            //仅个贷产品 生成积分、红包流水
            if ("3".equals(busiOrder.getProductType().toString())
                    && (busiOrder.getIntegralAmount().compareTo(BigDecimal.ZERO) > 0 || busiOrder.getCouponAmount().compareTo(BigDecimal.ZERO) > 0)) {
                //组装借款人 进 福利流水（积分、红包）
                TimeTenderCollectDto.AccountDetail accountIn = new TimeTenderCollectDto.AccountDetail();
                accountIn.setAccountNo(AccountWholeType.LOAN_DEDICATED_FRZ + busiOrder.getDebtorNum());
                accountIn.setOrderNo(busiOrder.getOrderId());
                accountIn.setAccountType(AccountWholeType.LOAN_DEDICATED_FRZ);
                accountIn.setAccountOprDirection(AccountOprDirection.IN);
                accountIn.setAmount(busiOrder.getIntegralAmount().add(busiOrder.getCouponAmount()));
                accountIn.setRelAccountNo(couponLoginId);
                accountIn.setRelAccountType(AccountWholeType.COM_COUPON);
                accountIn.setServiceCode(productId);
                accountIn.setServiceName(busiOrder.getProductName());
                BigDecimal integralAmt = busiOrder.getIntegralAmount();
                if (integralAmt.compareTo(BigDecimal.ZERO) > 0) {
                    accountIn.setAccountOprType(AccountOprType.LEAD_INTEGRAL_INCOME);
                } else {
                    accountIn.setAccountOprType(AccountOprType.LEAD_COUPON_INCOME);
                }
                accounts.add(accountIn);

                //组装红包户 出 福利流水（积分、红包）
                TimeTenderCollectDto.AccountDetail accountOut = new TimeTenderCollectDto.AccountDetail();
                accountOut.setAccountNo(accountIn.getRelAccountNo());
                accountOut.setOrderNo(busiOrder.getOrderId());
                accountOut.setAccountType(accountIn.getRelAccountType());
                accountOut.setAccountOprDirection(AccountOprDirection.OUT);
                accountOut.setAmount(accountIn.getAmount());
                accountOut.setRelAccountNo(accountIn.getAccountNo());
                accountOut.setRelAccountType(accountIn.getAccountType());
                accountOut.setServiceCode(productId);
                accountOut.setServiceName(busiOrder.getProductName());
                if (integralAmt.compareTo(BigDecimal.ZERO) > 0) {
                    accountOut.setAccountOprType(AccountOprType.COM_COUPON_DEDUCT_INTEGRAL);
                } else {
                    accountOut.setAccountOprType(AccountOprType.COM_COUPON_DEDUCT_COUPON);
                }
                accounts.add(accountOut);
            }
            //借款人进流水
            TimeTenderCollectDto.AccountDetail accountIn = new TimeTenderCollectDto.AccountDetail();
            accountIn.setAccountNo(AccountWholeType.LOAN_DEDICATED_FRZ + busiOrder.getDebtorNum());
            accountIn.setOrderNo(busiOrder.getOrderId());
            accountIn.setAccountType(AccountWholeType.LOAN_DEDICATED_FRZ);
            accountIn.setAccountOprDirection(AccountOprDirection.IN);
            accountIn.setAccountOprType(AccountOprType.LEAD_CASH_INCOME);
            accountIn.setAmount(busiOrder.getCashAmount());
            accountIn.setRelAccountNo(AccountWholeType.PERSONAL_INVEST_FRZ + busiOrder.getCustomerCmNumber());
            accountIn.setRelAccountType(AccountWholeType.PERSONAL_INVEST_FRZ);
            accountIn.setServiceCode(productId);
            accountIn.setServiceName(busiOrder.getProductName());
            accountIn.setWelfareAmount(busiOrder.getIntegralAmount().add(busiOrder.getCouponAmount()));
            accounts.add(accountIn);

            //出借人出流水
            TimeTenderCollectDto.AccountDetail accountOut = new TimeTenderCollectDto.AccountDetail();
            accountOut.setAccountNo(accountIn.getRelAccountNo());
            accountOut.setOrderNo(accountIn.getOrderNo());
            accountOut.setAccountType(accountIn.getRelAccountType());
            accountOut.setAccountOprDirection(AccountOprDirection.OUT);
            accountOut.setAccountOprType(AccountOprType.PERSONAL_INVEST_FRZ_INVEST_CONFIRM);
            accountOut.setAmount(accountIn.getAmount());
            accountOut.setRelAccountNo(accountIn.getAccountNo());
            accountOut.setRelAccountType(accountIn.getAccountType());
            accountOut.setServiceCode(productId);
            accountOut.setServiceName(busiOrder.getProductName());
            accountOut.setWelfareAmount(accountIn.getWelfareAmount());
            accountOut.setLendLoginId(busiOrder.getLoginId());
            accounts.add(accountOut);

            busiOrderAccountDetail.put(busiOrder.getOrderId(), accounts);
        }
        TimeTenderCollectDto collectDto = new TimeTenderCollectDto();
        collectDto.setTransNo(transNo);
        collectDto.setCallbackUrl(callBack);
        collectDto.setProductId(productId);
        collectDto.setBidNo(bidNo);
        collectDto.setAccountDetails(busiOrderAccountDetail);
        collectDto.setLoanLoginId(loanLoginId);

        log.info("放款归集(放款复审) TimeTenderCollectDto信息:" + JSON.toJSONString(collectDto));
        com.zdmoney.integral.api.common.dto.ResultDto<String> resultDto = accountFacadeService.timeTenderCollect(collectDto);
        if (resultDto == null || !resultDto.isSuccess() || !"0000".equals(resultDto.getCode())) {
            log.error("放款归集(放款复审)失败,标的号：{}，返回对象信息：{}", bidNo, JSON.toJSONString(resultDto));
            return Result.fail("放款归集(放款复审)失败，标的号：" + bidNo + "  异常信息：" + resultDto.getMsg());
        }
        return Result.success("放款归集(放款复审)成功，标的号：" + bidNo);
    }


    //更新用户授权信息,扣除当前订单缴费
    @Transactional
    private void updateUserGrantInfo(Long customerId, Long productId, BigDecimal orderAmt) {
        UserGrantBO userGrant = customerGrantInfoMapper.queryUserGrant(customerId);
        if (userGrant == null) {
            throw new BusinessException("查询不到用户授权信息！");
        }
        //当前缴费所需额度  订单金额*10%
        BigDecimal authRate = new BigDecimal(configParamBean.getAuthRate()); // eg:0.1
        BigDecimal orderFee = orderAmt.multiply(authRate).multiply(new BigDecimal(100));//单位：分
        BigDecimal feeAmt = new BigDecimal(userGrant.getAutoFeeAmt()).subtract(orderFee);//缴费授权额度

        userGrant.setCustomerId(customerId);
        userGrant.setAutoFeeAmt(feeAmt.longValue());

        customerGrantInfoMapper.updateCustomerGrantInfo(userGrant);
    }


    private AccountWholeType determinAccountWholeType(BusiOrderSub order) {
        return isFinanceProduct(order.getProductType().toString()) ? AccountWholeType.PERSONAL_INVEST_FRZ : AccountWholeType.PERSONAL;
    }

    private String determineAccountNo(BusiOrderSub order, CustomerMainInfo customer) {
        return isFinanceProduct(order.getProductType().toString()) ? AccountWholeType.PERSONAL_INVEST_FRZ + customer.getCmNumber() : customer.getCmNumber();
    }

    private String determineServiceCode(BusiOrderSub order) {
        return isYxProduct(order.getProductType().toString()) ? order.getProductId().toString() : order.getSubjectNo();
    }

    private boolean isYxProduct(String productType) {
        return productType.equals(AppConstants.ProductSubjectType.SUBJECT_YX);
    }

    private boolean isFinanceProduct(String productType) {
        return productType.equals(AppConstants.ProductSubjectType.FINANCE_PLAN_SUB) || productType.equals(AppConstants.ProductSubjectType.FINANCE_PLAN);
    }

    @Override
    public com.zdmoney.common.ResultDto<TransferResultDTO> doTransferCredit(Long transferOrderId, Long transfereeOrderId, BigDecimal transferFee, BigDecimal creditAmount) {
        BusiOrderSub transferOrder = busiOrderSubMapper.selectByPrimaryKey(transferOrderId);
        BusiOrderSub transfereeOrder = busiOrderSubMapper.selectByPrimaryKey(transfereeOrderId);
        return reallyDoTransfer(transferOrder, transfereeOrder, transferFee, creditAmount);
    }

    private com.zdmoney.common.ResultDto<TransferResultDTO> reallyDoTransfer(BusiOrderSub transferOrder, BusiOrderSub transfereeOrder, BigDecimal transferFee, BigDecimal creditAmount) {
        CustomerMainInfo transfer = customerMainInfoMapper.selectByCustomerId(transferOrder.getCustomerId());
        CustomerMainInfo transferee = customerMainInfoMapper.selectByCustomerId(transfereeOrder.getCustomerId());

        CompanyAccounts companyAccounts = CompanyAccounts.getCompanyAccounts();
        BorrowTransferDto transferDto = new BorrowTransferDto();
        transferDto.setFeesLoginId(companyAccounts.getJgsyAccountFuiouId());
        BorrowTransferDto.BidTransfer bidTransfer = new BorrowTransferDto.BidTransfer();
        transferDto.setTransNo("ZR" + transfereeOrder.getOrderId());
        bidTransfer.setBidNo(transfereeOrder.getSubjectNo());
        bidTransfer.setOrderNo(transfereeOrder.getOrderId());
        bidTransfer.setOrderAmount(transfereeOrder.getOrderAmt());
        bidTransfer.setCashAmount(transfereeOrder.getCashAmount());
        bidTransfer.setCreditAmount(creditAmount);
        bidTransfer.setFeeAmount(transferFee);
        bidTransfer.setCustomerNo(transferee.getCmNumber());
        bidTransfer.setLoginId(transferee.getFuiouLoginId());

        bidTransfer.setOriginalOrderNo(transferOrder.getOrderId());
        bidTransfer.setOriginalPlanOrderNo(transferOrder.getParentNo());
        bidTransfer.setCustomerTransferNo(transfer.getCmNumber());
        bidTransfer.setTransferLoginId(transfer.getFuiouLoginId());
        ProductType productType = ProductType.BID;
        if (isFinanceProduct(transferOrder.getProductType().toString())) {
            productType = ProductType.FINANCE_BID;
        }
        bidTransfer.setProductType(productType);

        List<BorrowTransferDto.AccountDetail> accounts = new ArrayList<>();

        AccountWholeType transfereeAccountType = determinAccountWholeType(transfereeOrder);
        String transfreeAccountNo = determineAccountNo(transfereeOrder, transferee);

        String serviceCode = determineServiceCode(transferOrder);

        BorrowTransferDto.AccountDetail personIn = new BorrowTransferDto.AccountDetail();
        personIn.setAccountOprType(AccountOprType.TRANSFER);
        personIn.setAccountOprDirection(AccountOprDirection.IN);
        personIn.setAmount(transfereeOrder.getOrderAmt());
        personIn.setAccountNo(transfer.getCmNumber());
        personIn.setAccountType(AccountWholeType.PERSONAL);

        personIn.setOrderNo(transfereeOrder.getOrderId());
        personIn.setRelAccountNo(transfreeAccountNo);
        personIn.setRelAccountType(transfereeAccountType);

        personIn.setServiceCode(serviceCode);
        personIn.setServiceName(serviceCode);
        accounts.add(personIn);
        if (transferFee.compareTo(BigDecimal.ZERO) > 0) {
            BorrowTransferDto.AccountDetail companyIn = new BorrowTransferDto.AccountDetail();
            companyIn.setAccountOprType(AccountOprType.ORG_INCOME_TRANSFER_FEE);
            companyIn.setAccountOprDirection(AccountOprDirection.IN);
            companyIn.setAmount(transferFee);
            companyIn.setAccountNo(companyAccounts.getJgsyAccount());
            companyIn.setAccountType(AccountWholeType.ORG_INCOME);
            companyIn.setOrderNo(transferOrder.getOrderId());

            companyIn.setRelAccountNo(transfer.getCmNumber());
            companyIn.setRelAccountType(AccountWholeType.PERSONAL);

            companyIn.setServiceCode(serviceCode);
            companyIn.setServiceName(serviceCode);
            accounts.add(companyIn);

            BorrowTransferDto.AccountDetail personOut = new BorrowTransferDto.AccountDetail();
            personOut.setAccountNo(transfer.getCmNumber());
            personOut.setAccountOprType(AccountOprType.FEE_OUT);
            personOut.setAccountOprDirection(AccountOprDirection.OUT);

            personOut.setAccountType(AccountWholeType.PERSONAL);
            personOut.setOrderNo(transferOrder.getOrderId());
            personOut.setAmount(transferFee);

            personOut.setRelAccountNo(companyAccounts.getJgsyAccount());
            personOut.setRelAccountType(AccountWholeType.ORG_INCOME);

            personOut.setServiceCode(serviceCode);
            personOut.setServiceName(serviceCode);
            accounts.add(personOut);
        }
        transferDto.setAccountDetails(accounts);
        transferDto.setBidTransfer(bidTransfer);
        log.info("转让参数:"+ JSON.toJSONString(transferDto));
        TransferResultDTO transferResultDTO = null;
        ResultDto<BorrowTransferResultDto> resultDto = bidFacadeService.bidTransfer(transferDto);
        if (resultDto != null && (resultDto.isSuccess() || resultDto.getCode().equals(AppConstants.ACCOUNT_CHARGEING_FEE_ERROR))) {
            transferResultDTO = new TransferResultDTO();
            List<ResultDto<AccountOprResultDto>> accountResults = resultDto.getData().getAccountResults();
            for (ResultDto<AccountOprResultDto> accountResult : accountResults) {
                if (accountResult.isSuccess() && accountResult.getData().getAccountOprType().equals(AccountOprType.TRANSFER)) {
                    transferResultDTO.setTransferSerialNo(accountResult.getData().getTransNo());
                } else if (accountResult.isSuccess() && accountResult.getData().getAccountOprType().equals(AccountOprType.FEE_IN)) {
                    transferResultDTO.setFeeInSerialNo(accountResult.getData().getTransNo());
                } else if (accountResult.isSuccess() && accountResult.getData().getAccountOprType().equals(AccountOprType.FEE_OUT)) {
                    transferResultDTO.setFeeOutSerialNo(accountResult.getData().getTransNo());
                }
            }
            if (resultDto.getCode().equals(AppConstants.ACCOUNT_CHARGEING_FEE_ERROR)) {
                MailUtil.sendMail("交割华瑞扣取服务费异常",
                        String.format("转让人：%s,订单编号：%s,手续费：%.2f", transfer.getCmNumber(), transferOrder.getOrderId(), transferFee));
            }
            return new com.zdmoney.common.ResultDto<>(transferResultDTO);
        }
        com.zdmoney.common.ResultDto<TransferResultDTO> transferResultDTOResultDto =
                new com.zdmoney.common.ResultDto<>(
                        String.format("交割失败,上家订单：%s ,下家订单：%s", transferOrder.getOrderId(), transfereeOrder.getOrderId()), false);
        return transferResultDTOResultDto;
    }

    private com.zdmoney.common.ResultDto<TransferResultDTO> doTransferCredit(String transferOrderNo, String transfereeOrderNo, BigDecimal transferFee, BigDecimal creditAmount) {
        BusiOrderSub transferOrder = busiOrderSubMapper.queryBusiOrderSubInfo(transferOrderNo);
        BusiOrderSub transfereeOrder = busiOrderSubMapper.queryBusiOrderSubInfo(transfereeOrderNo);
        return reallyDoTransfer(transferOrder, transfereeOrder, transferFee, creditAmount);
    }

    private com.zdmoney.common.ResultDto ALREADY_PROCESSED = new com.zdmoney.common.ResultDto();

    @Override
    public void finishCreditTransfer() {
        int successNum = 0;     //成功笔数
        int failNum = 0;        //失败笔数
        Map<String, Object> params = Maps.newTreeMap();
        params.put("isPlan", "no");
        params.put("isSettle", AppConstants.TransferDebtStatus.TRANSFER_SETTLE_NO);
        params.put("realTransferDate", DateUtil.dateToString(new Date()));
        List<BusiDebtTransfer> busiTransferList = busiDebtTransferService.selectDebtTransfer(params);//当日未交割
        StringBuilder errorMsg = new StringBuilder();
        StringBuilder failMsg = new StringBuilder();
        for (BusiDebtTransfer record : busiTransferList) {
            String newOrderNo = record.getNewOrderNo();
            String transferNo = record.getTransferNo();
            if (!StringUtil.isEmpty(newOrderNo) &&
                    AppConstants.DebtTransferStatus.TRANSFER_SUCCESS.equals(record.getTransferStatus())) {
                com.zdmoney.common.ResultDto resultDto = tryTransfer(record);
                if (resultDto.isSuccess()) {
                    successNum++;
                    //转让送现金券
                    welfareService.sendTransferCash(record.getTransferPrice().subtract(record.getTransferCharge()), record.getTransferId());
                }
                if (StringUtils.isNotEmpty(resultDto.getMsg())) {
                    errorMsg.append(resultDto.getMsg()).append("\n");
                }
            } else if (AppConstants.DebtTransferStatus.TRANSFER_INIT.equals(record.getTransferStatus())) {
                try {
                    processTransferFailure(record);
                } catch (Exception e) {
                    log.error("转让不成功处理发生异常:" + transferNo, e);
                    errorMsg.append(transferNo).append(e.getMessage() + "\n");
                }
                failMsg.append(transferNo + ",\n");
                failNum++;
            }
        }
        if (errorMsg.length() > 0) {
            MailUtil.sendMail("债权交割异常", errorMsg.toString());
        }
        MailUtil.sendMail("债权交割完成", String.format("成功执行%d笔，转让不成功%d笔\n%s", successNum, failNum, failMsg.toString()));
    }

    private boolean ensureNoConcurrentProcessing(String operType, String keyword) {
        OperStateRecord operStateRecord = new OperStateRecord();
        operStateRecord.setOperType(operType);
        operStateRecord.setKeyword(keyword);
        try {
            operStateRecordService.save(operStateRecord);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private com.zdmoney.common.ResultDto tryTransfer(BusiDebtTransfer record) {
        if (!ensureNoConcurrentProcessing(BusinessOperation.CREDIT_TRANSFER.getOperType(), record.getTransferNo())) {
            log.warn("transfer record is being processed by multiple threads: " + record.getTransferNo());
            return new com.zdmoney.common.ResultDto(null, false);
        }
        com.zdmoney.common.ResultDto result = new com.zdmoney.common.ResultDto();
        com.zdmoney.common.ResultDto<TransferResultDTO> resultDto = null;
        StringBuilder errorCollector = new StringBuilder();
        try {
            resultDto = doTransfer(record);
        } catch (Exception e) {
            log.error("交割债权失败！编号为:" + record.getTransferNo(), e);
            result.setCode(com.zdmoney.common.ResultDto.ERROR_CODE);
            errorCollector.append(record.getTransferNo()).append(":").append(e.getMessage()).append(";\t");
        }
        if (resultDto == ALREADY_PROCESSED) {
            ;
        } else if (resultDto != null && resultDto.isSuccess()) {
            try {
                processAfterTransfer(resultDto.getData(), record);
            } catch (Exception e) {
                log.error("交割成功后续处理异常：" + e.getMessage(), e);
                errorCollector.append("交割成功后续处理异常:").append(e.getMessage()).append(";\t");
            }
        } else {
            try {
                handleTransferFailed(record);
            } catch (Exception e) {
                log.error("交割失败后续处理异常：" + e.getMessage(), e);
                errorCollector.append("交割失败后续处理异常:").append(e.getMessage()).append(";\t");
            }
        }
        result.setMsg(errorCollector.toString());
        return result;
    }

    private com.zdmoney.common.ResultDto<TransferResultDTO> doTransfer(BusiDebtTransfer record) {
        ensureFuiouAutoPayingFeeAllowed(record.getTransferId(), record.getTransferCharge());
        String initOrderNo = record.getInitOrderNo();
        List<PaymentPlan> paymentPlans = getUnpaidPaymentPlans(initOrderNo);
        if (!ensureNoNeedingPayment(paymentPlans)) {
            log.warn("存在未还款的回款计划：" + initOrderNo);
            throw new BusinessException("存在未还款的回款计划：" + initOrderNo);
        }
        if (!updateTransferRecordStatus(record.getTransferNo())) {
            return ALREADY_PROCESSED;
        }
        BigDecimal creditWorth = calcuteCrediAmount(paymentPlans);

        com.zdmoney.common.ResultDto<TransferResultDTO> resultDto =
                doTransferCredit(record.getInitOrderNo(), record.getNewOrderNo(), record.getTransferCharge(), creditWorth);
        if (resultDto == null || !resultDto.isSuccess()) {
            throw new BusinessException("调用账户操作失败");
        }
        return resultDto;
    }

    private void ensureFuiouAutoPayingFeeAllowed(Long customerId, BigDecimal fee) {
        CustomerMainInfo transfer = customerMainInfoMapper.selectByCustomerId(customerId);
        QueryUserInfoDto queryDto = new QueryUserInfoDto();
        queryDto.setLoginId(transfer.getFuiouLoginId());
        ResultDto<QueryUserInfoResultDto> resultDto = depositFacadeService.queryUserInfo(queryDto);
        QueryUserInfoResultDto data = resultDto.getData();
        EnumMap<AuthType, AuthStatus> authStatus = data.getAuthStatus();
        AuthStatus status = authStatus.get(AuthType.AUTO_FEE);
        if (status.getValue().equals(AuthStatus.UNAUTHORIZED.getValue())) {
            throw new BusinessException(String.format("转让人%d 未授权", customerId.longValue()));
        }
        if (data.getAutoFeeAmt().compareTo(fee) < 0 || data.getAutoFeeTerm().compareTo(new Date()) < 0)
            throw new BusinessException(String.format("转让人授权缴费额度不足：%d,所需额度：%f", customerId.longValue(), fee));
    }

    private List<PaymentPlan> getUnpaidPaymentPlans(String orderNum) {
        List<String> status = Lists.newArrayList();
        status.add(AppConstants.PaymentPlanStatus.UNRETURN);
        List<PaymentPlan> paymentPlans = paymentPlanService.selectPaymentPlans(orderNum, status);
        if (CollectionUtils.isEmpty(paymentPlans)) {
            throw new BusinessException("找不到订单号" + orderNum + "对应回款计划");
        }
        return paymentPlans;
    }

    private boolean ensureNoNeedingPayment(List<PaymentPlan> paymentPlans) {
        Date now = new Date();
        for (PaymentPlan paymentPlan : paymentPlans) {
            if (paymentPlan.getRepayDay().compareTo(now) <= 0) {
                return false;
            }
        }
        return true;
    }

    private boolean updateTransferRecordStatus(String transferNo) {
        log.info("执行---更新债转记录为成功：" + transferNo);
        Map<String, Object> transferMap = Maps.newHashMap();
        transferMap.put("isSettle", AppConstants.TransferDebtStatus.TRANSFER_SETTLE_YES);
        transferMap.put("initIsSettle", AppConstants.TransferDebtStatus.TRANSFER_SETTLE_NO);
        transferMap.put("transferNo", transferNo);
        transferMap.put("updateStatus", AppConstants.DebtTransferStatus.TRANSFER_FINISH);//新状态
        int num = busiDebtTransferService.updateByMap(transferMap);//更新转让记录为交割成功
        if (num != 1) {
            log.warn("转让编号为" + transferNo + "已交割或不存在");
            return false;
        }
        return true;
    }

    private BigDecimal calcuteCrediAmount(List<PaymentPlan> paymentPlans) {
        BigDecimal totalLeftPropical = BigDecimal.ZERO;
        for (PaymentPlan paymentPlan : paymentPlans) {
            totalLeftPropical = totalLeftPropical.add(paymentPlan.getPrincipal());
        }
        return totalLeftPropical;
    }

    private void processAfterTransfer(TransferResultDTO transferResultDTO, BusiDebtTransfer record) {
        storeTransNoToRecord(transferResultDTO, record.getTransferNo());
        updateRelativeData(record);
        notifyBidTransferSuccess(record.getTransferNo(), record.getNewOrderNo());
        notifyMessageSuccess(record);
    }

    @Override
    public void notifyMessageSuccess(BusiDebtTransfer record){
        //转让人受让人信息
        DebtTransferDetail debtTransferDetail = debtTransferMapper.getBuyInfoAndTransferInfo(record.getId());
        //借款人信息
        CustomerMainInfo debtor = customerMainInfoMapper.getCustomerForOrderNo(record.getInitOrderNo());
        if (debtTransferDetail != null && debtor !=null) {
            debtTransferDetail.setBuyName(EncryptUtils.nameEncrypt(debtTransferDetail.getBuyName()));
            debtTransferDetail.setBuyIdNum(EncryptUtils.idNumEncrypt(debtTransferDetail.getBuyIdNum()));
            debtTransferDetail.setTransferName(EncryptUtils.nameEncrypt(debtTransferDetail.getTransferName()));
            debtTransferDetail.setTransferIdNum(EncryptUtils.idNumEncrypt(debtTransferDetail.getTransferIdNum()));
            debtTransferDetail.setDebtorId(debtor.getId().toString());
            debtTransferDetail.setDebtorName(debtor.getCmRealName());
            debtTransferDetail.setDebtorNum(debtor.getCmNumber());
            debtTransferDetail.setDebtorPhone(debtor.getCmCellphone());
            debtTransferDetail.setTransferPrice(record.getTransferPrice().toString());
            debtTransferDetail.setTradeDate(record.getTransferDate());
            busiDebtTransferService.sendMsgMessage(debtTransferDetail);
        } else {
            throw new BusinessException("转让编号为" + record.getTransferNo() + "交割成功时通知Message系统失败");
        }
    }

    private void handleTransferFailed(BusiDebtTransfer record) {
        refundTransferee(record);// -退款
        updateTransferRecordStatus(record.getTransferNo(), null, AppConstants.DebtTransferStatus.TRANSFER_FAILURE);
        removeProductFromShelve(record.getProductId(), "交割失败");
        notifyBidTransferFailed(record.getTransferNo());
    }

    private void storeTransNoToRecord(TransferResultDTO transferResultDTO, String transferNo) {
        Map<String, Object> transferMap = new HashMap<>();
        transferMap.put("transferNo", transferNo);
        if (!StringUtil.isEmpty(transferResultDTO.getTransferSerialNo())) {
            transferMap.put("transferSerial", transferResultDTO.getTransferSerialNo());
        }
        if (!StringUtil.isEmpty(transferResultDTO.getFeeInSerialNo())) {
            transferMap.put("inFeeSerial", transferResultDTO.getFeeInSerialNo());
        }
        if (!StringUtil.isEmpty(transferResultDTO.getFeeOutSerialNo())) {
            transferMap.put("outFeeSerial", transferResultDTO.getFeeOutSerialNo());
        }
        busiDebtTransferService.updateByMap(transferMap);
    }

    private boolean refundTransferee(BusiDebtTransfer record) {
        String newOrderNo = record.getNewOrderNo();
        if (!ensureNoConcurrentProcessing(BusinessOperation.REFUND_TRANSFEREE.getOperType(), newOrderNo)) {
            log.warn("refunding transferee is being processed by multiple threads: " + newOrderNo);
            return false;
        }
        Map<String, Object> orderMap = new HashMap<>();
        orderMap.put("orderNum", newOrderNo);
        List<BusiOrderSub> orders = busiOrderSubMapper.selectByMap(orderMap);
        if (CollectionUtils.isEmpty(orders)) {
            log.warn("订单号：" + newOrderNo + "对应数据不存在");
            return false;
        }
        BusiOrderSub busiOrder = orders.get(0);
        CustomerMainInfo transferee = customerMainInfoMapper.selectByCustomerId(busiOrder.getCustomerId());
        BusiProductSub productSub = busiProductService.getBusiProductSubById(busiOrder.getProductId());
        Map<String, String> serialMap = new HashMap<>();
        serialMap.put("orderNo", busiOrder.getId().toString());
        BusiOrderIntegral busiOrderIntegral = busiOrderIntegralService.selectByCondition(serialMap);

        TransferRefundDto accountOprDto = new TransferRefundDto();
        accountOprDto.setTransNo("TK" + busiOrder.getOrderId());
        accountOprDto.setSerialNo(busiOrderIntegral.getAccountSeriNo());
        accountOprDto.setAccountNo(transferee.getCmNumber());
        accountOprDto.setOrderNo(busiOrder.getOrderId());
        accountOprDto.setAmount(busiOrder.getOrderAmt());
        accountOprDto.setBidNo(productSub.getSubjectNo());
        accountOprDto.setLendLoginId(transferee.getFuiouLoginId());
        if (AppConstants.ProductSubjectType.SUBJECT_YX.equals(busiOrder.getProductType())) {
            accountOprDto.setProductType(ProductType.COMMON);
        } else {
            accountOprDto.setProductType(ProductType.BID);
        }
        try {
            ResultDto<TransferRefundResultDto> refund = accountFacadeService.transferRefund(accountOprDto);
            if (refund != null && refund.isSuccess()) {
                log.info("转让失败，订单号" + newOrderNo + "退款成功" + refund.getData().getRecordNum());
            } else if (refund != null) {
                log.info("转让失败，订单号" + newOrderNo + "退款失败" + refund.getMsg());
                MailUtil.sendMail("转让失败，订单号" + newOrderNo + "退款失败", refund.getMsg());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            MailUtil.sendMail("转让失败，订单号" + newOrderNo + "退款失败", e.getMessage());
        }
        Map<String, Object> map = new HashMap<>();
        map.put("id", busiOrder.getId());
        map.put("status", AppConstants.BusiOrderStatus.BUSIORDER_STATUS_4);
        busiOrderService.updateOrderByIdAndStatus(map);
        return true;
    }

    private void notifyBidTransferSuccess(String transferNo, String newOrderNo) {
        TransferSuccessReqDto reqDto = new TransferSuccessReqDto(AppConstants.PARTNER_NO, transferNo);
        reqDto.addOrderList(newOrderNo);
        AssetsResultDto<TransferSuccessResDto> assetsResultDto = lcbSubjectFacadeService.transferSuccessNotify(reqDto);//通知标的转让成功
        if (assetsResultDto != null && assetsResultDto.isSuccess()) {
            log.info(transferNo + ":" + assetsResultDto.getData() + ":" + assetsResultDto.getCode());
        } else {
            throw new BusinessException("转让编号为" + transferNo + "交割成功时通知标的失败");
        }
    }

    private void notifyBidTransferFailed(String transferNo) {
        TransferFailReqDto reqDto = new TransferFailReqDto(AppConstants.PARTNER_NO, transferNo);
        AssetsResultDto<TransferFailResDto> assetsResultDto = lcbSubjectFacadeService.transferFailNotify(reqDto);
        if (assetsResultDto != null && assetsResultDto.isSuccess()) {
            log.info(transferNo + " : " + assetsResultDto.getData());
        } else {
            throw new BusinessException("转让编号为" + transferNo + "交割失败时通知标的失败");
        }
    }

    private boolean updateRelativeData(BusiDebtTransfer record) {
        String newOrderNo = record.getNewOrderNo();
        String initOrderNo = record.getInitOrderNo();
        Date now = new Date();
        updateTransferPaymenPlan(initOrderNo, now);
        updateTransferOrderStatus(initOrderNo, now);
        updateTransfereeOrderLastestModifiedDate(newOrderNo, now);
        removeProductFromShelve(record.getProductId(), null);
        return true;
    }

    private void updateTransferPaymenPlan(String initOrderNo, Date realTime) {
        log.info("执行---更新上家回款计划为已转让：" + initOrderNo);
        Map<String, Object> planMap = new HashMap<>();
        planMap.put("orderNum", initOrderNo);//订单编号
        planMap.put("status", AppConstants.PaymentPlanStatus.RETURNED_TRANSFER);//新状态
        planMap.put("initStatus", AppConstants.PaymentPlanStatus.UNRETURN);//原状态
        planMap.put("realTime", realTime);
        paymentPlanService.updateByMap(planMap);//更新回款计划为已转让
    }

    private void updateTransferOrderStatus(String initOrderNo, Date modifyDate) {
        log.info("执行---更新上家订单状态为已转让：" + initOrderNo);
        BusiOrder busiOrder = new BusiOrder();
        busiOrder.setOrderId(initOrderNo);
        busiOrder.setModifyDate(modifyDate);
        busiOrder.setStatus(AppConstants.BusiOrderStatus.BUSIORDER_STATUS_16);
        busiOrderService.updateMainAndSubOrderWithOrderNo(busiOrder);//更新订单状态为转让完成
    }

    private void updateTransfereeOrderLastestModifiedDate(String newOrderNo, Date modifyDate) {
        log.info("执行---更新下家订单最近修改时间：" + newOrderNo);
        BusiOrder tmp = new BusiOrder();
        tmp.setOrderId(newOrderNo);
        tmp.setModifyDate(modifyDate);
        busiOrderService.updateMainAndSubOrderWithOrderNo(tmp);//更新下家订单最近修改时间
    }

    private void removeProductFromShelve(Long productId, String remark) {
        log.info("执行---更新转让产品为已下架：" + productId);
        Map<String, Object> updateProductMap = new HashMap<>();
        updateProductMap.put("productId", productId);
        updateProductMap.put("productMemo", remark);
        updateProductMap.put("upLowFlag", AppConstants.ProductUpLowStatus.NOT_ON_SALE);
        busiProductService.updateMainSub(updateProductMap);
    }

    private void updateTransferRecordStatus(String transferNo, String initStatus, String newStatus) {
        Map<String, Object> transferMap = Maps.newTreeMap();
        transferMap.put("initStatus", initStatus);//原状态
        transferMap.put("updateStatus", newStatus);//新状态
        transferMap.put("transferNo", transferNo);
        int i = busiDebtTransferService.updateByMap(transferMap);
        if (i != 1) {
            throw new BusinessException("转让编号为" + transferNo + "更新转让状态失败");
        }
    }

    private void processTransferFailure(BusiDebtTransfer record) {
        String removeProductFromShelveReason = "交割失败";
        removeProductFromShelve(record.getProductId(), removeProductFromShelveReason);
        updateTransferRecordStatus(record.getTransferNo(), AppConstants.DebtTransferStatus.TRANSFER_INIT, AppConstants.DebtTransferStatus.TRANSFER_FAILURE);
        notifyBidTransferFailed(record.getTransferNo());
    }


    /**
     * 公共建标接口
     *
     * @param busiProductContract 标的扩展表
     * @param busiProduct         散标建标传产品，理财计划子标传null
     * @return
     */
    @Override
    public com.zdmoney.webservice.api.common.dto.ResultDto bidBuild(BusiProductContract busiProductContract, BusiProduct busiProduct) {
        com.zdmoney.webservice.api.common.dto.ResultDto resultDto =com.zdmoney.webservice.api.common.dto.ResultDto.FAIL("未知原因");
        BidBuildDto buildDto = new BidBuildDto();
        buildDto.setTransNo("FB_" + busiProductContract.getSubjectNo());
        buildDto.setBidNo(busiProductContract.getSubjectNo());
        buildDto.setBidName(busiProduct == null ? busiProductContract.getSubjectNo() : busiProduct.getProductName());
        buildDto.setBidUsage(BidUsage.NORMAL);
        buildDto.setAmount(busiProductContract.getCollectAmount());
        // 起售日
        buildDto.setBeginDate(busiProductContract.getSaleStartDate());
        // 结息日
        buildDto.setEndDate(busiProductContract.getInterestEndDate());
        // 封闭期(结息日-起息日+1)
        int closeDays = DateUtil.getIntervalDays(busiProductContract.getInterestEndDate(), busiProductContract.getInterestStartDate()) + 1;
        buildDto.setBidDays(closeDays);
        // 筹标日期(结售日-起售日+1)
        int days = DateUtil.getIntervalDays(busiProductContract.getSaleEndDate(), busiProductContract.getSaleStartDate()) + 1;
        buildDto.setRaiseDays(days);
        buildDto.setAnnualRate(busiProductContract.getYearRate());
        buildDto.setNumPeriods(busiProductContract.getRepaymentTerms());
        String repayType = busiProductContract.getRepayType();
        if ("AVERAGE_CAPITAL_INTEREST".equals(repayType)) {
            buildDto.setPayMethod(PayMethod.AVERAGE_CAPITAL_INTEREST);
        } else if ("AVERAGE_CAPITAL".equals(repayType)) {
            buildDto.setPayMethod(PayMethod.AVERAGE_CAPITAL);
        } else if ("ONE_CAPTITAL_INTEREST".equals(repayType)) {
            buildDto.setPayMethod(PayMethod.ALL);
        } else if ("BEFORE_INTEREST_AFTER_CAPTITAL".equals(repayType)) {
            buildDto.setPayMethod(PayMethod.INTEREST_CAPITAL);
        } else {
            buildDto.setPayMethod(PayMethod.OTHER);
        }
        buildDto.setUserName(busiProductContract.getBorrowerName());
        CustomerMainInfo mainInfo = customerMainInfoService.findOneByCmNumber(busiProductContract.getCmNumber());
        if (mainInfo != null) {
            buildDto.setLoginId(mainInfo.getFuiouLoginId());
        }
        buildDto.setBidMemo(busiProduct == null ? "" : busiProduct.getProductDesc());
        log.info("建标参数:" + JsonUtils.toJson(buildDto));
        com.zdmoney.integral.api.common.dto.ResultDto<BidBuildResultDto> bidBuildResult = bidFacadeService.bidBuild(buildDto);
        if (bidBuildResult != null && !bidBuildResult.isSuccess()) {
            log.error("账户建标失败，标的号：{} ，返回消息：{}",buildDto.getBidNo(), bidBuildResult.getMsg());
            resultDto.setCode("1111");
            resultDto.setMsg(bidBuildResult.getMsg());
        }
        if (bidBuildResult != null && bidBuildResult.isSuccess()) {
            resultDto.setCode("0000");
            resultDto.setData(bidBuildResult);
        }
        return resultDto;

    }
}
