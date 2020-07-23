package com.zdmoney.webservice.api.dto.customer;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Created by user on 2017/11/27.
 */
@Getter
@Setter
public class WithdrawingDto extends BaseValidableDto {

    @NotEmpty(message = "客户编号不能为空！")
    private String customerId;

    @NotEmpty(message = "提现金额不能为空！")
    private String withDrawAmount;

    private String subBankCode;

    private String subBankName;

    @NotEmpty(message = "提现密码不能为空！")
    private String payPassword;

    /*银行卡号*/
    @NotBlank(message="银行卡号不能为空!")
    private String bankCard;
}
