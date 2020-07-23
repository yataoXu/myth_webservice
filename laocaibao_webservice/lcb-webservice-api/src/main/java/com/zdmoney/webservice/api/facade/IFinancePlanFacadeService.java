package com.zdmoney.webservice.api.facade;

import com.zdmoney.webservice.api.common.dto.PageResultDto;
import com.zdmoney.webservice.api.common.dto.ResultDto;
import com.zdmoney.webservice.api.dto.plan.*;

import java.util.List;
import java.util.Map;

/**
 * Created by 00232384 on 2017/6/13.
 */
public interface IFinancePlanFacadeService {

    /**
     * 获取特殊理财人编号
     * @return
     */
    ResultDto<SpecialFinancialPlannerVO> gainSpecialFinancialPlannerInfo();

    /**
     * 插入债权详情
     * @param busiDebtDetail
     * @return
     */
    ResultDto insertDebtDetail(BusiDebtDetailVO busiDebtDetail);

    /**
     * 发送理财计划产品
     */
    ResultDto sendProduct(SendProductReqDTO sendProductDTO);

    /**
     * 异常回购
     * @param subjectNo
     */
    ResultDto<BuyBackDTO> buyBack(String subjectNo);

    /**
     * 查询债权详情
     * @param debtDetailDTO
     * @return
     */
    PageResultDto<BusiDebtDetailVO> queryDeptProductDetail(DebtDetailReqDTO debtDetailDTO);

    /**
     * 查询资金详情
     * @param fundDetailDTO
     * @return
     */
    PageResultDto<BusiFundDetailVO> queryFundDetail(FundDetailReqDTO fundDetailDTO);

    /**
     * 解散理财计划产品
     * @param productCode
     * @return
     */
    ResultDto sendRevokeProduct(String productCode);

    /**
     * 债权匹配
     */
    ResultDto debtMatch(DebtMatchReqDTO debtMatchReqDTO);

    /**
     * 根据理财计划查询债权数量
     */
    ResultDto debtMatchNums(Long productId);

    /**
     * 根据撮合结果调用子产品下单
     * @param matchResultDto
     * @return
     */
    ResultDto matchResultOrder(MatchResultDto matchResultDto);

    /**
     * 撮合定时任务
     * @return
     */
    ResultDto matchResultOrderTask();

    ResultDto bidTenderRepeal(String orderNo,String tenderNo);

    ResultDto repairMatchOrder(String financeIds);

    ResultDto bidTransferRepeal(String newOrderNo,String initOrderNo,String flowNum);

    /**
     * 标的撮合兜底通知
     * @param matchSpecialReqDto
     * @return
     */
    ResultDto<MatchSpecialResultDto> matchSpecialResultOrder(MatchSpecialReqDto matchSpecialReqDto);

    /**
     * 推送债权信息
     * 债权类型 1-债权到期转让 2-提前退出转让
     * @param debtQueueDTO
     * @return
     */
    ResultDto sendDebtInfo(List<DebtQueueDTO> debtQueueDTO);

    /**
     * 特殊理财人转让
     *
     * @param specialTransferDebtList
     * @return
     */
    ResultDto specialTransfer(List<SpecialTransferDebtDTO> specialTransferDebtList);

    /**
     * 特殊理财人 异常回购
     * @param matchResultDto
     * @return
     */
    ResultDto specialBuyBackOrder(MatchResultDto matchResultDto);

    /**
     * 提前退出
     * manager job
     * @return
     */
    ResultDto earlyOut();

    /**
     * 查询撮合异常流水
     * @param matchExceptionDTO
     * @return
     */
    PageResultDto<BusiMatchExceptionVO> queryMatchException(MatchExceptionDTO matchExceptionDTO);

    /**
     * 查询撮合流水
     * @param matchFlowDTO
     * @return
     */
    PageResultDto<MatchFlowVO> queryMatchFlow(MatchFlowDTO matchFlowDTO);

    /**
     * 更新撮合异常
     * @param map
     * @return
     */
    ResultDto updateMatchExceptionByMap(Map<String,Object> map);

    /**
     * 获取特殊理财人列表
     * @return
     */
    ResultDto<List<SpecialFinancialPlannerVO>> gainSpecialFinancialPlannerInfoList();


    /**
     * 存管改造
     *华瑞投标归集(放款初审)
     */
    ResultDto bidTenderCollect(HRbidTenderCollectDto hRbidTenderCollectDto);

    /**
     * 存管改造
     *华瑞投标归集(放款初审) 回调通知
     */
    void bidTenderCollectCallback(CollectOrderInfoDTO record);

    /**
     * 存管改造
     *放款归集(放款复审)
     */
    ResultDto timeTenderCollect(LoanReviewDTO loanReviewDTO);

    /**
     *放款归集(放款复审)  回调通知
     */
    void timeTenderCollectCallback(CollectOrderInfoDTO record);

    /**
     * 存管改造
     * 反查归集审核结果
     */
    ResultDto queryCollectionReviewResult(CollectOrderInfoDTO record);


    /**
     * 放款初审、复审重试接口
     * type 归集类型，0：华瑞投标归集(放款初审)；1：放款归集(放款复审)；
     * @param record
     * @return
     */
    ResultDto bidTenderRetryCollect(BidTenderRetryCollectDto record);



    /**
     * 定时生成产品任务
     * @return
     */
    void generateProductTask();

    ResultDto<BidOrderInfoDto> getOrderDetail(String orderNo);
}
