package com.zdmoney.service.impl;

import com.google.common.collect.Maps;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.mapper.order.BusiOrderIntegralMapper;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.financePlan.BusiOrderSub;
import com.zdmoney.models.order.BusiOrderIntegral;
import com.zdmoney.models.order.BusiOrderTemp;
import com.zdmoney.service.BusiOrderIntegralService;
import com.zdmoney.service.welfare.WelfareService;
import com.zdmoney.utils.MailUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class BusiOrderIntegralServiceImpl implements BusiOrderIntegralService {

    @Autowired
    BusiOrderIntegralMapper busiOrderIntegralMapper;

    @Autowired
    private WelfareService welfareService;

    public int insert(BusiOrderIntegral record) {
        return busiOrderIntegralMapper.insert(record);
    }

    public BusiOrderIntegral selectByCondition(Map<String, String> map) {
        return busiOrderIntegralMapper.selectByCondition(map);
    }

    public int updateByCondition(BusiOrderIntegral record) {
        return busiOrderIntegralMapper.updateByCondition(record);
    }

    /**
     * 回退用户预约券-异步处理
     *
     * @param busiOrderIntegral
     * @param mainInfo
     * @param busiOrder
     */
    @Async
    public void refundAppointment(BusiOrderIntegral busiOrderIntegral, CustomerMainInfo mainInfo, BusiOrderSub busiOrder) {
        if (StringUtils.isNotBlank(busiOrderIntegral.getAppointmentSerialNo()) && StringUtils.isBlank(busiOrderIntegral.getAppointmentRefundSerialNo())) {
            log.info("异步退还预约券开始，订单号：【{}】", busiOrder.getId());
            try {
                String refundConsumeNo = welfareService.refundAppointment(mainInfo.getCmNumber(), busiOrder.getOrderId(), busiOrderIntegral.getAppointmentSerialNo());
                if (StringUtils.isNotBlank(refundConsumeNo)) {
                    busiOrderIntegral.setAppointmentRefundSerialNo(refundConsumeNo);
                    int num = busiOrderIntegralMapper.updateByCondition(busiOrderIntegral);
                    if (num == 1) {
                        log.info("异步退还预约券结束，订单号：【{}】", busiOrder.getId());
                    } else {
                        log.info("异步退还预约券失败，订单号：【{}】", busiOrder.getId());
                        throw new BusinessException("退还预约券失败，更新消费流水失败");
                    }
                } else {
                    log.info("异步退还预约券失败，订单号：【{}】，退还预约券操作未返回流水信息", busiOrder.getId());
                    throw new BusinessException("退还预约券失败，未生成预约券回退流水");
                }
            } catch (Exception e) {
                log.error("回退预约券失败：订单号【{}】", busiOrder.getId(), e);
                MailUtil.sendMail("回退预约券失败", "订单ID" + busiOrder.getId() + ",失败原因：" + e);
            }
        }
    }

    @Async
    public void refundVoucher(String cumNumber, BusiOrderSub busiOrder) {
        Map<String, String> map = Maps.newTreeMap();
        map.put("orderNo", busiOrder.getId().toString());
        BusiOrderIntegral busiOrderIntegral = busiOrderIntegralMapper.selectByCondition(map);
        if (busiOrderIntegral!=null&&StringUtils.isNotBlank(busiOrderIntegral.getVoucherSerialNo())&&StringUtils.isBlank(busiOrderIntegral.getVoucherRefundSerialNo())) {
            log.info("异步退还加息券开始，订单号：【{}】", busiOrder.getId());
            try {
                String refundConsumeNo = welfareService.refundVoucher(cumNumber, busiOrder.getOrderId(), busiOrderIntegral.getVoucherSerialNo());
                if (StringUtils.isNotBlank(refundConsumeNo)) {
                    busiOrderIntegral.setVoucherRefundSerialNo(refundConsumeNo);
                    int num = busiOrderIntegralMapper.updateByCondition(busiOrderIntegral);
                    if (num == 1) {
                        log.info("异步退还加息券结束，订单号：【{}】", busiOrder.getId());
                    } else {
                        log.info("异步退还加息券失败，订单号：【{}】", busiOrder.getId());
                        throw new BusinessException("退还加息券失败，更新消费流水失败");
                    }
                } else {
                    log.info("异步退还加息券失败，订单号：【{}】，退还加息券操作未返回流水信息", busiOrder.getId());
                    throw new BusinessException("退还加息券失败，未生成加息券回退流水");
                }
            } catch (Exception e) {
                log.error("回退加息券失败：订单号【{}】", busiOrder.getId(), e);
                MailUtil.sendMail("回退加息券失败", "订单ID" + busiOrder.getId() + ",失败原因：" + e);
            }
        }
    }

}
