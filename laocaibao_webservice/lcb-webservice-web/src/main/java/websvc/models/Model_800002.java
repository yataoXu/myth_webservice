package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class Model_800002 extends ReqParam {
    private static final long serialVersionUID = 4660141579652741436L;

    @NotNull(message="商品编号不能为空")
    private Long productId;

}
