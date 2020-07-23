package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

/**
 * 查询产品详情协议
 */
@Getter
@Setter
public class Model_540009 extends ReqParam {


    @NotNull(message = "产品ID不能为空")
    private Long productId;

}
