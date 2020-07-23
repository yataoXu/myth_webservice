package websvc.models;

import lombok.Data;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class Model_430019 extends ReqParam {

    @NotNull(message="customerId不能为空")
    private Long customerId;

    @NotNull(message="出借金额不能为空")
    private BigDecimal investAmt;

}
