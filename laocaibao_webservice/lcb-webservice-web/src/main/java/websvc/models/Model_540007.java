package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

/**
 * 支付协议模板  产品详情协议模板
 */
@Getter
@Setter
public class Model_540007 extends ReqParam {


    @NotNull(message = "产品ID不能为空")
    private Long productId;

}
