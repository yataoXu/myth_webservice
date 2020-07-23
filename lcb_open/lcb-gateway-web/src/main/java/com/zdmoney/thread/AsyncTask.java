package com.zdmoney.thread;

import com.zdmoney.service.TyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName AsyncTask
 * @Description 异步任务类
 * @Author huangcy
 * @Date 2018/9/4 11:42
 * @Version 1.0
 **/
@Slf4j
@Component
public class AsyncTask {

	@Autowired
	private TyService tyService;

	public void wdtyData(){
		log.info("网贷天眼异步任务*****starting*****");
		tyService.wdtyInfodata();
		tyService.wdtyUserData();
		tyService.wdtyPrepaymentData();
		log.info("网贷天眼异步任务*****ended*****");
	}
}
