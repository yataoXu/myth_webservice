package com.zdmoney.facade;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.zdmoney.assets.api.common.dto.AssetsResultDto;
import com.zdmoney.assets.api.dto.signature.SignCustomerChangeReqDto;
import com.zdmoney.assets.api.dto.signature.SignCustomerChangeResDto;
import com.zdmoney.assets.api.facade.subject.ILCBSubjectFacadeService;
import com.zdmoney.assets.api.utils.BeanUtil;
import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.common.Result;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.constant.BusiConstants;
import com.zdmoney.constant.InterfaceCode;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.integral.api.dto.lcbaccount.AccountDto;
import com.zdmoney.integral.api.dto.lcbaccount.CustomerRegisterDto;
import com.zdmoney.integral.api.dto.lcbaccount.enm.RechargeWithdrawQueryBusiType;
import com.zdmoney.integral.api.dto.lcbaccount.query.QueryUserRechargeWithdrawDto;
import com.zdmoney.integral.api.dto.lcbaccount.query.QueryUserRechargeWithdrawResultDto;
import com.zdmoney.integral.api.facade.IAccountFacadeService;
import com.zdmoney.integral.api.facade.IDepositFacadeService;
import com.zdmoney.mapper.CustomerBorrowInfoMapper;
import com.zdmoney.mapper.bank.CustomerBankAccountMapper;
import com.zdmoney.mapper.customer.CustomerAddressMapper;
import com.zdmoney.mapper.customer.CustomerAuthenticationMapper;
import com.zdmoney.mapper.customer.CustomerMainInfoMapper;
import com.zdmoney.mapper.trade.BusiTradeFlowMapper;
import com.zdmoney.models.CustomerBorrowInfo;
import com.zdmoney.models.bank.CustomerBankAccount;
import com.zdmoney.models.customer.CustomerAuthChannel;
import com.zdmoney.models.customer.CustomerAuthentication;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.financePlan.BusiOrderSub;
import com.zdmoney.models.order.BusiOrder;
import com.zdmoney.models.trade.BusiTradeFlow;
import com.zdmoney.service.*;
import com.zdmoney.service.order.OrderService;
import com.zdmoney.service.transfer.BusiDebtTransferService;
import com.zdmoney.session.RedisSessionManager;
import com.zdmoney.utils.*;
import com.zdmoney.vo.OrderVo;
import com.zdmoney.web.dto.CustomerAddressDTO;
import com.zdmoney.web.dto.EstimateDto;
import com.zdmoney.web.dto.Pay20DTO;
import com.zdmoney.web.dto.TransferInitDTO;
import com.zdmoney.webservice.api.common.CustomerAccountType;
import com.zdmoney.webservice.api.common.dto.PageResultDto;
import com.zdmoney.webservice.api.common.dto.ResultDto;
import com.zdmoney.webservice.api.common.validation.WebserviceValid;
import com.zdmoney.webservice.api.dto.customer.*;
import com.zdmoney.webservice.api.facade.IBusiBorrowCertificateService;
import com.zdmoney.webservice.api.facade.ICustomerInfoFacadeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import websvc.models.Model_500031;
import websvc.models.Model_500033;
import websvc.req.ReqHeadParam;
import websvc.req.ReqMain;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by user on 2017/9/5.
 */
@Component
@Slf4j
public class CustomerInfoFacadeService implements ICustomerInfoFacadeService {

    @Autowired
    private BankCardService bankCardService;

    @Autowired
    private ICustomerInfoService customerInfoService;

    @Autowired
    private CustomerMainInfoService customerMainInfoService;

    @Autowired
    private CustomerAddressMapper customerAddressMapper;

    @Autowired
    private CustomerMainInfoMapper customerMainInfoMapper;

    @Autowired
    private CustomerAuthenticationMapper customerAuthenticationMapper;

    @Autowired
    private CustomerBankAccountMapper customerBankAccountMapper;

    @Autowired
    private OrderService orderService;

    @Autowired
    private TradeService tradeService;

    @Autowired
    private BusiDebtTransferService busiDebtTransferService;

    @Autowired
    private CustomerBorrowInfoService customerBorrowInfoService;

    @Autowired
    private CustomerBorrowInfoMapper customerBorrowInfoMapper;

    @Autowired
    private BusiOrderService busiOrderService;

    @Autowired
    private ConfigParamBean configParamBean;

    @Autowired
    private ILCBSubjectFacadeService lcbSubjectFacadeService;

    @Autowired
    private IAccountFacadeService accountFacadeService;

    @Autowired
    private BusiTradeFlowMapper busiTradeFlowMapper;

    @Autowired
    private IDepositFacadeService depositFacadeService;

    @Autowired
    private ZdPayService zdPayService;

    @Autowired
    private IBusiBorrowCertificateService iBusiBorrowCertificateService;

    @Autowired
    private RedisSessionManager redisSessionManager;


    @Override
    public PageResultDto<BusiUnbindRecordVO> searchUnbindingRecords(UnbindingRecordSearchDTO searchDto){
        Map<String,Object> map = BeanUtil.transBean2Map(searchDto);
        return bankCardService.selectUnbindRecord(map);
    }

    @Override
    public PageResultDto<CustomerInfoVO> searchUnauthedCustomers(CustomerSearchDTO searchDto) {
        Map<String,Object> map = BeanUtil.transBean2Map(searchDto);
        return customerInfoService.getCustomerByParams(map);
    }

    @Override
    public ResultDto<Integer> clearTryingAuthedTimes(Long id) {
        CustomerMainInfo customerMainInfo = new CustomerMainInfo();
        customerMainInfo.setId(id);
        customerMainInfo.setCmAuthenCount(0);
        return customerInfoService.updateByCustomerId(customerMainInfo);
    }

    @Override
    public PageResultDto<ExpandedCustomerInfoVO> searchCustomersWithPlannerInfo(CustomerSearchDTO searchDto) {
        Map<String,Object> map = BeanUtil.transBean2Map(searchDto);
        return customerInfoService.searchCustomersWithPlannerInfo(map);
    }

    @Override
    public ResultDto<Integer> modifyCustomerMemberType(Long id) {
        ResultDto<Integer> resultDto = null;
        try{
            resultDto = customerInfoService.modifyCustomerMemberType(id);
        }catch(Exception e){
            resultDto = ResultDto.FAIL("操作失败");
        }
        return resultDto;
    }

    @Override
    public PageResultDto<ExpandedCustomerInfoVO> searchCustomersBankAccountInfo(CustomerSearchDTO searchDto) {
        Map<String,Object> map = BeanUtil.transBean2Map(searchDto);
        return customerInfoService.searchCustomersBankAccountInfo(map);
    }

    @Override
    public ResultDto<ExpandedCustomerInfoVO> searchBankAccountDetail(Long id) {
        return customerInfoService.searchBankAccountDetail(id);
    }

    @Override
    public ResultDto<Integer> updateCustomerBankAccount(CustomerBankAccountVO model) {
        return customerInfoService.updateCustomerBankAccount(model);
    }

    @Override
    @Transactional
    public ResultDto unbindingByOperationalStaff(@WebserviceValid UnbindingReqDto reqDto) {
        CustomerMainInfo mainInfo = customerMainInfoMapper.selectByPrimaryKey(reqDto.getCustomerId());
        if (mainInfo == null) {
            throw new BusinessException("用户信息不存在，请致电客服！");
        }
        //查询用户需要解绑卡信息
        CustomerBankAccount bankAccount = customerBankAccountMapper.selectBankAccountInfo(reqDto.getCustomerId(),reqDto.getBankAccount());
        if (bankAccount == null) {
            throw new BusinessException("该解绑银行卡信息不存在.");
        }
        //更新银行卡表中的记录，更新用户表中相关字段
        bankCardService.updateBankAccountAndCustomerInfo(bankAccount,mainInfo);
        //如果银行卡bankcode是连连的，表明未到华瑞绑卡，不需要调华瑞接口解绑
        if(bankAccount.getCbBankCode().length() != 8){
            bankCardService.callHRServcie(bankAccount,mainInfo);
        }
        //保存解绑记录
        bankCardService.persistUnbindingRecord(bankAccount,mainInfo,true);
        return ResultDto.SUCCESS();
    }

    @Override
    public PageResultDto<CustomerMainInfoVo> getCustomerBySearchParamNew(CustomerInfoDto customerInfoVo) {
        return customerMainInfoService.getCustomerBySearchParamNew(customerInfoVo);
    }

    @Override
    public ResultDto<CustomerAddress> getCustomerAddressInfo(Long customerId) {
        // 校验客户是否存在
        CustomerMainInfo customerInfo = customerMainInfoService.findOne(customerId);
        if(null == customerInfo) throw new BusinessException("customer.not.exist");
        List<CustomerAddressDTO> addressList = customerAddressMapper.queryCustomerAddressList(customerId);
        if(!CollectionUtils.isEmpty(addressList)){
            List<CustomerAddress> addrList = new ArrayList<>();
            for (CustomerAddressDTO cad : addressList) {
                CustomerAddress addr = new CustomerAddress();
                CopyUtil.copyProperties(addr, cad);
                addrList.add(addr);
            }
            ResultDto resultDto = new ResultDto();
            resultDto.setData(addrList.get(0));
            return resultDto;
        }
        return null;
    }

    @Override
    public ResultDto realNameAuth(String realName, String idCard) {
        Map<String, Object> resMap = new HashMap<>();
        CustomerAuthentication record =new CustomerAuthentication();
        String result = null;
        try {
            CustomerAuthChannel authChannel = customerMainInfoMapper.queryAuthChannelInfo();
            if(authChannel == null || authChannel.getStatus() != 1) return ResultDto.FAIL("无可用认证通道");
            if (AppConstants.authChannel.GZT == authChannel.getId()) {
                if(ID5Util.resultValidate(realName, idCard)){
                    record.setAuStatus((short)0);
                }else{
                    record.setAuStatus((short)1);
                }
            } else if (AppConstants.authChannel.AR == authChannel.getId()) {
                // 安融认证
                resMap = AnRongAuthenticationUtil.anRongAuth(realName, idCard);
                boolean isSuccess = (boolean) resMap.get("isSuccess");
                record.setAuStatus((short)(isSuccess ? 0 : 1));
            } else if (AppConstants.authChannel.SH == authChannel.getId()) {
                // 算话认证
                resMap = SuanHuaAuthenticationUtil.suanHuaAuth(realName, idCard);
                boolean isSuccess = (boolean) resMap.get("isSuccess");
                record.setAuStatus((short)(isSuccess ? 0 : 1));
            }
            result = resMap.get("msg").toString();
            record.setAuMsg(result);
            record.setCmCellphone("");
            record.setCmIdnum(idCard);
            record.setCmInputDate(new Date());
            record.setOperMan("system");
            record.setRealName(realName);
            record.setPlatform("webService");
            record.setChannelId(authChannel.getId());
            customerAuthenticationMapper.insert(record);
        } catch (Exception e) {
            return new ResultDto(e.getMessage(), false);
        }
        return new ResultDto(result, true);
    }

    /**
     * 构建ReqMain，设置platform和userAgent都为“manage”
     * @return
     */
    private ReqMain getReqMainFromManager(){
        ReqMain reqMain = new ReqMain();
        ReqHeadParam reqHeadParam = new ReqHeadParam();
        reqHeadParam.setPlatform(BusiConstants.LOGIN_TYPE_MANAGE);
        reqHeadParam.setUserAgent(BusiConstants.LOGIN_TYPE_MANAGE);
        reqMain.setReqHeadParam(reqHeadParam);
        return reqMain;
    }

    @Override
    public ResultDto<Long> order(OrderingDto orderingDto) {
        if(!orderingDto.isValid()) return ResultDto.FAIL(orderingDto.getErrrMsg());
        ResultDto<Long> resultDto = null;
        try {
            ReqMain reqMain = getReqMainFromManager();

            OrderVo orderVo = new OrderVo();
            orderVo.setCustomerId(orderingDto.getCustomerId());
            orderVo.setProductId( orderingDto.getProductId());
            orderVo.setOrderAmt(orderingDto.getOrderAmt());
            orderVo.setReqMain(reqMain);

            BusiOrderSub busiOrder = orderService.order(orderVo);
            if (busiOrder.getId() != null) {
                resultDto = new ResultDto<>(busiOrder.getId());
            }else{
                resultDto = ResultDto.FAIL("下单失败");
            }
        } catch (BusinessException e) {
            resultDto = ResultDto.FAIL(e.getMessage());
            log.error(e.getMessage(),e);

        } catch (Exception e) {
            resultDto = ResultDto.FAIL("下单失败");
            log.error(e.getMessage(),e);
        }
        return resultDto;
    }

    @Override
    public ResultDto pay(PayingDto payingDto) {
        if(!payingDto.isValid()) return ResultDto.FAIL(payingDto.getErrrMsg());
        ResultDto resultDto = null;
        try {
            ReqMain reqMain = getReqMainFromManager();

            Long customerId = payingDto.getCustomerId();
            Long orderId = payingDto.getOrderId();
            String integralAmt = payingDto.getIntegralAmt();
            String redId = payingDto.getRedId();
            String voucherId = payingDto.getVoucherId();
            String payPassword = payingDto.getPayPassword();
            boolean isNeedPassword = true;

            Result result = tradeService.pay(customerId, orderId, integralAmt, redId, voucherId, payPassword, isNeedPassword, reqMain);
            if(result == null || !result.getSuccess()) {
                resultDto = ResultDto.FAIL(result == null ? "支付失败" : result.getMessage());
            }else{//调用成功，是否支付成功需要看data中的payResult
                Pay20DTO data = (Pay20DTO) result.getData();
                if(data.getPayResult().equals("0"))//data中的payResult为“0”表示支付成功，其他情况下支付失败
                    resultDto = ResultDto.SUCCESS();
                else
                    resultDto = ResultDto.FAIL(data.getPayResultDesc());
            }
        } catch (BusinessException e) {
            resultDto = ResultDto.FAIL(e.getMessage());
            log.error(e.getMessage(),e);
        } catch (Exception e) {
            resultDto = ResultDto.FAIL("支付失败");
            log.error(e.getMessage(),e);
        }
        return resultDto;
    }

    /**
     * 查询转让估值列表
     * transferInit（）方法可能抛出异常，最终都会返回Result.SUCCESS()
     * 存在代还资金时，alterInfo不为空，alterType为1，可据此判断是否可以转让
     * @param orderId
     * @return
     */
    @Override
    public ResultDto<TransferEstimatedPricesVo> getOneEstimatedPrice(Long orderId) {
        if(orderId == null) return ResultDto.FAIL("订单ID不能为空");
        ResultDto<TransferEstimatedPricesVo> resultDto = ResultDto.FAIL("查询转让估值列表失败");
        try{
            ReqMain reqMain = getReqMainFromManager();
            Model_500031 cdtModel = new Model_500031();
            cdtModel.setOrderId(orderId);
            reqMain.setReqParam(cdtModel);

            Result result = busiDebtTransferService.transferInit(reqMain);
            if(result == null || !result.getSuccess()) {

            }else if(result.getData() != null){
                TransferInitDTO dto = (TransferInitDTO) result.getData();
                if(StringUtils.isNotBlank(dto.getAlertInfo())){//如果弹窗提示信息不为空，表明不能转让
                    resultDto = ResultDto.FAIL(dto.getAlertInfo());
                }else if(StringUtils.isBlank(dto.getAlertInfo()) && dto.getEstimateList() !=null && !dto.getEstimateList().isEmpty()) {//只有当查询到估值列表，且可以转让时，才返回估值信息
                    EstimateDto estimateDto = dto.getEstimateList().get(0);
                    TransferEstimatedPricesVo vo = new TransferEstimatedPricesVo(estimateDto.getDate(),new BigDecimal(estimateDto.getEstimatePrice()));
                    resultDto = new ResultDto(vo);
                }
            }
        } catch (BusinessException e) {
            resultDto = ResultDto.FAIL(e.getMessage());
            log.error(e.getMessage(),e);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
        return resultDto;
    }

    @Override
    public ResultDto requestTransfering(TransferingDto transferingDto) {
        if(!transferingDto.isValid()) return ResultDto.FAIL(transferingDto.getErrrMsg());
        ResultDto resultDto = null;
        try{
            ReqMain reqMain = getReqMainFromManager();
            Model_500033 cdtModel = new Model_500033();
            cdtModel.setCustomerId(transferingDto.getCustomerId());
            cdtModel.setOrderId(transferingDto.getOrderId());
            cdtModel.setTransferDate(transferingDto.getTransferDate());
            cdtModel.setTransferPrice(transferingDto.getTransferPrice());
            reqMain.setReqParam(cdtModel);

            Result result = busiDebtTransferService.transfer(reqMain);
            if(result == null || !result.getSuccess())
                resultDto = ResultDto.FAIL(result == null ? "发起转让失败" : result.getMessage());
            else
                resultDto = ResultDto.SUCCESS();
        } catch (BusinessException e) {
            resultDto = ResultDto.FAIL(e.getMessage());
            log.error(e.getMessage(),e);
        } catch (Exception e) {
            resultDto = ResultDto.FAIL("发起转让失败");
            log.error(e.getMessage(),e);
        }
        return resultDto;
    }

    /**
     * 更新借款人意向
     * @param borrowInfoDtoDto
     * @return
     */
    public ResultDto updateBorrowInfo(CustomerBorrowInfoDto borrowInfoDtoDto) {
        CustomerBorrowInfo borrowInfo =new  CustomerBorrowInfo();
        borrowInfo.setCmNumber(borrowInfoDtoDto.getCmNumber());
        borrowInfo = customerBorrowInfoMapper.selectOne(borrowInfo);
        if (borrowInfo==null){
            return new ResultDto("用户借款信息不存在",false);
        }
        CustomerBorrowInfo borrowInfos =new  CustomerBorrowInfo();
        borrowInfos.setModifyTime(new Date());
        borrowInfos.setId(borrowInfo.getId());
        borrowInfos.setCmNumber(borrowInfo.getCmNumber());
        borrowInfos.setThdFlag(borrowInfoDtoDto.getThdFlag());
        borrowInfos.setThdInfo(borrowInfoDtoDto.getThdInfo());
        try {
            customerBorrowInfoMapper.updateByPrimaryKeySelective(borrowInfos);
            return new ResultDto("更新用户借款意向成功",true);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return new ResultDto("更新用户借款意向失败",false);
        }
    }

    /**
     * 法大大签约确认
     */
    public ResultDto customerSignContract(CustomerSignContractDto signContractDto) {
        CustomerMainInfo customerMainInfo = customerMainInfoService.findOneByCmNumber(signContractDto.getCmNumber());
        if (customerMainInfo==null){
            return new ResultDto("该用户信息不存在",false);
        }
        customerMainInfo.setCmModifyDate(new Date());
        customerMainInfo.setSignContract(1);
        try {
            customerMainInfoMapper.updateByPrimaryKeySelective(customerMainInfo);
            return new ResultDto("用户法大大签约确认成功",true);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return new ResultDto("用户法大大签约确认失败",false);
        }
    }

    @Override
    public ResultDto<ExpandedCustomerInfoVO> selectOneCustomerBankAccount(CustomerSearchDTO searchDto) {
        Map<String,Object> map = BeanUtil.transBean2Map(searchDto);
        return customerInfoService.selectOneCustomerBankAccount(map);
    }

    @Override
    public ResultDto<CustomerInfoVO> selectOneCustomerInfo(String cmNumber) {
        return customerInfoService.selectOneCustomerInfo(cmNumber);
    }

    public ResultDto<String> getBankCardByIdNum(String idNum) {
        CustomerMainInfo  customerMainInfo = customerMainInfoService.selectBankCodeByIdNum(idNum);
        if (customerMainInfo == null) {
            return new ResultDto("查询不到该用户信息", false);
        }
        if (StringUtil.isEmpty(customerMainInfo.getBankAccountId())) {
            return new ResultDto("该用户暂无绑定银行卡", false);
        }
        CustomerBankAccount customerBankAccount = customerBankAccountMapper.selectByPrimaryKey(Long.parseLong(customerMainInfo.getBankAccountId()));
        if (customerBankAccount == null) {
            return new ResultDto("查询不到该用户银行卡信息", false);
        }
        return new ResultDto(customerBankAccount.getCbAccount());
    }

    @Override
    public ResultDto<String> changePhoneNo(String currentPhoneNo,String newPhoneNo) {
        ResultDto<String> resultDto = new ResultDto();
        try {
            CustomerMainInfo currentPhoneNoUser = customerMainInfoService.findOneByPhone(newPhoneNo);
            if (currentPhoneNoUser != null) {
                resultDto = ResultDto.FAIL("已存在新手机号用户");
            } else {
                CustomerMainInfo customer = customerMainInfoService.findOneByPhone(currentPhoneNo);
                if (customer != null) {
                    if(customer.getCmStatus() == AppConstants.CmStatus.MEMBER_AUTHEN_SUCCESS && StringUtils.isNotBlank(customer.getFuiouLoginId())) {
                        String errorMsg = null;
                        //通知账户变更手机号
                        CustomerRegisterDto customerRegisterDto = new CustomerRegisterDto();
                        customerRegisterDto.setUserNo(customer.getCmNumber());
                        customerRegisterDto.setTelNo(newPhoneNo);
                        com.zdmoney.integral.api.common.dto.ResultDto<Boolean> accountResult = accountFacadeService.customerInfoChange(customerRegisterDto);
                        if (accountResult == null) {
                            errorMsg = "通知账户变更手机号发生异常";
                        } else if (!accountResult.isSuccess()) {
                            errorMsg = "通知账户变更手机号失败:" + accountResult.getMsg();
                        }
                        if (errorMsg != null) {
                            log.error(errorMsg);
                            return ResultDto.FAIL(errorMsg);
                        }
                        //通知标的变更手机号
                        SignCustomerChangeReqDto reqDto = new SignCustomerChangeReqDto(customer.getCmNumber(), newPhoneNo, configParamBean.getSubjectPartnerNo());
                        AssetsResultDto<SignCustomerChangeResDto> result = lcbSubjectFacadeService.signChangeCustomerInfo(reqDto);
                        if (result == null) {
                            errorMsg = "通知标的变更手机号发生异常";
                        } else if (!result.isSuccess()) {
                            errorMsg = "通知标的变更手机号失败:" + result.getMsg();
                        }
                        if (errorMsg != null) {
                            log.error(errorMsg);
                            return ResultDto.FAIL(errorMsg);
                        }
                    }
                    customerMainInfoService.changePhoneNo(customer, newPhoneNo);//更新用户手机号，插入手机号解绑记录
                }else{
                    log.error("不存在改手机号用户："+currentPhoneNo);
                    resultDto = ResultDto.FAIL("不存在该手机号用户："+currentPhoneNo);
                }
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
            resultDto = ResultDto.FAIL("发生异常");
        }
        return resultDto;
    }

    @Override
    public ResultDto<String> deactivateAccountByPhone(String phoneNo) {
        ResultDto<String> resultDto = new ResultDto();
        try{
            CustomerMainInfo customer = customerMainInfoService.findOneByPhone(phoneNo);
            if(customer != null){
                Map<String,Object> map = new HashMap<>();
                map.put("customerId",customer.getId());
                map.put("statusList", Arrays.asList(AppConstants.BusiOrderStatus.BUSIORDER_STATUS_0,
                        AppConstants.BusiOrderStatus.BUSIORDER_STATUS_9,
                        AppConstants.BusiOrderStatus.BUSIORDER_STATUS_14,
                        AppConstants.BusiOrderStatus.BUSIORDER_STATUS_17,
                        AppConstants.BusiOrderStatus.BUSIORDER_STATUS_18));
                List<BusiOrder> orderList = busiOrderService.selectOrderByProperty(map);
                if(orderList.size() > 0){
                    resultDto = ResultDto.FAIL("存在未完结订单，不能注销");
                }else{
                    if(customer.getCmStatus() == AppConstants.CmStatus.MEMBER_AUTHEN_SUCCESS && StringUtils.isNotBlank(customer.getFuiouLoginId())){
                        com.zdmoney.integral.api.common.dto.ResultDto<AccountDto> accountBalance = accountFacadeService.getAccountBalance(customer.getCmNumber());
                        if (accountBalance.getData() != null && accountBalance.getData().getBalance().compareTo(BigDecimal.ZERO) > 0) {
                            return ResultDto.FAIL("账户余额不为空，不能注销");
                        }
                        SignCustomerChangeReqDto reqDto = new SignCustomerChangeReqDto(customer.getCmNumber(), customer.getCmCellphone(), configParamBean.getSubjectPartnerNo());
                        String errorMsg = null;
                        AssetsResultDto assetsResult = lcbSubjectFacadeService.cancelSignCustomer(reqDto);
                        if (assetsResult == null) {
                            errorMsg = "通知标的注销账户发生异常";
                        } else if (!assetsResult.isSuccess()) {
                            errorMsg = "通知标的注销账户失败:" + assetsResult.getMsg();
                        }
                        if (errorMsg != null) {
                            log.error(errorMsg);
                            return ResultDto.FAIL(errorMsg);
                        }
                    }
                    customerMainInfoService.deactivateAccount(customer);
                }
            }else{
                resultDto = ResultDto.FAIL("不存在该手机号用户");
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
            resultDto = ResultDto.FAIL("发生异常");
        }
        return resultDto;
    }

    @Override
    public PageResultDto<CustomerInfoVO> searchCustomerByNameAndPhone(CustomerSearchDTO searchDto) {
        Map<String,Object> map = BeanUtil.transBean2Map(searchDto);
        return customerInfoService.getCustomerByNameAndPhone(map);
    }

    @Override
    public void withdrawThawJob(){
        /**
         * 1、查询提现状态为 提现申请 并且 交易时间 小于当前时间半小时 的提现订单
         * 2、根据提现订单的支付流水号 调用账户系统接口，查询该提现订单状态
         * 3、根据账户返回订单状态，更新提现订单信息
         *
         */
        Map<String,Object> parameter =new HashMap<>();
        parameter.put("trdType",AppConstants.TradeStatusContants.WITHDRAWING);
        parameter.put("status",AppConstants.TradeStatusContants.INIT);
        DateTime trdDateStart = DateUtil.offsetHour(new Date(),-72);
        parameter.put("trdDateStart",trdDateStart.toJdkDate());
        DateTime trdDateEnd = DateUtil.offsetMinute(new Date(),-30);
        parameter.put("trdDateEnd",trdDateEnd.toJdkDate());
        parameter.put("jobFlag", "0");
        List<BusiTradeFlow>  busiTradeFlowList = busiTradeFlowMapper.selectByStatusTrddate(parameter);

        for (BusiTradeFlow busiTradeFlowItem : busiTradeFlowList){
            CustomerMainInfo mainInfo = customerMainInfoService.checkCustomerId(busiTradeFlowItem.getCustomerId());
            try {
                com.zdmoney.integral.api.common.dto.PageResultDto<QueryUserRechargeWithdrawResultDto> queryUserRechargeWithdrawResultDtoPageResultDto =null;
                boolean isSuccess = false; //该提现流水默认 提现失败

                Map params = new TreeMap<>();
                params.put("merchantCode", configParamBean.getFuiouMerchantCode());
                params.put("channelOrderNo", busiTradeFlowItem.getFlowNum());
                params.put("signature", MD5Utils.MD5Encrypt(params, configParamBean.getFuiouKey()));
                String url = configParamBean.getZdpayUrl()+InterfaceCode.THIRD_SERIALNO.getUrl();
                log.info("收银台getThirdSerialNo接口，请求路径：{} 请求参数:{} ", url, params.toString());
                try {
                    // 发送认证请求
                    String resultData = HttpClientUtil.post(url, params);
                    log.info("收银台getThirdSerialNo接口，请求路径：{} 请求参数:{} 请求结果：{}", url, params.toString(),resultData);
                    JSONObject jsonObject = StringUtils.isBlank(resultData)?null:JSON.parseObject(resultData);

                    if(jsonObject!=null
                            &&!jsonObject.isEmpty()
                            &&"0000".equals(jsonObject.getString("code"))
                            &&jsonObject.getJSONArray("data")!=null
                            &&jsonObject.getJSONArray("data").size()>0
                    ){
                        QueryUserRechargeWithdrawDto queryUserRechargeWithdrawDto =new QueryUserRechargeWithdrawDto();
                        queryUserRechargeWithdrawDto.setBusiType(RechargeWithdrawQueryBusiType.PWTX);
                        queryUserRechargeWithdrawDto.setTxnSsn(jsonObject.getJSONArray("data").getJSONObject(0).getString("thirdSerialNo"));
                        queryUserRechargeWithdrawDto.setStartTime(DateUtil.offsetHour(new Date(),-48).toJdkDate());
                        queryUserRechargeWithdrawDto.setEndTime(new Date());
                        queryUserRechargeWithdrawDto.setLoginId(mainInfo.getFuiouLoginId());
                        queryUserRechargeWithdrawDto.setPageNo(1);
                        queryUserRechargeWithdrawDto.setPageSize(100);
                        log.info("反查账户系统，提现订单号：{} 入参{} ", busiTradeFlowItem.getFlowNum(), JSON.toJSONString(queryUserRechargeWithdrawDto));
                        queryUserRechargeWithdrawResultDtoPageResultDto = depositFacadeService.queryUserRechargeOrWithdrawPage(queryUserRechargeWithdrawDto);
                        log.info("反查账户系统，提现订单号：{} 入参{}  账户返回内容:{}", busiTradeFlowItem.getFlowNum(), JSON.toJSONString(queryUserRechargeWithdrawDto), JSON.toJSONString(queryUserRechargeWithdrawResultDtoPageResultDto));

                        if (queryUserRechargeWithdrawResultDtoPageResultDto !=null
                                &&"0000".equals(queryUserRechargeWithdrawResultDtoPageResultDto.getCode())
                                &&queryUserRechargeWithdrawResultDtoPageResultDto.getDataList()!=null
                                &&queryUserRechargeWithdrawResultDtoPageResultDto.getDataList().size()>0){
                            QueryUserRechargeWithdrawResultDto queryUserRechargeWithdrawResultDto = queryUserRechargeWithdrawResultDtoPageResultDto.getDataList().get(0);
                            //账户返回该提现订单成功
                            if ("0000".equals(queryUserRechargeWithdrawResultDto.getTxnRspCode())){
                                isSuccess = true;
                                busiTradeFlowItem.setJobFlag(1);
                                busiTradeFlowMapper.updateByPrimaryKeySelective(busiTradeFlowItem);

                                log.error("提现订单号：{} 提现解冻定时任务，发现该订单交易系统状态显示申请中，在账户查询结果中状态为:提现成功，入参{}  返回内容:{}", busiTradeFlowItem.getFlowNum(), JSON.toJSONString(queryUserRechargeWithdrawDto), JSON.toJSONString(queryUserRechargeWithdrawResultDto));
                                MailUtil.sendMail("提现订单号："+busiTradeFlowItem.getFlowNum()+" 提现解冻定时任务异常", "提现订单号："+busiTradeFlowItem.getFlowNum()+" 提现解冻定时任务，发现该订单交易系统状态显示申请中，在账户查询结果中状态为:提现成功，入参"+JSON.toJSONString(queryUserRechargeWithdrawDto)+"  返回内容:"+JSON.toJSONString(queryUserRechargeWithdrawResultDto));
                            }
                        }
                    }

                } catch (Exception e) {
                    //如果在反查账户过程中出现异常，通过修改isSuccess状态为true，不做任何解冻操作
                    isSuccess = true;
                    e.printStackTrace();
                    log.error("提现订单号：{} 反查账户系统queryUserRechargeOrWithdrawPage接口失败，失败原因:{}", busiTradeFlowItem.getFlowNum(), e);
                }

                if (!isSuccess){
                    busiTradeFlowItem.setStatus(AppConstants.TradeStatusContants.PROCESS_FAIL);
                    //个人提现
                    if (CustomerAccountType.LENDER.getValue().equals(mainInfo.getAccountType()) || CustomerAccountType.BORROWER.getValue().equals(mainInfo.getAccountType())) {
                        String accountSeriNo = zdPayService.personalWithdrawRefund(busiTradeFlowItem.getFlowNum(),mainInfo,busiTradeFlowItem.getTrdAmt(), busiTradeFlowItem);
                        busiTradeFlowItem.setAccountSeriNo(accountSeriNo);
                        busiTradeFlowItem.setStatus(AppConstants.TradeStatusContants.PROCESS_FAIL);
                    }

                    busiTradeFlowMapper.updateByPrimaryKeySelective(busiTradeFlowItem);
                    log.info("提现订单号：{} 提现解冻定时任务成功", busiTradeFlowItem.getFlowNum());
                }

            }catch (Exception e){
                e.printStackTrace();
                log.error("提现订单号：{} 提现解冻定时任务失败，失败原因:{}", busiTradeFlowItem.getFlowNum(), e);
                MailUtil.sendMail("提现订单号："+busiTradeFlowItem.getFlowNum()+" 提现解冻定时任务失败", "提现订单:" + busiTradeFlowItem.getFlowNum() + "提现解冻定时任务失败,失败原因:" + e.getMessage());
            }
        }
    }

    @Override
    public ResultDto<List<UserInfoDTO>> listUserInfos(List<Long> userIds) {
        if (CollectionUtils.isEmpty(userIds)) {
            return ResultDto.FAIL("用户Id不能为空");
        }
        log.info("批量查询用户ids: " + JSON.toJSONString(userIds));
        List<UserInfoDTO> userList = customerMainInfoMapper.listUserInfos(userIds);
        return new ResultDto(userList);
    }

    @Override
    public ResultDto<Map<String, String>> getHoldAssest(String cmNumber) {
        if(StringUtils.isEmpty(cmNumber)){
            return ResultDto.FAIL("cmNumber is empty");
        }
        CustomerMainInfo info = customerMainInfoMapper.selectBycmNumber(cmNumber);
        Map<String, String> resultMap = Maps.newHashMap();
        resultMap.put("userLevel", info.getUserLevel());

        ResultDto<String> holdResult = iBusiBorrowCertificateService.getHoldAsset(String.valueOf(info.getId()));
        if(holdResult.isSuccess()){
            resultMap.put("holdAssets", holdResult.getData());
        }else {
            return ResultDto.FAIL(holdResult.getMsg());
        }

        return new ResultDto<Map<String, String>>(resultMap);
    }

    @Override
    public ResultDto<WxAuthUserInfoDTO> getWxAuthUserInfo(String code) {
        log.info("---------->根据code获取用户授权信息:" + code);
        ResultDto<WxAuthUserInfoDTO> resultDto = new ResultDto<>();
        if (StringUtils.isEmpty(code)) {
            resultDto.setCode(ResultDto.ERROR_CODE);
            resultDto.setMsg("code不能为空");
            return resultDto;
        }
        // 通过code获取access_token
        StringBuilder url1 = new StringBuilder("https://api.weixin.qq.com/sns/oauth2/access_token")
                .append("?appid=").append(configParamBean.getWx_appid()).append("&secret=").append(configParamBean.getWx_secret())
                .append("&code=").append(code).append("&grant_type=authorization_code");
        String result1 = HttpUtil.get(url1.toString());
        log.info("通过code获取access_token: 请求结果" + result1);
        if (result1.contains("errcode")) {
            return getWxError(result1);
        }
        JSONObject jsonObj1 = JSONObject.parseObject(result1);

        // 通过access_token和openid拉取用户信息
        StringBuilder url2 = new StringBuilder("https://api.weixin.qq.com/sns/userinfo")
                .append("?access_token=").append(jsonObj1.get("access_token")).append("&openid=").append(jsonObj1.get("openid")).append("&lang=zh_CN");
        String result2 = HttpUtil.get(url2.toString());
        log.info("通过access_token和openid拉取用户信息: 请求结果" + result2);
        if (result2.contains("errcode")) {
            return getWxError(result2);
        }
        WxAuthUserInfoDTO wxAuthUserInfo = JSON.parseObject(result2, WxAuthUserInfoDTO.class);
        wxAuthUserInfo.setAccessToken(Convert.toStr(jsonObj1.get("access_token")));
        resultDto.setData(wxAuthUserInfo);
        resultDto.setCode(ResultDto.SUCCESS_CODE);
        return resultDto;
    }

    @Override
    public ResultDto<WxTicketInfoDTO> getWxTicket(String url) {
        ResultDto<WxTicketInfoDTO> resultDto = new ResultDto<>();
        Map<String, Object> map = new HashMap<>();
        String ticket = null;

        String wx_key = "wx_token_ticket";
        String value = redisSessionManager.get(wx_key);
        if (StringUtils.isNotEmpty(value)) {
            map = JSON.parseObject(value, Map.class);
            ticket = Convert.toStr(map.get("ticket"));
        } else {
            StringBuilder url1 = new StringBuilder("https://api.weixin.qq.com/cgi-bin/token")
                    .append("?grant_type=client_credential")
                    .append("&appid=").append(configParamBean.getWx_appid()).append("&secret=").append(configParamBean.getWx_secret());
            String result1 = HttpUtil.get(url1.toString());
            log.info("https://api.weixin.qq.com/cgi-bin/token: 请求结果" + result1);
            JSONObject jsonObj1 = JSONObject.parseObject(result1);
            if (result1.contains("errcode")) {
                resultDto.setCode(ResultDto.ERROR_CODE);
                resultDto.setMsg("微信返回异常:" + jsonObj1.get("errmsg"));
                return resultDto;
            }
            StringBuilder url2 = new StringBuilder("https://api.weixin.qq.com/cgi-bin/ticket/getticket")
                    .append("?access_token=").append(jsonObj1.get("access_token"))
                    .append("&type=jsapi");
            String result2 = HttpUtil.get(url2.toString());
            log.info("https://api.weixin.qq.com/cgi-bin/ticket/getticket: 请求结果" + result2);
            JSONObject jsonObj2 = JSONObject.parseObject(result2);
            map.put("ticket", jsonObj2.get("ticket"));
            redisSessionManager.put(wx_key, JSON.toJSONString(map), 1, TimeUnit.HOURS);
        }
        Long timestamp = new Date().getTime() / 1000;
        String noncestr = DigestUtils.shaHex(new Date().toString());

        WxTicketInfoDTO wxTicketInfoDTO = new WxTicketInfoDTO();
        wxTicketInfoDTO.setAppId(configParamBean.getWx_appid());
        wxTicketInfoDTO.setTicket(ticket);
        wxTicketInfoDTO.setNoncestr(noncestr);
        wxTicketInfoDTO.setTimestamp(timestamp);
        String str = "jsapi_ticket=" + ticket + "&noncestr=" + noncestr + "&timestamp=" + timestamp + "&url=" + url;
        wxTicketInfoDTO.setSignature(DigestUtils.shaHex(str));
        resultDto.setData(wxTicketInfoDTO);
        resultDto.setCode(ResultDto.SUCCESS_CODE);
        return resultDto;
    }

    private static ResultDto<WxAuthUserInfoDTO> getWxError (String result) {
        log.info("微信请求异常:" + JSON.toJSONString(result));
        ResultDto<WxAuthUserInfoDTO> resultDto = new ResultDto<>();
        JSONObject jsonObj = JSONObject.parseObject(result);
        resultDto.setCode(ResultDto.ERROR_CODE);
        resultDto.setMsg("微信返回异常:" + jsonObj.get("errmsg"));
        return resultDto;
    }

}
