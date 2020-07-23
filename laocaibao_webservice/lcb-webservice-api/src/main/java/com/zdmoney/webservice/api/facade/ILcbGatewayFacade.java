package com.zdmoney.webservice.api.facade;


import com.zdmoney.webservice.api.common.dto.ResultDto;
import com.zdmoney.webservice.api.dto.customer.CustomerLevelChangeLogDto;
import com.zdmoney.webservice.api.dto.customer.CustomerMainInfoVo;
import com.zdmoney.webservice.api.dto.fl.FlwReqDto;
import com.zdmoney.webservice.api.dto.wdty.RebateReqDto;
import com.zdmoney.webservice.api.dto.wdty.WdtyReqDto;
import com.zdmoney.webservice.api.dto.wdzj.LoanInfoData;
import com.zdmoney.webservice.api.dto.wdzj.LoanInfoDto;
import com.zdmoney.webservice.api.dto.wdzj.LoanSplitDto;
import com.zdmoney.webservice.api.dto.wdzj.LoanUserVo;
import com.zdmoney.webservice.api.dto.ym.*;

import java.util.List;
import java.util.Map;

/**
 * Created by pc05 on 2017/11/21.
 */
public interface ILcbGatewayFacade {

	ResultDto getProductInfo(BusiProductDto busiProductDto);

	ResultDto getRebateInfo(BusiRebateDto busiRebateDto);

	ResultDto getCustomerInfo(CustomerMainInfoDto customerMainInfoDto);

	ResultDto getOrderInfo(ViewOrderDto viewOrderDto);

	ResultDto selectCountByCmNumber(String cmNumber);

	ResultDto updateByPrimaryKey(CustomerRelationShipDto customerRelationShipDto);

	ResultDto insertSelective(CustomerRelationShipDto customerRelationShipDto);

	ResultDto selectTotalCount();

	ResultDto getUserInfo(String cmNumber);

	ResultDto saveCustomerLevelLog(CustomerLevelChangeLogDto customerLevelLogDto);

	ResultDto getUserIdByInvideCode(String invideCode);

	ResultDto updateByCustomerId(CustomerMainInfoVo user);

	ResultDto selectCustomerByIdNum(String idNum);

	/**
	 * 网贷之家接口数据集合
	 *
	 * @param loanInfoDto
	 * @return
	 */
	ResultDto getLoanInfo(LoanInfoDto loanInfoDto);

	/**
	 * 根据类型获取标的数量
	 *
	 * @param dataType
	 * @return
	 */
	int countSplitInfo(Integer dataType);

	/**
	 * 根据类型获取投标信息数量
	 *
	 * @param dataType
	 * @return
	 */
	int countSplitUser(Integer dataType);

	/**
	 * 查询今日插入表的数量
	 *
	 * @return
	 */
	int countWdzjData();

	/**
	 * --蓝枫赛殷 个贷,理财计划非转让
	 *
	 * @param loanSplitDto
	 * @return
	 */
	ResultDto getLoanInfoData(LoanSplitDto loanSplitDto);

	/**
	 * 理财计划转让
	 *
	 * @param loanSplitDto
	 * @return
	 */
	ResultDto getLoanTransferData(LoanSplitDto loanSplitDto);

	/**
	 * 优选
	 *
	 * @param loanSplitDto
	 * @return
	 */
	ResultDto getPreferenceData(LoanSplitDto loanSplitDto);

	/**
	 * 投标人信息
	 *
	 * @param loanSplitDto
	 * @return
	 */
	ResultDto getLoanUserData(LoanSplitDto loanSplitDto);

	/**
	 * 保存标的信息
	 *
	 * @param list
	 * @return
	 */
	int saveLoanInfo(List<LoanInfoData> list);

	/**
	 * 保存投标信息
	 *
	 * @param list
	 * @return
	 */
	int saveLoanUser(List<LoanUserVo> list);

	LoanInfoData getBusiProductInfo(String productId);

	LoanInfoData getBusiProductSubInfo(String productId);

	/**
	 * 查询wdzj_loan_info 昨日起息的总金额
	 *
	 * @return
	 */
	String getLastMoney();

	/**
	 * 查询busi_order_sub昨日起息总金额
	 *
	 * @return
	 */
	String getOrderLastMoney();

	String earlyRepaymentData(Map params);

	/**
	 * 网贷天眼 - 借款数据
	 *
	 * @param req
	 * @return
	 */
	String wdtyBorrowData(WdtyReqDto req);

	/**
	 * 网贷天眼 - 投资数据
	 *
	 * @param req
	 * @return
	 */
	String wdtyInvestData(WdtyReqDto req);

	/**
	 * 网贷天眼 - 提前结清数据
	 *
	 * @param req
	 * @return
	 */
	String prepaymentData(WdtyReqDto req);

	/**
	 * 网贷天眼 - 查询每日借款数据插入条数
	 *
	 * @return
	 */
	int countWdzyInfoByDate();

	/**
	 * 网贷天眼 - 查询每日提前结清数据插入条数
	 *
	 * @return
	 */
	int countWdzyPrepaymentByDate();

	/**
	 * 网贷天眼 - 查询每日投资数据插入条数
	 *
	 * @return
	 */
	int countWdzyUserByDate();

	/**
	 * 网贷天眼 - 每日借款数据插入
	 *
	 * @return
	 */
	int insertLastDayWdzjInfoData();

	/**
	 * 网贷天眼 - 每日投资数据插入
	 *
	 * @return
	 */
	int insertLastDayWdzjUserData();

	/**
	 * 网贷天眼 - 每日提前结清数据插入
	 *
	 * @return
	 */
	int insertLastDayWdzjPrepaymentData();

	/**
	 * 网贷天眼 - 获取订单信息
	 *
	 * @param reqDto
	 * @return
	 */
	String getOrderRebates(RebateReqDto reqDto);

	/**
	 * 返利网 - 获取订单信息
	 *
	 * @param reqDto
	 * @return
	 */
	String getOrderFlw(FlwReqDto reqDto);

	/**
	 * 零壹财经标的信息推送
	 *
	 * @param dateTime
	 * @param pageNo
	 * @param pageSize
	 */
	String sendLycjInfoData(String dateTime, int pageNo, int pageSize);

	/**
	 * 零壹财经投标信息推送
	 *
	 * @param dateTime
	 * @param pageNo
	 * @param pageSize
	 */
	String sendLycjUserData(String dateTime, int pageNo, int pageSize);

	/**
	 * 零壹财经提前结清推送
	 *
	 * @param dateTime
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	String sendLycjPrepaymentData(String dateTime, int pageNo, int pageSize);

	/**
	 * 插入离职员工到白名单
	 *
	 * @return
	 */
	ResultDto insertStaffWhilte(String idNum, String quitTime);
}
