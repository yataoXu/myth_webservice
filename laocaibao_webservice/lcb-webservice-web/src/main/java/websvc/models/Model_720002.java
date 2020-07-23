package websvc.models;

import com.zdmoney.constant.AppConstants;
import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class Model_720002 extends ReqParam {

    @NotNull(message = AppConstants.LoginBusiOrder.BUSIORDER_ERROR_2)
    private Long customerId;

    @NotNull(message = "产品ID不能为空")
    private Long productId;

    @NotNull(message = "订单金额不能为空")
    private BigDecimal orderAmt;

    private String inviteCode;

    private String couponId;

}
