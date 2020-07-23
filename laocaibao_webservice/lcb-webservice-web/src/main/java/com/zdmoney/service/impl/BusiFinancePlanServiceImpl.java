package com.zdmoney.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zdmoney.assets.api.common.dto.AssetsResultDto;
import com.zdmoney.assets.api.dto.agreement.AgreementNameDto;
import com.zdmoney.assets.api.dto.agreement.OrderAgreementsResDto;
import com.zdmoney.assets.api.dto.agreement.OrdersAgreementsReqDto;
import com.zdmoney.assets.api.dto.match.MatchDebtItemDto;
import com.zdmoney.assets.api.dto.match.MatchDebtReqDto;
import com.zdmoney.assets.api.dto.match.MatchProductReqDto;
import com.zdmoney.assets.api.dto.match.MatchRevokeReqDto;
import com.zdmoney.assets.api.dto.match.enums.DebtType;
import com.zdmoney.assets.api.dto.subject.SubjectDto;
import com.zdmoney.assets.api.dto.subject.SubjectInfoObtainDto;
import com.zdmoney.assets.api.dto.subject.borrow.enums.CompanyNature;
import com.zdmoney.assets.api.facade.subject.ILCBSubjectFacadeService;
import com.zdmoney.assets.api.facade.subject.ILcbFinancialPlanFacadeService;
import com.zdmoney.common.Result;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.constant.Constants;
import com.zdmoney.constant.OrderConstants;
import com.zdmoney.enm.OrderGenerateType;
import com.zdmoney.helper.SerialNumberGenerator;
import com.zdmoney.integral.api.dto.appointment.AppointmentDto;
import com.zdmoney.integral.api.dto.lcbaccount.AccountRefundCollectDto;
import com.zdmoney.integral.api.dto.lcbaccount.FinancePlanCancelDto;
import com.zdmoney.integral.api.dto.lcbaccount.FinancePlanCancelResultDto;
import com.zdmoney.integral.api.dto.order.IntegralOrderRefundDto;
import com.zdmoney.integral.api.dto.voucher.VoucherDto;
import com.zdmoney.integral.api.facade.IBidFacadeService;
import com.zdmoney.life.api.utils.JsonUtils;
import com.zdmoney.mapper.BusiAbnorMatchSucInfoMapper;
import com.zdmoney.mapper.BusiMatchResultInfoMapper;
import com.zdmoney.mapper.BusiMatchSucInfoMapper;
import com.zdmoney.mapper.financePlan.BusiMatchExceptionMapper;
import com.zdmoney.mapper.financePlan.BusiOrderSubMapper;
import com.zdmoney.mapper.financePlan.DebtDetailMapper;
import com.zdmoney.mapper.financePlan.FundDetailMapper;
import com.zdmoney.mapper.order.BusiOrderIntegralMapper;
import com.zdmoney.mapper.order.BusiOrderMapper;
import com.zdmoney.mapper.product.BusiProductContractMapper;
import com.zdmoney.mapper.product.BusiProductSubMapper;
import com.zdmoney.mapper.transfer.BusiDebtTransferMapper;
import com.zdmoney.models.BusiAbnorMatchSucInfo;
import com.zdmoney.models.BusiMatchResultInfo;
import com.zdmoney.models.BusiMatchSucInfo;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.financePlan.*;
import com.zdmoney.models.order.BusiOrder;
import com.zdmoney.models.order.BusiOrderIntegral;
import com.zdmoney.models.product.BusiProduct;
import com.zdmoney.models.product.BusiProductContract;
import com.zdmoney.models.product.BusiProductSub;
import com.zdmoney.models.sys.SysParameter;
import com.zdmoney.models.transfer.BusiDebtTransfer;
import com.zdmoney.service.*;
import com.zdmoney.service.order.OrderService;
import com.zdmoney.service.subject.SubjectService;
import com.zdmoney.utils.GsonUtils;
import com.zdmoney.utils.*;
import com.zdmoney.vo.BusiDebtDetailVo;
import com.zdmoney.vo.OrderVo;
import com.zdmoney.vo.trade.TenderVo;
import com.zdmoney.vo.trade.TransferVo;
import com.zdmoney.webservice.api.common.dto.PageResultDto;
import com.zdmoney.webservice.api.common.dto.ResultDto;
import com.zdmoney.webservice.api.dto.enm.SuperfluousType;
import com.zdmoney.webservice.api.dto.plan.*;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.ContextLoader;
import tk.mybatis.mapper.entity.Example;
import websvc.exception.BusinessException;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by gaol on 2017/6/5
 **/
@Lazy
@Service
@Slf4j
public class BusiFinancePlanServiceImpl implements BusiFinancePlanService {

    @Autowired
    BusiProductService busiProductService;
    @Autowired
    BusiOrderIntegralMapper busiOrderIntegralMapper;
    @Autowired
    private DebtDetailMapper debtDetailMapper;
    @Autowired
    private FundDetailMapper fundDetailMapper;
    @Autowired
    private SysParameterService sysParameterService;
    @Autowired
    private BusiOrderService busiOrderService;
    @Autowired
    private ILcbFinancialPlanFacadeService lcbFinancialPlanFacadeService;
    @Autowired
    private BusiOrderSubMapper busiOrderSubMapper;
    @Autowired
    private CustomerMainInfoService customerMainInfoService;
    @Autowired
    private BusiOrderIntegralService busiOrderIntegralService;
    @Autowired
    private BusiProductContractMapper busiProductContractMapper;
    @Autowired
    private IBidFacadeService bidFacadeService;
    @Autowired
    private ILCBSubjectFacadeService lcbSubjectFacadeService;
    @Autowired
    private SubjectService subjectService;
    @Autowired
	@Lazy
    private TradeService tradeService;
    @Autowired
	@Lazy
    private OrderService orderService;
    @Autowired
    private BusiProductSubMapper busiProductSubMapper;
    @Autowired
    private BusiDebtTransferMapper debtTransferMapper;
    @Autowired
    private BusiMatchExceptionMapper busiMatchExceptionMapper;

    @Autowired
    private BusiMatchResultInfoMapper busiMatchResultInfoMapper;
    @Autowired
    private BusiMatchSucInfoMapper busiMatchSucInfoMapper;

    @Autowired
    private AccountOverview520003Service accountOverview520003Service;

    @Autowired
    BusiOrderMapper busiOrderMapper;

    @Autowired
    BusiAbnorMatchSucInfoMapper busiAbnorMatchSucInfoMapper;

    @Override
    public PageResultDto<BusiDebtDetailVO> queryDeptProductDetail(DebtDetailReqDTO debtDetailDTO) {
        if (debtDetailDTO.getMainProductId() == null) {
            throw new BusinessException("主产品编号不能为空！");
        }
        int pageSize = debtDetailDTO.getPageSize();
        int pageNo = debtDetailDTO.getPageNo();
        debtDetailDTO.setPageNo(pageSize * pageNo);
        debtDetailDTO.setPageSize(pageSize * (pageNo - 1));

        long count = debtDetailMapper.countDeptProductDetail(debtDetailDTO);
        List<BusiDebtDetailVO> debtDetaillist = debtDetailMapper.queryDeptProductDetail(debtDetailDTO);

        return new PageResultDto(debtDetaillist, (int) count, pageSize);
    }

    @Override
    public PageResultDto<BusiFundDetailVO> queryFundDetail(FundDetailReqDTO fundDetailDTO) {
        if (fundDetailDTO.getMainProductId() == null) {
            throw new BusinessException("主产品编号不能为空！");
        }
        int pageSize = fundDetailDTO.getPageSize();
        int pageNo = fundDetailDTO.getPageNo();
        fundDetailDTO.setPageNo(pageSize * pageNo);
        fundDetailDTO.setPageSize(pageSize * (pageNo - 1));

        long count = fundDetailMapper.countFundDetail(fundDetailDTO);
        List<BusiFundDetailVO> fundDetaillist = fundDetailMapper.queryFundDetail(fundDetailDTO);

        return new PageResultDto(fundDetaillist, (int) count, pageSize);
    }

    @Override
    public Page<BusiDebtDetailVo> findBusiDebtDetails(int pageNo, int pageSize, String productId) {
        Page<BusiDebtDetailVo> page = new Page<>();
        Map<String, Object> map = Maps.newTreeMap();
        if (StringUtils.isNotEmpty(productId)) {
            Map<String, Object> paramsMap = new HashMap<>();
            paramsMap.put("productId", productId);
            BusiProduct bp = busiProductService.findProductById(paramsMap);
            if (bp == null) {
                throw new BusinessException("该产品Id不存在！");
            }
            map.put("productId", productId);
        }
        page.setPageNo(pageNo);
        page.setPageSize(pageSize);
        map.put("page", page);
        List<BusiDebtDetailVo> busis = debtDetailMapper.selectFinancePlanDetails(map);
        log.info("产品id:{},查询对应债权数量：{}",productId,busis.size());
        page.setResults(busis);
        return page;
    }

    @Override
    public int insertDebtDetail(BusiDebtDetailVO busiDebtDetail) {
        return debtDetailMapper.insertDebtDetail(busiDebtDetail);
    }

    @Override
    public SpecialFinancialPlannerVO querySpecialFinancePeopleCode() {
        String cmNumber = this.gainDefaultSpecialLender();
        SpecialFinancialPlannerVO specialFinancialPlanner = new SpecialFinancialPlannerVO();
        if (org.apache.commons.lang3.StringUtils.isNotBlank(cmNumber)) {
            specialFinancialPlanner = customerMainInfoService.querySpecialFinancialPlannerInfo(cmNumber);
        }
        return specialFinancialPlanner;
    }

    @Override
    public Page<BusiDebtDetailVo> findOrderBusiDebtDetails(int pageNo, int pageSize, String orderId) {
        Page<BusiDebtDetailVo> page = new Page<>();
        Map<String, Object> map = Maps.newTreeMap();
        if (StringUtils.isNotEmpty(orderId)) {
            BusiOrder order = busiOrderService.selectByPrimaryKey(Long.parseLong(orderId));
            if (order == null) {
                throw new BusinessException("该订单不存在！");
            }
            map.put("orderId", orderId);
        }
        page.setPageNo(pageNo);
        page.setPageSize(pageSize);
        map.put("page", page);
        List<BusiDebtDetailVo> busis = debtDetailMapper.selectOrderBusiDebtDetails(map);
        if (CollectionUtils.isEmpty(busis)) {
            log.info("查询理财计划订单【{}】,暂无债权信息", orderId);
            return page;
        }
        log.info("订单id:{},查询对应债权数量：{}",orderId,busis.size());
        List<String> orders = new ArrayList<>();
        for (BusiDebtDetailVo busiVo : busis) {
            BusiOrderSub busiOrderSub = this.selectOrderSubByPrimaryKey(busiVo.getId());
            String orderNum = busiOrderSub.getOrderId();
            orders.add(orderNum);
        }
        OrdersAgreementsReqDto reqDto = new OrdersAgreementsReqDto();
        reqDto.setOrders(orders);
        reqDto.setPartnerNo("LCB");
        try {
            AssetsResultDto<List<OrderAgreementsResDto>> resultDto = lcbSubjectFacadeService.getOrdersAgreements(reqDto);
            if (!resultDto.isSuccess()) {
                log.error("调用标的系统，查询理财计划借款用户协议失败，失败原因【{}】", resultDto.getMsg());
            }
            if (!CollectionUtils.isEmpty(resultDto.getData())) {
                List<OrderAgreementsResDto> resList = resultDto.getData();
                log.info("*************获取借款人协议**************>:" + resList);
                Map agreementsMap = new HashMap();
                for(int i=0;i<resList.size();i++){
                    String orderNo =resList.get(i).getOrderNo();
                    List<AgreementNameDto> agreementTempletes =resList.get(i).getAgreementNameDtos();
                    agreementsMap.put(orderNo,agreementTempletes);
                }
                for (BusiDebtDetailVo busiVo : busis) {
                    BusiOrderSub busiOrderSub = this.selectOrderSubByPrimaryKey(busiVo.getId());
                    busiVo.setAgreementTempletes((List<AgreementNameDto>) agreementsMap.get(busiOrderSub.getOrderId()));
                }
            }
        } catch (Exception e) {
            log.error("调用标的系统，查询理财计划借款用户协议异常，订单ID【{}】", orderId, e);
            page.setResults(busis);
            return page;
        }
        page.setResults(busis);
        return page;
    }

    @Override
    public ResultDto sendProduct(SendProductReqDTO sendProductDTO) {
        sendProductDTO.validate();
        MatchProductReqDto matchProductReqDto = new MatchProductReqDto();
        matchProductReqDto.setProductCode(sendProductDTO.getProductCode());
        matchProductReqDto.setProductPrincipal(sendProductDTO.getProductPrincipal());
        matchProductReqDto.setMinMathAmount(sendProductDTO.getMinMatchAmount());
        matchProductReqDto.setAppointmentTime(sendProductDTO.getSettleDate());
        matchProductReqDto.setPartnerNo(AppConstants.PARTNER_NO);
        matchProductReqDto.setCloseDay(sendProductDTO.getCloseDay());
        matchProductReqDto.setYearRate(sendProductDTO.getYearRate());
        AssetsResultDto resultDto = null;
        try {
            resultDto = lcbFinancialPlanFacadeService.sendProduct(matchProductReqDto);
        } catch (Exception e) {
            log.error("发送理财计划产品失败", e);
            return new ResultDto("发送理财计划产品失败", false);
        }
        if (resultDto.isSuccess()) {
            return new ResultDto("发送理财计划产品成功", true);
        }
        return new ResultDto(resultDto.getMsg(), false);
    }

    @Override
    public ResultDto<BuyBackDTO> buyBack(String subjectNo) {
        log.info("异常回购标的编号:" + subjectNo);
        BuyBackDTO buyBackDTO = new BuyBackDTO();
        List<BuyBackOrder> buyBackOrderList = new ArrayList<>();
        try {
            Map<String, Object> paramsMap = new HashMap<>();
            paramsMap.put("subjectNo", subjectNo);
            paramsMap.put("orderStatus", AppConstants.ORDER_SUB_STATUS.ORDER_STATUS_1);
            // 查询标的状态为未完结的订单信息
            List<BusiOrderSub> orderSubList = busiOrderSubMapper.queryOrderSubInfo(paramsMap);
            // 查询特殊理财人
            String cmNumber = this.gainDefaultSpecialLender();
            CustomerMainInfo customerMainInfo = customerMainInfoService.findOneByCmNumber(cmNumber);
            if (customerMainInfo == null) {
                throw new BusinessException("特殊理财人不存在！");
            }
            buyBackDTO.setSubjectNo(subjectNo);
            buyBackDTO.setSpecialCmNumber(customerMainInfo.getCmNumber());

            if (CollectionUtils.isEmpty(orderSubList)) {
                throw new BusinessException("标的编号: " + subjectNo + "没有持有中的子单!");
            }
            for (BusiOrderSub orderSub : orderSubList) {
                BuyBackOrder order = new BuyBackOrder();
                order.setOrderNo(orderSub.getOrderId());
                order.setFinanceId(orderSub.getDebtNo());
                order.setPlanId(orderSub.getPlanId());
                buyBackOrderList.add(order);
            }
            buyBackDTO.setBuyBackOrderList(buyBackOrderList);
        } catch (Exception e){
            log.error("异常回购异常,标的编号:{}", subjectNo, e);
            MailUtil.sendMail("异常回购异常,标的编号" + subjectNo, e);
            return new ResultDto("异常回购失败", e, false);
        }
        return new ResultDto(buyBackDTO);
    }

    @Transactional
    public boolean debtMatch(DebtMatchReqDTO debtMatchReqDTO) {
        JSONObject json = JSONObject.fromObject(debtMatchReqDTO);
        log.info("匹配结果:" + json.toString());
        DebtMatchDTO debtMatchDTO = new DebtMatchDTO();
        debtMatchDTO.setProductId(debtMatchReqDTO.getProductId());
        debtMatchDTO.setStatus(debtMatchReqDTO.getStatus());
        if ("1".equals(debtMatchReqDTO.getStatus())) {
            List<DebtDetailReqDTO> debtDetailList = debtMatchReqDTO.getDebtDetailDTOList();
            List<DebtDetailDTO> debtDetailDTOList = new ArrayList<>();
            for (DebtDetailReqDTO debtDetailReqDTO : debtDetailList) {
                DebtDetailDTO debtDetailDTO = new DebtDetailDTO();
                CopyUtil.copyProperties(debtDetailDTO, debtDetailReqDTO);
                debtDetailDTOList.add(debtDetailDTO);
            }
            debtMatchDTO.setDebtDetailDTOList(debtDetailDTOList);
        }
        debtMatchDTO.setProductPrincipal(debtMatchReqDTO.getProductPrincipal());
        Long productId = debtMatchDTO.getProductId();
        Map<String, Object> map = Maps.newHashMap();
        if ("1".equals(debtMatchDTO.getStatus())) {
            BusiProduct busiProduct = busiProductService.findOne(productId);
            BigDecimal productPrincipal = debtMatchDTO.getProductPrincipal();
            BigDecimal productInterest = productPrincipal.multiply(busiProduct.getYearRate().multiply(new BigDecimal(busiProduct.getCloseDay()))).divide(new BigDecimal(365), 2, 1);
            map.put("planStatus", AppConstants.PRODUCT_PLAN_STATUS.PRODUCT_STATUS_2);
            map.put("initStatus", AppConstants.PRODUCT_PLAN_STATUS.PRODUCT_STATUS_1);
            map.put("productId", productId);
            map.put("productInterest", productInterest);
            map.put("productPrincipal", productPrincipal);
            //v4.3
            map.put("productRank",debtMatchReqDTO.getProductRank());
            map.put("liabilitiesRate",debtMatchReqDTO.getLiabilitiesRate());
            String purchaseCondition = rankTransfer(debtMatchReqDTO.getProductRank());
            map.put("purchaseCondition",purchaseCondition);
            if ("1".equals(busiProduct.getGoingOnShelf())) {
                map.put("upLowFlag", AppConstants.ProductUpLowStatus.PRODUCT_UP);
            }
            int i = busiProductService.updateMainSub(map);
            if (i != 1) {
                throw new com.zdmoney.exception.BusinessException("更新理财计划" + productId + "失败");
            }
            List<DebtDetailDTO> debtDetailDTOList = debtMatchDTO.getDebtDetailDTOList();
            for (DebtDetailDTO debtDetailDTO : debtDetailDTOList) {
                if (AppConstants.FinancePlan.DEBT_TYPE1.equals(debtDetailDTO.getDebtType())) {
                    createSubject(debtDetailDTO.getInitSubjectNo(), false);
                }
                CustomerMainInfo mainInfo = customerMainInfoService.selectByIdNum(debtDetailDTO.getBorrowerIdNum(),AppConstants.USER_TYPE_NORMAL);
                BusiDebtDetailVO busiDebtDetail = new BusiDebtDetailVO();
                CopyUtil.copyProperties(busiDebtDetail, debtDetailDTO);
                busiDebtDetail.setBorrowerName(mainInfo.getCmRealName());
                busiDebtDetail.setBorrowerNumber(mainInfo.getCmNumber());
                busiDebtDetail.setIsSpecialTransfer(debtDetailDTO.getIsTransfer());
                busiDebtDetail.setInitPay(debtDetailDTO.getPaymentManner());
                busiDebtDetail.setProductId(debtDetailDTO.getMainProductId());
                busiDebtDetail.setStatus(AppConstants.FinancePlan.DEBT_STATUS_1);
                try{
                    debtDetailMapper.insertDebtDetail(busiDebtDetail);
                }catch (Exception e){
                    throw new com.zdmoney.exception.BusinessException(productId + "生成债权失败");
                }
            }
        } else {
            map.put("planStatus", AppConstants.PRODUCT_PLAN_STATUS.PRODUCT_STATUS_3);
            map.put("initStatus", AppConstants.PRODUCT_PLAN_STATUS.PRODUCT_STATUS_1);
            map.put("productId", productId);
            int i = busiProductService.updateMainSub(map);
            if (i != 1) {
                throw new com.zdmoney.exception.BusinessException(productId + "更新计划状态失败");
            }
        }
        return true;
    }

    /**
     * 产品星级转换购买人条件
     * @param productRank
     * @return
     */
    @Override
    public String  rankTransfer(Long productRank){
        String purchaseCondition = null;
        if (null != productRank) {
            //产品星标 10-五星 9-四星半 8-四星 7-三星半 6-三星
            if (Constants.PRODUCT_RANK_10 == productRank.intValue()) {
                purchaseCondition = Constants.PURCHASE_CONDITION_10;
            } else if (Constants.PRODUCT_RANK_9 == productRank.intValue()) {
                purchaseCondition = Constants.PURCHASE_CONDITION_9;
            } else if (Constants.PRODUCT_RANK_8 == productRank.intValue()) {
                purchaseCondition = Constants.PURCHASE_CONDITION_8;
            } else if (Constants.PRODUCT_RANK_7 == productRank.intValue()) {
                purchaseCondition = Constants.PURCHASE_CONDITION_7;
            } else if (Constants.PRODUCT_RANK_6 == productRank.intValue()) {
                purchaseCondition = Constants.PURCHASE_CONDITION_6;
            }
        }
        return purchaseCondition;
    }
    @Override
    @Transactional
    public ResultDto sendRevokeProduct(String productCode) {
        log.info("解散理财计划产品编号: " + productCode);
        if (org.apache.commons.lang3.StringUtils.isBlank(productCode)) {
            throw new BusinessException("产品编号不能为空！");
        }
        BusiProduct busiProduct = busiProductService.findOne(Long.valueOf(productCode));
        if (busiProduct == null) {
            throw new BusinessException("该产品不存在！");
        }
        AssetsResultDto resultDto = null;
        try {
            Boolean flag = busiProduct.getPlanStatus().equals(AppConstants.PRODUCT_PLAN_STATUS.PRODUCT_STATUS_2) ||
                    busiProduct.getPlanStatus().equals(AppConstants.PRODUCT_PLAN_STATUS.PRODUCT_STATUS_8);
            if (!flag) {
                throw new BusinessException("仅【已匹配】【未满标-待撮合】状态可操作解散!");
            }
            Map<String, Object> paramsMap = new HashMap<>();
            paramsMap.put("productId", productCode);
            List<BusiOrder> orderList = busiOrderService.queryFinancePlanOrderInfo(paramsMap);

            for (BusiOrder order : orderList) {
                CustomerMainInfo mainInfo = customerMainInfoService.findOneByCustomerId(order.getCustomerId());

                FinancePlanCancelDto financePlanCancelDto = new FinancePlanCancelDto();
                financePlanCancelDto.setAccountNo(mainInfo.getCmNumber());
                financePlanCancelDto.setCashAmount(order.getCashAmount());
                financePlanCancelDto.setOriginPlanOrderNo(order.getOrderId());
                financePlanCancelDto.setTransNo(SerialNoGeneratorService.generateDismissSerialNo(order.getCustomerId()));

                Map<String, String> param = Maps.newTreeMap();
                param.put("orderNo", order.getId().toString());
                BusiOrderIntegral busiOrderIntegral = busiOrderIntegralService.selectByCondition(param);

                if (busiOrderIntegral == null) {
                    throw new com.zdmoney.exception.BusinessException("没有订单为" + order.getId() + "相关信息");
                }
                financePlanCancelDto.setSerialNo(busiOrderIntegral.getAccountSeriNo());
                if (busiOrderIntegral != null) {
                    // 退积分
                    if (StringUtils.isNotBlank(busiOrderIntegral.getIntegralAmount()) && !"0".equals(busiOrderIntegral.getIntegralAmount())) {
                        IntegralOrderRefundDto integralRefundDto = new IntegralOrderRefundDto();
                        integralRefundDto.setChannelOrderNo(String.valueOf(order.getId()));
                        integralRefundDto.setProductSerialNo(productCode);
                        integralRefundDto.setOrderNo(busiOrderIntegral.getIntegralOrderNo());
                        financePlanCancelDto.setIntegralRefundDto(integralRefundDto);
                        log.info("解散理财计划-->退积分: " + GsonUtils.toJson(integralRefundDto));
                    }
                    // 退红包
                    if (!StringUtils.isEmpty(busiOrderIntegral.getCouponAmount())) {
                        if (!"0".equals(busiOrderIntegral.getCouponAmount()) && !StringUtils.isEmpty(busiOrderIntegral.getCouponOrderNo())) {
                            AccountRefundCollectDto.CouponDto couponDto = new AccountRefundCollectDto.CouponDto();
                            couponDto.setNo(busiOrderIntegral.getCouponId());
                            couponDto.setOrderNo(order.getOrderId());
                            couponDto.setSerialNo(busiOrderIntegral.getCouponOrderNo());
                            financePlanCancelDto.setCouponDto(couponDto);
                            log.info("解散理财计划-->退红包: " + GsonUtils.toJson(couponDto));
                        }
                    }
                    //退加息券
                    if (busiOrderIntegral != null && StringUtils.isNotBlank(busiOrderIntegral.getVoucherSerialNo()) &&
                            StringUtils.isBlank(busiOrderIntegral.getVoucherRefundSerialNo())) {
                        VoucherDto voucherDto = new VoucherDto();
                        voucherDto.setNo(busiOrderIntegral.getVoucherId());
                        voucherDto.setOrderNo(order.getOrderId());
                        voucherDto.setSerialNo(busiOrderIntegral.getVoucherSerialNo());
                        voucherDto.setAccountNo(mainInfo.getCmNumber());
                        financePlanCancelDto.setVoucherDto(voucherDto);
                        log.info("解散理财计划-->退加息券: " + GsonUtils.toJson(voucherDto));
                    }

                    // 退预约券
                    if (StringUtils.isNotBlank(busiOrderIntegral.getAppointmentSerialNo()) &&
                            StringUtils.isBlank(busiOrderIntegral.getAppointmentRefundSerialNo())) {
                        AppointmentDto appointmentDto = new AppointmentDto();
                        appointmentDto.setNo(busiOrderIntegral.getAppointmentId());
                        appointmentDto.setOrderNo(order.getOrderId());
                        appointmentDto.setSerialNo(busiOrderIntegral.getAppointmentSerialNo());
                        appointmentDto.setAccountNo(mainInfo.getCmNumber());
                        financePlanCancelDto.setAppointmentDto(appointmentDto);
                        log.info("解散理财计划-->退预约券: " + GsonUtils.toJson(appointmentDto));
                    }
                }

                com.zdmoney.integral.api.common.dto.ResultDto<FinancePlanCancelResultDto> resDto = bidFacadeService.financePlanCancel(financePlanCancelDto);
                log.info("解散理财计划-->financePlanCancel: " + resDto.toString());
                if (resDto.isSuccess()) {
                    FinancePlanCancelResultDto dto = resDto.getData();
                    BusiOrderIntegral bi = new BusiOrderIntegral();
                    bi.setOrderNo(order.getId());
                    bi.setCouponProductSerialNo(dto.getCouponSerialNo());
                    bi.setIntegralProductSerialNo(dto.getIntegralSerialNo());
                    String appointSerialNo = (dto.getAppointmentOprResDto() != null) ? dto.getAppointmentOprResDto().getSerialNo() : null;
                    bi.setAppointmentRefundSerialNo(appointSerialNo);
                    String voucherSerialNo = (dto.getVoucherOprResDto() != null) ? dto.getVoucherOprResDto().getSerialNo() : null;
                    bi.setVoucherRefundSerialNo(voucherSerialNo);

                    int num = busiOrderIntegralMapper.updateByCondition(bi);
                    if (num < 1) {
                        throw new com.zdmoney.exception.BusinessException("更新消费流水失败");
                    }
                }
            }
            MatchRevokeReqDto matchRevoke = new MatchRevokeReqDto();
            matchRevoke.setProductCode(productCode);
            matchRevoke.setPartnerNo(AppConstants.PARTNER_NO);
            // 解散理财计划产品
            resultDto = lcbFinancialPlanFacadeService.sendRevokeProduct(matchRevoke);
            log.info("解散理财计划-->sendRevokeProduct: " + resultDto.toString());
            if (!resultDto.isSuccess()) {
                return new ResultDto("解散理财计划产品失败:" + resultDto.getMsg(), false);
            }
            return new ResultDto("解散理财计划产品成功", true);
        } catch (Exception e) {
            log.error("解散理财计划产品失败 :", e);
            return new ResultDto("解散理财计划产品失败:" + e.getMessage(), false);
        }
    }

    @Override
    public ResultDto matchResultOrder(BusiMatchResultInfo matchResultDto) {
        log.info("修复撮合结果参数:" + JsonUtils.toJson(matchResultDto));
        boolean isRepair = true;//修复流程调用
        return matchResultOrderPurchase(matchResultDto,isRepair);
    }


    @Override
    public ResultDto matchResultOrderTask() {

        Map<String,Object> resultInfoMap=Maps.newHashMap();
        //获取待撮合状态的撮合批次信息
        resultInfoMap.put("status", AppConstants.BUSI_MATCH_RESULT_INFO_STATUS.NON_MATCH);
        List<BusiMatchResultInfo> matchResultInfos = busiMatchResultInfoMapper.selectBusiMatchUnionByMap(resultInfoMap);
        log.info("撮合定时任务开始,matchResultInfos值：{}",JSON.toJSONString(matchResultInfos));

        BusiFinancePlanService busiFinancePlanService =ContextLoader.getCurrentWebApplicationContext().getBean(BusiFinancePlanService.class);
        //更改撮合批次状态
        busiFinancePlanService.updateMatchResultInfoStatusByInitStatus(matchResultInfos,AppConstants.BUSI_MATCH_RESULT_INFO_STATUS.MATCHMAKING,AppConstants.BUSI_MATCH_RESULT_INFO_STATUS.NON_MATCH);

        for (BusiMatchResultInfo matchResultInfo:matchResultInfos){
            matchResultInfo.setBusiMatchSucInfos(busiMatchSucInfoMapper.selectByMasterId(matchResultInfo.getId()));
            matchResultInfo.setBusiAbnorMatchSucInfos(busiAbnorMatchSucInfoMapper.selectByMasterId(matchResultInfo.getId()));
            log.info("撮合任务开始撮合批次ID：{}",matchResultInfo.getId());
            boolean isRepair = false;//定时任务调用，不是修复流程调用
            busiFinancePlanService.matchResultOrderPurchase(matchResultInfo,isRepair);
        }

        log.info("撮合定时任务结束");
        return new ResultDto("撮合定时任务结束!", true);
    }

    /**
     * @Author: weiNian
     * @param matchResultInfos
     * @Description: 更改撮合批次的数据状态
     * @Date: 2018/6/8 9:36
     */
    @Transactional
    public void  updateMatchResultInfoStatusByInitStatus(List<BusiMatchResultInfo> matchResultInfos ,String status, String initStatus){
        if (matchResultInfos != null && matchResultInfos.size()>0 ){
            //将正在下单的撮合批次状态持久到数据库
            Map<String,Object> resultInfoMap=Maps.newHashMap();
            resultInfoMap.put("status", status);
            resultInfoMap.put("initStatus", initStatus);
            List<Long> matchResultInfosIdList = new ArrayList<>();
            for (BusiMatchResultInfo busiMatchResultInfo: matchResultInfos){
                matchResultInfosIdList.add(busiMatchResultInfo.getId());
            }
            resultInfoMap.put("matchResultInfosIdList", matchResultInfosIdList);
            int num = busiMatchResultInfoMapper.updateStatusByInitStatus(resultInfoMap);
            if (matchResultInfos.size() != num){
                throw  new BusinessException("本次撮合任务包含已被修改撮合批次数据："+ JSON.toJSONString(resultInfoMap));
            }
        }

    }


    /**
     * 根据撮合批次信息下单
     * @param matchResultInfo  撮合批次信息
     * @return
     */
    @Async("matchJobThreadExecutor")
    public ResultDto matchResultOrderPurchase(BusiMatchResultInfo matchResultInfo,boolean isRepair) {
        Long productId = matchResultInfo.getProductId();
        try {
            /*
            * 校验本批次撮合详细数量 与实际从标的接收到的数量是否一致。
            * 由于BusiMatchResultInfo.matchSucSize为新增字段，为兼容历史撮合批次数据matchSucSize为0，不做判断。
            * 修复流程也不用校验。
            * */
            if (!isRepair && matchResultInfo.getMatchSucSize()!=0 && matchResultInfo.getMatchSucSize()!= matchResultInfo.getBusiMatchSucInfos().size()){
               //有可能撮合数据正在传输中，导致撮合数量不一致。故将撮合数据状态恢复到待撮合状态
                List<BusiMatchResultInfo> busiMatchResultInfos = new ArrayList<BusiMatchResultInfo>();
                busiMatchResultInfos.add(matchResultInfo);
                updateMatchResultInfoStatusByInitStatus(busiMatchResultInfos,AppConstants.BUSI_MATCH_RESULT_INFO_STATUS.NON_MATCH,AppConstants.BUSI_MATCH_RESULT_INFO_STATUS.MATCHMAKING);

                log.error("撮合批次ID{}，撮合结果失败:撮合详细数量为{}，数据库查到记录为{}",matchResultInfo.getId(),matchResultInfo.getMatchSucSize(),matchResultInfo.getBusiMatchSucInfos().size());
                throw new BusinessException("撮合定时任务失败：撮合批次ID"+matchResultInfo.getId()+"，撮合详细数量与实际接收到数量不一致！");
            }


            BusiProduct busiProduct = busiProductService.findOne(productId);
            log.info("理财计划产品:" + JsonUtils.toJson(busiProduct));
            Boolean newLend=false;//子撮合债权是否有新出借
            List<BusiMatchSucInfo> busiMatchSucInfoList = matchResultInfo.getBusiMatchSucInfos();
            for (BusiMatchSucInfo busiMatchSucInfo : busiMatchSucInfoList) {
                if (AppConstants.FinancePlan.FUND_TYPE0.equals(busiMatchSucInfo.getCapitalType())) {
                    newLend = true;
                    break;
                }
            }

            Map<String, Object> productSubMap = beforeOrderSub(busiProduct, busiMatchSucInfoList, matchResultInfo.getId());
            saveOrderSub(matchResultInfo, busiProduct, productSubMap,newLend);
            updatePlan(matchResultInfo,busiProduct,newLend);
        } catch (Exception e) {
            log.error("产品ID："+productId+"撮合结果失败:" + e.getMessage(), e);
            MailUtil.sendMail("撮合调用失败,产品ID：", productId + "\n异常原因:" + e);
            return new ResultDto("产品ID："+productId+"撮合结果失败:" + e.getMessage(), false);
        }
        return new ResultDto("撮合结果下单成功!", true);
    }


    public ResultDto<MatchSpecialResultDto> matchSpecialResultOrder(MatchSpecialReqDto matchResultDto){
        log.info("兜底撮合结果参数:" + JsonUtils.toJson(matchResultDto));
        MatchSpecialResultDto dto = new MatchSpecialResultDto();

        // 查询特殊理财人
        String cmNumber = this.gainDefaultSpecialLender();
        CustomerMainInfo mainInfo = customerMainInfoService.findOneByCmNumber(cmNumber);
        try {
            //下单
            OrderVo vo = new OrderVo();
            vo.setCustomerId(mainInfo.getId());
            vo.setProductId(matchResultDto.getProductId());
            vo.setHoldType(OrderConstants.OrderHoldType.HOLD_SPECIAL);
            vo.setOrderAmt(matchResultDto.getSuperfluousAmount());

            BigDecimal balance = accountOverview520003Service.getAccountBalance(mainInfo);
            if (balance.compareTo(matchResultDto.getSuperfluousAmount()) < 0) {
                return ResultDto.FAIL("特殊理财人账户余额不足");
            }
            BusiOrderSub orderSub = orderService.order(vo);
            if(orderSub == null){
                throw  new BusinessException("特殊理财人兜底下主单失败,");
            }
            //支付
            Result result = tradeService.pay(mainInfo.getId(),orderSub.getId(),null,null,null,null,false,null);
            if(!result.getSuccess()){
                throw  new BusinessException("特殊理财人兜底支付主单失败,失败原因："+result.getMessage());
            }


            //根据产品id获取订单最大的结息日
            Date interestEndDate = null;
            BusiOrder busiOrder = busiOrderMapper.getMaxInterestEndDate(matchResultDto.getProductId());
            if (busiOrder!=null){
                interestEndDate = busiOrder.getInterestEndDate();
                log.info("产品id获取复投订单最大的结息日:"+DateUtil.dateToString(interestEndDate));
            }
            if(SuperfluousType.REPEAT.equals(matchResultDto.getSuperfluousType())){
                // 更新订单起息日结息日，状态，主子
                BusiProductSub productSub = busiProductSubMapper.selectByPrimaryKey(matchResultDto.getProductId());
                SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.YMDSTR);
                Date date = new Date();
                Date nowDate = sdf.parse(sdf.format(new Date()));
                // 更新主单
                BusiOrder order = new BusiOrder();
                order.setId(orderSub.getId());
                order.setStatus(AppConstants.BusiOrderStatus.BUSIORDER_STATUS_17);
                order.setInterestStartDate(nowDate);
                order.setInterestEndDate(interestEndDate);
                order.setModifyDate(date);
                busiOrderService.updateByPrimaryKeySelective(order);

                // 更新主单备份数据
                BusiOrderSub orderSubTemp = new BusiOrderSub();
                orderSubTemp.setId(orderSub.getId());
                orderSubTemp.setStatus(AppConstants.BusiOrderStatus.BUSIORDER_STATUS_17);
                orderSubTemp.setInterestStartDate(nowDate);
                orderSubTemp.setInterestEndDate(interestEndDate);
                order.setModifyDate(date);
                busiOrderSubMapper.updateByPrimaryKeySelective(orderSubTemp);
            }
            List<MatchSpecialDto> list = Lists.newArrayList();
            Map queryMap = Maps.newHashMap();
            queryMap.put("orderId",orderSub.getId());
            queryMap.put("productId",orderSub.getProductId());
            queryMap.put("fundType", AppConstants.FinancePlan.FUND_TYPE0);
            queryMap.put("status", AppConstants.FinancePlan.FUND_STATUS_1);
            List<BusiFundDetail> fundList = fundDetailMapper.selectByCondition(queryMap);
            if(CollectionUtils.isEmpty(fundList)){
                throw  new BusinessException("资金编号不存在");
            }
            String code = fundList.get(0).getId().toString();
            for(MatchSucResult msr :matchResultDto.getWaitIngOutResultList()){
                MatchSpecialDto waitOutReqDto = new MatchSpecialDto();
                waitOutReqDto.setCapitalCode(code);
                waitOutReqDto.setCapitalAmount(orderSub.getOrderAmt());
                waitOutReqDto.setCustomerCode(mainInfo.getCmNumber());
                waitOutReqDto.setMatchOrderCode(msr.getMatchOrderCode());
                waitOutReqDto.setOrderNo(orderSub.getOrderId());
                list.add(waitOutReqDto);
            }
            dto.setSuperfluousAmount(matchResultDto.getSuperfluousAmount());
            dto.setProductId(matchResultDto.getProductId());
            dto.setBatchNo(matchResultDto.getBatchNo());
            dto.setResultList(list);

            // 更新资金状态
            Map<String,Object> map = new HashMap<>();
            map.put("status", AppConstants.FinancePlan.FUND_STATUS_2);
            map.put("orderId", orderSub.getId());
            map.put("batchNo", matchResultDto.getBatchNo());
            map.put("fundType", AppConstants.FinancePlan.FUND_TYPE4);
            fundDetailMapper.updateByMap(map);
        } catch (Exception e) {
            log.error("特殊理财人兜底撮合下主单失败,理财计划ID:【{}】",matchResultDto.getProductId(),e);
            MailUtil.sendMail("特殊理财人兜底撮合下主单失败", "理财计划ID" + matchResultDto.getProductId() + ",失败原因 ：" + e.getMessage());
            return ResultDto.FAIL(e.getMessage());
        }

        return new ResultDto(dto);
    }

    /**
     * 撮合下单前操作，加工撮合订单对应的理财子产品信息对象
     * @param busiProduct 理财计划产品
     * @param matchSucInfos BusiMatchSucInfo集合
     * @param masterId 撮合批次ID
     * @return
     */
    @Transactional
    public Map<String, Object> beforeOrderSub(BusiProduct busiProduct, List<BusiMatchSucInfo>  matchSucInfos,long masterId) {
        Map<String, Object> subjectMap = Maps.newHashMap();
        for (BusiMatchSucInfo matchResult : matchSucInfos) {
            String subjectNo = matchResult.getSubjectNo();
            Object o = subjectMap.get(subjectNo);
            if(o ==null){
                List<BusiMatchSucInfo> subMatchList=new ArrayList<>();
                subMatchList.add(matchResult);
                subjectMap.put(subjectNo,subMatchList);
            }else{
                List<BusiMatchSucInfo> subMatchList=(List<BusiMatchSucInfo>)o;
                subMatchList.add(matchResult);
            }
        }
        Map<String, Object> productSubMap = Maps.newHashMap();
        Set<String> subjectSet = subjectMap.keySet();
        for (String subjectNo : subjectSet) {
            List<BusiMatchSucInfo> subMatchList= (List<BusiMatchSucInfo>) subjectMap.get(subjectNo);
            boolean createFlag=false;//资金类型是否为新出借类型
            boolean debtFlag=false;//是否为新标的
            //判断该撮合是否包含有新标的、资金类型是否为新出借类型
            for(BusiMatchSucInfo matchResult:subMatchList){
                if(AppConstants.FinancePlan.DEBT_TYPE1.equals(matchResult.getDebtType())){
                    debtFlag=true;
                    String capitalType = matchResult.getCapitalType();
                    if(!AppConstants.FinancePlan.FUND_TYPE0.equals(capitalType)){
                        createFlag=true;
                        break;
                    }
                }
            }
            try {
                BusiProductSub busiProductSub = createSubProduct(createFlag,debtFlag,subjectNo,busiProduct);
                productSubMap.put(subjectNo, busiProductSub);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                Map<String,Object> sucInfoMap=Maps.newHashMap();
                sucInfoMap.put("masterId",masterId);
                sucInfoMap.put("operStatus","2");
                busiMatchSucInfoMapper.updateByMap(sucInfoMap);
                MailUtil.sendMail("创建子产品或标的发生异常", "标的编号:" + subjectNo + "，异常原因:" + e);
                throw new BusinessException("创建子产品或标的发生异常！标的编号:" + subjectNo);
            }
        }
        return productSubMap;
    }

    /**
     *创建理财子产品信息
     * @param createFlag 资金类型是否为新出借类型
     * @param debtFlag 是否为新标的
     * @param subjectNo 撮合订单ID
     * @param busiProduct  理财计划产品
     * @return
     */
    @Override
    public BusiProductSub createSubProduct(Boolean createFlag,Boolean debtFlag,String subjectNo, BusiProduct busiProduct) {
        BusiProductSub busiProductSub = new BusiProductSub();
        BusiProductContract productContract = null;
        Example example = new Example(BusiProductContract.class);
        example.createCriteria().andEqualTo("subjectNo", subjectNo);
        List<BusiProductContract> busiProductContracts = busiProductContractMapper.selectByExample(example);
        if (!CollectionUtils.isEmpty(busiProductContracts)) {
            productContract = busiProductContracts.get(0);
            log.info("标的参数:" + JsonUtils.toJson(productContract));
            //判断标的还款类型
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
            busiProductSub.setRepayType(repayType);
            busiProductSub.setInterestStartDate(productContract.getInterestStartDate());
            busiProductSub.setInterestEndDate(productContract.getInterestEndDate());
            busiProductSub.setSaleStartDate(productContract.getSaleStartDate());
            busiProductSub.setSaleEndDate(productContract.getSaleEndDate());
            busiProductSub.setYearRate(productContract.getYearRate());
        }
        if (createFlag && (productContract == null)) {//资金类型是新出借类型
            productContract = createSubject(subjectNo, true);
            if (productContract == null) {
                throw new BusinessException(subjectNo + "标的不存在！");
            }
            log.info("标的参数:" + JsonUtils.toJson(productContract));
        }
        if(debtFlag){
            busiProductSub=tradeService.bidBuildProduct(productContract,busiProduct);
        }
        return busiProductSub;
    }


    /**
     * 下子订单并更新资金信息
     * @param matchResultDto  撮合批次信息
     * @param busiProduct  理财计划产品
     * @param productSubMap 理财计划子产品
     * @param newLend  资金是否为新出借
     */
    private void saveOrderSub(BusiMatchResultInfo matchResultDto, BusiProduct busiProduct, Map<String, Object> productSubMap,boolean newLend) {
        List<BusiMatchSucInfo> matchSucResults=matchResultDto.getBusiMatchSucInfos(); //子撮合订单集合
        List<BusiAbnorMatchSucInfo> abnorMatchSucInfos = matchResultDto.getBusiAbnorMatchSucInfos();//债券匹配异常结果集
        Map<String, BusiFundDetail> fundMap = Maps.newHashMap();//子撮合订单 对应的 资金详情
        Map<Long, BusiOrderSub> orderMap = Maps.newHashMap();//撮合子订单对应的订单详情集合
        Map<Long, CustomerMainInfo> mainInfoMap = Maps.newHashMap();
        BusiFinancePlanService busiFinancePlanService =ContextLoader.getCurrentWebApplicationContext().getBean(BusiFinancePlanService.class);

        Map<Long, List<BusiMatchSucInfo>> matchMap = Maps.newHashMap();

        //债券异常
        Map<Long, List<BusiAbnorMatchSucInfo>> abnormalMatchMap = Maps.newHashMap();

        //撮合前数据预加工
        for (BusiMatchSucInfo matchSucResult : matchSucResults) {
            prepareData(matchSucResult,orderMap,mainInfoMap,fundMap,matchMap);
        }

        if (abnorMatchSucInfos != null) {
            for (BusiAbnorMatchSucInfo abnorMatchSucInfo : abnorMatchSucInfos) {
                prepareAbnorDate(abnorMatchSucInfo, abnormalMatchMap);
            }
        }
        Long productId = busiProduct.getId();
        log.info(productId + ":newLend:" + newLend);

        //修改主订单起结息日
        Map<String, Object> map = Maps.newTreeMap();
        map.put("productId", productId);
        List<BusiOrder> busiOrders = busiOrderMapper.selectOrdersByProperties(map);
        for (BusiOrder busiOrder : busiOrders){
            if(newLend&&StringUtil.isEmpty(busiOrder.getInterestStartDate())){
                Date interestStartDate = DateUtil.getDateBeginTime(DateUtil.addDays(new Date(), 1));
                Date interestEndDate = DateUtil.getDateBeginTime(DateUtil.addDays(interestStartDate, busiProduct.getCloseDay()));
                Map<String, Object> mainSub = Maps.newHashMap();
                mainSub.put("interestStartDate", interestStartDate);
                mainSub.put("interestEndDate", interestEndDate);
                mainSub.put("id", busiOrder.getId());
                busiOrderService.updateMainSub(mainSub);
            }
        }

        Set<Long> orderIdSet = orderMap.keySet();
        for (Long orderId : orderIdSet) {
            //根据主订单维度并发投标
            busiFinancePlanService.tenderByMainOrder(orderId,busiProduct,newLend, fundMap , matchMap, orderMap,mainInfoMap,matchResultDto,productSubMap,abnormalMatchMap);
        }

    }

    /**
     * @Author: weiNian
     * @param orderId  主订单编号
     * @param busiProduct 理财产品
     * @param newLend 是否为新出借
     * @param fundMap 资金详情集合
     * @param matchMap 撮合详细集合
     * @param orderMap  订单集合
     * @param mainInfoMap  账户信息
     * @param matchResultDto 撮合批次信息
     * @param productSubMap 子产品集合
     * @Description:
     * @Date: 2018/7/9 11:42
     */
//    @Async("matchMainOrderThreadExecutor")
    public void tenderByMainOrder(Long orderId,BusiProduct busiProduct,boolean newLend,Map<String, BusiFundDetail> fundMap ,
                                  Map<Long, List<BusiMatchSucInfo>> matchMap,Map<Long, BusiOrderSub> orderMap,Map<Long, CustomerMainInfo> mainInfoMap,
                                  BusiMatchResultInfo matchResultDto,Map<String, Object> productSubMap, Map<Long, List<BusiAbnorMatchSucInfo>> abnormatchMap){

        List<BusiOrderSub> orderSubList = new ArrayList<>();
        List<BusiOrderSub> transferSubList = new ArrayList<>();
        List<BusiDebtTransfer> debtTransferList = new ArrayList<>();
        Map<String, BusiOrderSub> transferMap = Maps.newHashMap();

        List<BusiMatchSucInfo> sucResults = matchMap.get(orderId);
        BusiOrderSub busiOrder = orderMap.get(orderId);
        log.info("主订单：" + JSONObject.fromObject(busiOrder));
        CustomerMainInfo mainInfo = mainInfoMap.get(orderId);
        BigDecimal welfareAmt = busiOrder.getOrderAmt().subtract(busiOrder.getCashAmount());
        log.info(busiOrder.getOrderId() + "welfareAmt:" + welfareAmt);
        if (newLend&& StringUtil.isEmpty(busiOrder.getSendSerialNo())&&welfareAmt.compareTo(BigDecimal.ZERO)>0) {
            try{
                //派送佣金。如果账户余额不够支付订单现金，由公司支付（红包）
                boolean commission = sendCommission(busiOrder, mainInfo.getCmNumber(), sucResults, welfareAmt);
                if (!commission) {
                    return;
                }else {
                    Map<String, Object> mainSub = Maps.newHashMap();
                    mainSub.put("sendSerialNo", busiOrder.getSendSerialNo());
                    mainSub.put("id", orderId);
                    busiOrderService.updateMainSub(mainSub);
                }
            }catch (Exception e){
                log.error(e.getMessage(), e);
                MailUtil.sendMail("更新订单：" + orderId+"的相关信息发生异常","异常原因:" + e);
            }
        }

        int fail = 0;//记录投标失败个数
        for (BusiMatchSucInfo matchSucResult : sucResults) {

            BusiProductSub busiProductSub = (BusiProductSub) productSubMap.get(matchSucResult.getSubjectNo());
            log.info("子产品：" + JSONObject.fromObject(busiProductSub));
            //生成当前子撮合的订单消息
            BusiOrderSub orderSub = generOrder(matchSucResult, busiOrder, mainInfo, busiProductSub);
            log.info("生成子订单信息：" + JSONObject.fromObject(orderSub));
            //投标
            boolean tender = tender(matchSucResult, orderSub, busiProductSub, mainInfo);
            log.info("撮合下单投标，债权编号：{} 投标结果：{} matchSucResult记录：{}  busiProductSub记录：{}"
                    ,matchSucResult.getFinanceId(),tender,JSON.toJSONString(matchSucResult),JSON.toJSONString(busiProductSub));
            Map<String,Object> sucInfoMap=Maps.newHashMap();
            if(!tender){
                fail++;
                sucInfoMap.put("financeId",matchSucResult.getFinanceId());
                sucInfoMap.put("operStatus","2");
                busiMatchSucInfoMapper.updateByMap(sucInfoMap);
            }else{
                sucInfoMap.put("financeId",matchSucResult.getFinanceId());
                sucInfoMap.put("operStatus","1");
                busiMatchSucInfoMapper.updateByMap(sucInfoMap);
                if (AppConstants.FinancePlan.DEBT_TYPE2.equals(matchSucResult.getDebtType())) {
                    String initOrderNum = matchSucResult.getInitOrderNum();
                    BigDecimal debtAmount = matchSucResult.getDebtWorth();
                    try {
                        //查询子单详情
                        BusiOrderSub initOrderSub = busiOrderSubMapper.queryBusiOrderSubInfo(initOrderNum);
                        Integer transferNum = matchSucResult.getTransferNum();
                        //转让
                        BusiDebtTransfer debtTransfer = tradeService.planTransfer(initOrderSub, orderSub, mainInfo, debtAmount, transferNum);
                        transferMap.put(debtTransfer.getTransferNo(),initOrderSub);
                        transferSubList.add(orderSub);
                        debtTransferList.add(debtTransfer);
                    } catch (Exception e) {
                        fail++;
                        log.error(initOrderNum+e.getMessage(), e);
                        MailUtil.sendMail("转让订单号:" + initOrderNum +"受让订单号"+orderSub.getOrderId()+"子订单转让交割发生异常",  "异常原因:" + e);
                        try {
                            sucInfoMap.put("financeId",matchSucResult.getFinanceId());
                            sucInfoMap.put("operStatus","2");
                            busiMatchSucInfoMapper.updateByMap(sucInfoMap);
                            tradeService.transferRefund(orderSub, busiProductSub);
                        } catch (Exception tr) {
                            log.error(e.getMessage(), e);
                            MailUtil.sendMail(orderSub.getOrderId() +"子订单退款发生异常",  "异常原因:" + e);
                        }
                    }
                } else {
                    orderSubList.add(orderSub);
                }
            }
        }

        updateFundDetail(orderId,matchResultDto,fundMap,sucResults,fail);

        if (abnormatchMap != null) {
            List<BusiAbnorMatchSucInfo> abnorSucInfos = abnormatchMap.get(orderId);
            if (abnorSucInfos != null) {
                abnormalDebtHandle(orderId, matchResultDto.getBatchNo(), fundMap, abnorSucInfos);
            }
        }
        if (!CollectionUtils.isEmpty(orderSubList)) {
            subjectService.notifyCommonProductSubject(orderSubList, mainInfoMap);//通知标的
        }
        if(!CollectionUtils.isEmpty(transferSubList)){
            subjectService.notifyTransferProductSubject(transferSubList, mainInfoMap, transferMap);//通知标的
            if (!debtTransferList.isEmpty()){
                for (BusiDebtTransfer debtTransfer : debtTransferList){
                    tradeService.notifyMessageSuccess(debtTransfer);
                }
            }
        }
    }

    private void updateFundDetail(Long orderId,BusiMatchResultInfo matchResultDto,Map<String, BusiFundDetail> fundMap,List<BusiMatchSucInfo> busiMatchSucInfoList,int fail){
        BigDecimal matchedAmt=BigDecimal.ZERO;
        String capitalCode="";
        int financeNum=0;
        for (BusiMatchSucInfo matchSucResult : busiMatchSucInfoList) {
            matchedAmt=matchedAmt.add(matchSucResult.getCapitalAmount());
            capitalCode=matchSucResult.getCapitalCode();
            financeNum = matchSucResult.getFinanceNum();
        }
        BusiFundDetail busiFundDetail = fundMap.get(capitalCode);
        BigDecimal matchingAmt=busiFundDetail.getMatchingAmt().subtract(matchedAmt);
        if(matchingAmt.compareTo(BigDecimal.ZERO)<0){
            matchingAmt=BigDecimal.ZERO;
        }
        try{
            Map<String, Object> fundParams = Maps.newHashMap();
            fundParams.put("id", capitalCode);
            if (fail == 0) {
                fundParams.put("matchDate", new Date());
                fundParams.put("matchingAmt", matchingAmt);
                Map<String,Object> detailMap=Maps.newHashMap();
                detailMap.put("batchNo",matchResultDto.getBatchNo());
                detailMap.put("operStatus", "1");
                detailMap.put("capitalCode",capitalCode);
                List<BusiMatchSucInfo> infoList = busiMatchSucInfoMapper.selectByMap(detailMap);
                if(CollectionUtils.isNotEmpty(infoList)&&infoList.size()==financeNum){
                    log.info("更新资金：" + orderId + "的状态");
                    fundParams.put("status", AppConstants.FinancePlan.FUND_STATUS_3);
                    fundParams.put("initStatus", AppConstants.FinancePlan.FUND_STATUS_2);
                    fundParams.put("matchingAmt", BigDecimal.ZERO);
                }
                fundDetailMapper.updateByMap(fundParams);
            }
        }catch (Exception e){
            log.error(e.getMessage(), e);
            MailUtil.sendMail("更新订单：" + orderId+"的相关信息发生异常","，异常原因:" + e);
        }
    }

    /**
     *
     * @param matchResultInfo 撮合批次信息
     * @param busiProduct 产品信息
     * @param newLend 本次撮合是否包含新出借
     */
    private void updatePlan(BusiMatchResultInfo matchResultInfo,BusiProduct busiProduct,boolean newLend){
        Long productId=busiProduct.getId();
        if (newLend&&StringUtil.isEmpty(busiProduct.getInterestStartDate())) {
            Date interestStartDate = DateUtil.getDateBeginTime(DateUtil.addDays(new Date(), 1));
            Date interestEndDate = DateUtil.getDateBeginTime(DateUtil.addDays(interestStartDate, busiProduct.getCloseDay()));
            Map<String, Object> mainSub = Maps.newHashMap();
            mainSub.put("interestStartDate", interestStartDate);
            mainSub.put("interestEndDate", interestEndDate);
            mainSub.put("productId", productId);
            mainSub.put("planStatus", AppConstants.PRODUCT_PLAN_STATUS.PRODUCT_STATUS_12);
            busiProductService.updateMainSub(mainSub);
        }
        // 更新债权详情状态
        Map<String, Object> map = new HashMap<>();
        map.put("productId", productId);
        map.put("matchDate", new Date());
        map.put("status", AppConstants.FinancePlan.DEBT_STATUS_3);
        map.put("initStatus", AppConstants.FinancePlan.DEBT_STATUS_1);
        debtDetailMapper.updateDebtDetail(map);

        matchResultInfo.setStatus(AppConstants.BUSI_MATCH_RESULT_INFO_STATUS.MATCH_END);
        busiMatchResultInfoMapper.updateByPrimaryKeySelective(matchResultInfo);
    }

    private void prepareAbnorDate(BusiAbnorMatchSucInfo abnorMatchSucResult,Map<Long, List<BusiAbnorMatchSucInfo>> abnormalMatchMap){
        String capitalCode = abnorMatchSucResult.getCapitalCode();
        BusiFundDetail fundDetail;
        fundDetail = fundDetailMapper.selectByPrimaryKey(Long.valueOf(capitalCode));
        Long orderId = fundDetail.getOrderId();
        List<BusiAbnorMatchSucInfo> matchResults = new ArrayList<>();
        matchResults.add(abnorMatchSucResult);
        abnormalMatchMap.put(orderId, matchResults);
    }


    /**
     * 撮合前数据预加工
     * @param matchSucResult 撮合子订单
     * @param orderMap  撮合子订单对应的订单详情集合
     * @param mainInfoMap  订单详情对应的客户信息集合
     * @param matchMap  以订单详情ID为key，相同订单ID的matchSucResult集合为value
     * @param fundMap  撮合子订单对应的资金详情集合
     *                 List<BusiAbnorMatchSucInfo> abnorMatchSucInfo,Map<Long, List<BusiAbnorMatchSucInfo>> abnormalMatchMap
     */
    private void prepareData(BusiMatchSucInfo matchSucResult,Map<Long, BusiOrderSub> orderMap,
                             Map<Long, CustomerMainInfo> mainInfoMap,Map<String, BusiFundDetail> fundMap,
                             Map<Long, List<BusiMatchSucInfo>> matchMap){

        String capitalCode = matchSucResult.getCapitalCode();
        BusiFundDetail fundDetail;
        if (fundMap.get(capitalCode) == null) {
            fundDetail = fundDetailMapper.selectByPrimaryKey(Long.valueOf(capitalCode));
            fundMap.put(capitalCode, fundDetail);
        } else {
            fundDetail = fundMap.get(capitalCode);
        }

        Long orderId = fundDetail.getOrderId();
        BusiOrderSub busiOrder;
        if (orderMap.get(orderId) == null) {
            busiOrder = busiOrderSubMapper.selectByPrimaryKey(orderId);
            orderMap.put(orderId, busiOrder);
                List<BusiMatchSucInfo> matchResults = new ArrayList<>();
                matchResults.add(matchSucResult);
                matchMap.put(orderId, matchResults);
        } else {
            busiOrder = orderMap.get(orderId);
                List<BusiMatchSucInfo> matchResults = matchMap.get(orderId);
                matchResults.add(matchSucResult);
        }

        if (mainInfoMap.get(orderId) == null) {
            CustomerMainInfo mainInfo = customerMainInfoService.findAuthCustomerById(busiOrder.getCustomerId());
            mainInfoMap.put(orderId, mainInfo);
        }
    }

    /**
     * 生成订单信息
     * @param matchSucResult 子撮合对象
     * @param busiOrder  子订单信息
     * @param mainInfo  客户信息
     * @param productSub 子产品信息
     * @return
     */
    private BusiOrderSub generOrder(BusiMatchSucInfo matchSucResult, BusiOrderSub busiOrder, CustomerMainInfo mainInfo, BusiProductSub productSub) {
        BusiOrderSub orderSub = new BusiOrderSub();
        Date date = new Date();
        CopyUtil.copyProperties(orderSub, busiOrder);
        String orderNumSub = SerialNumberGenerator.generatorOrderNum(OrderGenerateType.SUB_ORDER,mainInfo.getId());
        Date interestStartDate = productSub.getInterestStartDate();
        BigDecimal interest = BigDecimal.ZERO;
        //如果是退出转让子撮合
        if (AppConstants.FinancePlan.DEBT_TYPE2.equals(matchSucResult.getDebtType())) {
            interest = matchSucResult.getInterest();
            interestStartDate = DateUtil.getDateBeginTime(date);
        }
        //如果是新标的
        else{
            //撮合资金金额*子产品到期利息/子产品总额
            interest = matchSucResult.getCapitalAmount().multiply(productSub.getProductInterest()).divide(new BigDecimal(matchSucResult.getSubjectAmt()), 2, 1);
        }
        orderSub.setStatus(AppConstants.BusiOrderStatus.BUSIORDER_STATUS_1);
        orderSub.setOrderAmt(matchSucResult.getCapitalAmount());
        orderSub.setSubOrderStatus(AppConstants.ORDER_SUB_STATUS.ORDER_STATUS_1);
        orderSub.setCmNumber(mainInfo.getCmNumber());
        orderSub.setProductId(productSub.getId());
        orderSub.setProductName(productSub.getProductName());
        orderSub.setPrincipalinterest(matchSucResult.getCapitalAmount().add(interest));
        orderSub.setSubjectNo(matchSucResult.getSubjectNo());
        orderSub.setDebtNo(matchSucResult.getFinanceId());
        orderSub.setDebtType(matchSucResult.getDebtType());
        orderSub.setCashAmount(matchSucResult.getCapitalAmount());
        orderSub.setOrderTime(date);
        orderSub.setConfirmPaymentDate(date);
        orderSub.setParentId(busiOrder.getId());
        orderSub.setParentNo(busiOrder.getOrderId());
        orderSub.setProductType(5);
        orderSub.setOrderId(orderNumSub);
        orderSub.setDebtorNum(matchSucResult.getLoanCustomerNo());
        orderSub.setDebtorName(matchSucResult.getLoanCustomerName());
        orderSub.setRaiseRateIncome(BigDecimal.ZERO);
        orderSub.setHolderType("0"); // 默认普通用户

        orderSub.setInterestEndDate(productSub.getInterestEndDate());
        orderSub.setInterestStartDate(interestStartDate);
        if (AppConstants.FinancePlan.DEBT_TYPE1.equals(matchSucResult.getDebtType())) {
            orderSub.setProductId(productSub.getId());
        } else {
            orderSub.setProductId(0L);
        }
        orderSub.setPayType(productSub.getRepayType());
        orderSub.setSubOrderStatus(AppConstants.ORDER_SUB_STATUS.ORDER_STATUS_1);
        orderSub.setPlanId(Long.valueOf(matchSucResult.getProductCode()));
        orderSub.setBorrowerDate(matchSucResult.getBorrowerDate());
        orderSub.setCashAmount(matchSucResult.getCapitalAmount());
        orderSub.setIntegralAmount(BigDecimal.ZERO);
        orderSub.setCouponAmount(BigDecimal.ZERO);
        orderSub.setLoginId(mainInfo.getFuiouLoginId());
        return orderSub;
    }

    /**
     * 佣金派送
     * @param busiOrder  订单详情
     * @param cmNumber  客户编号
     * @param sucResults  撮合子订单
     * @param welfareAmt 佣金
     * @return
     */
    private boolean sendCommission(BusiOrderSub busiOrder, String cmNumber,List<BusiMatchSucInfo> sucResults,BigDecimal welfareAmt) {
        String orderNum = busiOrder.getOrderId();
        try {
            String recordNum = tradeService.financePlanSendCommission(orderNum, cmNumber, welfareAmt);
            busiOrder.setSendSerialNo(recordNum);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            Map<String,Object> exceptionMap= Maps.newHashMap();
            exceptionMap.put("productId",busiOrder.getProductId());
            exceptionMap.put("orderNum",busiOrder.getOrderId());
            exceptionMap.put("orderId",busiOrder.getId());
            exceptionMap.put("fundId",sucResults.get(0).getCapitalCode());
            for (BusiMatchSucInfo matchSucResult : sucResults) {
                exceptionMap.put("financeId",matchSucResult.getFinanceId());
                busiMatchExceptionMapper.saveMatchException(exceptionMap);
                Map<String,Object> sucInfoMap=Maps.newHashMap();
                sucInfoMap.put("financeId",matchSucResult.getFinanceId());
                sucInfoMap.put("operStatus","2");
                busiMatchSucInfoMapper.updateByMap(sucInfoMap);
            }
            MailUtil.sendMail("撮合佣金派送发生异常", "订单号:" + orderNum + "，异常原因:" + e);
            return false;
        }
        return true;
    }


    /**
     * 投标
     * @param matchSucResult 子撮合信息
     * @param orderSub  子撮合对应的订单信息
     * @param busiProductSub  子撮合对应的产品信息
     * @param mainInfo 客户信息
     * @return
     */
    private boolean tender(BusiMatchSucInfo matchSucResult, BusiOrderSub orderSub,BusiProductSub busiProductSub,CustomerMainInfo mainInfo) {
        TenderVo tenderVo = new TenderVo();
        tenderVo.setBidAmount(new BigDecimal(matchSucResult.getSubjectAmt()));
        tenderVo.setCustomerMainInfo(mainInfo);
        tenderVo.setDebtType(matchSucResult.getDebtType());
        tenderVo.setInitOrderNum(matchSucResult.getInitOrderNum());
        tenderVo.setYearRate(busiProductSub.getYearRate());
        tenderVo.setSaleEndDate(busiProductSub.getSaleEndDate());
        tenderVo.setSaleStartDate(busiProductSub.getSaleStartDate());
        tenderVo.setDebtWorth(matchSucResult.getDebtWorth());
        boolean insertFlag=false;
        try {
            busiOrderSubMapper.insert(orderSub);//生成子订单
            insertFlag=true;
            tradeService.tender(tenderVo, orderSub,busiProductSub);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            if(insertFlag){
                Random random = new Random(System.currentTimeMillis());
                String f = "f"+random.nextInt(10000);
                orderSub.setDebtNo(orderSub.getDebtNo()+f);
                orderSub.setStatus(AppConstants.BusiOrderStatus.BUSIORDER_STATUS_11);
                busiOrderSubMapper.updateByPrimaryKeySelective(orderSub);
                Map<String,Object> exceptionMap= Maps.newHashMap();
                exceptionMap.put("productId",orderSub.getPlanId());
                exceptionMap.put("orderNum",orderSub.getParentNo());
                exceptionMap.put("orderId",orderSub.getParentId());
                exceptionMap.put("fundId",matchSucResult.getCapitalCode());
                exceptionMap.put("financeId",matchSucResult.getFinanceId());
                busiMatchExceptionMapper.saveMatchException(exceptionMap);
            }
            MailUtil.sendMail("子订单投标发生异常", "债权编号:" + matchSucResult.getFinanceId() + "，异常原因:" + e);
            return false;
        }
        return true;
    }

    public BusiProductContract createSubject(String subjectNo,boolean flag) {
        if(!flag){
            Example example = new Example(BusiProductContract.class);
            example.createCriteria().andEqualTo("subjectNo", subjectNo);
            List<BusiProductContract> contracts = busiProductContractMapper.selectByExample(example);
            if(!CollectionUtils.isEmpty(contracts)){
                return contracts.get(0);
            }
        }
        SubjectInfoObtainDto obtainDto = new SubjectInfoObtainDto(subjectNo, AppConstants.APPLICATION_CONTEXT_NAME);
        AssetsResultDto<SubjectDto> resultDto = lcbSubjectFacadeService.getSubject(obtainDto);
        SubjectDto subject = resultDto.getData();
        String idNo = subject.getIdNo();
        if (idNo == null) {
            throw new com.zdmoney.exception.BusinessException("借款人身份证号不能为空");
        }
        idNo = idNo.trim();
        CustomerMainInfo mainInfo = customerMainInfoService.selectByIdNum(idNo,AppConstants.USER_TYPE_NORMAL);
        if (mainInfo == null) {
            throw new com.zdmoney.exception.BusinessException("没有身份证号" + idNo + "的用户信息");
        }

        BusiProductContract productContract = subjectToContract(subject);
        if(productContract.getSaleEndDate().compareTo(new Date())<0){
            throw new BusinessException("结售期已过不能再匹配或撮合:" + subjectNo);
        }
        productContract.setCmNumber(mainInfo.getCmNumber());
        productContract.setIsBuild("0");
        busiProductContractMapper.insertSelective(productContract);
        return  productContract;
    }

    private BusiProductContract subjectToContract(SubjectDto subject) {
        BusiProductContract productContract = new BusiProductContract();
        productContract.setSubjectNo(subject.getNo());
        productContract.setBankBranch(subject.getBankBranch());
        productContract.setBankCardNo(subject.getBankCardNo());
        productContract.setBankCardUser(subject.getBankCardUser());
        productContract.setBankName(subject.getBankName());
        productContract.setBorrowerName(subject.getBorrowerName());
        productContract.setBorrowerType(subject.getBorrowerType());
        productContract.setBorrowPurpose(subject.getBorrowPurpose());
        productContract.setTransferorName(subject.getTransferorName());
        productContract.setTransferoridNo(subject.getTransferorIdNo());
        productContract.setRepayType(subject.getRepayType());
        productContract.setIdNo(subject.getIdNo());
        productContract.setRepaymentTerms(subject.getBorrowTermNum());
        productContract.setLastExpire(subject.getLastExpire());
        if (subject.getProductTypeVal() == 1) {
            productContract.setSex(subject.getSex() + "");
            productContract.setMaritalStatus(subject.getMaritalStatus() + "");
            productContract.setBrithday(subject.getBirth());
            productContract.setCity(subject.getCity());
            productContract.setHasCar(subject.getHasCar() + "");
            productContract.setHasHourse(subject.getHasHourse() + "");
            productContract.setTradeInfo(subject.getTrade());
            productContract.setPostInfo(subject.getPost());
            productContract.setCompanyNature(subject.getCompanyNature() + "");
            productContract.setMonthIncome(subject.getMonthlyIncome());
            productContract.setCreditNums(subject.getCreditNums() + "");
            productContract.setHasCarLoan(subject.getHasCarLoan() + "");
            productContract.setHasHourseLoan(subject.getHasHourseLoan() + "");
            CompanyNature.getNameByCode(subject.getCompanyNature());
            String borrowerPhone = subject.getBorrowerPhone();
            productContract.setBorrowerPhone(borrowerPhone);
            if (borrowerPhone == null) {
                throw new com.zdmoney.exception.BusinessException("手机号不能为空");
            }
            CustomerMainInfo customerMainInfo = customerMainInfoService.findOneByPhone(borrowerPhone.trim());
            if (customerMainInfo == null) {
                throw new com.zdmoney.exception.BusinessException("没有手机号" + borrowerPhone + "的用户信息");
            }
        }
        productContract.setCreateDate(new Date());
        productContract.setStatus("0");
        productContract.setSubjectNo(subject.getNo());
        productContract.setIsPlan("1");
        productContract.setOrganizationName(subject.getSourceName());
        productContract.setOrganizationNo(subject.getSourceNo());
        productContract.setCollectAmount(new BigDecimal(subject.getCollectAmount().toString()));
        productContract.setInterestEndDate(subject.getInterestEndDate());
        productContract.setInterestStartDate(subject.getInterestStartDate());
        productContract.setSaleStartDate(subject.getSaleStartDate());
        productContract.setSaleEndDate(subject.getSaleEndDate());
        productContract.setYearRate(new BigDecimal(subject.getSaleRate()));
        //v4.3
        productContract.setProductRank(new BigDecimal(subject.getBorrowRate()));
        productContract.setLiabilitiesRate(subject.getIncomeDebtRatio());
        productContract.setBorrowUse(subject.getCollectBorrowPurpose());
        productContract.setBurrowIndustry(subject.getCollectTrade());
        productContract.setWorkNature(subject.getWorkNature());
        return productContract;
    }

    /**
     * 根据id查询债权信息
     *
     * @param id
     * @return
     */
    public BusiDebtDetailVO selectDebtDetailByPrimaryKey(Long id) {
        return debtDetailMapper.selectDebtDetailByPrimaryKey(id);
    }

    /**
     * 根据id查询子订单信息
     *
     * @param id
     * @return
     */
    public BusiOrderSub selectOrderSubByPrimaryKey(Long id) {
        return busiOrderSubMapper.selectByPrimaryKey(id);
    }

    @Override
    public int sendDebtInfo(List<DebtQueueDTO> debtQueueDTOList) {
        log.info("推送债权信息: " + GsonUtils.toJson(debtQueueDTOList));
        if (CollectionUtils.isEmpty(debtQueueDTOList)) return 0;
        MatchDebtReqDto debtReqDto = new MatchDebtReqDto();
        debtReqDto.setPartnerNo(AppConstants.PARTNER_NO);
        List<MatchDebtItemDto> itemDtos = new ArrayList<>();
        try {
            for (DebtQueueDTO queueDTO : debtQueueDTOList) {
                BusiOrderSub orderSub = busiOrderSubMapper.queryBusiOrderSubInfo(queueDTO.getOrderNo());
                if (orderSub == null) {
                    throw new BusinessException("订单" + queueDTO.getOrderNo() + "不存在！");
                }
                saveDebtQueue(orderSub);

                MatchDebtItemDto mdid = new MatchDebtItemDto();
                mdid.setProductCode(String.valueOf(orderSub.getProductId()));
                mdid.setOrderNo(queueDTO.getOrderNo());

                DebtType debtType;
                if ("1".equals(queueDTO.getDebtType())) {
                    debtType = DebtType.EXPIRE_ASSIGNMENT;
                } else {
                    debtType = DebtType.QUIT_ASSIGNMENT;
                }
                mdid.setDebtType(debtType);
                mdid.setFinanceId(orderSub.getDebtNo());
                mdid.setSubjectNo(orderSub.getSubjectNo());
                itemDtos.add(mdid);
            }
            debtReqDto.setItemDtos(itemDtos);
            AssetsResultDto resultDto = lcbFinancialPlanFacadeService.sendDebt(debtReqDto);
            log.info("推送债权: " + resultDto.toString());
            if (!resultDto.isSuccess()) {
                log.error("推送债权信息失败:" + resultDto.getMsg());
                return 0;
            }
        } catch (Exception e) {
            log.error("推送债权信息失败:" + e);
            return 0;
        }
        return 1;
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
    private BusiDebtTransfer createDebtTransfer(BusiOrderSub busiOrderSub, BusiOrderSub initOrderSub, BusiOrderSub originOrder, BigDecimal transferFee) throws com.zdmoney.exception.BusinessException {
        BusiDebtTransfer transfer = new BusiDebtTransfer();
        Date date = new Date();
        transfer.setTransferNo(buildTransferNo(initOrderSub.getCustomerId()));
        transfer.setTransferId(initOrderSub.getCustomerId());
        transfer.setInitOrderNo(initOrderSub.getOrderId());
        transfer.setPubDate(date);
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
        //存储最原始的订单号
        transfer.setOriginOrderNo(originOrder.getOrderId());
        transfer.setOriginProductId(originOrder.getProductId());
        transfer.setTradeDate(date);
        long leftDay = DateUtil.getBetweenDays(busiOrderSub.getInterestEndDate(), busiOrderSub.getInterestStartDate())+1;
        transfer.setLeftDay(Integer.valueOf(leftDay+""));
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

    /**
     * 特殊理财人转让
     *
     * @param specialTransferDebtList
     * @return
     */
    @Override
    public ResultDto specialTransfer(List<SpecialTransferDebtDTO> specialTransferDebtList) {
        log.info("特殊理财人转让: " + GsonUtils.toJson(specialTransferDebtList));
        if (CollectionUtils.isEmpty(specialTransferDebtList)) {
            return new ResultDto("请输入正确的参数信息", false);
        }

        MatchDebtReqDto debtReqDto = new MatchDebtReqDto();
        debtReqDto.setPartnerNo(AppConstants.PARTNER_NO);
        List<MatchDebtItemDto> itemDtos = new ArrayList<>();
        try {
            Map<Long, Object> msgMap = new HashMap<>();
            for (SpecialTransferDebtDTO std : specialTransferDebtList) {
                List<Long> orderIdList = new ArrayList<>();
                orderIdList.add(std.getMainOrderId());
                // 排除正在撮合中的订单
                long n = busiOrderSubMapper.queryFundByParentId(std.getMainOrderId());
                if (n > 0) {
                    msgMap.put(std.getMainOrderId(), "此订单有正在复投中的资金,不能转让");
                    continue;
                }
                List<BusiOrderSub> orderSubList = busiOrderSubMapper.queryOrderByParentId(std.getMainOrderId());
                if (CollectionUtils.isEmpty(orderSubList)) {
                    return new ResultDto("主订单号" + std.getMainOrderId() + "没有子单信息！", false);
                }
                for (BusiOrderSub orderSub : orderSubList) {
                    saveDebtQueue(orderSub);

                    MatchDebtItemDto mdid = new MatchDebtItemDto();
                    mdid.setProductCode(String.valueOf(orderSub.getProductId()));
                    mdid.setOrderNo(orderSub.getOrderId());
                    mdid.setDebtType(DebtType.QUIT_ASSIGNMENT);
                    mdid.setFinanceId(orderSub.getDebtNo());
                    mdid.setSubjectNo(orderSub.getSubjectNo());
                    itemDtos.add(mdid);
                }
                debtReqDto.setItemDtos(itemDtos);
                AssetsResultDto resultDto = lcbFinancialPlanFacadeService.sendDebt(debtReqDto);
                if (!resultDto.isSuccess()) {
                    log.info("lcbFinancialPlanFacadeService.sendDebt: " + resultDto.toString());
                    msgMap.put(std.getMainOrderId(), StringUtils.isBlank(resultDto.getMsg())?"标的sendDebt失败":resultDto.getMsg());
                } else {
                    // 批量更新单状态为退出中
                    int n1 = busiOrderSubMapper.batchUpdateOrderSubStatus(orderIdList);
                    int n2 = busiOrderSubMapper.batchUpdateOrderStatus(orderIdList);
                    if (n1 == 0 || n2 == 0){
                        return new ResultDto("更新订单状态失败:", false);
                    }
                }
            }
            if (!msgMap.isEmpty()) {
                return new ResultDto(ResultDto.ERROR_CODE, msgMap, false);
            }
        } catch (Exception e) {
            return new ResultDto("转让失败:" + e.getMessage(), false);
        }
        return ResultDto.SUCCESS();
    }

    @Override
    public ResultDto specialBuyBackOrder(MatchResultDto matchResultDto) {
        log.info("异常回购撮合结果参数:" + JsonUtils.toJson(matchResultDto));
        try {
            // 查询特殊理财人
            String cmNumber = this.gainDefaultSpecialLender();
            CustomerMainInfo mainInfo = customerMainInfoService.findOneByCmNumber(cmNumber);
            for (MatchSucResult msr : matchResultDto.getMatchSucResultList()){
                BigDecimal balance = accountOverview520003Service.getAccountBalance(mainInfo);
                if (balance.compareTo(msr.getCapitalAmount()) < 0) {
                    return ResultDto.FAIL("特殊理财人账户余额不足");
                }
                BusiOrderSub bos = busiOrderSubMapper.queryBusiOrderSubInfo(msr.getInitOrderNum());

                Map<String, Object> map = new HashMap<>();
                map.put("productId", bos.getProductId());
                BusiProductSub productSub = busiProductSubMapper.findProductById(map);

                BusiOrderSub orderSub = convertOrderSub(msr, mainInfo, productSub);
                busiOrderSubMapper.insert(orderSub);//生成子订单

                TenderVo tenderVo = new TenderVo();
                tenderVo.setBidAmount(new BigDecimal(msr.getSubjectAmt()));
                tenderVo.setCustomerMainInfo(mainInfo);
                tenderVo.setDebtType(msr.getDebtType());
                tenderVo.setYearRate(productSub.getYearRate());
                tenderVo.setSaleEndDate(productSub.getSaleEndDate());
                tenderVo.setSaleStartDate(productSub.getSaleStartDate());
                tenderVo.setHolderType(OrderConstants.OrderHoldType.HOLD_SPECIAL_EXCEPTION);
                tenderVo.setInitOrderNum(msr.getInitOrderNum());
                tradeService.tender(tenderVo, orderSub, productSub);

                //保存支付流水
                BusiOrderSub originOrder = getOriginOrder(msr.getInitOrderNum());
                if (originOrder == null) {
                    originOrder = orderSub;
                }
                BusiDebtTransfer debtTransfer = createDebtTransfer(orderSub, bos, originOrder, BigDecimal.ZERO);// 生成转让记录
                log.info("异常回购撮合-->[createDebtTransfer] 转让编号:" + debtTransfer.getTransferNo());

                orderSub.setTransferNo(debtTransfer.getTransferNo());
                tradeService.bidTransfer(orderSub, bos, originOrder, mainInfo, BigDecimal.ZERO, msr.getDebtWorth());
                BusiOrderSub busiOrderSub = new BusiOrderSub();
                busiOrderSub.setId(bos.getId());
                busiOrderSub.setSubOrderStatus(AppConstants.ORDER_SUB_STATUS.ORDER_STATUS_2);
                busiOrderSub.setTransferNo(debtTransfer.getTransferNo());
                busiOrderSub.setStatus(AppConstants.BusiOrderStatus.BUSIORDER_STATUS_16);
                busiOrderSub.setModifyDate(new Date());
                busiOrderSubMapper.updateByPrimaryKeySelective(busiOrderSub);

                CustomerMainInfo cmi = customerMainInfoService.findAuthCustomerById(bos.getCustomerId());

                TransferVo transferVo = new TransferVo();
                transferVo.setCmIdnum(cmi.getCmIdnum());
                transferVo.setCustomerPhone(cmi.getCmCellphone());
                transferVo.setCustomerName(bos.getCustomerName());
                transferVo.setCustomerNo(bos.getCmNumber());
                transferVo.setOrderNo(bos.getOrderId());
                transferVo.setSubjectNo(bos.getSubjectNo());
                transferVo.setFinanceId(msr.getFinanceId());
                log.info("异常回购撮合-->转让产品通知标的:" + debtTransfer.getTransferNo());
                subjectService.notifyTransferProductSubject(debtTransfer.getTransferNo(), orderSub, null, mainInfo, transferVo);//通知标的
            }
        } catch (Exception e) {
            log.error("异常回购撮合下单失败", e);
            MailUtil.sendMail("异常回购撮合下单失败", "失败原因 ：" + e.getMessage());
            return ResultDto.FAIL(e.getMessage());
        }
        return new ResultDto("异常回购下单成功", true);
    }

    private BusiOrderSub convertOrderSub(MatchSucResult matchSucResult, CustomerMainInfo mainInfo, BusiProductSub productSub){
        BusiOrderSub orderSub = new BusiOrderSub();
        Date date = new Date();
        String orderNumSub = SerialNumberGenerator.generatorOrderNum(OrderGenerateType.SUB_ORDER,mainInfo.getId());
        Date interestStartDate = productSub.getInterestStartDate();
        BigDecimal closeDay = new BigDecimal(DateUtil.getBetweenDays(productSub.getInterestEndDate(), interestStartDate) + 1);
        BigDecimal interest = matchSucResult.getInterest();
        interestStartDate = DateUtil.getDateBeginTime(date);
        orderSub.setStatus(AppConstants.BusiOrderStatus.BUSIORDER_STATUS_1);
        orderSub.setOrderAmt(matchSucResult.getCapitalAmount());
        orderSub.setSubOrderStatus(AppConstants.ORDER_SUB_STATUS.ORDER_STATUS_1);
        orderSub.setCustomerId(mainInfo.getId());
        orderSub.setCmNumber(mainInfo.getCmNumber());
        orderSub.setCustomerName(mainInfo.getCmRealName());
        orderSub.setProductName(productSub.getProductName());
        orderSub.setPrincipalinterest(matchSucResult.getCapitalAmount().add(interest));
        orderSub.setSubjectNo(matchSucResult.getSubjectNo());
        orderSub.setDebtNo(matchSucResult.getFinanceId());
        orderSub.setDebtType(matchSucResult.getDebtType());
        orderSub.setCashAmount(matchSucResult.getCapitalAmount());
        orderSub.setOrderTime(date);
        orderSub.setConfirmPaymentDate(date);
        orderSub.setProductType(5);
        orderSub.setOrderId(orderNumSub);
        orderSub.setDebtorNum(matchSucResult.getLoanCustomerNo());
        orderSub.setDebtorName(matchSucResult.getLoanCustomerName());
        orderSub.setRaiseRateIncome(BigDecimal.ZERO);
        orderSub.setHolderType(OrderConstants.OrderHoldType.HOLD_SPECIAL_EXCEPTION);
        orderSub.setInterestEndDate(productSub.getInterestEndDate());
        orderSub.setInterestStartDate(interestStartDate);
        orderSub.setProductId(0L);
        orderSub.setPayType(productSub.getRepayType());
        orderSub.setBorrowerDate(matchSucResult.getBorrowerDate());
        orderSub.setCloseDays(closeDay.longValue());
        orderSub.setYearRate(productSub.getYearRate());
        return orderSub;
    }

    void releaseYxProductDebt(){
        log.info("退出推送优选存量产品债权");
        Date beforeDate = DateUtils.addDays(new Date(), -1);
        String beforeDay = DateUtil.dateToString(beforeDate);
        Map<String, Object> map = new HashMap<>();
        map.put("endDate", beforeDay);
        map.put("orderStatus", AppConstants.BusiOrderStatus.BUSIORDER_STATUS_0);
        map.put("productType", AppConstants.ProductSubjectType.SUBJECT_YX);
        List<BusiOrderSub> subList = busiOrderSubMapper.selectByMap(map);

        if (CollectionUtils.isEmpty(subList)) {
            log.info("没有优选债权信息可推送");
            return ;
        }
        Map<Long,List<MatchDebtItemDto>> productMap=Maps.newHashMap();
        for(BusiOrderSub busiOrder : subList){
            Map<String,Object> queryMap= Maps.newHashMap();
            queryMap.put("parentId",busiOrder.getId());
            queryMap.put("subOrderStatus","1");
            List<BusiOrderSub> list = busiOrderSubMapper.selectByMap(queryMap);
            for(BusiOrderSub orderSub : list){
                if(AppConstants.BusiOrderStatus.BUSIORDER_STATUS_0.equals(orderSub.getStatus())
                        ||AppConstants.BusiOrderStatus.BUSIORDER_STATUS_14.equals(orderSub.getStatus())){
                    try{
                        saveDebtQueue(orderSub);
                    }catch (Exception e){
                        log.error(e.getMessage(),e);
                    }
                    MatchDebtItemDto mdid = new MatchDebtItemDto();
                    mdid.setProductCode(String.valueOf(orderSub.getProductId()));
                    mdid.setOrderNo(orderSub.getOrderId());
                    mdid.setDebtType(DebtType.EXPIRE_ASSIGNMENT);
                    mdid.setFinanceId(orderSub.getDebtNo());
                    mdid.setSubjectNo(orderSub.getSubjectNo());
                    if(productMap.get(orderSub.getProductId())==null){
                        List<MatchDebtItemDto> planItemList = new ArrayList<>();
                        planItemList.add(mdid);
                        productMap.put(orderSub.getProductId(),planItemList);
                    }else{
                        List<MatchDebtItemDto> planItemList=productMap.get(orderSub.getProductId());
                        planItemList.add(mdid);
                    }
                }
            }
        }
        if(productMap.isEmpty()){
            return;
        }
        Set<Long> planSet = productMap.keySet();
        for(Long planId:planSet){
            List<MatchDebtItemDto> planItemList=productMap.get(planId);
            MatchDebtReqDto debtReqDto = new MatchDebtReqDto();
            debtReqDto.setPartnerNo(AppConstants.PARTNER_NO);
            debtReqDto.setItemDtos(planItemList);
            AssetsResultDto resultDto = null;
            try{
                resultDto=lcbFinancialPlanFacadeService.sendDebt(debtReqDto);
            }catch (Exception e){
                log.error(planId+e.getMessage(),e);
                MailUtil.sendMail("推送优选债权信息失败", planId+e.getMessage());
            }
            if (resultDto!=null&&!resultDto.isSuccess()) {
                log.error("推送优选债权信息失败:" + resultDto.getMsg());
                MailUtil.sendMail("推送优选债权信息失败", resultDto.getMsg());
            }
        }
    }

    void releasePlanProductDebt(){
        log.info("退出推送智投宝债权");
        Date beforeDate = DateUtils.addDays(new Date(), -1);
        String beforeDay = DateUtil.dateToString(beforeDate);
        Map<String, Object> map = new HashMap<>();
        map.put("endDate", beforeDay);
        map.put("orderStatus", AppConstants.BusiOrderStatus.BUSIORDER_STATUS_17);
        map.put("productType", AppConstants.ProductSubjectType.FINANCE_PLAN);
        map.put("exiting", "0");
        List<BusiOrderSub> subList = busiOrderSubMapper.selectByMap(map);

        if (CollectionUtils.isEmpty(subList)) {
            log.info("没有智投宝债权信息可推送");
            return ;
        }
        for (BusiOrderSub busiOrder : subList) {
            Map<String,Object> objectMap=new HashMap<>();
            objectMap.put("status",AppConstants.BusiOrderStatus.BUSIORDER_STATUS_18);
            objectMap.put("oldStatus",AppConstants.BusiOrderStatus.BUSIORDER_STATUS_17);
            objectMap.put("id",busiOrder.getId());
            busiOrderService.updateOrderByIdAndStatus(objectMap);
        }

        Map<Long,Object> planMap=Maps.newHashMap();
        for(BusiOrderSub busiOrder : subList){
            Map<String,Object> queryMap= Maps.newHashMap();
            queryMap.put("parentId",busiOrder.getId());
            queryMap.put("subOrderStatus","1");
            List<BusiOrderSub> list = busiOrderSubMapper.selectByMap(queryMap);
            for(BusiOrderSub orderSub : list){
                if(AppConstants.BusiOrderStatus.BUSIORDER_STATUS_0.equals(orderSub.getStatus())
                        ||AppConstants.BusiOrderStatus.BUSIORDER_STATUS_14.equals(orderSub.getStatus())){
                    try{
                        saveDebtQueue(orderSub);
                    }catch (Exception e){
                        log.error(e.getMessage(),e);
                    }
                    MatchDebtItemDto mdid = new MatchDebtItemDto();
                    mdid.setProductCode(String.valueOf(orderSub.getProductId()));
                    mdid.setOrderNo(orderSub.getOrderId());
                    mdid.setDebtType(DebtType.EXPIRE_ASSIGNMENT);
                    mdid.setFinanceId(orderSub.getDebtNo());
                    mdid.setSubjectNo(orderSub.getSubjectNo());
                    if(planMap.get(orderSub.getPlanId())==null){
                        List<MatchDebtItemDto> planItemList = new ArrayList<>();
                        planItemList.add(mdid);
                        planMap.put(orderSub.getPlanId(),planItemList);
                    }else{
                        List<MatchDebtItemDto> planItemList=(List<MatchDebtItemDto>)planMap.get(orderSub.getPlanId());
                        planItemList.add(mdid);
                    }
                }
            }
        }
        if(planMap.isEmpty()){
            return;
        }

        Set<Long> planSet = planMap.keySet();
        for(Long planId:planSet){
            List<MatchDebtItemDto> planItemList=(List<MatchDebtItemDto>)planMap.get(planId);
            MatchDebtReqDto debtReqDto = new MatchDebtReqDto();
            debtReqDto.setPartnerNo(AppConstants.PARTNER_NO);
            debtReqDto.setItemDtos(planItemList);

            AssetsResultDto resultDto = null;
            try{
                resultDto=lcbFinancialPlanFacadeService.sendDebt(debtReqDto);
            }catch (Exception e){
                log.error(planId+e.getMessage(),e);
                MailUtil.sendMail("推送智投宝债权信息失败", planId+e.getMessage());
            }
            if (resultDto!=null&&!resultDto.isSuccess()) {
                log.error("推送智投宝债权信息失败:" + resultDto.getMsg());
                for (BusiOrderSub busiOrder : subList) {
                    if(busiOrder.getProductId().longValue()==planId.longValue()){
                        Map<String,Object> objectMap=new HashMap<>();
                        objectMap.put("status",AppConstants.BusiOrderStatus.BUSIORDER_STATUS_17);
                        objectMap.put("oldStatus",AppConstants.BusiOrderStatus.BUSIORDER_STATUS_18);
                        objectMap.put("id",busiOrder.getId());
                        try{
                            busiOrderService.updateOrderByIdAndStatus(objectMap);
                        }catch (Exception e){
                            log.error(planId+e.getMessage(),e);
                        }
                    }
                }
                MailUtil.sendMail("推送智投宝债权信息失败", resultDto.getMsg());
            }
        }
    }

    @Async
    @Override
    public ResultDto earlyOut() {
        log.info("退出推送债权");
        try{
            releaseYxProductDebt();
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        try{
            releasePlanProductDebt();
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return new ResultDto("推送债权信息成功调用", true);
    }

    @Override
    public ResultDto bidTenderRepeal(String orderNo, String tenderNo) {
        BusiOrderSub orderSub = busiOrderSubMapper.queryBusiOrderSubInfo(orderNo);
        if(orderSub==null){
            return new ResultDto(orderNo+"对应订单信息不存在", false);
        }
        try{
            tradeService.bidTenderRepeal(orderSub, tenderNo);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return new ResultDto(e.getMessage(), false);
        }
        return new ResultDto("撤标成功", true);
    }

    @Override
    public ResultDto bidTransferRepeal(String newOrderNo,String initOrderNo,String flowNum) {
        BusiOrderSub orderSub = busiOrderSubMapper.queryBusiOrderSubInfo(newOrderNo);
        if(orderSub==null){
            return new ResultDto(newOrderNo+"对应订单信息不存在", false);
        }
        CustomerMainInfo mainInfo = customerMainInfoService.findOne(orderSub.getCustomerId());
        if(mainInfo==null){
            return new ResultDto(newOrderNo+"对应用户信息不存在", false);
        }
        BusiOrderSub initOrderSub = busiOrderSubMapper.queryBusiOrderSubInfo(initOrderNo);
        if(initOrderSub==null){
            return new ResultDto(initOrderNo+"对应订单信息不存在", false);
        }
        CustomerMainInfo transferInfo = customerMainInfoService.findOne(initOrderSub.getCustomerId());
        if(transferInfo==null){
            return new ResultDto(initOrderSub+"对应出让用户信息不存在", false);
        }
        if(orderSub.getSubjectNo()==null){
            return new ResultDto(initOrderSub+"对应标的编号不存在", false);
        }
        BusiOrderSub originOrder = getOriginOrder(initOrderSub.getOrderId());
        if (originOrder == null) {
            originOrder = initOrderSub;
        }
        BusiDebtTransfer debtTransfer = createDebtTransfer(orderSub, initOrderSub, originOrder, BigDecimal.ZERO);// 生成转让记录

        BusiProductSub product =new BusiProductSub();
        product.setProductName(orderSub.getProductName());
        TransferVo transferVo = new TransferVo();
        transferVo.setCmIdnum(transferInfo.getCmIdnum());
        transferVo.setCustomerPhone(transferInfo.getCmCellphone());
        transferVo.setCustomerName(transferInfo.getCmRealName());
        transferVo.setCustomerNo(transferInfo.getCmNumber());
        transferVo.setOrderNo(initOrderSub.getOrderId());
        transferVo.setSubjectNo(initOrderSub.getSubjectNo());
        transferVo.setFinanceId(initOrderSub.getDebtNo());
        try{
            boolean transferRepeal = tradeService.bidTransferRepeal(orderSub,flowNum);
            if(transferRepeal){
                String recordNum = tradeService.anewBidTransfer(orderSub, initOrderSub, originOrder, mainInfo);
                initOrderSub.setSubOrderStatus(AppConstants.ORDER_SUB_STATUS.ORDER_STATUS_2);
                initOrderSub.setStatus(AppConstants.BusiOrderStatus.BUSIORDER_STATUS_16);
                busiOrderSubMapper.updateByPrimaryKeySelective(initOrderSub);
                debtTransfer.setIsSettle(AppConstants.TransferDebtStatus.TRANSFER_SETTLE_YES);
                debtTransfer.setTransferStatus(AppConstants.DebtTransferStatus.TRANSFER_FINISH);
                debtTransferMapper.updateByPrimaryKeySelective(debtTransfer);
                String transferNo=debtTransfer.getTransferNo();
                orderSub.setTransferNo(transferNo);
                orderSub.setTransferType(AppConstants.OrderTransferStatus.ORDER_TRANSFER);
                orderSub.setTransferSerialNo(recordNum);
                busiOrderSubMapper.updateByPrimaryKeySelective(orderSub);
                subjectService.notifyTransferProductSubject(transferNo, orderSub,product,mainInfo, transferVo);
            }
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return new ResultDto(e.getMessage(), false);
        }
        return new ResultDto("修复转让成功", true);
    }

    @Override
    public PageResultDto<BusiMatchExceptionVO> queryMatchException(MatchExceptionDTO matchExceptionDTO) {
        Map<String, Object> objectMap = CommonHelper.transBean2Map(matchExceptionDTO);
        int pageSize = matchExceptionDTO.getPageSize();
        Page<BusiMatchExceptionVO> page = new Page<>();
        page.setPageNo(matchExceptionDTO.getPageNo());
        page.setPageSize(pageSize);
        objectMap.put("page", page);
        List<BusiMatchExceptionVO> exceptionList = busiMatchExceptionMapper.queryMatchException(objectMap);
        return new PageResultDto(exceptionList, page.getTotalRecord(), pageSize);
    }

    @Override
    public PageResultDto<MatchFlowVO> queryMatchFlow(MatchFlowDTO matchFlowDTO){
        Map<String, Object> objectMap = CommonHelper.transBean2Map(matchFlowDTO);
        int pageSize = matchFlowDTO.getPageSize();
        Page<MatchFlowVO> page = new Page<>();
        page.setPageNo(matchFlowDTO.getPageNo());
        page.setPageSize(pageSize);
        objectMap.put("page", page);
        List<BusiMatchSucInfo> matchSucInfos = busiMatchSucInfoMapper.selectByMap(objectMap);
        List<MatchFlowVO> matchFlowList =new ArrayList<>();
        if(CollectionUtils.isNotEmpty(matchSucInfos)){
            for(BusiMatchSucInfo busiMatchSucInfo:matchSucInfos){
                MatchFlowVO matchFlowVO =new MatchFlowVO();
                CopyUtil.copyProperties(matchFlowVO,busiMatchSucInfo);
                matchFlowList.add(matchFlowVO);
            }
        }
        return new PageResultDto(matchFlowList, page.getTotalRecord(), pageSize);
    }

    @Override
    public ResultDto updateMatchExceptionByMap(Map<String, Object> map) {
        try{
            busiMatchExceptionMapper.updateByMap(map);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            return new ResultDto(e.getMessage(), false);
        }
        return new ResultDto("修正撮合成功", true);
    }

    @Transactional
    public long saveMatchResult(MatchResultDto matchResultDto){
        List<MatchSucResult> sucResultList = matchResultDto.getMatchSucResultList();
        List<MatchSucResult> abnormalSucResultList =  matchResultDto.getAbnormalMatchSucResult();
        BusiMatchResultInfo matchResultInfo=new BusiMatchResultInfo();
        Long productId = matchResultDto.getProductId();
        String batchNo = matchResultDto.getBatchNo();
        String serialNo = matchResultDto.getSerialNo();
        Map<String,Object> resultInfoMap=Maps.newHashMap();
        resultInfoMap.put("serialNo", serialNo);
        List<BusiMatchResultInfo> infos = busiMatchResultInfoMapper.selectByMap(resultInfoMap);
        if(CollectionUtils.isEmpty(infos)){
            matchResultInfo.setBatchNo(batchNo);
            matchResultInfo.setMatctAmount(matchResultDto.getMatchAmount());
            matchResultInfo.setProductId(productId);
            matchResultInfo.setStatus(AppConstants.BUSI_MATCH_RESULT_INFO_STATUS.NON_MATCH);
            matchResultInfo.setSerialNo(serialNo);
            matchResultInfo.setMatchSucSize(matchResultDto.getMatchSucSize());
            busiMatchResultInfoMapper.insert(matchResultInfo);

            for(MatchSucResult matchSucResult:sucResultList){
                BusiMatchSucInfo matchSucInfo=new BusiMatchSucInfo();
                CopyUtil.copyProperties(matchSucInfo,matchSucResult);
                matchSucInfo.setMasterId(matchResultInfo.getId());
                matchSucInfo.setOperStatus("0");
                matchSucInfo.setFinanceNum(matchSucResult.getFinanceNum());
                matchSucInfo.setBatchNo(batchNo);
                if(AppConstants.FinancePlan.DEBT_TYPE1.equals(matchSucResult.getDebtType())){
                    matchSucInfo.setTransferNum(0);
                }
                busiMatchSucInfoMapper.insert(matchSucInfo);
            }
            if (abnormalSucResultList != null && abnormalSucResultList.size() > 0){
                for(MatchSucResult matchSucResult:abnormalSucResultList) {
                    BusiAbnorMatchSucInfo matchSucInfo = new BusiAbnorMatchSucInfo();
                    CopyUtil.copyProperties(matchSucInfo, matchSucResult);
                    matchSucInfo.setMasterId(matchResultInfo.getId());
                    matchSucInfo.setOperStatus("0");
                    matchSucInfo.setFinanceNum(matchSucResult.getFinanceNum());
                    matchSucInfo.setBatchNo(batchNo);
                    if (AppConstants.FinancePlan.DEBT_TYPE1.equals(matchSucResult.getDebtType())) {
                        matchSucInfo.setTransferNum(0);
                    }
                    busiAbnorMatchSucInfoMapper.insert(matchSucInfo);
                }
            }else{
                log.info("没有提前还清的账单");
            }
        }else{
            log.error("已存在撮合信息" + serialNo+",不能重复操作");
            throw new BusinessException("已存在撮合信息" + serialNo+",不能重复操作");
        }
        return matchResultInfo.getId();
    }

    public Map<Long, Object> repairMatchOrder(String financeIds){
        Map<String,Object> paramMap=Maps.newHashMap();
        paramMap.put("operStatus", "2");
        if(!StringUtil.isEmpty(financeIds)){
            paramMap.put("list", Arrays.asList(financeIds.split(",")));
        }
        List<BusiMatchSucInfo> sucInfos = busiMatchSucInfoMapper.selectByMap(paramMap);
        if(CollectionUtils.isEmpty(sucInfos)){
            throw new BusinessException("撮合信息" + financeIds+"不存在");
        }else{
            Map<Long,Object> infoMap=Maps.newHashMap();
            for(BusiMatchSucInfo busiMatchSucInfo:sucInfos){
                long masterId=busiMatchSucInfo.getMasterId();
                if(infoMap.get(masterId)==null){
                    List<BusiMatchSucInfo> matchSucResultList =new ArrayList<>();
                    busiMatchSucInfo.setOperStatus("0");
                    matchSucResultList.add(busiMatchSucInfo);
                    infoMap.put(masterId,matchSucResultList);
                }else{
                    List<BusiMatchSucInfo> matchSucResultList=(List<BusiMatchSucInfo>)infoMap.get(masterId);
                    busiMatchSucInfo.setOperStatus("0");
                    matchSucResultList.add(busiMatchSucInfo);
                }
            }

            Map<Long,Object> matchMap=Maps.newHashMap();
            if(!infoMap.isEmpty()){
                Set<Long> masterIdSet = infoMap.keySet();
                for(Long masterId:masterIdSet){
                    BusiMatchResultInfo resultInfo = busiMatchResultInfoMapper.selectByPrimaryKey(masterId);
                    List<BusiMatchSucInfo> matchSucResultList=(List<BusiMatchSucInfo>)infoMap.get(masterId);
                    resultInfo.setBusiMatchSucInfos(matchSucResultList);
                    matchMap.put(masterId,resultInfo);
                }
            }
            return matchMap;
        }
    }

    /**
     * 保存资金队列
     * @param orderSub
     */
    private void saveDebtQueue(BusiOrderSub orderSub){
        BusiDebtQueue debtQueue = new BusiDebtQueue();
        debtQueue.setDebtType(orderSub.getDebtType());
        debtQueue.setOrderNo(orderSub.getOrderId());
        debtQueue.setCmNumber(orderSub.getCmNumber());
        debtQueue.setCreateTime(new Date());
        debtQueue.setProductId(orderSub.getProductId());
        debtQueue.setDiskNo("");
        debtQueue.setPushFlag(0);
        debtQueue.setHolderType(orderSub.getHolderType().equals("0") ? "0": "1");
        debtDetailMapper.saveDebtInfo(debtQueue);
    }

    /**
     * 处理是否完成
     * @param resultInfoMap
     */
    private boolean isFinish(Map<String,Object> resultInfoMap){
        List<BusiMatchResultInfo> infos = busiMatchResultInfoMapper.selectByMap(resultInfoMap);
        if(!CollectionUtils.isEmpty(infos)){
            for(BusiMatchResultInfo matchResultInfo:infos){
                if(!"1".equals(matchResultInfo.getStatus())){
                    return false;
                }
            }
        }
        return true;
    }


    @Override
    public List<SpecialFinancialPlannerVO> querySpecialFinancePeopleList() {
        List<String> cmNumberList =  this.gainSpecialLenders();
        List<SpecialFinancialPlannerVO> specialFinancialPlanners =null;
        if(CollectionUtils.isNotEmpty(cmNumberList)){
            Map map = Maps.newHashMap();
            map.put("cmNumberList",cmNumberList);
            specialFinancialPlanners = customerMainInfoService.querySpecialFinancialPlannerInfoList(map);
        }
        return specialFinancialPlanners;
    }


    @Override
    public String gainDefaultSpecialLender() {
        SysParameter sysParameter = sysParameterService.findOneByPrTypeDefaultWithoutCache(AppConstants.SPECIAL_FINANCE_PEOPLE);
        String cmNumber = (sysParameter == null) ? "" : sysParameter.getPrValue();
        return cmNumber;
    }


    @Override
    public List<String> gainSpecialLenders() {
        List<SysParameter> sysParameters = sysParameterService.findByPrTypeWithoutCache(AppConstants.SPECIAL_FINANCE_PEOPLE);
        List list = Lists.newArrayList();
        if(CollectionUtils.isNotEmpty(sysParameters)){
            for(SysParameter sysParameter:sysParameters){
                list.add(sysParameter.getPrValue());
            }
        }
        return list;
    }


    @Override
    public long debtMatchNums(Long productId) {
        return debtDetailMapper.debtMatchNums(productId);
    }

    public void abnormalDebtHandle(Long orderId,String batchNo, Map<String, BusiFundDetail> fundMap,List<BusiAbnorMatchSucInfo> busiMatchSucInfoList) {
        BigDecimal matchedAmt = BigDecimal.ZERO;
        String capitalCode = "";
        int financeNum = 0;
        if (busiMatchSucInfoList.isEmpty()){
            return;
        }
        for (BusiAbnorMatchSucInfo matchSucResult : busiMatchSucInfoList) {
            matchedAmt = matchedAmt.add(matchSucResult.getCapitalAmount());
            capitalCode = matchSucResult.getCapitalCode();
            financeNum = matchSucResult.getFinanceNum();
        }
        BusiFundDetail busiFundDetail = fundMap.get(capitalCode);
        if (busiFundDetail == null) {

            try {
                Map<String, Object> fundParams = Maps.newHashMap();
                fundParams.put("id", capitalCode);
                fundParams.put("matchDate", new Date());
                fundParams.put("matchingAmt", "0");
                Map<String, Object> detailMap = Maps.newHashMap();
                detailMap.put("batchNo", batchNo);
                detailMap.put("operStatus", "1");
                detailMap.put("capitalCode", capitalCode);
                List<BusiAbnorMatchSucInfo> infoList = busiAbnorMatchSucInfoMapper.selectByMap(detailMap);
                if (CollectionUtils.isNotEmpty(infoList) && infoList.size() == financeNum) {
                    fundParams.put("status", AppConstants.FinancePlan.FUND_STATUS_3);
                    fundParams.put("initStatus", AppConstants.FinancePlan.FUND_STATUS_2);
                    fundParams.put("matchingAmt", BigDecimal.ZERO);
                }
                fundDetailMapper.updateByMap(fundParams);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                MailUtil.sendMail("更新订单：" + orderId + "的相关信息发生异常", "，异常原因:" + e);
            }
        }
    }
}
