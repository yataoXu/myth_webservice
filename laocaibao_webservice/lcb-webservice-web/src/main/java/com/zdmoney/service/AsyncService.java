package com.zdmoney.service;

import com.zdmoney.data.agent.api.dto.bc.BusinessReportDto;
import com.zdmoney.integral.api.dto.appointment.AppointmentPublishDto;
import com.zdmoney.integral.api.dto.coupon.CouponGiveDto;
import com.zdmoney.integral.api.dto.coupon.CouponPublishDto;
import com.zdmoney.integral.api.dto.voucher.VoucherPublishDto;
import com.zdmoney.message.api.dto.log.LogDataReqDto;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.financePlan.BusiOrderSub;
import com.zdmoney.models.product.BusiProductSub;
import com.zdmoney.models.sys.SysRequestLog;

/**
 * Created by gaol on 2017/2/8
 **/
public interface AsyncService {

    void saveAccessLog(LogDataReqDto reqDto);

    /**
     * 发加息券
     * @param publishDto
     */
    void publishVoucher(VoucherPublishDto publishDto);

    /**
     * 发红包
     * @param giveDto
     */
    void gainCoupon(CouponGiveDto giveDto);

    /**
     * 发预约券
     * @param publishDto
     */
    void publishAppointment(AppointmentPublishDto publishDto);

    /**
     * 审计调用
     * @param brDto
     */
    void commonAuditInfo(BusinessReportDto brDto);

    /**
     * 注册送红包
     */
    void publishCouponBySource(CouponPublishDto giveDto);

    /**
     * 异步-通知标的系统下单结果
     */
    void nodifySubjectByOrderPay(CustomerMainInfo customerMainInfo, BusiOrderSub order,
                                 BusiProductSub product,String transferNo);

    /**
     * 保存请求日志
     * @param reqLog
     */
    void saveReqLog(SysRequestLog reqLog);
}
