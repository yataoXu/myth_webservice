package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class Model_500037 extends ReqParam {

    @NotNull(message = "协议编号不能为空")
    private String protocolNo;

}
