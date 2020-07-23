package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class Model_820004 extends ReqParam {

    @NotNull(message = "用户编号不能为空")
    private Long customerId;

    @NotNull(message = "小队编号不能为空")
    private Long teamId;

    private Long inviteId;

}
