package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class Model_810001 extends ReqParam {
    @NotNull(message="cmToken不能为空")
    private String cmToken;

}
