package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

/**
 * Created by zhou on 2016/5/5.
 */
@Setter
@Getter
public class Model_800017 extends ReqParam {

    @NotNull(message = "用户编号不能为空")
    private Long customerId;

    @NotNull(message = "商品编号不能为空")
    private Long productId;

}
