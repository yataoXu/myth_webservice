package com.zdmoney.service.welfare;

import com.google.common.collect.Lists;
import com.thoughtworks.xstream.core.BaseException;
import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.enm.SerialNoType;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.integral.api.common.dto.ResultDto;
import com.zdmoney.integral.api.dto.appointment.AppointmentDto;
import com.zdmoney.integral.api.dto.appointment.AppointmentOprResDto;
import com.zdmoney.integral.api.dto.appointment.AppointmentPublishDto;
import com.zdmoney.integral.api.dto.cash.CashDto;
import com.zdmoney.integral.api.dto.cash.CashOpenDto;
import com.zdmoney.integral.api.dto.cash.enm.CashPublishSource;
import com.zdmoney.integral.api.dto.coin.CoinOprDto;
import com.zdmoney.integral.api.dto.coin.CoinOprResultDto;
import com.zdmoney.integral.api.dto.coupon.*;
import com.zdmoney.integral.api.dto.voucher.VoucherDto;
import com.zdmoney.integral.api.dto.voucher.VoucherOprResDto;
import com.zdmoney.integral.api.dto.voucher.VoucherPublishDto;
import com.zdmoney.integral.api.facade.*;
import com.zdmoney.integral.api.utils.DateUtils;
import com.zdmoney.mapper.BusiCashRecordMapper;
import com.zdmoney.mapper.BusiCashTicketConfigMapper;
import com.zdmoney.models.BusiCashRecord;
import com.zdmoney.models.BusiCashTicketConfig;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.financePlan.BusiOrderSub;
import com.zdmoney.models.order.BusiOrder;
import com.zdmoney.service.CustomerMainInfoService;
import com.zdmoney.service.SerialNoGeneratorService;
import com.zdmoney.utils.JSONUtils;
import com.zdmoney.utils.LaocaiUtil;
import com.zdmoney.web.dto.PacketDTO;
import com.zdmoney.webservice.api.dto.customer.CashCouponDto;
import com.zdmoney.webservice.api.facade.IManagerFacadeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class WelfareService {

    @Autowired
    private ICouponFacadeService couponFacadeService;

    @Autowired
    private CustomerMainInfoService customerMainInfoService;

    @Autowired
    private ConfigParamBean configParamBean;

    @Autowired
    private IAppointmentFacadeService appointmentFacadeService;

    @Autowired
    private IVoucherFacadeService voucherFacadeService;

    @Autowired
    private ICoinFacadeService coinFacadeService;

    @Autowired
    private ICashFacadeService cashFacadeService;

    @Autowired
    private BusiCashRecordMapper cashRecordMapper;

    @Autowired
    private BusiCashTicketConfigMapper cashTicketConfigMapper;

    @Autowired
    private IManagerFacadeService managerFacadeService;

    //查询有效红包
    public List<PacketDTO> gainRedPacket(String sessionToken,Integer investPeriod) {
        List<PacketDTO> redPacketDTOs = Lists.newArrayList();
        String cmNumber = LaocaiUtil.sessionToken2CmNumber(sessionToken, configParamBean.getUserTokenKey());
        CustomerMainInfo customerMainInfo = customerMainInfoService.findOneByCmNumber(cmNumber);
        if(customerMainInfo != null){
            CouponSearchDto dto = new CouponSearchDto();
            dto.setStatus("AVAIL");
            dto.setInvestPeriod(investPeriod);
            dto.setCurDate(new Date());
            dto.setAccountNo(cmNumber);
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
        }
        return redPacketDTOs;
    }

    /**
     * 回退预约券
     * @param cmNumber
     * @param orderNo
     * @param consumeAppointmnetSerialNo
     * @return
     */
    public String refundAppointment(String cmNumber, String orderNo, String consumeAppointmnetSerialNo) {
        log.info("调用账户系统--->回退预约券开始，订单号【{}】", orderNo);
        AppointmentDto dto = new AppointmentDto();
        dto.setAccountNo(cmNumber);
        dto.setOrderNo(orderNo);
        dto.setSerialNo(consumeAppointmnetSerialNo);
        dto.setTransNo(SerialNoGeneratorService.generateTransNoByOrderId(orderNo, "REFUND"));
        ResultDto<AppointmentOprResDto> resultDto = appointmentFacadeService.refund(dto);
        if (resultDto.isSuccess()) {
            log.info("调用账户系统--->回退预约券结束，订单号【{}】", orderNo);
            return resultDto.getData().getSerialNo();
        } else {
            log.info("调用账户系统--->回退预约券失败，订单号【{}】，失败原因【{}】", orderNo,resultDto.getMsg());
            throw new BusinessException("回退预约券失败");
        }
    }

    /**
     * 回退加息券
     * @param cmNumber
     * @param orderNo
     * @param consumeVoucherSerialNo
     * @return
     */
    public String refundVoucher(String cmNumber, String orderNo, String consumeVoucherSerialNo) {
        log.info("调用账户系统--->回退加息券开始，订单号【{}】", orderNo);
        VoucherDto dto = new VoucherDto();
        dto.setAccountNo(cmNumber);
        dto.setOrderNo(orderNo);
        dto.setSerialNo(consumeVoucherSerialNo);
        ResultDto<VoucherOprResDto> resultDto = voucherFacadeService.refundVoucher(dto);
        if (resultDto.isSuccess()) {
            log.info("调用账户系统--->退还加息券结束，订单号【{}】", orderNo);
            return resultDto.getData().getSerialNo();
        } else {
            log.info("调用账户系统--->回退加息券失败，订单号【{}】，失败原因【{}】", orderNo,resultDto.getMsg());
            throw new BusinessException("回退加息券失败");
        }
    }

    /**
     * 发送加息券
     * @param publishDto
     */
    public ResultDto<VoucherOprResDto> publishVoucher(VoucherPublishDto publishDto) {
        ResultDto<VoucherOprResDto> resultDto = voucherFacadeService.publishVoucher(publishDto);
        return resultDto;
    }

    /**
     * 发送红包
     * @param giveDto
     */
    public ResultDto<CouponGiveResDto> gainCoupon(CouponGiveDto giveDto) {
        ResultDto<CouponGiveResDto> resultDto = couponFacadeService.gainCoupon(giveDto);
        return resultDto;
    }

    /**
     * 根据规则发送红包
     *
     * @param couponPublishDto
     * @return
     */
    public ResultDto<List<CouponResDto>> publishCouponBySource(CouponPublishDto couponPublishDto) {
        ResultDto<List<CouponResDto>> resultDto = couponFacadeService.publishCouponBySource(couponPublishDto);
        return resultDto;
    }

    /**
     * 发送预约券
     * @param publishDto
     */
    public ResultDto<AppointmentOprResDto> publishAppointment(AppointmentPublishDto publishDto) {
        ResultDto<AppointmentOprResDto> resultDto = appointmentFacadeService.publish(publishDto);
        return resultDto;
    }

    /**
     * 发送捞财币
     * @param cod
     */
    public Boolean gainCoin(CoinOprDto cod) {
        Boolean flag = true;
        ResultDto<CoinOprResultDto> resultDto = coinFacadeService.gainCoin(cod);
        if(resultDto.isSuccess()){
            log.info("cmNumber:" + cod.getAccountNo() +",获得:" + cod.getCoin() + "个捞财币");
        }else{
            log.error("发送捞财币失败:" + resultDto.getMsg());
            flag = false;
            throw new BusinessException("发送捞财币失败");
        }
        return flag;
    }

    /**
     * 批量发送捞财币
     * @param coinOprDtoList
     * @return
     */
    public ResultDto<List<CoinOprResultDto>> batchGainCoin( List<CoinOprDto> coinOprDtoList){
        ResultDto<List<CoinOprResultDto>> resultDto = coinFacadeService.batchGainCoin("TN_"+System.currentTimeMillis(), coinOprDtoList);
        return resultDto;
    }


    /**
     * 注册送现金券
     * @param cmNumber
     * @return
     */
    @Async
    public void sendRegistCash(String cmNumber) {

        List<BusiCashTicketConfig> cashTicketConfigList = cashTicketConfigMapper.queryBusiCashTickets(AppConstants.CASH_TYPE.CASH_REG);
        if (CollectionUtils.isEmpty(cashTicketConfigList)) {
            log.info("注册送现金券未配置，请检查");
            return;
        }
        List<CashDto> cashDtos = new ArrayList<>();
        for (BusiCashTicketConfig cashTicketConfig : cashTicketConfigList) {
            CashDto cashDto = new CashDto();
            cashDto.setAccountNo(cmNumber);
            cashDto.setPublishSource(CashPublishSource.REG);
            cashDto.setAmount(new BigDecimal(cashTicketConfig.getAmount()));
            cashDto.setInvestMin(new BigDecimal(cashTicketConfig.getInvestMinAmount()));
            cashDto.setPeriod(cashTicketConfig.getPeriod().intValue());
            cashDto.setInvestPeriodMin(cashTicketConfig.getInvestMinPeriod().intValue());
            if(cashTicketConfig.getInvestMaxPeriod()!=null){
                cashDto.setInvestPeriodMax(cashTicketConfig.getInvestMaxPeriod().intValue());
            }

            cashDtos.add(cashDto);
        }
        try {
            log.info("调用账户系统--->注册送现金券开始，用户遍号【{}】", cmNumber);
            com.zdmoney.integral.api.common.dto.ResultDto resultDto = cashFacadeService.publishCash(cashDtos, SerialNoGeneratorService.commonGenerateTransNo(SerialNoType.REGIST_CASH, cmNumber));
            if (!resultDto.isSuccess()) {
                log.error("调用账户系统--->注册送现金券失败! 用户编号为:{},错误原因:{}", cmNumber, resultDto.getMsg());
                return;
            }
        } catch (Exception e) {
            log.error("调用账户系统--->，注册送现金券异常，cmNumber【{}】", cmNumber, e);
            throw new BusinessException("调用账户系统--->，注册送现金券异常:" + e);
        }

        BusiCashRecord record = new BusiCashRecord();
        record.setCashCouponType(AppConstants.CASH_TYPE.CASH_REG);
        record.setCmNumber(cmNumber);
        record.setCreateDate(new Date());
        record.setModifyDate(new Date());
        record.setStatus("1");
        record.setCashCouponId("0");
        record.setExpireDate(new Date());
        cashRecordMapper.saveBusiCashRecord(record);
    }


    /**
     * 投资激活现金券
     * @param cmNumber
     * @return
     */
    @Async
    public void sendInvestCash(String cmNumber,BusiOrderSub order) {
        //转让不送现金券
        if("1".equals(order.getTransferType())){
            return;
        }
        log.info("调用账户系统--->投资激活现金券开始，用户遍号【{}】", cmNumber);
        CashOpenDto cashDto = new CashOpenDto();
        cashDto.setAccountNo(cmNumber);
        cashDto.setOrderNo(order.getOrderId());
        cashDto.setInvestAmount(order.getOrderAmt());
        cashDto.setInvestPeriod(order.getCloseDays().intValue());
        try{
            com.zdmoney.integral.api.common.dto.ResultDto  resultDto = cashFacadeService.openCash(cashDto);
            if (!resultDto.isSuccess()) {
                log.error("调用账户系统--->投资激活现金券失败! 用户编号为:{},错误原因:{}", cmNumber, resultDto.getMsg());
                return;
            }
        }catch (Exception e) {
            log.error("调用账户系统--->，投资激活现金券异常，cmNumber【{}】", cmNumber, e);
            throw new BusinessException("调用账户系统--->，投资激活现金券异常:"+e);
        }

        BusiCashRecord record  = new BusiCashRecord();
        record.setCashCouponType(AppConstants.CASH_TYPE.CASH_INVEST);
        record.setCmNumber(cmNumber);
        record.setCreateDate(new Date());
        record.setModifyDate(new Date());
        record.setStatus("1");
        record.setCashCouponId("0");
        record.setExpireDate(new Date());
        cashRecordMapper.saveBusiCashRecord(record);
    }

    /**
     *转让交割送现金券
     */
    @Async
    public void sendTransferCash(BigDecimal transferPrice,Long transferId) {
        CustomerMainInfo customerMainInfo = customerMainInfoService.findAuthCustomerById(transferId);
        if (customerMainInfo==null){
            throw new BusinessException("transferId对应的用户不存在");
        }
        CashCouponDto cashCouponDto = new CashCouponDto();
        cashCouponDto.setCmNumber(customerMainInfo.getCmNumber());
        cashCouponDto.setCashType("2");
        cashCouponDto.setRepayAmt(transferPrice);
        cashCouponDto.setRepayDate(new Date());
        managerFacadeService.sendRepayCashCoupon(cashCouponDto);
    }
}
