package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class Model_520001  extends ReqParam {

    /*客户编号*/
    @NotNull(message = "用户编号不能为空")
    private Long customerId;

    /*需要支付的订单号*/
    @NotNull(message = "订单编号不能为空")
    private Long orderId;

    /*使用积分金额*/
    private String integralAmt;

    /*使用红包消费的红包id*/
    private String redId;

    /*使用加息券的编号*/
    private String voucherId;

    /*支付密码*/
    private String payPassword;
}
