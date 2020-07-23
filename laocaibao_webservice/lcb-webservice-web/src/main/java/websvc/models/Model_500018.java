package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class Model_500018 extends ReqParam {
    @NotNull(message = "订单号不能为空")
    private Long orderId;
}
