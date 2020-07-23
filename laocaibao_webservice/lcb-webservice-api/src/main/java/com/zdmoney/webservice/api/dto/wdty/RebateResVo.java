package com.zdmoney.webservice.api.dto.wdty;

import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName RebateResVo
 * @Description 网贷天眼平台返利 - 响应vo
 * @Author huangcy
 * @Date 2018/9/14 9:24
 * @Version 1.0
 **/
@Data
public class RebateResVo<T> implements Serializable{

	private String code;

	private String message;

	private List<T> data;

	public RebateResVo(String code, String message, List data){
		this.code=code;
		this.message=message;
		this.data=data;
	}

	public RebateResVo() {

	}

	public static RebateResVo SUCCESS(List data){
		return new RebateResVo(RebateResCode.SUCCESS.code,RebateResCode.SUCCESS.msg,data);
	}

	public static RebateResVo FAIL(RebateResCode rebateResCode){
		return new RebateResVo(rebateResCode.code,rebateResCode.msg,null);
	}


	/**
	 * 网贷天眼平台返利 响应
	 */
	public enum RebateResCode {

		SUCCESS("200","成功"),

		TIME_OUT("4001","查询时间戳超时"),

		CHECK_SIGN_FAILED("4002","签名校验失败"),

		CHECK_WHITELIST_FAILED("4003","白名单校验失败"),

		NO_SERVICE("4004","接口服务不存在"),

		NO_USER("4005","查询的用户不存在"),

		NO_ORDER("4006","查询的订单不存在"),

		REQDATA_PARSE_ERROR("4007","请求数据解析错误"),

		ENDTIME_LESS_STARTTIME("4008","查询结束时间小于开始时间"),

		TIME_OUT_LIST("4009","查询时间跨度超出限制"),

		SYS_EXCEPTION("5000","系统服务暂停");

		private String code;
		private String msg;

		RebateResCode(String code,String msg) {
			this.code=code;
			this.msg = msg;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}
	}
}
