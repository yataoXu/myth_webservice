package com.zdmoney.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Random;

import com.zdmoney.assets.api.utils.DateUtils;
import com.zdmoney.mapper.financePlan.BusiOrderSubMapper;
import com.zdmoney.models.financePlan.BusiOrderSub;
import com.zdmoney.service.businessReport.BusinessReportService;
import com.zdmoney.service.welfare.WelfareService;
import com.zdmoney.trace.log.provider.LogProvider;
import com.zdmoney.webservice.api.dto.customer.CashCouponDto;
import com.zdmoney.webservice.api.facade.IManagerFacadeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.zdmoney.trace.log.common.LogLevel;

import com.zdmoney.service.HealthCheckService;

@Controller
@Scope("singleton")
@RequestMapping("/healthCheck")
@Slf4j
public class HealthCheckController {
	@Autowired
	private HealthCheckService checkService;

	@Autowired
	private IManagerFacadeService managerFacadeService;


	@Autowired
	private RedisTemplate<String,Object> redisTemplate;



	@Autowired
	BusinessReportService reportService;

	@Autowired
	private WelfareService welfareService;

	@Autowired
	private BusiOrderSubMapper orderSubMapper;

	@RequestMapping(value = "/verify")
	@ResponseBody
	public String testConnection() throws IOException {
		try {
			int i = checkService.testConnection();

			log.info("Health check begin-----------------------------------");
			log.info("最大可用内存:" + Runtime.getRuntime().maxMemory() / 1024 / 1024 + "M\t");
			log.info("当前JVM空闲内存:" + Runtime.getRuntime().freeMemory() / 1024 / 1024 + "M\t");
			log.info("当前JVM占用的内存总数:" + Runtime.getRuntime().totalMemory() / 1024 / 1024 + "M\t");
			log.info("Health check end------------------------------------");

			return "OK";
		} catch (Exception e) {
			return "FAIL";
		}
	}

	@RequestMapping(value = {"/logLevel"}, method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public String changeLogLevel(String packageName, String logLevel) throws IOException {
		if (StringUtils.isBlank(packageName) || StringUtils.isBlank(logLevel)) {
			return "packageName or logLevel must not null";
		}
		try {
			LogProvider logProvider = new LogProvider();
			//修改某个包或类名（全路径）的等级
			logProvider.setLoggerNameLevel(packageName,LogLevel.valueOf(logLevel));
			return "chane log Level OK";
		} catch (Exception e) {
			return " chane log Level Fail"+e;
		}
	}

	@RequestMapping(value = {"/test"}, method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public String test(String d) throws IOException {

		CashCouponDto cashCouponDto = new CashCouponDto();
		cashCouponDto.setCmNumber("01170311000008283");
		cashCouponDto.setCashType("2");
		cashCouponDto.setRepayAmt(new BigDecimal(1900));
		Date date = DateUtils.parse(d);
		cashCouponDto.setRepayDate(date);
		managerFacadeService.sendRepayCashCoupon(cashCouponDto);
			return " true";

	}

	@RequestMapping(value = {"/order"}, method = {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public String order() throws IOException {

		BusiOrderSub sub= orderSubMapper.selectByPrimaryKey(1382413L);
		welfareService.sendInvestCash("01170311000008283",sub);
		return " true";

	}

}
