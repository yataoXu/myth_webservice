package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class Model_400016 extends ReqParam {

    @NotNull(message = "用户编号不能为空")
    private Long customerId;

}
