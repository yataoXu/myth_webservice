package websvc.models;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import sun.nio.cs.ext.Big5;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class Model_430010 extends ReqParam {

    @NotNull(message = "用户ID不能为空")
    private Long customerId;

    @NotEmpty(message = "返回地址不能为空")
    private String pageUrl;

    private String backUrl;

    /**
     * 转让服务费
     * 为空  购买不足授权, 反之  转让不足，授权
     */
    private BigDecimal serviceFee;

    private Long productId;

    private BigDecimal orderAmt;
}
