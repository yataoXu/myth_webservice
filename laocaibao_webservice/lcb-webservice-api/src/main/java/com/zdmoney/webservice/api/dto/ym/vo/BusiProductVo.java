package com.zdmoney.webservice.api.dto.ym.vo;/**
 * Created by pc05 on 2017/11/21.
 */

import com.zdmoney.webservice.api.common.dto.BaseDto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 描述 :
 *
 * @author : huangcy
 * @create : 2017-11-21 15:49
 * @email : huangcy01@zendaimoney.com
 **/
@Data
public class BusiProductVo extends BaseDto {

    private String productName;

    private BigDecimal yearRate;

    private String term;

    private Date interestStartDate;

    private BigDecimal productPrincipal;

    private String upLowFlag;

    private BigDecimal totalInvestAmt;

    private BigDecimal totalInvestPerson;

    private String name;

    private Date showStartDate;

    private Date showEndDate;

    private Long limitType;

    private BigDecimal investLower;

    private String productType;

    private Date saleStartDate;

    private Date saleEndDate;

}
