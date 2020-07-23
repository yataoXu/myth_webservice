package com.zdmoney.service;

import cn.hutool.core.convert.Convert;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.zdmoney.assets.api.dto.wacai.WacaiNotifyStatus;
import com.zdmoney.assets.api.dto.wacai.WacaiWithdrawReqDto;
import com.zdmoney.assets.api.facade.subject.ILCBSubjectFacadeService;
import com.zdmoney.common.CompanyAccounts;
import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.common.Result;
import com.zdmoney.component.redis.KeyGenerator;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.constant.CreditConstant;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.integral.api.common.dto.ResultDto;
import com.zdmoney.integral.api.dto.lcbaccount.*;
import com.zdmoney.integral.api.dto.lcbaccount.enm.*;
import com.zdmoney.integral.api.facade.IAccountFacadeService;
import com.zdmoney.life.api.utils.JsonUtils;
import com.zdmoney.mapper.NotifyCreditLogMapper;
import com.zdmoney.mapper.bank.CustomerBankAccountMapper;
import com.zdmoney.mapper.customer.CustomerMainInfoMapper;
import com.zdmoney.mapper.trade.BusiTradeFlowDetailMapper;
import com.zdmoney.mapper.trade.BusiTradeFlowMapper;
import com.zdmoney.mapper.zdpay.CustomerGrantInfoMapper;
import com.zdmoney.models.NotifyCreditLog;
import com.zdmoney.models.bank.CustomerBankAccount;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.sys.SysParameter;
import com.zdmoney.models.trade.BusiTradeFlow;
import com.zdmoney.models.trade.BusiTradeFlowDetail;
import com.zdmoney.models.zdpay.*;
import com.zdmoney.service.subject.SubjectService;
import com.zdmoney.session.RedisSessionManager;
import com.zdmoney.utils.*;
import com.zdmoney.webservice.api.common.CustomerAccountType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Logger;

/**
 * 回调处理
 *
 * Author: gosling
 * Date: 2018年8月15日 16:47:50
 */
@Slf4j
@Component
public class ZdPayService {

    @Autowired
    private CustomerMainInfoService customerMainInfoService;

    @Autowired
    private IAccountFacadeService accountFacadeService;

    @Autowired
    private CustomerGrantInfoMapper customerGrantInfoMapper;

    @Autowired
    private CustomerBankAccountMapper customerBankAccountMapper;

    @Autowired
    private BankCardService bankCardService;

    @Autowired
    private CustomerMainInfoMapper customerMainInfoMapper;

    @Autowired
    private BusiTradeFlowMapper busiTradeFlowMapper;

    @Autowired
    private BusiOrderService busiOrderService;

    @Autowired
    private BusiTradeFlowDetailMapper tradeFlowDetailMapper;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private SysParameterService sysParameterService;

    @Autowired
    private RedisSessionManager redisSessionManager;

    @Autowired
    private NotifyCreditLogMapper notifyCreditLogMapper;

    @Autowired
    private ConfigParamBean configParamBean;

    @Autowired
    private ILCBSubjectFacadeService lCBSubjectFacadeService;

    @Autowired
    private AccountOverview520003Service accountOverview520003Service;

    /**
     * 账户开户
     * @param data
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void zdpay1001(String data) {
        UserOpenBO userOpen = JSON.parseObject(data, UserOpenBO.class);
        CustomerMainInfo mainInfo = customerMainInfoService.checkCustomerId(Long.valueOf(userOpen.getUserId()));
        // 不可重复进行绑卡开户操作
        if (StringUtils.isNotEmpty(mainInfo.getFuiouLoginId()) || StringUtils.isNotEmpty(mainInfo.getBankAccountId())) {
            log.error("收银台回调-不可重复进行绑卡开户操作，用户ID:{}", mainInfo.getId());
            return;
        }
        // 保存绑卡信息
        long id = saveBankCard(mainInfo, userOpen);
        // 已设置华瑞交易密码
        mainInfo.setIsSetPwd(1);
        mainInfo.setCmOpenAccountFlag(AppConstants.CmOpenAccountFlag.OPEN);
        mainInfo.setBankAccountId(String.valueOf(id));
        Date date = new Date();
        mainInfo.setCmModifyDate(date);
        mainInfo.setCmOpenAccountDate(date);
        mainInfo.setFuiouLoginId(userOpen.getLoginId());
        mainInfo = customerMainInfoService.updateNotNull(mainInfo);
        // 保存授权信息
        saveUserOpenInfo(mainInfo, userOpen);
        // 平台户只更新用户表
        if (AppConstants.CustomerType.CUSTOMER_TYPE_2.equals(mainInfo.getCustomerType())) {
            return;
        }
        CustomerRegisterDto customerRegisterDto = new CustomerRegisterDto();
        customerRegisterDto.setUserNo(mainInfo.getCmNumber());
        customerRegisterDto.setUserName(mainInfo.getCmRealName());
        customerRegisterDto.setIDcard(mainInfo.getCmIdnum());
        customerRegisterDto.setTelNo(mainInfo.getCmCellphone());
        customerRegisterDto.setRoleType(RoleType.LENDER);
        if (CustomerAccountType.BORROWER.getValue().equals(mainInfo.getAccountType())) {
            customerRegisterDto.setRoleType(RoleType.BORROWER);
        }
        customerRegisterDto.setLoginId(userOpen.getLoginId());
        customerRegisterDto.setTransNo(SerialNoGeneratorService.generateAuthSerialNo(mainInfo.getId()));
        log.info("调用账户开户, 参数:{}", JSON.toJSONString(customerRegisterDto));
        ResultDto result = accountFacadeService.customerRegister(customerRegisterDto);
        if (!result.isSuccess()) {
            log.error("收银台回调-账户开户失败，用户ID:{},失败原因:{}", mainInfo.getId(), result.getMsg());
            throw new BusinessException("收银台回调-账户开户失败:" + result.getMsg());
        }
        subjectService.applySignature(mainInfo);
    }

    /**
     * 绑卡
     *
     * @param data
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void zdpay1002(String data) {
        BankCardBindRespBO bankCardBindRespBO = JSON.parseObject(data, BankCardBindRespBO.class);
        CustomerMainInfo mainInfo = customerMainInfoService.queryCustomerInfoByLoginId(bankCardBindRespBO.getLoginId());
        if (StringUtils.isNotEmpty(mainInfo.getBankAccountId())) {
            log.info("用户id:{}已绑卡, 无需重复绑卡!", mainInfo.getId());
            return;
        }
        Date date = new Date();
        CustomerBankAccount customerBankAccount = new CustomerBankAccount();
        customerBankAccount.setCbAccount(bankCardBindRespBO.getCardNo());
        customerBankAccount.setCbAccountName(mainInfo.getCmRealName());
        customerBankAccount.setCbBankCode(bankCardBindRespBO.getParentBankId());
        customerBankAccount.setCbBankName(bankCardBindRespBO.getParentBankName());
        customerBankAccount.setCbBranchName(bankCardBindRespBO.getBankNm());
        customerBankAccount.setCustomerId(mainInfo.getId());
        customerBankAccount.setCbInputDate(date);
        customerBankAccount.setCbBindPhone(bankCardBindRespBO.getMobileNo());
        customerBankAccount.setCbValid((short) 0);
        customerBankAccount.setCbModifyDate(date);
        customerBankAccountMapper.insert(customerBankAccount);

        //绑卡成功，更新用户绑卡开户标志
        CustomerMainInfo customerMainInfo = new CustomerMainInfo();
        customerMainInfo.setId(mainInfo.getId());
        customerMainInfo.setCmBindcardDate(date);
        customerMainInfo.setBankAccountId(String.valueOf(customerBankAccount.getId()));
        customerMainInfo.setCmModifyDate(new Date());
        customerMainInfoService.updateNotNull(customerMainInfo);
    }

    /**
     * 解绑卡
     *
     * @param data
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void zdpay1003(String data) {
        BankCardUnBindRespBO bankCardUnBindRespBO = JSON.parseObject(data, BankCardUnBindRespBO.class);
        CustomerMainInfo mainInfo = customerMainInfoService.queryCustomerInfoByLoginId(bankCardUnBindRespBO.getLoginId());
        if (StringUtils.isEmpty(mainInfo.getBankAccountId())) {
            log.info(mainInfo.getId() + ",该用户银行卡已解绑");
            return;
        }
        //查询用户需要解绑卡信息
        CustomerBankAccount bankAccount = customerBankAccountMapper.selectByPrimaryKey(Long.valueOf(mainInfo.getBankAccountId()));
        //银行卡标记为删除
        bankAccount.setCbValid(Short.valueOf("1"));
        bankAccount.setCbModifyDate(new Date());
        customerBankAccountMapper.updateByPrimaryKey(bankAccount);
        mainInfo.setBankAccountId(null);
        mainInfo.setCmModifyDate(new Date());
        customerMainInfoMapper.updateByPrimaryKey(mainInfo);
        //保存解绑操作记录
        bankCardService.persistUnbindingRecord(bankAccount, mainInfo, false);
        String key = KeyGenerator.USER_STATUS.getKey() + mainInfo.getId();
        if (redisSessionManager.exists(key)) {
            redisSessionManager.remove(key);
        }
    }

    /**
     * 更改银行预留手机号
     *
     * @param data
     */
    public void zdpay1004(String data) {
        ChangeMobileRespBO changeMobileRespBO = JSON.parseObject(data, ChangeMobileRespBO.class);
        CustomerMainInfo mainInfo = customerMainInfoService.queryCustomerInfoByLoginId(changeMobileRespBO.getLoginId());
        if (StringUtils.isEmpty(mainInfo.getBankAccountId())) {
            log.info(mainInfo.getId() + ",该用户未绑定银行卡");
            return;
        }
        CustomerBankAccount bankAccount = customerBankAccountMapper.selectByPrimaryKey(Long.valueOf(mainInfo.getBankAccountId()));
        String oldBindPhone = bankAccount.getCbBindPhone();
        bankAccount.setCbBindPhone(changeMobileRespBO.getNewMobile());
        bankAccount.setCbModifyDate(new Date());
        customerBankAccountMapper.updateByPrimaryKey(bankAccount);
        //保存操作记录
        bankCardService.persistUnbindBankPhoneRecord(bankAccount,mainInfo,oldBindPhone);
    }

    /**
     * 充值（快捷，收银台）
     * @param data
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void zdpay1008(String data){
        UserRechargeBO rechargeBO = JSON.parseObject(data, UserRechargeBO.class);
        CustomerMainInfo customerInfo = customerMainInfoService.queryCustomerInfoByLoginId(rechargeBO.getLoginId());
        BusiTradeFlow tradeFlow = busiTradeFlowMapper.queryTradeFlow(rechargeBO.getChannelOrderNo());
        if (tradeFlow == null) {
            throw new BusinessException("充值流水不存在！");
        }
        if (AppConstants.TradeStatusContants.PROCESS_SUCCESS.equals(tradeFlow.getStatus())) {
            log.info(rechargeBO.getChannelOrderNo() + "该笔订单已充值成功");
            return;
        }
        AccountOprDto accountOprDto = new AccountOprDto();
        accountOprDto.setAccountNo(customerInfo.getCmNumber());
        accountOprDto.setAmount(new BigDecimal(rechargeBO.getPayAmt()).divide(new BigDecimal(100)));
        accountOprDto.setOrderNo(rechargeBO.getChannelOrderNo());
        if (CustomerAccountType.LENDER.getValue().equals(customerInfo.getAccountType()) || CustomerAccountType.BORROWER.getValue().equals(customerInfo.getAccountType())) {
            accountOprDto.setAccountType(AccountWholeType.PERSONAL);
        } else if (CustomerAccountType.MARKETING.getValue().equals(customerInfo.getAccountType())){
            accountOprDto.setAccountType(AccountWholeType.COM_COUPON);
            //公司线下还款loginId
            SysParameter sysParameter = sysParameterService.findOneByPrType("com_repayment_offline_loginid");
            //挖财保证金用户列表
            SysParameter wacaiGuaranteeAccounts = sysParameterService.findOneByPrType("wacai_guarantee_accounts_fuiou_id");

            if (sysParameter == null) {
                throw new BusinessException("获取公司线下还款loginId失败！");
            }
            if (wacaiGuaranteeAccounts == null) {
                throw new BusinessException("挖财保证金用户列表失败！");
            }
            if (sysParameter.getPrValue().equals(rechargeBO.getLoginId())){
                accountOprDto.setAccountType(AccountWholeType.COM_REPAYMENT_OFFLINE);
            }

            //挖财保证金用户配置
            if (wacaiGuaranteeAccounts.getPrValue().indexOf(rechargeBO.getLoginId())>-1){
                accountOprDto.setAccountType(AccountWholeType.WC_GUARANTEE);
                accountOprDto.setAccountOprType(AccountOprType.WC_GUARANTEE_RECHARGE);
            }

        }
        accountOprDto.setPayChannelType("0".equals(rechargeBO.getClientType()) ? PayChannelType.WEB : PayChannelType.APP);
        accountOprDto.setSerialNo(rechargeBO.getMchntTxnSsn());
        accountOprDto.setTransNo("YM"+rechargeBO.getChannelOrderNo());
        log.info("----->>调用账户充值参数:" + JSONUtils.toJSON(accountOprDto));
        BusiTradeFlow temp = new BusiTradeFlow();
        ResultDto<AccountOprResultDto> resultDto = accountFacadeService.recharge(accountOprDto);
        if (!resultDto.isSuccess()) {
            temp.setId(tradeFlow.getId());
            temp.setStatus(AppConstants.TradeStatusContants.PROCESS_FAIL);
            log.error("收银台回调-账户充值失败，用户ID:{},失败原因:{}", customerInfo.getId(), resultDto.getMsg());
        } else {
            tradeFlow.setPaySeriNo(rechargeBO.getMchntTxnSsn());
            BusiTradeFlowDetail tradeFlowDetail = saveTradeFlowDetail(tradeFlow, customerInfo.getBankAccountId());
            temp.setId(tradeFlow.getId());
            temp.setStatus(AppConstants.TradeStatusContants.PROCESS_SUCCESS);
            temp.setAccountSeriNo(resultDto.getData().getRecordNum());
            temp.setFlowDetailId(tradeFlowDetail.getId());
        }
        temp.setPaySeriNo(rechargeBO.getMchntTxnSsn());
        // 更新充值流水
        busiTradeFlowMapper.updateByPrimaryKeySelective(temp);
    }

    /**
     * 线下转账充值
     * @param data
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
    public void zdpay1013(String data) {
        UserRechargeBO rechargeBO = JSON.parseObject(data, UserRechargeBO.class);
        CustomerMainInfo customerInfo = customerMainInfoService.queryCustomerInfoByLoginId(rechargeBO.getLoginId());
        BusiTradeFlow tradeFlow = busiTradeFlowMapper.queryTradeFlow(rechargeBO.getChannelOrderNo());
        if (tradeFlow == null) {
            // 保存充值流水
            tradeFlow = saveTradeFlow(rechargeBO, customerInfo);
        }
        if (AppConstants.TradeStatusContants.PROCESS_SUCCESS.equals(tradeFlow.getStatus())) {
            log.info(rechargeBO.getChannelOrderNo() + "该笔订单已充值成功");
            return;
        }
        AccountOprDto accountOprDto = new AccountOprDto();
        if (CustomerAccountType.LENDER.getValue().equals(customerInfo.getAccountType()) || CustomerAccountType.BORROWER.getValue().equals(customerInfo.getAccountType())) {
            accountOprDto.setAccountType(AccountWholeType.PERSONAL);
        } else if (CustomerAccountType.MARKETING.getValue().equals(customerInfo.getAccountType())){
            accountOprDto.setAccountType(AccountWholeType.COM_COUPON);
        }
        accountOprDto.setAccountNo(customerInfo.getCmNumber());
        accountOprDto.setAmount(new BigDecimal(rechargeBO.getPayAmt()).divide(new BigDecimal(100)));
        accountOprDto.setOrderNo(tradeFlow.getFlowNum());
        accountOprDto.setPayChannelType(PayChannelType.OFFLINE_RECHARGE);
        accountOprDto.setSerialNo(rechargeBO.getMchntTxnSsn());
        String transNo = "XX" + rechargeBO.getMchntTxnSsn();
        accountOprDto.setTransNo(transNo);
        log.info("----->>调用账户充值参数:" + JSONUtils.toJSON(accountOprDto));
        BusiTradeFlow temp = new BusiTradeFlow();
        ResultDto<AccountOprResultDto> resultDto = accountFacadeService.recharge(accountOprDto);
        if (!resultDto.isSuccess()) {
            temp.setId(tradeFlow.getId());
            temp.setStatus(AppConstants.TradeStatusContants.PROCESS_FAIL);
            log.error("收银台回调-账户充值失败，用户ID:{},失败原因:{}", customerInfo.getId(), resultDto.getMsg());
        } else {
            tradeFlow.setPaySeriNo(rechargeBO.getMchntTxnSsn());
            BusiTradeFlowDetail tradeFlowDetail = saveTradeFlowDetail(tradeFlow, customerInfo.getBankAccountId());
            temp.setId(tradeFlow.getId());
            temp.setStatus(AppConstants.TradeStatusContants.PROCESS_SUCCESS);
            temp.setAccountSeriNo(resultDto.getData().getRecordNum());
            temp.setFlowDetailId(tradeFlowDetail.getId());
        }
        temp.setPaySeriNo(rechargeBO.getMchntTxnSsn());
        // 更新充值流水
        busiTradeFlowMapper.updateByPrimaryKeySelective(temp);
    }

    /**
     * 用户授权
     * @param data
     */
    public void zdpay1010(String data){
        UserGrantBO userGrantBO = JSON.parseObject(data, UserGrantBO.class);
        CustomerMainInfo customerInfo = customerMainInfoService.queryCustomerInfoByLoginId(userGrantBO.getLoginId());
        UserGrantBO userGrant = customerGrantInfoMapper.queryUserGrant(customerInfo.getId());
        UserOpenBO userOpenBO = new UserOpenBO();
        CopyUtil.copyProperties(userOpenBO, userGrantBO);
        if (StringUtils.isEmpty(userGrantBO.getAuthStatus())) {
            throw new BusinessException("授权参数异常！");
        }
        if (userGrant == null) {
            saveUserOpenInfo(customerInfo, userOpenBO);
        } else {
            userGrantBO.setCustomerId(customerInfo.getId());
            customerGrantInfoMapper.updateCustomerGrantInfo(userGrantBO);
        }
    }

    /**
     * @Author: weiNian
     * @param data
     * @Description:
     * @Date: 2018/8/20 17:31
     */
    public void zdpay1009(String data,String code,String msg,String interfaceCode){
        WithdrawBo withdrawBo = JSON.parseObject(data, WithdrawBo.class);
        BusiTradeFlow tradeFlow = busiTradeFlowMapper.findTradeConcentrationByFlownum(withdrawBo.getChannelOrderNo());
        log.info("zdpay1009提现回调data：{}  code：{}   msg：{}  interfaceCode：{}",data,code,msg,interfaceCode);
        if (tradeFlow == null) {
            log.error("提现订单号："+withdrawBo.getChannelOrderNo()+"， 未找到对应提现订单流水记录！");
            throw new BusinessException("提现订单号："+withdrawBo.getChannelOrderNo()+"， 未找到对应提现订单流水记录");
        }

        //账户提现对象
        AccountOprDto accountOprDto = new AccountOprDto();
        CustomerMainInfo customerInfo = customerMainInfoService.queryCustomerInfoByLoginId(withdrawBo.getLoginId());
        BigDecimal amount = Convert.toBigDecimal( withdrawBo.getPayAmt()).divide(Convert.toBigDecimal( "100"));
        accountOprDto.setAmount(amount);
        accountOprDto.setOrderNo(withdrawBo.getChannelOrderNo());
        accountOprDto.setLendLoginId(customerInfo.getFuiouLoginId());
        accountOprDto.setFeeLoginId(CompanyAccounts.getCompanyAccounts().getJgsyAccountFuiouId());
        accountOprDto.setFeeAmount(tradeFlow.getServiceCharge());
        accountOprDto.setFeeAccountNO(CompanyAccounts.getCompanyAccounts().getJgsyAccount());
        //设置账户编号及类型
        if (withdrawBo.getLoginId().equals(CompanyAccounts.getCompanyAccounts().getJgsyAccountFuiouId()) ) {
            //本机构收益提现
            accountOprDto.setAccountType(AccountWholeType.ORG_INCOME);
            accountOprDto.setAccountNo(CompanyAccounts.getCompanyAccounts().getJgsyAccount());
        }else if (withdrawBo.getLoginId().equals(CompanyAccounts.getCompanyAccounts().getHzjgsyAccountFuiouId()) ) {
            //合作机构收益提现
            accountOprDto.setAccountType(AccountWholeType.COM_ORG_PARTNER);
            accountOprDto.setAccountNo(CompanyAccounts.getCompanyAccounts().getHzjgsyAccount());
        }else{
            //个人提现
            accountOprDto.setAccountType(AccountWholeType.PERSONAL);
            accountOprDto.setAccountNo(customerInfo.getCmNumber());
        }

        //提现流水对象
        tradeFlow.setCustomerId(customerInfo.getId());
        tradeFlow.setPaySeriNo(withdrawBo.getMchntTxnSsn());
        //查询用户需要充值卡信息
        CustomerBankAccount bankAccount = customerBankAccountMapper.selectBankAccountByBankCodeIgnoreDeletion(tradeFlow.getBankCardNum());
        tradeFlow.setFlowNum(withdrawBo.getChannelOrderNo());
        tradeFlow.setBankName(bankAccount.getCbBankName());
        tradeFlow.setBankCardNum(bankAccount.getCbAccount());
        tradeFlow.setBankCardId(bankAccount.getId());

        //提现成功
        if ("0000".equals(code)){
            withdrawing(bankAccount, customerInfo ,withdrawBo , tradeFlow , accountOprDto);

        }
        //提现退款
        else if ("1111".equals(code)){
            withdrawRefund(customerInfo ,withdrawBo , tradeFlow , accountOprDto);
            wacaiRepaymentFreeze(withdrawBo.getMchntTxnSsn(),amount,customerInfo.getCmNumber());
        }
        //收银台其他异常情况
        else {

            //更新提现流水
            HashMap infosMap = new HashMap();
            List statusList = new ArrayList();
            statusList.add("0");//申请中
            infosMap.put("initStatus",statusList);
            infosMap.put("status",AppConstants.TradeStatusContants.PROCESSING);
            infosMap.put("id",tradeFlow.getId());
            int num = busiTradeFlowMapper.updateStatusOfBusiTradeFlow(infosMap);
            if ( num>0 ){

                //如果是个人用户提现失败，造个人账户进流水 及解冻流水
                if (AccountWholeType.PERSONAL.getValue().equals(accountOprDto.getAccountType().getValue())){
                    String accountSeriNo = personalWithdrawRefund(accountOprDto.getOrderNo(),customerInfo,accountOprDto.getAmount(), tradeFlow);
                    tradeFlow.setAccountSeriNo(accountSeriNo);
                }
                tradeFlow.setStatus(AppConstants.TradeStatusContants.PROCESS_FAIL);
                busiTradeFlowMapper.updateByPrimaryKeySelective(tradeFlow);

                //保存提现流水Detail
                BusiTradeFlowDetail busiTradeFlowDetail = saveTradeFlowDetail(tradeFlow, customerInfo.getBankAccountId());
                tradeFlow.setFlowDetailId(busiTradeFlowDetail.getId());
                log.error("收银台提现回调结果失败,订单号:{}，接口号{},失败原因:{}",tradeFlow.getFlowNum(),interfaceCode,  msg);
            }else{
                log.error("提现异常，该订单原始状态不是申请中 提现订单号："+withdrawBo.getChannelOrderNo()+"，客户号：" + customerInfo.getCmNumber() + "，提现金额：" + amount);
            }
        }
        if(CreditConstant.WACAI_CHANNEL.getCode().equals(customerInfo.getChannelCode())){
            notifyAssets(tradeFlow.getId());
        }
        notifyCredit(tradeFlow.getId());
    }

    /**
     * @Author: xuyt
     * @param data
     * @Description: 修改/重置手机号
     */
    public void zdpay1005(String data){
        PasswordModifyBO passwordModify = JSON.parseObject(data, PasswordModifyBO.class);
        CustomerMainInfo customerInfo = customerMainInfoService.queryCustomerInfoByLoginId(passwordModify.getLoginId());
        CustomerMainInfo customer = new CustomerMainInfo();
        customer.setId(customerInfo.getId());
        customer.setIsSetPwd(1);
        customer.setCmModifyDate(new Date());
        customerMainInfoService.updateNotNull(customer);
    }



    /**
     * @title 挖财还款冻结
     * @description 挖财提现弃借，余额返还款冻结户
     * @author weiNian
     * @param: serialNo 提现流水号
     * @param: amount 提现金额
     * @param: customerInfo 用户对象
     * @updateTime 2019/4/15 11:19
     * @throws
     */
    public Result wacaiRepaymentFreeze(String serialNo,BigDecimal amount,String cmNumber ){

        log.info("挖财提现弃借，余额返还款冻结户。订单号：{} ,用户编号：{}",serialNo,cmNumber);
        String transNo ="";
        try{
            CustomerMainInfo customerInfo = customerMainInfoService.findOneByCmNumber(cmNumber);
            if(!CreditConstant.WACAI_CHANNEL.getCode().equals(customerInfo.getChannelCode())){
                log.error("还款冻结户失败，用户编号{}不属于挖财用户",cmNumber);
                return Result.fail("该用户不属于挖财用户，不能还款冻结");
            }

            // 校验可冻结金额是否充足
            BigDecimal accountBalance = accountOverview520003Service.getAccountBalance(customerInfo);
            if (accountBalance.compareTo(amount) == -1) {
                log.error("提现，客户号：{}，还款冻结金额：{}， 可冻结余额为：{} 余额不足！",customerInfo.getCmNumber(),amount.toString(),accountBalance.toString());
                return Result.fail("可冻结余额不足！");
            }

            AccountRepayCollectDto accountRepayCollectDto =new AccountRepayCollectDto();
            transNo = "WACAI"+serialNo;
            accountRepayCollectDto.setTransNo(transNo);

            List<AccountRepayCollectDto.AccountDetail> accounts = new ArrayList<>();

            //借款人账户->借款人还款专户 出流水
            AccountRepayCollectDto.AccountDetail outAccount = new AccountRepayCollectDto.AccountDetail();
            outAccount.setServiceCode(serialNo);
            outAccount.setServiceName(customerInfo.getCmRealName()+"挖财提现失败");
            outAccount.setAmount(amount);
            outAccount.setRemark(serialNo);
            outAccount.setAccountType(AccountWholeType.PERSONAL);
            //借款人账户号出
            outAccount.setAccountNo(customerInfo.getCmNumber());
            outAccount.setAccountOprDirection(AccountOprDirection.OUT);
            //划扣
            outAccount.setAccountOprType(AccountOprType.DRAW);
            //入借款人还款专户
            outAccount.setRelAccountNo(AccountWholeType.BORROW_REPAYMENT_DEDICATED.name()+customerInfo.getCmNumber());
            outAccount.setRelAccountType(AccountWholeType.BORROW_REPAYMENT_DEDICATED);
            accounts.add(outAccount);

            //借款人账户->借款人还款专户 进流水
            AccountRepayCollectDto.AccountDetail inAccount = new AccountRepayCollectDto.AccountDetail();
            inAccount.setServiceCode(outAccount.getServiceCode());
            inAccount.setServiceName(outAccount.getServiceName());
            inAccount.setAmount(outAccount.getAmount());
            inAccount.setRemark(outAccount.getRemark());
            inAccount.setAccountType(AccountWholeType.BORROW_REPAYMENT_DEDICATED);
            inAccount.setAccountNo(outAccount.getRelAccountNo());
            inAccount.setAccountOprDirection(AccountOprDirection.IN);
            inAccount.setAccountOprType(AccountOprType.BORROW_ONLINE_DEDUCTION);
            inAccount.setRelAccountType(AccountWholeType.PERSONAL);
            inAccount.setRelAccountNo(outAccount.getAccountNo());
            accounts.add(inAccount);
            accountRepayCollectDto.setAccountDetails(accounts);

            log.info("挖财提现余额返还款冻结户,通知流水号{} 流水信息{}, ",transNo,JSON.toJSONString(accountRepayCollectDto));
            ResultDto<List<ResultDto<AccountOprResultDto>>>  resultDto =  accountFacadeService.repayCollect(accountRepayCollectDto);

            if (resultDto.isSuccess()){
                MailUtil.sendMail("挖财提现弃借成功通知","挖财提现弃借成功，余额返还款冻结户。订单号："+serialNo+"  用户编号："+cmNumber+"  弃借金额："+amount.toString());
                return Result.success("余额转还款冻结户成功");
            }else {
                MailUtil.sendMail("挖财提现弃借失败通知","挖财提现弃借成功，余额返还款冻结户。订单号："+serialNo+"  用户编号："+cmNumber+"  弃借金额："+amount.toString()+" \n原因："+ JSON.toJSONString(resultDto));
                return Result.fail("余额转还款冻结户失败，原因："+resultDto.getMsg());
            }

        }
        catch (Exception e){
            log.error("挖财提现余额返还款冻结户异常,通知流水号{} ",transNo,e);
            MailUtil.sendMail("挖财提现弃借异常,提现流水号："+transNo, "挖财提现弃借失败。订单号："+serialNo+"  用户编号："+cmNumber+"  弃借金额："+amount.toString()+"\n异常原因:" + e);
            return Result.fail("余额转还款冻结户异常，提现流水号:"+transNo+"  异常原因:" + e.getMessage());
        }
    }


    /**
     * @Author: weiNian
     * @param orderNo
     * @param customerInfo
     * @param amount
     * @param tradeFlow
     * @Description: 个人提现退款，通知账户两调流水：个人账户进流水 及解冻流水
     * @Date: 2018/10/8 11:25
     */
    public String personalWithdrawRefund(String orderNo,CustomerMainInfo customerInfo,BigDecimal amount, BusiTradeFlow tradeFlow){
        String accountSeriNo ="";
        //提现解冻流水
        String txjdTransNo = "TXJD"+orderNo;
        WithdrawRefundDto withdrawRefundDto = new WithdrawRefundDto();
        withdrawRefundDto.setTransNo(txjdTransNo);
        withdrawRefundDto.setOrderNo(orderNo);
        withdrawRefundDto.setLendLoginId(customerInfo.getFuiouLoginId());
        withdrawRefundDto.setCouponLoginId(CompanyAccounts.getCompanyAccounts().getGshbAccountFuiouId());
        List<WithdrawRefundDto.AccountDetail> accountDetails = new ArrayList<>();
        //个人账户进
        WithdrawRefundDto.AccountDetail accountIn = new WithdrawRefundDto.AccountDetail();
        accountIn.setAccountNo(customerInfo.getCmNumber());
        accountIn.setAccountType(AccountWholeType.PERSONAL);
        accountIn.setAccountOprType(AccountOprType.WITHDRAW_APPLY_FAILURE);
        accountIn.setAmount(amount);
        accountIn.setAccountOprDirection(AccountOprDirection.IN);
        accountIn.setRelAccountNo(AccountWholeType.PERSONAL_WITHDRAW_FRZ + customerInfo.getCmNumber());
        accountIn.setRelAccountType(AccountWholeType.PERSONAL_WITHDRAW_FRZ);
        accountDetails.add(accountIn);

        //冻结户出
        WithdrawRefundDto.AccountDetail accountOut = new WithdrawRefundDto.AccountDetail();
        accountOut.setAccountNo(accountIn.getRelAccountNo());
        accountOut.setAccountType(accountIn.getRelAccountType());
        accountOut.setAccountOprType(AccountOprType.PERSONAL_WITHDRAW_FRZ_WITHDRAW_UNFRZ);
        accountOut.setAmount(accountIn.getAmount());
        accountOut.setAccountOprDirection(AccountOprDirection.OUT);
        accountOut.setRelAccountNo(accountIn.getAccountNo());
        accountOut.setRelAccountType(accountIn.getAccountType());
        accountDetails.add(accountOut);

        if (AppConstants.WITHDRAW_TYPE.WITHDRAW_TYPE_2 == tradeFlow.getType() && !CreditConstant.WACAI_CHANNEL.getCode().equals(customerInfo.getChannelCode())) {
            //个人手续费账户进
            WithdrawRefundDto.AccountDetail accountFeeIn = new WithdrawRefundDto.AccountDetail();
            accountFeeIn.setAccountNo(customerInfo.getCmNumber());
            accountFeeIn.setAccountType(AccountWholeType.PERSONAL);
            accountFeeIn.setAccountOprType(AccountOprType.WITHDRAW_FEE_REFUND);
            accountFeeIn.setAmount(tradeFlow.getServiceCharge());
            accountFeeIn.setAccountOprDirection(AccountOprDirection.IN);
            accountFeeIn.setRelAccountNo(AccountWholeType.PERSONAL_WITHDRAW_FRZ + customerInfo.getCmNumber());
            accountFeeIn.setRelAccountType(AccountWholeType.PERSONAL_WITHDRAW_FRZ);
            accountDetails.add(accountFeeIn);

            //冻结手续费出
            WithdrawRefundDto.AccountDetail accountFeeOut = new WithdrawRefundDto.AccountDetail();
            accountFeeOut.setAccountNo(accountFeeIn.getRelAccountNo());
            accountFeeOut.setAccountType(accountFeeIn.getRelAccountType());
            accountFeeOut.setAccountOprType(AccountOprType.PERSONAL_WITHDRAW_FRZ_FEE_UNFRZ);
            accountFeeOut.setAmount(tradeFlow.getServiceCharge());
            accountFeeOut.setAccountOprDirection(AccountOprDirection.OUT);
            accountFeeOut.setRelAccountNo(accountFeeIn.getAccountNo());
            accountFeeOut.setRelAccountType(accountFeeIn.getAccountType());
            accountDetails.add(accountFeeOut);
        }

        withdrawRefundDto.setFeeAmount(tradeFlow.getServiceCharge());
        withdrawRefundDto.setAccountDetails(accountDetails);
        log.info("提现订单：{} 解冻，入参：{}", orderNo ,JSON.toJSONString(withdrawRefundDto));
        ResultDto<List<AccountOprResultDto>> resultDto = accountFacadeService.withdrawRefund(withdrawRefundDto);
        log.info("提现订单：{} 解冻，入参：{} ，账户返回结果{}", orderNo ,JSON.toJSONString(withdrawRefundDto), JSON.toJSONString(resultDto));
        //记录账户退款流水
        if (resultDto!=null && resultDto.isSuccess()
                && resultDto.getData()!=null
                && resultDto.getData().size()>0) {
            for(AccountOprResultDto accountOprResultDtoItem : resultDto.getData()){
                if (AccountOprType.WITHDRAW_APPLY_FAILURE.getCode().equals(accountOprResultDtoItem.getAccountOprType().getCode())){
                    accountSeriNo = accountOprResultDtoItem.getRecordNum();
                    break;
                }
            }

            wacaiRepaymentFreeze(orderNo,amount,customerInfo.getCmNumber());
        }else {
            log.error("个人提现退款 账户返回结果异常,订单号:{}，账户返回内容:{}",orderNo,JSON.toJSONString(resultDto));
        }
        return accountSeriNo;
    }

    /**
     * @Author: weiNian
     * @param bankAccount
     * @param customerInfo
     * @param withdrawBo
     * @param tradeFlow
     * @param accountOprDto
     * @Description:  提现
     * @Date: 2018/10/6 17:27
     */
    public void withdrawing(CustomerBankAccount bankAccount, CustomerMainInfo customerInfo ,WithdrawBo withdrawBo , BusiTradeFlow tradeFlow , AccountOprDto accountOprDto){
        ResultDto<AccountOprResultDto> resultDto=null;
        boolean withdrawStatus = true;
        BigDecimal amount = accountOprDto.getAmount();
        String bankCard =bankAccount.getCbBankCode();

        //更新提现流水
        HashMap infosMap = new HashMap();
        List statusList = new ArrayList();
        statusList.add(AppConstants.TradeStatusContants.INIT);//申请中
        statusList.add(AppConstants.TradeStatusContants.PROCESS_FAIL);//申请失败
        infosMap.put("initStatus",statusList);
        infosMap.put("status",AppConstants.TradeStatusContants.PROCESSING);
        infosMap.put("id",tradeFlow.getId());
        int num = busiTradeFlowMapper.updateStatusOfBusiTradeFlow(infosMap);

        if ( num>0 ){
            try {
                String transNo = "WTX" +withdrawBo.getChannelOrderNo();
                accountOprDto.setTransNo(transNo);
                accountOprDto.setThirdRequestNo(withdrawBo.getMchntTxnSsn());
                log.info("提现，提现流水号：{}，账户入参内容:{}",withdrawBo.getChannelOrderNo(),JSON.toJSONString(accountOprDto));
                if (AccountWholeType.PERSONAL.getValue().equals(accountOprDto.getAccountType().getValue())){
                    resultDto = accountFacadeService.withdraw(accountOprDto);
                    if (resultDto.getCode().equals(resultDto.ERROR_FEE_CODE)) {
                        MailUtil.sendMail("账户提现解冻成功, 存管缴费失败", "用户ID:" +customerInfo.getId()+",账户提现解冻成功, 存管缴费失败!");
                    }
                    log.info("个人提现，提现流水号：{}，提现金额：{}，账户返回内容:{}",withdrawBo.getChannelOrderNo(),amount,JSON.toJSONString(resultDto));
                }else {
                    resultDto = accountFacadeService.platformWithdraw(accountOprDto);
                    log.info("平台提现，提现流水号：{}，提现金额：{}，账户返回内容:{}",withdrawBo.getChannelOrderNo(),amount,JSON.toJSONString(resultDto));
                }
            } catch (Exception e) {
                withdrawStatus =false;
                log.error("收银台回调-账户提现失败，提现流水号:{},失败原因:{}", withdrawBo.getChannelOrderNo(), e);
            }

            //保存提现流水Detail
            BusiTradeFlowDetail busiTradeFlowDetail = saveTradeFlowDetail(tradeFlow, customerInfo.getBankAccountId());
            tradeFlow.setFlowDetailId(busiTradeFlowDetail.getId());

            //通知提现成功流水
            if (withdrawStatus && (resultDto.getCode().equals(resultDto.ERROR_FEE_CODE) || resultDto.isSuccess())) {
                tradeFlow.setStatus(AppConstants.TradeStatusContants.PROCESS_SUCCESS);
                busiTradeFlowMapper.updateByPrimaryKeySelective(tradeFlow);
                log.info("提现成功，保存提现流水成功！提现流水号：" + withdrawBo.getChannelOrderNo() +",银行卡号："+ bankCard +"，提现金额：" + amount );
            }
            //通知账户失败
            else {
                tradeFlow.setStatus(AppConstants.TradeStatusContants.PROCESS_FAIL);
                busiTradeFlowMapper.updateByPrimaryKeySelective(tradeFlow);
                log.error("收银台回调-通知账户提现失败，提现流水号:{}", withdrawBo.getChannelOrderNo());
            }
        }else{
            log.error("提现异常，该订单原始状态不是申请中或申请失败  提现订单号："+withdrawBo.getChannelOrderNo()+"，客户号：" + customerInfo.getCmNumber() + "，提现金额：" + amount);
        }

    }

    /**
     * @Author: weiNian
     * @param customerInfo
     * @param withdrawBo
     * @param tradeFlow
     * @param accountOprDto
     * @Description: 提现退款
     * @Date: 2018/10/6 17:31
     */
    public void withdrawRefund(CustomerMainInfo customerInfo ,WithdrawBo withdrawBo , BusiTradeFlow tradeFlow , AccountOprDto accountOprDto){

        ResultDto<List<AccountOprResultDto>> accountOprResultDtoList=null;
        boolean withdrawStatus = true;
        BigDecimal amount = accountOprDto.getAmount();


        //更新提现流水
        HashMap infosMap = new HashMap();
        List statusList = new ArrayList();
        //提现成功
        statusList.add(AppConstants.TradeStatusContants.PROCESS_SUCCESS);
        infosMap.put("initStatus",statusList);
        //提现退款中
        infosMap.put("status",AppConstants.TradeStatusContants.WITHDRAW_REFUND_START);
        infosMap.put("id",tradeFlow.getId());
        int num = busiTradeFlowMapper.updateStatusOfBusiTradeFlow(infosMap);
        if ( num>0 ){

            //更新提现退款时间 到BusiTradeFlowDetail
            BusiTradeFlowDetail busiTradeFlowDetail = new BusiTradeFlowDetail();
            busiTradeFlowDetail.setFlowNum(tradeFlow.getFlowNum());
            BusiTradeFlowDetail busiTradeFlowDetailResult = tradeFlowDetailMapper.selectByCondition(busiTradeFlowDetail);
            busiTradeFlowDetailResult.setDealTime(new Date());
            tradeFlowDetailMapper.updateById(busiTradeFlowDetailResult);

            try {
                //提现退款流水
                String transNo = "TXTK"+withdrawBo.getChannelOrderNo();
                WithdrawRefundDto withdrawRefundDto =new WithdrawRefundDto();
                withdrawRefundDto.setTransNo(transNo);
                withdrawRefundDto.setOrderNo(withdrawBo.getChannelOrderNo());
                withdrawRefundDto.setFeeAmount(tradeFlow.getServiceCharge());
                withdrawRefundDto.setLendLoginId(customerInfo.getFuiouLoginId());
                withdrawRefundDto.setCouponLoginId(CompanyAccounts.getCompanyAccounts().getGshbAccountFuiouId());
                withdrawRefundDto.setCouponAccountNo(CompanyAccounts.getCompanyAccounts().getGshbAccount());
                //个人提现退款造一条流水
                if (AccountWholeType.PERSONAL.getValue().equals(accountOprDto.getAccountType().getValue())){
                    List<WithdrawRefundDto.AccountDetail> accountDetails = new ArrayList<>();
                    WithdrawRefundDto.AccountDetail accountDetail = new WithdrawRefundDto.AccountDetail();
                    accountDetail.setAccountNo(customerInfo.getCmNumber());
                    accountDetail.setRecordNum(tradeFlow.getAccountSeriNo());
                    accountDetail.setAccountType(AccountWholeType.PERSONAL);
                    accountDetail.setAccountOprType(AccountOprType.WITHDRAW_REFUND);
                    accountDetail.setAmount(amount);
                    accountDetail.setAccountOprDirection(AccountOprDirection.IN);
                    accountDetails.add(accountDetail);
                    withdrawRefundDto.setAccountDetails(accountDetails);
                }
                //平台提现退款造一条流水
                else {
                    List<WithdrawRefundDto.AccountDetail> accountDetails = new ArrayList<>();
                    WithdrawRefundDto.AccountDetail accountDetail = new WithdrawRefundDto.AccountDetail();
                    accountDetail.setAccountNo(accountOprDto.getAccountNo());
                    //合作机构收益户 COM_PARTNER_WITHDRAW_REFUND
                    if (accountOprDto.getAccountType().getValue().equals(AccountWholeType.COM_ORG_PARTNER.getValue())){
                        accountDetail.setAccountOprType(AccountOprType.COM_PARTNER_WITHDRAW_REFUND);
                    }
                    //收益户 事件类型 ORG_INCOME_WITHDRAW_REFUND
                    else {
                        accountDetail.setAccountOprType(AccountOprType.ORG_INCOME_WITHDRAW_REFUND);
                    }
                    accountDetail.setAccountType(accountOprDto.getAccountType());
                    accountDetail.setRecordNum(tradeFlow.getAccountSeriNo());
                    accountDetail.setAmount(amount);
                    accountDetail.setAccountOprDirection(AccountOprDirection.IN);
                    accountDetails.add(accountDetail);
                    withdrawRefundDto.setAccountDetails(accountDetails);
                }

                log.info("提现退款，客户号：{} ，提现退回金额：{}，账户入参：{}",customerInfo.getCmNumber(),amount,JSON.toJSONString(withdrawRefundDto));
                accountOprResultDtoList = accountFacadeService.withdrawRefund(withdrawRefundDto);
                log.info("提现退款成功，客户号：{} ，提现退回金额：{}，账户端返回内容：{}",customerInfo.getCmNumber(),amount,JSON.toJSONString(accountOprResultDtoList));
            } catch (Exception e) {
                withdrawStatus =false;
                log.error("收银台回调-账户提现退款，用户ID:{},失败原因:{}", customerInfo.getId(), e);
            }

            //更新提现退款流水
            if (withdrawStatus && accountOprResultDtoList.getData()!=null
                    && accountOprResultDtoList.isSuccess()
                    && accountOprResultDtoList.getData().size()>0) {
                String accountSeriNo ="";
                for(AccountOprResultDto accountOprResultDtoItem : accountOprResultDtoList.getData()){
                    if (AccountOprType.COM_PARTNER_WITHDRAW_REFUND.getCode().equals(accountOprResultDtoItem.getAccountOprType().getCode())
                    ||AccountOprType.ORG_INCOME_WITHDRAW_REFUND.getCode().equals(accountOprResultDtoItem.getAccountOprType().getCode())
                    ||AccountOprType.WITHDRAW_REFUND.getCode().equals(accountOprResultDtoItem.getAccountOprType().getCode())){
                        accountSeriNo = accountOprResultDtoItem.getRecordNum();
                        break;
                    }
                }
                tradeFlow.setStatus(AppConstants.TradeStatusContants.WITHDRAW_REFUND_SUCCESS);
                tradeFlow.setAccountSeriNo(accountSeriNo);
                busiTradeFlowMapper.updateByPrimaryKeySelective(tradeFlow);
            }else {
                tradeFlow.setStatus(AppConstants.TradeStatusContants.WITHDRAW_REFUND_FAIL);
                busiTradeFlowMapper.updateByPrimaryKeySelective(tradeFlow);
                String msg ="调用账户提现接口异常";
                if (accountOprResultDtoList!=null && !StringUtil.isEmpty(accountOprResultDtoList.getMsg())){
                    msg =accountOprResultDtoList.getMsg();
                }
                log.error("收银台回调-账户提现退回失败，用户ID:{},失败原因:{}", customerInfo.getId(), msg);
            }
        }else{
            log.error("提现退款异常，该订单原始状态不是提现成功 提现订单号："+withdrawBo.getChannelOrderNo()+"，客户号：" + customerInfo.getCmNumber() + "，提现金额：" + amount);
        }
    }

    /**
     * 保存充值流水
     *
     * @param rechargeBO
     * @param customerInfo
     */
    public BusiTradeFlow saveTradeFlow(UserRechargeBO rechargeBO, CustomerMainInfo customerInfo) {
        String flowNum = busiOrderService.buildCeNumber("R", "8888", customerInfo.getId());
        BusiTradeFlow tradeFlow = new BusiTradeFlow();
        tradeFlow.setFlowNum(flowNum);
        tradeFlow.setTrdDate(new Date());
        tradeFlow.setType(103);
        tradeFlow.setTrdType(AppConstants.TradeStatusContants.RECHARGEING);
        BigDecimal amount = Convert.toBigDecimal(rechargeBO.getPayAmt()).divide(Convert.toBigDecimal( "100"));
        tradeFlow.setTrdAmt(amount);
        tradeFlow.setCustomerId(customerInfo.getId());
        tradeFlow.setStatus(AppConstants.TradeStatusContants.INIT);
        tradeFlow.setPaySeriNo(rechargeBO.getMchntTxnSsn());
        if (StringUtils.isNotEmpty(customerInfo.getBankAccountId())){
            CustomerBankAccount account = customerBankAccountMapper.selectByPrimaryKey(Long.valueOf(customerInfo.getBankAccountId()));
            if (account != null) {
                tradeFlow.setBankName(account.getCbBankName());
                tradeFlow.setBankCardId(account.getId());
                tradeFlow.setBankCardNum(account.getCbAccount());
            }
        }
        busiTradeFlowMapper.insert(tradeFlow);
        return tradeFlow;
    }

    /**
     * 保存充值流水详情
     *
     * @param tradeFlow
     */
    public BusiTradeFlowDetail saveTradeFlowDetail(BusiTradeFlow tradeFlow, String accountId){
        BusiTradeFlowDetail tradeFlowDetail = new BusiTradeFlowDetail();
        CustomerBankAccount account = null;

        if(!StringUtil.isEmpty(accountId)){
            account = customerBankAccountMapper.selectByPrimaryKey(Long.valueOf(accountId));
        }else {
            account = customerBankAccountMapper.selectBankAccountByBankCodeIgnoreDeletion(tradeFlow.getBankCardNum());
        }

        if (account != null) {
            tradeFlowDetail.setBankName(account.getCbBankName());
            tradeFlowDetail.setBankCode(account.getCbBankCode());
            tradeFlowDetail.setSubBankCode(account.getCbSubBankCode());
            tradeFlowDetail.setSubBankName(account.getCbBranchName());
            tradeFlowDetail.setBankCardNum(account.getCbAccount());
        }
        tradeFlowDetail.setFlowNum(tradeFlow.getFlowNum());
        tradeFlowDetail.setPaySerialno(tradeFlow.getPaySeriNo());
        tradeFlowDetail.setCreator("WS_SYSTEM");
        Date date = new Date();
        tradeFlowDetail.setDealTime(date);
        tradeFlowDetail.setCreateTime(date);
        tradeFlowDetailMapper.insert(tradeFlowDetail);
        return tradeFlowDetail;
    }

    /**
     * 保存用户授权信息
     * @param mainInfo
     * @param userOpen
     */
    public void saveUserOpenInfo(CustomerMainInfo mainInfo, UserOpenBO userOpen){
        if ("000000000000".equals(userOpen.getAuthStatus())) {
            return;
        }
        UserGrantBO userGrant = new UserGrantBO();
        userGrant.setCustomerId(mainInfo.getId());
        userGrant.setUserName(mainInfo.getCmRealName());
        userGrant.setCellPhone(userOpen.getMobileNo());
        userGrant.setAuthStatus(userOpen.getAuthStatus());
        userGrant.setUserAttr(userOpen.getUserAttr());
        userGrant.setAutoLendAmt(userOpen.getAutoLendAmt());
        userGrant.setAutoLendTerm(userOpen.getAutoLendTerm());
        userGrant.setAutoFeeAmt(userOpen.getAutoFeeAmt());
        userGrant.setAutoFeeTerm(userOpen.getAutoFeeTerm());
        userGrant.setLeaveLendAmt(userOpen.getLeaveLendAmt());
        userGrant.setUsedLendAmt(userOpen.getUsedLendAmt());
        userGrant.setAutoRepayAmt(userOpen.getAutoRepayAmt());
        userGrant.setAutoRepayTerm(userOpen.getAutoRepayTerm());
        userGrant.setAutoCompenAmt(userOpen.getAutoCompenAmt());
        userGrant.setAutoCompenTerm(userOpen.getAutoCompenTerm());
        customerGrantInfoMapper.saveCustomerGrantInfo(userGrant);
    }

    /**
     * 保存用户绑定的银行卡信息
     * @param mainInfo
     * @param userOpen
     */
    public long saveBankCard(CustomerMainInfo mainInfo, UserOpenBO userOpen){
        CustomerBankAccount customerBankAccount = new CustomerBankAccount();
        customerBankAccount.setCbAccount(userOpen.getCardNo());
        customerBankAccount.setCbAccountName(mainInfo.getCmRealName());
        customerBankAccount.setCbBankCode(userOpen.getParentBankId());
        customerBankAccount.setCbBankName(userOpen.getParentBankName());
        customerBankAccount.setCbBranchName(userOpen.getBankNm());
        customerBankAccount.setCustomerId(mainInfo.getId());
        customerBankAccount.setCbInputDate(new Date());
        customerBankAccount.setCbBindPhone(userOpen.getMobileNo());
        customerBankAccount.setCbValid((short) 0);
        customerBankAccount.setCbMemo("开户时回调绑卡");
        customerBankAccountMapper.insert(customerBankAccount);
        return customerBankAccount.getId();
    }

    @Async
    private void notifyCredit(Long flowId){
        log.info("提现申请回调前前----start:tradeFlowId= " + flowId);
        BusiTradeFlow flow = busiTradeFlowMapper.selectByPrimaryKey(flowId);
        CustomerMainInfo mainInfo = customerMainInfoMapper.selectByCustomerId(flow.getCustomerId());
        if(mainInfo == null){
            log.info("提现申请回调接口异常，查询不到用户:"+flow.getCustomerId());
            return;
        }
        if(!"credit".equals(mainInfo.getCmOpenPlatform()) || !"credit".equals(mainInfo.getCmOpenChannel()) ||
                (mainInfo.getChannelCode()!=null && !"".equals(mainInfo.getChannelCode()))
                ){
            log.info("该用户不是前前渠道：" + mainInfo.getId());
            return;
        }
        NotifyCreditLog creditLog = new NotifyCreditLog();

        //组装记录流水
        String status = flow.getStatus();
        Integer notifyType;
        creditLog.setApplyNo(flow.getFlowNum());
        creditLog.setCustomerId(flow.getCustomerId());
        if (AppConstants.TradeStatusContants.PROCESS_SUCCESS.equals(status)) {
            notifyType = 2;
        } else if (AppConstants.TradeStatusContants.PROCESS_FAIL.equals(status)) {
            notifyType = 1;
        } else {
            notifyType = 3;
        }
        creditLog.setNotifyType(notifyType);
        Map<String, Object> map = Maps.newHashMap();
        map.put("applyNo", flow.getFlowNum());
        map.put("idNo", mainInfo.getCmIdnum());
        map.put("businessDate", DateUtil.getDateFormatString(flow.getTrdDate(), DateUtil.fullFormat));
        map.put("applyAmt", flow.getTrdAmt());
        map.put("notifyType", notifyType.toString());
        String sign = MD5Utils.MD5Encrypt(map, configParamBean.getZdqqMd5Key(), "&");
        map.put("sign", sign);
        try {
            creditLog.setReqParams(map.toString());
            StringBuffer rsp = HttpUtils.URLPost(configParamBean.getZdqqNotifyUrl(), map);
            log.info("前前返回结果："+rsp.toString());
            creditLog.setRspResult(rsp.toString());
            notifyCreditLogMapper.save(creditLog);
        } catch (IOException e) {
            log.error("提现申请回调前期异常："+e.getMessage());
        } catch (Exception e){
            log.error("提现申请回调前前系统异常："+e.getMessage());
        }
        log.info("提现申请回调前前----end");
    }


    /**
     * 挖财提现通知标的
     */
    @Async
    private void notifyAssets(Long flowId){
        log.info("提现申请回调标的----start:tradeFlowId= " + flowId);
        BusiTradeFlow flow = busiTradeFlowMapper.selectByPrimaryKey(flowId);
        WacaiWithdrawReqDto dto = new WacaiWithdrawReqDto();
        dto.setStatusChangeDate(new Date());
        dto.setTransNo(flow.getFlowNum());
        String status = flow.getStatus();
        if (AppConstants.TradeStatusContants.PROCESS_SUCCESS.equals(status)) {
            dto.setWacaiNotifyStatus(WacaiNotifyStatus.WITHDRAW_SUCCESS);
        }else {
            dto.setWacaiNotifyStatus(WacaiNotifyStatus.WITHDRAW_FAILED);
        }
        String reason = "";
        switch (status){
            case AppConstants.TradeStatusContants.INIT : reason+="初始";break;
            case AppConstants.TradeStatusContants.PROCESS_SUCCESS : reason+="处理成功";break;
            case AppConstants.TradeStatusContants.PROCESSING : reason+="处理中";break;
            case AppConstants.TradeStatusContants.PROCESS_FAIL : reason+="处理失败";break;
            case AppConstants.TradeStatusContants.UNPROCESS : reason+="待处理";break;
            case AppConstants.TradeStatusContants.WITHDRAW_FROZEN : reason+="提现冻结";break;
            case AppConstants.TradeStatusContants.WITHDRAW_REFUND_START : reason+="提现退款中";break;
            case AppConstants.TradeStatusContants.WITHDRAW_REFUND_FAIL : reason+="提现退款失败";break;
            case AppConstants.TradeStatusContants.WITHDRAW_REFUND_SUCCESS : reason+="提现退款成功";break;
        }
        dto.setReason(reason);
        lCBSubjectFacadeService.wacaiWithdrawNotify(dto);
        log.info("提现申请回调标的----end");
    }

}
