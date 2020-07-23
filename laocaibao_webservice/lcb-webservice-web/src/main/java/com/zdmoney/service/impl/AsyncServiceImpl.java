package com.zdmoney.service.impl;

import com.zdmoney.constant.AppConstants;
import com.zdmoney.data.agent.api.dto.bc.BusinessReportDto;
import com.zdmoney.data.agent.api.facade.IDataAgentBcFacadeService;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.integral.api.common.dto.ResultDto;
import com.zdmoney.integral.api.dto.appointment.AppointmentOprResDto;
import com.zdmoney.integral.api.dto.appointment.AppointmentPublishDto;
import com.zdmoney.integral.api.dto.coupon.CouponGiveDto;
import com.zdmoney.integral.api.dto.coupon.CouponGiveResDto;
import com.zdmoney.integral.api.dto.coupon.CouponPublishDto;
import com.zdmoney.integral.api.dto.coupon.CouponResDto;
import com.zdmoney.integral.api.dto.voucher.VoucherOprResDto;
import com.zdmoney.integral.api.dto.voucher.VoucherPublishDto;
import com.zdmoney.integral.api.facade.IAppointmentFacadeService;
import com.zdmoney.integral.api.facade.ICouponFacadeService;
import com.zdmoney.integral.api.facade.IVoucherFacadeService;
import com.zdmoney.message.api.common.dto.MessageResultDto;
import com.zdmoney.message.api.dto.log.LogDataReqDto;
import com.zdmoney.message.api.dto.log.LogDataRspDto;
import com.zdmoney.message.api.facade.ILogFacadeService;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.financePlan.BusiOrderSub;
import com.zdmoney.models.product.BusiProductSub;
import com.zdmoney.models.sys.SysRequestLog;
import com.zdmoney.service.AsyncService;
import com.zdmoney.service.SerialNoGeneratorService;
import com.zdmoney.service.SysLogService;
import com.zdmoney.service.subject.SubjectService;
import com.zdmoney.vo.trade.TransferVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by gaol on 2017/2/8
 **/
@Service
@Slf4j
public class AsyncServiceImpl implements AsyncService{

    @Autowired
    private ILogFacadeService logFacadeService;

    @Autowired
    private IVoucherFacadeService voucherFacadeService;

    @Autowired
    private ICouponFacadeService couponFacadeService;

    @Autowired
    private IAppointmentFacadeService appointmentFacadeService;

    @Autowired
    private IDataAgentBcFacadeService dataAgentBcFacadeService;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private SysLogService sysLogService;

    @Override
    @Async("logExecutor")
    public void saveAccessLog(LogDataReqDto reqDto) {
        if (reqDto == null) return;
        MessageResultDto<LogDataRspDto> resultDto = logFacadeService.accessLog(reqDto);
        if (!resultDto.isSuccess()) {
            log.error("接口用时统计日志入库失败, 类和方法为:" + reqDto.getMethodName());
        }
    }

    @Override
    @Async("taskExecutor")
    public void publishVoucher(VoucherPublishDto publishDto) {
        publishDto.setTransNo(SerialNoGeneratorService.generateTransNo(publishDto.getAccountNo()));
        ResultDto<VoucherOprResDto> resultDto = voucherFacadeService.publishVoucher(publishDto);
        if(resultDto.isSuccess()){
            log.info("cmNumber:" + publishDto.getAccountNo() + ",已连续签到7天,额外赠送:" + publishDto.getRate() + "%的加息券一张");
        }else{
            throw new BusinessException("签到赠送加息券失败");
        }
    }

    @Override
    @Async("taskExecutor")
    public void gainCoupon(CouponGiveDto giveDto) {
        giveDto.setTransNo(SerialNoGeneratorService.generateTransNo(giveDto.getAccountNo()));
        ResultDto<CouponGiveResDto> resultDto = couponFacadeService.gainCoupon(giveDto);
        if(resultDto.isSuccess()){
            log.info("cmNumber:" + giveDto.getAccountNo() + ",已连续签到15天,额外赠送:" + giveDto.getAmount() + "元红包一个");
        }else{
            throw new BusinessException("签到赠送红包失败");
        }
    }

    @Override
    @Async("taskExecutor")
    public void publishAppointment(AppointmentPublishDto publishDto) {
        String transNo = SerialNoGeneratorService.generateTransNo(publishDto.getAccountNo());
        publishDto.setTransNo(transNo);
        ResultDto<AppointmentOprResDto> resultDto = appointmentFacadeService.publish(publishDto);
        if(resultDto.isSuccess()){
            log.info("cmNumber:" + publishDto.getAccountNo() + ",已连续签到30天,额外赠送一张预约券");
        }else{
            throw new BusinessException("签到赠送预约券失败");
        }
    }

    /**
     * 注册送红包
     */
    @Async("taskExecutor")
    public void publishCouponBySource(CouponPublishDto giveDto) {
        try {
            ResultDto<List<CouponResDto>> resultDto = couponFacadeService.publishCouponBySource(giveDto);
            if(!resultDto.isSuccess()){
                log.error("cmNumber:｛｝", giveDto.getAccountNo() + ",注册赠送红包失败",resultDto.getMsg());
                throw new BusinessException("注册赠送红包失败");
            }
            log.info("cmNumber:{}" , giveDto.getAccountNo() + ",注册赠送红包成功");
        } catch (Exception e) {
            log.error("[cmNumber: {}] 调用积分接口发送红包出现异常，{}", giveDto.getAccountNo(), e);
        }
    }

    @Override
    @Async("taskExecutor")
    public void commonAuditInfo(BusinessReportDto brDto) {
        com.zdmoney.data.agent.api.base.ResultDto<String> stringResultDto = dataAgentBcFacadeService.businessFeatureNotice(brDto);
        if(!stringResultDto.isSuccess()){
            throw new BusinessException("调用审计失败: "+stringResultDto.getMsg());
        }
        log.info("调用审计成功,systemNo:{},featureNo:{} ",brDto.getSystemNo(),brDto.getFeatureNo());
    }

    /**
     * 异步-通知标的系统下单结果
     */
    public void nodifySubjectByOrderPay(CustomerMainInfo customerMainInfo, BusiOrderSub order,
                                        BusiProductSub product,String transferNo) {
        //非转让产品
        if (AppConstants.OrderTransferStatus.ORDER_NORMAL.equals(order.getTransferType())) {
            //优选产品
            if (AppConstants.OrderProductType.COMMON.equals(order.getProductType())) {
                subjectService.signProductAgreement(order, product, customerMainInfo);

            }
            if(AppConstants.OrderProductType.FINANCE_PLAN.equals(order.getProductType())){
                subjectService.addMatchInvestOrder(order,product,customerMainInfo);
            }
            if(AppConstants.OrderProductType.CONTRACT.equals(order.getProductType())
                    ||AppConstants.OrderProductType.PERSONAL.equals(order.getProductType())
                    ||AppConstants.OrderProductType.FINANCE_PLAN_SUB.equals(order.getProductType())){
                subjectService.notifyCommonProductSubject(order, customerMainInfo);
            }
        }
        //转让产品
        else{
            TransferVo transferVo=new TransferVo();
            transferVo.setCmIdnum(customerMainInfo.getCmIdnum());
            transferVo.setCustomerName(customerMainInfo.getCmRealName());
            transferVo.setCustomerNo(customerMainInfo.getCmNumber());
            transferVo.setOrderNo(order.getOrderId());
            transferVo.setSubjectNo(order.getSubjectNo());
            transferVo.setCustomerPhone(customerMainInfo.getCmCellphone());
            subjectService.notifyTransferProductSubject(transferNo, order, product, customerMainInfo,null);
        }
    }

    @Override
    @Async("logExecutor")
    public void saveReqLog(SysRequestLog reqLog) {
        reqLog.setCreateTime(new Date());
        if (reqLog.getMethodCode().startsWith("43") || reqLog.getMethodCode().startsWith("zdpay") || reqLog.getMethodCode().startsWith("channel")) {
            sysLogService.saveSysRequestLog(reqLog);
        }
    }
}
