package com.zdmoney.service.impl;

import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.common.Result;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.integral.api.common.dto.ResultDto;
import com.zdmoney.integral.api.dto.lcbaccount.*;
import com.zdmoney.mapper.bank.BusiBankLimitMapper;
import com.zdmoney.mapper.bank.BusiUnbindRecordMapper;
import com.zdmoney.mapper.bank.CustomerBankAccountMapper;
import com.zdmoney.mapper.customer.CustomerMainInfoMapper;
import com.zdmoney.mapper.trade.BusiTradeFlowMapper;
import com.zdmoney.models.bank.BusiBankLimit;
import com.zdmoney.models.bank.BusiUnbindRecord;
import com.zdmoney.models.bank.CustomerBankAccount;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.payChannel.BusiPayChannel;
import com.zdmoney.models.sys.SysParameter;
import com.zdmoney.models.trade.BusiTradeFlow;
import com.zdmoney.service.*;
import com.zdmoney.service.payChannel.BusiPayChannelService;
import com.zdmoney.utils.*;
import com.zdmoney.web.dto.*;
import com.zdmoney.webservice.api.common.dto.PageResultDto;
import com.zdmoney.webservice.api.dto.customer.BankCardBindNotifyDto;
import com.zdmoney.webservice.api.dto.customer.BusiUnbindRecordDto;
import com.zdmoney.webservice.api.dto.customer.BusiUnbindRecordVO;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import websvc.models.*;
import websvc.req.ReqHeadParam;
import websvc.req.ReqMain;
import websvc.utils.SysLogUtil;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 00225181 on 2015/12/3.
 * 2.0银行卡相关接口
 */
@Service
@Slf4j
public class BankCardServiceImpl implements BankCardService {

    @Autowired
    private CustomerMainInfoMapper customerMainInfoMapper;

    @Autowired
    private CustomerBankAccountMapper customerBankAccountMapper;

    @Autowired
    private BusiOrderService busiOrderService;

    @Autowired
    private BusiTradeFlowMapper busiTradeFlowMapper;

    @Autowired
    private BusiUnbindRecordMapper busiUnbindRecordMapper;

    @Autowired
    private BusiBankLimitMapper busiBankLimitMapper;

    @Autowired
    private BusiPayChannelService busiPayChannelService;

    @Autowired
    private ConfigParamBean configParamBean;

    @Autowired
    private CustomerMainInfoService customerMainInfoService;

    @Autowired
    private SysParameterService sysParameterService;

    /*充值绑卡3.0*/
    @Transactional(rollbackFor = Exception.class)
    public Result rechargeAmt(ReqMain reqMain) throws Exception {
        Model_520077 model_520077 = (Model_520077) reqMain.getReqParam();
        String customerId = model_520077.getCustomerId();
        String rechargeAmt = model_520077.getAmount();
        String bankCard = model_520077.getBankCard();
        if (StringUtils.isEmpty(customerId)){
            throw new BusinessException("用户编号不能为空！");
        }
        SysParameter minrecharge = sysParameterService.findOneByPrType("minrecharge");
        if (minrecharge == null) {
            throw new BusinessException("系统没有配置最小充值金额，充值失败，如有疑问请联系管理员！");
        }
        if (StringUtils.isEmpty(minrecharge.getPrValue())) {
            throw new BusinessException("系统没有配置最小充值金额，充值失败，如有疑问请联系管理员！");
        }
        if (new BigDecimal(rechargeAmt).compareTo(new BigDecimal(minrecharge.getPrValue())) == -1) {
            throw new BusinessException("充值金额小于系统设置的最小充值金额" + minrecharge.getPrValue() + "元，充值失败！");
        }
        //查询用户需要充值卡信息
        CustomerBankAccount bankAccount = customerBankAccountMapper.selectBankAccountInfo(Long.parseLong(customerId),bankCard);
        if (bankAccount == null) {
            throw new BusinessException("该银行卡信息不存在.");
        }
        BusiPayChannel payChannel = busiPayChannelService.getCurrentPayChannel();
        BusiBankLimit limit = getPayBank(bankAccount.getCbBankCode(), payChannel.getPayPlatformCode());
        if (limit!=null && "1".equals(limit.getLlRecharge())){
            throw new BusinessException("该银行卡不支持连连充值，如有疑问请联系管理员！");
        }

        Long custId = Long.parseLong(customerId);
        CustomerMainInfo mainInfo = customerMainInfoService.findOpenCustomerById(custId);

        String flowId = busiOrderService.buildCeNumber("R", "8888", custId);
        BusiTradeFlow tradeFlow = new BusiTradeFlow();
        tradeFlow.setCustomerId(custId);
        tradeFlow.setTrdAmt(new BigDecimal(model_520077.getAmount()));
        tradeFlow.setTrdType(AppConstants.TradeStatusContants.RECHARGEING);
        tradeFlow.setTrdDate(new Date());
        tradeFlow.setFlowNum(flowId);
        tradeFlow.setStatus(AppConstants.TradeStatusContants.INIT);
        tradeFlow.setBankName(bankAccount.getCbBankName());
        tradeFlow.setBankCardNum(bankAccount.getCbAccount());
        tradeFlow.setBankCardId(bankAccount.getId());
        int num = busiTradeFlowMapper.insert(tradeFlow);
        if (num !=1) {
            log.error("保存充值流水信息失败，" + JSONObject.fromObject(model_520077));
            throw new BusinessException("保存充值流水信息失败！");
        }
            RechargeDTO dto = new RechargeDTO();
            List<BusiPayChannel> channels = busiPayChannelService.getPayChannel(model_520077.getBankCode(), new BigDecimal(model_520077.getAmount()));
            for (BusiPayChannel channel : channels) {
                String sessionToken = LaocaiUtil.makeUserToken(configParamBean.getUserTokenKey(), mainInfo.getCmNumber());
                if (AppConstants.PayChannelCodeContants.HUARUI_BANK.equals(channel.getPayPlatformCode())) {
                    ReqHeadParam headParam = reqMain.getReqHeadParam();
                    String businessType = "credit".equalsIgnoreCase(headParam.getSystem()) ? "1" : "2";//1-理财端 2-借款端
                    StringBuffer sb = new StringBuffer();
                    sb.append(configParamBean.getTouchWebappHomeUrl() + channel.getPayUrl());
                    sb.append("&sessionToken=" + sessionToken);
                    if (StringUtils.isNotBlank(bankAccount.getCbBindPhone())) {
                        sb.append("&cellphone=" + bankAccount.getCbBindPhone());
                        dto.setCellphone(bankAccount.getCbBindPhone());
                    }
                    sb = sb.append("&businessType=" + businessType)
                            .append("&bankCode=" + model_520077.getBankCode())
                            .append("&orderId=" + flowId)
                            .append("&bankCard=" + model_520077.getBankCard())
                            .append("&amount=" + model_520077.getAmount())
                            .append("&source=1")
                            .append("&payTime=" + DateUtil.getDateFormatString(new Date(), DateUtil.dateFormat));
                    channel.setPayUrl(sb.toString());
                    channel.setPayMerchantNo(configParamBean.getPayMerchantNo());
                    channel.setPayPrivateKey(configParamBean.getPayPrivateKey());
                    channel.setPayVesion(configParamBean.getPayVersion3());

                } else {
                    channel.setPayUrl(configParamBean.getCashierAddr() + channel.getPayUrl());
                    if (!AppConstants.PayChannelCodeContants.TPP.equals(channel.getPayPlatformCode())) {
                        channel.setPayMerchantNo(configParamBean.getPayMerchantNo());
                        channel.setPayPrivateKey(configParamBean.getPayPrivateKey());
                        channel.setPayVesion(configParamBean.getPayVersion3());
                    }
                }
            }

        dto.setChannels(channels);
        dto.setFlowId(flowId);
        dto.setAmount(model_520077.getAmount());
        dto.setTppAgreementUrl(configParamBean.getTppAgreementH5Url());
        return Result.success(dto);
    }

    /**
     * 更新用户绑定银行卡表
     * 更新用户表
     * @param bankAccount
     * @param mainInfo
     */
    public void updateBankAccountAndCustomerInfo(CustomerBankAccount bankAccount,CustomerMainInfo mainInfo){
        bankAccount.setCbValid(Short.valueOf("1"));//银行卡标记为删除
        int num = customerBankAccountMapper.updateByPrimaryKey(bankAccount);
        if (num != 1) {
            throw new BusinessException("解绑银行卡失败，请致电客服！，status：1");
        }
        List<CustomerBankAccount> bankAccounts = customerBankAccountMapper.listCustBankAccount(mainInfo.getId());
        if(CollectionUtils.isEmpty(bankAccounts)){
            mainInfo.setBankAccountId("");
        }else{
            mainInfo.setBankAccountId(bankAccounts.get(0).getId().toString());//默认取最早绑定银行卡
        }

        mainInfo.setOldBankAccountId(null);
        mainInfo.setCmModifyDate(new Date());
        num = customerMainInfoMapper.updateByPrimaryKey(mainInfo);
        if (num != 1) {
            throw new BusinessException("解绑银行卡失败，请致电客服！，status：2");
        }
    }
    /**
     * 存管改造-调用账户系统（华瑞）解绑银行卡接口
     * @param bankAccount
     * @param mainInfo
     */
    public void callHRServcie(CustomerBankAccount bankAccount,CustomerMainInfo mainInfo){
        BankCardUnbindDto unbindDto = new BankCardUnbindDto();
        unbindDto.setBankCode(bankAccount.getCbBankCode());
        String hrBankId = changeBankCode(AppConstants.PayChannelCodeContants.HUARUI_BANK, bankAccount.getCbBankCode());
        unbindDto.setBankId(hrBankId);
        unbindDto.setBankNo(bankAccount.getCbAccount());
        unbindDto.setBankName(bankAccount.getCbAccountName());
        unbindDto.setTelNo(bankAccount.getCbBindPhone());
        unbindDto.setIDcard(mainInfo.getCmIdnum());
        unbindDto.setUserNo(mainInfo.getCmNumber());
        unbindDto.setUserName(mainInfo.getCmRealName());
        unbindDto.setTransNo(SerialNoGeneratorService.generateUnbindSerialNo(mainInfo.getId()));
        ResultDto result = null;//accountFacadeService.bankCardUnbind(unbindDto);
        if (!result.isSuccess()) {
            log.error("调用账户（华瑞）解绑失败，客户号：" + mainInfo.getId() + "，bankId：" + bankAccount.getId());
            SysLogUtil.save("调用收银台解绑失败，客户号：" + mainInfo.getId() + "，bankId：" + bankAccount.getId(), "Unbind-bank-card-fail", BankCardServiceImpl.class.getName());
            throw new BusinessException("调用解绑失败，请致电客服！");
        }
        log.info("用户：" + mainInfo.getId() + ",账户:" + bankAccount.getCbAccount() + ",华瑞解绑成功");
    }

    /**
     * 保存解绑操作记录
     * @param bankAccount
     * @param mainInfo
     * @param isStaffOperating
     */
    public void persistUnbindingRecord(CustomerBankAccount bankAccount,CustomerMainInfo mainInfo,boolean isStaffOperating){
        BusiUnbindRecord unbindRecord = new BusiUnbindRecord();
        unbindRecord.setOperTime(new Date());
        unbindRecord.setUbIdnum(mainInfo.getCmIdnum());
        unbindRecord.setUbBankCode(bankAccount.getCbAccount());
        unbindRecord.setUbRealName(mainInfo.getCmRealName());
        unbindRecord.setUbTelephone(mainInfo.getCmCellphone());
        unbindRecord.setOperMan(mainInfo.getCmRealName());
        unbindRecord.setRemark(isStaffOperating ? "后台解绑银行卡" : "客户端解绑银行卡");
        unbindRecord.setUnType("1");
        int num = busiUnbindRecordMapper.insertBusiUnbindRecord(unbindRecord);
        if (num != 1) {
            log.error("保存解绑流水失败，客户号：" + mainInfo.getId() + "，bankId：" + bankAccount.getId());
            SysLogUtil.save("保存解绑流水失败，客户号：" + mainInfo.getId() + "，bankId：" + bankAccount.getId(), "Unbind-bank-card-fail", BankCardServiceImpl.class.getName());
            throw new BusinessException("保存解绑流水失败，请致电客服！");
        }
    }

    /**
     * 解绑手机号
     *
     * @param mainInfo
     * @return
     */
    public void unBindPhone(CustomerMainInfo mainInfo) {
        try {
            String bankId = mainInfo.getBankAccountId();
            if (!StringUtils.isEmpty(bankId)) {
                CustomerBankAccount bankAccount = customerBankAccountMapper.selectByPrimaryKey(Long.parseLong(bankId));
                Map<String, Object> params = new HashMap<>();
                params.put("certNo", mainInfo.getCmIdnum());
                params.put("bankCardNo", bankAccount.getCbAccount());
                Map<String, Object> jsonMap = unBind(params);
                Object responseDesc = jsonMap.get("responseDesc");
                log.info("responseDesc=" + responseDesc);
                if (AppConstants.INTEGRAL_STATUS_SUCCESS.equals(jsonMap.get("responseCode")) || "0038".equals(jsonMap.get("responseCode"))) {
                    /**插入解绑记录*/
                    BusiUnbindRecord busiUnbindRecord = new BusiUnbindRecord();
                    busiUnbindRecord.setOperMan(mainInfo.getCmRealName());
                    busiUnbindRecord.setOperTime(new Date());
                    busiUnbindRecord.setUbBankCode(bankAccount.getCbAccount());
                    busiUnbindRecord.setUbIdnum(mainInfo.getCmIdnum());
                    busiUnbindRecord.setUbRealName(mainInfo.getCmRealName());
                    busiUnbindRecord.setUbTelephone(mainInfo.getCmCellphone());
                    busiUnbindRecord.setRemark("客户修改手机号，解绑手机号,原手机号" + mainInfo.getCmCellphone());
                    busiUnbindRecord.setUnType("2");
                    Integer num = busiUnbindRecordMapper.insertBusiUnbindRecord(busiUnbindRecord);
                    if (num != 1) {
                        log.error("保存解绑手机流水失败，客户号：" + mainInfo.getId() + "，bankId：" + bankAccount.getId());
                        SysLogUtil.save("保存解绑手机流水失败，客户号：" + mainInfo.getId() + "，bankId：" + bankAccount.getId(), "Unbind-bank-phone-fail", BankCardServiceImpl.class.getName());
                    }
                } else {
                    log.error("调用收银台解绑手机失败，客户号：" + mainInfo.getId() + "，bankId：" + bankAccount.getId());
                    SysLogUtil.save("调用收银台解绑手机失败，客户号：" + mainInfo.getId() + "，bankId：" + bankAccount.getId(), "Unbind-bank-phone-fail", BankCardServiceImpl.class.getName());
                }
            }
        } catch (Exception e) {
            log.error("调用收银台解绑手机失败" + e.getMessage());
            SysLogUtil.save("前端调用收银台解绑手机失败，客户号：" + mainInfo.getId(), "Unbind-bank-phone-fail", BankCardServiceImpl.class.getName());
        }
    }

    public BusiBankLimit getPayBank(String bankCode, String channelCode) {
        BusiBankLimit limit = new BusiBankLimit();
        limit.setBankCode(bankCode);
        limit.setPayChannel(channelCode);
        //连连转华瑞银行编码
        if (AppConstants.PayChannelCodeContants.HUARUI_BANK.equals(channelCode) && 8 == bankCode.length()) {
            BusiBankLimit query = new BusiBankLimit();
            query.setBankCode(null);
            query.setLlBankCode(bankCode);
            query.setPayChannel(channelCode);
            List<BusiBankLimit> bankLimits = busiBankLimitMapper.selectByCondition(query);
            if (CollectionUtils.isEmpty(bankLimits)) {
                throw new BusinessException("银行代码转换错误");
            }
            if (bankLimits.size() != 1) {
                throw new BusinessException("银行代码转换错误");
            } else {
                BusiBankLimit busiBankLimit = bankLimits.get(0);
                limit.setBankCode(busiBankLimit.getBankCode());
            }
        }
        //华瑞银行转连连编码
        if (AppConstants.PayChannelCodeContants.LIANLIAN.equals(channelCode) && 8 != bankCode.length()) {
            BusiBankLimit query = new BusiBankLimit();
            query.setBankCode(bankCode);
            query.setPayChannel(AppConstants.PayChannelCodeContants.HUARUI_BANK);
            List<BusiBankLimit> bankLimits = busiBankLimitMapper.selectByCondition(query);
            if (CollectionUtils.isEmpty(bankLimits)) {
                throw new BusinessException("银行代码转换错误");
            }
            if (bankLimits.size() != 1) {
                throw new BusinessException("银行代码转换错误");
            } else {
                BusiBankLimit busiBankLimit = bankLimits.get(0);
                limit.setBankCode(busiBankLimit.getLlBankCode());
            }
        }
        List<BusiBankLimit> limits = busiBankLimitMapper.selectByCondition(limit);
        if (limits.isEmpty()) {
            throw new BusinessException("银行代码转换错误");
        } else {
            if (limits.size() > 1) {
                throw new BusinessException("银行代码转换错误");
            } else {
                return limits.get(0);
            }
        }
    }

    /**
     * 调用收银台解绑
     */
    private Map<String, Object> unBind(Map<String, Object> params) throws Exception {
        String merchantCode = configParamBean.getPayMerchantNo();
        String privateKey = configParamBean.getPayPrivateKey();
        String payUrl = configParamBean.getCashierAddrLan() + "/query/cardNewUnbind";
        Map<String, String> map = new HashMap<String, String>();
        String certNo = params.get("certNo").toString();
        String bankCardNo = params.get("bankCardNo").toString();
        String sign = MD5.MD5Encode(merchantCode + "|0|" + certNo + "|" + bankCardNo + "|" + privateKey);
        map.put("merchantCode", merchantCode);
        map.put("certType", "0");
        map.put("certNo", certNo);
        map.put("bankCardNo", bankCardNo);
        map.put("sign", sign);
        String strResult = HttpConnectionUtil.send2(payUrl, map);
        log.info("strResult=" + strResult);
        Map<String, Object> jsonMap = com.zdmoney.utils.CommonHelper.jsonToMapObj(strResult);
        return jsonMap;
    }

    /**
     * 连连-华瑞 银行代码转换
     *
     * @param channelCode
     * @param bankCode
     * @return
     */
    public String changeBankCode(String channelCode, String bankCode) {
        String code = bankCode;
        //连连转华瑞银行编码
        if (AppConstants.PayChannelCodeContants.HUARUI_BANK.equals(channelCode) && 8 == bankCode.length()) {
            BusiBankLimit query = new BusiBankLimit();
            query.setBankCode(null);
            query.setLlBankCode(bankCode);
            query.setPayChannel(channelCode);
            List<BusiBankLimit> bankLimits = busiBankLimitMapper.selectByCondition(query);
            if (CollectionUtils.isEmpty(bankLimits)) {
                throw new BusinessException("银行代码转换错误");
            }
            if (bankLimits.size() != 1) {
                throw new BusinessException("银行代码转换错误");
            } else {
                BusiBankLimit busiBankLimit = bankLimits.get(0);
                code = busiBankLimit.getCode();
            }
        }
        if (AppConstants.PayChannelCodeContants.LIANLIAN.equals(channelCode) && 8 != bankCode.length()) {
            BusiBankLimit queryLL = new BusiBankLimit();
            queryLL.setBankCode(bankCode);
            queryLL.setPayChannel(AppConstants.PayChannelCodeContants.HUARUI_BANK);
            List<BusiBankLimit> bankLimits = busiBankLimitMapper.selectByCondition(queryLL);
            if (CollectionUtils.isEmpty(bankLimits)) {
                throw new BusinessException("银行代码转换错误");
            }
            if (bankLimits.size() != 1) {
                throw new BusinessException("银行代码转换错误");
            } else {
                BusiBankLimit busiBankLimit = bankLimits.get(0);
                code = busiBankLimit.getLlBankCode();
            }
        }
        return code;
    }

    public boolean checkBindCard(CustomerMainInfo customerMainInfo) {
        boolean flag = false;
        if (org.apache.commons.lang3.StringUtils.isNotBlank(customerMainInfo.getBankAccountId())) {
            flag = true;
        } else {
            if (null != customerMainInfo.getOldBankAccountId()) {
                BusiPayChannel busiPayChannel = busiPayChannelService.getLianLianPayChannel();
                if (busiPayChannel != null) {
                    flag = true;
                }
            }
        }
        return flag;
    }

    /**
     * 本查询为分页查询，需要数据pageNo和pageSize两个参数，参数丢失则直接返回
     * 为了提升查询效率，需要设置查询时间范围，否则只查询7日前到当前时间的数据
     * 查询结果需要转换成指定的数据结构，注意设置总页数、记录总条数、查询状态码等信息
     * @param map
     * @return
     * @throws Exception
     */
    @Override
    public PageResultDto<BusiUnbindRecordVO> selectUnbindRecord(Map<String,Object> map){
        PageResultDto<BusiUnbindRecordVO> resultDto= new PageResultDto<>();
        if(map.get("pageNo") == null || map.get("pageSize") == null){
            resultDto.setCode("1111");
            resultDto.setMsg("分页参数丢失");
            return resultDto;
        }
        Page<BusiUnbindRecord> page = new Page<>();
        page.setBaseParam(map);
        String startDate = (String) map.get("startDate");
        String endDate = (String) map.get("endDate");

        if(StringUtils.isBlank(startDate) && StringUtils.isBlank(endDate)){
            map.put("startDate",DateUtil.getDateFormatString(DateUtil.getDateBefore(new Date(),7),DateUtil.fullFormat));
        }
        map.put("page",page);
        try {
            List<BusiUnbindRecordVO> recordList = busiUnbindRecordMapper.selectUnbindRecord(map);
            //设置resultDto的成员变量
            resultDto.setCode("0000");
            resultDto.setPageNo(page.getPageNo());
            resultDto.setTotalPage(page.getTotalPage());
            resultDto.setTotalSize(page.getTotalRecord());
            resultDto.setDataList(recordList);
        }catch(Exception e){
            log.error(e.getMessage(),e);
            resultDto.setCode("1111");
            resultDto.setMsg("查询数据发生异常");
        }
        return resultDto;
    }

    @Override
    public void persistUnbindBankPhoneRecord(CustomerBankAccount bankAccount, CustomerMainInfo mainInfo,String oldBindPhone) {
        BusiUnbindRecord unbindRecord = new BusiUnbindRecord();
        unbindRecord.setOperTime(new Date());
        unbindRecord.setUbIdnum(mainInfo.getCmIdnum());
        unbindRecord.setUbBankCode(bankAccount.getCbAccount());
        unbindRecord.setUbRealName(mainInfo.getCmRealName());
        unbindRecord.setUbTelephone(bankAccount.getCbBindPhone());
        unbindRecord.setOperMan(mainInfo.getCmRealName());
        unbindRecord.setRemark(oldBindPhone);
        unbindRecord.setUnType("3");
        int num = busiUnbindRecordMapper.insertBusiUnbindRecord(unbindRecord);
        if (num != 1) {
            log.error("保存更改银行预留手机号流水失败，客户号：" + mainInfo.getId() + "，bankId：" + bankAccount.getId());
            SysLogUtil.save("保存更改银行预留手机号流水失败，客户号：" + mainInfo.getId() + "，bankId：" + bankAccount.getId(), "Unbind-bank-card-fail", BankCardServiceImpl.class.getName());
            throw new BusinessException("保存更改银行预留手机号流水失败，请致电客服！");
        }
    }

    @Override
    public PageResultDto<BankCardBindNotifyDto> selectBindCardRecord(Map<String, Object> map) {
        PageResultDto<BankCardBindNotifyDto> resultDto= new PageResultDto<>();
        if(map.get("pageNo") == null || map.get("pageSize") == null){
            resultDto.setCode("1111");
            resultDto.setMsg("分页参数丢失");
            return resultDto;
        }
        Page<BankCardBindNotifyDto> page = new Page<>();
        page.setBaseParam(map);
        String startDate = (String) map.get("startDate");
        String endDate = (String) map.get("endDate");

        if(StringUtils.isBlank(startDate) && StringUtils.isBlank(endDate)){
            map.put("startDate",DateUtil.getDateFormatString(DateUtil.getDateBefore(new Date(),7),DateUtil.fullFormat));
        }
        map.put("page",page);
        try {
            List<BankCardBindNotifyDto> recordList = customerBankAccountMapper.selectBindCardRecord(map);
            //设置resultDto的成员变量
            resultDto.setCode("0000");
            resultDto.setPageNo(page.getPageNo());
            resultDto.setTotalPage(page.getTotalPage());
            resultDto.setTotalSize(page.getTotalRecord());
            resultDto.setDataList(recordList);
        }catch(Exception e){
            log.error(e.getMessage(),e);
            resultDto.setCode("1111");
            resultDto.setMsg("查询数据发生异常");
        }
        return resultDto;
    }

    @Override
    public PageResultDto<BusiUnbindRecordDto> queryBankUnbindRecord(Map<String, Object> map) {
        PageResultDto<BusiUnbindRecordDto> resultDto= new PageResultDto<>();
        if(map.get("pageNo") == null || map.get("pageSize") == null){
            resultDto.setCode("1111");
            resultDto.setMsg("分页参数丢失");
            return resultDto;
        }
        Page<BusiUnbindRecordDto> page = new Page<>();
        page.setBaseParam(map);
        String startDate = (String) map.get("startDate");
        String endDate = (String) map.get("endDate");

        if(StringUtils.isBlank(startDate) && StringUtils.isBlank(endDate)){
            map.put("startDate",DateUtil.getDateFormatString(DateUtil.getDateBefore(new Date(),7),DateUtil.fullFormat));
        }
        map.put("page",page);
        try {
            List<BusiUnbindRecordDto> recordList = busiUnbindRecordMapper.selectBankUnbindByTime(map);
            //设置resultDto的成员变量
            resultDto.setCode("0000");
            resultDto.setPageNo(page.getPageNo());
            resultDto.setTotalPage(page.getTotalPage());
            resultDto.setTotalSize(page.getTotalRecord());
            resultDto.setDataList(recordList);
        }catch(Exception e){
            log.error(e.getMessage(),e);
            resultDto.setCode("1111");
            resultDto.setMsg("查询数据发生异常");
        }
        return resultDto;
    }



}
