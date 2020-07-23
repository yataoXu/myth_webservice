package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class Model_500805 extends ReqParam {

    @NotNull(message = "订单号不能为空")
    private Long orderId;

    @NotNull(message = "用户编号不能为空")
    private Long customerId;

   // @NotNull(message = "提前退出服务费不能为空")
   // private BigDecimal exitFeeRateAmt;//扣除手续费

  //  @NotNull(message = "提前退出价格不能为空")
   // private BigDecimal totalAmt;//扣除手续费后金额

}
