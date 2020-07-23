package com.zdmoney.web.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class RechargeHRDTO implements Serializable {

	/**充值订单号*/
	String orderId;

	/**充值金额*/
	String amount;


}
