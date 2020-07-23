package com.zdmoney.webservice.api.dto.customer;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created by user on 2017/11/21.
 */
public class OrderingDto extends BaseValidableDto{

    @NotNull(message = "用户ID无效")
    private Long customerId;

    @NotNull(message = "产品ID不能为空")
    private Long productId;

    @NotNull(message = "订单金额不能为空")
    private BigDecimal orderAmt;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public BigDecimal getOrderAmt() {
        return orderAmt;
    }

    public void setOrderAmt(BigDecimal orderAmt) {
        this.orderAmt = orderAmt;
    }
}
