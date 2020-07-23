package com.zdmoney.service;

import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zdmoney.common.util.DateUtil;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.enm.AuditMethodType;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.exception.HessianRpcException;
import com.zdmoney.integral.api.common.dto.PageResultDto;
import com.zdmoney.integral.api.common.dto.ResultDto;
import com.zdmoney.integral.api.dto.appointment.*;
import com.zdmoney.integral.api.dto.coin.*;
import com.zdmoney.integral.api.dto.coupon.CouponGiveDto;
import com.zdmoney.integral.api.dto.coupon.CouponGiveResDto;
import com.zdmoney.integral.api.dto.lottery.LotteryQualificationDto;
import com.zdmoney.integral.api.dto.voucher.*;
import com.zdmoney.integral.api.facade.*;
import com.zdmoney.mapper.BusiMallMapper;
import com.zdmoney.mapper.order.BusiOrderExchangeMapper;
import com.zdmoney.models.BusiMall;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.order.BusiOrderExchange;
import com.zdmoney.models.sys.SysParameter;
import com.zdmoney.service.base.BaseBusinessService;
import com.zdmoney.service.businessReport.BusinessReportService;
import com.zdmoney.service.welfare.WelfareService;
import com.zdmoney.utils.LaocaiUtil;
import com.zdmoney.utils.NumberUtil;
import com.zdmoney.utils.Page;
import com.zdmoney.vo.PayBespeakDTO;
import com.zdmoney.web.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by zhou on 2016/2/26.
 * 捞财币
 */
@Service
@Slf4j
public class BusiMallService extends BaseBusinessService<BusiMall, Long>{

    private BusiMallMapper getBusiMallMapper() {
        return (BusiMallMapper) baseMapper;
    }

    @Autowired
    CustomerMainInfoService customerMainInfoService;

    @Autowired
    ICoinFacadeService coinFacadeService;

    @Autowired
    IVoucherFacadeService voucherFacadeService;

    @Autowired
    ICouponFacadeService couponFacadeService;

    @Autowired
    BusiOrderService busiOrderService;

    @Autowired
    BusiOrderExchangeMapper busiOrderExchangeMapper;

    @Autowired
    private SysParameterService sysParameterService;

    @Autowired
    private ILotteryQualificationFacadeService lotteryQualificationFacadeService;

    @Autowired
    private IAppointmentFacadeService appointmentFacadeService;

    @Autowired
    private BusinessReportService businessReportService;

    @Autowired
    private WelfareService welfareService;

    public ProductDTO mallHome(Long customerId) throws Exception {
        CustomerMainInfo mainInfo = checkExistByCustomerId(customerId);
        ProductDTO productDTO = new ProductDTO();
        productDTO.setCoinBalance(getCoinBalance(mainInfo.getCmNumber()));
        productDTO.setCoinBalanceLast(getCoinBalanceLast(mainInfo.getCmNumber()));
        productDTO.setOverTime(getOverTimeStr());
        String remindStr = getRemindTimeStr();
        if(StringUtils.isNotBlank(remindStr)){
            String[] reminds = remindStr.split(";");
            productDTO.setRemindVail(reminds[0]);
            productDTO.setRemindTips(reminds[1]);
        }

        List<BusiMall> productList = getBusiMallMapper().getProductList();
        List<BusiMall> coupons = Lists.newArrayList();
        List<BusiMall> vouchers = Lists.newArrayList();
        List<BusiMall> nDaysVouchers = Lists.newArrayList();
        List<BusiMall> bespeaks = Lists.newArrayList();
        for (BusiMall mall : productList) {
            if (AppConstants.PRODUCT_TYPE.TYPE_1.equals(mall.getMerchandiseType())) {
                coupons.add(mall);
            }
            if (AppConstants.PRODUCT_TYPE.TYPE_2.equals(mall.getMerchandiseType())) {
                if (mall.getInterestDay() == null || mall.getInterestDay() == 0) {
                    vouchers.add(mall);
                } else {
                    nDaysVouchers.add(mall);
                }
            }
            if (AppConstants.PRODUCT_TYPE.TYPE_3.equals(mall.getMerchandiseType())) {
                bespeaks.add(mall);
            }
        }
        productDTO.setCoupons(coupons);
        productDTO.setVouchers(vouchers);
        productDTO.setNDaysVouchers(nDaysVouchers);
        productDTO.setBespeaks(bespeaks);
        return productDTO;
    }


    public Long getCoinBalance(String cmNumber) {
        ResultDto<CoinAccountDto> resultDto = coinFacadeService.getCoinAccount(cmNumber);
        if (resultDto.isSuccess()) {
            return resultDto.getData().getCoin();
        } else {
            throw new HessianRpcException("coin.balance", resultDto.getMsg());
        }
    }

    public Long getCoinBalanceLast(String cmNumber) {
        ResultDto<CoinAccountDto> resultDto = coinFacadeService.getCoinAccount(cmNumber);
        if (resultDto.isSuccess()) {
            return resultDto.getData().getLastCoin();
        } else {
            throw new HessianRpcException("coin.balance", resultDto.getMsg());
        }
    }

    @Transactional(propagation= Propagation.NOT_SUPPORTED)
    public ExchangeResultDTO exchangeCoin(Long customerId, Long productId,String periodFlag) throws Exception {
        ExchangeResultDTO dto = new ExchangeResultDTO();
        CustomerMainInfo mainInfo = checkExistByCustomerId(customerId);
        BusiMall busiMall = findOne(productId);
        if (busiMall == null) {
            throw new BusinessException("product.not.exist");
        }
        //商品是否已上架且显示
        if (!("1".equals(busiMall.getStatus()) && "1".equals(busiMall.getShowStatus()))) {
            throw new BusinessException("product.invalidStatus");
        }
        //商品已抢完
        if (busiMall.getBuyNum() >= busiMall.getMerchandiseNum()) {
            throw new BusinessException("product.notEnough");
        }

        //查询用户 捞财币余额 或 上期捞财币余额
        Long coinBalance = 0L;
        if ("this".equals(periodFlag)) {
            coinBalance = getCoinBalance(mainInfo.getCmNumber());
        } else if ("last".equals(periodFlag)) {
            coinBalance = getCoinBalanceLast(mainInfo.getCmNumber());
        }
        //余额是否足够此次购买
        if (coinBalance.compareTo(busiMall.getBuyAmt().longValue()) == -1) {
            throw new BusinessException("coin.notEnough");
        }
        //下订单
        BusiOrderExchange exchange = new BusiOrderExchange();
        String orderNum = busiOrderService.buildCeNumber("C", "6666", customerId);
        exchange.setOrderNum(orderNum);
        exchange.setCustomerId(customerId);
        exchange.setOrderAmt(busiMall.getBuyAmt());
        exchange.setAmtType(busiMall.getAmtType());
        exchange.setProductId(productId);
        exchange.setProductType(busiMall.getMerchandiseType());
        exchange.setOrderTime(new Date());
        exchange.setStatus(AppConstants.OrderExchargeStatus.STATUS_0);
        int num = insertBusiOrderExchange(exchange);
        if (num != 1) {
            log.error("保存兑换订单失败，客户号：" + customerId + "，orderId：" + orderNum);
            throw new BusinessException("coin.exchange.save.failed");
        }
        //调账户，扣捞财币
        CoinOprDto oprDto = new CoinOprDto();
        oprDto.setAccountNo(mainInfo.getCmNumber());
        oprDto.setTips("兑换" + busiMall.getMerchandiseName());
        oprDto.setOrderNo(orderNum);
        oprDto.setCoin(busiMall.getBuyAmt().longValue());
        oprDto.setDetails("");
        oprDto.setTransNo(SerialNoGeneratorService.generateTransNoByOrderId(orderNum,"CONSUMECOIN"));
        ResultDto<CoinOprResultDto> resultDto = null;
        if ("this".equals(periodFlag)) {
            resultDto = coinFacadeService.consumeCoin(oprDto);
        } else if ("last".equals(periodFlag)) {
            resultDto = coinFacadeService.consumeLastCoin(oprDto);
        }
        if (resultDto.isSuccess()) {
            CoinOprResultDto coinOprResultDto = resultDto.getData();
            //更新订单状态-消费捞财币成功
            exchange.setStatus(AppConstants.OrderExchargeStatus.STATUS_2);
            exchange.setPaySerNum(coinOprResultDto.getRecordNum());
            updateBusiOrderExchange(exchange);
            try {
                //领相应的红包或者捞财币
                if (AppConstants.PRODUCT_TYPE.TYPE_1.equals(busiMall.getMerchandiseType())) {
                    CouponGiveDto giveDto = new CouponGiveDto();
                    giveDto.setAmount(busiMall.getCouponAmt().intValue());
                    giveDto.setAccountNo(mainInfo.getCmNumber());
                    giveDto.setSource("EXCHANGE");
                    giveDto.setInvestAmount(busiMall.getInvestAmt() == null ? 0 : busiMall.getInvestAmt().intValue());
                    giveDto.setInvestPeriod(busiMall.getInvestPeriod() == null ? 0 : busiMall.getInvestPeriod().intValue());
                    giveDto.setPeriod(busiMall.getValidDay() == null ? 0 : busiMall.getValidDay().intValue());
                    giveDto.setTransNo(SerialNoGeneratorService.generateTransNoByOrderId(orderNum, "EXCOUPON"));
                    ResultDto<CouponGiveResDto> couponResultDto = welfareService.gainCoupon(giveDto);
                    if (couponResultDto.isSuccess()) {
                        CouponGiveResDto couponGiveResDto = couponResultDto.getData();
                        //更新订单状态-兑换商品成功
                        exchange.setStatus(AppConstants.OrderExchargeStatus.STATUS_4);
                        exchange.setExchangeSerNum(couponGiveResDto.getSerialNo());
                        exchange.setProductNum(couponGiveResDto.getNo());
                        exchange.setSuccessTime(new Date());
                        updateBusiOrderExchange(exchange);
                        //商品购买个数加1
                        busiMall.setBuyNum(busiMall.getBuyNum() + 1);
                        update(busiMall);
                    } else {
                        //更新订单状态-兑换商品失败
                        exchange.setStatus(AppConstants.OrderExchargeStatus.STATUS_3);
                        updateBusiOrderExchange(exchange);
                        throw new BusinessException("coin.exchange.coupon.failed");
                    }
                } else if (AppConstants.PRODUCT_TYPE.TYPE_2.equals(busiMall.getMerchandiseType())) {
                    VoucherPublishDto publishDto = new VoucherPublishDto();
                    publishDto.setInvestAmount(busiMall.getInvestAmt() == null ? 0 : busiMall.getInvestAmt().intValue());
                    publishDto.setAccountNo(mainInfo.getCmNumber());
                    publishDto.setDays(busiMall.getInterestDay() == null ? 0 : busiMall.getInterestDay().intValue());
                    publishDto.setRate(busiMall.getInterestRate());
                    publishDto.setInvestPeriod(busiMall.getInvestPeriod() == null ? 0 : busiMall.getInvestPeriod().intValue());
                    publishDto.setPeriod(busiMall.getValidDay() == null ? 0 : busiMall.getValidDay().intValue());
                    publishDto.setOperator("SYSTEM");
                    publishDto.setSource("EXCHANGE");
                    publishDto.setInvestMaxAmount(busiMall.getAmtUpperLimit() == null ? null : busiMall.getAmtUpperLimit().intValue());
                    publishDto.setInvestMaxPeriod(busiMall.getPeriodUpperLimit() == null ? null : busiMall.getPeriodUpperLimit().intValue());
                    publishDto.setTransNo(SerialNoGeneratorService.generateTransNoByOrderId(orderNum, "EXVOUCHER"));
                    ResultDto<VoucherOprResDto> VoucherResultDto = welfareService.publishVoucher(publishDto);
                    if (VoucherResultDto.isSuccess()) {
                        VoucherOprResDto voucherOprResDto = VoucherResultDto.getData();
                        //更新订单状态-兑换商品成功
                        exchange.setStatus(AppConstants.OrderExchargeStatus.STATUS_4);
                        exchange.setExchangeSerNum(voucherOprResDto.getSerialNo());
                        exchange.setProductNum(voucherOprResDto.getNo());
                        exchange.setSuccessTime(new Date());
                        updateBusiOrderExchange(exchange);
                        //商品购买个数加1
                        busiMall.setBuyNum(busiMall.getBuyNum() + 1);
                        update(busiMall);
                    } else {
                        //更新订单状态-兑换商品失败
                        exchange.setStatus(AppConstants.OrderExchargeStatus.STATUS_3);
                        updateBusiOrderExchange(exchange);
                        throw new BusinessException("coin.exchange.voucher.failed");
                    }
                } else if (AppConstants.PRODUCT_TYPE.TYPE_3.equals(busiMall.getMerchandiseType())) {
                    //兑换预约券
                    AppointmentPublishDto  publishDto = new AppointmentPublishDto ();
                    publishDto.setAccountNo(mainInfo.getCmNumber());
                    publishDto.setPeriod(busiMall.getValidDay() == null ? 0 : busiMall.getValidDay().intValue());
                    publishDto.setOperator("SYSTEM");
                    publishDto.setSource("EXCHANGE");
                    publishDto.setRemark(busiMall.getRemark());
                    publishDto.setInvestPeriod(busiMall.getInvestPeriod().intValue());
                    publishDto.setInvestAmount(busiMall.getInvestAmt());
                    publishDto.setTransNo(SerialNoGeneratorService.generateTransNoByOrderId(orderNum, "EXAPPOINTMENT"));
                    ResultDto<AppointmentOprResDto> appointResultDto = welfareService.publishAppointment(publishDto);
                    if (appointResultDto.isSuccess()) {
                        AppointmentOprResDto appointmentOprResDto = appointResultDto.getData();
                        //更新订单状态-兑换商品成功
                        exchange.setStatus(AppConstants.OrderExchargeStatus.STATUS_4);
                        exchange.setExchangeSerNum(appointmentOprResDto.getSerialNo());
                        exchange.setProductNum(appointmentOprResDto.getNo());
                        exchange.setSuccessTime(new Date());
                        updateBusiOrderExchange(exchange);
                        //商品购买个数加1
                        busiMall.setBuyNum(busiMall.getBuyNum() + 1);
                        update(busiMall);
                    } else {
                        //更新订单状态-兑换商品失败
                        exchange.setStatus(AppConstants.OrderExchargeStatus.STATUS_3);
                        updateBusiOrderExchange(exchange);
                        throw new BusinessException("coin.exchange.bespeak.failed");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                log.error("兑换失败,兑换订单号：" + exchange.getOrderNum());
                //退还捞财币
                refundLCB(exchange,mainInfo.getCmNumber());
            }
        } else {
            //更新订单状态-消费捞财币失败
            exchange.setStatus(AppConstants.OrderExchargeStatus.STATUS_1);
            updateBusiOrderExchange(exchange);
            throw new BusinessException("coin.use.failed");
        }
        dto.setAwardType(busiMall.getMerchandiseType());
        //审计是否兑换成功
        businessReportService.sendReport(AuditMethodType.AUDIT_coin_EXCHANGE.getMethod(),orderNum);

        return dto;
    }

    @Transactional
    private int insertBusiOrderExchange(BusiOrderExchange exchange){
        return busiOrderExchangeMapper.insertBusiOrderExchange(exchange);
    }

    @Transactional
    private void updateBusiOrderExchange(BusiOrderExchange exchange){
        busiOrderExchangeMapper.updateBusiOrderExchange(exchange);
    }

    /*
     *捞财币退还
     */
    @Transactional
    private void refundLCB(BusiOrderExchange exchange,String accountNo) {
        CoinOprDto oprDto = new CoinOprDto();
        oprDto.setAccountNo(accountNo);
        oprDto.setTips("退捞财币");
        oprDto.setOrderNo(exchange.getOrderNum());
        oprDto.setSerialNo(exchange.getPaySerNum());
        oprDto.setCoin(exchange.getOrderAmt().longValue());
        oprDto.setTransNo(SerialNoGeneratorService.generateTransNoByOrderId(exchange.getOrderNum(),"REFUND"));
        ResultDto<CoinOprResultDto> resultDto = coinFacadeService.refund(oprDto);
        if(resultDto!=null){
            if(resultDto.isSuccess()){
                log.error("退还成功,退还流水：" + resultDto.getData().getRecordNum());
                //更新订单状态-兑换商品失败
                exchange.setReturnSerNum(resultDto.getData().getRecordNum());
                exchange.setStatus(AppConstants.OrderExchargeStatus.STATUS_3);
                updateBusiOrderExchange(exchange);
                throw new BusinessException("coin.exchange.voucher.failed");
            }
        }

    }

    public boolean gainCoin(Long customerId, Long coin, String tips) throws Exception {
        CustomerMainInfo mainInfo = checkExistByCustomerId(customerId);
        CoinOprDto oprDto = new CoinOprDto();
        oprDto.setAccountNo(mainInfo.getCmNumber());
        oprDto.setTips(tips);
        oprDto.setCoin(coin);
        oprDto.setTransNo(SerialNoGeneratorService.generateTransferNoByCmNum(mainInfo.getCmNumber()));
        return welfareService.gainCoin(oprDto);
    }

    public Page<ExchangeRecordDTO> exchangeRecord(Long customerId, int pageNo, int pageSize) throws Exception {
        CustomerMainInfo mainInfo = checkExistByCustomerId(customerId);
        PageHelper.startPage(pageNo, pageSize);
        List<ExchangeRecordDTO> list = busiOrderExchangeMapper.getExchangeRecords(customerId);
        com.github.pagehelper.Page<ExchangeRecordDTO> page = (com.github.pagehelper.Page<ExchangeRecordDTO>) list;
        return LaocaiUtil.convertPage(page);
    }

    public Page<BusiCoinRecordDTO> coinRecord(Long customerId, int pageNo, int pageSize) throws Exception {
        CustomerMainInfo mainInfo = checkExistByCustomerId(customerId);
        CoinRecordSearchDto searchDto = new CoinRecordSearchDto();
        searchDto.setPageNo(pageNo);
        searchDto.setPageSize(pageSize);
        searchDto.setAccountNo(mainInfo.getCmNumber());
        PageResultDto<CoinRecordDto> resultDto = coinFacadeService.getCoinRecords(searchDto);
        if (resultDto.isSuccess()) {
            List<CoinRecordDto> list = resultDto.getDataList();
            List<BusiCoinRecordDTO> dtoList = Lists.newArrayList();
            BusiCoinRecordDTO dto = null;
            for (CoinRecordDto record : list) {
                dto = new BusiCoinRecordDTO();
                BeanUtils.copyProperties(record, dto);
                dto.setDateStr(DateUtil.timeFormat(record.getCreateDate(), "yyyy-MM-dd HH:mm"));
                dtoList.add(dto);
            }
            //封装为webservice的统一分页格式
            Page<BusiCoinRecordDTO> coinRecords = new Page<BusiCoinRecordDTO>();
            coinRecords.setResults(dtoList);
            coinRecords.setPageNo(resultDto.getPageNo());
            coinRecords.setPageSize(pageSize);
            coinRecords.setTotalRecord(resultDto.getTotalSize());
            coinRecords.setTotalPage(resultDto.getTotalPage());
            return coinRecords;
        } else {
            throw new HessianRpcException("coin.record", resultDto.getMsg());
        }
    }


    public Map<String, Object> getVoucherList(Long customerId, Integer pageNo, Integer pageSize) throws Exception {
        Map<String, Object> rtnMap = Maps.newTreeMap();
        CustomerMainInfo mainInfo = checkExistByCustomerId(customerId);
        List<PayVoucherDTO> payVoucherDTOs = Lists.newArrayList();
        int voucherNum = 0;
//        //查询未使用加息券
//        getVoucherListBySortFlag(payVoucherDTOs, 1, mainInfo.getCmNumber());
//        //查询已使用、已过期加息券
//        getVoucherListBySortFlag(payVoucherDTOs, 2, mainInfo.getCmNumber());
        VoucherSearchDto searchDto = new VoucherSearchDto();
        searchDto.setAccountNo(mainInfo.getCmNumber());
        searchDto.setPageNo(pageNo);
        searchDto.setPageSize(pageSize);
        PageResultDto<VoucherDto> result = voucherFacadeService.getPageVouchersByAccountNo(searchDto);
        if (result.isSuccess()) {
            if(result!=null){
                rtnMap.put("pageNo",result.getPageNo());
                rtnMap.put("pageSize",searchDto.getPageSize());
                rtnMap.put("totalPage",result.getTotalPage());
                rtnMap.put("totalSize",result.getTotalSize());
                PayVoucherDTO payVoucherDTO = null;
                for (VoucherDto voucherDto : result.getDataList()) {
                    payVoucherDTO = new PayVoucherDTO();
                    payVoucherDTO.setVoucherNo(voucherDto.getNo());
                    payVoucherDTO.setVoucherTitle(voucherDto.getDays() == null || voucherDto.getDays() == 0 ? "加息券" : voucherDto.getDays() + "天加息券");
                    payVoucherDTO.setConditionString(voucherDto.getConditionString());
                    payVoucherDTO.setRate(voucherDto.getRate());
                    payVoucherDTO.setRateStr(NumberUtil.fortmatBigDecimal(voucherDto.getRate().multiply(new BigDecimal(100))) + "%");
                    payVoucherDTO.setFlag(AppConstants.VoucherStatus.getVoucherStatus(voucherDto.getStatus()));
                    payVoucherDTO.setVoucherMemo(getMessage("voucher.memo"));
                    payVoucherDTO.setValidDate(voucherDto.getDateString());
                    payVoucherDTOs.add(payVoucherDTO);
                }
            }

        } else {
            throw new HessianRpcException("voucher.record", result.getMsg());
        }
        rtnMap.put("result", payVoucherDTOs);
        ResultDto<VoucherNumDto> resultDto = voucherFacadeService.getVoucherNum(mainInfo.getCmNumber());
        if (resultDto.isSuccess()) {
             voucherNum = resultDto.getData().getAvailNum();
        } else {
            throw new BusinessException("查询加息券数量查询失败");
        }
        rtnMap.put("voucherNum", voucherNum);
        return rtnMap;
    }

    /**
     * 查询用户加息券
     *
     * @param payVoucherDTOs 要返回的加息券集合
     * @param sortFlag       1.按未使用升序排  2.按已使用，已过期降序排
     * @param cmNumber
     * @return
     */
    public void getVoucherListBySortFlag(List<PayVoucherDTO> payVoucherDTOs, Integer sortFlag, String cmNumber) {
        VoucherSearchDto searchDto = new VoucherSearchDto();
        searchDto.setAccountNo(cmNumber);
        searchDto.setSortFlag(sortFlag);
        ResultDto<List<VoucherDto>> result = voucherFacadeService.getVouchersByAccountNo(searchDto);
        if (result.isSuccess()) {
            PayVoucherDTO payVoucherDTO = null;
            for (VoucherDto voucherDto : result.getData()) {
                payVoucherDTO = new PayVoucherDTO();
                payVoucherDTO.setVoucherNo(voucherDto.getNo());
                payVoucherDTO.setVoucherTitle(voucherDto.getDays() == null || voucherDto.getDays() == 0 ? "加息券" : voucherDto.getDays() + "天加息券");
                payVoucherDTO.setConditionString(voucherDto.getConditionString());
                payVoucherDTO.setRate(voucherDto.getRate());
                payVoucherDTO.setRateStr(NumberUtil.fortmatBigDecimal(voucherDto.getRate().multiply(new BigDecimal(100))) + "%");
                payVoucherDTO.setFlag(AppConstants.VoucherStatus.getVoucherStatus(voucherDto.getStatus()));
                payVoucherDTO.setVoucherMemo(getMessage("voucher.memo"));
                payVoucherDTO.setValidDate(voucherDto.getDateString());
                payVoucherDTOs.add(payVoucherDTO);
            }
        } else {
            throw new HessianRpcException("voucher.record", result.getMsg());
        }
    }

    public Map<String, Object> getBespeakList(Long customerId, Integer pageNo, Integer pageSize) throws Exception {
        Map<String, Object> rtnMap = Maps.newTreeMap();
        CustomerMainInfo mainInfo = checkExistByCustomerId(customerId);
        List<PayBespeakDTO> payBespeakDTOs = Lists.newArrayList();
        AppointmentSearchDto searchDto = new AppointmentSearchDto();
        searchDto.setPageNo(pageNo);
        searchDto.setPageSize(pageSize);
        searchDto.setAccountNo(mainInfo.getCmNumber());
        try{
            PageResultDto<AppointmentDto> result = appointmentFacadeService.getPageAppointmentsByAccountNo(searchDto);
            if (result.isSuccess()) {
                rtnMap.put("pageNo", result.getPageNo());
                rtnMap.put("pageSize", searchDto.getPageSize());
                rtnMap.put("totalPage", result.getTotalPage());
                rtnMap.put("totalSize", result.getTotalSize());
                PayBespeakDTO payBespeakDTO;
                for (AppointmentDto appointmentDto : result.getDataList()) {
                    payBespeakDTO = new PayBespeakDTO();
                    payBespeakDTO.setBespeakNo(appointmentDto.getNo());
                    payBespeakDTO.setBespeakTitle("预约券");
                    payBespeakDTO.setConditionString(appointmentDto.getPeriod() == 0 ? "永久有效" : appointmentDto.getConditionString());
                    payBespeakDTO.setFlag(AppConstants.VoucherStatus.getVoucherStatus(appointmentDto.getStatus()));
                    payBespeakDTO.setBespeakMemo("出借金额≥" + appointmentDto.getInvestAmount() + "元, 出借期限≥" + appointmentDto.getInvestPeriod() +"天\n" + getMessage("bespeak.memo"));
                    payBespeakDTO.setValidDate(appointmentDto.getPeriod());
                    payBespeakDTOs.add(payBespeakDTO);
                }
                rtnMap.put("result", payBespeakDTOs);
            } else {
                throw new HessianRpcException("bespeak.record", result.getMsg());
            }
        }catch (Exception e){
            log.error(e.getMessage());
            throw new BusinessException("查询预约券失败");
        }
        try{
            ResultDto<AppointmentNumDto> resultDto = appointmentFacadeService.getAppointmentNum(mainInfo.getCmNumber(), new BigDecimal(0), 0);
            if (resultDto.isSuccess()) {
                Integer availNum = resultDto.getData().getAvailNum();
                rtnMap.put("bespeakNum", availNum);
            } else {
                throw new BusinessException("查询预约券数量失败");
            }
        }catch (Exception e){
            log.error(e.getMessage());
            throw new BusinessException("查询预约券数量失败");
        }
        return rtnMap;
    }

    @Transactional(noRollbackFor = BusinessException.class)
    public void lcCoin2GuaGuaKa(Long customerId) throws Exception {
        CustomerMainInfo mainInfo = checkExistByCustomerId(customerId);
        //产品表取出兑换刮刮卡消耗的捞财币数量
        BusiMall search = new BusiMall();
        search.setMerchandiseType("3");
        List<BusiMall> busiMalls = getBusiMallMapper().select(search);
        if(busiMalls.size()==0){
            throw new BusinessException("product.not.exist");
        }
        BusiMall busiMall = busiMalls.get(0);
        if (busiMall == null) {
            throw new BusinessException("product.not.exist");
        }

        //查询用户捞财币余额
        Long coinBalance = getCoinBalance(mainInfo.getCmNumber());
        //余额是否足够此次购买
        if (coinBalance.compareTo(busiMall.getBuyAmt().longValue()) == -1) {
            throw new BusinessException("coin.notEnough");
        }
        //下订单
        BusiOrderExchange exchange = new BusiOrderExchange();
        String orderNum = busiOrderService.buildCeNumber("C", "6666", customerId);
        exchange.setOrderNum(orderNum);
        exchange.setCustomerId(customerId);
        exchange.setOrderAmt(busiMall.getBuyAmt());
        exchange.setAmtType(busiMall.getAmtType());
        exchange.setProductId(busiMall.getId());
        exchange.setProductType(busiMall.getMerchandiseType());
        exchange.setOrderTime(new Date());
        exchange.setStatus(AppConstants.OrderExchargeStatus.STATUS_0);
        int num = insertBusiOrderExchange(exchange);
        if (num != 1) {
            log.error("保存兑换订单失败，客户号：" + customerId + "，orderId：" + orderNum);
            throw new BusinessException("coin.exchange.save.failed");
        }
        //调账户，扣捞财币
        CoinOprDto oprDto = new CoinOprDto();
        oprDto.setAccountNo(mainInfo.getCmNumber());
        oprDto.setTips("兑换" + busiMall.getMerchandiseName());
        oprDto.setOrderNo(orderNum);
        oprDto.setCoin(busiMall.getBuyAmt().longValue());
        oprDto.setDetails("");
        oprDto.setTransNo(SerialNoGeneratorService.generateTransNoByOrderId(orderNum,"CONSUMECOIN"));
        ResultDto<CoinOprResultDto> resultDto = coinFacadeService.consumeCoin(oprDto);
        if (resultDto.isSuccess()) {
            CoinOprResultDto coinOprResultDto = resultDto.getData();
            //更新订单状态-消费捞财币成功
            exchange.setStatus(AppConstants.OrderExchargeStatus.STATUS_2);
            exchange.setPaySerNum(coinOprResultDto.getRecordNum());
            updateBusiOrderExchange(exchange);
            //领刮刮卡
            LotteryQualificationDto ggkDto = new LotteryQualificationDto();
            ggkDto.setCmNumber(mainInfo.getCmNumber());
            ggkDto.setSource("EXCHANGE");
            ggkDto.setLotteryQuantity(1);
            ggkDto.setTiggerOrder(orderNum);
            SysParameter reLotteryCode = sysParameterService.findOneByPrType("reLotteryCode");
            if (reLotteryCode != null) {
                int lotteryType = Integer.parseInt(reLotteryCode.getPrValue());
                ggkDto.setLotteryCode(lotteryType);
                ResultDto<String> lotteryResultDto = lotteryQualificationFacadeService.addLotteryQualification(ggkDto);
                if (resultDto.isSuccess()) {
                    //更新订单状态-兑换商品成功
                    exchange.setStatus(AppConstants.OrderExchargeStatus.STATUS_4);
                    exchange.setSuccessTime(new Date());
                    updateBusiOrderExchange(exchange);
                } else {
                    //更新订单状态-兑换商品失败
                    exchange.setStatus(AppConstants.OrderExchargeStatus.STATUS_3);
                    updateBusiOrderExchange(exchange);
                    throw new HessianRpcException("lotteryCode.exchange", resultDto.getMsg());
                }
            } else {
                throw new BusinessException("lotteryCode.not.config");
            }

        } else {
            //更新订单状态-消费捞财币失败
            exchange.setStatus(AppConstants.OrderExchargeStatus.STATUS_1);
            updateBusiOrderExchange(exchange);
            throw new BusinessException("coin.use.failed");
        }
    }

    public static String getOverTimeStr(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);//获取年份
        int month=cal.get(Calendar.MONTH);//获取月份
        int day=cal.get(Calendar.DATE);//获取日
        int hour=cal.get(Calendar.HOUR);//小时
        int minute=cal.get(Calendar.MINUTE);//分
        int second=cal.get(Calendar.SECOND);//秒
        if(compareDate(new Date(year - 1900, 6 - 1, 1, 00, 00, 00), new Date(year - 1900, 6 - 1, 7, 23, 59, 59),
                new Date())){  //是否在3月8号-6月7号之间
            return year+"/6/7 23:59:59";
        }else if(compareDate(new Date(year - 1900, 9 - 1, 1, 00, 00, 00), new Date(year - 1900, 9 - 1, 7, 23, 59, 59),
                new Date())){  //是否在6月8号-9月7号之间
            return year+"/9/7 23:59:59";

        }else if(compareDate(new Date(year - 1900, 12 - 1, 1, 00, 00, 00), new Date(year - 1900, 12 - 1, 7, 23, 59, 59),
                new Date())){  //是否在9月8号-12月7号之间
            return year+"/12/7 23:59:59";

        }else if(compareDate(new Date(year - 1900, 3 - 1, 1, 00, 00, 00), new Date(year - 1900, 3 - 1, 7, 23, 59, 59),
                new Date())){  //是否在12月8号-3月7号之间
            return year+"/3/7 23:59:59";

        }
        return "";
    }

    private  String getRemindTimeStr(){
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);//获取年份
        int month=cal.get(Calendar.MONTH);//获取月份
        int day=cal.get(Calendar.DATE);//获取日
        int hour=cal.get(Calendar.HOUR);//小时
        int minute=cal.get(Calendar.MINUTE);//分
        int second=cal.get(Calendar.SECOND);//秒
        if(compareRemindDate(new Date(year - 1900, 1 - 1, 1, 00, 00, 00), new Date(year - 1900, 3 - 1, 1, 00, 00, 00),
                cal.getTime())){
            return year+"/3/1"+";"+year+"/3/7";
        } else if(compareRemindDate(new Date(year - 1900, 3 - 1, 8, 00, 00, 00), new Date(year - 1900, 6 - 1, 1, 00, 00, 00),
                cal.getTime())){
            return year+"/6/1"+";"+year+"/6/7";
        }else if(compareRemindDate(new Date(year - 1900, 6 - 1, 8, 00, 00, 00), new Date(year - 1900, 9 - 1, 1, 00, 00, 00),
                cal.getTime())){
            return year+"/9/1"+";"+year+"/9/7";
        }else if(compareRemindDate(new Date(year-1900, 9-1, 8, 00, 00, 00), new Date(year-1900, 12-1, 1, 00, 00, 00),
                cal.getTime())){
            return year+"/12/1"+";"+year+"/12/7";
        }
        else if(compareRemindDate(new Date(year-1900, 12-1, 8, 00, 00, 00), new Date(year+1-1900, 1-1, 1, 00, 00, 00),
                cal.getTime())){
            return (year+1)+"/3/1"+";"+(year+1)+"/3/7";
        }
        return "";
    }

    // 判断时间是否在时间段内
    public static boolean compareDate(Date start, Date end, Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("start:"+format.format(start));
        System.out.println("end:"+format.format(end));
        System.out.println("date:"+format.format(date));

        if (date.getTime() >= start.getTime() && date.getTime() <= end.getTime()) {
            return true;
        }
        return false;
    }

    // 判断时间是否在时间段内
    public static boolean compareRemindDate(Date start, Date end, Date date) {
        if (date.getTime() >= start.getTime() && date.getTime() < end.getTime()) {
            return true;
        }
        return false;
    }

    public BusiOrderExchange  getBusiOrderExchangeInfo(String orderNum){
        return  busiOrderExchangeMapper.getBusiOrderExchangeInfo(orderNum);
    }


}
