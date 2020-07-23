package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class Model_800014 extends ReqParam {

    @NotNull(message = "openId不能为空")
    private String openId;

    private String deviceId;

    private String clientId;

}
