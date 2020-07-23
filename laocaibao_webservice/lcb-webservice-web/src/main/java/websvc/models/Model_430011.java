package websvc.models;

import lombok.Data;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

@Data
public class Model_430011 extends ReqParam {

    @NotNull(message = "用户ID不能为空")
    private Long customerId;

}
