package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;


@Setter
@Getter
public class Model_500055 extends ReqParam {

    @NotNull(message="转让价格不能为空")
    private BigDecimal transferPrice;

    @NotNull(message="产品估值不能为空")
    private BigDecimal estimatePrice;

    @NotNull(message="订单ID不能为空")
    private Long orderId;

    @NotNull(message="转让日期不能为空")
    private Date transferDate;

}
