package websvc.models;

import lombok.Data;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class Model_430013 extends ReqParam {

    @NotNull(message="客户号不能为空!")
    private Long customerId;

    @NotNull(message="产品ID不能为空")
    private Long productId;

    @NotNull(message = "订单金额不能为空")
    private BigDecimal orderAmt;

}
