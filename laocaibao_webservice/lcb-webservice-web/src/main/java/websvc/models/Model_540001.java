package websvc.models;

import lombok.Data;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

/**
 * Created by user 016/10/21.
 */
@Data
public class Model_540001 extends ReqParam {

    @NotNull(message="用户ID不能为空")
    private Long customerId;

}
