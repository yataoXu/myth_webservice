package com.zdmoney.webservice.api.dto.Asset;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2018/8/16.
 */
@Data
public class UnpaidDebtToTransferDto implements Serializable {

    private static final long serialVersionUID = 4013530681515523170L;

    @NotNull(message = "订单编号不能为空")
    private String orderNo;

    @NotNull(message = "订单金额不能为空")
    private BigDecimal orderAmt;

    @NotNull(message = "债权价值不能为空")
    private BigDecimal debtWorth;

    private List<Integer> periods = new ArrayList<>();
}
