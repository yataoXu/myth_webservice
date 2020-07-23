/*
* Copyright (c) 2015 zendaimoney.com. All Rights Reserved.
*/  
package com.zdmoney.vo;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class BusiRebateVo {
	private String purchaseDate;
	
	private String inviteCode;
	
	private BigDecimal rebateAmt;

}
 