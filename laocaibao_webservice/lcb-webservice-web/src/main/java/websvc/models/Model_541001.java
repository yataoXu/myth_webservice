package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

/**
 * 理财计划续投服务协议
 */
@Getter
@Setter
public class Model_541001 extends ReqParam {


    @NotNull(message = "订单ID不能为空")
    private Long orderId;

}
