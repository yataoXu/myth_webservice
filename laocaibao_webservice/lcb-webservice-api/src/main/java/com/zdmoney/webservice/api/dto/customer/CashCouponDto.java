package com.zdmoney.webservice.api.dto.customer;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @ Author : silence.cheng.
 * @ Description :
 * @ Date : Crreate in 2018/11/30 16:41
 */
@Data
public class CashCouponDto implements Serializable {

    @NotBlank(message="用户编号不能为空!")
    private String cmNumber;

    @NotNull(message = "回款日期不能为空")
    private Date repayDate;

    private String cashType;//现金券类型 1：注册  2：回款 3：投资

    @NotNull(message = "回款金额不能为空")
    private BigDecimal repayAmt;


}
