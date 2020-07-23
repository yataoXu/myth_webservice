package com.zdmoney.controller;

import com.zdmoney.webservice.api.dto.fl.FlwReqDto;
import com.zdmoney.webservice.api.facade.ILcbGatewayFacade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @ClassName FanliController
 * @Description 返利网
 * @Author huangcy
 * @Date 2018/10/29 11:14
 * @Version 1.0
 **/
@Slf4j
@Controller
@RequestMapping("/openapi/fanli")
public class FanliController {
	@Autowired
	private ILcbGatewayFacade lcbGatewayFacade;

	@RequestMapping("/orderInfo")
	@ResponseBody
	public Object borrowData(FlwReqDto flwReqDto) {
		log.info("返利网查询订单接口Req:[{}]",flwReqDto);
		String orderFlw = lcbGatewayFacade.getOrderFlw(flwReqDto);
		log.info("返利网查询订单接口Res:[{}]",orderFlw);
		return orderFlw;
	}
}
