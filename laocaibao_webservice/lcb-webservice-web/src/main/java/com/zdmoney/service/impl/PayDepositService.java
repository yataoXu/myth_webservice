package com.zdmoney.service.impl;

import com.google.common.collect.Maps;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.mapper.product.BusiProductContractMapper;
import com.zdmoney.mapper.product.BusiProductSubMapper;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.financePlan.BusiOrderSub;
import com.zdmoney.models.order.BusiOrderIntegral;
import com.zdmoney.models.order.BusiOrderTemp;
import com.zdmoney.models.product.BusiProduct;
import com.zdmoney.models.product.BusiProductContract;
import com.zdmoney.models.product.BusiProductSub;
import com.zdmoney.models.transfer.BusiDebtTransfer;
import com.zdmoney.service.BusiOrderIntegralService;
import com.zdmoney.service.BusiOrderTempService;
import com.zdmoney.service.BusiProductService;
import com.zdmoney.service.CustomerMainInfoService;
import com.zdmoney.service.transfer.BusiDebtTransferService;
import com.zdmoney.utils.MailUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * Created by 00232384 on 2017/3/7.
 */
@Slf4j
@Service
public class PayDepositService {

    @Autowired
    private BusiOrderTempService busiOrderTempService;

    @Autowired
    private BusiDebtTransferService busiDebtTransferService;

    @Autowired
    private BusiProductService busiProductService;

    @Autowired
    private BusiOrderIntegralService busiOrderIntegralService;

    @Autowired
    private CustomerMainInfoService customerMainInfoService;

    @Autowired
    private BusiProductContractMapper busiProductContractMapper;

    @Autowired
    private BusiProductSubMapper busiProductSubMapper;



    /**
     * 锁定订单信息
     * @param order
     * @param transfer
     * @param mainInfo
     */
    @Transactional
    public void lockOrder(BusiOrderSub order, BusiDebtTransfer transfer, CustomerMainInfo mainInfo) {
        log.info(">>>>>锁定订单【{}】开始：",order.getId());
        try {
            busiOrderTempService.updateOrderStatus(order.getProductType(),order.getId(), AppConstants.BusiOrderStatus.BUSIORDER_STATUS_1, AppConstants.BusiOrderStatus.BUSIORDER_STATUS_15);
            //更新转让单状态
            if (transfer != null) {
                busiDebtTransferService.updateTransferStatus(transfer, AppConstants.DebtTransferStatus.TRANSFER_INIT,AppConstants.DebtTransferStatus.TRANSFER_DURING, mainInfo.getId(), order.getOrderId(), order.getId());
            }
            //变更产品库存
            busiProductService.consumeProduct(order);
            log.info(">>>>>锁定订单【{}】结束：",order.getId());
        } catch (Exception e) {
            log.error(">>>>>锁定订单【{}】发生异常：",order.getId(), e);
            throw new BusinessException("支付失败");
        }
    }

    /**
     * 确定订单信息
     * @param order
     * @param transfer
     * @param mainInfo
     */
    @Transactional
    public void confirmOrder(BusiOrderSub order, BusiDebtTransfer transfer, CustomerMainInfo mainInfo) {
        log.info(">>>>>>>>>>确定订单开始,订单号：【{}】", order.getId());
        try {
            //将订单状态置为付款成功
            busiOrderTempService.updateOrderStatus(order.getProductType(),order.getId(), AppConstants.BusiOrderStatus.BUSIORDER_STATUS_15, AppConstants.BusiOrderStatus.BUSIORDER_STATUS_0);
            Map subMap = Maps.newTreeMap();
            if (transfer != null) {
                //设置转让编号
                subMap.put("transferNo", transfer.getTransferNo());
                //将转让订单状态置为转让成功
                busiDebtTransferService.updateTransferStatus(transfer, AppConstants.DebtTransferStatus.TRANSFER_DURING, AppConstants.DebtTransferStatus.TRANSFER_SUCCESS, mainInfo.getId(), order.getOrderId(), order.getId());
            }

            BusiProductSub product = busiProductSubMapper.selectByPrimaryKey(order.getProductId());
            if(product != null){
                if (product.getTotalInvestAmt().compareTo(product.getProductPrincipal()) >= 0) {
                    busiProductContractMapper.updateFinishStatus(product.getSubjectNo());
                    Map<String,Object> map=Maps.newHashMap();
                    map.put("productId",product.getId());
                    map.put("isArea","0");
                    map.put("isRecommend","0");
                    map.put("sortFlag","0");
                    busiProductService.updateMainSub(map);
                }
                if(!AppConstants.ProductSubjectType.FINANCE_PLAN.equals(product.getSubjectType())){
                    subMap.put("id", order.getId());
                    subMap.put("subjectNo",product.getSubjectNo());
                    log.info(">>>>>>>>>>更新子订单开始，订单号：【{}】，标的编号：【{}】", order.getId(), product.getSubjectNo());
                    busiOrderTempService.updateOrderSub(subMap);
                }
            }

            log.info(">>>>>>>>>>确定订单结束,订单号：【{}】", order.getId());
        }
        catch (Exception e){
            log.error(">>>>>>>>>>确定订单【{}】发生异常", order.getId(),e);
            MailUtil.sendMail("确定订单操作发生异常", "订单号:"+order.getId()+"异常原因"+e.getMessage());
        }
    }

    /**
     * 解锁订单信息
     * @param order
     * @param transfer
     * @param mainInfo
     * @param appointmentId
     * @param voucherId
     * @param isFirst
     */
    @Transactional
    public void unLockOrder(BusiOrderSub order,BusiDebtTransfer transfer,CustomerMainInfo mainInfo,String appointmentId,String voucherId,boolean isFirst) {
        log.info(">>>>>>>>>>解锁订单开始,订单号：【{}】", order.getId());
        try {
            //回退产品库存
            busiProductService.refundProduct(order);
            //回退订单状态
            busiOrderTempService.updateOrderStatus(order.getId(), AppConstants.BusiOrderStatus.BUSIORDER_STATUS_15, AppConstants.BusiOrderStatus.BUSIORDER_STATUS_11);
            Map<String, String> param = Maps.newTreeMap();
            param.put("orderNo", order.getId().toString());
            BusiOrderIntegral busiOrderIntegral = busiOrderIntegralService.selectByCondition(param);
            //有支付流水
            if (busiOrderIntegral != null) {
                //订单未支付
                if (StringUtils.isBlank(busiOrderIntegral.getAccountSeriNo())) {
                    //异步回滚预约券
                    if (StringUtils.isNotBlank(appointmentId)) {
                        busiOrderIntegralService.refundAppointment(busiOrderIntegral, mainInfo, order);
                    }
                    //异步回滚加息券
                    if (StringUtils.isNotBlank(voucherId)) {
                        busiOrderIntegralService.refundVoucher(mainInfo.getCmNumber(), order);
                    }
                }
            }
            //回滚用户新手状态
            customerMainInfoService.refundCustomerFirst(mainInfo, isFirst);
            //回滚转让单状态
            if (transfer != null) {
                busiProductService.dealWithTransferProduct(order);
                busiDebtTransferService.updateTransferStatus(transfer, AppConstants.DebtTransferStatus.TRANSFER_DURING,AppConstants.DebtTransferStatus.TRANSFER_INIT, null, null, order.getId());
            }
            log.info(">>>>>>>>>>解锁订单结束,订单号：【{}】", order.getId());
        } catch (Exception e) {
            log.error(">>>>>>>>>>解锁订单【{}】发生异常", order.getId(),e);
            MailUtil.sendMail("解锁订单操作发生异常", "订单号:"+order.getId()+"，异常原因:"+e);
            throw new BusinessException("支付失败");
        }
    }
}
