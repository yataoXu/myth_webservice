package com.zdmoney.controller;

import com.alibaba.fastjson.JSON;
import com.zdmoney.base.BaseTest;
import com.zdmoney.utils.MD5Utils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class WdzjControllerTest extends BaseTest{

	@Test
	public void getLoanInfo() {


	}


	//重新授权 缴费额度重新授权
	public static void main(String args[]){
		Map<String, Object> map = new HashMap<>();
		map.put("merchantCode", "820181212861");
		map.put("backNotifyUrl", "https://trade.laocaibao.com/laocaibaoVesionService/zdpay/backNotify");
		map.put("clientType", 0);
		map.put("loginId", "13728210849");
		Map<String, Object> temp = new HashMap<>();
		temp.put("userId", "960551");
		map.put("remark", JSON.toJSONString(temp));
		map.put("pageUrl", "https://www.laocaibao.com/credit/security/openAccountAuthorization");
		map.put("grantType", 2);
		map.put("autoFeeAmt", 100000000);
		map.put("autoFeeTerm", "20290411");
		map.put("autoRepayTerm", "20290411");
		map.put("autoRepayAmt", 0);
		map.put("authStatus", "010100000000");
		map.put("backUrl", "https://www.laocaibao.com/credit");


		StringBuilder url = new StringBuilder("https://zdpay.laocaibao.com/zdpay_cashier/trade/userGrant?merchantCode=820181212861");

		for (Map.Entry<String, Object> entry : map.entrySet()) {
			if ("merchantCode".equals(entry.getKey())) continue;
			url.append("&").append(entry.getKey()).append("=").append(entry.getValue());
		}
		url.append("&signature=").append(MD5Utils.MD5Encrypt(map, "67bbd9287912a261"));
		System.out.println(url.toString());
	}
}