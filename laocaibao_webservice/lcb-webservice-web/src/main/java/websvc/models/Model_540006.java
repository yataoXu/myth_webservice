package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

/**
 * 个贷-委托书协议
 */
@Getter
@Setter
public class Model_540006 extends ReqParam {


    @NotNull(message = "订单ID不能为空")
    private Long orderId;

}
