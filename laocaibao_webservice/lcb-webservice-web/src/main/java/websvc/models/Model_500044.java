package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class Model_500044 extends ReqParam {

    @NotNull(message = "订单号不能为空")
    private Long orderId;

    @NotNull(message = "用户编号不能为空")
    private Long customerId;

    @NotNull(message = "转让价格不能为空")
    @Digits(integer = 11, fraction = 2, message = "转让价格格式不正确")
    private BigDecimal transferPrice;

    @NotNull(message = "转让日期不能为空")
    private Date transferDate;



}
