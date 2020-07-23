package websvc.models;

import lombok.Data;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Model_521005
 *
 * @Author: wein
 * @Description:
 * @Date: Created in 2018/8/28 15:07
 * @Mail: wein@zendaimoney.com
 */
@Data
public class Model_521005 extends ReqParam {

    @NotNull(message="客户号不能为空!")
    private Long customerId;

    @NotNull(message = "订单号不能为空")
    private Long orderId;

    @NotNull(message = "续期类型不能为空")
    private String reinvestType;
}
