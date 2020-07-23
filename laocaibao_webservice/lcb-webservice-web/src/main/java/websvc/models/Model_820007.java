package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class Model_820007 extends ReqParam {

    @NotNull(message = "用户编号不能为空")
    private Long customerId;

    @NotNull(message = "任务编号不能为空")
    private Long flowId;

}
