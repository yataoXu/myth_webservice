/*
* Copyright (c) 2016 zendaimoney.com. All Rights Reserved.
*/
package websvc.servant.impl;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.zdmoney.assets.api.common.dto.AssetsResultDto;
import com.zdmoney.assets.api.dto.subject.borrow.BorrowerInfoExtendReqDto;
import com.zdmoney.assets.api.dto.subject.borrow.BorrowerInfoExtendResDto;
import com.zdmoney.assets.api.facade.subject.ILCBSubjectFacadeService;
import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.common.Result;
import com.zdmoney.common.anno.FunctionId;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.enm.SerialNoType;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.integral.api.common.dto.ResultDto;
import com.zdmoney.integral.api.dto.account.IntegralGiveDto;
import com.zdmoney.integral.api.dto.account.IntegralResDto;
import com.zdmoney.integral.api.dto.cash.CashDto;
import com.zdmoney.integral.api.dto.cash.enm.CashPublishSource;
import com.zdmoney.integral.api.facade.ICashFacadeService;
import com.zdmoney.integral.api.facade.IIntegralAccountFacadeService;
import com.zdmoney.integral.api.utils.DateUtils;
import com.zdmoney.mapper.BusiCashRecordMapper;
import com.zdmoney.mapper.CustomerRatingConfigMapper;
import com.zdmoney.mapper.bank.BusiBankLimitMapper;
import com.zdmoney.mapper.bank.CustomerBankAccountMapper;
import com.zdmoney.mapper.customer.CustomerMainInfoMapper;
import com.zdmoney.mapper.payChannel.BusiPayChannelMapper;
import com.zdmoney.mapper.product.BusiProductMapper;
import com.zdmoney.mapper.task.BusiTaskMapper;
import com.zdmoney.mapper.welfare.BusiWelfareRuleMapper;
import com.zdmoney.models.*;
import com.zdmoney.models.bank.BusiBankLimit;
import com.zdmoney.models.bank.CustomerBankAccount;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.customer.CustomerSign;
import com.zdmoney.models.payChannel.BusiPayChannel;
import com.zdmoney.models.sys.SysParameter;
import com.zdmoney.models.task.BusiTask;

import com.zdmoney.service.*;
import com.zdmoney.service.customer.CustomerAddressService;
import com.zdmoney.service.customer.CustomerSignInfoService;
import com.zdmoney.service.welfare.WelfareService;
import com.zdmoney.utils.*;
import com.zdmoney.vo.UserUnReceiveAsset;
import com.zdmoney.web.dto.*;
import com.zdmoney.webservice.api.dto.welfare.BusiWelfareRuleDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import websvc.models.*;
import websvc.req.ReqMain;
import websvc.servant.UserFunctionService;

import java.math.BigDecimal;
import java.text.Collator;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * UserFunctionServiceImpl
 * <p/>
 * Author: Hao Chen
 * Date: 2016-03-25 10:28
 * Mail: haoc@zendaimoney.com
 */
@Service
@Slf4j
public class UserFunctionServiceImpl implements UserFunctionService {

    @Autowired
    private CustomerMainInfoService customerMainInfoService;

    @Autowired
    private CustomerSignService customerSignService;

    @Autowired
    private BusiRiskAssessService riskAssessService;

    @Autowired
    private ConfigParamBean configParamBean;

    @Autowired
    private BusiTaskMapper busiTaskMapper;

    @Autowired
    private CustomerBankAccountMapper customerBankAccountMapper;

    @Autowired
    private BankCardService bankCardService;

    @Autowired
    private BusiPayChannelMapper busiPayChannelMapper;

    @Autowired
    private BusiBankLimitMapper busiBankLimitMapper;

    @Autowired
    private SysParameterService sysParameterService;

    @Autowired
    private CustomerSignInfoService customerSignInfoService;

    @Autowired
    private CustomerBorrowInfoService customerBorrowInfoService;

    @Autowired
    private BusiProductMapper busiProductMapper;

    @Autowired
    private BusiHandleBorrowCertificateService busiHandleBorrowCertificateService;

    @Autowired
    private WelfareService welfareService;

    @Autowired
    private IIntegralAccountFacadeService integralAccountFacadeService;

    @Autowired
    private AccountOverview520003Service accountOverview520003Service;

    @Autowired
    private CustomerRatingConfigMapper customerRatingConfigMapper;

    @Autowired
    private BusiWelfareRuleMapper busiWelfareRuleMapper;

    @Autowired
    private ICashFacadeService cashFacadeService;

    @Autowired
    private BusiCashRecordMapper cashRecordMapper;

    @Autowired
    private CustomerRatingConfigService customerRatingConfigService;
    @Autowired
    private BusiWelfareRuleMapper busiWelfareMapper;

    @Autowired
    private ILCBSubjectFacadeService ilcbSubjectFacadeService;

    @Autowired
    private CustomerMainInfoMapper customerMainInfoMapper;



    @FunctionId("400002")
    @Override
    public Result login(final ReqMain reqMain) throws Exception {
        Model_400002 cdtModel = (Model_400002) reqMain.getReqParam();
        final String cellphone = StringUtils.trim(cdtModel.getCmCellphone());
        final String password = StringUtils.trim(cdtModel.getCmPassword());
        final String deviceId = cdtModel.getDeviceId();
        final String clientId = cdtModel.getClientId();
        CustomerDTO dto = customerMainInfoService.loginAndDecorate(LaocaiUtil.getLoginType(reqMain.getReqHeadParam()), cellphone, password, deviceId, clientId, reqMain.getReqHeadParam());
        return Result.success(dto);
    }


    @FunctionId("400003")
    @Override
    public Result register(ReqMain reqMain) throws Exception {
        Model_400003 cdtModel = (Model_400003) reqMain.getReqParam();
        String cmCellphone = StringUtils.trim(cdtModel.getCmCellphone());
        String cmPassword = StringUtils.trim(cdtModel.getCmPassword());
        String validateCode = StringUtils.trim(cdtModel.getValidateCode());
        String introduceCode = StringUtils.trim(cdtModel.getCmIntroduceCode());
        String redNo = StringUtils.trim(cdtModel.getRedNo());
        String ip = subIp(cdtModel.getIp());
        customerMainInfoService.registerWithValidateCode(cmCellphone, cmPassword, validateCode, introduceCode, redNo, null, reqMain.getReqHeadParam(), ip,cdtModel);
        Integral integral = new Integral();
        return Result.success("register.success.reward", integral);
    }

    /**
     * 对其他系统传过来的ip进行处理,防止多个ip导致数据库宝库
     * @param ip
     * @return
     */
    private String subIp(String ip){
        if (StrUtil.isNotBlank(ip)){
            return StrUtil.split(StrUtil.trim(ip), ",")[0];
        }
        return "";
    }

    @FunctionId("400004")
    @Override
    public Result changePassword(ReqMain reqMain) throws Exception {
        Model_400004 cdtModel = (Model_400004) reqMain.getReqParam();
        String cellphone = StringUtils.trim(cdtModel.getCmCellphone());
        String oldPassword = StringUtils.trim(cdtModel.getCmOldPassword());
        String newPassword = StringUtils.trim(cdtModel.getCmNewPassword());
        if (oldPassword.equals(newPassword)) {
            throw new BusinessException("password.equal");
        }
        customerMainInfoService.changePassword(cellphone, oldPassword, newPassword);
        return Result.success();
    }

    @FunctionId("400005")
    @Override
    public Result resetPassword(ReqMain reqMain) throws Exception {
        Model_400005 cdtModel = (Model_400005) reqMain.getReqParam();
        String cellphone = StringUtils.trim(cdtModel.getCmCellphone());
        String password = StringUtils.trim(cdtModel.getCmPassword());
        String sign = StringUtils.trim(cdtModel.getSign());

        customerMainInfoService.resetPassword(cellphone, password, sign);
        return Result.success();
    }

    @FunctionId("400055")
    @Override
    public Result checkValidateCode(ReqMain reqMain) throws Exception {
        Model_400055 cdtModel = (Model_400055) reqMain.getReqParam();
        String cellphone = StringUtils.trim(cdtModel.getCmCellphone());
        String sign = StringUtils.trim(cdtModel.getSign());

        customerMainInfoService.checkSmsCode(cellphone, sign);
        return Result.success();

    }

    @FunctionId("400006")
    @Override
    public Result realNameAuth(ReqMain reqMain) throws Exception {
        Model_400006 cdtModel = (Model_400006) reqMain.getReqParam();
        Long cmCustomerId = cdtModel.getCmCustomerId();
        String cmRealName = cdtModel.getCmRealName();
        cmRealName = cmRealName.trim();
        String cmIdnum = StringUtils.upperCase(cdtModel.getCmIdnum());
        CustomerDTO customerDTO  = customerMainInfoService.realNameAuthInit(cmCustomerId);
        if (customerDTO.getAuthStatus() == 1){
            return Result.success("认证失败，请联系客服", customerDTO);
        }

        CustomerDTO dto = customerMainInfoService.realNameAuth(cmCustomerId, cmRealName, cmIdnum, reqMain.getReqHeadParam());
        dto.setSessionToken(reqMain.getReqHeadParam().getSessionToken());
        if (StringUtils.isNotEmpty(dto.getMsg())) {
            return Result.success("重复绑定", dto);
        }
        return Result.success("auth.success.reward", dto);
    }

    @FunctionId("400007")
    public Result checkIntroduceCode(ReqMain reqMain) throws Exception {
        Model_400007 cdtModel = (Model_400007) reqMain.getReqParam();
        String cmIntroduceCode = cdtModel.getCmIntroduceCode();
        customerMainInfoService.validateIntroduceCodeCmOrOrg(cmIntroduceCode.trim().toUpperCase());
        return Result.success();
    }

    @FunctionId("400008")
    @Override
    public Result logout(ReqMain reqMain) throws Exception {
        Model_400008 cdtModel = (Model_400008) reqMain.getReqParam();
        Long customerId = cdtModel.getCmCustmonerId();
        boolean isSuccess = customerMainInfoService.logout(customerId, reqMain.getReqHeadParam());
        return new Result(isSuccess);
    }

    @FunctionId("400009")
    @Override
    public Result sign(ReqMain reqMain) throws Exception {
        Model_400009 cdtModel = (Model_400009) reqMain.getReqParam();
//        String platform = ReqUtils.getUserAgent(reqMain.getReqHeadParam());
        CustomerSign sign = customerSignService.sign(cdtModel.getSessionToken());
        return Result.success(sign);
    }

    @FunctionId("400011")
    @Override
    public Result cmToken2CmNumber(ReqMain reqMain) throws Exception {
        Model_400011 cdtModel = (Model_400011) reqMain.getReqParam();
        String userToken = StringUtils.trim(cdtModel.getUserToken());
        CustomerMainInfo mainInfo = customerMainInfoService.findOneBySessionToken(userToken);
        return Result.success(mainInfo.getCmNumber());
    }

    @FunctionId("400031")
    @Override
    public Result queryCustomerById(ReqMain reqMain) throws Exception {
        Map<String, Object> map = new HashMap<>();
        Model_400031 cdtModel = (Model_400031) reqMain.getReqParam();
        CustomerMainInfo mainInfo = customerMainInfoService.findOneByCustomerId(cdtModel.getCustomerId());
        if (mainInfo != null) {
            map.put("name", mainInfo.getCmRealName());
            map.put("phone", mainInfo.getCmCellphone());
            map.put("accountNo", mainInfo.getCmAccount());
            map.put("customerId", mainInfo.getId());
            map.put("cmNumber", mainInfo.getCmNumber());
        }
        return Result.success(map);
    }


    @FunctionId("400014")
    @Override
    public Result isPhoneRegistered(ReqMain reqMain) throws Exception {
        Model_400014 cdtModel = (Model_400014) reqMain.getReqParam();
        String cellphone = cdtModel.getCellphone();
        CustomerMainInfo mainInfo = customerMainInfoService.findOneByPhone(cellphone);
        return Result.success(mainInfo == null);
    }

    @FunctionId("400020")
    @Override
    public Result validatePassword(ReqMain reqMain) throws Exception {
        Model_400020 cdtModel = (Model_400020) reqMain.getReqParam();
        Map<String, Object> map = Maps.newHashMap();
        String cellPhone = StringUtils.trim(cdtModel.getCellPhone());
        String password = cdtModel.getPassword();
        boolean result = customerMainInfoService.validatePassword(cellPhone, password);
        map.put("isCheck", result ? 1 : 0);
        return Result.success(map);
    }

    @FunctionId("410001")
    @Override
    public Result setTradePassword(ReqMain reqMain) throws Exception {
        Model_410001 cdtModel = (Model_410001) reqMain.getReqParam();
        String cellphone = StringUtils.trim(cdtModel.getCmCellphone());
        String password = StringUtils.trim(cdtModel.getTradePassword());
        String validateCode = StringUtils.trim(cdtModel.getValidateCode());
        customerMainInfoService.setTradePassword(cellphone, password, validateCode);
        return Result.success();
    }

    @FunctionId("410002")
    @Override
    public Result changeTradePassword(ReqMain reqMain) throws Exception {
        Model_410002 cdtModel = (Model_410002) reqMain.getReqParam();
        String cellphone = StringUtils.trim(cdtModel.getCmCellphone());
        String oldPassword = StringUtils.trim(cdtModel.getOldTradePassword());
        String newPassword = StringUtils.trim(cdtModel.getNewTradePassword());

        if (oldPassword.equals(newPassword)) {
            throw new BusinessException("password.equal");
        }
        ResetTradePasswordDTO dto = customerMainInfoService.changeTradePassword(cellphone, oldPassword, newPassword);
        if (dto == null) {
            return Result.success();
        } else {
            return Result.fail(dto.getMsg(), dto);
        }
    }


    @FunctionId("410003")
    @Override
    public Result resetTradePassword(ReqMain reqMain) throws Exception {
        Model_410003 cdtModel = (Model_410003) reqMain.getReqParam();
        String cellphone = StringUtils.trim(cdtModel.getCmCellphone());
        String password = StringUtils.trim(cdtModel.getTradePassword());
        String idCard = StringUtils.trim(cdtModel.getIdCard());
        String validateCode = StringUtils.trim(cdtModel.getValidateCode());
        customerMainInfoService.resetTradePassword(cellphone, password, idCard, validateCode);
        return Result.success();
    }


    @FunctionId("710001")
    @Override
    public Result channelRegister(ReqMain reqMain) throws Exception {
        Model_710001 cdtModel = (Model_710001) reqMain.getReqParam();
        String cmCellphone = StringUtils.trim(cdtModel.getCmCellphone());
        String cmPassword = StringUtils.trim(cdtModel.getCmPassword());
        String validateCode = StringUtils.trim(cdtModel.getValidateCode());
        String channelCode = StringUtils.trim(cdtModel.getChannelCode());
        String ip = subIp(cdtModel.getIp());
        String inviteCode = StringUtils.trim(cdtModel.getInviteCode());
        customerMainInfoService.registerFromChannel(cmCellphone, cmPassword, validateCode, channelCode, reqMain.getReqHeadParam(), ip,inviteCode,null,null);
        return Result.success("register.success.reward");
    }

    @FunctionId("800010")
    @Override
    public Result cmToken2CmId(ReqMain reqMain) throws Exception {
        Model_800010 cdtModel = (Model_800010) reqMain.getReqParam();
        String userToken = StringUtils.trim(cdtModel.getUserToken());
        CustomerMainInfo mainInfo = customerMainInfoService.findOneBySessionToken(userToken);
        return Result.success(mainInfo.getId());
    }

    @FunctionId("800016")
    @Override
    public Result sessionToken2CmNumber(ReqMain reqMain) throws Exception {
        Model_800016 cdtModel = (Model_800016) reqMain.getReqParam();
        String userToken = StringUtils.trim(cdtModel.getUserToken());
        CustomerMainInfo mainInfo = customerMainInfoService.findOneBySessionToken(userToken);
        return Result.success(mainInfo.getCmNumber());
    }

    /*
     * 800019 修改手机号之前检验信息
     *
     */
    @FunctionId("800019")
    @Override
    public Result checkCustomerInfo(ReqMain reqMain) throws Exception {
        Model_800019 cdtModel = (Model_800019) reqMain.getReqParam();
        Long customerId = cdtModel.getCustomerId();
        String validateCode = StringUtils.trim(cdtModel.getValidateCode());
        String idNumber = StringUtils.trim(cdtModel.getIdNumber());
        String validateType = StringUtils.trim(cdtModel.getValidateType());
        String payPassword = StringUtils.trim(cdtModel.getPayPassword());
        customerMainInfoService.checkCustomerInfo(customerId, validateCode, idNumber, validateType, payPassword);
        return Result.success();
    }

    /*
     * 800020 修改手机号   存管版本移除此功能
     *
     */
    @FunctionId("800020")
    @Override
    public Result changeCellphone(ReqMain reqMain) throws Exception {
        Model_800020 cdtModel = (Model_800020) reqMain.getReqParam();
        Long customerId = cdtModel.getCustomerId();
        String validateCode = StringUtils.trim(cdtModel.getValidateCode());
        String cellphone = StringUtils.trim(cdtModel.getCellphone());
        customerMainInfoService.changeCellphone(customerId, validateCode, cellphone);
        return Result.success();
    }


    @FunctionId("400072")
    @Override
    public Result riskTest(ReqMain reqMain) throws Exception {
        Model_400072 model = (Model_400072) reqMain.getReqParam();
        CustomerMainInfo mainInfo = customerMainInfoService.findOneBySessionToken(model.getSessionToken());
        if (mainInfo == null) {
            throw new BusinessException("用户不存在");
        }
        long testScores = 0;
        String resStr = model.getAnswerResult().toUpperCase();
        if (resStr.endsWith(",")) {
            resStr = resStr.substring(0, resStr.length() - 1);
        }
        String[] resArr = resStr.split(",");
        int testFlag = 0;
        for (String ts : resArr) {
            testFlag++;
            if (StringUtils.isBlank(ts)) continue;
            if (testFlag > 5) break;//第6,7题不计分数
            if ("A".equals(ts)) testScores += AppConstants.riskAnswerResult.RISK_ANSWER_RESULT_A;
            if ("B".equals(ts)) testScores += AppConstants.riskAnswerResult.RISK_ANSWER_RESULT_B;
            if ("C".equals(ts)) testScores += AppConstants.riskAnswerResult.RISK_ANSWER_RESULT_C;
            if ("D".equals(ts)) testScores += AppConstants.riskAnswerResult.RISK_ANSWER_RESULT_D;
            if ("E".equals(ts)) testScores += AppConstants.riskAnswerResult.RISK_ANSWER_RESULT_E;
        }
        BusiRiskAssess riskAssess = getBusiRiskAssess(testScores);

        long testCount = riskAssessService.queryRiskTest(mainInfo.getId());
        BusiRiskTest riskTest = new BusiRiskTest();
        riskTest.setAnswerResult(model.getAnswerResult().toUpperCase().toString());
        riskTest.setCustomerId(mainInfo.getId());

        riskTest.setType(riskAssess.getType());
        riskTest.setIsFirst(testCount == 0 ? 1L : 0L);
        riskTest.setInsertTime(new Date());
        if (testCount == 0) {
            riskAssessService.saveRiskAnswer(riskTest);
            // 第一次进行风险测试送捞财币
            BusiTask task = busiTaskMapper.getBusiTaskByRiskTest();
            if (task == null) {
                return Result.fail("未配置风险测评赠送积分数量");
            }
            Integer integral = task.getLcAmt().intValue();
            IntegralGiveDto giveDto = new IntegralGiveDto();
            giveDto.setTransNo("risk_Test_" + mainInfo.getId() + "_" + DateUtil.timeFormat(new Date(), "yyyyMMdd"));
            giveDto.setIntegral(integral);
            giveDto.setNo(AppConstants.GIVE_INTEGRAL.RISK_TEST);
            giveDto.setAccountNo(mainInfo.getCmNumber());
            giveDto.setPlatform(reqMain.getReqHeadParam().getPlatform());
            giveDto.setChannel(AppConstants.APPLICATION_CONTEXT_NAME);
            ResultDto<IntegralResDto> result = integralAccountFacadeService.giveGetIntegral(giveDto);
            if (!result.isSuccess()) {
                throw new BusinessException("账户赠送积分异常:" + result.getMsg());
            }
            riskAssess.setAwardCoin(Convert.toLong(integral));
            riskAssess.setIsFirst(1);
        } else {
            riskTest.setUpdateTime(new Date());
            riskAssess.setIsFirst(0);
            //riskAssessService.updateRiskAnswer(riskTest);
            //v4.3用户评测新增记录不更新
            riskAssessService.saveRiskAnswer(riskTest);

        }
        riskAssess.setType(getRiskType(riskAssess.getType()));
        riskAssess.setConsumed((mainInfo.getIsConsumed() != null && Convert.toInt(mainInfo.getIsConsumed()) > 0) ? 1 : 0);
        int flag = (mainInfo.getSignContract() == null || mainInfo.getSignContract() == 0) ? 0 : 1;
        riskAssess.setSignContract(flag);
        riskAssess.setCanSignContract(0);
        if (flag == 0){
            riskAssess.setCanSignContract(cmStatus(mainInfo));
        }

        riskAssess.setSignContract((mainInfo.getSignContract() == null || mainInfo.getSignContract() == 0) ? 1 : 0 );
        customerMainInfoService.updateCustomerRiskTestType(riskTest.getType(), mainInfo.getId());
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.YEAR, 1);
        riskAssess.setRiskExpireTime(cal.getTime());

        //风险测评数据
        JSONObject json = new JSONObject();
        json.put("cmNumber", mainInfo.getCmNumber());
        json.put("customerId", mainInfo.getId());
        json.put("riskTime", new Date());
        json.put("riskTestType", riskTest.getType());

        return Result.success(riskAssess);
    }

    /**
     * (未实名 || 未开户)   可以风险测评 不可以电子签章
     * (已实名 && 已开户)   可以电子签章
     * @param mainInfo
     * @return
     */
    public Integer cmStatus(CustomerMainInfo mainInfo){
        if (mainInfo == null){
            return 0;
        }
        if (mainInfo.getCmStatus() == null || mainInfo.getCmStatus() != 3){
            return 0;
        }
        if (StringUtils.isBlank(mainInfo.getCmOpenAccountFlag()) || StringUtils.equals(mainInfo.getCmOpenAccountFlag(),"0")){
            return 0;
        }
        return 1;
    }

    @FunctionId("400073")
    @Override
    public Result queryCustomerRiskTestType(ReqMain reqMain) throws Exception {
        Model_400073 model = (Model_400073) reqMain.getReqParam();
        CustomerMainInfo mainInfo = customerMainInfoService.findOneBySessionToken(model.getSessionToken());
        if (mainInfo == null) {
            throw new BusinessException("用户不存在");
        }

        BusiRiskAssess riskAssess = riskAssessService.queryCustomerRiskTestType(mainInfo.getId());
        if (riskAssess == null ){
            riskAssess = new BusiRiskAssess();
        }
        riskAssess.setCustomerId(mainInfo.getId());
        riskAssess.setConsumed((mainInfo.getIsConsumed() != null && Convert.toInt(mainInfo.getIsConsumed()) > 0) ? 1 : 0);
        riskAssess.setType(getRiskType(riskAssess.getType(),mainInfo.getRiskExpireTime()));
        int flag = (mainInfo.getSignContract() == null || mainInfo.getSignContract() == 0) ? 0 : 1;
        riskAssess.setSignContract(flag);
        riskAssess.setCanSignContract(0);
        if (flag == 0){
            riskAssess.setCanSignContract(cmStatus(mainInfo));
        }

        return Result.success(riskAssess);
    }

    private BusiRiskAssess getBusiRiskAssess(long testScores) {
        BusiRiskAssess riskAssess = new BusiRiskAssess();
        List<BusiRiskAssess> riskAssessList = riskAssessService.listBusiRiskAssess(); //T_RISK_ASSESS
        for (BusiRiskAssess risk : riskAssessList) {
            if (testScores == 0 || testScores <= risk.getPointMax()) {
                riskAssess = risk;
                break;
            }
            if (testScores > risk.getPointMin() & testScores <= risk.getPointMax()) {
                riskAssess = risk;
                break;
            }
        }
        return riskAssess;
    }

    private Long getRiskTestAward() {
        Long lcbNum = 0L;
        List<BusiTask> taskList = busiTaskMapper.selectEnableTask(new Date());//查询所有任务
        for (BusiTask task : taskList) {
            String taskType = task.getTaskType();
            if (AppConstants.MallTaskStatus.TASK_RISK_TEST.equals(taskType)) {
                lcbNum = task.getLcAmt();
                break;
            }
        }
        return lcbNum;
    }

    private String getRiskType(String type) {
        if ("A".equals(type)) return AppConstants.riskType.RISK_TYPE_A;
        if ("B".equals(type)) return AppConstants.riskType.RISK_TYPE_B;
        if ("C".equals(type)) return AppConstants.riskType.RISK_TYPE_C;
        if ("D".equals(type)) return AppConstants.riskType.RISK_TYPE_D;
        if ("E".equals(type)) return AppConstants.riskType.RISK_TYPE_E;
        return null;

    }

    private String getRiskType(String type,Date riskExpireTime) {
        if (riskExpireTime!=null && DateUtil.getIntervalDays2(new Date(),riskExpireTime)>0) {
            return null;
        }
        if ("A".equals(type)) return AppConstants.riskType.RISK_TYPE_A;
        if ("B".equals(type)) return AppConstants.riskType.RISK_TYPE_B;
        if ("C".equals(type)) return AppConstants.riskType.RISK_TYPE_C;
        if ("D".equals(type)) return AppConstants.riskType.RISK_TYPE_D;
        if ("E".equals(type)) return AppConstants.riskType.RISK_TYPE_E;
        return null;
    }

    /**
     * 获取用户绑卡信息列表
     *
     * @param customerId
     * @return
     */
    public List<BankCardInfoDTO> getBindCardInfoByList(long customerId) {
        CustomerMainInfo mainInfo = customerMainInfoService.findOpenCustomerById(customerId);
        List<CustomerBankAccount> bankAccountList = customerBankAccountMapper.listCustBankAccount(mainInfo.getId());
        List<BankCardInfoDTO> bankCardInfoList = new ArrayList<>();
        BusiPayChannel payChannel = getBusiPayChannelInfo();
        for (CustomerBankAccount bank : bankAccountList) {
            BankCardInfoDTO bankCardInfoDTO = new BankCardInfoDTO();
            bankCardInfoDTO.setPayCode(payChannel.getPayPlatformCode());
            BusiBankLimit limit = bankCardService.getPayBank(bank.getCbBankCode(), bankCardInfoDTO.getPayCode());
            bankCardInfoDTO.setBankName(bank.getCbBankName());
            bankCardInfoDTO.setBankCode(limit.getCode());
            bankCardInfoDTO.setSubBankCode(bank.getCbSubBankCode() == null ? "" : bank.getCbSubBankCode());
            bankCardInfoDTO.setSubBankName(StringUtils.isEmpty(bank.getCbBranchName()) ? "" : bank.getCbBranchName());
            bankCardInfoDTO.setBankCard(bank.getCbAccount());
            bankCardInfoDTO.setLlRecharge(limit.getLlRecharge());
            BusiBankLimit bankLimit = new BusiBankLimit();
            bankLimit.setBankCode(limit.getBankCode());
            bankLimit.setBankStatus("1");
            bankLimit.setPayChannel(payChannel.getPayPlatformCode());
            List<BusiBankLimit> bankLimits = busiBankLimitMapper.selectByCondition(bankLimit);
            BigDecimal amtRank = new BigDecimal(10000);//以10000区分万还是元
            if (bankLimits.isEmpty()) {
                throw new BusinessException("获取银行限额信息失败！");
            } else {
                bankLimit = bankLimits.get(0);
                String limitDesc = "限额：";
                String singleAmtDesc = bankLimit.getSingleAmt() + "元，";
                String dayAmtDesc = bankLimit.getDayAmt() + "元，";
                String monthDesc = bankLimit.getMonthAmt() + "元";
                if (new BigDecimal(bankLimit.getSingleAmt()).compareTo(amtRank) > 0) {
                    singleAmtDesc = new BigDecimal(bankLimit.getSingleAmt()).divide(amtRank) + "万元，";
                }
                if (new BigDecimal(bankLimit.getDayAmt()).compareTo(amtRank) > 0) {
                    dayAmtDesc = new BigDecimal(bankLimit.getDayAmt()).divide(amtRank) + "万元，";
                }
                if (new BigDecimal(bankLimit.getMonthAmt()).compareTo(amtRank) > 0) {
                    monthDesc = new BigDecimal(bankLimit.getMonthAmt()).divide(amtRank) + "万元";
                }
                limitDesc += "单笔" + singleAmtDesc;
                limitDesc += "单日" + dayAmtDesc;
                limitDesc += "单月" + monthDesc;
                bankCardInfoDTO.setLimitDesc(limitDesc);
            }
            String withdrawDesc = "";
            SysParameter sysParameter = sysParameterService.findOneByPrType("withdrawtimes");
            if (sysParameter == null) {
                throw new BusinessException("获取单日提现次数失败！");
            }
            String value = sysParameter.getPrValue();
            withdrawDesc += "1、单日限" + value + "笔，";
            sysParameter = sysParameterService.findOneByPrType("withdrawlimit");
            if (sysParameter == null) {
                throw new BusinessException("获取提现限额信息失败！");
            }
            value = sysParameter.getPrValue();
//            NumberFormat nf = new DecimalFormat("###,###.##");
//            String valueFmt = nf.format(Long.valueOf(value));
            String valueFmtDesc = value + "元，";
            if (new BigDecimal(value).compareTo(amtRank) > 0) {
                String limitAmt = new BigDecimal(value).divide(amtRank).setScale(2, BigDecimal.ROUND_DOWN).toPlainString();
                valueFmtDesc = CoreUtil.subZeroAndDot(limitAmt) + "万元，";
            }
            withdrawDesc += "上限为" + valueFmtDesc + "\n2、提现到账时间：预计T+1个工作日";
            withdrawDesc += "\n3、当日充值金额，待华瑞银行核定后方可发起提现；如遇节假日，核定时间顺延至工作日";
            bankCardInfoDTO.setWithdrawDesc(withdrawDesc);
            bankCardInfoList.add(bankCardInfoDTO);
        }
        Collections.sort(bankCardInfoList, new Comparator<BankCardInfoDTO>() {
            @Override
            public int compare(BankCardInfoDTO o1, BankCardInfoDTO o2) {
                Collator collator = Collator.getInstance();
                return collator.compare(o1.getLlRecharge(), o2.getLlRecharge());
            }
        });
        return bankCardInfoList;
    }

    /**
     * 获取支付渠道信息
     *
     * @return
     */
    public BusiPayChannel getBusiPayChannelInfo() {
        BusiPayChannel busiPayChannel = new BusiPayChannel();
        busiPayChannel.setIsEnable((short) 1);
        busiPayChannel.setUseCity("1");
        List<BusiPayChannel> channels = busiPayChannelMapper.select(busiPayChannel);
        int resCount = channels.size();
        if (resCount == 0) {
            throw new BusinessException("获取支付渠道失败，原因：后台没有设置可使用的支付渠道！");
        }
        if (resCount > 1) {
            throw new BusinessException("获取支付渠道失败，原因：后台设置可使用的支付渠道为多个！");
        }
        BusiPayChannel payChannel = channels.get(0);
        return payChannel;
    }

    @FunctionId("550007")
    @Override
    public Result customerSign(ReqMain reqMain) throws Exception {
        return customerSignInfoService.customerSign(reqMain);
    }

    @FunctionId("550008")
    @Override
    public Result queryCustomerSignCount(ReqMain reqMain) throws Exception {
        return customerSignInfoService.queryCustomerSignCount(reqMain);
    }

    @FunctionId("420005")
    public Result customerBorrowInfo(ReqMain reqMain) throws Exception {
        Model_420005 cdtModel = (Model_420005) reqMain.getReqParam();
        customerBorrowInfoService.saveCustomerBorrowInfo(cdtModel);
        return Result.success("借款意向收集成功");
    }


    @FunctionId("400022")
    @Override
    public Result validateCodeLogin(final ReqMain reqMain) throws Exception {
        Model_400022 cdtModel = (Model_400022) reqMain.getReqParam();
        final String cellphone = StringUtils.trim(cdtModel.getCmCellphone());

        final String validateCode = cdtModel.getValidateCode();
        final String deviceId = cdtModel.getDeviceId();
        final String clientId = cdtModel.getClientId();
        CustomerDTO dto = customerMainInfoService.loginValidateCode(LaocaiUtil.getLoginType(reqMain.getReqHeadParam()), cellphone, validateCode, deviceId, clientId, reqMain.getReqHeadParam());
        return Result.success(dto);
    }

    @FunctionId("420006")
    @Override
    public Result customerAuthorize(ReqMain reqMain) throws Exception {
        Model_420006 model_420006 = (Model_420006) reqMain.getReqParam();
        boolean auth = customerMainInfoService.isAuthorize(model_420006.getCustomerId());
        CustomerAuthorizeDTO authorizeDTO = null;
        if (!auth) {
            authorizeDTO = busiProductMapper.queryProductSettleDate(model_420006.getProductId());
        }
        return Result.success(authorizeDTO);
    }

    /**
     * 获取出借凭证验证码
     *
     * @param reqMain
     * @return
     * @throws Exception
     */
    @FunctionId("420007")
    @Override
    public Result borrowCertificate(ReqMain reqMain) throws Exception {
        Model_420007 model_420007 = (Model_420007) reqMain.getReqParam();
        return busiHandleBorrowCertificateService.getBorrowCertificateValidateCode(model_420007, reqMain.getReqHeadParam());

    }

    /**
     * 检验邮箱验证码是否正确，发送邮件
     *
     * @param reqMain
     * @return
     * @throws Exception
     */
    @FunctionId("420008")
    @Override
    public Result checkEmailValidateCode(ReqMain reqMain) throws Exception {
        Model_420008 model_420008 = (Model_420008) reqMain.getReqParam();
        String email = StringUtils.trim(model_420008.getEmail());
        String sign = StringUtils.trim(model_420008.getSign());
        return  busiHandleBorrowCertificateService.checkEmailValidateCode(email, sign);

    }

    /**
     * 个人中心风险测试
     * @param reqMain
     * @return
     * @throws Exception
     */
    @FunctionId("400077")
    @Override
    public Result userCenterRisk(ReqMain reqMain) throws Exception {
        Model_400077 model = (Model_400077) reqMain.getReqParam();
        CustomerMainInfo mainInfo = customerMainInfoService.findOneByCustomerId(model.getCustomerId());
        if (mainInfo == null) {
            throw new BusinessException("用户不存在");
        }
        Map userRiskInfo= Maps.newHashMap();
        String riskFlag="未测试";
        Date riskExpireTime = mainInfo.getRiskExpireTime();
        if (mainInfo.getRiskTestType()!=null){
            riskFlag = getRiskType(mainInfo.getRiskTestType());
        }
        if (riskExpireTime!=null && DateUtil.getIntervalDays2(new Date(),riskExpireTime)>0){
            riskFlag="已过期";
        }
        userRiskInfo.put("riskFlag",riskFlag);
        userRiskInfo.put("riskUrl",configParamBean.getRiskTestUrl());

        return Result.success(userRiskInfo);
    }


    @FunctionId("750001")
    public Result fanLiRegister(ReqMain reqMain) throws Exception {
        Model_750001 cdtModel = (Model_750001) reqMain.getReqParam();
        String cmCellphone = StringUtils.trim(cdtModel.getCmCellphone());
        String cmPassword = StringUtils.trim(cdtModel.getCmPassword());
        String validateCode = StringUtils.trim(cdtModel.getValidateCode());
        String channelCode = StringUtils.trim(cdtModel.getChannelCode());
        String ip = subIp(cdtModel.getIp());
        String inviteCode = StringUtils.trim(cdtModel.getInviteCode());
        String tc = StringUtils.trim(cdtModel.getTc());
        String uid = StringUtils.trim(cdtModel.getUid());
        String actionTime = StringUtils.trim(cdtModel.getActionTime());
        String code =StringUtils.trim(cdtModel.getCode());
        //验证返利网code
        String shopKey = configParamBean.getShopKey();
        String md5Code = StringUtils.lowerCase(MD5.MD5Encode(uid+shopKey+actionTime));
        if (!code.equals(md5Code)){
            throw new BusinessException("返利网验签错误。");
        }
        customerMainInfoService.registerFromChannel(cmCellphone, cmPassword, validateCode, channelCode, reqMain.getReqHeadParam(), ip,inviteCode,uid,tc);
        return Result.success("register.success.reward");
    }
    @Autowired
    private CustomerAddressService customerAddressService;

    @FunctionId("550001")
    @Override
    public Result queryCustomerAddressList(ReqMain reqMain) {
        return customerAddressService.queryCustomerAddressList(reqMain);
    }

    @FunctionId("550002")
    @Override
    public Result saveOrUpdateAddress(ReqMain reqMain) {
        return customerAddressService.saveOrUpdateAddress(reqMain);
    }

    @FunctionId("550003")
    @Override
    public Result deleteCustomerAddressById(ReqMain reqMain) {
        return customerAddressService.deleteCustomerAddressById(reqMain);
    }

    @FunctionId("560003")
    @Override
    public Result userCenter(ReqMain reqMain)  {
        Model_560003 model = (Model_560003) reqMain.getReqParam();
        CustomerMainInfo mainInfo = customerMainInfoService.findOneByCustomerId(model.getCustomerId());
        if (mainInfo == null) {
            throw new BusinessException("用户不存在");
        }
        Integer memberLevel =  mainInfo.getMemberLevel();//1:铁象 3:铜象 5:银象 7:金象 9:白金象 11:钻石象 13:无极象
        if (memberLevel == null) {
            throw new BusinessException("该用户没有对应等级");
        }
        Date ratingChangingDate = mainInfo.getRatingChangingDate();
        String expireDate = "--";
        int levelExpireDay = Integer.parseInt(configParamBean.getLevelExpireDay());
        if (ratingChangingDate != null) {
            ratingChangingDate = DateUtils.plusDays(ratingChangingDate, levelExpireDay);
            expireDate = new SimpleDateFormat("yyyy年MM月dd日").format(ratingChangingDate);
        }
        BigDecimal holdAssert = getUserHoldAssert(mainInfo);

        //查询当前用户等级对应的配置信息
        CustomerRatingConfig userLevelConfig = new CustomerRatingConfig();
        CustomerRatingConfig userNextLevelConfig = new CustomerRatingConfig();
        //查询用户等级对应的配置信息
        List<CustomerRatingConfig> ratingConfigs= customerRatingConfigMapper.queryRatingConfigs();
        if (ratingConfigs == null) {
            throw new BusinessException("会员等级对应的配置信息不存在");
        }
        for (int i = 0; i < ratingConfigs.size(); i++) {
            if (memberLevel.longValue() == ratingConfigs.get(i).getRatingNum()) {
                //获取当前用户等级信息
                userLevelConfig = ratingConfigs.get(i);
                //获取当前用户下一等级信息
                if (i==ratingConfigs.size()-1){
                    userNextLevelConfig = ratingConfigs.get(i);
                }else{
                    userNextLevelConfig = ratingConfigs.get(i+1);
                }

            }
        }
        log.info("当前用户会员等级：{},下一级别会员等级：{}", JSONUtils.toJSON(userLevelConfig),JSONUtils.toJSON(userNextLevelConfig));
//      显示距离下个会员等级的差额（公式=下一个等级区间最低在投金额-当前在投资金）
        BigDecimal amtRank = new BigDecimal(10000);//以10000区分万还是元
        BigDecimal minInvestingAmt = userNextLevelConfig.getMinInvestingAmt();
        //下一个等级差额
        BigDecimal nextDiffAmt= minInvestingAmt.multiply(amtRank).subtract(holdAssert).setScale(2, BigDecimal.ROUND_HALF_DOWN);
        if (nextDiffAmt.compareTo(new BigDecimal(1))<0){
            nextDiffAmt = new BigDecimal(1);
        }

        Map userCenterMap = Maps.newHashMap();
        userCenterMap.put("memberLevel", memberLevel);
        userCenterMap.put("ratingDescr", userLevelConfig.getRatingDescr());
        userCenterMap.put("expireDate",expireDate);
        userCenterMap.put("minInvestingAmt",userLevelConfig.getMinInvestingAmt().multiply(amtRank));
        userCenterMap.put("maxInvestingAmt",userLevelConfig.getMaxInvestingAmt()==null?"0":userLevelConfig.getMaxInvestingAmt().multiply(amtRank));

        userCenterMap.put("nextMemberLevel",userNextLevelConfig.getRatingNum());
        userCenterMap.put("nextRatingDescr", userNextLevelConfig.getRatingDescr());
        userCenterMap.put("nextDiffAmt", nextDiffAmt);
        userCenterMap.put("holdAssert", holdAssert);
        userCenterMap.put("memberDesc", configParamBean.getTouchProductDetailsUrl()+configParamBean.getMemberDescUrl());


        userCenterMap.put("userLevelGift",getUserLevelGift(memberLevel,mainInfo));

        return Result.success(userCenterMap);
    }



    //用户持有中资产
    private  BigDecimal getUserHoldAssert(CustomerMainInfo mainInfo){
        BigDecimal holdAssert = new BigDecimal(0);
        try {
            UserUnReceiveAsset unReceiveAsset = accountOverview520003Service.getHoldAsset(mainInfo);
            holdAssert =new BigDecimal(CoreUtil.BigDecimalAccurate(unReceiveAsset.getUnReceivePrinciple()));
            log.info("当前用户持有中的资产：{}", holdAssert);
        } catch (Exception e) {
            log.info("-----查询用户持有资产失败-----：",e);
        }
        return holdAssert;
    }

    private  Map getUserLevelGift(Integer memberLevel,CustomerMainInfo mainInfo){
        Map giftMap = Maps.newHashMap();
        List <Map<String,Object>> giftList = new ArrayList<>();

        String giftType="";
        String giftUrls[] ={configParamBean.getLevelGiftUrl1(),configParamBean.getLevelGiftUrl2(),configParamBean.getLevelGiftUrl3(),configParamBean.getLevelGiftUrl4(),configParamBean.getLevelGiftUrl5(),configParamBean.getLevelGiftUrl6(),configParamBean.getLevelGiftUrl7()};
        String levelGifts[] ={AppConstants.MEMBER_LEVEL_GIFT.IRON_ELEPHANT_GIFT,AppConstants.MEMBER_LEVEL_GIFT.COPPER_ELEPHANT_GIFT,AppConstants.MEMBER_LEVEL_GIFT.SILVER_ELEPHANT_GIFT,AppConstants.MEMBER_LEVEL_GIFT.GOLD_ELEPHANT_GIFT,AppConstants.MEMBER_LEVEL_GIFT.PLATINUM_ELEPHANT_GIFT,AppConstants.MEMBER_LEVEL_GIFT.DIAMOND_ELEPHANT_GIFT,AppConstants.MEMBER_LEVEL_GIFT.INFINATE_ELEPHANT_GIFT};
        int [] memberLevels={AppConstants.MEMBER_LEVEL.MEMBER_LEVEL_1,AppConstants.MEMBER_LEVEL.MEMBER_LEVEL_3,AppConstants.MEMBER_LEVEL.MEMBER_LEVEL_5,AppConstants.MEMBER_LEVEL.MEMBER_LEVEL_7,AppConstants.MEMBER_LEVEL.MEMBER_LEVEL_9,AppConstants.MEMBER_LEVEL.MEMBER_LEVEL_11,AppConstants.MEMBER_LEVEL.MEMBER_LEVEL_13};

        for (int i = 0; i < memberLevels.length; i++) {
            if (memberLevels[i] == memberLevel){
                giftType = levelGifts[i];
            }
        }

        String[] result = giftType.split(",");
        for (int i = 0; i < result.length; i++) {
            Map<String,Object>  typeMap  = new HashMap<String,Object> ();
            typeMap.put("giftUrl",configParamBean.getTouchProductDetailsUrl()+giftUrls[i]);
            typeMap.put("giftType",i+1);
            typeMap.put("status",0);
            if (i ==2 && 0 == gainFlag(mainInfo)){
                BusiCashRecord busiCashRecord = cashRecordMapper.queryMonthCashRecord(mainInfo.getCmNumber());
                if (busiCashRecord == null) {
                    typeMap.put("status",1);
                }
            }
            giftList.add(typeMap);
        }
        giftMap.put("giftList",giftList);
        return giftMap;
    }

    @FunctionId("570003")
    @Override
    public Result initMonthGift(ReqMain reqMain){
        int gainStatus = 0;//0:未到领取时间，1：未领取，2，已领取
        Model_570003 model = (Model_570003) reqMain.getReqParam();
        String sessionToken=model.getSessionToken();
        CustomerMainInfo customerMainInfo=customerMainInfoService.findOneBySessionToken(sessionToken);
        if (customerMainInfo==null){
            throw new BusinessException("查询不到用户信息！");
        }
        BusiCashRecord busiCashRecord  = cashRecordMapper.queryMonthCashRecord(customerMainInfo.getCmNumber());
        DateTime dateTime = DateTime.now();
        int day = dateTime.dayOfMonth();
        int giftDay = Integer.parseInt(configParamBean.getMonthGiftDate());
        if (day < giftDay){
            log.info("当前时间:{},每月领取时间:{}号",dateTime,giftDay);
            gainStatus = 0;
        }else if (busiCashRecord != null) {
            gainStatus = 2;
        }else{
            gainStatus = 1;
        }

        Map cashMap = getRatingConfigMap();
        Map gainMap = Maps.newHashMap();
        gainMap.put("RatingConfigs",cashMap);
        gainMap.put("gainStatus",gainStatus);
        return Result.success(gainMap);
    }

    public Map getRatingConfigMap(){
        Map cashMap = Maps.newHashMap();
        Map map = Maps.newTreeMap();

        List<CustomerRatingConfig> customerRatingConfigs= customerRatingConfigMapper.queryCustomerRatingConfig(map);
        for(CustomerRatingConfig ratingConfig : customerRatingConfigs){
            Map<String, Object> paramsMap = Maps.newTreeMap();
            if (ratingConfig.getMonthlyPresent() == null){
                cashMap.put(ratingConfig.getRatingCode(),null);
            }else{
                List<String> welfaceIdList = Arrays.asList(ratingConfig.getMonthlyPresent().split(","));
                paramsMap.put("welfaceIds",welfaceIdList);
                List<BusiWelfareRuleDto> welfareByIds = busiWelfareMapper.getWelfareByIds(paramsMap);
                BigDecimal welfareAmt = BigDecimal.ZERO;
                for (BusiWelfareRuleDto busiWelfareRuleDto :welfareByIds){
                    welfareAmt =  welfareAmt.add(busiWelfareRuleDto.getAmount());
                }
                cashMap.put(ratingConfig.getRatingCode(),welfareAmt);
            }
        }
        return cashMap;
    }

    private int gainFlag(CustomerMainInfo customerMainInfo){
        int gainFlag=0;//0:显示领取礼包，1：不显示
        if (customerMainInfo.getMemberLevel() == null || customerMainInfo.getMemberLevel().intValue() == 1) {
            gainFlag = 1;
        }
        log.info("当前用户会员等级：{}", customerMainInfo.getMemberLevel());

        int giftDay = Integer.parseInt(configParamBean.getMonthGiftDate());
        Calendar cal=Calendar.getInstance();
        int currentDay = cal.get(Calendar.DATE);//当月的第几号
        if (currentDay < giftDay) {
            gainFlag = 1;
            log.info("当前时间:{},每月领取时间:{}号",DateUtil.dateToString(new Date()),giftDay);
        }
        return gainFlag;
    }

    @FunctionId("580003")
    @Override
    public Result gainMonthGift(ReqMain reqMain){
        Model_580003 model = (Model_580003) reqMain.getReqParam();
        String sessionToken=model.getSessionToken();
        CustomerMainInfo customerMainInfo=customerMainInfoService.findOneBySessionToken(sessionToken);
        if (customerMainInfo==null){
            throw new BusinessException("查询不到用户信息！");
        }
        if (gainFlag(customerMainInfo)==1){
            throw new BusinessException("领取失败,不符合领取条件！");
        }

        CustomerRatingConfig userLevelConfig = customerRatingConfigMapper.queryRatingConfigByRatingNum(Long.valueOf(customerMainInfo.getMemberLevel()));

        List<CashDto> cashDtos = new ArrayList<>();
        if(StringUtils.isNotEmpty(userLevelConfig.getMonthlyPresent())) {
            Map paramsMap = Maps.newTreeMap();
            List<String> welfaceIdList = Arrays.asList(userLevelConfig.getMonthlyPresent().split(","));
            paramsMap.put("welfaceIds",welfaceIdList);
            List<BusiWelfareRuleDto> welfareRuleDtos = busiWelfareRuleMapper.getWelfareByIds(paramsMap);
            for (BusiWelfareRuleDto welfareRuleDto : welfareRuleDtos) {
                if (welfareRuleDto == null) {
                    throw new BusinessException("月度礼包福利id无效！");
                }
                CashDto cashDto = new CashDto();
                cashDto.setAccountNo(customerMainInfo.getCmNumber());
                cashDto.setPublishSource(CashPublishSource.VIP);
                cashDto.setAmount(welfareRuleDto.getAmount());
                cashDto.setInvestMin(welfareRuleDto.getInvestMinAmount());
                cashDto.setPeriod(welfareRuleDto.getPeriod());
                cashDto.setInvestPeriodMin(welfareRuleDto.getInvestMinPeriod());
                if(welfareRuleDto.getInvestMaxPeriod()!=null){
                    cashDto.setInvestPeriodMax(welfareRuleDto.getInvestMaxPeriod());
                }
                cashDto.setInvestPeriodMax(welfareRuleDto.getInvestMaxPeriod());
                cashDtos.add(cashDto);
            }
            String transNo = SerialNoGeneratorService.commonGenerateTransNo(SerialNoType.MONTH_CASH, customerMainInfo.getCmNumber(), DateUtil.timeFormat(new Date(), DateUtil.YYYYMM));
            try {
                log.info("调用账户系统--->月度礼包赠送现金券开始，用户遍号【{}】", customerMainInfo.getCmNumber());
                com.zdmoney.integral.api.common.dto.ResultDto resultDto = cashFacadeService.publishCash(cashDtos, transNo);
                if (!resultDto.isSuccess()) {
                    log.error("调用账户系统--->月度礼包送现金券失败! 用户编号为:{},错误原因:{}", customerMainInfo.getCmNumber(), resultDto.getMsg());
                    throw new BusinessException("月度礼包送现金券失败：" + resultDto.getMsg());
                }
            } catch (Exception e) {
                log.error("调用账户系统--->，月度礼包送现金券异常，cmNumber【{}】", customerMainInfo.getCmNumber(), e);
                throw new BusinessException("月度礼包送现金券异常");
            }
        }
        BusiCashRecord record = new BusiCashRecord();
        record.setCashCouponType(AppConstants.CASH_TYPE.CASH_MONTH_GIFT);
        record.setCmNumber(customerMainInfo.getCmNumber());
        record.setCreateDate(new Date());
        record.setModifyDate(new Date());
        record.setStatus("1");
        record.setCashCouponId("0");
        record.setExpireDate(new Date());
        cashRecordMapper.saveBusiCashRecord(record);
        return Result.success("领取成功");
    }

    @FunctionId("590003")
    @Override
    public Result memberLevelInfo(ReqMain reqMain){
        Model_590003 model = (Model_590003) reqMain.getReqParam();
        String sessionToken=model.getSessionToken();
        CustomerMainInfo customerMainInfo=customerMainInfoService.findOneBySessionToken(sessionToken);
        if (customerMainInfo==null){
            throw new BusinessException("查询不到用户信息！");
        }
        List<CustomerRatingConfig> customerRatingConfigs = customerRatingConfigService.findAll();
        if (CollectionUtils.isEmpty(customerRatingConfigs)){
            throw new BusinessException("暂无会员等级配置信息！");
        }
        List levelList = new ArrayList();
        for (CustomerRatingConfig ratingConfig : customerRatingConfigs){
            Map<String,Object>  levelInfoMap  = new HashMap<String,Object> ();
            levelInfoMap.put("ratingDescr",ratingConfig.getRatingDescr());
            levelInfoMap.put("ratingNum",ratingConfig.getRatingNum());
            levelInfoMap.put("rebateCreditRate",ratingConfig.getRebateCreditRate());
            levelInfoMap.put("investmentCreditRate",ratingConfig.getInvestmentCreditRate());
            levelInfoMap.put("upgradingCredit",ratingConfig.getUpgradingCredit());
            levelInfoMap.put("minInvestingAmt",ratingConfig.getMinInvestingAmt());
            levelInfoMap.put("maxInvestingAmt",ratingConfig.getMaxInvestingAmt());

            levelList.add(levelInfoMap);
        }
        return Result.success(levelList);
    }

    @FunctionId("590004")
    @Override
    public Result getBorrowerInfo(ReqMain reqMain) {
        Model_590004 model = (Model_590004) reqMain.getReqParam();
        BorrowerInfoExtendResDto resDto = null;
        try{
            CustomerMainInfo mainInfo = customerMainInfoMapper.findByIdNoAndPhone(model.getCellPhone(), model.getIdNo());
            if(mainInfo == null){
                log.error("获取借款人信息失败：用户不存在");
                throw new BusinessException("用户不存在！");
            }
            BorrowerInfoExtendReqDto reqDto = new BorrowerInfoExtendReqDto();
            reqDto.setSubjectNo(model.getSubjectNo());
            reqDto.setCustomerIdNo(model.getIdNo());
            reqDto.setCustomerPhone(model.getCellPhone());
            reqDto.setPartnerNo(configParamBean.getSubjectPartnerNo());
            AssetsResultDto<BorrowerInfoExtendResDto> res = ilcbSubjectFacadeService.getBorrowerInfoExtend(reqDto);
            if (res.isSuccess()) {
                resDto = res.getData();
            } else {
                log.error("查询借款人信息失败:" + res.getMsg());
                throw new BusinessException("查询借款人信息失败");
            }
        } catch (Exception e){
            e.printStackTrace();
            log.error("查询借款人信息失败:" + e.getMessage());
            throw new BusinessException("查询借款人信息失败");
        }
        return Result.success(resDto);
    }
}