package com.zdmoney.webservice.api.dto.ym.vo;/**
 * Created by pc05 on 2017/11/22.
 */

import com.zdmoney.webservice.api.common.dto.BaseDto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 描述 :
 *
 * @author : huangcy
 * @create : 2017-11-22 9:33
 * @email : huangcy01@zendaimoney.com
 **/
@Data
public class BusiRebateVo extends BaseDto{
    private String orderNo;

    private String customerCode;

    private BigDecimal rebateAmt;

    private Date createDate;
}
