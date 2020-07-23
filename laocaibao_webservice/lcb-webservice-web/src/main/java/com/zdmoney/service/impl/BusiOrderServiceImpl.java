package com.zdmoney.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.zdmoney.assets.api.common.dto.AssetsResultDto;
import com.zdmoney.assets.api.dto.subject.origin.RemainRepayDto;
import com.zdmoney.assets.api.facade.subject.IOriginSubjectFacadeService;
import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.common.Result;
import com.zdmoney.common.ResultDto;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.constant.Constants;
import com.zdmoney.constant.OrderConstants;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.life.api.utils.DateUtils;
import com.zdmoney.mapper.financePlan.BusiOrderSubMapper;
import com.zdmoney.mapper.financePlan.FundDetailMapper;
import com.zdmoney.mapper.order.BusiOrderMapper;
import com.zdmoney.mapper.product.BusiProductMapper;
import com.zdmoney.mapper.zdpay.CustomerGrantInfoMapper;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.financePlan.BusiOrderSub;
import com.zdmoney.models.order.BusiOrder;
import com.zdmoney.models.order.BusiOrderSubVo;
import com.zdmoney.models.product.BusiProduct;
import com.zdmoney.models.product.BusiProductSub;
import com.zdmoney.models.sys.SysParameter;
import com.zdmoney.models.zdpay.UserAuthDto;
import com.zdmoney.models.zdpay.UserGrantBO;
import com.zdmoney.service.BusiOrderService;
import com.zdmoney.service.CustomerMainInfoService;
import com.zdmoney.service.SysParameterService;
import com.zdmoney.service.TradeService;
import com.zdmoney.service.order.OrderService;
import com.zdmoney.session.RedisSessionManager;
import com.zdmoney.utils.*;
import com.zdmoney.vo.*;


import com.zdmoney.web.dto.Pay20DTO;
import com.zdmoney.webservice.api.dto.finance.QueryOrderReqDto;
import lombok.extern.slf4j.Slf4j;
import com.zdmoney.webservice.api.dto.finance.BusiOrderDto;
import com.zdmoney.webservice.api.dto.finance.BusiOrderSumDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import com.zdmoney.webservice.api.common.dto.PageResultDto;
import com.zdmoney.webservice.api.dto.plan.*;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class BusiOrderServiceImpl implements BusiOrderService {

	/**logger**/
	private static final Logger logger = LoggerFactory.getLogger(BusiOrderServiceImpl.class);

	@Autowired
	private CustomerMainInfoService mainInfoService;

	@Autowired
	private BusiOrderMapper busiOrderMapper;

	@Autowired
	private BusiOrderSubMapper busiOrderSubMapper;

	@Autowired
	private OrderService orderService;

	@Autowired
	private TradeService tradeService;

	@Autowired
	private CustomerMainInfoService customerMainInfoService;

	@Autowired
	private CustomerGrantInfoMapper customerGrantInfoMapper;

	@Autowired
	private BusiProductMapper busiProductMapper;


	@Autowired
	private ConfigParamBean configParamBean;

	@Autowired
	private IOriginSubjectFacadeService originSubjectFacadeService;

	@Autowired
	private SysParameterService parameterService;

	@Autowired
	private RedisSessionManager redisSessionManager;

	private final static String DAILY_MONEY_SPENT_KEY_PREFIX = "daily_money_spent";

	private final static long DAILY_MONEY_SPENT_KEY_REMAIN_TIME = 60*60*24;

	private final static String BUY_THE_REMAINING_TASK_LOCK = "buy_the_remaining_taks_lock";


	@Override
	public int insert(BusiOrder record) {
		return busiOrderMapper.insert(record);
	}

	@Override
	public BusiOrder selectByPrimaryKey(Long id) {
		return busiOrderMapper.selectByPrimaryKey(id);
	}

	@Override
	public BusiOrder selectByOrderNo(String orderNo) {
		return busiOrderMapper.selectByOrderNo(orderNo);
	}

	@Override
	public int updateByPrimaryKeySelective(BusiOrder record) {
		return busiOrderMapper.updateByPrimaryKeySelective(record);
	}

	public List<BusiOrder> selectAllOrder(Map<String,Object> map){
		return busiOrderMapper.selectAllOrder(map);
	}

	public List<BusiOrder> selectOrderByProperty(Map<String,Object> map){
		return busiOrderMapper.selectOrderByProperty(map);
	}

	public List<BusiOrderProductVo> selectOrderProductByProperty(Map<String,Object> map){
    	return busiOrderMapper.selectOrderProductByProperty(map);
    }

	public List<OrderInterest> selectOrderTotalInterest(Map<String,Object> map){
		return busiOrderMapper.selectOrderTotalInterest(map);
	}


	public BigDecimal selectOrderNotStartInterest(Map<String,Object> map){
		return busiOrderMapper.selectOrderNotStartInterest(map);
	}

	public DTO selectOrderEndInterest(Map<String,Object> map){
		return busiOrderMapper.selectOrderEndInterest(map);
	}

	/**
	 * 生产序列号
	 * 朱灿
	 */
	public  String buildCeNumber(String type, long custid) {
		String ceNo = "";
		try {
			ceNo = "C" + type + custid;// C代表消费订单，后面拼上消费类型加客户ID
			SimpleDateFormat timeStrFormat = new SimpleDateFormat(
					"yyyyMMddHHmmssSSS");
			ceNo += timeStrFormat.format(new Date());// 加上时间戳
			ceNo += (int) (Math.random() * (10)) + "";// 生成1位随机码拼 以免重复
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ceNo;
	}

	public String buildCeNumber(String busiType,String type, long custid){
		String ceNo = "";
		try {
			ceNo = busiType + type + custid;// C代表消费订单，后面拼上消费类型加客户ID
			SimpleDateFormat timeStrFormat = new SimpleDateFormat(
					"yyyyMMddHHmmssSSS");
			ceNo += timeStrFormat.format(new Date());// 加上时间戳
			ceNo += (int) (Math.random() * (10)) + "";// 生成1位随机码拼 以免重复
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ceNo;
	}

	/**
	 * 计算订单本利
	 * zhucan
	 */
	public BigDecimal calculationPrincipalinterest(BusiProduct busiProduct,BigDecimal orderAmt,BigDecimal addRate)
	{
		//本金+本金*年化*有效投资天数/365
		int day=DateUtil.getIntervalDays(busiProduct.getInterestEndDate(), busiProduct.getInterestStartDate())+1;
		BigDecimal principalin= orderAmt.multiply(busiProduct.getYearRate().add(addRate)).multiply(new BigDecimal(day)).divide(new BigDecimal(365),18,BigDecimal.ROUND_HALF_DOWN);
		String princi=CoreUtil.BigDecimalAccurate(principalin);
		principalin = new BigDecimal(princi);
		return principalin.add(orderAmt);
	}

	/**
	 * 计算订单收益
	 */
	public BigDecimal calculationInterest(BigDecimal principal, BigDecimal rate,int numOfDays,BigDecimal addRate)
	{
		//本金*年化*有效投资天数/365
		BigDecimal principalin= principal.multiply(rate.add(addRate)).multiply(new BigDecimal(numOfDays)).divide(new BigDecimal(365),18,BigDecimal.ROUND_HALF_DOWN);
		String princi=CoreUtil.BigDecimalAccurate(principalin);
		principalin = new BigDecimal(princi);
		return principalin;
	}


	public List<BusiOrder> selectOrderByCustomerIdAndStatus(Map<String,Object> map){
		return busiOrderMapper.selectOrderByCustomerIdAndStatus(map);
	}

	public int selectInviteCustFirstInvestCount(String inviteCode,Long customerId){
		Map<String,Object> map = Maps.newTreeMap();
		map.put("inviteCode",inviteCode);
		map.put("customerId",customerId);
		return busiOrderMapper.selectInviteCustFirstInvestCount(map);
	}

	/**
	 * 计算订单本利 (公式：本息=订单金额+订单金额/项目本金*项目利息)
	 */
	public BigDecimal computePrincipleAndInterest(BusiProductSub busiProduct,BigDecimal orderAmt,BigDecimal addRate)
	{
		BigDecimal interest = this.computeInterest(busiProduct, orderAmt, addRate);
		String strInterest=CoreUtil.BigDecimalAccurate(interest);
		return orderAmt.add(new BigDecimal(strInterest));
	}

	/**
	 * 计算每日收益
	 * @param busiProduct
	 * @param orderAmt
	 * @param addRate
	 * @return
	 */
	public BigDecimal computeDailyInterest(BusiProductSub busiProduct, BigDecimal orderAmt, BigDecimal addRate){
		BigDecimal interest = this.computeInterest(busiProduct, orderAmt, addRate);
		int day;
		if (AppConstants.OrderProductType.FINANCE_PLAN.toString().equals(busiProduct.getSubjectType())){
			day=busiProduct.getCloseDay();//理财计划取封闭期
		}else{
			day=DateUtil.getIntervalDays(busiProduct.getInterestEndDate(), busiProduct.getInterestStartDate())+1;
		}
		BigDecimal dailyInterest = interest.divide(new BigDecimal(day), 18, BigDecimal.ROUND_HALF_DOWN);
		String dailyInterestStr=CoreUtil.BigDecimalAccurate(dailyInterest);
		return  new BigDecimal(dailyInterestStr);
	}

	@Override
	public HistorySaleVo selectHistorySale() {
		return busiOrderMapper.selectHistorySale();
	}

	@Override
	public BigDecimal selectYestodaySale(String datetime) {
		return busiOrderMapper.selectYestodaySale(datetime);
	}

	@Override
	public List<BusiOrder> selectProductOrders(Map<String, Object> map) {
		return busiOrderMapper.selectProductOrders(map);
	}

	@Override
	public List<BusiOrder> selectOrderViewByProperty(Map<String, Object> map) {
		return busiOrderMapper.selectOrderViewByProperty(map);
	}

	@Override
	public  List<BusiOrder> selectOrderPayByDate(Map<String,Object> map) {
		return busiOrderMapper.selectOrderPayByDate(map);
	}

	@Override
	public List selectOrderPayTotalAmtByDate(Map<String, Object> map) {
		return busiOrderMapper.selectOrderPayTotalAmtByDate(map);
	}

	BigDecimal computeInterest(BusiProductSub busiProduct, BigDecimal orderAmt, BigDecimal addRate){
		int day ;
		BigDecimal interest = BigDecimal.ZERO;
		if (AppConstants.ProductSubjectType.FINANCE_PLAN.equals(busiProduct.getSubjectType())){
			day = busiProduct.getCloseDay();//理财计划取封闭期
            //理财计划订单利息计算：订单金额*年利率*封闭期/365
			interest = orderAmt.multiply(busiProduct.getYearRate()).multiply(new BigDecimal(day)).divide(new BigDecimal(365), 18, BigDecimal.ROUND_HALF_DOWN);
		}else{
			day = DateUtil.getIntervalDays(busiProduct.getInterestEndDate(), busiProduct.getInterestStartDate()) + 1;
			//计算加息的收益=项目本金*加息利率*封闭期/365
			BigDecimal totalInterest = busiProduct.getProductInterest().add(busiProduct.getProductPrincipal().multiply(addRate).multiply(new BigDecimal(day)).divide(new BigDecimal(365), 18, BigDecimal.ROUND_HALF_DOWN));
			//订单利息计算：订单金额/产品金额*产品总利息
			 interest = orderAmt.multiply(totalInterest).divide(busiProduct.getProductPrincipal(), 18, BigDecimal.ROUND_HALF_DOWN);
		}

		return interest;
	}

	public List<BusiOrder> selectOrderByCustomerIdAndDate(Map<String, Object> map){
		return busiOrderMapper.selectOrderByCustomerIdAndDate(map);
	}

	@Override
	public Long findUpdateCustomerCount(Long customerId) {
		return busiOrderMapper.findUpdateCustomerCount(customerId);
	}

	@Override
	public void updateCustomerCount(Map<String, Object> map) {
		busiOrderMapper.updateCustomerCount(map);
	}

	@Override
	public void insertCustomerCount(Map<String, Object> map) {
		busiOrderMapper.insertCustomerCount(map);
	}

	@Override
	public int updateMainSub(Map<String, Object> map) {
		return busiOrderMapper.updateMainSub(map);
	}

	@Override
	public List<BusiOrder> queryFinancePlanOrderInfo(Map<String, Object> paramsMap) {
		return busiOrderMapper.queryFinancePlanOrderInfo(paramsMap);
	}

	@Override
	public int updateOrderByIdAndStatus(Map<String, Object> paramsMap) {
		busiOrderSubMapper.updateOrderByIdAndStatus(paramsMap);
		return busiOrderMapper.updateOrderByIdAndStatus(paramsMap);
	}

	/**
	 * 查询用户订单关联产品是否提前结清
	 * @param paramsMap
	 * @return
	 */
	@Override
	public List<OrderProductExit> queryOrderProductExit(Map<String, Object> paramsMap) {
		return busiOrderMapper.queryOrderProductExit(paramsMap);
	}

	@Override
	@Transactional
	public int updateMainAndSubOrderByCondition(BusiOrder record) {
		BusiOrderSub sub = new BusiOrderSub();
		BeanUtils.copyProperties(record,sub);
		busiOrderSubMapper.updateByPrimaryKeySelective(sub);
		return busiOrderMapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public PageResultDto<DataOrderDTO> getTransferOrderData(Map<String, Object> paramsMap) {
		PageResultDto<DataOrderDTO> resultDto = new PageResultDto<>();
		List<DataOrderDTO> dataOrderList = new ArrayList<>();
		Page<BusiOrder> page = new Page<>();
		page.setBaseParam(paramsMap);
		paramsMap.put("page", page);
		try {
			List<BusiOrder> orderList = busiOrderMapper.getTransferOrderData(paramsMap);
			if (CollectionUtils.isNotEmpty(orderList)) {
				for (BusiOrder order : orderList) {
					CustomerMainInfo customerInfo = mainInfoService.findOne(order.getCustomerId());
					if (customerInfo == null) {
						continue;
					}
					DataOrderDTO dataOrderDTO = new DataOrderDTO();
					dataOrderDTO.setUserId(order.getCustomerId());
					dataOrderDTO.setOrderNo(order.getOrderId());
					dataOrderDTO.setUserNo(customerInfo.getCmNumber());
					dataOrderDTO.setOrderAmount(order.getOrderAmt());
					dataOrderDTO.setOrderTime(order.getOrderTime());
					dataOrderDTO.setProductType(order.getProductType());
					dataOrderDTO.setTenderDevice("WEB".equalsIgnoreCase(order.getCmOpenPlatform())? "WEB" : "MOBILE");
					dataOrderList.add(dataOrderDTO);
				}
			}
			resultDto.setPageNo(page.getPageNo());
			resultDto.setTotalPage(page.getTotalPage());
			resultDto.setTotalSize(page.getTotalRecord());
			resultDto.setDataList(dataOrderList);
		} catch (Exception e) {
			resultDto.setCode("1111");
			resultDto.setMsg("查询数据发生异常:" + e.getMessage());
		}
		return resultDto;
	}

	@Override
	public PageResultDto<DataRepayplanDTO> getRepayPlanList(Map<String, Object> paramsMap) {
		PageResultDto<DataRepayplanDTO> resultDto = new PageResultDto<>();
		List<DataRepayplanDTO> repayplanList = new ArrayList<>();
		Page<BusiOrder> page = new Page<>();
		page.setBaseParam(paramsMap);
		paramsMap.put("page", page);
		try {
			List<BusiOrder> orderList = busiOrderMapper.getActivateOrder(paramsMap);
			if (CollectionUtils.isEmpty(orderList)) {
				resultDto.setCode("1111");
				resultDto.setMsg("没有起息订单");
			}
			for (BusiOrder order : orderList) {
				if (order == null) continue;
				List<BusiOrderSubVo> orderSubList = busiOrderSubMapper.selectRepayplanDetail(order.getOrderId());
				if (CollectionUtils.isEmpty(orderSubList)) continue;
				List<RepayPlan> planList = new ArrayList<>();
				if (CollectionUtils.isNotEmpty(orderSubList)) {
					for (BusiOrderSubVo sub : orderSubList) {
						RepayPlan plan = new RepayPlan();
						if (sub.getProductType() != null && 4 == sub.getProductType()) {
							plan.setRepayTime(order.getInterestEndDate().getTime());
							plan.setRepayAmount(order.getOrderAmt());
							plan.setPeriod(Integer.valueOf(1));
						} else {
							plan.setRepayTime(sub.getRepayDay() != null ? sub.getRepayDay().getTime() : null);
							plan.setRepayAmount(sub.getOrderAmt());
							plan.setPeriod(sub.getCurrTerm());
						}
						if (plan.getRepayAmount() != null)
						planList.add(plan);
					}
				}
				if (CollectionUtils.isEmpty(planList)) continue;
                DataRepayplanDTO repayplanDTO = new DataRepayplanDTO();
                repayplanDTO.setUserId(order.getCustomerId());
                repayplanDTO.setOrderNo(order.getOrderId());
                repayplanDTO.setRepayPlanList(planList);
                repayplanList.add(repayplanDTO);
			}
			resultDto.setPageNo(page.getPageNo());
			resultDto.setTotalPage(page.getTotalPage());
			resultDto.setTotalSize(page.getTotalRecord());
			resultDto.setDataList(repayplanList);
		} catch (Exception e) {
			e.printStackTrace();
			resultDto.setCode("1111");
			resultDto.setMsg("查询数据发生异常:" + e.getMessage());
		}
		return resultDto;
	}

	@Override
	public PageResultDto<DataRepayOrderDTO> getRepayCompleteOrderList(Map<String, Object> paramsMap) {
		PageResultDto<DataRepayOrderDTO> resultDto = new PageResultDto<>();
		List<DataRepayOrderDTO> repayCompleteList = new ArrayList<>();
		Page<BusiOrder> page = new Page<>();
		page.setBaseParam(paramsMap);
		paramsMap.put("page", page);
		try {
			List<BusiOrderSubVo> orderList = busiOrderMapper.getRepayCompleteOrderList(paramsMap);
			if (CollectionUtils.isNotEmpty(orderList)) {
				for (BusiOrderSubVo order : orderList) {
					DataRepayOrderDTO repayOrder = new DataRepayOrderDTO();
					repayOrder.setUserId(order.getCustomerId());
					repayOrder.setUserNo(order.getCmNumber());
					repayOrder.setOrderNo(order.getOrderId());
					repayOrder.setCompleteTime(order.getRepayDay().getTime());
					repayOrder.setOrderAmt(order.getOrderAmt());
					repayOrder.setCurrTerm(order.getCurrTerm());
					repayCompleteList.add(repayOrder);
				}
			}
			resultDto.setPageNo(page.getPageNo());
			resultDto.setTotalPage(page.getTotalPage());
			resultDto.setTotalSize(page.getTotalRecord());
			resultDto.setDataList(repayCompleteList);
		} catch (Exception e) {
			resultDto.setCode("1111");
			resultDto.setMsg("查询数据发生异常:" + e.getMessage());
		}
		return resultDto;
	}

	@Override
	public PageResultDto<DataOrderInfoDTO> getInvestOverList(Map<String, Object> paramsMap) {
		PageResultDto<DataOrderInfoDTO> resultDto = new PageResultDto<>();
		List<DataOrderInfoDTO> overOrderList = new ArrayList<>();
		Page<BusiOrder> page = new Page<>();
		page.setBaseParam(paramsMap);
		paramsMap.put("page", page);
		try {
			List<BusiOrder> orderList = busiOrderMapper.getInvestOverList(paramsMap);
			if (CollectionUtils.isNotEmpty(orderList)) {
				for (BusiOrder order : orderList) {
					DataOrderInfoDTO orderInfo = new DataOrderInfoDTO();
					orderInfo.setCustomerId(order.getCustomerId());
					orderInfo.setOrderNo(order.getOrderId());
					if (4 == order.getProductType()) {
						orderInfo.setCompleteDate(order.getExitActualDate()!=null ? order.getExitActualDate().getTime() : null);
					} else {
						orderInfo.setCompleteDate(order.getModifyDate()!=null?order.getModifyDate().getTime():null);
					}
					overOrderList.add(orderInfo);
				}
			}
			resultDto.setPageNo(page.getPageNo());
			resultDto.setTotalPage(page.getTotalPage());
			resultDto.setTotalSize(page.getTotalRecord());
			resultDto.setDataList(overOrderList);
		} catch (Exception e) {
			resultDto.setCode("1111");
			resultDto.setMsg("查询数据发生异常:" + e.getMessage());
		}
		return resultDto;
	}

    @Override
    public BusiOrderSumDto getReinvestOrderTotal(Map<String, Object> paramsMap) {
		return busiOrderMapper.selectReinvestOrderTotal(paramsMap);
    }

    @Override
    public BusiOrderSumDto getReinvestOrderSumTotal(Map<String, Object> paramsMap) {
        return busiOrderMapper.selectReinvestOrderSumTotal(paramsMap);
    }

    @Override
	public PageResultDto<BusiOrderDto> getReinvestOrderList(Map<String, Object> paramsMap) {
		PageResultDto<BusiOrderDto> resultDto = new PageResultDto<>();
		List<BusiOrderDto> reinvestOrderList ;
		Page<BusiOrderDto> page = new Page<>();
		page.setBaseParam(paramsMap);
		paramsMap.put("page", page);
		try {
			reinvestOrderList = busiOrderMapper.selectReinvestOrderList(paramsMap);
			resultDto.setPageNo(page.getPageNo());
			resultDto.setTotalPage(page.getTotalPage());
			resultDto.setTotalSize(page.getTotalRecord());
			resultDto.setDataList(reinvestOrderList);
		} catch (Exception e) {
			resultDto.setCode("1111");
			resultDto.setMsg("查询数据发生异常:" + e.getMessage());
		}
		return resultDto;
	}

    @Override
	public PageResultDto<BusiOrderSumDto> getReinvestOrderSum(Map<String, Object> paramsMap) {
		PageResultDto<BusiOrderSumDto> resultDto = new PageResultDto<>();
		List<BusiOrderSumDto> reinvestOrderSum = new ArrayList<>();
		Page<BusiOrderSumDto> page = new Page<>();
		page.setBaseParam(paramsMap);
		paramsMap.put("page", page);
		try {
			reinvestOrderSum = busiOrderMapper.selectReinvestOrderSum(paramsMap);
			resultDto.setPageNo(page.getPageNo());
			resultDto.setTotalPage(page.getTotalPage());
			resultDto.setTotalSize(page.getTotalRecord());
			resultDto.setDataList(reinvestOrderSum);
		} catch (Exception e) {
			resultDto.setCode("1111");
			resultDto.setMsg("查询数据发生异常:" + e.getMessage());
		}
		return resultDto;
	}

	@Transactional
	public int updateMainAndSubOrderWithOrderNo(BusiOrder record) {
		BusiOrderSub sub = new BusiOrderSub();
		BeanUtils.copyProperties(record,sub);
		busiOrderSubMapper.updateByPrimaryKeySelectiveByOrderNum(sub);
		return busiOrderMapper.updateByPrimaryKeySelectiveByOrderNum(record);
	}

	/**
 *
 * @param judgeBorrowFlag
 * @param judgePayFlag
 * @param msgType 0:下单  1：转让
 * @return
 */
	private Map getGrantMap(int judgeBorrowFlag, int judgePayFlag ,int msgType) {
		Map alertInfo = Maps.newHashMap();
		if (judgeBorrowFlag == 1 || judgePayFlag == 1) {
			alertInfo.put("title", configParamBean.getAuthTitle());
			if(msgType==0){
				alertInfo.put("invokeMsg", configParamBean.getAuthOrderMsg());
			}
			if(msgType==1){
				alertInfo.put("invokeMsg", configParamBean.getAuthTransferMsg());
			}
		}
		return alertInfo;
	}


	public Map  userAuthJudge(Long customerId,Long productId,BigDecimal orderAmt) {
		Map userJudgeInfo = Maps.newHashMap();
		int judgeBorrowFlag=0,judgePayFlag=0;

		UserGrantBO userGrant = customerGrantInfoMapper.queryUserGrant(customerId);
		if (userGrant ==null){
			throw new BusinessException("查询不到用户授权信息！");
		}
		BusiProduct busiProduct = busiProductMapper.selectByPrimaryKey(productId);
		if (busiProduct == null) {
			throw new BusinessException(AppConstants.LoginBusiOrder.BUSIORDER_ERROR_1);
		}
		Date autoLendTerm ; //授权期限
		Date autoFeeTerm ;//缴费期限
		BigDecimal amtRate = new BigDecimal(100);//分为单位
		BigDecimal authRate = new BigDecimal(configParamBean.getAuthRate()); //授权乘以比例
		BigDecimal authFee =  orderAmt.multiply(authRate).multiply(amtRate);

		UserAuthDto  userAuthDto = getOrderUserAuth(customerId, productId, orderAmt);
		log.info("获取用户下单时所需授权信息："+ JSONUtils.toJSON(userAuthDto));
		if (userAuthDto != null) {
			if (userAuthDto.getAuthTotalAmt().multiply(amtRate).compareTo(new BigDecimal(userGrant.getAutoLendAmt()))>=0 ){
				judgeBorrowFlag=1;
				//	throw new BusinessException("授权出借余额不足/借期限不足");
				log.info("授权不足，当前所需授权额度{},大于授权额度{}",userAuthDto.getAuthTotalAmt().multiply(amtRate),userGrant.getAutoLendAmt());
			}
			if (authFee.compareTo(new BigDecimal(userGrant.getAutoFeeAmt()))>=0){
				judgePayFlag=1;
				log.info("授权不足，当前缴费所需额度{},大于缴费额度{}",authFee,userGrant.getAutoFeeAmt());
			}

			if(StringUtils.isEmpty(userGrant.getAutoLendTerm())){
				judgeBorrowFlag=1;
				log.info("授权不足，当前授权出借期限未设置。");
			}else{
				autoLendTerm = DateUtil.strFormatToDate(userGrant.getAutoLendTerm(),DateUtil.YYYYMMDD);
				if (DateUtil.compareStringDate(autoLendTerm,userAuthDto.getAuthPeriod())<=0){
					judgeBorrowFlag=1;
					//	throw new BusinessException("授权出借余额不足/借期限不足");
					log.info("授权不足，已授权期限{},小于当前授权期限{}",DateUtil.dateToString(autoLendTerm),DateUtil.dateToString(userAuthDto.getAuthPeriod()));
				}
			}
			if(StringUtils.isEmpty(userGrant.getAutoFeeTerm())){
				judgePayFlag=1;
				log.info("授权额度不足，当前授权缴费期限未设置。");
			}else{
				autoFeeTerm = DateUtil.strFormatToDate(userGrant.getAutoFeeTerm(),DateUtil.YYYYMMDD);//缴费期限
				if (DateUtil.compareStringDate(autoFeeTerm,userAuthDto.getPayPeriod())<=0){
					judgePayFlag=1;
					log.info("授权不足，已授权缴费期限{},小于当前授权缴费期限{}",DateUtil.dateToString(autoFeeTerm),DateUtil.dateToString(userAuthDto.getPayPeriod()));
				}
			}

		}

		int grantFlag=0;//0： 授权充足 1： 授权不足
		if (judgeBorrowFlag == 1 || judgePayFlag == 1) {
			grantFlag=1;
		}
		Map gantInfos = getGrantMap(judgeBorrowFlag, judgePayFlag,0);
		userJudgeInfo.put("grantFlag",grantFlag);
		userJudgeInfo.put("grantInfos",gantInfos);

		return userJudgeInfo;
	}


	/**
	 *
	 * @param customerId
	 * @return
	 */
	private  List<UserAssetVo> getUserAssets(Long customerId){
		Map<String, Object> map = Maps.newTreeMap();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		String status = AppConstants.BusiOrderStatus.BUSIORDER_STATUS_0 + "," + AppConstants.BusiOrderStatus.BUSIORDER_STATUS_9 + "," + AppConstants.BusiOrderStatus.BUSIORDER_STATUS_14+
				","+AppConstants.BusiOrderStatus.BUSIORDER_STATUS_17+","+AppConstants.BusiOrderStatus.BUSIORDER_STATUS_18;
		map.put("customerId", customerId);
		map.put("sDate", sdf.format(new Date()));
		map.put("status", status.split(","));
		map.put("sort", "desc");
		map.put("orderType","1");
		List<UserAssetVo> vos=busiOrderMapper.selelctAssetByCustomerIdAndStatus(map);
		return vos;
	}


	/**
	 * 获取用户在投散标、智投宝信息
	 * @param customerId
	 * @return
	 */
	public  UserAuthOrderDto commonUserAuthOrder(Long customerId){
		Map<String, Object> map = Maps.newTreeMap();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		String status = AppConstants.BusiOrderStatus.BUSIORDER_STATUS_0 + "," + AppConstants.BusiOrderStatus.BUSIORDER_STATUS_9 + "," + AppConstants.BusiOrderStatus.BUSIORDER_STATUS_14+
				","+AppConstants.BusiOrderStatus.BUSIORDER_STATUS_17+","+AppConstants.BusiOrderStatus.BUSIORDER_STATUS_18;
		map.put("customerId", customerId);
		map.put("sDate", sdf.format(new Date()));
		map.put("status", status.split(","));
		map.put("sort", "desc");
		List<UserAssetVo> vos=busiOrderMapper.selelctAssetByCustomerIdAndStatus(map);

		BigDecimal totalPlanAmt = new BigDecimal(0);//智投宝订单金额
		BigDecimal totalLoanAmt = new BigDecimal(0);//散标订单总金额
		List<UserAssetVo> planList = new ArrayList<>();
		Date authDate = null;
		Date interestEndDate=null;
		BigDecimal authScale = new BigDecimal(configParamBean.getAuthScale()); //授权乘以系数
		int authTerm = Integer.parseInt(configParamBean.getAuthTerm());//授权增加天数
		BigDecimal authRate = new BigDecimal(configParamBean.getAuthRate()); //授权乘以比例

		if (!CollectionUtils.isEmpty(vos)){
			for (UserAssetVo vo : vos) {
				//智投宝
				if ("4".equals(vo.getSubjectType())){
					totalPlanAmt= totalPlanAmt.add(vo.getOrderAmt());
					planList.add(vo);
				}
				//散标
				if ("3".equals(vo.getSubjectType())){
					totalLoanAmt= totalLoanAmt.add(vo.getOrderAmt());
				}
			}

		}
		if (!CollectionUtils.isEmpty(planList)){
			 interestEndDate = planList.get(0).getInterestEndDate();
		}

		if (interestEndDate==null){
			authDate = DateUtils.plusDays(new Date(),authTerm);
		}else {
			authDate = DateUtils.plusDays(interestEndDate,authTerm);

		}

		UserAuthOrderDto authOrderDto = new UserAuthOrderDto();
		authOrderDto.setTotalPlanAmt(totalPlanAmt.setScale(2, RoundingMode.DOWN));
		authOrderDto.setTotalLoanAmt(totalLoanAmt.setScale(2, RoundingMode.DOWN));
		authOrderDto.setInterestEndDate(interestEndDate);
		authOrderDto.setTotalAmt(totalPlanAmt.multiply(authScale).add(totalLoanAmt).setScale(2, RoundingMode.DOWN));
		authOrderDto.setAuthDate(authDate);
		authOrderDto.setAuthFee(totalPlanAmt.multiply(authRate).add(totalLoanAmt.multiply(authRate)).setScale(2, RoundingMode.DOWN));

		return authOrderDto;
	}

	/**
	 * 获取标的借款用户待还信息
	 * @param customerId
	 * 授权额度 ： 待还总额度*2+新签约合同金额*2
	 * 授权期限 ： max（借款最大时长or新签约时长）+90
	 * 缴费额度：待还总额度*50%+新签约合同金额*50%
	 * @return
	 */
	public  UserAuthOrderDto borrowerAuthInfo(Long customerId,Date signDate,BigDecimal borrowAmt){
		Date borrowDate= null;//获取借款人最长签约时间
		BigDecimal totalBorrowAmt = new BigDecimal(0);//未还总金额
		BigDecimal totalBorrowFee = new BigDecimal(0);//缴费额度
		Date authDate ;
		BigDecimal authScale = new BigDecimal(configParamBean.getAuthScale()); //授权乘以系数
		int authTerm = Integer.parseInt(configParamBean.getAuthTerm());//授权增加天数
		BigDecimal authBorrowScale = new BigDecimal(configParamBean.getAuthBorrowScale()); //借款授权乘以比例

		CustomerMainInfo customerMainInfo = customerMainInfoService.validateCustomerInfo(customerId);
		if (customerMainInfo == null) {
			throw new BusinessException("查询不到用户信息！");
		}
		RemainRepayDto repayDto=null;
		try {
			AssetsResultDto<RemainRepayDto> resultDto = originSubjectFacadeService.getRemainReplyByIdNo(customerMainInfo.getCmIdnum());
			if(resultDto.isSuccess() && resultDto.getData()!=null)  {
				repayDto = resultDto.getData();
				borrowDate = repayDto.getRepayDay();
				totalBorrowAmt = repayDto.getTotalAmount();
			}

		}catch (Exception e){
			log.error("调用标的系统查询标的借款人待还信息失败：【身份证号:{}】",customerMainInfo.getCmIdnum(),e);
		}

		BigDecimal totalAmt = totalBorrowAmt.multiply(authScale).add(borrowAmt.multiply(authScale));
		totalBorrowFee = totalBorrowAmt.multiply(authBorrowScale).add(borrowAmt.multiply(authBorrowScale));

		if (borrowDate == null) {
			authDate  = DateUtils.plusDays(signDate,authTerm);
		}else{
			//借款最大时长与新签约时长比较、取最大时间
			if (DateUtil.compareStringDate(signDate,borrowDate)<0){
				authDate  = DateUtils.plusDays(borrowDate,authTerm);
			}else{
				authDate = DateUtils.plusDays(signDate,authTerm);
			}
		}

		UserAuthOrderDto authOrderDto = new UserAuthOrderDto();
		authOrderDto.setTotalAmt(totalAmt.setScale(2, RoundingMode.DOWN));
		authOrderDto.setAuthDate(authDate);
		authOrderDto.setAuthFee(totalBorrowFee.setScale(2, RoundingMode.DOWN));

		return authOrderDto;
	}

	@Override
	public BigDecimal statisticsOrderAmt(Long customerId) {
		Map<String, Object> map = Maps.newTreeMap();
		BigDecimal totalAmt = new BigDecimal(0);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		String status = AppConstants.BusiOrderStatus.BUSIORDER_STATUS_0 + "," + AppConstants.BusiOrderStatus.BUSIORDER_STATUS_9 + "," + AppConstants.BusiOrderStatus.BUSIORDER_STATUS_14+
				","+AppConstants.BusiOrderStatus.BUSIORDER_STATUS_17+","+AppConstants.BusiOrderStatus.BUSIORDER_STATUS_18;
		map.put("customerId", customerId);
		map.put("sDate", sdf.format(new Date()));
		map.put("status", status.split(","));
		map.put("sort", "desc");
		List<UserAssetVo> vos = busiOrderMapper.selelctAssetByCustomerIdAndStatus(map);
		if (!CollectionUtils.isEmpty(vos)){
			for (UserAssetVo vo : vos) {
				totalAmt= totalAmt.add(vo.getOrderAmt());
			}
		}
		return totalAmt;
	}

	/**
	 * 我的网贷,转让页面授权是否充足
	 * @param customerId
	 * @param authFlag 0:我的网贷授权 1: //转让授权
	 * @return
	 */
	public Map userGrantFlag(Long customerId,int authFlag,BigDecimal transferFee) {
		Map gantMap = Maps.newHashMap();
		int grantFlag=0;//0： 授权充足 1： 授权不足 2:未授权
		int borrowFlag=0,payFlag=0;
		Map grantInfoMap = null;

		UserGrantBO userGrant = customerGrantInfoMapper.queryUserGrant(customerId);
		if (userGrant == null) {
			grantFlag= 2;
			grantInfoMap = getGrantMap(borrowFlag,payFlag,1);
		}else{
			BigDecimal amtRank= new BigDecimal(100);//分为单位
			UserAuthOrderDto authDto = commonUserAuthOrder(customerId);
			log.info("获取用户所需授权额度信息："+ JSONUtils.toJSON(authDto));
			if (authFlag==0){//我的网贷授权
//				if(new BigDecimal(userGrant.getAutoLendAmt()).compareTo(authDto.getTotalAmt().multiply(amtRank))<0){
//					borrowFlag=1;
//				}
//				if (authDto.getAuthFee().multiply(amtRank).compareTo(new BigDecimal(userGrant.getAutoFeeAmt()))>0){
//					payFlag=1;
//				}
//				grantInfoMap = getGrantMap(borrowFlag,payFlag,0);
			}
			if (authFlag==1){//转让授权
				BigDecimal payAmt = transferFee.multiply(amtRank);//缴费额度
				if(new BigDecimal(userGrant.getAutoLendAmt()).compareTo(authDto.getTotalAmt().multiply(amtRank))<=0){
					borrowFlag=1;
					log.info("转让授权不足，当前转让所需出借额度：{}，当前授权额度:{}",authDto.getTotalAmt().multiply(amtRank),userGrant.getAutoLendAmt());
				}
				if (payAmt.compareTo(new BigDecimal(userGrant.getAutoFeeAmt()))>=0){
					payFlag=1;
					log.info("转让授权不足，当前转让所需缴费额度：{}，当前缴费额度:{}",payAmt,userGrant.getAutoFeeAmt());
				}

				if (StringUtils.isEmpty(userGrant.getAutoLendTerm())){
					borrowFlag=1;
					log.info("转让授权不足，当前授权出借期限未设置。");
				}else{
					Date autoLendTerm =DateUtil.strFormatToDate(userGrant.getAutoLendTerm(),DateUtil.YYYYMMDD);
					if (DateUtil.compareStringDate(autoLendTerm,authDto.getAuthDate())<=0){
						borrowFlag=1;
						log.info("转让授权不足，已授权出借期限：{}，当前出借期限:{}",DateUtil.dateToString(autoLendTerm),DateUtil.dateToString(authDto.getAuthDate()));
					}
				}

				if (StringUtils.isEmpty(userGrant.getAutoFeeTerm())){
					payFlag=1;
					log.info("转让授权不足，当前授权缴费期限未设置。");
				}else{
					Date autoFeeTerm = DateUtil.strFormatToDate(userGrant.getAutoFeeTerm(),DateUtil.YYYYMMDD);
					if (DateUtil.compareStringDate(autoFeeTerm,authDto.getAuthDate())<=0){
						payFlag=1;
						log.info("转让授权不足，已授权缴费期限：{}，当前缴费期限:{}",DateUtil.dateToString(autoFeeTerm),DateUtil.dateToString(authDto.getAuthDate()));
					}
				}

				grantInfoMap = getGrantMap(borrowFlag,payFlag,1);
			}

		}

		if (borrowFlag==1 || payFlag ==1){
			grantFlag=1;
		}

		gantMap.put("authInfos",grantInfoMap);
		gantMap.put("grantFlag",grantFlag);

		return gantMap;
	}


	/**
	 * 获取下单用户授权信息
	 * @param customerId
	 * @return
	 */
	public UserAuthDto getOrderUserAuth(Long customerId,Long productId,BigDecimal orderAmt){
		UserGrantBO userGrant = customerGrantInfoMapper.queryUserGrant(customerId);
		if (userGrant ==null){
			throw new BusinessException("查询不到用户授权信息！");
		}
		BusiProduct busiProduct = busiProductMapper.selectByPrimaryKey(productId);
		if (busiProduct == null) {
			throw new BusinessException(AppConstants.LoginBusiOrder.BUSIORDER_ERROR_1);
		}
		BigDecimal authTotalAmt= new BigDecimal(0);//授权出借额度
		Date authPeriod = null;//授权出借期限
		BigDecimal payAmt = new BigDecimal(0);//授权缴费额度
		Date payPeriod = null;//授权缴费期限

		BigDecimal authScale = new BigDecimal(configParamBean.getAuthScale()); //授权乘以系数
		int authTerm = Integer.parseInt(configParamBean.getAuthTerm());//授权增加天数
		BigDecimal authRate = new BigDecimal(configParamBean.getAuthRate()); //授权乘以比例

		//用户订单信息（智投宝、散标）
		UserAuthOrderDto orderDto = commonUserAuthOrder(customerId);
		Date interestEndDate = orderDto.getInterestEndDate();
		//散标
		if("3".equals(busiProduct.getSubjectType())){
			if (interestEndDate == null) {
				authPeriod = DateUtils.plusDays(new Date(),authTerm);
				payPeriod=authPeriod;
			}else{
				authPeriod = DateUtils.plusDays(interestEndDate,authTerm);
				payPeriod=authPeriod;
			}
			authTotalAmt = orderDto.getTotalAmt().add(orderAmt);
		}
		//智投宝
		Date productEndDate = busiProduct.getInterestEndDate();
		if (productEndDate == null) {//结息日为空时，当前期限 = newDate+封闭期+10+期限
			productEndDate= DateUtils.plusDays(new Date(),busiProduct.getCloseDay());
			productEndDate=DateUtils.plusDays(productEndDate,10);
		}
		if("4".equals(busiProduct.getSubjectType())){
			authTotalAmt = orderDto.getTotalAmt().add(orderAmt.multiply(authScale));
			//授权出借期限 （封闭期最远or当前订单最远日期） +90
			if (interestEndDate == null) {
				authPeriod= DateUtils.plusDays(productEndDate,authTerm);
				payPeriod =authPeriod;
			}else{
				//当前期限大于订单期限,取当前订单期限
				if (DateUtil.compareStringDate(productEndDate, interestEndDate) > 0) {
					authPeriod = DateUtils.plusDays(productEndDate, authTerm);
					payPeriod=authPeriod;
				}else{
					authPeriod =DateUtils.plusDays(interestEndDate,authTerm);
					payPeriod=authPeriod;
				}
			}

		}
		payAmt = orderDto.getAuthFee().add(orderAmt.multiply(authRate));

		UserAuthDto authDto = new UserAuthDto();
		authDto.setTotalPayPlanAmt(orderDto.getTotalPlanAmt().setScale(2, RoundingMode.DOWN));
		authDto.setAuthTotalAmt(authTotalAmt.setScale(2, RoundingMode.DOWN));
		authDto.setAuthPeriod(authPeriod);
		authDto.setPayPeriod(payPeriod);
		authDto.setPayAmt(payAmt.setScale(2, RoundingMode.DOWN));

		return authDto;
	}

	@Override
	public void buyRemainingPartOfWacaiProduct() {
		String value = "1";
		boolean locked = false;
		try {
			locked = redisSessionManager.tryGetDistributedLock(BUY_THE_REMAINING_TASK_LOCK, value, 1000*30, 5);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		if(locked) {
			try {
				CustomerMainInfo buyer = customerMainInfoService.findOneByCmNumber(configParamBean.getWacaiProductRemainingPartBuyer());
				BigDecimal thresholdOnSingleProduct = getThresholdOnSingleProduct();
				BigDecimal daliyThreshold = getDaliyThreshold();
				List<BusiProduct> products = findEligibleProducts();
				for (BusiProduct product : products) {
					ResultDto<String> resultDto = buyRemainingPart(product, buyer, thresholdOnSingleProduct, daliyThreshold);
					handleOrderResult(resultDto, product, buyer);
				}
			}catch (Exception e){
				logger.error(e.getMessage(), e);
				MailUtil.sendMail("挖财兜底异常", e.toString());
			}finally {
				redisSessionManager.releaseDistributedLock(BUY_THE_REMAINING_TASK_LOCK, value);
			}
		}else{
			logger.warn("存在并发兜底");
		}
	}

	private BigDecimal getThresholdOnSingleProduct(){
		SysParameter parameter = parameterService.findOneByPrTypeWithoutCache(Constants.THRESHOLD_ON_SINGLE_PRODUCT_KEY);
		BigDecimal moneyApproval = BigDecimal.valueOf(Double.valueOf(parameter.getPrValue()));
		moneyApproval = moneyApproval.multiply(BigDecimal.valueOf(10000));
		return moneyApproval;
	}

	private BigDecimal getDaliyThreshold(){
		SysParameter parameter = parameterService.findOneByPrTypeWithoutCache(Constants.DAILY_MONEY_SPENT_THRESHOLD_KEY);
		BigDecimal moneyApproval = BigDecimal.valueOf(Double.valueOf(parameter.getPrValue()));
		moneyApproval = moneyApproval.multiply(BigDecimal.valueOf(10000));
		return moneyApproval;
	}

	private List<BusiProduct> findEligibleProducts(){
		Map<String, Object> conditions = new HashMap<>();
		Date now = new Date();
		now.setTime(now.getTime() - getDuration());
		conditions.put("startSellingTimeBefore", now);
		conditions.put("notSoldOut", Boolean.TRUE);
		conditions.put("creditSource", AppConstants.CreditSource.WACAI.name());
		return busiProductMapper.queryProductInfo(conditions);
	}

	private long getDuration(){
		SysParameter parameter = parameterService.findOneByPrTypeWithoutCache(Constants.START_BUYING_REMAINING_AFTER);
		int minutes = Integer.parseInt(parameter.getPrValue());
		return 1000 * 60 * minutes;
	}

	private ResultDto<String> buyRemainingPart(BusiProduct product, CustomerMainInfo buyer, BigDecimal thresholdOnSingleProduct, BigDecimal daliyThreshold){
		ResultDto<String> resultDto = null;
		BigDecimal remainingAmt = product.getProductPrincipal().subtract(product.getTotalInvestAmt());
		try {
			if (checkThresholdOnSingleProduct(remainingAmt, thresholdOnSingleProduct)) {
				BigDecimal moneyAlreadySpent = getMoneyAlreadySpent();
				logger.info("单日已兜底"+moneyAlreadySpent);
				if(checkDaliyThreshold(remainingAmt, moneyAlreadySpent, daliyThreshold)) {
					resultDto = doBuy(product, buyer, remainingAmt);
				}else {
					resultDto = new ResultDto("兜底已达单日上限", false);
				}
			} else {
				resultDto = new ResultDto("兜底金额超出单个产品允许上限",false);
			}
		}catch (Exception e){
			String msg = String.format("用户 %s 兜底产品 %d 失败:%s,预计兜底金额：%f",buyer.getCmNumber(),product.getId(),e.getMessage(),remainingAmt);
			logger.error(msg, e);
			resultDto = new ResultDto(msg,false);
		}
		return resultDto;
	}

	private boolean checkThresholdOnSingleProduct(BigDecimal orderAmt, BigDecimal threshold){
		//String threshold = redisSessionManager.get(Constants.THRESHOLD_ON_SINGLE_PRODUCT_KEY);
		if(threshold.compareTo(orderAmt) >= 0)
			return true;
		return false;
	}

	private BigDecimal getMoneyAlreadySpent(){
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String alreadySpent = redisSessionManager.get(DAILY_MONEY_SPENT_KEY_PREFIX + sdf.format(now));
		BigDecimal moneyAlreadySpent = BigDecimal.ZERO;
		if(alreadySpent != null){
			moneyAlreadySpent = BigDecimal.valueOf(Double.valueOf(alreadySpent));
		}
		return moneyAlreadySpent;
	}

	private boolean checkDaliyThreshold(BigDecimal orderAmt, BigDecimal moneyAlreadySpent, BigDecimal threshold){
		//String threshold = redisSessionManager.get(Constants.DAILY_MONEY_SPENT_THRESHOLD_KEY);
		if(moneyAlreadySpent.add(orderAmt).compareTo(threshold) <= 0){
			return true;
		}
		return false;
	}

	private ResultDto doBuy(BusiProduct product, CustomerMainInfo buyer, BigDecimal orderAmt) throws Exception {
		BusiOrderSub orderSub = order(product, buyer, orderAmt);
		return pay(orderSub);
	}

	private BusiOrderSub order(BusiProduct product, CustomerMainInfo buyer, BigDecimal orderAmt) throws Exception{
		OrderVo orderVo = new OrderVo();
		orderVo.setOrderAmt(orderAmt);
		orderVo.setProductId(product.getId());
		orderVo.setCustomerId(buyer.getId());
		orderVo.setHoldType(OrderConstants.OrderHoldType.HOLD_SPECIAL);
		return orderService.order(orderVo);
	}

	private ResultDto pay(BusiOrderSub orderSub) throws Exception {
		Result result = tradeService.pay(orderSub.getCustomerId(), orderSub.getId(), null, null, null, null, Boolean.FALSE, null);
		ResultDto resultDto = null;
		if(result == null || !result.getSuccess()) {
			resultDto = new ResultDto(result == null ? "支付失败" : result.getMessage(), false);
		}else{//调用成功，是否支付成功需要看data中的payResult
			Pay20DTO data = (Pay20DTO) result.getData();
			if(AppConstants.PayStatusContants.SUCCESS.equals(data.getPayResult()))//data中的payResult为“0”表示支付成功，其他情况下支付失败
				resultDto = new ResultDto();
			else
				resultDto = new ResultDto(data.getPayResultDesc(), false);
		}
		return resultDto;
	}

	private void handleOrderResult(ResultDto<String> orderResult, BusiProduct product, CustomerMainInfo buyer){
		BigDecimal orderAmt = product.getProductPrincipal().subtract(product.getTotalInvestAmt());
		if(!orderResult.isSuccess()) {
			String msg = String.format("用户[%s]兜底购买产品%d[%s]失败，预期兜底金额：%f,失败原因：%s",
					buyer.getCmNumber(), product.getId(), product.getProductName(), orderAmt, orderResult.getMsg());
			MailUtil.sendMail("挖财产品兜底失败", msg);
		}else{
			BigDecimal moneyAlreadySpent = getMoneyAlreadySpent();
			moneyAlreadySpent = moneyAlreadySpent.add(orderAmt);
			updateMoneyDailySpent(moneyAlreadySpent);
		}
	}

	private void updateMoneyDailySpent(BigDecimal moneyAlreadySpent){
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String key = DAILY_MONEY_SPENT_KEY_PREFIX + sdf.format(now);
		redisSessionManager.put(key, String.valueOf(moneyAlreadySpent.doubleValue()),1,TimeUnit.DAYS);
	}

	@Override
	@Transactional
	public int saveSubOrders(List<BusiOrderSub> orders) {
		return busiOrderSubMapper.insertOrders(orders);
	}

	@Override
	public PageResultDto<BusiOrderDto> queryMatchedOrders(QueryOrderReqDto reqDto) {
		reqDto.setStatusList(Arrays.asList(AppConstants.BusiOrderStatus.BUSIORDER_STATUS_0,AppConstants.BusiOrderStatus.BUSIORDER_STATUS_17,
				AppConstants.BusiOrderStatus.BUSIORDER_STATUS_18,AppConstants.BusiOrderStatus.BUSIORDER_STATUS_19));
		return this.queryMainOrderInfo(reqDto);
	}

	public PageResultDto<BusiOrderDto> queryMainOrderInfo(QueryOrderReqDto reqDto) {
		PageResultDto<BusiOrderDto> pageResultDto = new PageResultDto<>();
		Map<String,Object> conditions = CommonHelper.transBean2Map(reqDto);
		Page<BusiOrderDto> page = new Page<>();
		page.setPageNo(reqDto.getPageNo());
		page.setPageSize(reqDto.getPageSize());
		conditions.put("page", page);
		try {
			List<BusiOrderDto> orders = busiOrderMapper.queryMatchedOrders(conditions);
			pageResultDto.setDataList(orders);
			pageResultDto.setTotalPage(page.getTotalPage());
			pageResultDto.setTotalSize(page.getTotalRecord());
		}catch (Exception e){
			logger.error(e.getMessage(), e);
		}
		return pageResultDto;
	}

	@Override
	public void updateWacaiFundDetail() {
		Page<BusiOrderDto> page = new Page<>();
		page.setPageNo(0);
		page.setPageSize(1000);
		Map<String,Object> conditions = getQueryConditions();
		conditions.put("page", page);
		do {
			page.setPageNo(page.getPageNo()+1);
			List<BusiOrderDto> orders = busiOrderMapper.queryMatchedOrders(conditions);
			updateFundDetailIfNecessary(orders);
		}while(page.getPageNo()<page.getTotalPage());
	}

	private Map<String,Object> getQueryConditions(){
		Date now = new Date();
		Date endTime = DateUtil.getDateBeginTime(now);
		Date startTime = DateUtil.getDateBefore(endTime,1);
		Map<String,Object> conditions = new HashMap<>();
		conditions.put("productType", AppConstants.ProductSubjectType.FINANCE_PLAN);
		conditions.put("creditSource", AppConstants.CreditSource.WACAI.name());
		conditions.put("startTime", startTime);
		conditions.put("endTime", endTime);
		conditions.put("statusList", Arrays.asList(AppConstants.BusiOrderStatus.BUSIORDER_STATUS_0,AppConstants.BusiOrderStatus.BUSIORDER_STATUS_17));
		return conditions;
	}

	private void updateFundDetailIfNecessary(List<BusiOrderDto> orders){
		try {
			List<Long> succeededOrders = new ArrayList<>(orders.size());
			List<Long> failedOrders = new ArrayList<>();
			for(BusiOrderDto orderDto : orders){
				if(orderDto.getFailedSubOrderNum() == 0 && orderDto.getSubOrderSum().compareTo(orderDto.getOrderAmt()) == 0)
					succeededOrders.add(orderDto.getId());
				else
					failedOrders.add(orderDto.getId());
			}
			updateFundDetail(succeededOrders, AppConstants.FinancePlan.FUND_STATUS_3);
			updateFundDetail(failedOrders, AppConstants.FinancePlan.FUND_MATCHING_FAILED);
		}catch (Exception e){
			logger.error(e.getMessage(), e);
			MailUtil.sendMail("更新挖财资金详情记录状态发生异常", e.getMessage());
		}
	}

	@Autowired
	private FundDetailMapper fundDetailMapper;

	private void updateFundDetail(List<Long> orderIds, String status){
		if(orderIds.isEmpty()) return;
		Map<String,Object> map = new HashMap<>();
		map.put("initStatus", AppConstants.FinancePlan.FUND_STATUS_1);
		map.put("status", status);
		map.put("orderIds", orderIds);
		fundDetailMapper.updateByMap(map);
	}

	@Override
	public PageResultDto<BusiOrderDto> queryWacaiSubOrders(QueryOrderReqDto reqDto) {
		PageResultDto<BusiOrderDto> pageResultDto = new PageResultDto<>();
		Map<String,Object> conditions = CommonHelper.transBean2Map(reqDto);
		Page<BusiOrderSub> page = new Page<>();
		page.setPageNo(reqDto.getPageNo());
		page.setPageSize(reqDto.getPageSize());
		conditions.put("page", page);
		try {
			List<BusiOrderSub> orderSubs = busiOrderSubMapper.queryByConditions(conditions);
			List<BusiOrderDto> busiOrderDtos = new ArrayList<>(orderSubs.size());
			for(BusiOrderSub orderSub : orderSubs){
				BusiOrderDto orderDto = new BusiOrderDto();
				BeanUtils.copyProperties(orderSub, orderDto);
				busiOrderDtos.add(orderDto);
			}
			pageResultDto.setDataList(busiOrderDtos);
			pageResultDto.setTotalPage(page.getTotalPage());
			pageResultDto.setTotalSize(page.getTotalRecord());
		}catch (Exception e){
			logger.error(e.getMessage(), e);
		}
		return pageResultDto;
	}
}
