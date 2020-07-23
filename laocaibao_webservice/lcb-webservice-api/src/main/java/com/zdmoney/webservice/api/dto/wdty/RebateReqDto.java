package com.zdmoney.webservice.api.dto.wdty;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName RebateReqDto
 * @Description 网贷天眼平台返利 请求dto
 * @Author huangcy
 * @Date 2018/9/13 21:27
 * @Version 1.0
 **/
@Data
public class RebateReqDto implements Serializable{

	private String org_code="p2peye";

	private String start_time;

	private String end_time;

	private String mobile;

	private String order_id;

	private Integer timestamp;

	private String signature;
}
