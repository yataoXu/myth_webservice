package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created by 00245337 on 2016/9/22.
 */
@Getter
@Setter
public class Model_500040 extends ReqParam {
    private static final long serialVersionUID = -5085395263019922410L;
    @NotNull(message = "用户Id不能为空")
    private Long customerId;//标的编号
    @NotNull(message = "产品Id不能为空")
    private Long productId;//标的编号

    private BigDecimal orderAmt = new BigDecimal(0);
}