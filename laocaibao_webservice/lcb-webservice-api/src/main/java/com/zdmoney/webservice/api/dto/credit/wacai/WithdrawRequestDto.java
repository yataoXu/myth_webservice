package com.zdmoney.webservice.api.dto.credit.wacai;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Created by 46186 on 2019/3/7.
 */
@Data
public class WithdrawRequestDto extends CashierRequestDto {

    /**提现金额*/
    @NotBlank(message = "提现金额不能为空")
    private String amount;

    /**1-普通提现 2-快速提现*/
    @NotBlank(message = "提现类型不能为空")
    private String withdrawType;

    /**用户*/
    @NotNull(message = "借款编号不能为空")
    private String borrowNo;

}
