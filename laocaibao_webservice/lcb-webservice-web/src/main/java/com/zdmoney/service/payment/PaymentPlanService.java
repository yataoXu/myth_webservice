package com.zdmoney.service.payment;

import com.google.common.collect.Lists;
import com.zdmoney.common.service.BaseService;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.mapper.payment.PaymentCalendarMapper;
import com.zdmoney.mapper.payment.PaymentPlanMapper;
import com.zdmoney.models.order.BusiOrderTemp;
import com.zdmoney.models.payment.PaymentPlan;
import com.zdmoney.models.transfer.BusiDebtTransfer;
import com.zdmoney.service.BusiOrderTempService;
import com.zdmoney.service.transfer.BusiDebtTransferService;
import com.zdmoney.utils.DateUtil;
import com.zdmoney.vo.AssetCalendar;
import com.zdmoney.web.dto.PaymentPlanArrayDTO;
import com.zdmoney.web.dto.PaymentPlanDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by wu.hg on 2016/4/6.
 */
@Service
@Slf4j
public class PaymentPlanService extends BaseService<PaymentPlan, Long> {

    @Autowired
    private BusiOrderTempService busiOrderTempService;

    @Autowired
    private BusiDebtTransferService busiDebtTransferService;

    @Autowired
    private PaymentCalendarMapper paymentCalendarMapper;

    private PaymentPlanMapper getPaymentPlanMapper() {
        return (PaymentPlanMapper) baseMapper;
    }

    public PaymentPlanArrayDTO getPaymentPlanList(String orderNum) {
        PaymentPlanArrayDTO dto = new PaymentPlanArrayDTO();
        BusiOrderTemp order = busiOrderTempService.selectViewByPrimaryKey(Long.valueOf(orderNum));
        if (order != null) {
            List<PaymentPlan> list = Lists.newArrayList();
            BigDecimal noPayTotalAmt = new BigDecimal(0);//待回本金总额
            BigDecimal noPayPrincipalInterest = new BigDecimal(0);//待回本息和
            BigDecimal noPayTotalInterest = new BigDecimal(0);//待回利息
            BigDecimal payTotalAmt = new BigDecimal(0);//已回本金总额
            BigDecimal payPrincipalInterest = new BigDecimal(0);//已回本息和
            BigDecimal payTotalInterest = new BigDecimal(0);//已回利息和
            /*Example example = new Example(PaymentPlan.class);
            example.createCriteria().andEqualTo("orderNum", order.getOrderId());
            example.setOrderByClause("curr_term asc");
            list = getPaymentPlanMapper().selectByExample(example);
            if (CollectionUtils.isEmpty(list)) {
                //普通产品
                if (AppConstants.OrderProductType.COMMON.equals(order.getProductType()) && AppConstants.OrderTransferStatus.ORDER_NORMAL.equals(order.getTransferType())) {
                    PaymentPlan plan = new PaymentPlan();
                    plan.setInterest(order.getPrincipalinterest().subtract(order.getOrderAmt()));
                    plan.setPrincipal(order.getOrderAmt());
                    plan.setPrincipalInterest(order.getPrincipalinterest());
                    plan.setCurrTerm(1);
                    plan.setTerm(1);
                    plan.setRepayDay(order.getInterestEndDate());
                    plan.setRepayStatus(AppConstants.PaymentPlanStatus.UNRETURN);
                    if (order.getStatus().equals(AppConstants.BusiOrderStatus.BUSIORDER_STATUS_9)) {
                        plan.setRepayStatus(AppConstants.PaymentPlanStatus.RETURNING);
                    }
                    if (order.getStatus().equals(AppConstants.BusiOrderStatus.BUSIORDER_STATUS_10)) {
                        plan.setRepayStatus(AppConstants.PaymentPlanStatus.RETURNED);
                    }
                    if (order.getStatus().equals(AppConstants.BusiOrderStatus.BUSIORDER_STATUS_16)) {
                        plan.setRepayStatus(AppConstants.PaymentPlanStatus.RETURNED_TRANSFER);
                    }
                    list.add(plan);
                }
            }*/
            //普通产品
            if (AppConstants.OrderProductType.COMMON.equals(order.getProductType())) {
                PaymentPlan plan = new PaymentPlan();
                plan.setInterest(order.getPrincipalinterest().subtract(order.getOrderAmt()));
                plan.setPrincipal(order.getOrderAmt());
                plan.setPrincipalInterest(order.getPrincipalinterest());
                plan.setCurrTerm(1);
                plan.setTerm(1);
                plan.setRepayDay(order.getInterestEndDate());
                plan.setRepayStatus(AppConstants.PaymentPlanStatus.UNRETURN);
                if (order.getStatus().equals(AppConstants.BusiOrderStatus.BUSIORDER_STATUS_9)) {
                    plan.setRepayStatus(AppConstants.PaymentPlanStatus.RETURNING);
                }
                if (order.getStatus().equals(AppConstants.BusiOrderStatus.BUSIORDER_STATUS_10)) {
                    plan.setRepayStatus(AppConstants.PaymentPlanStatus.RETURNED);
                }
                if (order.getStatus().equals(AppConstants.BusiOrderStatus.BUSIORDER_STATUS_16)) {
                    plan.setRepayStatus(AppConstants.PaymentPlanStatus.RETURNED_TRANSFER);
                }
                list.add(plan);
            }
            //标的产品
            else {
                Example example = new Example(PaymentPlan.class);
                example.createCriteria().andEqualTo("orderNum", order.getOrderId());
                example.setOrderByClause("curr_term asc");
                list = getPaymentPlanMapper().selectByExample(example);
            }
            //0-上期待回 1-上期已回
            String aheadFlag = "";
            for (PaymentPlan paymentPlan : list) {
                PaymentPlanDTO plan = new PaymentPlanDTO();
                plan.setCurrTerm(paymentPlan.getCurrTerm().toString());
                plan.setTerm(paymentPlan.getTerm().toString());
                plan.setPrincipal(paymentPlan.getPrincipal().setScale(2, BigDecimal.ROUND_DOWN).toString());
                plan.setInterest(paymentPlan.getInterest().setScale(2, BigDecimal.ROUND_DOWN).toString());
                plan.setPrincipalInterest(paymentPlan.getPrincipalInterest().setScale(2, BigDecimal.ROUND_DOWN).toString());
                plan.setRepayDay(DateUtil.dateToString(paymentPlan.getRepayDay()));
                String title = plan.getCurrTerm() + "/" + plan.getTerm();
                String pcTitle = "";
                //待回款
                if (paymentPlan.getRepayStatus().equals(AppConstants.PaymentPlanStatus.UNRETURN)
                        || paymentPlan.getRepayStatus().equals(AppConstants.PaymentPlanStatus.RETURNING)
                        || paymentPlan.getRepayStatus().equals(AppConstants.PaymentPlanStatus.RETURNED_TRANSFER)
                        || (paymentPlan.getRepayStatus().equals(AppConstants.PaymentPlanStatus.RETURNED_AHEAD) && "0".equals(aheadFlag))
                        ) {
                    if (!AppConstants.PaymentPlanStatus.RETURNED_TRANSFER.equals(paymentPlan.getRepayStatus())) {
                        if (paymentPlan.getRepayStatus().equals(AppConstants.PaymentPlanStatus.RETURNING)) {
                            aheadFlag = "0";
                        }
                        if (paymentPlan.getRepayStatus().equals(AppConstants.PaymentPlanStatus.RETURNED_AHEAD)) {
                            title += "(提前结清到账)";
                            pcTitle += "(提前结清到账)";
                            plan.setInterest(BigDecimal.ZERO.toString());
                            plan.setPrincipalInterest(paymentPlan.getPrincipal().setScale(2, BigDecimal.ROUND_DOWN).toString());

                        }
                        noPayTotalAmt = new BigDecimal(plan.getPrincipal()).add(noPayTotalAmt);
                        noPayPrincipalInterest = new BigDecimal(plan.getPrincipalInterest()).add(noPayPrincipalInterest);
                        noPayTotalInterest = new BigDecimal(plan.getInterest()).add(noPayTotalInterest);
                    }

                    title += "待回本息";
                    pcTitle += "待回";
                    //转让方
                    BusiDebtTransfer transfer = busiDebtTransferService.getBusiDebtTransferByOrderId(order.getCustomerId(), order.getOrderId(), AppConstants.OrderTransferDirect.ORDER_TRANSFER_TRANSFER);
                    if (transfer != null) {
                        if (transfer != null) {
                            if (AppConstants.PaymentPlanStatus.RETURNED_TRANSFER.equals(paymentPlan.getRepayStatus())) {
                                title += " - 已转让";
                                pcTitle += " - 已转让";
                            } else {
                                if (transfer.getTransferStatus().equals(AppConstants.DebtTransferStatus.TRANSFER_SUCCESS) || transfer.getTransferStatus().equals(AppConstants.DebtTransferStatus.TRANSFER_INIT) || transfer.getTransferStatus().equals(AppConstants.DebtTransferStatus.TRANSFER_DURING)) {
                                    if (AppConstants.PaymentPlanStatus.UNRETURN.equals(paymentPlan.getRepayStatus())) {
                                        if (DateUtil.compareStringDate(paymentPlan.getRepayDay(), transfer.getTransferDate()) >= 0) {
                                            title += " - 转让中";
                                            pcTitle += " - 转让中";
                                        }
                                    }
                                }
                            }
                        }
                    } else {
                        BusiDebtTransfer transferee = busiDebtTransferService.getBusiDebtTransferByOrderId(order.getCustomerId(), order.getOrderId(), AppConstants.OrderTransferDirect.ORDER_TRANSFER_TRANSFEREE);
                        //下家状态
                        if (transferee != null) {
                            if (DateUtil.compareStringDate(paymentPlan.getRepayDay(), transferee.getTransferDate()) < 0) {
                                title += " - 转让前";
                                pcTitle += " - 转让前";
                            }
                        }
                    }
                    plan.setTitle(title);
                    plan.setPcTitle(pcTitle);
                    dto.getNotPaymentList().add(plan);
                }
                //已回款
                if (paymentPlan.getRepayStatus().equals(AppConstants.PaymentPlanStatus.RETURNED)
                        || paymentPlan.getRepayStatus().equals(AppConstants.PaymentPlanStatus.RETURNED_TRANSFER_UP)
                        || paymentPlan.getRepayStatus().equals(AppConstants.PaymentPlanStatus.RETURNING_TRANSFER_UP)
                        || paymentPlan.getRepayStatus().equals(AppConstants.PaymentPlanStatus.RETURNED_AHEAD) && "1".equals(aheadFlag)) {
                    title += "已回本息";
                    pcTitle += "已回";
                    if (paymentPlan.getRepayStatus().equals(AppConstants.PaymentPlanStatus.RETURNED)) {
                        payTotalAmt = paymentPlan.getPrincipal().add(payTotalAmt);
                        payPrincipalInterest = paymentPlan.getPrincipalInterest().add(payPrincipalInterest);
                        payTotalInterest = paymentPlan.getInterest().add(payTotalInterest);
                        aheadFlag = "1";
                    }
                    if (paymentPlan.getRepayStatus().equals(AppConstants.PaymentPlanStatus.RETURNED_AHEAD)) {
                        title += "(提前结清到账)";
                        pcTitle += "(提前结清到账)";
                        plan.setInterest(BigDecimal.ZERO.toString());
                        plan.setPrincipalInterest(paymentPlan.getPrincipal().setScale(2, BigDecimal.ROUND_DOWN).toString());
                        payTotalAmt = paymentPlan.getPrincipal().add(payTotalAmt);
                        payPrincipalInterest = paymentPlan.getPrincipal().add(payPrincipalInterest);
                        payTotalInterest = BigDecimal.ZERO.add(payTotalInterest);
                    }
                    if (paymentPlan.getRepayStatus().equals(AppConstants.PaymentPlanStatus.RETURNED_TRANSFER_UP) || paymentPlan.getRepayStatus().equals(AppConstants.PaymentPlanStatus.RETURNING_TRANSFER_UP)) {
                        title += " - 转让前";
                        pcTitle += " - 转让前";
                    }
                    plan.setTitle(title);
                    plan.setPcTitle(pcTitle);
                    dto.getPaymentList().add(plan);
                }
            }
            dto.setNoPayTotalAmt(noPayTotalAmt.setScale(2, BigDecimal.ROUND_DOWN));
            dto.setNoPayPrincipalInterest(noPayPrincipalInterest.setScale(2, BigDecimal.ROUND_DOWN));
            dto.setNoPayTotalInterest(noPayTotalInterest.setScale(2, BigDecimal.ROUND_DOWN));

            dto.setPayTotalAmt(payTotalAmt.setScale(2, BigDecimal.ROUND_DOWN));
            dto.setPayPrincipalInterest(payPrincipalInterest.setScale(2, BigDecimal.ROUND_DOWN));
            dto.setPayTotalInterest(payTotalInterest.setScale(2, BigDecimal.ROUND_DOWN));
        }
        return dto;
    }

    public List<PaymentPlan> selectPaymentPlans(String orderNum, List<String> status) {
        Example example = new Example(PaymentPlan.class);
        example.createCriteria().andEqualTo("orderNum", orderNum).andIn("repayStatus", status);
        example.setOrderByClause("curr_term asc");
        List<PaymentPlan> list = getPaymentPlanMapper().selectByExample(example);
        return list;
    }

    public BigDecimal getTotalPrincipal(String orderId){
        return getPaymentPlanMapper().getTotalPrincipal(orderId);
    }
    /**
     * 查询末期回款计划状态
     * @param orderNum
     * @return
     */
    public PaymentPlan selectLastPaymentPlans(String orderNum) {
        Example example = new Example(PaymentPlan.class);
        example.createCriteria().andEqualTo("orderNum", orderNum);
        example.setOrderByClause("curr_term desc");
        List<PaymentPlan> list = getPaymentPlanMapper().selectByExample(example);
        return CollectionUtils.isEmpty(list) ? null :list.get(0);
    }

    public int updateByMap(Map<String, Object> map){
        return getPaymentPlanMapper().updateByMap(map);
    }

    /**
     * 查询当前时间之前未还款信息
     * @param orderNum
     * @return
     */
    public List<PaymentPlan> selectPayPlansByCurrentTime(String orderNum,String repayDay) {
        return getPaymentPlanMapper().selectPayPlansByCurrentTime(orderNum,repayDay);
    }
    /**
     * 根据时间，订单号查还款信息
     * @return
     */
    public List<PaymentPlan> selectPayPlansByDate(Map map) {
        return getPaymentPlanMapper().selectPayPlansByDate(map);
    }

}
