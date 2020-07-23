package websvc.models;

import lombok.Data;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

@Data
public class Model_521004 extends ReqParam {
    @NotNull(message="orderID不能为空!")
    private Long orderId;


}