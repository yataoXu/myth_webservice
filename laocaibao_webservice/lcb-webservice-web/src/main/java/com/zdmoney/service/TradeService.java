package com.zdmoney.service;

import com.zdmoney.common.Result;
import com.zdmoney.integral.api.dto.lcbaccount.bid.BorrowTenderDto;
import com.zdmoney.integral.api.dto.lcbaccount.bid.BorrowTenderResultDto;
import com.zdmoney.integral.api.dto.lcbaccount.bid.BorrowTransferDto;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.financePlan.BusiOrderSub;
import com.zdmoney.models.product.BusiProduct;
import com.zdmoney.models.product.BusiProductContract;
import com.zdmoney.models.product.BusiProductSub;
import com.zdmoney.models.trade.BusiCollectInfo;
import com.zdmoney.models.transfer.BusiDebtTransfer;
import com.zdmoney.vo.trade.TenderVo;
import com.zdmoney.web.TransferResultDTO;
import com.zdmoney.webservice.api.dto.plan.CollectOrderInfoDTO;
import com.zdmoney.webservice.api.dto.plan.HRbidTenderCollectDto;
import com.zdmoney.webservice.api.dto.plan.LoanReviewDTO;
import websvc.req.ReqMain;

import java.math.BigDecimal;

/**
 * Created by 00225181 on 2015/12/1.
 * 2.0 交易相关接口
 */
public interface TradeService {

    /*
     *2.0支付
     */
    Result pay(Long customerId, Long orderId, String integralAmt, String redId, String voucherId, String payPassword,boolean isNeedPayPwd, ReqMain reqMain) throws Exception;

    /*
     *2.0提现
     */
    Result withDraw(ReqMain reqMain) throws Exception;


    /**
     * 收银台接口
     */
    Result cashierPay(ReqMain reqMain)throws Exception;

    /**
     * 收银台初始化接口
     */
    Result cashierPayInit(ReqMain reqMain)throws Exception;


    void sendMsg(BusiOrderSub order, CustomerMainInfo customerMainInfo, Boolean isFirst,BusiProductSub productSub);

    /**
     * 平台运营数据
     * @return
     * @throws Exception
     */
    Result findOperationDataStatistics(ReqMain reqMain)throws Exception;


    /**
     * 债权转让
     * @param initOrderSub 原出让订单编号
     * @param orderSub  购买订单
     * @param mainInfo 购买用户
     * @param debtAmount  债权价值
     * @param transferNum  一转多的数目
     * @return
     */
    BusiDebtTransfer planTransfer(BusiOrderSub initOrderSub, BusiOrderSub orderSub, CustomerMainInfo mainInfo, BigDecimal debtAmount, int transferNum);

    String bidTransfer(BusiOrderSub orderSub, BusiOrderSub initOrderSub,BusiOrderSub originOrder, CustomerMainInfo mainInfo,BigDecimal transferFee,BigDecimal debtAmount);

    com.zdmoney.common.ResultDto<TransferResultDTO> doTransferCredit(Long transferOrderId, Long transfereeOrderId, BigDecimal transferFee, BigDecimal creditAmount);

    void finishCreditTransfer();

    String tender(TenderVo tenderVo, BusiOrderSub orderSub,BusiProductSub busiProductSub);

    void transferRefund(BusiOrderSub orderSub,BusiProductSub busiProductSub);

    BusiProductSub bidBuildProduct(BusiProductContract productContract, BusiProduct busiProduct);


    BusiProductSub bidBuildProductForWacai(BusiProductContract productContract, BusiProduct busiProduct);

    String financePlanSendCommission(String orderNum,String cmNumber,BigDecimal welfareAmt);

    boolean bidTenderRepeal(BusiOrderSub orderSub,String tenderNo);

    String anewBidTransfer(BusiOrderSub orderSub,BusiOrderSub initOrderSub,BusiOrderSub originOrder,CustomerMainInfo mainInfo);

    boolean bidTransferRepeal(BusiOrderSub orderSub,String flowNum);


    /**
     * 续投
     * @param customerId
     * @param orderId
     * @param reinvestType
     * @return
     * @throws Exception
     */
    Result reinvestment(Long customerId, Long orderId , String reinvestType)throws Exception;

    /**
     * 华瑞投标
     * @param tenderDto
     * @return
     */
    BorrowTenderResultDto commonBidTender(BorrowTenderDto tenderDto);

    /**
     * 投标参数
     * @param tenderVo
     * @param orderSub
     * @return
     */
    BorrowTenderDto commonTenderDto(TenderVo tenderVo,BusiOrderSub orderSub);

    BorrowTransferDto commonGetTransferDto(BusiOrderSub orderSub, BusiOrderSub initOrderSub,BusiOrderSub originOrder, CustomerMainInfo mainInfo, BigDecimal transferFee,BigDecimal debtAmount);


    /**
     *华瑞投标归集(放款初审)
     */
    boolean bidTenderCollect(HRbidTenderCollectDto hRbidTenderCollectDto);

    boolean bidTenderCollect(HRbidTenderCollectDto hRbidTenderCollectDto,boolean isRetry);

    /**
     *华瑞投标归集(放款初审) 回调通知
     */
    void bidTenderCollectCallback(CollectOrderInfoDTO record);

    /**
     *放款归集(放款复审)
     */
    Result timeTenderCollect(LoanReviewDTO loanReviewDTO);

    Result timeTenderCollect(LoanReviewDTO loanReviewDTO,boolean isRetry);

    /**
     *放款归集(放款复审) 回调通知
     */
    void timeTenderCollectCallback(CollectOrderInfoDTO record);


    /**
     * 提供给标的反查接口
     * type 归集类型，0：华瑞投标归集(放款初审)；1：放款归集(放款复审)；
     * @param record
     * @return
     */
    BusiCollectInfo queryCollectOrderInfo(CollectOrderInfoDTO record);

    /**
     * 公共建标
     * @param busiProductContract
     * @param busiProduct
     * @return
     */
    com.zdmoney.webservice.api.common.dto.ResultDto bidBuild(BusiProductContract busiProductContract, BusiProduct busiProduct);


    void notifyMessageSuccess(BusiDebtTransfer debtTransfer);
}
