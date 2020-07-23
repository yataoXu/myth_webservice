package com.zdmoney.service;

import com.zdmoney.models.*;
import com.zdmoney.models.financePlan.BusiFundDetail;
import com.zdmoney.models.BusiMatchSucInfo;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.financePlan.BusiFundDetail;
import com.zdmoney.models.financePlan.BusiOrderSub;
import com.zdmoney.models.product.BusiProduct;
import com.zdmoney.models.product.BusiProductSub;
import com.zdmoney.utils.Page;
import com.zdmoney.vo.BusiDebtDetailVo;
import com.zdmoney.webservice.api.common.dto.PageResultDto;
import com.zdmoney.webservice.api.common.dto.ResultDto;
import com.zdmoney.webservice.api.dto.plan.*;

import java.util.List;
import java.util.Map;


/**
 *
 * Created by gaol on 2017/6/5
 **/
public interface BusiFinancePlanService {

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
     * 产品详情--借款人明细列表（app）
     * @return
     * @throws Exception
     */
    Page<BusiDebtDetailVo> findBusiDebtDetails(int pageNo, int pageSize, String productId);

    /**
     * 插入债权详情
     * @param busiDebtDetail
     * @return
     */
    int insertDebtDetail(BusiDebtDetailVO busiDebtDetail);

    /**
     * 查询特殊理财人编号
     * @return
     */
    SpecialFinancialPlannerVO querySpecialFinancePeopleCode();

    /**
     * 理财计划订单产品详情--借款人明细列表（app）
     * @return
     * @throws Exception
     */
    Page<BusiDebtDetailVo> findOrderBusiDebtDetails(int pageNo, int pageSize, String orderId);

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
     * 撤销已撮合订单
     * @param productCode
     * @return
     */
    ResultDto sendRevokeProduct(String productCode);

    /**
     * 债权匹配
     */
    boolean debtMatch(DebtMatchReqDTO debtMatchReqDTO);

    /**
     * 根据撮合结果调用子产品下单
     * @param matchResultDto 撮合订单DTO
     * @return
     */
    ResultDto matchResultOrder(BusiMatchResultInfo matchResultDto);

    /**
     * 定时任务，批量撮合结果调用子产品下单
     * @return
     */
    ResultDto matchResultOrderTask();

    void updateMatchResultInfoStatusByInitStatus(List<BusiMatchResultInfo> matchResultInfos ,String status, String initStatus);

    /**
     * 根据撮合批次下单
     * @param matchResultInfo
     * @param isRepair
     * @return
     */
    ResultDto matchResultOrderPurchase(BusiMatchResultInfo matchResultInfo,boolean isRepair);


    /*根据主订单维度并发投标*/
    void tenderByMainOrder(Long orderId, BusiProduct busiProduct, boolean newLend, Map<String, BusiFundDetail> fundMap , Map<Long, List<BusiMatchSucInfo>> matchMap, Map<Long, BusiOrderSub> orderMap, Map<Long, CustomerMainInfo> mainInfoMap, BusiMatchResultInfo matchResultDto, Map<String, Object> productSubMap,Map<Long, List<BusiAbnorMatchSucInfo>> abnormatchMap);


    /**
     * 特殊理财人兜底下单
     * @param matchSpecialReqDto
     * @return
     */
    ResultDto<MatchSpecialResultDto> matchSpecialResultOrder(MatchSpecialReqDto matchSpecialReqDto);

    /**
     * 根据id查询债权信息
     * @param id
     * @return
     */
    BusiDebtDetailVO selectDebtDetailByPrimaryKey(Long id);

    /**
     * 根据id查询子订单信息
     * @param id
     * @return
     */
    BusiOrderSub selectOrderSubByPrimaryKey(Long id);

    /**
     * 推送债权信息
     * @param debtQueueDTO
     * @return
     */
    int sendDebtInfo(List<DebtQueueDTO> debtQueueDTO);

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
     * 撤标
     * @param orderNo
     * @param tenderNo
     * @return
     */
    ResultDto bidTenderRepeal(String orderNo, String tenderNo);

    /**
     * 修复转让
     * @param newOrderNo
     * @param initOrderNo
     * @return
     */
    ResultDto bidTransferRepeal(String newOrderNo,String initOrderNo,String flowNum);

    PageResultDto<BusiMatchExceptionVO> queryMatchException(MatchExceptionDTO matchExceptionDTO);


    /**
     * 更新撮合异常
     * @param map
     * @return
     */
    ResultDto updateMatchExceptionByMap(Map<String,Object> map);

    long saveMatchResult(MatchResultDto matchResultDto);

    Map<Long,Object> repairMatchOrder(String financeIds);

    PageResultDto<MatchFlowVO> queryMatchFlow(MatchFlowDTO matchFlowDTO);

    /**
     * 查询特殊理财人列表
     * @return
     */
    List<SpecialFinancialPlannerVO> querySpecialFinancePeopleList();

    /**
     * 获取默认特殊理财人编号
     * @return
     */
    String gainDefaultSpecialLender();

    /**
     * 获取特殊理财人编号列表
     * @return
     */
    List<String> gainSpecialLenders();

    /**
     * 根据理财计划id获取匹配信数量
     */
    long debtMatchNums(Long productId);

    /**
     *创建理财子产品信息
     * @param createFlag 资金类型是否为新出借类型
     * @param debtFlag 是否为新标的
     * @param subjectNo 撮合订单ID
     * @param busiProduct  理财计划产品
     * @return
     */
    BusiProductSub createSubProduct(Boolean createFlag, Boolean debtFlag, String subjectNo, BusiProduct busiProduct);


    /**
     * 产品星级转换购买人条件
     * @param productRank
     * @return
     */
    String  rankTransfer(Long productRank);
}

