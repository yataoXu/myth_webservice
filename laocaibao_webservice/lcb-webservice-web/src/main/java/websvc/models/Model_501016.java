package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class Model_501016 extends ReqParam {

    @NotNull(message="产品ID不能为空")
    private Long productId;

    private Long customerId;
}
