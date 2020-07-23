package com.zdmoney.service;

import com.zdmoney.webservice.api.facade.ILcbGatewayFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

/**
 * @ClassName TyService
 * @Description 网贷天眼定时任务
 * @Author huangcy
 * @Date 2018/9/4 10:07
 * @Version 1.0
 **/
@Slf4j
@Service
public class TyService {

	@Autowired
	private ILcbGatewayFacade lcbGatewayFacade;

	/**
	 * 网贷天眼 推送标的信息
	 * @param dateTime
	 * @param pageNo
	 * @param pageSize
	 */
	@Async
	public Future<String> sendLycjLoanInfo(String dateTime, int pageNo, int pageSize){
		return new AsyncResult<>(lcbGatewayFacade.sendLycjInfoData(dateTime, pageNo, pageSize));
	}

	/**
	 * 网贷天眼 推送投标数据
	 * @param dateTime
	 * @param pageNo
	 * @param pageSize
	 */
	@Async
	public Future<String> sendLycjLoanUser(String dateTime,int pageNo,int pageSize){
		return new AsyncResult<>(lcbGatewayFacade.sendLycjUserData(dateTime,pageNo,pageSize));
	}

	@Async
	public Future<String> sendLycjPrepayment(String dateTime,int pageNo,int pageSize){
		return new AsyncResult<>(lcbGatewayFacade.sendLycjPrepaymentData(dateTime,pageNo,pageSize));
	}

	/**
	 * @description 网贷天眼 - 借款数据
	 *		
	 * @params []
	 * @return void
	 * @date: 2018/9/4 10:10
	 * @author: huangcy
	 */
	@Async
	public void wdtyInfodata(){
		int lastDayWdzjInfoData = 0 ;
		int countWdzyInfoByDate = lcbGatewayFacade.countWdzyInfoByDate();
		log.info("网贷天眼每日借款数据数据抓取*****starting*****,countWdzyInfoByDate:"+countWdzyInfoByDate);
		if(countWdzyInfoByDate==0){//当天没有插入数据
			lastDayWdzjInfoData = lcbGatewayFacade.insertLastDayWdzjInfoData();
		}
		log.info("网贷天眼每日借款数据数据抓取*****ended*****,影响行数:"+lastDayWdzjInfoData);
	}

	/**
	 * @description 网贷天眼 - 投资数据
	 *		
	 * @params []
	 * @return void
	 * @date: 2018/9/4 10:11
	 * @author: huangcy
	 */
	@Async
	public void wdtyUserData(){
		int lastDayWdzjUserData = 0 ;
		int countWdzyUserByDate = lcbGatewayFacade.countWdzyUserByDate();
		log.info("网贷天眼每日投资数据数据抓取*****starting*****,countWdzyUserByDate:"+countWdzyUserByDate);
		if(countWdzyUserByDate==0){//当天没有插入数据
			lastDayWdzjUserData = lcbGatewayFacade.insertLastDayWdzjUserData();
		}
		log.info("网贷天眼每日投资数据抓取*****ended*****,影响行数:"+lastDayWdzjUserData);
	}
	
	/**
	 * @description 网贷天眼 - 提前结清数据
	 *		
	 * @params []
	 * @return void
	 * @date: 2018/9/7 9:53
	 * @author: huangcy
	 */
	@Async
	public void wdtyPrepaymentData(){
		int lastDayWdzjPrepaymentData = 0 ;
		int countWdzyPrepaymentByDate = lcbGatewayFacade.countWdzyPrepaymentByDate();
		log.info("网贷天眼每日提前结清数据数据抓取*****starting*****,countWdzyPrepaymentByDate:"+countWdzyPrepaymentByDate);
		if(countWdzyPrepaymentByDate==0){//当天没有插入数据
			lastDayWdzjPrepaymentData = lcbGatewayFacade.insertLastDayWdzjPrepaymentData();
		}
		log.info("网贷天眼每日提前结清数据抓取*****ended*****,影响行数:"+lastDayWdzjPrepaymentData);
	}
}
