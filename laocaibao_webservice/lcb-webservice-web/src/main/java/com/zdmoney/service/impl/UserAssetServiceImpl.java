package com.zdmoney.service.impl;

import cn.hutool.core.convert.Convert;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.common.Result;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.mapper.order.BusiSellOrderMapper;
import com.zdmoney.models.order.BusiSellOrder;
import com.zdmoney.service.OrderReinvestConstants;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.mapper.AtChMapper;
import com.zdmoney.mapper.customer.CustomerMainInfoMapper;
import com.zdmoney.mapper.order.BusiOrderMapper;
import com.zdmoney.mapper.order.BusiOrderTempMapper;
import com.zdmoney.mapper.payment.PaymentPlanMapper;
import com.zdmoney.mapper.product.BusiProductContractMapper;
import com.zdmoney.mapper.transfer.BusiDebtTransferMapper;
import com.zdmoney.mapper.BusiOrderToPayMapper;
import com.zdmoney.models.AtCh;
import com.zdmoney.models.BusiOrderToPay;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.order.BusiOrderTemp;
import com.zdmoney.models.payment.PaymentPlan;
import com.zdmoney.models.product.BusiProduct;
import com.zdmoney.models.product.BusiProductContract;
import com.zdmoney.models.transfer.BusiDebtTransfer;
import com.zdmoney.service.*;
import com.zdmoney.service.payment.PaymentPlanService;
import com.zdmoney.service.transfer.BusiDebtTransferService;
import com.zdmoney.utils.*;
import com.zdmoney.vo.*;
import com.zdmoney.web.dto.UserAssetDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;

import org.joda.time.DateTime;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;
import websvc.models.Model_520004;
import websvc.models.Model_520044;
import websvc.models.Model_521003;
import websvc.models.Model_521004;
import websvc.req.ReqMain;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by 00225181 on 2015/12/2.
 * 2.0用户持有资产接口
 */
@Service
@Slf4j
public class UserAssetServiceImpl implements UserAssetService {

    @Autowired
    CustomerMainInfoMapper customerMainInfoMapper;

    @Autowired
    BusiOrderMapper busiOrderMapper;

    @Autowired
    BusiOrderTempMapper busiOrderTempMapper;

    @Autowired
    BusiOrderTempService busiOrderTempService;

    @Autowired
    BusiProductService busiProductService;

    @Autowired
    BusiProductContractMapper busiProductContractMapper;

    @Autowired
    AtChMapper atChMapper;

    @Autowired
    ConfigParamBean configParamBean;

    @Autowired
    PaymentPlanMapper payPlanMapper;

    @Autowired
    BusiDebtTransferMapper transferMapper;

    @Autowired
    BusiDebtTransferService busiDebtTransferService;

    @Autowired
    AccountOverview520003Service accountOverview520003Service;

    @Autowired
    PaymentPlanService paymentPlanService;

    @Autowired
    private BusiOrderToPayMapper orderToPayMapper;

    @Autowired
    BusiOrderService busiOrderService;

    @Autowired
    private OrderReinvestConstants orderReinvestConstants;

    @Autowired
    CustomerMainInfoService customerMainInfoService;

    @Autowired
    private BusiSellOrderMapper sellOrderMapper;


    private static BigDecimal getTotalAddRate(BigDecimal actionRate, BigDecimal inviteRate) {
        return (actionRate == null ? new BigDecimal(0) : actionRate).add(inviteRate == null ? new BigDecimal(0) : inviteRate);
    }

    @Override
    public Result getUserAsset(ReqMain reqMain) throws Exception {
        UserAssetDTO dto = new UserAssetDTO();
        Model_520004 model_520004 = (Model_520004) reqMain.getReqParam();
        String customerId = model_520004.getCustomerId();
        if (!StringUtils.isEmpty(customerId)) {
            CustomerMainInfo customerMainInfo = customerMainInfoMapper.selectByPrimaryKey(Long.parseLong(customerId));
            if (customerMainInfo != null) {
                String status = "";
                Map<String, Object> map = Maps.newTreeMap();
                if ("0".equals(model_520004.getIsHold())) {
                    status = AppConstants.BusiOrderStatus.BUSIORDER_STATUS_0 + "," + AppConstants.BusiOrderStatus.BUSIORDER_STATUS_9 + "," + AppConstants.BusiOrderStatus.BUSIORDER_STATUS_14 +
                            "," + AppConstants.BusiOrderStatus.BUSIORDER_STATUS_17 + "," + AppConstants.BusiOrderStatus.BUSIORDER_STATUS_18;
                } else if ("1".equals(model_520004.getIsHold())) {
                    status = AppConstants.BusiOrderStatus.BUSIORDER_STATUS_4 + "," + AppConstants.BusiOrderStatus.BUSIORDER_STATUS_10 + "," + AppConstants.BusiOrderStatus.BUSIORDER_STATUS_16 +
                            "," + AppConstants.BusiOrderStatus.BUSIORDER_STATUS_19;
                    map.put("isHold", "1");
                } else if ("2".equals(model_520004.getIsHold())) {
                    status = AppConstants.BusiOrderStatus.BUSIORDER_STATUS_0 + "," + AppConstants.BusiOrderStatus.BUSIORDER_STATUS_9 + "," + AppConstants.BusiOrderStatus.BUSIORDER_STATUS_14 +
                            "," + AppConstants.BusiOrderStatus.BUSIORDER_STATUS_17 + "," + AppConstants.BusiOrderStatus.BUSIORDER_STATUS_18;
                    ;
                    map.put("sort", "asc");
                }
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                map.put("customerId", customerId);
                map.put("sDate", sdf.format(new Date()));
                map.put("status", status.split(","));
                if (model_520004.getOrderId() != null) {
                    map.put("orderId", model_520004.getOrderId());
                }
                int totalRecord = busiOrderMapper.selelctAssetByCustomerIdAndStatus(map).size();

                Page<UserAssetVo> page = new Page<>();
                page.setPageNo(Integer.parseInt(model_520004.getPageNo()));
                page.setPageSize(Integer.parseInt(model_520004.getPageSize()));
                map.put("page", page);

                List<UserAssetVo> vos = busiOrderMapper.selelctAssetByCustomerIdAndStatus(map);

                String repayType = "";
                for (UserAssetVo vo : vos) {
                    if (AppConstants.ProductTransferStatus.COMMON_PRODUCT.equals(vo.getIsTransfer())) {
                        vo.setProjectDetailUrl(configParamBean.getTouchProductDetailsUrl() + "/productDetails?productId=" + vo.getProductId());
                    }
                    if (AppConstants.ProductTransferStatus.TRANSFER_PRODUCT.equals(vo.getIsTransfer())) {
                        vo.setProjectDetailUrl(configParamBean.getTouchProductDetailsUrl() + "/transferDetails?productId=" + vo.getProductId());
                    }
                    Long orderId = vo.getOrderId();
                    BusiOrderTemp busiOrderTemp = busiOrderTempService.selectViewByPrimaryKey(orderId);
                    Long productId = busiOrderTemp.getProductId();
                    Map<String, Object> paramsMap = new HashMap<>();
                    paramsMap.put("currentDate", DateUtil.timeFormat(new Date(), DateUtil.fullFormat));
                    paramsMap.put("productId", productId);
                    BusiProduct busiProduct = busiProductService.findProductById(paramsMap);
                    if (busiProduct == null) {
                        continue;
                    }
                    repayType = vo.getRepayType();
                    //若还款类型为等额本息或先息后本，增加回款计划
                    if (StringUtils.isNotBlank(repayType) && !AppConstants.OrderPaymentType.ONE_TIME.equals(repayType)) {
                        int term = 0;
                        BigDecimal payPrincipalInterest = new BigDecimal(0);
                        BigDecimal principal = new BigDecimal(0);
                        BigDecimal payInterest = new BigDecimal(0);
                        //已回款计划
                        List<PaymentPlan> paymentPlanList = payPlanMapper.selectPaymentPlanById(busiOrderTemp.getOrderId());//orderNum
                        if (CollectionUtils.isNotEmpty(paymentPlanList)) {
                            int returnedTerm = 0;
                            term = paymentPlanList.get(0).getTerm();
                            int isReturned = 1;//默认0-未还，1-已还
                            for (PaymentPlan payPlan : paymentPlanList) {
                                if (AppConstants.PaymentPlanStatus.UNRETURN.equals(payPlan.getRepayStatus()) || AppConstants.PaymentPlanStatus.RETURNING.equals(payPlan.getRepayStatus())) {
                                    isReturned = 0;
                                }
                                //正常回款
                                if (AppConstants.PaymentPlanStatus.RETURNED.equals(payPlan.getRepayStatus())) {
                                    payPrincipalInterest = payPlan.getPrincipalInterest().add(payPrincipalInterest);
                                    payInterest = payInterest.add(payPlan.getInterest());
                                    returnedTerm++;
                                }
                                //上家已回款，不记录收益
                                if (AppConstants.PaymentPlanStatus.RETURNED_TRANSFER_UP.equals(payPlan.getRepayStatus())) {
                                    returnedTerm++;
                                }
                                //提前结清-已回
                                if (AppConstants.PaymentPlanStatus.RETURNED_AHEAD.equals(payPlan.getRepayStatus()) && isReturned == 1) {
                                    payPrincipalInterest = payPlan.getPrincipal().add(payPrincipalInterest);
                                    returnedTerm++;
                                }
                            }
                            vo.setPayAndTerm(returnedTerm + "/" + term);//已回/总期数
                            vo.setPayPrincipalInterest(payPrincipalInterest.setScale(2, BigDecimal.ROUND_DOWN));//本息和
                            vo.setPayInterest(payInterest.setScale(2, BigDecimal.ROUND_DOWN));//利息
                            vo.setPrincipal(principal.setScale(2, BigDecimal.ROUND_DOWN));//本金
                            vo.setInterest(payInterest.setScale(2, BigDecimal.ROUND_DOWN));
                        } else {
                            vo.setPayAndTerm("0/0");
                        }
                        //单个标的产品代收利息
                        Example example = new Example(PaymentPlan.class);
                        example.createCriteria().andEqualTo("orderNum", busiOrderTemp.getOrderId());
                        List<PaymentPlan> paylist = payPlanMapper.selectByExample(example);
                        BigDecimal noPayTotalInterest = new BigDecimal(0);//待回利息
                        for (PaymentPlan paymentPlan : paylist) {
                            //待回款
                            if (paymentPlan.getRepayStatus().equals(AppConstants.PaymentPlanStatus.UNRETURN) || paymentPlan.getRepayStatus().equals(AppConstants.PaymentPlanStatus.RETURNING)) {
                                noPayTotalInterest = paymentPlan.getInterest().add(noPayTotalInterest);
                            }
                        }
                        vo.setOrderTransferInterest(noPayTotalInterest.setScale(2, BigDecimal.ROUND_DOWN));

                        //计算包含提前结清状态的回款计划
                        if ("10".equals(vo.getStatus())) {
                            List<PaymentPlan> aheadList = payPlanMapper.selectPaymentPlansById(busiOrderTemp.getOrderId());//orderNUm
                            for (PaymentPlan plan : aheadList) {
                                if (plan.getRepayStatus().equals(AppConstants.PaymentPlanStatus.RETURNED_AHEAD)) {
                                    vo.setPrincipalInterest(vo.getPrincipalInterest().subtract(plan.getInterest()));
                                }
                            }
                        }

                    } else {
                        //单个普通产品待收利息
                        vo.setOrderTransferInterest(vo.getPrincipalInterest().subtract(vo.getOrderAmt()));
                    }
                    //查询是否有转让产品记录
                    Example example = new Example(BusiDebtTransfer.class);
                    example.createCriteria().andEqualTo("initOrderNo", busiOrderTemp.getOrderId());
                    example.setOrderByClause("update_date desc");
                    List<BusiDebtTransfer> transfersList = transferMapper.selectByExample(example);
                    //转让人   个贷提前结清 （未交割 转让中 显示 已结清）
                    if (transfersList != null && transfersList.size() > 0) {
                        BusiDebtTransfer transfer = transfersList.get(0);
                        if (vo.getStatus().equals("10") && (
                                transfer.getTransferStatus().equals(AppConstants.DebtTransferStatus.TRANSFER_DURING)
                                        || transfer.getTransferStatus().equals(AppConstants.DebtTransferStatus.TRANSFER_FINISH))) {
                            vo.setStatusDesc("已结清");
                        }
                    }
                    if (vo.getStatus().equals("2")) {// 待付款状态
                        vo.setHasTransferStatus(AppConstants.HasTransferStatus.HAS_TRANSFER_PAY);
                        vo.setOrderTransferDesc(vo.getStatusDesc());
                        //单个普通产品待收利息
                        vo.setOrderTransferInterest(vo.getPrincipalInterest().subtract(vo.getOrderAmt()));
                    } else {
                        if (vo.getTransferStatus().equals(AppConstants.ProductTransferProperty.CAN_NOT_TRANSFER)) {//不可转让产品
                            if (vo.getStatus().equals("4")) {//退款
                                vo.setHasTransferStatus(AppConstants.HasTransferStatus.HAS_NO_TRANSFER_END);
                                vo.setOrderTransferDesc(vo.getStatusDesc());
                            } else if (vo.getStatus().equals("10")) {
                                vo.setHasTransferStatus(AppConstants.HasTransferStatus.HAS_NO_TRANSFER_END);
                                vo.setOrderTransferDesc(vo.getStatusDesc());
                            } else {
                                vo.setHasTransferStatus(AppConstants.HasTransferStatus.HAS_TRANSFER_NORMAL);
                                vo.setOrderTransferDesc(vo.getStatusDesc());
                            }
                        }
//                    else if (vo.getTransferStatus().equals("0") && vo.getStatus().equals("10")) {
//                        vo.setHasTransferStatus(AppConstants.HasTransferStatus.HAS_NO_TRANSFER_END);
//                        vo.setOrderTransferDesc(vo.getStatusDesc());
//                    }
                        else if (vo.getIsAddRate() != null && vo.getIsAddRate().intValue() == 1) {//使用加息劵的不能转让
                            vo.setHasTransferStatus(AppConstants.HasTransferStatus.HAS_TRANSFER_NORMAL);
                            vo.setOrderTransferDesc(vo.getStatusDesc());
                        } else {
                            if (vo.getTransferStatus().equals("1") && vo.getStatus().equals("14") && judgeLastPlan(busiOrderTemp.getOrderId())) {
                                vo.setHasTransferStatus(AppConstants.HasTransferStatus.HAS_TRANSFER_NORMAL);
                                vo.setOrderTransferDesc(vo.getStatusDesc());
                            } else if (vo.getTransferStatus().equals("1") && CollectionUtils.isEmpty(transfersList) && vo.getIsTransferOrder().equals("0")) {
                                vo.setHasTransferStatus(AppConstants.HasTransferStatus.HAS_TRANSFER_SHOW_BUTTON);
                                vo.setOrderTransferDesc(vo.getStatusDesc());
                            } else if (vo.getTransferStatus().equals("1") && CollectionUtils.isNotEmpty(transfersList) && vo.getIsTransferOrder().equals("0") &&
                                    (transfersList.get(0).getTransferStatus().equals("3") || transfersList.get(0).getTransferStatus().equals("4"))) {
                                vo.setHasTransferStatus(AppConstants.HasTransferStatus.HAS_TRANSFER_RESHOW_BUTTON);
                                //获取最新一条转让失败信息
                                if (transfersList.get(0).getTransferStatus().equals("3")) {
                                    Map trMap = new HashMap();
                                    trMap.put("orderNo", busiOrderTemp.getOrderId());
                                    trMap.put("transferStatus", "3");
                                    List<UserAssetVo> transferFails = busiOrderMapper.selectTransferFailRecord(trMap);
                                    if (CollectionUtils.isNotEmpty(transferFails)) {
                                        vo.setTransferPrice(transferFails.get(0).getTransferPrice());
                                        vo.setTransferRate(transferFails.get(0).getTransferRate());
                                        vo.setTransferDate(transferFails.get(0).getTransferDate());
                                        vo.setPubDate(transferFails.get(0).getPubDate());
                                    }
                                    if (DateUtil.getIntervalDays2(new Date(), transferFails.get(0).getUpdateDate()) > 0) {
                                        vo.setOrderTransferDesc(vo.getStatusDesc());
                                        vo.setPubDate(transferFails.get(0).getPubDate());
                                    } else {
                                        vo.setOrderTransferDesc("转让失败");
                                        vo.setPubDate(transferFails.get(0).getPubDate());
                                    }
                                    vo.setIsOneDay(transferFails.get(0).getIsOneDay());
                                }
                                if (transfersList.get(0).getTransferStatus().equals("4")) {
                                    Map trMap = new HashMap();
                                    trMap.put("orderNo", busiOrderTemp.getOrderId());
                                    trMap.put("transferStatus", "4");
                                    List<UserAssetVo> transferFails = busiOrderMapper.selectTransferFailRecord(trMap);
                                    if (CollectionUtils.isNotEmpty(transferFails)) {
                                        vo.setTransferPrice(transferFails.get(0).getTransferPrice());
                                        vo.setTransferRate(transferFails.get(0).getTransferRate());
                                        vo.setTransferDate(transferFails.get(0).getTransferDate());
                                        vo.setPubDate(transferFails.get(0).getPubDate());
                                        vo.setUpdateDate(transferFails.get(0).getUpdateDate());
                                    }
                                    if (DateUtil.getIntervalDays2(new Date(), transferFails.get(0).getUpdateDate()) > 0) {
                                        vo.setOrderTransferDesc(vo.getStatusDesc());
                                    } else {
                                        vo.setOrderTransferDesc("审核未通过");
                                    }
                                    vo.setIsOneDay(transferFails.get(0).getIsOneDay());
                                }
                                //回款中显示普通状态
                            } else if (vo.getTransferStatus().equals("1") && vo.getIsTransferOrder().equals("1") && vo.getStatus().equals("9")) {
                                vo.setHasTransferStatus(AppConstants.HasTransferStatus.HAS_TRANSFER_NORMAL);
                                vo.setOrderTransferDesc(vo.getStatusDesc());
                            } else if (vo.getTransferStatus().equals("1") && vo.getIsTransferOrder().equals("1") && (vo.getStatus().equals("1") || vo.getStatus().equals("14") || vo.getStatus().equals("0"))) {
                                vo.setHasTransferStatus(AppConstants.HasTransferStatus.HAS_TRANSFER_DAY);
                                vo.setOrderTransferDesc(vo.getStatusDesc());
                            } else if (CollectionUtils.isNotEmpty(transfersList) && transfersList.get(0).getTransferStatus().equals("0")) {
                                vo.setHasTransferStatus(AppConstants.HasTransferStatus.HAS_TRANSFER_CANCEL_BUTTON);
                                String transferStatus = transfersList.get(0).getTransferStatus();

                                Map<String, Object> pMap = new HashMap<>();
                                pMap.put("currentDate", DateUtil.timeFormat(new Date(), DateUtil.fullFormat));
                                pMap.put("productId", transfersList.get(0).getProductId());
                                BusiProduct bp = busiProductService.findProductById(pMap);

                                //判断上架时间30分钟
                                if (DateUtil.addMinutes(new Date(), 2).after(bp.getShowStartDate())) {
                                    vo.setOrderTransferStatus("0");
                                    vo.setOrderTransferDesc("转让-已上架");
                                } else {
                                    vo.setOrderTransferStatus("0");
                                    vo.setOrderTransferDesc("转让-审核中");
                                }
                                getTransferInfo(transfersList, vo);

                            } else if (CollectionUtils.isNotEmpty(transfersList) && !transfersList.get(0).getTransferStatus().equals("0") && (transfersList.get(0).getTransferStatus().equals("1") || transfersList.get(0).getTransferStatus().equals("2"))) {
                                vo.setHasTransferStatus(AppConstants.HasTransferStatus.HAS_TRANSFER_HIDE_BUTTON);
                                vo.setOrderTransferDesc("转让-交易中");

                                // if (transfersList.get(0).getTransferStatus().equals("5")){
                                //     vo.setOrderTransferDesc("转让成功");
                                //  }
                                getTransferInfo(transfersList, vo);
                            } else if (vo.getTransferStatus().equals("1") && vo.getStatus().equals("16") && transfersList.get(0).getTransferStatus().equals("5")) {
                                vo.setHasTransferStatus(AppConstants.HasTransferStatus.HAS_TRANSFER_SUCCESS);
                                vo.setOrderTransferDesc("已转让");
                                getTransferInfo(transfersList, vo);
                            } else if (vo.getTransferStatus().equals("1") && CollectionUtils.isEmpty(transfersList) && vo.getStatus().equals("10")) {
                                vo.setHasTransferStatus(AppConstants.HasTransferStatus.HAS_TRANSFER_NOLOG_END);
                                vo.setOrderTransferDesc(vo.getStatusDesc());
                            } else if (vo.getTransferStatus().equals("1") && CollectionUtils.isNotEmpty(transfersList) && vo.getStatus().equals("10")) {
                                vo.setHasTransferStatus(AppConstants.HasTransferStatus.HAS_TRANSFER_FAIL_END);
                                vo.setOrderTransferDesc(vo.getStatusDesc());
                            } else if (vo.getTransferStatus().equals("1") && vo.getStatus().equals("4")) {
                                //可转让产品 退款
                                vo.setHasTransferStatus(AppConstants.HasTransferStatus.HAS_NO_TRANSFER_END);
                                vo.setOrderTransferDesc(vo.getStatusDesc());
                            }
                        }
                    }

                    Long contractId = busiProduct.getContractId();
//                    if (contractId > 0) {//标的
                    if (!AppConstants.ProductSubjectType.SUBJECT_YX.equals(busiProduct.getSubjectType())) {
                        vo.setType("loan");
                        BusiProductContract contract = busiProductContractMapper.selectByPrimaryKey(contractId);
                        Date loanDate = null;
                        if (contract != null) {
                            loanDate = contract.getLoanDate();
                        }
                        if (loanDate != null && loanDate.before(DateTime.now().toDate())) {//放款后才展示协议
                            String contractType = busiProduct.getContractType();
                            if (AppConstants.ContractType.SUBJECT.equals(contractType)) {
                                if (AppConstants.PersonalLoan.PERSONAL_LOAN == busiProduct.getPersonLoan()) {
                                    vo.setHasTrans(5);
                                    vo.setAgreementUrl(configParamBean.getTradeAgreementUrl() + "?customerId=" + customerMainInfo.getId() + "&orderId=" + vo.getOrderId() + "&type=loan&hasTrans=5&subjectNo=" + busiProduct.getSubjectNo());//标的借款协议 委托协议
                                } else {
                                    vo.setHasTrans(3);
                                    vo.setAgreementUrl(configParamBean.getTradeAgreementUrl() + "?orderId=" + vo.getOrderId() + "&type=loan&hasTrans=3");//标的借款协议
                                }

                            } else {
                                vo.setHasTrans(4);
                                vo.setAgreementUrl(configParamBean.getTradeAgreementUrl() + "?orderId=" + vo.getOrderId() + "&type=loan&hasTrans=4");//标的债权转让协议
                            }
                        }
//                        else {
//                            vo.setHasTrans(2);
//                            vo.setAgreementUrl(configParamBean.getTradeAgreementUrl() + "?orderId=" + vo.getOrderId() + "&type=loan&hasTrans=2");//标的投资协议
//                        }
                    } else {//捞财宝产品
                        vo.setType("lcb");
                        AtCh atCh = new AtCh();
                        atCh.setOrderId(new BigDecimal(vo.getOrderId()));
                        List<AtCh> atChes = atChMapper.select(atCh);
                        if (atChes.isEmpty()) {

                            vo.setHasTrans(0);
                            vo.setAgreementUrl(configParamBean.getTradeAgreementUrl() + "?orderId=" + vo.getOrderId() + "&type=lcb&hasTrans=0");//无捞财宝债权转让协议
                        } else {
                            vo.setHasTrans(1);
                            vo.setAgreementUrl(configParamBean.getTradeAgreementUrl() + "?orderId=" + vo.getOrderId() + "&type=lcb&hasTrans=1");//捞财宝债权转让协议
                        }
                    }
                    //增加用户转让协议
                    if (StringUtils.isNotBlank(vo.getAgreementUrl())) {
                        String defaultUrl = "&flag=0&transferNo=&downOrderId=";
                        BusiDebtTransfer transfer = busiDebtTransferService.getSuccessBusiDebtTransfer(busiOrderTemp.getCustomerId(), busiOrderTemp.getOrderId(), AppConstants.OrderTransferDirect.ORDER_TRANSFER_TRANSFER);
                        String tempStr = "";
                        if (transfer != null) {
                            BusiOrderTemp temp = busiOrderTempMapper.selectViewByOrderNo(transfer.getNewOrderNo());
                            Long tempId = temp == null ? null : temp.getId();
                            BusiOrderTemp tempOrigin = busiOrderTempMapper.selectViewByOrderNo(transfer.getOriginOrderNo());
                            Long tempOriginId = tempOrigin == null ? null : tempOrigin.getId();
                            defaultUrl = "&flag=1&transferNo=" + transfer.getTransferNo() + "&downOrderId=" + tempId + "&originOrderId=" + tempOriginId;
                            tempStr += "&transferNo=" + transfer.getTransferNo() + "&transferOrderId=" + tempId + "&originOrderId=" + tempOriginId;
                        }
                        BusiDebtTransfer transferee = busiDebtTransferService.getSuccessBusiDebtTransfer(busiOrderTemp.getCustomerId(), busiOrderTemp.getOrderId(), AppConstants.OrderTransferDirect.ORDER_TRANSFER_TRANSFEREE);
                        if (transferee != null) {
                            BusiOrderTemp temp = busiOrderTempMapper.selectViewByOrderNo(transferee.getNewOrderNo());
                            Long tempId = temp == null ? null : temp.getId();
                            BusiOrderTemp tempOrigin = busiOrderTempMapper.selectViewByOrderNo(transferee.getOriginOrderNo());
                            Long tempOriginId = tempOrigin == null ? null : tempOrigin.getId();
                            defaultUrl = "&flag=2&transferNo=" + transferee.getTransferNo() + "&downOrderId=" + tempId + "&originOrderId=" + tempOriginId;
                            tempStr += "&transfereeNo=" + transferee.getTransferNo() + "&transfereeOrderId=" + tempId + "&originOrderId=" + tempOriginId;
                        }
                        if (transfer != null && transferee != null) {
                            defaultUrl = "&flag=3" + tempStr;
                        }
                        vo.setAgreementUrl(vo.getAgreementUrl() + defaultUrl);
                    }
                    vo.setTotalAddRate(getTotalAddRate(vo.getActionRate(), vo.getInviteRate()).multiply(new BigDecimal(100)));
                    //理财计划 -- subjectType:4
                    getFinPlanUserAseetVo(vo, busiOrderTemp);

                }
                page.setResults(vos);
                page.setTotalRecord(totalRecord);
                dto.setUserAssetVos(page);

                //计算用户已收利息
                BigDecimal totalInterest = this.computeHistoryInterest(customerMainInfo.getId());
                dto.setCollectedInterest(CoreUtil.BigDecimalAccurate(totalInterest));
                //计算用户持有资产及待收利息
                UserUnReceiveAsset unReceiveAsset = accountOverview520003Service.getHoldAsset(customerMainInfo);
                dto.setHoldAsset(CoreUtil.BigDecimalAccurate(unReceiveAsset.getUnReceivePrinciple()));
                dto.setUnCollectInterest(CoreUtil.BigDecimalAccurate(unReceiveAsset.getUnReceiveInterest()));
                //回款计划资产(分期中+历史分期结束)
//                List<PaymentPlan> paymentPlanList = payPlanMapper.selectRepayInfoByCustomerId(customerId);
//                BigDecimal totalInterest=new BigDecimal(0);
//                if (CollectionUtils.isNotEmpty(paymentPlanList)) {
//                    for (PaymentPlan repayPlan : paymentPlanList){
//                        totalInterest= totalInterest.add(repayPlan.getInterest());
//                    }
//                }
//                //分期回款中资产
//                Map<String, Object> orderMap = new HashMap<>();
//                orderMap.put("customerId", customerId);
//                orderMap.put("repayStatus", 3);
//                orderMap.put("status", AppConstants.BusiOrderStatus.BUSIORDER_STATUS_14);
//                List<PaymentPlan> paymentOrderPlanList = payPlanMapper.selectOrderRepayByCustomerId(orderMap);
//                BigDecimal orderAmt=new BigDecimal(0);
//                BigDecimal orderInterest=new BigDecimal(0);
//                if (CollectionUtils.isNotEmpty(paymentOrderPlanList)) {
//                    for (PaymentPlan orderRepayPlan : paymentOrderPlanList){
//                        orderAmt=orderAmt.add(orderRepayPlan.getPrincipal());
//                        orderInterest=orderInterest.add(orderRepayPlan.getInterest());
//                    }
//
//                }
//
//                UserUnReceiveAsset unReceiveAsset = accountOverview520003Service.getHoldAsset(customerMainInfo);
//                dto.setHoldAsset(CoreUtil.BigDecimalAccurate(unReceiveAsset.getUnReceivePrinciple()));
//                dto.setUnCollectInterest(CoreUtil.BigDecimalAccurate(unReceiveAsset.getUnReceiveInterest()));
//
//                //历史收益统计
//                BigDecimal notNormalInterest = payPlanMapper.selectHistoryInterestByCustomerId(customerMainInfo.getId());
//
//                map.clear();
//                map.put("customerId", customerId);
//                map.put("status", AppConstants.BusiOrderStatus.BUSIORDER_STATUS_10);
//                UserAssetIntstAndTtlAmtVo vo = busiOrderMapper.selectEffectiveOrderAmtByCustomerID(map);
//                if (vo != null && vo.getTotalInterest() != null) {
//                    if (totalInterest.compareTo(new BigDecimal(0)) != 0) {
//                        dto.setCollectedInterest(CoreUtil.BigDecimalAccurate(vo.getTotalInterest().add(totalInterest)));//历史资产利息
//                    } else {
//                        dto.setCollectedInterest(CoreUtil.BigDecimalAccurate(vo.getTotalInterest()));//历史资产利息
//                    }
//                } else {
//                    dto.setCollectedInterest(CoreUtil.BigDecimalAccurate(totalInterest));//历史资产利息
//                }
            } else {
                throw new BusinessException("查询不到用户信息！");
            }
        } else {
            throw new BusinessException("客户号不能为空！");
        }
        return Result.success(dto);
    }

    private void getTransferInfo(List<BusiDebtTransfer> transfersList, UserAssetVo vo) {
        vo.setTransferPrice(transfersList.get(0).getTransferPrice());
        vo.setTransferRate(transfersList.get(0).getTransferRate());
        vo.setTransferCharge(transfersList.get(0).getTransferCharge());
        vo.setTransferDate(transfersList.get(0).getTransferDate());
        vo.setUpdateDate(transfersList.get(0).getUpdateDate());
        vo.setTransferReceivedAmt(transfersList.get(0).getTransferPrice().subtract(transfersList.get(0).getTransferCharge()));
        vo.setTransferPreReceivedAmt(transfersList.get(0).getTransferPrice().subtract(transfersList.get(0).getTransferCharge()));
        vo.setPubDate(transfersList.get(0).getPubDate());
    }

    @Override
    public String getAvialableBalance(ReqMain reqMain) throws Exception {
        return null;
    }

    /**
     * 统计用户历史收益
     *
     * @param customerId
     * @return
     */
    public BigDecimal computeHistoryInterest(Long customerId) {
        BigDecimal result = BigDecimal.ZERO;
        //标的或转让产品订单收益
        BigDecimal notNormalInterest = payPlanMapper.selectHistoryInterestByCustomerId(customerId);
        result = result.add(notNormalInterest);
        //普通订单收益，理财计划订单收益
        Map map = Maps.newTreeMap();
        map.put("customerId", customerId);
        String[] status = new String[]{AppConstants.BusiOrderStatus.BUSIORDER_STATUS_10, AppConstants.BusiOrderStatus.BUSIORDER_STATUS_19};
        map.put("status", status);
        UserAssetIntstAndTtlAmtVo vo = busiOrderMapper.selectEffectiveOrderAmtByCustomerID(map);
        result = result.add(vo.getTotalInterest());
        return result;
    }

    /**
     * 统计用户单笔订单历史收益（标的或转让产品订单）
     *
     * @param customerId
     * @return
     */
    public UserRepaymentVo computeOrderHistoryInterest(Long customerId, String orderNum) {
        Map map = Maps.newTreeMap();
        map.put("customerId", customerId);
        map.put("orderNum", orderNum);
        //标的或转让产品订单收益
        UserRepaymentVo repayVo = payPlanMapper.selectHistoryOrderInterestByCustomerId(map);
        return repayVo;
    }

    /**
     * 判断回款计划最后一期状态是否可转让
     *
     * @param orderNum
     * @return
     */
    private boolean judgeLastPlan(String orderNum) {
        boolean flag = false;
        PaymentPlan paymentPlan = paymentPlanService.selectLastPaymentPlans(orderNum);
        if (paymentPlan != null) {
            if (!AppConstants.PaymentPlanStatus.UNRETURN.equals(paymentPlan.getRepayStatus())) {
                flag = true;
            }
        }
        return flag;
    }

    /**
     * 理财计划订单信息
     */
    private UserAssetVo getFinPlanUserAseetVo(UserAssetVo vo, BusiOrderTemp busiOrderTemp) {
        String subjectType = vo.getSubjectType();
        if (StringUtils.isNotBlank(subjectType) && AppConstants.ProductSubjectType.FINANCE_PLAN.equals(subjectType) &&  !vo.getStatus().equals("2")) {// 排除待付款状态
            Date exitCheckDate = vo.getExitCheckDate();
            //默认不显示提前申请退出按钮
            if (exitCheckDate == null && vo.getExitCheckStatus() == 0) {
                vo.setHasTransferStatus(AppConstants.HasTransferStatus.FIN_PLAN_HIDDEN_EXIT_BUTTON);
            }
            //提前退出审核状态 +（当前时间-审核时间3天之内)显示提前退出按钮
            if (exitCheckDate != null) {
                long days = DateUtil.getIntervalDays2(new Date(), exitCheckDate);
                if (vo.getExitCheckStatus() == 1 && days < 3) {
                    vo.setHasTransferStatus(AppConstants.HasTransferStatus.FIN_PLAN_SHOW_EXIT_BUTTON);
                } else {
                    vo.setHasTransferStatus(AppConstants.HasTransferStatus.FIN_PLAN_HIDDEN_EXIT_BUTTON);
                }
            }

            if (AppConstants.BusiOrderStatus.BUSIORDER_STATUS_0.equals(vo.getStatus()) || (AppConstants.BusiOrderStatus.BUSIORDER_STATUS_4).equals(vo.getStatus())) {
                vo.setShowFinPlan(0);
            } else {
                vo.setShowFinPlan(1);
            }
            if (AppConstants.BusiOrderStatus.BUSIORDER_STATUS_0.equals(vo.getStatus())) {
                //理财计划相关协议 (服务协议)
                vo.setAgreementUrl(configParamBean.getTradeAgreementUrl() + "?orderId=" + vo.getOrderId() + "&type=finPlan");
                vo.setOrderTransferDesc(AppConstants.FinPlanTips.FIN_PLAN_INVEST);
                vo.setStatusDesc(AppConstants.FinPlanTips.FIN_PLAN_INVEST);
            }
            if (AppConstants.BusiOrderStatus.BUSIORDER_STATUS_17.equals(vo.getStatus())&& vo.getReinvestCount() == 0) {
                vo.setAgreementUrl(configParamBean.getTradeAgreementUrl() + "?orderId=" + vo.getOrderId() + "&type=finPlan");
                vo.setOrderTransferDesc(AppConstants.FinPlanTips.FIN_PLAN_INVEST_VILIDATE);
                vo.setStatusDesc(AppConstants.FinPlanTips.FIN_PLAN_INVEST_VILIDATE);
                //债权信息
                //vo.setCreditorRightsUrl(configParamBean.getTouchWebappHomeUrl() + "/commonDispatchRequest?method=500803&resultPage=front/app/claimInfo&orderId=" + busiOrderTemp.getId()+"&productId="+vo.getProductId()+"&status=2");
                vo.setCreditorRightsUrl(configParamBean.getTouchProductDetailsUrl() + "/orderClaim?orderId=" + busiOrderTemp.getId() + "&productId=" + vo.getProductId() + "&status=2");
            }
            if (AppConstants.BusiOrderStatus.BUSIORDER_STATUS_17.equals(vo.getStatus()) && vo.getReinvestCount() > 0) {
                vo.setAgreementUrl(configParamBean.getTradeAgreementUrl() + "?orderId=" + vo.getOrderId() + "&type=finPlan&reinvest=1");
                vo.setOrderTransferDesc(AppConstants.FinPlanTips.FIN_PLAN_INVEST_VILIDATE);
                vo.setStatusDesc(AppConstants.FinPlanTips.FIN_PLAN_INVEST_VILIDATE);
                //债权信息
                //vo.setCreditorRightsUrl(configParamBean.getTouchWebappHomeUrl() + "/commonDispatchRequest?method=500803&resultPage=front/app/claimInfo&orderId=" + busiOrderTemp.getId()+"&productId="+vo.getProductId()+"&status=2");
                vo.setCreditorRightsUrl(configParamBean.getTouchProductDetailsUrl() + "/orderClaim?orderId=" + busiOrderTemp.getId() + "&productId=" + vo.getProductId() + "&status=2");
            }
            if (AppConstants.BusiOrderStatus.BUSIORDER_STATUS_18.equals(vo.getStatus())) {
                vo.setAgreementUrl(configParamBean.getTradeAgreementUrl() + "?orderId=" + vo.getOrderId() + "&type=finPlan");
                vo.setOrderTransferDesc(AppConstants.FinPlanTips.FIN_PLAN_EXIT);
                vo.setStatusDesc(AppConstants.FinPlanTips.FIN_PLAN_EXIT);
                //债权信息
                //vo.setCreditorRightsUrl(configParamBean.getTouchWebappHomeUrl() + "/commonDispatchRequest?method=500803&resultPage=front/app/claimInfo&orderId=" + busiOrderTemp.getId()+"&productId="+vo.getProductId()+"&status=2");
                vo.setCreditorRightsUrl(configParamBean.getTouchProductDetailsUrl() + "/orderClaim?orderId=" + busiOrderTemp.getId() + "&productId=" + vo.getProductId() + "&status=2");

            }
            if (AppConstants.BusiOrderStatus.BUSIORDER_STATUS_19.equals(vo.getStatus())) {
                vo.setAgreementUrl(configParamBean.getTradeAgreementUrl() + "?orderId=" + vo.getOrderId() + "&type=finPlan");
                vo.setOrderTransferDesc(AppConstants.FinPlanTips.FIN_PLAN_EXIT_END);
                vo.setStatusDesc(AppConstants.FinPlanTips.FIN_PLAN_EXIT_END);
                vo.setPrincipalInterest(vo.getExitActualAmt());
               // vo.setInterestEndDate(vo.getExitActualDate());
                vo.setInterest(vo.getExitActualInterest());
                //债权信息
                //vo.setCreditorRightsUrl(configParamBean.getTouchWebappHomeUrl() + "/commonDispatchRequest?method=500803&resultPage=front/app/claimInfo&orderId=" + busiOrderTemp.getId()+"&productId="+vo.getProductId()+"&status=2");
                vo.setCreditorRightsUrl(configParamBean.getTouchProductDetailsUrl() + "/orderClaim?orderId=" + busiOrderTemp.getId() + "&productId=" + vo.getProductId() + "&status=2");

            }
            if (AppConstants.BusiOrderStatus.BUSIORDER_STATUS_4.equals(vo.getStatus())) {
                vo.setOrderTransferDesc(AppConstants.FinPlanTips.FIN_PLAN_RETURN_AMT);
                vo.setStatusDesc(AppConstants.FinPlanTips.FIN_PLAN_RETURN_AMT);
            }
            Date exitClickDate = busiOrderTemp.getExitClickDate();
            if (null != exitClickDate) {
                vo.setExitApplyStatus(1);
                vo.setHasTransferStatus(AppConstants.HasTransferStatus.FIN_PLAN_HIDDEN_CHARGEFEE);
                int dayScope = DateUtil.getIntervalDays2(new Date(), exitClickDate);
                BusiOrderTemp order;
                if (dayScope < 3 || dayScope > 3) {
                    vo.setStatusDesc(AppConstants.FinPlanTips.FIN_PLAN_RETURN_APPLY);
                    order = busiOrderTempService.selectByPrimaryKey(vo.getOrderId());
                    if (AppConstants.BusiOrderStatus.BUSIORDER_STATUS_19.equals(order.getStatus())) {
                        vo.setStatusDesc(AppConstants.FinPlanTips.FIN_PLAN_EXIT_APPLY_SUCCESS);
                        vo.setHasTransferStatus(AppConstants.HasTransferStatus.FIN_PLAN_SHOW_CHARGEFEE);
                        vo.setPrincipalInterest(vo.getExitActualAmt());
                        vo.setInterestEndDate(vo.getExitActualDate());
                        vo.setInterest(vo.getExitActualInterest());
                    }
                }
            }
        }
        return vo;
    }

    /**
     * 用户资产4.1
     * @param reqMain
     * @return
     * @throws Exception
     */
    public Result getUserAssets(ReqMain reqMain) throws Exception {
        UserAssetDTO dto = new UserAssetDTO();
        Model_520044 model_520004 = (Model_520044) reqMain.getReqParam();
        String customerId = model_520004.getCustomerId();
        String orderType=model_520004.getOrderType();
        if (StringUtils.isEmpty(customerId)) {
            throw new BusinessException("客户号不能为空！");
        }
        CustomerMainInfo customerMainInfo = customerMainInfoMapper.selectByPrimaryKey(Long.parseLong(customerId));
        if (customerMainInfo == null) {
            throw new BusinessException("查询不到用户信息！");
        }
        String status = "";
        Map<String, Object> map = Maps.newTreeMap();
        if ("0".equals(model_520004.getIsHold())) {
            status = AppConstants.BusiOrderStatus.BUSIORDER_STATUS_0 + "," + AppConstants.BusiOrderStatus.BUSIORDER_STATUS_9 + "," + AppConstants.BusiOrderStatus.BUSIORDER_STATUS_14+
                    ","+AppConstants.BusiOrderStatus.BUSIORDER_STATUS_17+","+AppConstants.BusiOrderStatus.BUSIORDER_STATUS_18;
        } else if ("1".equals(model_520004.getIsHold())) {
            status = AppConstants.BusiOrderStatus.BUSIORDER_STATUS_4 + "," + AppConstants.BusiOrderStatus.BUSIORDER_STATUS_10 + "," + AppConstants.BusiOrderStatus.BUSIORDER_STATUS_16+
                    ","+AppConstants.BusiOrderStatus.BUSIORDER_STATUS_19;
            map.put("isHold","1");
        } else if ("2".equals(model_520004.getIsHold())) {
            status = AppConstants.BusiOrderStatus.BUSIORDER_STATUS_0 + "," + AppConstants.BusiOrderStatus.BUSIORDER_STATUS_9 + "," + AppConstants.BusiOrderStatus.BUSIORDER_STATUS_14 +
                    "," + AppConstants.BusiOrderStatus.BUSIORDER_STATUS_17 + "," + AppConstants.BusiOrderStatus.BUSIORDER_STATUS_18;

            map.put("sort", "asc");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        map.put("customerId", customerId);
        map.put("sDate", sdf.format(new Date()));
        map.put("status", status.split(","));
        if (model_520004.getOrderId() != null) {
            map.put("orderId", model_520004.getOrderId());
        }
        int totalRecord = busiOrderMapper.selelctAssetByCustomerIdAndStatus(map).size();
        //int totalRecord =0;
        Page<UserAssetVo> page = new Page<>();
        page.setPageNo(Integer.parseInt(model_520004.getPageNo()));
        page.setPageSize(Integer.parseInt(model_520004.getPageSize()));
        map.put("page", page);

        List<UserAssetVo> vos = null;
        if (AppConstants.ORDER_TYPE.ORDER_TYPE_ALL.equals(orderType)) {//全部
            vos = busiOrderMapper.selelctAssetByCustomerIdAndStatus(map);
            // totalRecord=vos.size();
        }
        if (AppConstants.ORDER_TYPE.ORDER_TYPE_FINPLAN.equals(orderType)) {//智投计划
            map.put("orderType", "1");
            vos = busiOrderMapper.selelctAssetByCustomerIdAndStatus(map);
            totalRecord = vos.size();
        }
        if (AppConstants.ORDER_TYPE.ORDER_TYPE_PERSONAL_SUBJECT.equals(orderType)) {//散标
            map.put("orderType", "2");
            vos = busiOrderMapper.selelctAssetByCustomerIdAndStatus(map);
            totalRecord = vos.size();
        }
        if (AppConstants.ORDER_TYPE.ORDER_TYPE_NORMAL.equals(orderType)) {//定期
            map.put("orderType", "3");
            vos = busiOrderMapper.selelctAssetByCustomerIdAndStatus(map);
            totalRecord = vos.size();
        }
        if (AppConstants.ORDER_TYPE.ORDER_TYPE_TRANSFER.equals(orderType)) {//转让
            map.put("orderType", "4");
            vos = busiOrderMapper.selelctAssetByCustomerIdAndStatus(map);
            totalRecord = vos.size();
        }

        String repayType = "";
        for (UserAssetVo vo : vos) {
            if (vo.getBigDecimalRate() != null) {
                vo.setRate(vo.getBigDecimalRate().toString());
            }
            if (AppConstants.ProductTransferStatus.COMMON_PRODUCT.equals(vo.getIsTransfer())) {
                vo.setProjectDetailUrl(configParamBean.getTouchProductDetailsUrl() + "/productDetails?productId=" + vo.getProductId());
            }
            if (AppConstants.ProductTransferStatus.TRANSFER_PRODUCT.equals(vo.getIsTransfer())) {
                vo.setProjectDetailUrl(configParamBean.getTouchProductDetailsUrl() + "/transferDetails?productId=" + vo.getProductId());
            }
            Long orderId = vo.getOrderId();
            BusiOrderTemp busiOrderTemp = busiOrderTempService.selectViewByPrimaryKey(orderId);
            Long productId = busiOrderTemp.getProductId();
            Map<String, Object> paramsMap = new HashMap<>();
            paramsMap.put("currentDate", DateUtil.timeFormat(new Date(), DateUtil.fullFormat));
            paramsMap.put("productId", productId);
            BusiProduct busiProduct = busiProductService.findProductById(paramsMap);
            if (busiProduct == null) {
                continue;
            }
            repayType = vo.getRepayType();
            //若还款类型为等额本息或先息后本，增加回款计划
            if (StringUtils.isNotBlank(repayType) && !AppConstants.OrderPaymentType.ONE_TIME.equals(repayType)) {
                int term = 0;
                BigDecimal payPrincipalInterest = new BigDecimal(0);
                BigDecimal principal = new BigDecimal(0);
                BigDecimal payInterest = new BigDecimal(0);
                //已回款计划
                List<PaymentPlan> paymentPlanList = payPlanMapper.selectPaymentPlanById(busiOrderTemp.getOrderId());//orderNum
                if (CollectionUtils.isNotEmpty(paymentPlanList)) {
                    int returnedTerm = 0;
                    term = paymentPlanList.get(0).getTerm();
                    int isReturned = 1;//默认0-未还，1-已还
                    for (PaymentPlan payPlan : paymentPlanList) {
                        if (AppConstants.PaymentPlanStatus.UNRETURN.equals(payPlan.getRepayStatus()) || AppConstants.PaymentPlanStatus.RETURNING.equals(payPlan.getRepayStatus())) {
                            isReturned = 0;
                        }
                        //正常回款
                        if (AppConstants.PaymentPlanStatus.RETURNED.equals(payPlan.getRepayStatus())) {
                            payPrincipalInterest = payPlan.getPrincipalInterest().add(payPrincipalInterest);
                            payInterest = payInterest.add(payPlan.getInterest());
                            returnedTerm++;
                        }
                        //上家已回款，不记录收益
                        if (AppConstants.PaymentPlanStatus.RETURNED_TRANSFER_UP.equals(payPlan.getRepayStatus())) {
                            returnedTerm++;
                        }
                        //提前结清-已回
                        if (AppConstants.PaymentPlanStatus.RETURNED_AHEAD.equals(payPlan.getRepayStatus()) && isReturned == 1) {
                            payPrincipalInterest = payPlan.getPrincipal().add(payPrincipalInterest);
                            returnedTerm++;
                        }
                    }
                    vo.setPayAndTerm(returnedTerm + "/" + term);//已回/总期数
                    vo.setPayPrincipalInterest(payPrincipalInterest.setScale(2, BigDecimal.ROUND_DOWN));//本息和
                    vo.setPayInterest(payInterest.setScale(2, BigDecimal.ROUND_DOWN));//利息
                    vo.setPrincipal(principal.setScale(2, BigDecimal.ROUND_DOWN));//本金
                    vo.setInterest(payInterest.setScale(2, BigDecimal.ROUND_DOWN));
                } else {
                    vo.setPayAndTerm("0/0");
                }
                //单个标的产品代收利息
                Example example = new Example(PaymentPlan.class);
                example.createCriteria().andEqualTo("orderNum", busiOrderTemp.getOrderId());
                List<PaymentPlan> paylist = payPlanMapper.selectByExample(example);
                BigDecimal noPayTotalInterest = new BigDecimal(0);//待回利息
                for (PaymentPlan paymentPlan : paylist) {
                    //待回款
                    if (paymentPlan.getRepayStatus().equals(AppConstants.PaymentPlanStatus.UNRETURN) || paymentPlan.getRepayStatus().equals(AppConstants.PaymentPlanStatus.RETURNING)) {
                        noPayTotalInterest = paymentPlan.getInterest().add(noPayTotalInterest);
                    }
                }
                vo.setOrderTransferInterest(noPayTotalInterest.setScale(2, BigDecimal.ROUND_DOWN));

                //计算包含提前结清状态的回款计划
                if ("10".equals(vo.getStatus())) {
                    List<PaymentPlan> aheadList = payPlanMapper.selectPaymentPlansById(busiOrderTemp.getOrderId());//orderNUm
                    for (PaymentPlan plan : aheadList) {
                        if (plan.getRepayStatus().equals(AppConstants.PaymentPlanStatus.RETURNED_AHEAD)) {
                            vo.setPrincipalInterest(vo.getPrincipalInterest().subtract(plan.getInterest()));
                        }
                    }
                    //v4.7提前结清借款期限，最后借款日，最后到帐日
                    List<PaymentPlan> earlyPlanList = payPlanMapper.selectEarlyPayPlan(busiOrderTemp.getOrderId());//orderNUm
                    if (CollectionUtils.isNotEmpty(earlyPlanList)) {
                        PaymentPlan plan = earlyPlanList.get(0);
                        vo.setInterestEndDate(plan.getRepayDay());
                        vo.setExitActualDate(plan.getRealTime());
                        vo.setInvestPeriod(DateUtil.getIntervalDays2(plan.getRepayDay(), vo.getInterestStartDate()) + 1);
                    } else {
                        if (!"4".equals(vo.getSubjectType())) {
                            List<BusiSellOrder> sellOrders = sellOrderMapper.findSellOrderByOrderId(Long.parseLong(customerId), busiOrderTemp.getId());
                            if (CollectionUtils.isNotEmpty(sellOrders)) {
                                vo.setExitActualDate(sellOrders.get(0).getPayTime());
                            }
                        }
                    }

                }


            } else {
                //单个普通产品待收利息
                vo.setOrderTransferInterest(vo.getPrincipalInterest().subtract(vo.getOrderAmt()));
                //v4.7实际到帐时间(非理财计划)
                if ("10".equals(vo.getStatus()) && !"4".equals(vo.getSubjectType())) {
                    List<BusiSellOrder> sellOrders = sellOrderMapper.findSellOrderByOrderId(Long.parseLong(customerId), busiOrderTemp.getId());
                    if (CollectionUtils.isNotEmpty(sellOrders)) {
                        vo.setExitActualDate(sellOrders.get(0).getPayTime());
                    }
                }

            }
            //查询是否有转让产品记录
            Example example = new Example(BusiDebtTransfer.class);
            example.createCriteria().andEqualTo("initOrderNo", busiOrderTemp.getOrderId());
            //example.setOrderByClause("update_date desc");
            example.setOrderByClause("id desc");
            List<BusiDebtTransfer> transfersList = transferMapper.selectByExample(example);
            //转让人   个贷提前结清 （未交割 转让中 显示 已结清）
            if (transfersList != null && transfersList.size() > 0) {
                BusiDebtTransfer transfer = transfersList.get(0);
                if (vo.getStatus().equals("10") && (
                        transfer.getTransferStatus().equals(AppConstants.DebtTransferStatus.TRANSFER_DURING)
                                || transfer.getTransferStatus().equals(AppConstants.DebtTransferStatus.TRANSFER_FINISH))) {
                    vo.setStatusDesc("已结清");
                }
            }
            if (vo.getStatus().equals("2")) {// 待付款状态
                vo.setHasTransferStatus(AppConstants.HasTransferStatus.HAS_TRANSFER_PAY);
                vo.setOrderTransferDesc(vo.getStatusDesc());
                //单个普通产品待收利息
                vo.setOrderTransferInterest(vo.getPrincipalInterest().subtract(vo.getOrderAmt()));
            } else {
                if (vo.getTransferStatus().equals(AppConstants.ProductTransferProperty.CAN_NOT_TRANSFER)) {//不可转让产品
                    if (vo.getStatus().equals("4")) {//退款
                        vo.setHasTransferStatus(AppConstants.HasTransferStatus.HAS_NO_TRANSFER_END);
                        vo.setOrderTransferDesc(vo.getStatusDesc());
                    } else if (vo.getStatus().equals("10")) {
                        vo.setHasTransferStatus(AppConstants.HasTransferStatus.HAS_NO_TRANSFER_END);
                        vo.setOrderTransferDesc(vo.getStatusDesc());
                    } else {
                        vo.setHasTransferStatus(AppConstants.HasTransferStatus.HAS_TRANSFER_NORMAL);
                        vo.setOrderTransferDesc(vo.getStatusDesc());
                    }
                } else if (vo.getIsAddRate() != null && vo.getIsAddRate().intValue() == 1) {//使用加息劵的不能转让
                    vo.setHasTransferStatus(AppConstants.HasTransferStatus.HAS_TRANSFER_NORMAL);
                    vo.setOrderTransferDesc(vo.getStatusDesc());
                } else {
                    if (vo.getTransferStatus().equals("1") && vo.getStatus().equals("14") && judgeLastPlan(busiOrderTemp.getOrderId())) {
                        vo.setHasTransferStatus(AppConstants.HasTransferStatus.HAS_TRANSFER_NORMAL);
                        vo.setOrderTransferDesc(vo.getStatusDesc());
                    } else if (vo.getTransferStatus().equals("1") && CollectionUtils.isEmpty(transfersList) && vo.getIsTransferOrder().equals("0")) {
                        vo.setHasTransferStatus(AppConstants.HasTransferStatus.HAS_TRANSFER_SHOW_BUTTON);
                        vo.setOrderTransferDesc(vo.getStatusDesc());
                    } else if (vo.getTransferStatus().equals("1") && CollectionUtils.isNotEmpty(transfersList) && vo.getIsTransferOrder().equals("0") &&
                            (transfersList.get(0).getTransferStatus().equals("3") || transfersList.get(0).getTransferStatus().equals("4"))) {
                        vo.setHasTransferStatus(AppConstants.HasTransferStatus.HAS_TRANSFER_RESHOW_BUTTON);
                        //获取最新一条转让失败信息
                        if (transfersList.get(0).getTransferStatus().equals("3")) {
                            Map trMap = new HashMap();
                            trMap.put("orderNo", busiOrderTemp.getOrderId());
                            trMap.put("transferStatus", "3");
                            List<UserAssetVo> transferFails = busiOrderMapper.selectTransferFailRecord(trMap);
                            if (CollectionUtils.isNotEmpty(transferFails)) {
                                vo.setTransferPrice(transferFails.get(0).getTransferPrice());
                                vo.setTransferRate(transferFails.get(0).getTransferRate());
                                vo.setTransferDate(transferFails.get(0).getTransferDate());
                                vo.setPubDate(transferFails.get(0).getPubDate());
                            }
                            if (DateUtil.getIntervalDays2(new Date(), transferFails.get(0).getUpdateDate()) > 0) {
                                vo.setOrderTransferDesc(vo.getStatusDesc());
                                vo.setPubDate(transferFails.get(0).getPubDate());
                            } else {
                                vo.setOrderTransferDesc("转让失败");
                                vo.setPubDate(transferFails.get(0).getPubDate());
                            }
                            vo.setIsOneDay(transferFails.get(0).getIsOneDay());
                        }
                        if (transfersList.get(0).getTransferStatus().equals("4")) {
                            Map trMap = new HashMap();
                            trMap.put("orderNo", busiOrderTemp.getOrderId());
                            trMap.put("transferStatus", "4");
                            List<UserAssetVo> transferFails = busiOrderMapper.selectTransferFailRecord(trMap);
                            if (CollectionUtils.isNotEmpty(transferFails)) {
                                vo.setTransferPrice(transferFails.get(0).getTransferPrice());
                                vo.setTransferRate(transferFails.get(0).getTransferRate());
                                vo.setTransferDate(transferFails.get(0).getTransferDate());
                                vo.setPubDate(transferFails.get(0).getPubDate());
                                vo.setUpdateDate(transferFails.get(0).getUpdateDate());
                            }
                            if (DateUtil.getIntervalDays2(new Date(), transferFails.get(0).getUpdateDate()) > 0) {
                                vo.setOrderTransferDesc(vo.getStatusDesc());
                            } else {
                                vo.setOrderTransferDesc("审核未通过");
                            }
                            vo.setIsOneDay(transferFails.get(0).getIsOneDay());
                        }
                        //回款中显示普通状态
                    } else if (vo.getTransferStatus().equals("1") && vo.getIsTransferOrder().equals("1") && vo.getStatus().equals("9")) {
                        vo.setHasTransferStatus(AppConstants.HasTransferStatus.HAS_TRANSFER_NORMAL);
                        vo.setOrderTransferDesc(vo.getStatusDesc());
                    } else if (vo.getTransferStatus().equals("1") && vo.getIsTransferOrder().equals("1") && (vo.getStatus().equals("1") || vo.getStatus().equals("14") || vo.getStatus().equals("0"))) {
                        vo.setHasTransferStatus(AppConstants.HasTransferStatus.HAS_TRANSFER_DAY);
                        vo.setOrderTransferDesc(vo.getStatusDesc());
                    } else if (CollectionUtils.isNotEmpty(transfersList) && transfersList.get(0).getTransferStatus().equals("0")) {
                        vo.setHasTransferStatus(AppConstants.HasTransferStatus.HAS_TRANSFER_CANCEL_BUTTON);
                        String transferStatus = transfersList.get(0).getTransferStatus();

                        Map<String, Object> pMap = new HashMap<>();
                        pMap.put("currentDate", DateUtil.timeFormat(new Date(), DateUtil.fullFormat));
                        pMap.put("productId", transfersList.get(0).getProductId());
                        BusiProduct bp = busiProductService.findProductById(pMap);

                        //判断上架时间30分钟
                        if (DateUtil.addMinutes(new Date(), 2).after(bp.getShowStartDate())) {
                            vo.setOrderTransferStatus("0");
                            vo.setOrderTransferDesc("转让-已上架");
                        } else {
                            vo.setOrderTransferStatus("0");
                            vo.setOrderTransferDesc("转让-审核中");
                        }
                        getTransferInfo(transfersList, vo);

                    } else if (CollectionUtils.isNotEmpty(transfersList) && !transfersList.get(0).getTransferStatus().equals("0") && (transfersList.get(0).getTransferStatus().equals("1") || transfersList.get(0).getTransferStatus().equals("2"))) {
                        vo.setHasTransferStatus(AppConstants.HasTransferStatus.HAS_TRANSFER_HIDE_BUTTON);
                        vo.setOrderTransferDesc("转让-交易中");

                        getTransferInfo(transfersList, vo);
                    } else if (vo.getTransferStatus().equals("1") && vo.getStatus().equals("16") && transfersList.get(0).getTransferStatus().equals("5")) {
                        vo.setHasTransferStatus(AppConstants.HasTransferStatus.HAS_TRANSFER_SUCCESS);
                        vo.setOrderTransferDesc("已转让");
                        getTransferInfo(transfersList, vo);
                    } else if (vo.getTransferStatus().equals("1") && CollectionUtils.isEmpty(transfersList) && vo.getStatus().equals("10")) {
                        vo.setHasTransferStatus(AppConstants.HasTransferStatus.HAS_TRANSFER_NOLOG_END);
                        vo.setOrderTransferDesc(vo.getStatusDesc());
                    } else if (vo.getTransferStatus().equals("1") && CollectionUtils.isNotEmpty(transfersList) && vo.getStatus().equals("10")) {
                        vo.setHasTransferStatus(AppConstants.HasTransferStatus.HAS_TRANSFER_FAIL_END);
                        vo.setOrderTransferDesc(vo.getStatusDesc());
                    } else if (vo.getTransferStatus().equals("1") && vo.getStatus().equals("4")) {
                        //可转让产品 退款
                        vo.setHasTransferStatus(AppConstants.HasTransferStatus.HAS_NO_TRANSFER_END);
                        vo.setOrderTransferDesc(vo.getStatusDesc());
                    }
                }
            }

            Long contractId = busiProduct.getContractId();

            if (!AppConstants.ProductSubjectType.SUBJECT_YX.equals(busiProduct.getSubjectType())) {
                vo.setType("loan");
                BusiProductContract contract = busiProductContractMapper.selectByPrimaryKey(contractId);
                Date loanDate = null;
                if (contract != null) {
                    loanDate = contract.getLoanDate();
                }
                if (loanDate != null && loanDate.before(DateTime.now().toDate())) {//放款后才展示协议
                    String contractType = busiProduct.getContractType();
                    if (AppConstants.ContractType.SUBJECT.equals(contractType)) {
                        if (AppConstants.PersonalLoan.PERSONAL_LOAN == busiProduct.getPersonLoan()) {
                            vo.setHasTrans(5);
                            vo.setAgreementUrl(configParamBean.getTradeAgreementUrl() + "?customerId=" + customerMainInfo.getId() + "&orderId=" + vo.getOrderId() + "&type=loan&hasTrans=5&subjectNo=" + busiProduct.getSubjectNo());//标的借款协议 委托协议
                        } else {
                            vo.setHasTrans(3);
                            vo.setAgreementUrl(configParamBean.getTradeAgreementUrl() + "?orderId=" + vo.getOrderId() + "&type=loan&hasTrans=3");//标的借款协议
                        }

                    } else {
                        vo.setHasTrans(4);
                        vo.setAgreementUrl(configParamBean.getTradeAgreementUrl() + "?orderId=" + vo.getOrderId() + "&type=loan&hasTrans=4");//标的债权转让协议
                    }
                }

            } else {//捞财宝产品
                vo.setType("lcb");
                AtCh atCh = new AtCh();
                atCh.setOrderId(new BigDecimal(vo.getOrderId()));
                List<AtCh> atChes = atChMapper.select(atCh);
                if (atChes.isEmpty()) {

                    vo.setHasTrans(0);
                    vo.setAgreementUrl(configParamBean.getTradeAgreementUrl() + "?orderId=" + vo.getOrderId() + "&type=lcb&hasTrans=0");//无捞财宝债权转让协议
                } else {
                    vo.setHasTrans(1);
                    vo.setAgreementUrl(configParamBean.getTradeAgreementUrl() + "?orderId=" + vo.getOrderId() + "&type=lcb&hasTrans=1");//捞财宝债权转让协议
                }
            }
            //增加用户转让协议
            if (StringUtils.isNotBlank(vo.getAgreementUrl())) {
                String defaultUrl = "&flag=0&transferNo=&downOrderId=";
                BusiDebtTransfer transfer = busiDebtTransferService.getSuccessBusiDebtTransfer(busiOrderTemp.getCustomerId(), busiOrderTemp.getOrderId(), AppConstants.OrderTransferDirect.ORDER_TRANSFER_TRANSFER);
                String tempStr = "";
                if (transfer != null) {
                    BusiOrderTemp temp = busiOrderTempMapper.selectViewByOrderNo(transfer.getNewOrderNo());
                    Long tempId = temp == null ? null : temp.getId();
                    BusiOrderTemp tempOrigin = busiOrderTempMapper.selectViewByOrderNo(transfer.getOriginOrderNo());
                    Long tempOriginId = tempOrigin == null ? null : tempOrigin.getId();
                    defaultUrl = "&flag=1&transferNo=" + transfer.getTransferNo() + "&downOrderId=" + tempId + "&originOrderId=" + tempOriginId;
                    tempStr += "&transferNo=" + transfer.getTransferNo() + "&transferOrderId=" + tempId + "&originOrderId=" + tempOriginId;
                }
                BusiDebtTransfer transferee = busiDebtTransferService.getSuccessBusiDebtTransfer(busiOrderTemp.getCustomerId(), busiOrderTemp.getOrderId(), AppConstants.OrderTransferDirect.ORDER_TRANSFER_TRANSFEREE);
                if (transferee != null) {
                    BusiOrderTemp temp = busiOrderTempMapper.selectViewByOrderNo(transferee.getNewOrderNo());
                    Long tempId = temp == null ? null : temp.getId();
                    BusiOrderTemp tempOrigin = busiOrderTempMapper.selectViewByOrderNo(transferee.getOriginOrderNo());
                    Long tempOriginId = tempOrigin == null ? null : tempOrigin.getId();
                    defaultUrl = "&flag=2&transferNo=" + transferee.getTransferNo() + "&downOrderId=" + tempId + "&originOrderId=" + tempOriginId;
                    tempStr += "&transfereeNo=" + transferee.getTransferNo() + "&transfereeOrderId=" + tempId + "&originOrderId=" + tempOriginId;
                }
                if (transfer != null && transferee != null) {
                    defaultUrl = "&flag=3" + tempStr;
                }
                vo.setAgreementUrl(vo.getAgreementUrl() + defaultUrl);
            }
            vo.setTotalAddRate(getTotalAddRate(vo.getActionRate(), vo.getInviteRate()).multiply(new BigDecimal(100)));

            //理财计划 -- subjectType:4
            getFinPlanUserAseetVo(vo, busiOrderTemp);
            //获取单个订单本金及利息
            getOrderPrincipalOrInterest(vo, busiOrderTemp, customerMainInfo, model_520004.getIsHold());
            //散标新增借款人信息
            if ("3".equals(vo.getSubjectType()) && "0".equals(vo.getTransferType())) {
                vo.setBorrowerUrl(configParamBean.getTouchProductDetailsUrl() + "/borrower?productId=" + vo.getProductId() + "&subjectType=3&track=1");
            }
            //优选提前结清订单
            if ("1".equals(vo.getSubjectType()) && AppConstants.BusiOrderStatus.BUSIORDER_STATUS_10.equals(vo.getStatus())) {
                BusiOrderToPay orderToPay = orderToPayMapper.getBusiOrdertToPay(busiOrderTemp.getOrderId());
                if (orderToPay != null && orderToPay.getPrincipalInterest() != null) {
                    vo.setPrincipalInterest(orderToPay.getPrincipalInterest());
                    vo.setEarlyExitDate(orderToPay.getExitDate());
                    //新增提前结清产品展示协议
                    vo.setAgreementUrl(configParamBean.getTradeAgreementUrl() + "?orderId=" + vo.getOrderId() + "&exitType=orderExit&type=lcb");
                }

            }
        }
        page.setResults(vos);
        page.setTotalRecord(totalRecord);
        dto.setUserAssetVos(page);

        //计算用户已收总利息
        BigDecimal totalInterest = this.computeHistoryInterest(customerMainInfo.getId());
        dto.setCollectedInterest(CoreUtil.BigDecimalAccurate(totalInterest));
        //计算用户持有资产及待收总利息
        UserUnReceiveAsset unReceiveAsset = accountOverview520003Service.getHoldAsset(customerMainInfo);
        dto.setHoldAsset(CoreUtil.BigDecimalAccurate(unReceiveAsset.getUnReceivePrinciple()));
        dto.setUnCollectInterest(CoreUtil.BigDecimalAccurate(unReceiveAsset.getUnReceiveInterest()));

        //待收利息，待收加息
        UserUnReceiveAsset noReceiveAsset = accountOverview520003Service.getNoRecieveAmt(customerMainInfo);
        dto.setNoRecieveAddInterest(CoreUtil.BigDecimalAccurate(noReceiveAsset.getNoReceiveInterest()));
        dto.setNoRecieveInterest(CoreUtil.BigDecimalAccurate(unReceiveAsset.getUnReceiveInterest().subtract(noReceiveAsset.getNoReceiveInterest())));
        //已收利息，已收加息
        UserUnReceiveAsset receiveAsset = accountOverview520003Service.getTotalCouponAndIntegralAmt(customerMainInfo);
        dto.setRecieveAddInterest(CoreUtil.BigDecimalAccurate(receiveAsset.getReceiveInterest()));
        dto.setRecieveInterest(CoreUtil.BigDecimalAccurate(totalInterest.subtract(receiveAsset.getReceiveInterest())));

        return Result.success(dto);
    }

    /**
     * 获取单个订单本金及利息-散标（个贷+标的）
     */
    private UserAssetVo getOrderPrincipalOrInterest(UserAssetVo vo, BusiOrderTemp busiOrderTemp, CustomerMainInfo customerMainInfo, String isHold) throws Exception {
        if ("2".equals(vo.getSubjectType()) || "3".equals(vo.getSubjectType()) || "1".equals(vo.getTransferType())) {
            //持有
            if ("0".equals(isHold)) {
                UserUnReceiveAsset unReceiveAsset = accountOverview520003Service.getHoldAssets(customerMainInfo, busiOrderTemp);
                vo.setOrderAmt(unReceiveAsset.getUnReceivePrinciple());
                vo.setPrincipalInterest(unReceiveAsset.getUnReceivePrinciple().add(unReceiveAsset.getUnReceiveInterest()));
            }
            //历史
            if ("1".equals(isHold)) {
                UserRepaymentVo repaymentVo = computeOrderHistoryInterest(customerMainInfo.getId(), busiOrderTemp.getOrderId());
                if (repaymentVo != null) {
                    // vo.setOrderAmt(repaymentVo.getReceivedPrincipal());
                    // vo.setPrincipalInterest(repaymentVo.getReceivedPrincipal().add(repaymentVo.getReceivedInterest()));
                    vo.setPrincipalInterest(vo.getOrderAmt().add(repaymentVo.getReceivedInterest()));

                }
            }
        }
        //退款,已收本金、本息取订单金额
        if ("4".equals(vo.getStatus())) {
            vo.setOrderAmt(vo.getOrderAmt());
            vo.setPrincipalInterest(vo.getOrderAmt());
        }
        return vo;
    }

    /**
     * 获取续约订单初始化
     *
     * @param reqMain
     * @return
     */
    @Override
    public Result orderContinuedInit(ReqMain reqMain) {
        Model_521004 model = (Model_521004) reqMain.getReqParam();
        BusiOrderProductVo busiOrder = busiOrderMapper.selectOrderProductByPrimaryKey(model.getOrderId());
        List list = Lists.newArrayList();
        CustomerMainInfo customerMainInfo = customerMainInfoService.checkCustomerId(busiOrder.getCustomerId());
        String regulations;
        if ("1".equals(customerMainInfo.getUserLabel())) {
            regulations = configParamBean.getReinvestAccountantRegulations();
        } else {
            regulations = configParamBean.getReinvestNetworkuserRegulations();
        }

        if (busiOrder.getCloseDays().longValue() >= (Long.valueOf(regulations).longValue())) {
            OrderReinvestConstants orderReinvestConstant = orderReinvestConstants.getOrderReinvestTypeByType("0");
            OrderContinuedInitVO continueOrder = getContinueOrder(busiOrder, orderReinvestConstant);
            OrderReinvestConstants orderReinvestConstant1 = orderReinvestConstants.getOrderReinvestTypeByType("1");
            OrderContinuedInitVO continueOrder1 = getContinueOrder(busiOrder, orderReinvestConstant1);
            list.add(continueOrder);
            list.add(continueOrder1);
        }
        return Result.success(list);
    }

    public OrderContinuedInitVO getContinueOrder(BusiOrderProductVo busiOrder, OrderReinvestConstants orderReinvestConstant) {

        OrderContinuedInitVO continueOrder = new OrderContinuedInitVO();
        BeanUtils.copyProperties(busiOrder, continueOrder);
        //在投到收益
        continueOrder.setInterest(busiOrder.getPrincipalinterest().subtract(busiOrder.getOrderAmt()));
        //在投年利率
        continueOrder.setYearRateStr(busiOrder.getYearRate().multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_DOWN).toString() + "%");
        continueOrder.setReinvestName(orderReinvestConstant.getReinvestName());
        continueOrder.setRateDiffer(orderReinvestConstant.getRateDiffer());
        //续期服务期限
        continueOrder.setCloseDayExt(orderReinvestConstant.getDays());
        //续期年化收益率
        continueOrder.setReinvestYearRate(orderReinvestConstant.getRate());
        continueOrder.setReinvestYearRateStr(orderReinvestConstant.getRate().multiply(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_DOWN).toString() + "%");
        //封闭结束期
        continueOrder.setInterestEndDateExt(DateUtil.plusDay(busiOrder.getInterestEndDate(), orderReinvestConstant.getDays()));
        //续约类型
        continueOrder.setReinvestType(orderReinvestConstant.getType());
        //续期预期收益
        continueOrder.setContinuedInterest(
                busiOrderService.calculationInterest(continueOrder.getPrincipalinterest(), orderReinvestConstant.getRate(), orderReinvestConstant.getDays(), BigDecimal.ZERO));
        //续约后到期本息
        continueOrder.setReinvestPrincipalInterest(continueOrder.getPrincipalinterest().add(continueOrder.getContinuedInterest()));

        return continueOrder;
    }

    /**
     * 获取可续约订单列表
     *
     * @param reqMain
     * @return
     */
    @Override
    public Result unableOrderContinuedList(ReqMain reqMain) throws Exception {
        Model_521003 cdtModel = (Model_521003) reqMain.getReqParam();
        String userToken = org.apache.commons.lang3.StringUtils.trim(cdtModel.getUserToken());
        CustomerMainInfo mainInfo = customerMainInfoService.findOneBySessionToken(userToken);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map hashMap = Maps.newHashMap();
        List<BusiOrderProductVo> busiOrderList = Lists.newArrayList();
        if (DateTime.now().toDate().compareTo(sdf.parse(configParamBean.getPromotionStartDate())) >= 0 && DateTime.now().toDate().compareTo(sdf.parse(configParamBean.getPromotionEndDate())) <= 0) {
            Map map = Maps.newTreeMap();
            map.put("customerId", mainInfo.getId());
            map.put("startDate", configParamBean.getRangeInterestStartDate());
            map.put("endDate", configParamBean.getRangeInterestEndDate());
            if ("1".equals(mainInfo.getUserLabel())) {
                map.put("regulations", configParamBean.getReinvestAccountantRegulations());
            } else {
                map.put("regulations", configParamBean.getReinvestNetworkuserRegulations());
            }
            busiOrderList = busiOrderMapper.getContinuedOrderList(map);
            hashMap.put("busiOrderList", busiOrderList);
            hashMap.put("promotionEndDate", DateUtil.getDistanceTime(DateTime.now().toDate(), sdf.parse(configParamBean.getPromotionEndDate())));
            hashMap.put("REINVEST_360DAYS", orderReinvestConstants.getOrderReinvestTypeByType("0").getRate());
            hashMap.put("REINVEST_180DAYS", orderReinvestConstants.getOrderReinvestTypeByType("1").getRate());
        } else {
            throw new BusinessException("暂无可续约订单！");
        }
        return Result.success(hashMap);
    }
}