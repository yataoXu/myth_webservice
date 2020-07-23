package websvc.models;

import lombok.Data;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

/**
 * Created by user on 2016/12/27.
 */
@Data
public class Model_500060 extends ReqParam {

    @NotNull(message = "产品ID不能为空")
    private Long productId;

    private Integer pageNo;

    private Integer pageSize;

}
