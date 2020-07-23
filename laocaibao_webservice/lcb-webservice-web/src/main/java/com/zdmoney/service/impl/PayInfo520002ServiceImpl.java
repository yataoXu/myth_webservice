package com.zdmoney.service.impl;

import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.zdmoney.assets.api.dto.agreement.AgreementNameDto;
import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.common.Result;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.constant.BusiConstants;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.facade.InnerEmployeeService;
import com.zdmoney.integral.api.common.dto.ResultDto;
import com.zdmoney.integral.api.dto.coupon.CouponDto;
import com.zdmoney.integral.api.dto.coupon.CouponSearchDto;
import com.zdmoney.integral.api.dto.product.IntegralProductDto;
import com.zdmoney.integral.api.dto.voucher.VoucherDto;
import com.zdmoney.integral.api.dto.voucher.VoucherSearchDto;
import com.zdmoney.integral.api.facade.ICouponFacadeService;
import com.zdmoney.integral.api.facade.IVoucherFacadeService;
import com.zdmoney.mapper.customer.CustomerMainInfoMapper;
import com.zdmoney.mapper.order.BusiOrderTempMapper;
import com.zdmoney.mapper.product.BusiProductLimitMapper;
import com.zdmoney.mapper.product.BusiProductRuleMapper;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.order.BusiOrderTemp;
import com.zdmoney.models.product.BusiProduct;
import com.zdmoney.models.product.BusiProductLimit;
import com.zdmoney.models.product.BusiProductRule;
import com.zdmoney.models.sys.SysParameter;
import com.zdmoney.service.AccountOverview520003Service;
import com.zdmoney.service.BusiProductService;
import com.zdmoney.service.PayInfo520002Service;
import com.zdmoney.service.SysParameterService;
import com.zdmoney.service.subject.SubjectService;
import com.zdmoney.utils.CoreUtil;
import com.zdmoney.utils.DateUtil;
import com.zdmoney.utils.LaocaiUtil;
import com.zdmoney.utils.NumberUtil;
import com.zdmoney.web.dto.PacketDTO;
import com.zdmoney.web.dto.PayInfo20DTO;
import com.zdmoney.web.dto.PayVoucherDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import websvc.models.Model_520002;
import websvc.models.Model_521002;
import websvc.req.ReqMain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by 00225181 on 2015/12/2.
 * 2.0获取支付信息
 */
@Service
@Slf4j
public class PayInfo520002ServiceImpl implements PayInfo520002Service {
    @Autowired
    BusiProductService productService;
    @Autowired
    BusiProductLimitMapper busiProductLimitMapper;
    @Autowired
    BusiProductRuleMapper busiProductRuleMapper;
    @Autowired
    private CustomerMainInfoMapper customerMainInfoMapper;
    @Autowired
    private BusiOrderTempMapper busiOrderTempMapper;
    @Autowired
    private ICouponFacadeService couponFacadeService;
    @Autowired
    private IVoucherFacadeService voucherFacadeService;
    @Autowired
    private AccountOverview520003Service accountOverview520003Service;
    @Autowired
    private SysParameterService sysParameterService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private InnerEmployeeService innerEmployeeService;
    @Autowired
    ConfigParamBean configParamBean;


    @Override
    public Result getPayInfo(ReqMain reqMain) throws Exception {
        Model_520002 model_520002 = (Model_520002) reqMain.getReqParam();
        PayInfo20DTO payInfo20DTO = new PayInfo20DTO();
        String customerId = model_520002.getCustomerId();
        if (!StringUtils.isEmpty(customerId)) {
            CustomerMainInfo mainInfo = customerMainInfoMapper.selectByPrimaryKey(Long.parseLong(customerId));
            if (mainInfo != null) {
                BigDecimal accountBalance = accountOverview520003Service.getAccountBalance(mainInfo);
                String balance = CoreUtil.BigDecimalAccurate(accountBalance);
                payInfo20DTO.setAccountBalance(balance);
                String orderId = model_520002.getOrderId();
                BusiOrderTemp busiOrderTemp = busiOrderTempMapper.selectByPrimaryKey(Long.parseLong(orderId));
                if (busiOrderTemp != null) {
                    //订单金额
                    payInfo20DTO.setOrderAmount(CoreUtil.BigDecimalAccurate(busiOrderTemp.getOrderAmt()));
                    Long productId = busiOrderTemp.getProductId();
                    if (productId == null) {
                        log.error("订单状态异常！没有产品编号，订单号：" + orderId);
                        throw new BusinessException("订单状态异常！");
                    }
                    //积分余额
                    Integer integral = accountOverview520003Service.getIntegralBalance(mainInfo);
                    BusiProduct busiProduct = productService.findOne(productId);
                    payInfo20DTO.setIsTransfer(busiProduct.getIsTransfer());
                    //转让
                    if (AppConstants.ProductTransferStatus.TRANSFER_PRODUCT.equals(busiProduct.getIsTransfer())) {
                        payInfo20DTO.setProductType("2");
                        payInfo20DTO.setTips("温馨提示：转让产品不能使用积分/红包/加息券");
                    }
                    //转让产品不能使用福利
                    if (AppConstants.ProductTransferStatus.COMMON_PRODUCT.equals(busiProduct.getIsTransfer())) {
                        //智选
//                        if(busiProduct.getContractId()>0){
//                            payInfo20DTO.setProductType("1");
//                        }
                        //优选
                        if (AppConstants.ProductSubjectType.SUBJECT_YX.equals(busiProduct.getSubjectType())) {
                            payInfo20DTO.setProductType("0");
                        } else if (AppConstants.ProductSubjectType.FINANCE_PLAN.equals(busiProduct.getSubjectType())) {//理财计划
                            payInfo20DTO.setProductType("4");
                        }
                        //智选
                        else {
                            payInfo20DTO.setProductType("1");
                        }
                        Long ruleId = busiProduct.getRuleId();
                        BusiProductRule busiProductRule = busiProductRuleMapper.selectByPrimaryKey(ruleId);
                        if (busiProductRule != null) {
                            payInfo20DTO.setLimitType(busiProduct.getLimitType());
                            payInfo20DTO.setNewRule(true);
                            String welfare = busiProductRule.getWelfare();
                            if (StringUtils.isNotBlank(welfare)) {
                                String[] statusList = welfare.split(",");
                                if (statusList != null && statusList.length > 0) {
                                    for (String status : statusList) {
                                        //积分
                                        if ("1".equals(status)) {
                                            payInfo20DTO.setCanUseIntegral(true);
                                            //查询积分兑换比例
                                            IntegralProductDto productDto = accountOverview520003Service.getIntegralProductInfo();
                                            payInfo20DTO.setIntegralBalance(integral.toString());
                                            if (productDto != null) {
                                                payInfo20DTO.setRate(String.valueOf(productDto.getIntegral()));
                                                payInfo20DTO.setCashAmt(String.valueOf(productDto.getReturnValue()));
                                            }
                                        }
                                        //红包
                                        if ("2".equals(status)) {
                                            payInfo20DTO.setCanUseCoupon(true);
                                            List<PacketDTO> dtos = getRedPacket(mainInfo, busiOrderTemp);
                                            payInfo20DTO.setRedPackets(dtos);
                                        }
                                        //加息券
                                        if ("3".equals(status)) {
                                            payInfo20DTO.setCanUseVoucher(true);
                                            List<PayVoucherDTO> payVoucherDTOs = getVoucherPacket(mainInfo, busiOrderTemp);
                                            payInfo20DTO.setVoucherDtos(payVoucherDTOs);
                                        }
                                        payInfo20DTO.setTips("温馨提示：积分、红包和加息券不能同时使用");
                                    }
                                } else {
                                    payInfo20DTO.setTips("温馨提示：此产品不能使用任何福利");
                                }
                            }
                        } else {
                            Long limitTpye = busiProduct.getLimitType();
                            BusiProductLimit busiProductLimit = busiProductLimitMapper.selectByPrimaryKey(limitTpye);
                            if (busiProductLimit != null) {
                                if (AppConstants.ProductLimitType.LIMIT.equals(busiProductLimit.getType())) {
                                    payInfo20DTO.setLimitType(AppConstants.ProductLimitType.LIMIT);//限购
                                } else if (AppConstants.ProductLimitType.NEW_HAND.equals(busiProductLimit.getType())) {
                                    payInfo20DTO.setLimitType(AppConstants.ProductLimitType.NEW_HAND);//新手标
//                             payInfo20DTO.setIntegralBalance("0");
                                } else if (AppConstants.ProductLimitType.WECHAT.equals(busiProductLimit.getType())) {
                                    payInfo20DTO.setLimitType(AppConstants.ProductLimitType.WECHAT);//微信
                                } else if (AppConstants.ProductLimitType.COMMON.equals(busiProductLimit.getType()) || AppConstants.ProductLimitType.CHANNEL.equals(busiProductLimit.getType())) {
//                                payInfo20DTO.setLimitType(AppConstants.ProductLimitType.COMMON);//非限购
                                    payInfo20DTO.setLimitType(AppConstants.ProductLimitType.COMMON.equals(busiProductLimit.getType()) ? AppConstants.ProductLimitType.COMMON : AppConstants.ProductLimitType.CHANNEL);
                                    //查询积分兑换比例
                                    IntegralProductDto productDto = accountOverview520003Service.getIntegralProductInfo();
                                    payInfo20DTO.setIntegralBalance(integral.toString());
                                    if (productDto != null) {
                                        payInfo20DTO.setRate(String.valueOf(productDto.getIntegral()));
                                        payInfo20DTO.setCashAmt(String.valueOf(productDto.getReturnValue()));
                                    }
                                    //查询红包
                                    List<PacketDTO> dtos = getRedPacket(mainInfo, busiOrderTemp);
                                    payInfo20DTO.setRedPackets(dtos);
//                                    if (busiProduct.getContractId() == 0L) {//非标的产品才能使用加息券
                                    //优选产品才能使用加息券
                                    if (AppConstants.ProductSubjectType.SUBJECT_YX.equals(busiProduct.getSubjectType())) {
                                        //查询加息券
                                        List<PayVoucherDTO> payVoucherDTOs = getVoucherPacket(mainInfo, busiOrderTemp);
                                        payInfo20DTO.setVoucherDtos(payVoucherDTOs);
                                    }
                                } else {
                                    payInfo20DTO.setLimitType(9L);//其他
                                }
                            }
                        }
                        //判断是否是员工
                        boolean isStaff = "0".equals(mainInfo.getUserLevel()) || "4".equals(mainInfo.getUserLevel());//sysStaffInfoService.isExistIdnum(mainInfo.getCmIdnum());
                        SysParameter sysParameter = null;
                        if (isStaff) {//员工
                            sysParameter = sysParameterService.findOneByPrType("integralProp");
                        } else {//非员工
                            sysParameter = sysParameterService.findOneByPrType("integralPropNEmp");
                        }
                        if (sysParameter != null) {
                            payInfo20DTO.setIntegralPro(sysParameter.getPrValue());
                        }
                    }
                    List<AgreementNameDto> agreementTempletes = subjectService.gainPayAgreementsTemplete(busiProduct);
                    payInfo20DTO.setAgreementTempletes(agreementTempletes);
                }
            }
        }
        return Result.success(payInfo20DTO);
    }


    //查询有效红包
    public List<PacketDTO> getRedPacket(CustomerMainInfo customerMainInfo, BusiOrderTemp orderTemp) {
        List<PacketDTO> redPacketDTOs = Lists.newArrayList();
        Long productId = orderTemp.getProductId();
        BusiProduct product = productService.findOne(productId);
        if ("1".equals(product.getIsNewHand())) {
            //新手标产品不能使用红包
            return redPacketDTOs;
        }
        CouponSearchDto dto = new CouponSearchDto();
        dto.setPageNo(1);
        dto.setPageSize(999);
        dto.setStatus("AVAIL");
        dto.setCurDate(new Date());
        dto.setAccountNo(customerMainInfo.getCmNumber());
        dto.setInvestAmount(orderTemp.getOrderAmt().intValue());
        if (AppConstants.OrderProductType.FINANCE_PLAN.toString().equals(product.getSubjectType())) {
            dto.setInvestPeriod(product.getCloseDay());
        } else {
            dto.setInvestPeriod(Integer.parseInt(String.valueOf((orderTemp.getInterestEndDate().getTime() - orderTemp.getInterestStartDate().getTime()) / (24 * 60 * 60 * 1000) + 1)));
        }
        ResultDto<List<CouponDto>> searchresult = couponFacadeService.getCouponsByAccountNo(dto);
        if (searchresult.isSuccess()) {
            for (CouponDto couponDto : searchresult.getData()) {
                PacketDTO packetDTO = new PacketDTO();
                packetDTO.setAmount(couponDto.getAmount().toString());
                packetDTO.setPacketId(couponDto.getNo());
                packetDTO.setCondition(couponDto.getConditionString());
                packetDTO.setPacketTime(couponDto.getDateString());
                packetDTO.setEndTime(couponDto.getEndTime().toString());
                packetDTO.setStartTime(couponDto.getStartTime().toString());
                packetDTO.setStatus(couponDto.getStatus());
                packetDTO.setInvestAmount(couponDto.getInvestAmount().toString());
                packetDTO.setInvestPrice(couponDto.getInvestPeriod().toString());
                redPacketDTOs.add(packetDTO);
            }
        }
        return redPacketDTOs;
    }

    //查询有效的加息券
    public List<PayVoucherDTO> getVoucherPacket(CustomerMainInfo customerMainInfo, BusiOrderTemp orderTemp) {
        List<PayVoucherDTO> payVoucherDTOs = Lists.newArrayList();
        Long productId = orderTemp.getProductId();
        BusiProduct product = productService.findOne(productId);
        //新手标产品不能使用加息券
        if ("1".equals(product.getIsNewHand())) {
            return payVoucherDTOs;
        }

        //已活动加息的产品不能使用加息券
        if (orderTemp.getActionRate() != null && orderTemp.getActionRate().compareTo(new BigDecimal(0)) == 1) {
            return payVoucherDTOs;
        }

        VoucherSearchDto dto = new VoucherSearchDto();
        dto.setAccountNo(customerMainInfo.getCmNumber());
        dto.setStatus("AVAIL");

        if (AppConstants.OrderProductType.FINANCE_PLAN.toString().equals(product.getSubjectType())) {
            dto.setActualInvestPeriod(product.getCloseDay());
        } else {
            Long betweenDays = (DateUtil.getBetweenDays(product.getInterestEndDate(), product.getInterestStartDate())) + 1;
            dto.setActualInvestPeriod(betweenDays.intValue());
        }

        dto.setActualInvestAmount(orderTemp.getOrderAmt());
        dto.setCurDateStr(DateUtil.getDateFormatString(new Date(), DateUtil.YMDSTR));
        ResultDto<List<VoucherDto>> resultDto = voucherFacadeService.getVouchersByAccountNo(dto);
        if (resultDto.isSuccess()) {
            PayVoucherDTO payVoucherDTO = null;
            for (VoucherDto voucherDto : resultDto.getData()) {
                payVoucherDTO = new PayVoucherDTO();
                payVoucherDTO.setVoucherNo(voucherDto.getNo());
                payVoucherDTO.setVoucherTitle(voucherDto.getDays() == null || voucherDto.getDays() == 0 ? "加息券" : voucherDto.getDays() + "天加息券");
                payVoucherDTO.setConditionString(voucherDto.getConditionString());
                payVoucherDTO.setRate(voucherDto.getRate());
                payVoucherDTO.setRateStr(NumberUtil.fortmatBigDecimal(voucherDto.getRate().multiply(new BigDecimal(100))) + "%");
                payVoucherDTO.setVoucherMemo("加息产品除外");
                payVoucherDTO.setValidDate(voucherDto.getDateString());
                payVoucherDTO.setPlusInterestDays(voucherDto.getDays() == null ? 0 : voucherDto.getDays());
                Integer flag = -1;
                String status = voucherDto.getStatus();
                if ("AVAIL".equals(status)) {
                    flag = 1;
                } else if ("CONSUMED".equals(status)) {
                    flag = 3;
                } else if ("OVERDUE".equals(status)) {
                    flag = 2;
                } else {
                    flag = -1;
                }
                payVoucherDTO.setFlag(flag);
                payVoucherDTOs.add(payVoucherDTO);
            }
        }
        return payVoucherDTOs;

    }

    /**
     * 优惠券接口 (收银台)
     */
    public Result vouchers(ReqMain reqMain) throws Exception {
        Model_521002 model_521002 = (Model_521002) reqMain.getReqParam();
        PayInfo20DTO payInfo20DTO = new PayInfo20DTO();
        Long customerId = model_521002.getCustomerId();
        Long productId = model_521002.getProductId();
        BigDecimal orderAmt = model_521002.getOrderAmt();
        String orderId = model_521002.getOrderId();
        String IPStr = reqMain.getReqHeadParam().getIp();
        String loginType = BusiConstants.LOGIN_TYPE_MANAGE;
        if (reqMain != null) {
            loginType = LaocaiUtil.getLoginType(reqMain.getReqHeadParam());
        }
        CustomerMainInfo mainInfo = customerMainInfoMapper.selectByPrimaryKey(customerId);
        if (mainInfo == null) {
            throw new BusinessException("该用户信息不存在。");
        }
        if (orderAmt.compareTo(new BigDecimal(0)) == -1) {
            throw new BusinessException("输入金额不合法");
        }
        BigDecimal accountBalance = accountOverview520003Service.getAccountBalance(mainInfo);
        String balance = CoreUtil.BigDecimalAccurate(accountBalance);
        payInfo20DTO.setAccountBalance(balance);

        //订单金额 (pc使用)
        if (StringUtils.isNotBlank(orderId)) {
            BusiOrderTemp busiOrderTemp = busiOrderTempMapper.selectByPrimaryKey(Long.parseLong(orderId));
            if (busiOrderTemp != null) {
                payInfo20DTO.setOrderAmount(CoreUtil.BigDecimalAccurate(busiOrderTemp.getOrderAmt()));
            }
        }

        //积分余额
        Integer integral = accountOverview520003Service.getIntegralBalance(mainInfo);
        BusiProduct busiProduct = productService.findOne(productId);
        if (busiProduct == null) {
            throw new BusinessException("该产品id不存在:" + productId);
        }
        payInfo20DTO.setIsTransfer(busiProduct.getIsTransfer());
        //转让
        if (AppConstants.ProductTransferStatus.TRANSFER_PRODUCT.equals(busiProduct.getIsTransfer())) {
            payInfo20DTO.setProductType("2");
            payInfo20DTO.setTips("温馨提示：转让产品不能使用积分/红包/加息券");
        }
        //转让产品不能使用福利
        if (AppConstants.ProductTransferStatus.COMMON_PRODUCT.equals(busiProduct.getIsTransfer())) {
            //优选
            if (AppConstants.ProductSubjectType.SUBJECT_YX.equals(busiProduct.getSubjectType())) {
                payInfo20DTO.setProductType("0");
            } else if (AppConstants.ProductSubjectType.FINANCE_PLAN.equals(busiProduct.getSubjectType())) {//理财计划
                payInfo20DTO.setProductType("4");
            } else {//智选
                payInfo20DTO.setProductType("1");
            }
            Long ruleId = busiProduct.getRuleId();
            BusiProductRule busiProductRule = busiProductRuleMapper.selectByPrimaryKey(ruleId);
            if (busiProductRule != null) {
                payInfo20DTO.setLimitType(busiProduct.getLimitType());
                payInfo20DTO.setNewRule(true);
                String welfare = busiProductRule.getWelfare();
                if (StringUtils.isNotBlank(welfare)) {
                    String[] statusList = welfare.split(",");
                    if (statusList != null && statusList.length > 0) {
                        for (String status : statusList) {
                            //积分
                            if ("1".equals(status)) {
                                payInfo20DTO.setCanUseIntegral(true);
                                //查询积分兑换比例
                                IntegralProductDto productDto = accountOverview520003Service.getIntegralProductInfo();
                                payInfo20DTO.setIntegralBalance(integral.toString());
                                if (productDto != null) {
                                    payInfo20DTO.setRate(String.valueOf(productDto.getIntegral()));
                                    payInfo20DTO.setCashAmt(String.valueOf(productDto.getReturnValue()));
                                }
                            }
                            //红包
                            if ("2".equals(status)) {
                                payInfo20DTO.setCanUseCoupon(true);
                                List<PacketDTO> dtos = getRedPacketForCashierPay(mainInfo, busiProduct, orderAmt);
                                payInfo20DTO.setRedPackets(dtos);
                            }
                            //加息券
                            if ("3".equals(status)) {
                                payInfo20DTO.setCanUseVoucher(true);
                                List<PayVoucherDTO> payVoucherDTOs = getVoucherPacketForCashierPay(mainInfo, busiProduct, orderAmt);
                                payInfo20DTO.setVoucherDtos(payVoucherDTOs);
                            }
                            payInfo20DTO.setTips("温馨提示：积分、红包和加息券不能同时使用");
                        }
                    } else {
                        payInfo20DTO.setTips("温馨提示：此产品不能使用任何福利");
                    }
                } else {
                    //配置福利为空(不可用福利)
                    payInfo20DTO.setUseWelfare("1");
                }
            } else {
                Long limitTpye = busiProduct.getLimitType();
                BusiProductLimit busiProductLimit = busiProductLimitMapper.selectByPrimaryKey(limitTpye);
                if (busiProductLimit != null) {
                    if (AppConstants.ProductLimitType.LIMIT.equals(busiProductLimit.getType())) {
                        payInfo20DTO.setLimitType(AppConstants.ProductLimitType.LIMIT);//限购
                    } else if (AppConstants.ProductLimitType.NEW_HAND.equals(busiProductLimit.getType())) {
                        payInfo20DTO.setLimitType(AppConstants.ProductLimitType.NEW_HAND);//新手标
                    } else if (AppConstants.ProductLimitType.WECHAT.equals(busiProductLimit.getType())) {
                        payInfo20DTO.setLimitType(AppConstants.ProductLimitType.WECHAT);//微信
                    } else if (AppConstants.ProductLimitType.COMMON.equals(busiProductLimit.getType()) || AppConstants.ProductLimitType.CHANNEL.equals(busiProductLimit.getType())) {
                        //payInfo20DTO.setLimitType(AppConstants.ProductLimitType.COMMON);//非限购
                        payInfo20DTO.setLimitType(AppConstants.ProductLimitType.COMMON.equals(busiProductLimit.getType()) ? AppConstants.ProductLimitType.COMMON : AppConstants.ProductLimitType.CHANNEL);
                        //查询积分兑换比例
                        IntegralProductDto productDto = accountOverview520003Service.getIntegralProductInfo();
                        payInfo20DTO.setIntegralBalance(integral.toString());
                        if (productDto != null) {
                            payInfo20DTO.setRate(String.valueOf(productDto.getIntegral()));
                            payInfo20DTO.setCashAmt(String.valueOf(productDto.getReturnValue()));
                        }
                        //查询红包
                        List<PacketDTO> dtos = getRedPacketForCashierPay(mainInfo, busiProduct, orderAmt);
                        payInfo20DTO.setRedPackets(dtos);
                        //if (busiProduct.getContractId() == 0L) {//非标的产品才能使用加息券
                        //优选产品才能使用加息券
                        if (AppConstants.ProductSubjectType.SUBJECT_YX.equals(busiProduct.getSubjectType())) {
                            //查询加息券
                            List<PayVoucherDTO> payVoucherDTOs = getVoucherPacketForCashierPay(mainInfo, busiProduct, orderAmt);
                            payInfo20DTO.setVoucherDtos(payVoucherDTOs);
                        }
                    } else {
                        payInfo20DTO.setLimitType(9L);//其他
                    }
                }
            }
            boolean isStaff = "0".equals(mainInfo.getUserLevel()) || "4".equals(mainInfo.getUserLevel());
            SysParameter sysParameter = null;
            if (isStaff) {//员工
                sysParameter = sysParameterService.findOneByPrType("integralProp");
            } else {//非员工
                sysParameter = sysParameterService.findOneByPrType("integralPropNEmp");
            }
            if (sysParameter != null) {
                payInfo20DTO.setIntegralPro(sysParameter.getPrValue());
            }

            //专区产品
            Boolean productLimitType = innerEmployeeService.isProductLimitType(String.valueOf(busiProduct.getLimitType()));
            if (productLimitType && !(BusiConstants.LOGIN_TYPE_MANAGE.equalsIgnoreCase(loginType))) {
                // 订单金额必须是10的倍数
                 if (orderAmt.intValue() % 10 !=0){
                     throw new BusinessException("订单金额必须是10的倍数!");
                 }
                payInfo20DTO.setLimitType(busiProduct.getLimitType());
                Result staffAuth = innerEmployeeService.staffAuth(customerId,IPStr);
                if (!(staffAuth.getSuccess()) ||  StrUtil.equals(staffAuth.getData().toString(),"0")) {
                    throw new BusinessException(staffAuth.getMessage());
                }
                //内部员工积分兑换比例
                payInfo20DTO.setCanUseIntegralStaffRate(configParamBean.getStaffPaymentRate());
                //实际支付多少金额
                BigDecimal actualPayment = orderAmt.multiply(BigDecimal.ONE.subtract(new BigDecimal(payInfo20DTO.getCanUseIntegralStaffRate()))).setScale(2,BigDecimal.ROUND_DOWN);
                payInfo20DTO.setActualPayment(actualPayment);
                //抵扣多少金额
                payInfo20DTO.setDeductionAmount(orderAmt.subtract(actualPayment));
                //需要使用多少积分
                Integer actualUseIntegral = payInfo20DTO.getDeductionAmount().multiply(new BigDecimal(payInfo20DTO.getRate())).intValue();
                payInfo20DTO.setActualUseIntegral(actualUseIntegral);
            }
        }
        List<AgreementNameDto> agreementTempletes = subjectService.gainPayAgreementsTemplete(busiProduct);
        payInfo20DTO.setAgreementTempletes(agreementTempletes);
        return Result.success(payInfo20DTO);
    }

    //查询有效红包（收银台）
    public List<PacketDTO> getRedPacketForCashierPay(CustomerMainInfo customerMainInfo, BusiProduct product, BigDecimal orderAmt) {
        List<PacketDTO> redPacketDTOs = Lists.newArrayList();
        //v4.7 新手可以使用福利
        //if ("1".equals(product.getIsNewHand())) {
        //            //新手标产品不能使用红包
        //            return redPacketDTOs;
        //        }
        CouponSearchDto dto = new CouponSearchDto();
        dto.setPageNo(1);
        dto.setPageSize(999);
        dto.setStatus("AVAIL");
        dto.setCurDate(new Date());
        dto.setAccountNo(customerMainInfo.getCmNumber());
        dto.setInvestAmount(orderAmt.intValue());
        if (AppConstants.OrderProductType.FINANCE_PLAN.toString().equals(product.getSubjectType())) {
            dto.setInvestPeriod(product.getCloseDay());
        } else {
            dto.setInvestPeriod(Integer.parseInt(String.valueOf((product.getInterestEndDate().getTime() - product.getInterestStartDate().getTime()) / (24 * 60 * 60 * 1000) + 1)));
        }
        ResultDto<List<CouponDto>> searchresult = couponFacadeService.getCouponsByAccountNo(dto);
        if (searchresult.isSuccess()) {
            for (CouponDto couponDto : searchresult.getData()) {
                PacketDTO packetDTO = new PacketDTO();
                packetDTO.setAmount(couponDto.getAmount().toString());
                packetDTO.setPacketId(couponDto.getNo());
                packetDTO.setCondition(couponDto.getConditionString());
                packetDTO.setPacketTime(couponDto.getDateString());
                packetDTO.setEndTime(couponDto.getEndTime().toString());
                packetDTO.setStartTime(couponDto.getStartTime().toString());
                packetDTO.setStatus(couponDto.getStatus());
                packetDTO.setInvestAmount(couponDto.getInvestAmount().toString());
                packetDTO.setInvestPrice(couponDto.getInvestPeriod().toString());
                redPacketDTOs.add(packetDTO);
            }
        }
        return redPacketDTOs;
    }

    //查询有效的加息券（收银台）
    public List<PayVoucherDTO> getVoucherPacketForCashierPay(CustomerMainInfo customerMainInfo, BusiProduct product, BigDecimal orderAmt) {
        List<PayVoucherDTO> payVoucherDTOs = Lists.newArrayList();
        //新手标产品不能使用加息券
//        if ("1".equals(product.getIsNewHand())) {
//            return payVoucherDTOs;
//        }
        //已活动加息的产品不能使用加息券
        if (product.getAddInterest() != null && product.getAddInterest().compareTo(new BigDecimal(0)) == 1) {
            return payVoucherDTOs;
        }
        //标的产品不能使用加息券
        if (AppConstants.ProductSubjectType.SUBJECT_ZX.equals(product.getSubjectType()) || AppConstants.ProductSubjectType.SUBJECT_GD.equals(product.getSubjectType())) {
            return payVoucherDTOs;
        }

        VoucherSearchDto dto = new VoucherSearchDto();
        dto.setAccountNo(customerMainInfo.getCmNumber());
        dto.setStatus("AVAIL");

        if (AppConstants.OrderProductType.FINANCE_PLAN.toString().equals(product.getSubjectType())) {
            dto.setActualInvestPeriod(product.getCloseDay());
        } else {
            Long betweenDays = (DateUtil.getBetweenDays(product.getInterestEndDate(), product.getInterestStartDate())) + 1;
            dto.setActualInvestPeriod(betweenDays.intValue());
        }

        dto.setActualInvestAmount(orderAmt);
        dto.setCurDateStr(DateUtil.getDateFormatString(new Date(), DateUtil.YMDSTR));
        ResultDto<List<VoucherDto>> resultDto = voucherFacadeService.getVouchersByAccountNo(dto);
        if (resultDto.isSuccess()) {
            PayVoucherDTO payVoucherDTO = null;
            for (VoucherDto voucherDto : resultDto.getData()) {
                payVoucherDTO = new PayVoucherDTO();
                payVoucherDTO.setVoucherNo(voucherDto.getNo());
                payVoucherDTO.setVoucherTitle(voucherDto.getDays() == null || voucherDto.getDays() == 0 ? "加息券" : voucherDto.getDays() + "天加息券");
                payVoucherDTO.setConditionString(voucherDto.getConditionString());
                payVoucherDTO.setRate(voucherDto.getRate());
                payVoucherDTO.setRateStr(NumberUtil.fortmatBigDecimal(voucherDto.getRate().multiply(new BigDecimal(100))) + "%");
                payVoucherDTO.setVoucherMemo("加息产品除外");
                payVoucherDTO.setValidDate(voucherDto.getDateString());
                payVoucherDTO.setPlusInterestDays(voucherDto.getDays() == null ? 0 : voucherDto.getDays());
                Integer flag = -1;
                String status = voucherDto.getStatus();
                if ("AVAIL".equals(status)) {
                    flag = 1;
                } else if ("CONSUMED".equals(status)) {
                    flag = 3;
                } else if ("OVERDUE".equals(status)) {
                    flag = 2;
                } else {
                    flag = -1;
                }
                payVoucherDTO.setFlag(flag);
                payVoucherDTOs.add(payVoucherDTO);
            }
        }
        return payVoucherDTOs;
    }

}
