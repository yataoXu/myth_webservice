package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

/**
 * 个贷-借款协议
 */
@Getter
@Setter
public class Model_540008 extends ReqParam {


    @NotNull(message = "订单ID不能为空")
    private Long orderId;

}
