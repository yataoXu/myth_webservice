package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

/**
 * Created by zhou on 2016/2/23.
 */
@Setter
@Getter
public class Model_800006 extends ReqParam {

    @NotNull(message = "用户编号不能为空")
    private Long customerId;

    @NotNull(message = "商品编号不能为空")
    private Long productId;

}
