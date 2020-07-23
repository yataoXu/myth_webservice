package com.zdmoney.webservice.api.dto.customer;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by user on 2017/11/28.
 */
@Getter
@Setter
public class TransferingDto extends BaseValidableDto{

    @NotNull(message = "订单号不能为空")
    private Long orderId;

    @NotNull(message = "用户编号不能为空")
    private Long customerId;

    @NotNull(message = "转让价格不能为空")
    private BigDecimal transferPrice;

    @NotNull(message = "转让日期不能为空")
    private Date transferDate;
}
