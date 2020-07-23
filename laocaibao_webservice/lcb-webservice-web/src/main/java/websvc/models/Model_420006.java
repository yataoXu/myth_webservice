package websvc.models;

import lombok.Data;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

@Data
public class Model_420006 extends ReqParam {

    @NotNull(message = "客户ID不能为空")
    private Long customerId;

    @NotNull(message = "产品ID不能为空")
    private Long productId;
}
