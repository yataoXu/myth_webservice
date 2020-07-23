package com.zdmoney.service;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.zdmoney.assets.api.common.dto.AssetsResultDto;
import com.zdmoney.assets.api.dto.signature.SignCustomerChangeReqDto;
import com.zdmoney.assets.api.dto.signature.SignCustomerChangeResDto;
import com.zdmoney.assets.api.facade.subject.ILCBSubjectFacadeService;
import com.zdmoney.assets.api.utils.MD5Utils;
import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.common.Result;
import com.zdmoney.common.util.MD5;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.constant.BusiConstants;
import com.zdmoney.constant.ParamConstant;
import com.zdmoney.data.agent.api.user.base.UserTag;
import com.zdmoney.enm.AuditMethodType;
import com.zdmoney.enums.BusiTypeEnum;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.integral.api.dto.coupon.CouponRuleSource;
import com.zdmoney.integral.api.facade.IAccountFacadeService;
import com.zdmoney.integral.api.facade.IDepositFacadeService;
import com.zdmoney.mapper.BusiOrganMapper;
import com.zdmoney.mapper.bank.BusiUnbindRecordMapper;
import com.zdmoney.mapper.bank.CustomerBankAccountMapper;
import com.zdmoney.mapper.customer.CustomerAuthorizeMapper;
import com.zdmoney.mapper.customer.CustomerMainInfoMapper;
import com.zdmoney.mapper.payChannel.BusiPayBankLimitMapper;
import com.zdmoney.mapper.team.TeamMemberInfoMapper;
import com.zdmoney.marketing.entity.AuthMessage;
import com.zdmoney.marketing.entity.BindMessage;
import com.zdmoney.marketing.entity.RegisterMessage;
import com.zdmoney.message.api.dto.push.MsgDeviceCollectDto;
import com.zdmoney.message.api.facade.IMsgPushFacadeService;
import com.zdmoney.models.BusiOrgan;
import com.zdmoney.models.LoginEntry;
import com.zdmoney.models.MerchantInfo;
import com.zdmoney.models.MerchantRegisterRecord;
import com.zdmoney.models.bank.BusiUnbindRecord;
import com.zdmoney.models.bank.CustomerBankAccount;
import com.zdmoney.models.customer.CustomerAuthChannel;
import com.zdmoney.models.customer.CustomerAuthorizeInfo;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.payChannel.BusiPayBankLimit;
import com.zdmoney.models.team.TeamMemberInfo;
import com.zdmoney.component.mq.ProducerService;
import com.zdmoney.secure.utils.ThreeDesUtil;
import com.zdmoney.service.base.BaseBusinessService;
import com.zdmoney.service.businessReport.BusinessReportService;
import com.zdmoney.service.customer.CustomerValidateCodeService;
import com.zdmoney.service.payChannel.BusiPayChannelService;
import com.zdmoney.service.sys.AppSysInitService;
import com.zdmoney.service.sys.SysSwitchService;
import com.zdmoney.service.welfare.WelfareService;
import com.zdmoney.session.CustomerSessionService;
import com.zdmoney.session.RedisSessionManager;
import com.zdmoney.trace.utils.LcbTraceRunnable;
import com.zdmoney.utils.*;
import com.zdmoney.web.dto.CustomerDTO;
import com.zdmoney.web.dto.ResetTradePasswordDTO;
import com.zdmoney.webservice.api.common.CustomerAccountType;
import com.zdmoney.webservice.api.common.dto.PageResultDto;
import com.zdmoney.webservice.api.dto.customer.*;
import com.zdmoney.webservice.api.dto.plan.SpecialFinancialPlannerVO;
import com.zdmoney.webservice.api.dto.ym.CustomerMainInfoDto;
import com.zendaimoney.laocaibao.wx.common.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;
import websvc.models.Model_400003;
import websvc.req.ReqHeadParam;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
@Slf4j
public class CustomerMainInfoService extends BaseBusinessService<CustomerMainInfo, Long> {

    @Autowired
    private BusiOrganMapper busiOrganMapper;

    @Autowired
    private CustInviteLineService custInviteLineService;

    @Autowired
    private ConfigParamBean configParamBean;

    @Autowired
    private CustomerValidateCodeService customerValidateCodeService;

    @Autowired
    private CustomerSessionService customerSessionService;

    @Autowired
    private AppSysInitService appSysInitService;

    @Autowired
    private SysParameterService sysParameterService;

    @Autowired
    private MerchantRegisterRecordService merchantRegisterRecordService;

    @Autowired
    private MerchantInfoService merchantInfoService;

    @Autowired
    private IMsgPushFacadeService msgPushFacadeService;

    @Autowired
    private TeamMemberInfoMapper teamMemberInfoMapper;

    @Autowired
    private BankCardService bankCardService;
    @Autowired
    private ILCBSubjectFacadeService lcbSubjectFacadeService;
    @Autowired
    private BusiPayChannelService busiPayChannelService;
    @Autowired
    private BusiPayBankLimitMapper busiPayBankLimitMapper;
    @Autowired
    private BusinessReportService businessReportService;

    @Autowired
    private SysSwitchService sysSwitchService;

    @Autowired
    private RedisSessionManager redisSessionManager;

    @Autowired
    private CustomerAuthorizeMapper customerAuthorizeMapper;

    @Autowired
    private BusiUnbindRecordMapper busiUnbindRecordMapper;


    @Autowired
    private WelfareService welfareService;

    @Autowired
    private CustomerBankAccountMapper bankAccountMapper;

    private CustomerMainInfoMapper getCustomerMainInfoMapper() {
        return (CustomerMainInfoMapper) baseMapper;
    }

    public CustomerMainInfo checkCustomerId(Long customerId) {
        return checkExistByCustomerId(customerId);
    }

    /**
     * 查询有效用户且已实名认证
     *
     * @param customerId
     * @return
     */
    public CustomerMainInfo findAuthCustomerById(Long customerId) {
        CustomerMainInfo mainInfo = getCustomerMainInfoMapper().selectByPrimaryKey(customerId);
        if (mainInfo == null) {
            throw new BusinessException("customer.not.exist");
        }
        if (AppConstants.CustomerValidStatus.CUSTOMER_VALID != mainInfo.getCmValid()) {
            throw new BusinessException("customer.not.exist");
        }
        if (AppConstants.CmStatus.MEMBER_AUTHEN_SUCCESS != (mainInfo.getCmStatus())) {
            throw new BusinessException("customer.not.auth");
        }
        return mainInfo;
    }

    /**
     * 查询已开户用户
     *
     * @param customerId
     * @return
     */
    public CustomerMainInfo findOpenCustomerById(Long customerId) {
        CustomerMainInfo mainInfo = this.findAuthCustomerById(customerId);
        if (AppConstants.CmOpenAccountFlag.UNOPEN.equals(mainInfo.getCmOpenAccountFlag())) {
            throw new BusinessException("customer.not.open");
        }
        return mainInfo;
    }

    public CustomerMainInfo findOneByInviteCode(String inviteCode) {
        if (!StringUtils.isEmpty(inviteCode)) {
            CustomerMainInfo mainInfo = new CustomerMainInfo();
            mainInfo.setCmInviteCode(inviteCode);
            mainInfo.setCmValid(0);
            return getCustomerMainInfoMapper().selectOne(mainInfo);
        } else {
            return null;
        }
    }

    public CustomerMainInfo findOneByPhone(String phone) {
        CustomerMainInfo mainInfo = new CustomerMainInfo();
        mainInfo.setCmCellphone(phone);
        mainInfo.setCmValid(0);
        return getCustomerMainInfoMapper().selectOne(mainInfo);
    }

    public CustomerMainInfo selectByIdNum(String borrowerIdNum,String customerType) {
        CustomerMainInfo mainInfo = new CustomerMainInfo();
        mainInfo.setCmIdnum(borrowerIdNum);
        mainInfo.setCmValid(0);
        mainInfo.setCustomerType(customerType);
        return getCustomerMainInfoMapper().selectOne(mainInfo);
    }


    public CustomerMainInfo findOneByCmNumber(String cmNumber) {
        CustomerMainInfo mainInfo = new CustomerMainInfo();
        mainInfo.setCmNumber(cmNumber);
        mainInfo.setCmValid(0);
        return getCustomerMainInfoMapper().selectOne(mainInfo);
    }

    public boolean isExistByIdNum(String cmIdnum,String customerType) {
        cmIdnum = StringUtils.upperCase(cmIdnum);
        Example example = new Example(getEntityClass());
        example.createCriteria()
                .andEqualTo("cmValid", "0")
                .andCondition("CM_IDNUM=", ThreeDesUtil.encryptMode(cmIdnum))
                .andCondition("cm_status=", 3)
                .andCondition("customer_type=", customerType);


        return getCustomerMainInfoMapper().selectCountByExample(example) > 0;
    }


    public CustomerMainInfo validateIntroduceCode(String cmIntroduceCode) {
        return getCustomerMainInfoMapper().validateIntroduceCode(cmIntroduceCode);
    }

    public void validateIntroduceCodeCmOrOrg(String cmIntroduceCode) {
        cmIntroduceCode = StringUtils.upperCase(cmIntroduceCode);
        //不验证000000
        if (!AppConstants.SYS_INVITE_CODE.equals(cmIntroduceCode)) {
            int param = Integer.valueOf(sysParameterService.findOneByPrType("codeLimit").getPrValue());
            //2016/3/29 应该统一使用系统时间或者数据库时间
            if (getCustomerMainInfoMapper().validateIntroduceCodeByDay(cmIntroduceCode) >= param) {
                throw new BusinessException("invitedCode.today.maxLimit");
            }
        }

        boolean isOrganCustomer = isOrganCustomer(cmIntroduceCode);

        Map<String, Object> searchParam = Maps.newHashMap();//查询参数Map
        if (StringUtils.isNotEmpty(cmIntroduceCode)) {//判断邀请码是否有效
            searchParam.put("cmInviteCode", cmIntroduceCode);
            searchParam.put("cmStatus", "3");
            searchParam.put("cmValid", "0");
            long resCount = getCustMainCountByParam(searchParam);
            if (resCount == 0 && !isOrganCustomer) {//不是机构邀请码
                throw new BusinessException("invitedCode.invalid");
            }
        }
    }

    public CustomerMainInfo getCustMainInfoByIntroduceCode(String cmIntroduceCode){
        CustomerMainInfo mainInfo = new CustomerMainInfo();
        if (StringUtils.isNotBlank(cmIntroduceCode)) {
            Map<String, Object> searchParam = Maps.newHashMap();
            searchParam.put("cmInviteCode", cmIntroduceCode);
            searchParam.put("cmStatus", "3");
            searchParam.put("cmValid", "0");
            mainInfo = getCustomerMainInfoMapper().queryCustMainInfoByIntroduceCode(searchParam);
        }
        return mainInfo;
    }

    public void updatepassword(String cellphone, String password, String cmLoginPassword) {
        getCustomerMainInfoMapper().updatepassword(cellphone, password, cmLoginPassword);
    }


    public long getCustMainCountByParam(Map<String, Object> searchParam) {
        return getCustomerMainInfoMapper().getCustMainCountByParam(searchParam);
    }

    public CustomerDTO loginAndDecorate(String loginType, String cellphone, String password, String deviceId, String clientId, ReqHeadParam headParam) throws Exception {
        CustomerMainInfo mainInfo = checkExistByCellphone(cellphone);
        checkLoginPassword(password, mainInfo.getCmSalt(), mainInfo.getCmLoginPassword());
        if (mainInfo.getCmStatus() == AppConstants.CmStatus.FROZEN_MEMBER) {
            throw new BusinessException("customer.status.frozen");
        }
        //借款人只能在借款端登录
        if (!"credit".equals(headParam.getSystem()) && CustomerAccountType.BORROWER.getValue().equalsIgnoreCase(mainInfo.getAccountType())) {
            throw new BusinessException("该手机号码是在捞财宝借款端注册的");
        }
        //出借人只能在理财端登录
        if ("credit".equals(headParam.getSystem()) && CustomerAccountType.LENDER.getValue().equalsIgnoreCase(mainInfo.getAccountType())) {
            throw new BusinessException("该手机号码是在捞财宝出借端注册的");
        }
        String userAgent = ReqUtils.getUserAgent(headParam);
        String token = headParam.getToken();
        return decorateUser(loginType, mainInfo, token, deviceId, userAgent, cellphone, clientId);
    }

    /**
     * 验证码登录
     * @param loginType
     * @param cellphone
     * @param validateCode
     * @param deviceId
     * @param clientId
     * @param headParam
     * @return
     * @throws Exception
     */
    public CustomerDTO loginValidateCode(String loginType, String cellphone, String validateCode, String deviceId, String clientId, ReqHeadParam headParam) throws Exception {
        CustomerMainInfo mainInfo = checkExistByCellphone(cellphone);
        checkWhiteListByPhone(cellphone);
        checkSmsCode( cellphone,  validateCode ,13);
        if (mainInfo.getCmStatus() == AppConstants.CmStatus.FROZEN_MEMBER) {
            throw new BusinessException("customer.status.frozen");
        }
        String userAgent = ReqUtils.getUserAgent(headParam);
        String token = headParam.getToken();
        return decorateUser(loginType, mainInfo, token, deviceId, userAgent, cellphone, clientId);
    }


    /**
     * 封装用户登录对象
     *
     * @param loginType 登录类型 app/微信/网页
     * @param mainInfo  用户信息
     * @param token
     * @param identify  标识 app->设备号/微信->openId/网页
     * @param userAgent 平台
     * @param cellPhone 手机号
     * @return 登录对象
     * @throws Exception
     */
    public CustomerDTO decorateUser(String loginType, CustomerMainInfo mainInfo, String token, String identify, String userAgent, String cellPhone, String clientId) throws Exception {
        CustomerDTO dto = CustomerDTO.fromCustomerMainInfo(mainInfo);

        if ("0".equals(mainInfo.getUserLabel()) && "4".equals(mainInfo.getUserLevel())) {
            dto.setCmEmployee(ParamConstant.VALID);
        } else {
            dto.setCmEmployee(ParamConstant.UNVALID);
        }
        dto.setToken(token);
        dto.setDeviceId(identify);
        dto.setCmNumToken(LaocaiUtil.makeUserToken(configParamBean.getUserTokenKey(), mainInfo.getCmNumber()));
        String sessionToken = customerSessionService.loginCustomer(loginType, cellPhone, identify, dto,mainInfo.getId());
        Map<String, Object> payload = new HashMap<>();
        payload.put("cmNumber", mainInfo.getCmNumber());
        dto.setSessionToken(sessionToken);
        if (StringUtils.isNotEmpty(dto.getCmIdnum())) {
            dto.setQrCode(configParamBean.getQrCodeurl() + mainInfo.getCmInviteCode());
        }
        if(StringUtils.isEmpty(mainInfo.getAccountType()) || CustomerAccountType.LENDER.getValue().equals(mainInfo.getAccountType())){//出借人允许购买
            dto.setBuyingPermitted(1);
        }else{
            dto.setBuyingPermitted(0);
        }

        // 清空数据库中该token和deviceType的值
//        if (StringUtils.isNotEmpty(token) && StringUtils.isNotEmpty(deviceType)) {
//            synchronized(this) {
//                clearToken(token, deviceType);
//                updateToken(token, deviceType, mainInfo.getId());
//            }
//        }

        String shareRedPackUrl = appSysInitService.getShareRedPackUrl(mainInfo);
        if (shareRedPackUrl != null) {
            dto.setShareRedUrl(shareRedPackUrl);
        }
        final String newCellphone = cellPhone;
        final String newClientId = clientId;
        String deviceType = null;
        String appType = AppConstants.getAppType(userAgent);
        if (AppConstants.APP_TYPE_ANDROID.equals(appType)) {
            deviceType = "1";
        } else if (AppConstants.APP_TYPE_IOS.equals(appType)) {
            deviceType = "2";
        }
        final String newDeviceType = deviceType;
        log.info("用户登录userAgent=" + userAgent + "clientId=" + clientId);
        if (StringUtils.isNotBlank(deviceType) && StringUtils.isNotBlank(clientId)) {
            AsyncTaskUtil.asyncTask(new LcbTraceRunnable() {
                @Override
                public void concreteRun() {
                    MsgDeviceCollectDto collectDto = new MsgDeviceCollectDto();
                    collectDto.setDeviceType(newDeviceType);
                    collectDto.setCellphoneNum(newCellphone);
                    collectDto.setDeviceId(newClientId);
                    msgPushFacadeService.collect(collectDto);
                }
            });
        }
        String tips = null;
        if (dto.getIsSetPwd() == 0) {
            if (StringUtils.isNotEmpty(mainInfo.getBankAccountId())) {
                CustomerBankAccount bankAccount = bankAccountMapper.selectByPrimaryKey(Convert.toLong(mainInfo.getBankAccountId()));
                String bankNo = bankAccount.getCbAccount();
                tips = "开通时请输入您的尾号为" + bankNo.substring(bankNo.length()-4, bankNo.length())+"的"+bankAccount.getCbBankName()+"卡";
            } else {
                tips = "开通时请输入您名下的且华瑞银行支持的银行卡";
            }
            dto.setPwdTips(tips + "，作为您开通华瑞银行交易密码的验证信息。");
        }
        return dto;
    }

    public CustomerMainInfo register(String cellphone, String password, String introduceCode, String redNo, String loginOpenId, ReqHeadParam headParam, String ip,Model_400003 cdtModel,String uid,String tc) {
        return register(cellphone, password, introduceCode, redNo, loginOpenId, null, headParam, ip,cdtModel,uid,tc);
    }

    public CustomerMainInfo register(String cellphone, String password, String introduceCode, String redNo, String loginOpenId, String channelCode, ReqHeadParam headParam, String ip,Model_400003 cdtModel,String uid,String tc) {
        boolean lock = false;
        String LOCK = "REGISTER-LOCK-" + cellphone;
        String KEY = "REGISTER-KEY-" + cellphone;
        CustomerMainInfo mainInfo = new CustomerMainInfo();
        try{
            lock = redisSessionManager.setNX(KEY, LOCK);
            if(lock) {
                // 1分钟后,key值失效,自动释放锁
                redisSessionManager.expire(KEY, 1, TimeUnit.MINUTES);
                if (StringUtils.isEmpty(introduceCode)) {
                    introduceCode = AppConstants.SYS_INVITE_CODE;
                }
                // 转换成大写
                password = StringUtils.upperCase(password);
                introduceCode = StringUtils.upperCase(introduceCode);
                log.info("注册传入的邀请码："+introduceCode);

                boolean isOrganCustomer = isOrganCustomer(introduceCode);
                //增加挖财需求不验证 introduceCode
                if(null != cdtModel && "wacai".equals(cdtModel.getWacai_flag())){

                }else{
                    validateIntroduceCodeCmOrOrg(introduceCode);
                }

                checkNotRegisterByCellphone(cellphone);

				//默认为互联网用户
				mainInfo.setUserLabel("0");
				mainInfo.setUserLevel("3");

				// 默认已设置华瑞密码
                mainInfo.setIsSetPwd(1);
                // 默认为铁象
                mainInfo.setMemberLevel(AppConstants.MEMBER_LEVEL.MEMBER_LEVEL_1);
                mainInfo.setRatingChangingDate(new Date());

                // 获取介绍人信息
                CustomerMainInfo custMainInfo = getCustMainInfoByIntroduceCode(introduceCode);
                if (custMainInfo != null) {
                    mainInfo.setIntroducer(custMainInfo.getCmRealName());
                    mainInfo.setIntroducerIdNum(custMainInfo.getCmIdnum());
                    log.info("----------->介绍人姓名:" + mainInfo.getIntroducer() + "身份证号:" + mainInfo.getIntroducerIdNum());
                    //用户层级 0：理财师  1：理财师1级客户   2：理财师2级客户  3:互联网客户
                    if (custMainInfo.getUserLevel() !=null){
                        if ("0".equals(custMainInfo.getUserLevel())){//介绍人是理财师
                            mainInfo.setUserLabel("1");
                            mainInfo.setUserLevel("1");
                        }
                        if ("1".equals(custMainInfo.getUserLevel())){//介绍人是理财师1级客户
                            mainInfo.setUserLabel("1");
                            mainInfo.setUserLevel("2");
                        }
                    }
                }
                if("credit".equals(headParam.getPlatform()) || "credit".equals(headParam.getSystem())){
                    mainInfo.setAccountType(CustomerAccountType.BORROWER.getValue());//借款人
                }else {
                    mainInfo.setAccountType(CustomerAccountType.LENDER.getValue());//出借人
                }
                if (cdtModel != null && StringUtils.isNotEmpty(cdtModel.getAccountType())) {
                    mainInfo.setAccountType(cdtModel.getAccountType());
                }
                if("wacai".equals(headParam.getPlatform()) || "wacai".equals(headParam.getSystem())){//挖财用户就是借款人
                    mainInfo.setAccountType(CustomerAccountType.BORROWER.getValue());//借款人
                }else{
                    mainInfo.setInviterMemberLevel(custMainInfo.getMemberLevel());
                    mainInfo.setInviterUserLevel(custMainInfo.getUserLevel());
                }
                mainInfo.setIp(ip);
                mainInfo.setCmCellphone(cellphone); //手机号
                mainInfo.setCmInputDate(new Date()); //录入时间
                mainInfo.setCmModifyDate(new Date());//修改时间
                if ("credit".equals(headParam.getOpenchannel())) {
                    mainInfo.setCmPassword(ThreeDesUtils.encryptMode(password));
                } else {
                    mainInfo.setCmPassword(password.length() < 32 ? MD5Utils.encode(password) : password);//密码  全部转大写入库
                }
                mainInfo.setCmSalt(genSalt());//盐值
                mainInfo.setCmLoginPassword(genLoginPassword(password, mainInfo.getCmSalt()));    //生成登录密码
                String advisor = queryIsAdvisor(introduceCode);
                mainInfo.setMemberType(AppConstants.MemberType.NORMAL);
                if (StringUtils.isNotBlank(advisor)) {
                    mainInfo.setMemberType(AppConstants.MemberType.ADVISOR_MEMBER);
                    mainInfo.setPlannerInviteCode(advisor);
                }
                mainInfo.setCmInputId(9999999L); //表示系统默认录入
                mainInfo.setCmStatus(1);//新建用户
                mainInfo.setCmValid(0);//正常标志位
                mainInfo.setCmNumber(LaocaiUtil.buildCustNum("01"));//构建客户编号，01代表APP注册用户
                mainInfo.setCmIntroduceCode(introduceCode);//邀请码
                mainInfo.setCmOrigin(isOrganCustomer ? BusiConstants.INVITER_ORGAN : BusiConstants.INVITER_PERSONAL);
                mainInfo.setCmEmployee(0);//是否员工标识符
                mainInfo.setCmAuthenCount(0); //验证此时默认赋值0

                //注册相关信息
                mainInfo.setCmOpenMechanism(headParam.getMechanism());
                mainInfo.setCmOpenChannel(headParam.getOpenchannel());
                mainInfo.setCmOpenPlatform(headParam.getPlatform());
                mainInfo.setCmTogatherType(headParam.getTogatherType());
                mainInfo.setCmRegisterVersion(headParam.getVersion());
                mainInfo.setIsConsumed(0L);
                mainInfo.setCmRecommend(AppConstants.RecommendStatus.RECOMMEND_STATUS_5);
                mainInfo.setLoginOpenId(StringUtils.defaultString(loginOpenId));
                mainInfo.setResetErrorTime(0);
                mainInfo.setPayErrorTime(0);
                mainInfo.setBuyWechat(0);
                mainInfo.setChannelCode(channelCode);
                mainInfo.setCustomerType("0");
                mainInfo.setRegisterSource("credit".equalsIgnoreCase(headParam.getSystem()) ? AppConstants.RegisterSource.CREDIT : AppConstants.RegisterSource.LCB);

                //返利网信息
                if (StringUtils.isNotBlank(uid)){
                    mainInfo.setFanliUid(uid);//返利网会员标识
                }
                if (StringUtils.isNotBlank(tc)){
                    mainInfo.setFanliTc(tc);//返利网订单跟踪信息
                }

                //机构理财用户信息(manager)
                if (BusiConstants.LOGIN_TYPE_MANAGE.equalsIgnoreCase(headParam.getPlatform())) {
                    mainInfo.setBusinessLicense(cdtModel.getBusinessLicense());
                    mainInfo.setOrgCode(cdtModel.getOrgCode());
                    mainInfo.setEnterpriseName(cdtModel.getEnterpriseName());
                    mainInfo.setCustomerType(cdtModel.getCustomerType());
                }
                mainInfo = save(mainInfo);

                if (StringUtils.isNotBlank(channelCode)) {
                    MerchantRegisterRecord merchantRegisterRecord = new MerchantRegisterRecord();
                    merchantRegisterRecord.setCustomerNo(mainInfo.getCmNumber());
                    merchantRegisterRecord.setMerchantNo(channelCode);
                    merchantRegisterRecord.setMerchantType(AppConstants.MerchantType.CHANNEL);
                    merchantRegisterRecord.setRegisterDate(new Date());
                    merchantRegisterRecordService.save(merchantRegisterRecord);
                }

                if (!isOrganCustomer) {//邀请人非机构用户
                    CustomerMainInfo inviteCustomer = findOneByInviteCode(mainInfo.getCmIntroduceCode());
                    if (inviteCustomer != null) {
                        custInviteLineService.saveCustInviteLine(mainInfo, inviteCustomer, AppConstants.CustInviteLineStatusContants.RIGISTER);
                    }
                }


                RegisterMessage message = new RegisterMessage();
                message.setCmNumber(mainInfo.getCmNumber());
                message.setCellphone(cellphone);
                message.setPlatform(ReqUtils.getUserAgent(headParam));
                message.setSource(CouponRuleSource.REG.name());
                message.setCustomerId(mainInfo.getId());
                message.setRedNo(redNo);
                message.setChannelCode(channelCode);
                if (!isOrganCustomer(introduceCode)) {//邀请者是普通用户送积分，机构用户不送
                    CustomerMainInfo info = validateIntroduceCode(introduceCode);
                    if (info != null) {
                        message.setInviteCmNumber(info.getCmNumber());
                    }
                }

                //注册送红包
                boolean isCoupon = sysSwitchService.getSwitchIsOn("isCoupon");
                if (isCoupon) {//是否开启红包
                    welfareService.sendRegistCash(mainInfo.getCmNumber());
                }
                //审计
//                businessReportService.sendReport("001", message);
                //注册数据推送
               // pushRegistData(mainInfo,cellphone);
            } else{
                // 没有获取到锁
                throw new BusinessException("注册失败");
            }
        } catch (Exception e) {
            log.error("注册失败:" + e.getMessage());
            throw new BusinessException("注册失败!");
        } finally {
            if(lock){// 如果获取了锁，则释放锁
                redisSessionManager.remove(KEY);
                log.info(Thread.currentThread().getName() + "用户注册请求结束，释放锁!");
            }
        }
        return mainInfo;
    }
    private void pushRegistData(CustomerMainInfo mainInfo,String cellphone){
        JSONObject json = new JSONObject();
        json.put("cmNumber", mainInfo.getCmNumber());
        json.put("customerId", mainInfo.getId());
        json.put("registerTime", new Date());
        json.put("telPhone",cellphone);
        UserTag userTag = UserTag.LENDER;
        if(mainInfo.getAccountType().equals(CustomerAccountType.BORROWER.getValue())){
            userTag = UserTag.BORROW;
        }
        json.put("userTag",userTag.name());
        String userType="0";
        //查询注册用户信息
        String userLevel =mainInfo.getUserLevel();
        if (userLevel != null) {
            if ("0".equals(userLevel) || "1".equals(userLevel)||  "2".equals(userLevel)){
                userType="1";//理财师客户
            }
        }
        json.put("userType",userType);
        json.put("ipAddress",StringUtils.isBlank(mainInfo.getIp())?"":mainInfo.getIp());
        json.put("registStatus","1");//注册类型
        //producerService.sendCollectingDataMsg(json.toString(),MsgType.REGISTER);
    }

    @Transactional
    public Long doRegister(String cmCellphone,String cmPassword,String validateCode,String cmRealName,String cmIdnum,String IP,ReqHeadParam reqHeadParam) throws Exception {
        CustomerMainInfo customerMainInfo = this.registerWithValidateCode(cmCellphone, cmPassword, validateCode, null, null, null, reqHeadParam, IP,null);
        Long customerId = customerMainInfo.getId();
        this.realNameAuth(customerId, cmRealName, cmIdnum, reqHeadParam);
        return customerId;
    }

    @Transactional
    public Long doChannelRegister(String cmCellphone,String cmPassword,String validateCode,String channelCode,String IP,ReqHeadParam reqHeadParam) throws Exception {
        Integer cvType = AppConstants.ValidateCode.REGISTER;
        if ("credit".equals(reqHeadParam.getOpenchannel())) {
            cvType = AppConstants.ValidateCode.CREDIT;
        }
        customerValidateCodeService.checkValidateCode(cvType, cmCellphone, validateCode);
        CustomerMainInfo customerMainInfo = register(cmCellphone, cmPassword, null, null, null, channelCode, reqHeadParam, IP,null,null,null);
        return customerMainInfo.getId();
    }

    public CustomerMainInfo registerWithValidateCode(String cellphone, String password, String inputValidateCode, String introduceCode, String redNo, String loginOpenId, ReqHeadParam headParam, String ip,Model_400003 cdtModel) {
        Integer cvType = AppConstants.ValidateCode.REGISTER;
        if ("credit".equals(headParam.getOpenchannel())) {
            cvType = AppConstants.ValidateCode.CREDIT;
        }
        //校验诈骗用户 针对注册
        if(AppConstants.ValidateCode.REGISTER.equals(cvType)){
            if (!"credit".equals(headParam.getSystem())) {
                boolean isIllegal = customerValidateCodeService.checkIsIllegal(cellphone);
                if(!isIllegal){
                    throw new BusinessException("该手机号已注册");
                }
            }
        }
        customerValidateCodeService.checkValidateCode(cvType, cellphone, inputValidateCode);
        return register(cellphone, password, introduceCode, redNo, loginOpenId, headParam, ip,cdtModel,null,null);
    }

    public CustomerMainInfo registerFromChannel(String cellphone, String password, String inputValidateCode, String channelCode, ReqHeadParam headParam, String ip,String inviteCode,String uid,String tc) {
        MerchantInfo merchantInfo = merchantInfoService.findMerchantInfo(channelCode);
        if (merchantInfo == null) {
            throw new BusinessException("register.failed.channelCode");
        }
        log.info("获取渠道注册传入的邀请码："+inviteCode);
        customerValidateCodeService.checkValidateCode(AppConstants.ValidateCode.REGISTER, cellphone, inputValidateCode);
        return register(cellphone, password, inviteCode, null, null, channelCode, headParam, ip,null,uid,tc);
    }

    /*
    * 判读用户填写的邀请码是否是机构邀请码
    */
    public boolean isOrganCustomer(String inviteCode) {
        if (StringUtils.isEmpty(inviteCode)) {
            return false;
        }
        BusiOrgan busiOrgan = new BusiOrgan();
        busiOrgan.setInviteCode(inviteCode);
        //2016/3/29 是否要判断有效状态
        return busiOrganMapper.selectCount(busiOrgan) > 0;
    }

    public CustomerMainInfo changePassword(String cellphone, String oldPassword, String newPassword) {
        CustomerMainInfo mainInfo = checkExistByCellphone(cellphone);
        checkLoginPassword(oldPassword, mainInfo.getCmSalt(), mainInfo.getCmLoginPassword());
        String cmLoginPassword = genLoginPassword(newPassword.toUpperCase(), mainInfo.getCmSalt());
        updatepassword(cellphone, newPassword, cmLoginPassword);
        //修改成功，若用户绑定过微信，则微信推送通知
        String openId = mainInfo.getOpenId();
        if (!StringUtils.isEmpty(openId)) {
            Map<String, String> map = ImmutableMap.of(
                    "keyword1", "登陆密码",
                    "keyword2", DateTime.now().toString("yyyy-MM-dd HH:mm")
            );
            sendWxTemplateMsg(openId, Constants.MSG_TPL_ACCOUNT_CHANGED, map);
        }
        return mainInfo;
    }

    public void checkLoginPassword(String inputPassword, String salt, String loginPassword) {
        String inputPasswordEncode = genLoginPassword(inputPassword, salt);
        if (!StringUtils.equalsIgnoreCase(inputPasswordEncode, loginPassword)) {
            throw new BusinessException("password.error");
        }
    }

    /**
     * 实名认证
     */
    public  CustomerDTO realNameAuthInit(Long customerId) throws Exception {
        CustomerMainInfo mainInfo = checkExistByCustomerId(customerId);
        //查询用户信息
        CustomerDTO dto = CustomerDTO.fromCustomerMainInfo(mainInfo);
        if (mainInfo.getCmAuthenCount() > 2) {
            dto.setAuthStatus(1);
            dto.setMsg("auth.maxLimit");
            return dto;
        }
        return dto;
    }

    /**
     * 实名认证
     */
    public  CustomerDTO realNameAuth(Long customerId, String realName, String idnum, ReqHeadParam headParam) throws Exception {
        CustomerMainInfo mainInfo = checkExistByCustomerId(customerId);
        //查询用户信息
        CustomerDTO dto = CustomerDTO.fromCustomerMainInfo(mainInfo);
        if (isExistByIdNum(StringUtils.upperCase(idnum),mainInfo.getCustomerType())) {
            CustomerMainInfo temp = getCustomerMainInfoMapper().selectByIdNum(idnum);
            String type = "";
            if (CustomerAccountType.LENDER.getValue().equals(temp.getAccountType())) {
                type = CustomerAccountType.LENDER.getDescr();
            } else {
                type = CustomerAccountType.BORROWER.getDescr();
            }
            if("wacai".equals(headParam.getSystem())){
               return dto;
            }
            throw new BusinessException("您的身份证号已在捞财宝平台开通"+type+"账户，请不要重复开户。");
        }

        if (mainInfo.getCmStatus() == AppConstants.CmStatus.MEMBER_AUTHEN_SUCCESS) {
            if("wacai".equals(headParam.getSystem())){
                return dto;
            }
            throw new BusinessException("auth.already");
        }
        if(!"wacai".equals(headParam.getSystem())){
            //借款人不能实名认证
            if (!"credit".equals(headParam.getSystem())  && CustomerAccountType.BORROWER.getValue().equalsIgnoreCase(mainInfo.getAccountType())) {
                throw new BusinessException("auth.borrow.failed");
            }
        }
        if (mainInfo.getCmAuthenCount() > 2) {
//            if("wacai".equals(headParam.getSystem())){
//                return dto;
//            }else {
                throw new BusinessException("auth.maxLimit");
//            }
        }
        boolean lock = false;
        String lockKey = "REALNAME_AUTH_LCK_" + idnum + "_" + mainInfo.getCustomerType() + "_KEY";
        String lockValue = "REALNAME_AUTH_LCK_" + idnum + "_" + mainInfo.getCustomerType() + "_LOCK";
        try {
            // 获取锁
            lock = redisSessionManager.setNX(lockKey, lockValue);
            log.info(Thread.currentThread().getName() + "当前实名认证请求线程是否获取到锁:" + lock);
            if(lock){
                // 5分钟后,key值失效,自动释放锁
                redisSessionManager.expire(lockKey, 5, TimeUnit.MINUTES);
                if (!checkIdNum(StringUtils.upperCase(idnum))) {
                    throw new BusinessException("idnum.failed");
                }
                if (getAgeByIdNum(StringUtils.upperCase(idnum)) < 18) {
                    throw new BusinessException("age.noPass");
                }
                if (isExistByIdNum(StringUtils.upperCase(idnum),mainInfo.getCustomerType())) {
                    throw new BusinessException("idnum.exist");
                }
                CustomerAuthChannel auth = getCustomerMainInfoMapper().queryAuthChannelInfo();
                if (auth == null) throw new BusinessException("无可用认证通道!");
                if (AppConstants.authChannel.GZT == auth.getId()) {
                    //国政通验证
                    if (!"OFF".equals(configParamBean.getID5Switch())) {
                        if (!ID5Util.resultValidate(realName, idnum)) {
                            throw new BusinessException("auth.failed");
                        }
                    }
                } else if (AppConstants.authChannel.AR == auth.getId()) {
                    // 安融认证
                    if (!AnRongAuthenticationUtil.authentication(realName, idnum)) {
                        throw new BusinessException("auth.failed");
                    }
                } else if (AppConstants.authChannel.SH == auth.getId()) {
                    // 算话认证
                    if (!SuanHuaAuthenticationUtil.authentication(realName, idnum)) {
                        throw new BusinessException("auth.failed");
                    }
                } else {
                    throw new BusinessException("无可用认证通道!");
                }

                boolean inviteStaff = "0".equals(mainInfo.getUserLabel()) && "4".equals(mainInfo.getUserLevel());//sysStaffInfoService.isExistIdnum(idnum);
                boolean indroduceStaff = false;
                if (!StringUtil.isEmpty(mainInfo.getCmIntroduceCode())) {
                    CustomerMainInfo introduceMainInfo = findOneByInviteCode(mainInfo.getCmIntroduceCode());
                    indroduceStaff = introduceMainInfo != null && inviteStaff;
                }

                if (StringUtil.isEmpty(mainInfo.getCmIntroduceCode()))
                    mainInfo.setCmRecommend(inviteStaff ? AppConstants.RecommendStatus.RECOMMEND_STATUS_1 : AppConstants.RecommendStatus.RECOMMEND_STATUS_3);
                else if (inviteStaff)
                    mainInfo.setCmRecommend(indroduceStaff ? AppConstants.RecommendStatus.RECOMMEND_STATUS_6 : AppConstants.RecommendStatus.RECOMMEND_STATUS_2);
                else if (!indroduceStaff)
                    mainInfo.setCmRecommend(AppConstants.RecommendStatus.RECOMMEND_STATUS_4);
                else
                    mainInfo.setCmRecommend(AppConstants.RecommendStatus.RECOMMEND_STATUS_5);

                mainInfo.setCmRealName(realName);
                mainInfo.setCmIdnum(idnum);
                mainInfo.setCmIdnumType(Integer.parseInt(ParamConstant.IDTYPESF));
                mainInfo.setCmStatus(AppConstants.CmStatus.MEMBER_AUTHEN_SUCCESS);
                mainInfo.setCmInviteCode(String.valueOf(mainInfo.getId() + 123456));
                mainInfo.setCmModifyId(9999999L);
                Date date = new Date();
                mainInfo.setCmModifyDate(date);
                mainInfo.setRealNameTime(date);

                mainInfo = updateNotNull(mainInfo);
            }else {
                log.info("实名请求没有获取到锁，实名失败。");
                throw new BusinessException("实名出现异常，请稍后重试");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            if (e instanceof BusinessException) {
                //存管更新
                getCustomerMainInfoMapper().updateCmStatusFailure(mainInfo.getId());
            }
            throw e;
        }finally {
            if(lock){// 如果获取了锁，则释放锁
                redisSessionManager.remove(lockKey);
                log.info(Thread.currentThread().getName() + "用户实名认证请求结束，释放锁!");
            }
        }
        dto = CustomerDTO.fromCustomerMainInfo(mainInfo);
        dto.setCmStatus(String.valueOf(mainInfo.getCmStatus()));
        dto.setIsAuth(1);
        dto.setCmEmployee(StringUtils.defaultString(mainInfo.getCmEmployee().toString(), ParamConstant.UNVALID));
        dto.setDeviceId("");
        dto.setToken(StringUtils.defaultString(mainInfo.getCmToken()));
        dto.setQrCode(configParamBean.getQrCodeurl() + mainInfo.getCmInviteCode());
        dto.setCmNumToken(LaocaiUtil.makeUserToken(configParamBean.getUserTokenKey(), mainInfo.getCmNumber()));
        /*获取分享红包url*/
        String shareRedPackUrl = appSysInitService.getShareRedPackUrl(mainInfo);
        if (shareRedPackUrl != null) {
            dto.setShareRedUrl(shareRedPackUrl);
        }
        AuthMessage message = new AuthMessage();
        message.setCmNumber(mainInfo.getCmNumber());
        message.setCustomerId(mainInfo.getId());
        sendRocketMqMsg(BusiTypeEnum.AUTH, message);

        businessReportService.sendReport("002",message);

        //实名认证数据推送
        JSONObject json = new JSONObject();
        json.put("cmNumber", mainInfo.getCmNumber());
        json.put("customerId", mainInfo.getId());
        json.put("certificateTime", new Date());
        json.put("name",realName);
        json.put("idCardNo",idnum);

        //producerService.sendCollectingDataMsg(json.toString(), MsgType.AUTHENTICATE_REAL_NAME);

        //保存邀请记录表的用户状态
        if (!isOrganCustomer(mainInfo.getCmIntroduceCode())) {//邀请人非机构用户
            CustomerMainInfo inviteCustomer = findOneByInviteCode(mainInfo.getCmIntroduceCode());
            if (inviteCustomer != null) {
                custInviteLineService.saveCustInviteLine(mainInfo, inviteCustomer, AppConstants.CustInviteLineStatusContants.VALID);
            }
        }
        return dto;
    }

    public boolean checkIdNum(String idnum) {
        return IdcardValidatorUtil.isValidatedAllIdcard(idnum);
    }

    public int getAgeByIdNum(String idnum) {
        int age = 0;
        try {
            age = IdcardValidatorUtil.getAge(idnum);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return age;
    }

    public void resetPassword(String cellphone, String password, String validateCode) {
        customerValidateCodeService.checkValidateCode(AppConstants.ValidateCode.RESET_PASSWORD, cellphone, validateCode);
        CustomerMainInfo mainInfo = checkExistByCellphone(cellphone);
        String cmLoginPassword = genLoginPassword(password.toUpperCase(), mainInfo.getCmSalt());
        updatepassword(cellphone, password, cmLoginPassword);
    }
    public void checkSmsCode(String cellphone, String validateCode) {
        checkSmsCode(cellphone, validateCode ,null);
        CustomerMainInfo mainInfo = checkExistByCellphone(cellphone);
    }

    /**
     * 重载检查验证码方法
     * @param cellphone 电话号码
     * @param validateCode 验证码
     * @param type 验证类型
     */
    public void checkSmsCode(String cellphone, String validateCode ,Integer type) {
        if (type != null){
            customerValidateCodeService.checkValidateCode(type, cellphone, validateCode);
        }else {
            customerValidateCodeService.checkValidateCode(AppConstants.ValidateCode.RESET_PASSWORD, cellphone, validateCode);
        }
    }

    public CustomerMainInfo wxBind(String cellphone, String openId, String password) {
        CustomerMainInfo customerMainInfo = checkExistByCellphone(cellphone);
        if (StringUtils.isNotEmpty(customerMainInfo.getOpenId())) {
            throw new BusinessException("wechat.open.bound");
        }
        checkLoginPassword(password, customerMainInfo.getCmSalt(), customerMainInfo.getCmLoginPassword());

        Map<String, Object> map = Maps.newTreeMap();
        map.put("customerId", customerMainInfo.getId());
        map.put("openId", openId);
        map.put("wxBindTime",new Date());
        getCustomerMainInfoMapper().bindWeichat(map);
        BindMessage message = new BindMessage();
        message.setCmNumber(customerMainInfo.getCmNumber());
        message.setCustomerId(customerMainInfo.getId());
        super.sendRocketMqMsg(BusiTypeEnum.BIND, message);

        //审计
        // businessReportService.sendReport(AuditMethodType.AUDIT_BIND_WX.getMethod(),message);
        //微信绑定数据
        /*JSONObject json = new JSONObject();
        json.put("cmNumber", customerMainInfo.getCmNumber());
        json.put("customerId", customerMainInfo.getId());
        json.put("bindDate", new Date());*/

        //producerService.sendCollectingDataMsg(json.toString(), MsgType.BIND_WECHAT);
        return customerMainInfo;
    }

    public int initCustomerData(String id) {
        Map<String, Object> params = Maps.newTreeMap();
        if (!StringUtil.isEmpty(id))
            params.put("id", id);

        //查询所有用户
        List<CustomerMainInfo> customerMainInfoList = getCustomerMainInfoMapper().getCustomerBySearchParam(params);

        int updateNum = 0;
        for (CustomerMainInfo mainInfoObj : customerMainInfoList) {
            //生成盐值
            mainInfoObj.setCmSalt(genSalt());
            //生成新密码
            //新密码为：原密码+盐值的第4-10位
            mainInfoObj.setCmLoginPassword(genLoginPassword(mainInfoObj.getCmPassword(), mainInfoObj.getCmSalt()));
            if (getCustomerMainInfoMapper().updateByPrimaryKey(mainInfoObj) > 0)
                updateNum++;
        }
        return updateNum;
    }

    //生成盐值
    public String genSalt() {
        String randStr = StringUtil.getRandomNumByLength(6);
        return MD5.MD5Encode(randStr).toUpperCase();
    }

    //生成登录密码
    public String genLoginPassword(String password, String salt) {
        password = StringUtils.upperCase(password);
        salt = StringUtils.upperCase(salt);
        if (salt.length() < 10)
            return "";
        return MD5.MD5Encode(password + salt.substring(4, 10)).toUpperCase();
    }


    public boolean validatePassword(String cellphone, String password) {
        CustomerMainInfo customerMainInfo = checkExistByCellphone(cellphone);
        try {
            checkLoginPassword(password, customerMainInfo.getCmSalt(), customerMainInfo.getCmLoginPassword());
            return true;
        } catch (BusinessException e) {
            return false;
        }
    }

    public CustomerMainInfo checkBind(String openId) {
        return getCustomerMainInfoMapper().checkBind(openId);
    }

    public CustomerMainInfo checkLoginOpenIdBind(String loginOpenId) {
        return getCustomerMainInfoMapper().checkLoginOpenIdBind(loginOpenId);
    }

    public boolean isLocked(Date date, Integer lhour) {
        if (date == null) {
            return false;
        }
        long ltimes = 1000 * 60 * 60 * lhour;
        Date nowDate = new Date();
        return nowDate.getTime() - date.getTime() <= ltimes;
    }

    public void setTradePassword(String cellphone, String password, String validateCode) throws Exception {
        CustomerMainInfo mainInfo = checkExistByCellphone(cellphone);
        //校验验证码
        customerValidateCodeService.checkValidateCode(AppConstants.ValidateCode.SET_PAY_PASSWORD, cellphone, validateCode);
        if (StringUtils.isNotEmpty(mainInfo.getPayPassword())) {
            throw new BusinessException("tradePassword.exist");
        }
        //得到加密后的交易密码
        String payPassword = genLoginPassword(password.toUpperCase(), mainInfo.getCmSalt());
        //更新交易密码
        getCustomerMainInfoMapper().updateTradePassword(cellphone, payPassword);
    }

    public ResetTradePasswordDTO changeTradePassword(String cellphone, String oldPassword, String newPassword) throws Exception {
        CustomerMainInfo mainInfo = checkExistByCellphone(cellphone);
        //判断是否设置过交易密码
        String payPassword = mainInfo.getPayPassword();
        if (StringUtils.isEmpty(payPassword)) {
            throw new BusinessException("tradePassword.unset");
        }
        int bnum = Integer.valueOf(sysParameterService.findOneByPrType("resetErrorTimes").getPrValue());
        int lhour = Integer.valueOf(sysParameterService.findOneByPrType("resetLockHour").getPrValue());
        //判断是否被锁定
        boolean isLocked = isLocked(mainInfo.getResetLockTime(), lhour);
        if (isLocked) {
            throw new BusinessException("tradePassword.error.lock", new Object[]{bnum, lhour});
        }
        String oldPwd = genLoginPassword(oldPassword, mainInfo.getCmSalt());
        String newPwd = genLoginPassword(newPassword, mainInfo.getCmSalt());
        if (StringUtils.equalsIgnoreCase(oldPwd, payPassword)) {
            getCustomerMainInfoMapper().updateTradePassword(cellphone, newPwd);
            //更新错误次数，错误时间
            getCustomerMainInfoMapper().updateResetLockInfos(cellphone, 0, null);
            sendTradePasswordChangeWxMsg(mainInfo);
            return null;
        } else {
            String errorMsg = "";
            ResetTradePasswordDTO dto = new ResetTradePasswordDTO();
            //更新错误次数，错误时间
            int s1 = mainInfo.getResetErrorTime();
            int s2 = 1;
            int s3 = (short) (s1 + s2);

            DateTime payErrorLastTime = new DateTime(mainInfo.getResetErrorLastTime() == null ? DateTime.now().toDate() : mainInfo.getResetErrorLastTime());
            Long payErrorLastTimeStr = Long.parseLong(payErrorLastTime.toString("yyyyMMdd"));
            Long now = Long.parseLong(DateTime.now().toString("yyyyMMdd"));
            if (now > payErrorLastTimeStr) {//清除隔天的密码错误记数
                s3 = 1;
            }
            errorMsg = PropertiesUtil.getDescrptionProperties().get("reset.pay_password.error");
            mainInfo.setResetErrorLastTime(DateTime.now().toDate());
            if (s3 >= bnum) {
                mainInfo.setResetErrorTime(0);
                DateTime nowDate = DateTime.now();
                mainInfo.setResetLockTime(nowDate.toDate());
                mainInfo.setResetErrorLastTime(nowDate.toDate());
                getCustomerMainInfoMapper().updateByPrimaryKey(mainInfo);
                errorMsg = PropertiesUtil.getDescrptionProperties().get("reset.pay_password.lock");
                errorMsg = MessageFormat.format(errorMsg, s3, bnum, lhour);

                dto.setErrorTime(s3);
                dto.setIsLock(true);
                dto.setMsg(errorMsg);
                return dto;
            } else {
                mainInfo.setResetErrorTime(s3);
                mainInfo.setResetErrorLastTime(DateTime.now().toDate());
                getCustomerMainInfoMapper().updateByPrimaryKey(mainInfo);
                errorMsg = MessageFormat.format(errorMsg, s3, bnum, lhour);
                dto.setErrorTime(s3);
                dto.setIsLock(false);
                dto.setMsg(errorMsg);
                return dto;
            }
        }
    }

    private void sendTradePasswordChangeWxMsg(CustomerMainInfo mainInfo) {
        //修改成功，若用户绑定过微信，则微信推送通知
        String openId = mainInfo.getOpenId();
        if (!StringUtils.isEmpty(openId)) {
            Map<String, String> map = Maps.newHashMap();
            map.put("keyword1", "交易密码");
            map.put("keyword2", DateTime.now().toString("yyyy-MM-dd HH:mm"));
            sendWxTemplateMsg(openId, Constants.MSG_TPL_ACCOUNT_CHANGED, map);
        }
    }

    public CustomerMainInfo resetTradePassword(String cellphone, String password, String idCard, String validateCode) throws Exception {
        CustomerMainInfo mainInfo = checkExistByCellphone(cellphone);
        if (3 != mainInfo.getCmStatus()) {
            throw new BusinessException("customer.not.auth");
        }
        String dbIdnum = mainInfo.getCmIdnum();
        idCard = idCard.toUpperCase();
        dbIdnum = dbIdnum.toUpperCase();
        if (!StringUtils.equals(idCard, dbIdnum)) {
            throw new BusinessException("idnum.error");
        }

        //校验验证码
        customerValidateCodeService.checkValidateCode(AppConstants.ValidateCode.RESET_PAY_PASSWORD, cellphone, validateCode);

        //得到加密后的交易密码
        String payPassword = genLoginPassword(password.toUpperCase(), mainInfo.getCmSalt());
        //重置交易密码
        getCustomerMainInfoMapper().updateTradePassword(cellphone, payPassword);

        //修改成功，若用户绑定过微信，则微信推送通知
        sendTradePasswordChangeWxMsg(mainInfo);
        return mainInfo;
    }

    public boolean logout(Long customerId, ReqHeadParam headParam) {
        if (StringUtils.isEmpty(headParam.getSessionToken())) {
            return false;
        }
        LoginEntry entry = new LoginEntry();
        String userNo = customerId.toString();
        String deviceNo = headParam.getToken();
        String reqTimestamp = Long.toString(System.currentTimeMillis());//时间戳
        String sn = AppConstants.HttpHead.PROJECTNO + userNo + UUID.randomUUID().toString().substring(0, 8);//流水号
        String validateCode = AppConstants.HttpHead.PROJECTNO + userNo + deviceNo + reqTimestamp + sn + configParamBean.getPushKey();
        String sign = com.zdmoney.utils.MD5.MD5Encode(validateCode);//校验码
        entry.setProjectNo(AppConstants.HttpHead.PROJECTNO);
        entry.setUserNo(userNo);
        entry.setDeviceNo(deviceNo);
        entry.setReqTimestamp(reqTimestamp);
        entry.setSn(sn);
        entry.setSign(sign);
        log.info("注销json:" + JSON.toJSONString(entry));
        return customerSessionService.cleanToken(LaocaiUtil.getLoginType(headParam), headParam.getSessionToken(),customerId);
    }

    public CustomerMainInfo findOneBySessionToken(String userToken) {
        String cmNumber = LaocaiUtil.sessionToken2CmNumber(userToken, configParamBean.getUserTokenKey());
        return checkExistByCmNumber(cmNumber);
    }

    /**
     * 注册+绑定微信
     *
     * @param cellphone
     * @param password
     * @param validateCode
     * @param openId
     * @param deviceId
     * @param headParam
     * @return
     */
    @Transactional
    public Result wechatRegist(String cellphone, String password, String validateCode, String openId, String deviceId, String clientId, ReqHeadParam headParam, String ip) throws Exception {
        //校验验证码
        customerValidateCodeService.checkValidateCode(AppConstants.ValidateCode.WECHAT_REGISTER, cellphone, validateCode);
        //校验openId是否已经绑定
        if (checkLoginOpenIdBind(openId) != null) {
            throw new BusinessException("wechat.login.bound");
        }
        //校验手机号码是否已被注册
        CustomerMainInfo mainInfo = findOneByPhone(cellphone);
        if (mainInfo != null) {//手机号已经存在
            if (StringUtils.isNotEmpty(mainInfo.getLoginOpenId())) {
                throw new BusinessException("cellphone.bound");
            }
            checkLoginPassword(password, mainInfo.getCmSalt(), mainInfo.getCmLoginPassword());

            //判断密码是否相同
            if (mainInfo.getCmStatus() == AppConstants.CmStatus.FROZEN_MEMBER) {
                throw new BusinessException(AppConstants.LoginBack.ERROR_DATA_3);//返回会员冻结
            } else {
                mainInfo.setLoginOpenId(openId);//绑定微信
                update(mainInfo);
                AuthMessage message = new AuthMessage();
                message.setCmNumber(mainInfo.getCmNumber());
                message.setCustomerId(mainInfo.getId());
                sendRocketMqMsg(BusiTypeEnum.BIND, message);
            }
        } else {
            mainInfo = register(cellphone, password, null, null, openId, headParam, ip,null,null,null);
            AuthMessage message = new AuthMessage();
            message.setCmNumber(mainInfo.getCmNumber());
            message.setCustomerId(mainInfo.getId());
            sendRocketMqMsg(BusiTypeEnum.BIND, message);
        }
        CustomerDTO dto = loginAndDecorate(LaocaiUtil.getLoginType(headParam), cellphone, password, deviceId, clientId, headParam);
        // 微信推送
        if (StringUtils.isNotEmpty(openId)) {
            Map<String, String> map = Maps.newTreeMap();
            map.put("name1", "捞财宝");
            map.put("name2", "微信");
            map.put("time", DateTime.now().toString("yyyy-MM-dd HH:mm"));
            sendWxTemplateMsg(mainInfo.getOpenId(), Constants.MSG_TPL_ACCOUNT_BIND, map);
        }
        return Result.success(dto);
    }

    @Transactional(rollbackFor = Exception.class)
    public String marchantRegistor(String cellPhone, String password, String openId, String merchantCode, String merchantName, ReqHeadParam reqHeadParam, String ip) {
        try {
            CustomerMainInfo mainInfo = register(cellPhone, password, "", "", "", reqHeadParam, ip,null,null,null);
            mainInfo.setOpenId(openId);
            updateNotNull(mainInfo);
            //增加商户注册记录
            MerchantRegisterRecord merchantRegisterRecord = new MerchantRegisterRecord();
            merchantRegisterRecord.setCustomerNo(mainInfo.getCmNumber());
            merchantRegisterRecord.setMerchantNo(merchantCode);
            merchantRegisterRecord.setMerchantType(AppConstants.MerchantType.MERCHANT);
            merchantRegisterRecord.setRegisterDate(new Date());
            String vCode = "";
            while (StringUtils.isEmpty(vCode)) {
                vCode = NumberUtil.randomString(6);
                vCode = merchantRegisterRecordService.checkUniqueMerchantValidCode(merchantCode, vCode) ? "" : vCode;
            }
            merchantRegisterRecord.setValidCode(vCode);
            merchantRegisterRecordService.save(merchantRegisterRecord);

            BindMessage message = new BindMessage();
            message.setCmNumber(mainInfo.getCmNumber());
            message.setCustomerId(mainInfo.getId());
            sendRocketMqMsg(BusiTypeEnum.BIND, message);

            Map<String, String> tplmap = Maps.newTreeMap();
            tplmap.put("keyword1", merchantCode + "(" + merchantName + ")");
            tplmap.put("keyword2", cellPhone);
            tplmap.put("keyword3", mainInfo.getId().toString());
            tplmap.put("keyword4", DateUtil.timeFormat(mainInfo.getCmInputDate(), "yyyy-MM-dd HH:mm:ss"));
            sendWxTemplateMsg(openId, Constants.MSG_TPL_MERCHANT_REGISTER_SUCCESS, tplmap);

            return vCode;
        } catch (BusinessException e) {
            log.error("商户用户注册失败：{}", e.getMessage());
            throw new BusinessException("注册失败");
        }
    }

    /**
     * 验证用户是否正常公共方法
     */
    public CustomerMainInfo validateCustomerInfo(Long customerId) {
        CustomerMainInfo customerMainInfo = getCustomerMainInfoMapper().selectByPrimaryKey(customerId);
        if (customerMainInfo == null) {
            throw new BusinessException("用户信息不存在");
        }
        if (AppConstants.CmStatus.MEMBER_AUTHEN_SUCCESS != customerMainInfo.getCmStatus()) {
            throw new BusinessException("用户没有实名认证！");
        }
        if (AppConstants.CustomerValidStatus.CUSTOMER_INVALID == (customerMainInfo.getCmValid())) {
            throw new BusinessException("用户已失效");
        }
        return customerMainInfo;
    }

    /**
     * 根据介绍码查询上家或上上家是否为理财师
     */
    public String queryIsAdvisor(String introduceCode) {
        String advisor = "";
        CustomerMainInfo customer1 = getCustomerMainInfoMapper().validateIntroduceCode(introduceCode);
        if (customer1 != null) {
            if (AppConstants.MemberType.ADVISOR.equals(customer1.getMemberType())) {
                advisor = customer1.getCmInviteCode();
            }
            if (AppConstants.MemberType.ADVISOR_MEMBER.equals(customer1.getMemberType())) {
//                CustomerMainInfo customer2 = getCustomerMainInfoMapper().validateIntroduceCode(customer1.getCmIntroduceCode());
//                if(customer2!=null){
//                    if(AppConstants.MemberType.ADVISOR.equals(customer2.getMemberType())){
//                        advisor = customer2.getCmInviteCode();
//                    }
//                }
                if (customer1.getPlannerInviteCode().equals(customer1.getCmIntroduceCode())) {
                    advisor = customer1.getPlannerInviteCode();
                }
            }
        }
        return advisor;
    }

    public void checkCustomerInfo(Long customerId, String validateCode, String idNumber, String validateType, String payPassword) {
        CustomerMainInfo customerInfo = checkCustomerId(customerId);
        if (ParamConstant.VALIDATE_TYPE_1.equals(validateType)) {
            customerValidateCodeService.checkValidateCode(AppConstants.ValidateCode.CELLPHONE_MODIFY, customerInfo.getCmCellphone(), validateCode);
        } else if (ParamConstant.VALIDATE_TYPE_2.equals(validateType)) {
            //判断是否设置过交易密码
            String innerPayPassword = customerInfo.getPayPassword();
            if (StringUtils.isEmpty(payPassword)) {
                throw new BusinessException("tradePassword.unset");
            }
            int bnum = Integer.valueOf(sysParameterService.findOneByPrType("resetErrorTimes").getPrValue());
            int lhour = Integer.valueOf(sysParameterService.findOneByPrType("resetLockHour").getPrValue());
            //判断是否被锁定
            boolean isLocked = isLocked(customerInfo.getMobileLockTime(), lhour);
            if (isLocked) {
                throw new BusinessException("tradePassword.error.lock", new Object[]{bnum, lhour});
            }
            payPassword = genLoginPassword(payPassword.toUpperCase(), customerInfo.getCmSalt());
            if (!StringUtils.equalsIgnoreCase(payPassword, innerPayPassword)) {
                //更新错误次数，错误时间
                AtomicInteger errorTimes = new AtomicInteger(customerInfo.getMobileErrorTimes());
                DateTime mobileErrorLastTime = new DateTime(customerInfo.getMobileLastTime() == null ? DateTime.now().toDate() : customerInfo.getMobileLastTime());
                Long payErrorLastTimeStr = Long.parseLong(mobileErrorLastTime.toString("yyyyMMdd"));
                Long now = Long.parseLong(DateTime.now().toString("yyyyMMdd"));
                if (now > payErrorLastTimeStr) {//清除隔天的密码错误记数
                    errorTimes.set(0);
                }
                customerInfo.setMobileLastTime(DateTime.now().toDate());
                if (errorTimes.incrementAndGet() >= bnum) {
                    customerInfo.setMobileErrorTimes(0);
                    DateTime lockTime = DateTime.now();
                    customerInfo.setMobileLockTime(lockTime.toDate());
                    customerInfo.setMobileLastTime(DateTime.now().toDate());
                    getCustomerMainInfoMapper().updateByPrimaryKey(customerInfo);
                    throw new BusinessException("tradePassword.error.lock", new Object[]{bnum, lhour});
                } else {
                    customerInfo.setMobileErrorTimes(errorTimes.get());
                    customerInfo.setMobileLastTime(DateTime.now().toDate());
                    getCustomerMainInfoMapper().updateByPrimaryKey(customerInfo);
                    throw new BusinessException("tradePassword.error", new Object[]{errorTimes.get(), bnum, lhour});
                }
            }
        } else {
            throw new BusinessException("validateType.error");
        }
        if (!idNumber.equals(customerInfo.getCmIdnum())) {
            throw new BusinessException("idnum.error");
        }
    }

    /**
     * 根据用户ID查找用户
     *
     * @param customerId 用户编号
     * @return 用户实体
     */
    public CustomerMainInfo findOneByCustomerId(Long customerId) {
        CustomerMainInfo mainInfo = this.checkCustomerId(customerId);
        if (0 != mainInfo.getCmValid()) {
            throw new BusinessException("customer.not.exist");
        }
        return mainInfo;
    }

    @Transactional
    public void changeCellphone(Long customerId, String validateCode, final String cellphone) {
        CustomerMainInfo customerInfo = checkCustomerId(customerId);
        customerValidateCodeService.checkValidateCode(AppConstants.ValidateCode.CELLPHONE_MODIFY, cellphone, validateCode);
        //验证新手机是否已经存在
        checkNotRegisterByCellphone(cellphone);
        final String orgMobile = customerInfo.getCmCellphone();
        //修改组队信息手机号
        TeamMemberInfo memberInfo = new TeamMemberInfo();
        memberInfo.setMemberId(customerId);
        memberInfo.setMemberCellphone(cellphone);
        teamMemberInfoMapper.updateCellphoneByMemberId(memberInfo);
        //解除手机号绑定
        bankCardService.unBindPhone(customerInfo);
        //修改用户手机号
        customerInfo.setCmCellphone(cellphone);
        customerInfo.setCmModifyDate(new Date());
        getCustomerMainInfoMapper().updateByPrimaryKey(customerInfo);
        //异步通知标的
        final String customerNo = customerInfo.getCmNumber();

        ExecutorService executor = Executors.newFixedThreadPool(2);
        executor.submit(new LcbTraceRunnable() {
            @Override
            public void concreteRun() {
                try {
                    SignCustomerChangeReqDto reqDto = new SignCustomerChangeReqDto(customerNo, cellphone, configParamBean.getSubjectPartnerNo());
                    AssetsResultDto<SignCustomerChangeResDto> resultDto = lcbSubjectFacadeService.signChangeCustomerInfo(reqDto);
                    if (!resultDto.isSuccess()) {
                        log.error("通知标的手机号修改失败,原手机号" + orgMobile + ",新手机号" + cellphone + ",失败原因" + resultDto.getMsg());
                        MailUtil.sendMail("PC修改手机号通知标的失败", "通知标的手机号修改失败,原手机号" + orgMobile + ",新手机号" + cellphone + ",失败原因" + resultDto.getMsg());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    log.error("通知标的手机号修改发生异常,原手机号" + orgMobile + ",新手机号" + cellphone + ",异常原因" + e.getMessage());
                    MailUtil.sendMail("PC修改手机号通知标的异常", "通知标的手机号修改发生异常,原手机号" + orgMobile + ",新手机号" + cellphone + ",异常原因" + e.getMessage());
                }
            }
        });
        //清除Session,强制用户退出
        try {
//            customerSessionService.cleanSessionByLike(orgMobile);
            customerSessionService.cleanSessionById(customerId);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("清除Session,强制用户退出失败" + e.getMessage());
        }
    }

    /**
     * 接触绑定
     * 安全退出
     *
     * @param customerId
     * @return
     */
    public int unBind(Long customerId) {
        return getCustomerMainInfoMapper().unBind(customerId);
    }

    /**
     * 回退用户首次购买状态
     *
     * @param customerMainInfo
     * @param isFirst
     */
    public void refundCustomerFirst(CustomerMainInfo customerMainInfo, boolean isFirst) {
        if (isFirst && customerMainInfo.getIsConsumed().equals(1L)) {
            log.info(">>>>>>>>>>回退用户新手状态开始，用户ID：【{}】", customerMainInfo.getId());
            customerMainInfo.setIsConsumed(0L);
            customerMainInfo.setFirstConsumeDate(null);
            customerMainInfo.setCmModifyDate(new Date());
            int num = getCustomerMainInfoMapper().updateByPrimaryKey(customerMainInfo);
            if (num == 1) {
                log.info(">>>>>>>>>>回退用户新手状态结束，用户ID：【{}】", customerMainInfo.getId());
            } else {
                log.info(">>>>>>>>>>回退用户新手状态失败，用户ID：【{}】", customerMainInfo.getId());
                throw new BusinessException(AppConstants.LoginBusiOrder.BUSIORDER_ERROR_16);
            }
        }
    }

    public void updateCustomerRiskTestType(String riskTestType, Long customerId){
        getCustomerMainInfoMapper().updateCustomerRiskTestType(riskTestType, customerId);
    }

    /**
     * 校验银行卡 卡bin信息
     * @param bankCode 银行编码
     * @param bankCard 银行卡号
     */
    public void checkCardBinInfo(String bankCode, String bankCard) {
        if (org.apache.commons.lang.StringUtils.isBlank(bankCode)) {
            throw new BusinessException("银行代码为空！");
        }
        BusiPayBankLimit payBankLimit = new BusiPayBankLimit();
        payBankLimit.setCode(bankCode);
        payBankLimit.setPayChannel(AppConstants.PayChannelCodeContants.LIANLIAN);
        List<BusiPayBankLimit> limits = busiPayBankLimitMapper.select(payBankLimit);
        if (limits.isEmpty()) {
            log.error("查询连连银行代码失败：code=" + bankCode);
            throw new BusinessException("银行转换失败，如有疑问请联系管理员！");
        }
        bankCode = limits.get(0).getBankCode();
        boolean isCheck = busiPayChannelService.checkCardBin(bankCard, bankCode);
        if (!isCheck) {
            log.error("校验银行卡失败:code=" + bankCode + ",bankCard=" + bankCard);
            throw new BusinessException("校验银行卡不通过！");
        }
    }

    public SpecialFinancialPlannerVO querySpecialFinancialPlannerInfo(String cmNumber){
        return getCustomerMainInfoMapper().querySpecialFinancialPlannerInfo(cmNumber);
    }

    public PageResultDto getCustomerBySearchParamNew(CustomerInfoDto customerInfoVo){
        Map<String, Object> objectMap = CommonHelper.transBean2Map(customerInfoVo);
        int pageSize = customerInfoVo.getPageSize();
        Page<CustomerMainInfoVo> page =new Page<>();
        page.setPageNo(customerInfoVo.getPageNo());
        page.setPageSize(pageSize);
        objectMap.put("page",page);
        List<CustomerMainInfoVo> customerList = getCustomerMainInfoMapper().getCustomerBySearchParamNew(objectMap);
        return new PageResultDto(customerList,page.getTotalRecord(), pageSize);
    }

    public List<com.zdmoney.vo.CustomerMainInfoVo> getCustomerInfo(CustomerMainInfoDto customerMainInfoDto) {
        PageHelper.startPage(customerMainInfoDto.getPageNo(),customerMainInfoDto.getPageSize());
        return getCustomerMainInfoMapper().getCustomerInfo(customerMainInfoDto);
    }

    public CustomerMainInfo validCodeLogin(String cellphone, String validateCode){
        CustomerMainInfo customerMainInfo = checkExistByCellphone(cellphone);
        customerValidateCodeService.checkValidateCode(AppConstants.ValidateCode.CREDIT, cellphone, validateCode);
        return customerMainInfo;
    }

    public long saveCustomerAuthorize(CustomerAuthorizeInfo customerAuthorizeInfo){
        return customerAuthorizeMapper.saveCustomerAuthorizeInfo(customerAuthorizeInfo);
    }

    /**
     * 查询用户是否授权
     * @param customerId
     * @return
     */
    public boolean isAuthorize(long customerId) {
        boolean isAuthorize = true;
        CustomerAuthorizeInfo authorizeInfo = customerAuthorizeMapper.queryCustomerAuthorizeInfo(customerId);
        if (authorizeInfo == null || !"1".equals(authorizeInfo.getAuthorizeStatus())){
            isAuthorize = false;
        }
        return isAuthorize;
    }

    public CustomerMainInfo selectBankCodeByIdNum(String borrowerIdNum) {
        CustomerMainInfo mainInfo = new CustomerMainInfo();
        mainInfo.setCmIdnum(borrowerIdNum);
        mainInfo.setCmValid(0);
        return getCustomerMainInfoMapper().selectOne(mainInfo);
    }

    @Transactional
    public void changePhoneNo(CustomerMainInfo customer,String phoneNo){
        CustomerMainInfo tmp = new CustomerMainInfo();
        tmp.setId(customer.getId());
        tmp.setCmCellphone(phoneNo);
        getCustomerMainInfoMapper().updateByCustomerId(tmp);//更新用户手机号
        BusiUnbindRecord unbindRecord = new BusiUnbindRecord();
        unbindRecord.setOperMan("admin");
        unbindRecord.setOperTime(new Date());
        unbindRecord.setRemark("admin");
        unbindRecord.setUbIdnum(customer.getCmIdnum());
        unbindRecord.setUbRealName(customer.getCmRealName());
        unbindRecord.setUbTelephone(customer.getCmCellphone());
        unbindRecord.setUnType("2");
        busiUnbindRecordMapper.insertBusiUnbindRecord(unbindRecord);//插入手机号解绑记录
    }

    @Transactional
    public void deactivateAccount(CustomerMainInfo customer){
        CustomerMainInfo tmp = new CustomerMainInfo();
        tmp.setId(customer.getId());
        tmp.setCmCellphone(customer.getCmCellphone()+"x");
        tmp.setCmValid(1);
        tmp.setCmIdnum(customer.getCmIdnum() != null ? customer.getCmIdnum()+"x":null);
        getCustomerMainInfoMapper().updateByCustomerId(tmp);//更新用户状态等，即注销用户
    }

    public List<SpecialFinancialPlannerVO> querySpecialFinancialPlannerInfoList(Map map){
        return getCustomerMainInfoMapper().querySpecialFinancialPlannerInfoList(map);
    }

    /**
     * 根据富友loginId查询用户信息
     * @param loginId
     * @return
     */
    public CustomerMainInfo queryCustomerInfoByLoginId(String loginId) {
        CustomerMainInfo mainInfo = getCustomerMainInfoMapper().queryCustomerInfoByLoginId(loginId);
        if (mainInfo == null) {
            throw new BusinessException("customer.not.exist");
        }
        return mainInfo;
    }

    public Page<CustomerGrantInfoDTO> searchCustomerGrantInfo(CustomerGrantInfoSearchDTO searchDTO){
        Map<String,Object> map = new HashMap<>();
        map.put("cmNumber", searchDTO.getCmNumber());
        map.put("cmCellphone", searchDTO.getCmCellphone());
        map.put("cmRealName", searchDTO.getCmRealName());
        map.put("cmIdnum", searchDTO.getCmIdnum());
        map.put("grantStatus", searchDTO.getGrantStatus());
        map.put("accountType", searchDTO.getAccountType());

        Page<CustomerGrantInfoDTO> page = new Page<>();
        page.setPageNo(searchDTO.getPageNo());
        page.setPageSize(searchDTO.getPageSize());
        map.put("page", page);
        List<CustomerGrantInfoDTO> data = getCustomerMainInfoMapper().searchCustomerGrantInfo(map);
        page.setResults(data);
        return page;
    }

    /**
     * 获取用户注册记录
     *
     * @param map
     * @return
     */
    public PageResultDto<RegisterNotifyDto> getCustomerInfoByRegistNotify(Map<String, Object> map) {
        PageResultDto<RegisterNotifyDto> resultDto = new PageResultDto<>();
        if (map.get("pageNo") == null || map.get("pageSize") == null) {
            resultDto.setCode("1111");
            resultDto.setMsg("分页参数丢失");
            return resultDto;
        }
        Page<RegisterNotifyDto> page = new Page<>();
        page.setBaseParam(map);
        String startDate = (String) map.get("startDate");
        String endDate = (String) map.get("endDate");

        if (StringUtils.isBlank(startDate) && StringUtils.isBlank(endDate)) {
            map.put("startDate", DateUtil.getDateFormatString(DateUtil.getDateBefore(new Date(), 7), DateUtil.fullFormat));
        }
        map.put("page", page);
        try {
            List<RegisterNotifyDto> recordList = getCustomerMainInfoMapper().queryRegisterNotify(map);
            resultDto.setPageNo(page.getPageNo());
            resultDto.setTotalPage(page.getTotalPage());
            resultDto.setTotalSize(page.getTotalRecord());
            resultDto.setDataList(recordList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultDto.setCode("1111");
            resultDto.setMsg("查询数据发生异常");
        }
        return resultDto;
    }

    /**
     * 获取用户实名列表
     *
     * @param map
     * @return
     */
    public PageResultDto<NameCertificateNotifyDto> getNameCertificateNotify(Map<String, Object> map) {
        PageResultDto<NameCertificateNotifyDto> resultDto = new PageResultDto<>();
        if (map.get("pageNo") == null || map.get("pageSize") == null) {
            resultDto.setCode("1111");
            resultDto.setMsg("分页参数丢失");
            return resultDto;
        }
        Page<NameCertificateNotifyDto> page = new Page<>();
        page.setBaseParam(map);
        String startDate = (String) map.get("startDate");
        String endDate = (String) map.get("endDate");

        if (StringUtils.isBlank(startDate) && StringUtils.isBlank(endDate)) {
            map.put("startDate", DateUtil.getDateFormatString(DateUtil.getDateBefore(new Date(), 7), DateUtil.fullFormat));
        }
        map.put("page", page);
        try {
            List<NameCertificateNotifyDto> recordList = getCustomerMainInfoMapper().queryNameCertificateNotify(map);
            //设置resultDto的成员变量
            resultDto.setCode("0000");
            resultDto.setPageNo(page.getPageNo());
            resultDto.setTotalPage(page.getTotalPage());
            resultDto.setTotalSize(page.getTotalRecord());
            resultDto.setDataList(recordList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultDto.setCode("1111");
            resultDto.setMsg("查询数据发生异常");
        }
        return resultDto;
    }

    /**
     * 获取用户微信绑定列表
     *
     * @param map
     * @return
     */
    public PageResultDto<RecordDto> getWeChatBindNotify(Map<String, Object> map) {
        PageResultDto<RecordDto> resultDto = new PageResultDto<>();
        if (map.get("pageNo") == null || map.get("pageSize") == null) {
            resultDto.setCode("1111");
            resultDto.setMsg("分页参数丢失");
            return resultDto;
        }
        Page<RecordDto> page = new Page<>();
        page.setBaseParam(map);
        String startDate = (String) map.get("startDate");
        String endDate = (String) map.get("endDate");

        if (StringUtils.isBlank(startDate) && StringUtils.isBlank(endDate)) {
            map.put("startDate", DateUtil.getDateFormatString(DateUtil.getDateBefore(new Date(), 7), DateUtil.fullFormat));
        }
        map.put("page", page);
        try {
            List<RecordDto> recordList = getCustomerMainInfoMapper().queryWeChatBindNotify(map);
            //设置resultDto的成员变量
            resultDto.setCode("0000");
            resultDto.setPageNo(page.getPageNo());
            resultDto.setTotalPage(page.getTotalPage());
            resultDto.setTotalSize(page.getTotalRecord());
            resultDto.setDataList(recordList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultDto.setCode("1111");
            resultDto.setMsg("查询数据发生异常");
        }
        return resultDto;
    }

    /**
     * 获取用户销户列表
     *
     * @param map
     * @return
     */
    public PageResultDto<RecordDto> getCloseAccountInfo(Map<String, Object> map) {
        PageResultDto<RecordDto> resultDto = new PageResultDto<>();
        if (map.get("pageNo") == null || map.get("pageSize") == null) {
            resultDto.setCode("1111");
            resultDto.setMsg("分页参数丢失");
            return resultDto;
        }
        Page<RecordDto> page = new Page<>();
        page.setBaseParam(map);
        String startDate = (String) map.get("startDate");
        String endDate = (String) map.get("endDate");

        if (StringUtils.isBlank(startDate) && StringUtils.isBlank(endDate)) {
            map.put("startDate", DateUtil.getDateFormatString(DateUtil.getDateBefore(new Date(), 7), DateUtil.fullFormat));
        }
        map.put("page", page);
        try {
            List<RecordDto> recordList = getCustomerMainInfoMapper().queryCloseAccount(map);
            //设置resultDto的成员变量
            resultDto.setCode("0000");
            resultDto.setPageNo(page.getPageNo());
            resultDto.setTotalPage(page.getTotalPage());
            resultDto.setTotalSize(page.getTotalRecord());
            resultDto.setDataList(recordList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultDto.setCode("1111");
            resultDto.setMsg("查询数据发生异常");
        }
        return resultDto;
    }

    /**
     * 获取解绑手机号列表
     *
     * @param map
     * @return
     */
    public PageResultDto<BusiUnbindRecordDto> queryPhoneBindModify(Map<String, Object> map) {
        PageResultDto<BusiUnbindRecordDto> resultDto = new PageResultDto<>();
        if (map.get("pageNo") == null || map.get("pageSize") == null) {
            resultDto.setCode("1111");
            resultDto.setMsg("分页参数丢失");
            return resultDto;
        }
        Page<BusiUnbindRecordDto> page = new Page<>();
        page.setBaseParam(map);
        String startDate = (String) map.get("startDate");
        String endDate = (String) map.get("endDate");

        if (StringUtils.isBlank(startDate) && StringUtils.isBlank(endDate)) {
            map.put("startDate", DateUtil.getDateFormatString(DateUtil.getDateBefore(new Date(), 7), DateUtil.fullFormat));
        }
        map.put("page", page);
        try {
            List<BusiUnbindRecordDto> recordList = getCustomerMainInfoMapper().queryPhoneBindModify(map);
            //设置resultDto的成员变量
            resultDto.setCode("0000");
            resultDto.setPageNo(page.getPageNo());
            resultDto.setTotalPage(page.getTotalPage());
            resultDto.setTotalSize(page.getTotalRecord());
            resultDto.setDataList(recordList);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            resultDto.setCode("1111");
            resultDto.setMsg("查询数据发生异常");
        }
        return resultDto;
    }

    public CustomerMainInfo queryByPhoneAndIdNum(String phone, String idNum) {
        CustomerMainInfo mainInfo = getCustomerMainInfoMapper().selectByIdNumAndPhone(phone);
        if (mainInfo == null) {
            throw new BusinessException("customer.not.exist");
        }
        Boolean isAuth = StringUtils.isNotEmpty(mainInfo.getCmIdnum()) && 3 == mainInfo.getCmStatus();
        if(isAuth){
            if(!mainInfo.getCmIdnum().equalsIgnoreCase(idNum)){
                throw new BusinessException("customer.not.exist");
            }
        }else {
            throw new BusinessException("customer.not.auth");
        }
        return mainInfo;
    }

    /**
     * 获得债券转让成功的用户
     * @param map
     * @return
     */
    public List<CustomerMainInfo> getCustomerForDebtTransfer(Map<String, Object> map) {
        return getCustomerMainInfoMapper().getCustomerForDebtTransfer(map);
    }
}