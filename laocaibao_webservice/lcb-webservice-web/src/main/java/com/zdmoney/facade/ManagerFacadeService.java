package com.zdmoney.facade;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.RandomUtil;
import com.google.common.collect.Lists;
import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.enm.SerialNoType;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.helper.SystemParameterHelper;
import com.zdmoney.integral.api.dto.cash.CashDto;
import com.zdmoney.integral.api.dto.cash.enm.CashPublishSource;
import com.zdmoney.integral.api.dto.lcbaccount.BankCardBindDto;
import com.zdmoney.integral.api.dto.lcbaccount.BankCardBindResultDto;
import com.zdmoney.integral.api.dto.lcbaccount.enm.AuthStatus;
import com.zdmoney.integral.api.dto.lcbaccount.enm.AuthType;
import com.zdmoney.integral.api.dto.lcbaccount.enm.CustomerType;
import com.zdmoney.integral.api.dto.lcbaccount.query.QueryUserInfoDto;
import com.zdmoney.integral.api.dto.lcbaccount.query.QueryUserInfoResultDto;
import com.zdmoney.integral.api.facade.IBidFacadeService;
import com.zdmoney.integral.api.facade.ICashFacadeService;
import com.zdmoney.integral.api.facade.IDepositFacadeService;
import com.zdmoney.integral.api.utils.DateUtils;
import com.zdmoney.mapper.BusiBrandMapper;
import com.zdmoney.mapper.BusiCashRecordMapper;
import com.zdmoney.mapper.BusiCashTicketConfigMapper;
import com.zdmoney.mapper.bank.CustomerBankAccountMapper;
import com.zdmoney.mapper.financePlan.BusiOrderSubMapper;
import com.zdmoney.mapper.order.BusiOrderMapper;
import com.zdmoney.mapper.payment.PaymentPlanMapper;
import com.zdmoney.mapper.product.BusiProductContractMapper;
import com.zdmoney.mapper.sys.SysIconMapper;
import com.zdmoney.match.api.IMatchApi;
import com.zdmoney.match.dto.MatchApiResult;
import com.zdmoney.match.dto.MatchDetail;
import com.zdmoney.match.dto.MatchPart;
import com.zdmoney.match.dto.ResourceMatchResult;
import com.zdmoney.message.api.common.dto.MessageResultDto;
import com.zdmoney.message.api.dto.sm.SendSmNotifyReqDto;
import com.zdmoney.message.api.facade.ISmFacadeService;
import com.zdmoney.models.BusiCashRecord;
import com.zdmoney.models.BusiCashTicketConfig;
import com.zdmoney.models.CustomerRatingConfig;
import com.zdmoney.models.bank.BusiBankLimit;
import com.zdmoney.models.bank.CustomerBankAccount;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.financePlan.BusiOrderSub;
import com.zdmoney.models.order.BusiOrder;
import com.zdmoney.models.product.BusiProduct;
import com.zdmoney.models.product.BusiProductContract;
import com.zdmoney.models.product.BusiProductSub;
import com.zdmoney.models.sys.SysParameter;
import com.zdmoney.service.*;
import com.zdmoney.service.subject.SubjectService;
import com.zdmoney.session.RedisSessionManager;
import com.zdmoney.utils.ApplicationEventSupport;
import com.zdmoney.utils.JSONUtils;
import com.zdmoney.utils.Page;
import com.zdmoney.webservice.api.common.dto.PageResultDto;
import com.zdmoney.webservice.api.common.dto.PageSearchDto;
import com.zdmoney.webservice.api.common.dto.ResultDto;
import com.zdmoney.webservice.api.dto.Asset.UserBindCardDTO;
import com.zdmoney.webservice.api.dto.busi.BusiBrandDto;
import com.zdmoney.webservice.api.dto.busi.CheckAutoPayingFeeAllowedDto;
import com.zdmoney.webservice.api.dto.busi.CheckIfPurchaseAllowedDto;
import com.zdmoney.webservice.api.dto.customer.CashCouponDto;
import com.zdmoney.webservice.api.dto.customer.CustomerGrantInfoDTO;
import com.zdmoney.webservice.api.dto.customer.CustomerGrantInfoSearchDTO;
import com.zdmoney.webservice.api.dto.customer.CustomerRatingConfigDto;
import com.zdmoney.webservice.api.dto.finance.BusiOrderDto;
import com.zdmoney.webservice.api.dto.finance.QueryOrderReqDto;
import com.zdmoney.webservice.api.dto.sys.SysIconDto;
import com.zdmoney.webservice.api.dto.welfare.BusiWelfareRuleDto;
import com.zdmoney.webservice.api.facade.IBusiBorrowCertificateService;
import com.zdmoney.webservice.api.facade.IManagerFacadeService;
import com.zendaimoney.laocaibao.wx.api.facade.IWechatFacadeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import tk.mybatis.mapper.entity.Example;
import websvc.req.ReqHeadParam;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by 00250968 on 2017/9/5.
 */
@Component
@Slf4j
public class ManagerFacadeService implements IManagerFacadeService {

    /**logger**/
    private static final Logger LOGGER = LoggerFactory.getLogger(ManagerFacadeService.class);

    @Autowired
    private CustomerMainInfoService customerMainInfoService;

    @Autowired
    private RedisSessionManager redisSessionManager;

    @Autowired
    private CustomerBankAccountMapper customerBankAccountMapper;

    @Autowired
    private BusiBankLimitService busiBankLimitService;

    @Autowired
    private BusiProductContractMapper productContractMapper;

    @Autowired
    private BusiProductContractService productContractService;

    @Autowired
    private IBidFacadeService bidFacadeService;

    @Autowired
    private BusiProductService productService;

    @Autowired
    private PaymentPlanMapper paymentPlanMapper;

    @Autowired
    private BusiOrderService busiOrderService;

    @Autowired
    private BusiOrderMapper busiOrderMapper;

    @Autowired
    private BusiOrderSubMapper busiOrderSubMapper;

    @Autowired
    private TradeService tradeService;

    @Autowired
    private BusiCashRecordMapper cashRecordMapper;

    @Autowired
    private ICashFacadeService cashFacadeService;

    @Autowired
    private ConfigParamBean configParamBean;

    @Autowired
    private BusiCashTicketConfigMapper cashTicketConfigMapper;

    @Autowired
    private SysIconMapper sysIconMapper;

    @Autowired
    private IWechatFacadeService wechatFacadeService;

    @Autowired
    private ISmFacadeService iSmFacadeService;

    @Autowired
    private IBusiBorrowCertificateService iBusiBorrowCertificateService;

    @Autowired
    private CustomerRatingConfigService customerRatingConfigService;

    @Autowired
    private BusiBrandMapper busiBrandMapper;


    private static final String PLATFORM = "manager";
    private static final String SYSTEM = "manager";
    private static final String MECHANISM = "证大";
    private static final String VERSION = "1.0.0";
    private static final String CHANNEL = "manager";

    private ReqHeadParam createDefaultReqHeadParam() {
        ReqHeadParam reqHeadParam = new ReqHeadParam();
        reqHeadParam.setUserAgent(PLATFORM);
        reqHeadParam.setVersion(VERSION);
        reqHeadParam.setMechanism(MECHANISM);
        reqHeadParam.setPlatform(PLATFORM);
        reqHeadParam.setTogatherType(PLATFORM);
        reqHeadParam.setOpenchannel(PLATFORM);
        reqHeadParam.setSystem(SYSTEM);
        reqHeadParam.setOpenchannel(CHANNEL);
        return reqHeadParam;
    }

    @Override
    public ResultDto register(String cellPhone) {
        Map map = new HashMap<>();
        try {
            CustomerMainInfo customerMainInfo = customerMainInfoService.findOneByPhone(cellPhone);
            if (customerMainInfo == null) {
                // 手机号后4位作为默认密码
                String password = cellPhone.substring(cellPhone.length() - 4, cellPhone.length());
                ReqHeadParam reqHeadParam = createDefaultReqHeadParam();
                customerMainInfo = customerMainInfoService.register(StringUtils.trim(cellPhone), password, null, null, null, reqHeadParam, null, null, null, null);
                map.put("code", "1");
                map.put("customerId", customerMainInfo.getId());
            } else {
                if (!CHANNEL.equals(customerMainInfo.getCmOpenChannel())) {
                    map.put("code", "2");
                    return new ResultDto("该手机号已注册", map);
                }
                map.put("code", "1");
                map.put("customerId", customerMainInfo.getId());
            }

        } catch (Exception e) {
            return new ResultDto("注册失败:" + e, false);
        }
        return new ResultDto("注册成功", map);
    }

    @Override
    public ResultDto realNameAuth(Long customerId, String idNum, String realName) {
        try {
            CustomerMainInfo customerMainInfo = customerMainInfoService.findOneByCustomerId(customerId);
            if (3 == customerMainInfo.getCmStatus()) {
                if (!idNum.equalsIgnoreCase(customerMainInfo.getCmIdnum())) {
                    return new ResultDto("身份信息不一致", false);
                }
                if (!realName.equals(customerMainInfo.getCmRealName())) {
                    return new ResultDto("姓名不一致", false);
                }
                return new ResultDto("该用户已实名认证成功", true);
            }
            customerMainInfoService.realNameAuth(customerId, realName, idNum, new ReqHeadParam());
        } catch (Exception e) {
            return new ResultDto("实名认证失败:" + e, false);
        }
        return new ResultDto("实名认证成功", true);
    }

    @Override
    public ResultDto bindCard(UserBindCardDTO userBindCardDTO) {
        Long customerId = userBindCardDTO.getCustomerId();
        String bankCode = userBindCardDTO.getBankCode();
        String bankCard = userBindCardDTO.getBankCard();
        String cellphone = userBindCardDTO.getCellphone();

        boolean lock = false;
        String LOCK = "MANAGER-BIND-CARD" + customerId;
        String KEY = "MANAGER_" + customerId;
        try {
            // 获取锁
            lock = redisSessionManager.setNX(KEY, LOCK);
            if (lock) {
                // 1分钟后,key值失效,自动释放锁
                redisSessionManager.expire(KEY, 1, TimeUnit.MINUTES);

                CustomerMainInfo mainInfo = customerMainInfoService.findOneByCustomerId(customerId);
                CustomerBankAccount bankAccount = customerBankAccountMapper.selectBankAccountByBankCode(bankCard);
                if (bankAccount != null) {
                    if (customerId.longValue() != bankAccount.getCustomerId().longValue()) {
                        return new ResultDto("该卡已被他人绑定", false);
                    }
                    return new ResultDto("绑卡成功", true);
                }

                //校验银行卡bin信息
                customerMainInfoService.checkCardBinInfo(bankCode, bankCard);
                //校验银行信息
                BusiBankLimit busiBankLimit = busiBankLimitService.validateBankLimit(AppConstants.PayChannelCodeContants.HUARUI_BANK, bankCode);

                //调用华瑞验卡绑卡接口
                BankCardBindDto bankCardBindDto = new BankCardBindDto();
                bankCardBindDto.setBankId(busiBankLimit.getBankCode());
                bankCardBindDto.setBankCode(busiBankLimit.getBankCode());
                bankCardBindDto.setBankNo(bankCard);
                bankCardBindDto.setBankName(busiBankLimit.getBankName());
                bankCardBindDto.setTelNo(cellphone);
                bankCardBindDto.setIDcard(mainInfo.getCmIdnum());
                bankCardBindDto.setUserName(mainInfo.getCmRealName());
                bankCardBindDto.setUserNo(mainInfo.getCmNumber());
                bankCardBindDto.setTransNo(SerialNoGeneratorService.generateBindSerialNo(mainInfo.getId()));
                bankCardBindDto.setCustomerType(CustomerType.ORGANIZATION);
                com.zdmoney.integral.api.common.dto.ResultDto<BankCardBindResultDto> result = null;//accountFacadeService.bankCardBind(bankCardBindDto);
                if (!result.isSuccess()) {
                    return new ResultDto("绑定银行卡失败:" + result.getMsg(), false);
                } else {
                    Date date = new Date();
                    CustomerBankAccount customerBankAccount = new CustomerBankAccount();
                    customerBankAccount.setCbAccount(bankCard);
                    customerBankAccount.setCbAccountName(mainInfo.getCmRealName());
                    customerBankAccount.setCbBankCode(busiBankLimit.getBankCode());
                    customerBankAccount.setCbBankName(busiBankLimit.getBankName());
                    customerBankAccount.setCustomerId(mainInfo.getId());
                    customerBankAccount.setCbInputDate(date);
                    customerBankAccount.setCbModifyDate(date);
                    customerBankAccount.setCbBindPhone(cellphone);
                    customerBankAccount.setCbValid((short) 0);
                    int num = customerBankAccountMapper.insert(customerBankAccount);
                    if (num != 1) {
                        throw new BusinessException("新增绑卡流水失败");
                    }
                    CustomerMainInfo customerMainInfo = new CustomerMainInfo();
                    customerMainInfo.setId(mainInfo.getId());
                    customerMainInfo.setBankAccountId(String.valueOf(customerBankAccount.getId()));
                    customerMainInfoService.updateNotNull(customerMainInfo);
                }
            }
        } catch (Exception e) {
            return new ResultDto("绑卡失败:" + e, false);
        } finally {
            if (lock) {
                // 如果获取了锁，则释放锁
                redisSessionManager.remove(KEY);
            }
        }
        return new ResultDto("绑卡成功", true);
    }

    @Override
    public ResultDto bidBuild(String subjectNo, Long productId) {
        BusiProductContract obj = new BusiProductContract();
        obj.setSubjectNo(subjectNo);
        List<BusiProductContract> productContractList = productContractMapper.select(obj);
        if (CollectionUtils.isEmpty(productContractList)) {
            return ResultDto.FAIL("不存在该标的");
        }
        BusiProduct product = productService.findOne(productId);
        if (product == null) {
            return ResultDto.FAIL("产品不存在");
        }
        BusiProductContract productContract = productContractList.get(0);
        ResultDto resultDto = tradeService.bidBuild(productContract, product);
        if (!resultDto.isSuccess()) {
            return ResultDto.FAIL("调用账户发标失败");
        }
        return ResultDto.SUCCESS();
    }

    @Override
    public ResultDto<Boolean> checkIfAutoPayingAllowed(CheckAutoPayingFeeAllowedDto checkDto) {
        Map map = busiOrderService.userGrantFlag(checkDto.getCustomerId(), 1, checkDto.getFeeAmount());
        String grantFlag = map.get("grantFlag").toString();
        Boolean enough = Boolean.TRUE;
        if ("1".equals(grantFlag))
            enough = Boolean.FALSE;
        return new ResultDto(enough);
    }

    @Override
    public ResultDto<Boolean> checkIfAuthedPurchaseAllowed(CheckIfPurchaseAllowedDto checkDto) {
        Map userAuth = busiOrderService.userAuthJudge(checkDto.getCustomerId(), checkDto.getProductId(), checkDto.getOrderAmt());
        String grantFlag = userAuth.get("grantFlag").toString();
        Boolean enough = Boolean.TRUE;
        if ("1".equals(grantFlag)) {
            enough = Boolean.FALSE;
        }
        return new ResultDto(enough);
    }

    @Override
    public PageResultDto<CustomerGrantInfoDTO> searchCustomerGrantInfo(CustomerGrantInfoSearchDTO searchDTO) {
        ensurePageParamsValid(searchDTO);
        Page<CustomerGrantInfoDTO> customerGrantInfoDTOS = customerMainInfoService.searchCustomerGrantInfo(searchDTO);
        PageResultDto<CustomerGrantInfoDTO> pageResultDto = wrapPage(customerGrantInfoDTOS);
        return pageResultDto;
    }


    private <T> PageResultDto<T> wrapPage(Page<T> page) {
        PageResultDto<T> pageResultDto = new PageResultDto<>();
        pageResultDto.setTotalSize(page.getTotalRecord());
        pageResultDto.setTotalPage(page.getTotalPage());
        pageResultDto.setDataList(page.getResults());
        return pageResultDto;
    }

    private void ensurePageParamsValid(PageSearchDto searchDto) {
        if (searchDto.getPageNo() <= 0) {
            log.warn("invalid page no:" + searchDto.getPageNo());
            searchDto.setPageNo(1);
        }
        if (searchDto.getPageSize() <= 0) {
            log.warn("invalid page size:" + searchDto.getPageSize());
            searchDto.setPageSize(20);
        }
    }

    @Autowired
    private IDepositFacadeService depositFacadeService;

    @Override
    public ResultDto<Boolean> checkHRAutoPayingAllowed(CheckAutoPayingFeeAllowedDto checkDto) {
        QueryUserInfoDto queryDto = new QueryUserInfoDto();
        if (StringUtils.isNotBlank(checkDto.getFuiouLoginId())) {
            queryDto.setLoginId(checkDto.getFuiouLoginId());
        } else if (checkDto.getCustomerId() != null) {
            CustomerMainInfo mainInfo = customerMainInfoService.findOne(checkDto.getCustomerId());
            queryDto.setLoginId(mainInfo.getFuiouLoginId());
        } else {
            return new ResultDto<>("用户ID和富友ID不能都为空", Boolean.FALSE);
        }
        try {
            com.zdmoney.integral.api.common.dto.ResultDto<QueryUserInfoResultDto> resultDto = depositFacadeService.queryUserInfo(queryDto);
            if (resultDto == null || !resultDto.isSuccess()) {
                String msg = "查询华瑞授权额度失败：" + (resultDto == null ? "调用账户接口失败" : resultDto.getMsg());
                return new ResultDto<>(msg, Boolean.FALSE);
            }
            QueryUserInfoResultDto data = resultDto.getData();
            EnumMap<AuthType, AuthStatus> authStatus = data.getAuthStatus();
            AuthStatus status = authStatus.get(AuthType.AUTO_FEE);
            if (status.getValue().equals(AuthStatus.UNAUTHORIZED.getValue())) {
                return new ResultDto<>("未授权", Boolean.FALSE);
            }
            if (data.getAutoFeeAmt().compareTo(checkDto.getFeeAmount()) > 0 && data.getAutoFeeTerm().compareTo(new Date()) > 0)
                return new ResultDto<>(Boolean.TRUE);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            new ResultDto<>("查询华瑞授权额度发生异常", Boolean.FALSE);
        }
        return new ResultDto<>("授权额度或期限不足", Boolean.FALSE);
    }


    /**
     * 回款发放现金券
     *
     * @param cashCouponDto
     * @return
     */
    @Transactional
    public ResultDto sendRepayCashCoupon(CashCouponDto cashCouponDto) {
        log.info("获取用户回款信息：{}", JSONUtils.toJSON(cashCouponDto));
        BigDecimal relayAmtLimit = new BigDecimal(configParamBean.getRepayAmtLimit());
        String cmNumber = cashCouponDto.getCmNumber();
        CustomerMainInfo mainInfo = customerMainInfoService.findOneByCmNumber(cmNumber);
        if (mainInfo == null) {
            return new ResultDto("当前cmnumber对应的用户不存在: " + cmNumber, Boolean.FALSE);
        }
        if (isSpecialInvestor(cmNumber)){
            return new ResultDto("特殊理财人或者兜底人回款不送现金券 " + cmNumber, Boolean.FALSE);
        }

        if ("1".equals(mainInfo.getUserLabel())){
            return new ResultDto("理财师用户回款不送现金券 " + cmNumber, Boolean.FALSE);
        }

        if (cashCouponDto.getRepayAmt().compareTo(relayAmtLimit) < 0) {
            return new ResultDto("回款金额小于" + relayAmtLimit, Boolean.FALSE);
        }
        List<BusiCashTicketConfig> cashTicketConfigList = cashTicketConfigMapper.queryBusiCashTickets(AppConstants.CASH_TYPE.CASH_REPAY_AMT);
        if (CollectionUtils.isEmpty(cashTicketConfigList)) {
            log.info("回款送现金券未配置，请检查");
            return new ResultDto("回款送现金券未配置，发放失败", Boolean.FALSE);
        }
        List<CashDto> cashDtos = new ArrayList<>();
        for (BusiCashTicketConfig cashTicketConfig : cashTicketConfigList) {
            CashDto cashDto = new CashDto();
            cashDto.setAccountNo(cmNumber);
            cashDto.setPublishSource(CashPublishSource.PAY_BACK);
            cashDto.setAmount(new BigDecimal(cashTicketConfig.getAmount()));
            cashDto.setInvestMin(new BigDecimal(cashTicketConfig.getInvestMinAmount()));
            cashDto.setPeriod(cashTicketConfig.getPeriod().intValue());
            cashDto.setInvestPeriodMin(cashTicketConfig.getInvestMinPeriod().intValue());
            if(cashTicketConfig.getInvestMaxPeriod()!=null){
                cashDto.setInvestPeriodMax(cashTicketConfig.getInvestMaxPeriod().intValue());
            }
            cashDtos.add(cashDto);
        }

        String lockKey = "repayCash_" + cmNumber;
        String randomNumbers = RandomUtil.randomNumbers(15);
        try {
            if (redisSessionManager.tryGetDistributedLock(lockKey, randomNumbers, 1000 * 60 * 30, 5)) {
                BusiCashRecord cashRecord = cashRecordMapper.queryCashRecordByRepayDate(cmNumber, cashCouponDto.getRepayDate());
                if (cashRecord != null) {
                    log.info("当前回款周期内已发放过回款送现金券：" + JSONUtils.toJSON(cashRecord));
                    return new ResultDto("当前回款周期内已发放过回款送现金券", Boolean.FALSE);
                }

                BusiCashRecord record = new BusiCashRecord();
                record.setCashCouponType(AppConstants.CASH_TYPE.CASH_REPAY_AMT);
                record.setCmNumber(cmNumber);
                record.setCreateDate(new Date());
                record.setModifyDate(new Date());
                record.setStatus("1");
                record.setCashCouponId("0");
                Date expireDate = DateUtils.plusDays(cashCouponDto.getRepayDate(), 5);
                record.setExpireDate(expireDate);
                cashRecordMapper.saveBusiCashRecord(record);

                Boolean cashFlag = repayCash(cashDtos, cmNumber);
                if (!cashFlag) {
                    throw new BusinessException("调用积分回款送现金券失败");
                }
                //回款发送消息通知
                pushSms(mainInfo);
            }else{
                return new ResultDto("回款发放现金券中，请稍后查收", Boolean.FALSE);
            }

        } catch (Exception e) {
            log.error("回款送现金券,未获取到锁，" + e.getMessage(), e);
            throw new BusinessException("回款送现金券失败");
        } finally {
            redisSessionManager.releaseDistributedLock(lockKey, randomNumbers);
        }
        return new ResultDto("回款送现金券成功", Boolean.TRUE);
    }

    @Override
    public ResultDto<List<SysIconDto>> querySysIcons() {
        return new ResultDto(sysIconMapper.querySysIcon());
    }

    @Override
    public ResultDto updateSysIcon(SysIconDto sysIconDto) {
        return new ResultDto(sysIconMapper.updateSysIcon(sysIconDto));
    }

    @Override
    public ResultDto deleteIcon() {
        return new ResultDto(sysIconMapper.deleteIcon());
    }

    @Override
    public ResultDto getHoldAsset(String customerId) {
        return iBusiBorrowCertificateService.getHoldAsset(customerId);
    }

    @Override
    public ResultDto<List<CustomerRatingConfigDto>> getCustomerRatingConfigList() {
        List<CustomerRatingConfig> customerRatingConfigList = customerRatingConfigService.findAllNoCache();
        if (customerRatingConfigList == null || customerRatingConfigList.size() == 0){
            return new ResultDto<>(null);
        }
        List<CustomerRatingConfigDto> customerRatingConfigDtoList = Lists.newArrayList();
        for (CustomerRatingConfig customerRatingConfig : customerRatingConfigList) {
            CustomerRatingConfigDto customerRatingConfigDto = new CustomerRatingConfigDto();
            BeanUtil.copyProperties(customerRatingConfig,customerRatingConfigDto);
            customerRatingConfigDtoList.add(customerRatingConfigDto);
        }
        return new ResultDto(customerRatingConfigDtoList);
    }

    @Override
    public ResultDto<Integer> updateCustomerRatingConfig(CustomerRatingConfigDto customerRatingConfigDto) {
        CustomerRatingConfig customerRatingConfig = new CustomerRatingConfig();
        BeanUtil.copyProperties(customerRatingConfigDto,customerRatingConfig);
        return new ResultDto<>(customerRatingConfigService.updateCustomerRatingConfig(customerRatingConfig));
    }

    @Override
    public ResultDto<List<BusiBrandDto>> queryBusiBrand(Map<String, Object> params) {
        ResultDto<List<BusiBrandDto>> resultDto = new ResultDto();
        List<BusiBrandDto> brandList;
        try {
            brandList = busiBrandMapper.queryBusiBrand(params);
            resultDto.setData(brandList);
            resultDto.setCode("0000");
        } catch (Exception e) {
            log.error("ManagerFacadeService获取品牌文案宣传失败:" + e.getMessage());
            resultDto.setMsg("获取品牌文案宣传失败:" + e.getMessage());
            resultDto.setCode("1111");
        }
        return resultDto;
    }

    @Override
    public ResultDto insertBusiBrand(BusiBrandDto brandDto) {
        return new ResultDto(busiBrandMapper.saveBusiBrand(brandDto));
    }

    @Override
    public ResultDto updateBusiBrand(BusiBrandDto brandDto) {
        return new ResultDto(busiBrandMapper.updateBusiBrand(brandDto));
    }

    @Override
    public ResultDto deleteBusiBrand(Long id) {
        return new ResultDto(busiBrandMapper.removeBusiBrandById(id));
    }

    @Override
    public ResultDto<BusiBrandDto> getBusiBrandById(Long id) {
        return new ResultDto(busiBrandMapper.selectById(id));
    }


    private Boolean repayCash(List<CashDto> cashDtos, String cmNumber) {
        Boolean cashFlag = true;
        try {
            com.zdmoney.integral.api.common.dto.ResultDto resultDto = cashFacadeService.publishCash(cashDtos, SerialNoGeneratorService.commonGenerateTransNo(SerialNoType.REPAY_CASH, cmNumber));
            if (!resultDto.isSuccess()) {
                log.error("回款送现金券失败! 用户编号为:{},错误原因:{}", cmNumber, resultDto.getMsg());
                cashFlag = false;
            }
        } catch (Exception e) {
            log.error("调用账户系统--->，回款送现金券异常，cmNumber【{}】", cmNumber, e);
            cashFlag = false;
        }
        return cashFlag;
    }


    @Async
    private void pushSms(CustomerMainInfo customerMainInfo) {
        try {
            SendSmNotifyReqDto reqDto = new SendSmNotifyReqDto();
            //短信内容
            reqDto.setMobile(customerMainInfo.getCmCellphone());
            String msgContent = "您有资金回款到账，特赠1188元现金福利，开启后可提现。资金不闲置，助您好收益，立即来拿现金吧！退订回T";
            reqDto.setSendMsg(msgContent);
            MessageResultDto resultDto = iSmFacadeService.sendNotifyMsg(reqDto);
            if (resultDto.isSuccess()) {
                log.info("发送短信成功，用户编号:{}", customerMainInfo.getCmNumber());
            } else {
                log.error("发送短信失败，用户编号:{}", customerMainInfo.getCmNumber());
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("发送短信或微信异常,用户编号:{},原因:{}", customerMainInfo.getCmNumber(), e.getMessage());
        }
    }

    private boolean isSpecialInvestor(String cmNumber){
        if(cmNumber.equals(configParamBean.getGuarantee_cm_number())) return true;
        List<SysParameter> parameters = SystemParameterHelper.findParameterByPrType(AppConstants.SPECIAL_FINANCE_PEOPLE);
        for(SysParameter parameter : parameters){
            if(parameter.getPrValue().equals(cmNumber))
                return true;
        }
        return false;
    }

    @Override
    public ResultDto<List<CustomerRatingConfigDto>> findCustomerRatingConfigs() {
        ResultDto<List<CustomerRatingConfigDto>> resultDto = ResultDto.SUCCESS();
        try {
            List<CustomerRatingConfig> configs = customerRatingConfigService.findAll();
            List<CustomerRatingConfigDto> list = new ArrayList<>(configs.size());
            for(CustomerRatingConfig config: configs){
                CustomerRatingConfigDto tmp = new CustomerRatingConfigDto();
                BeanUtils.copyProperties(config, tmp);
                list.add(tmp);
            }
            resultDto.setData(list);
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            resultDto = ResultDto.FAIL("发生异常");
        }
        return resultDto;
    }

    public PageResultDto<BusiOrderDto> findMatchedOrders(QueryOrderReqDto reqDto){
        ensurePageParamsValid(reqDto);
        return busiOrderService.queryMatchedOrders(reqDto);
    }

    @Override
    public PageResultDto<BusiOrderDto> queryWacaiSubOrders(QueryOrderReqDto reqDto) {
        ensurePageParamsValid(reqDto);
        return busiOrderService.queryWacaiSubOrders(reqDto);
    }

    @Autowired
    private IMatchApi matchApi;

    @Autowired
    private MatchResultService matchResultService;


    public ResultDto<String> matchOrderAndCredit(String orderNum){
        ResultDto<String> resultDto = new ResultDto<>();
        try {
            ApplicationEventSupport.publishApplicationEvent(new ApplicationEventSupport.NewOrderEvent(orderNum));
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            resultDto = new ResultDto<>("发生异常："+e.getMessage(), false);
        }
        return resultDto;
    }

    public ResultDto<String> rematchOrderAndCredit(String orderNum){
        ResultDto<String> resultDto = new ResultDto<>();
        try {
            BusiOrder orderInfo = busiOrderMapper.getOrderInfo(orderNum);
            Assert.notNull(orderInfo, "订单不存在");
            MatchPart fund = new MatchPart();
            fund.setAppId(System.getProperty("app.id"));
            fund.setPoolId(orderInfo.getProductId().toString());
            fund.setMatchId(orderInfo.getOrderId());
            fund.setMatchValue(orderInfo.getOrderAmt());
            fund.setCreateTime(orderInfo.getConfirmPaymentDate());
            matchApi.rematchResource(fund);
            //ApplicationEventSupport.publishApplicationEvent(new ApplicationEventSupport.NewOrderEvent(orderInfo.getOrderId()));
        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            resultDto = new ResultDto<>("发生异常："+e.getMessage(), false);
        }
        return resultDto;
    }

    public ResultDto<String> processMatchResultBySubject(String subjectNo){
        ResultDto<String> resultDto = new ResultDto<>();
        try {//根据标的查询需要重新处理的匹配结果
            Map<String,Object> conditions = new HashMap<>();
            conditions.put("subjectNo", subjectNo);
            List<BusiOrderSub> orders = busiOrderSubMapper.selectByMap(conditions);
            if(!orders.isEmpty()) return new ResultDto<>("撮合子单已落地，请去选择子订单投标", false);
            BusiProductSub productSub = productService.findProductSubBySubjectNo(subjectNo);
            MatchApiResult<ResourceMatchResult> matchResult =
                    matchApi.getResourceMatchResult(productSub.getPlanId().toString(), System.getProperty("app.id"), subjectNo);
            if(matchResult == null){
                resultDto = new ResultDto<>("没有已撮合记录：", false);
            }else{
                BigDecimal totalAmt = BigDecimal.ZERO;
                ResourceMatchResult data = matchResult.getData();
                for(MatchDetail matchDetail :data.getMatchPartResults()){
                    totalAmt = totalAmt.add(matchDetail.getMatchDetailValue());
                }
                if(totalAmt.compareTo(productSub.getProductPrincipal()) !=0 )
                    resultDto = new ResultDto<>("标的尚未撮合完成：", false);
                else
                    matchResultService.processMatchResult(matchResult.getData(), false);
            }

        }catch (Exception e){
            LOGGER.error(e.getMessage(), e);
            resultDto = new ResultDto<>("发生异常："+e.getMessage(), false);
        }
        return resultDto;
    }

    public ResultDto<String> tenderSpecificOrders(List<String> orderNos){
        ResultDto<String> resultDto = new ResultDto<>("操作成功",true);
        StringBuilder msg = new StringBuilder();
        Map<String,BusiProductContract> subjects = new HashMap<>();
        for(String orderNum : orderNos){
            msg.append(tender(orderNum, subjects));
        }
        if(msg.length() >0 ){
            resultDto.setMsg(msg.toString());
            resultDto.setCode(ResultDto.ERROR_CODE);
        }
        return resultDto;
    }

    private String tender(String orderNum, Map<String,BusiProductContract> subjects){
        String msg = "";
        try{
            BusiOrderSub orderSub = busiOrderSubMapper.queryBusiOrderSubInfo(orderNum);
            if(StringUtils.isNotBlank(orderSub.getPaySerNum())) return orderNum+"已投标，请不要重新发起\r\n";
            CustomerMainInfo customer = customerMainInfoService.findOne(orderSub.getCustomerId());
            BusiProductContract subjectInfo = retriveSubjectInfoFromMap(subjects, orderSub.getSubjectNo());
            com.zdmoney.common.ResultDto<BusiOrderSub> tenderResult =
                    matchResultService.doTender(orderSub, customer, subjectInfo);
            if(!tenderResult.isSuccess()){
                msg = tenderResult.getMsg()+"\r\n";
            }else{
                updateOrder(orderSub, AppConstants.BusiOrderStatus.BUSIORDER_STATUS_0);
                ApplicationEventSupport.publishApplicationEvent(new ApplicationEventSupport.TenderSucceededEvent(orderNum));
            }
        }catch (Exception e){
            msg = String.format("订单%s投标失败：%s", orderNum, e.getMessage());
            LOGGER.error(msg, e);
        }
        return msg;
    }

    private void updateOrder(BusiOrderSub orderSub, String status){
        LOGGER.info("投标后更新子单信息【{}】：status->{},pay_ser_num->{}", orderSub.getOrderId(), status, orderSub.getPaySerNum());
        BusiOrderSub template = new BusiOrderSub();
        template.setOrderId(orderSub.getOrderId());
        template.setPaySerNum(orderSub.getPaySerNum());
        template.setStatus(status);
        busiOrderSubMapper.updateByPrimaryKeySelectiveByOrderNum(template);
    }

    private BusiProductContract retriveSubjectInfoFromMap(Map<String,BusiProductContract> subjects, String subjectNo){
        BusiProductContract subjectInfo = subjects.get(subjectNo);
        if(subjectInfo == null){
            subjectInfo = productContractService.selectBySubjectNo(subjectNo);
            subjects.put(subjectNo, subjectInfo);
        }
        return subjectInfo;
    }

    @Autowired
    private SubjectService subjectService;

    public ResultDto notifyBidApp(String subjectNo){
        ResultDto resultDto;
        try{
            resultDto = subjectService.sendGoalReachedNoticeIfNecessary(subjectNo, false);
        }catch (Exception e){
            log.error(e.getMessage(), e);
            resultDto = new ResultDto(e.getMessage(), false);
        }
        return resultDto;
    }
}
