package com.zdmoney.web.dto.team;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TeamListDTO {
	private Long teamId;
	private String teamName;
	private Long teamNum;
	private Long limitNum;
	private BigDecimal monthInvestAmt;
	private String no;
}
