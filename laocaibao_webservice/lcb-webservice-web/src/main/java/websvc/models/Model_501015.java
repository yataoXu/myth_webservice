package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;

@Getter
@Setter
public class Model_501015 extends ReqParam {

//    @NotBlank(message="用户编号不能为空")
    private String customerId;

    private String fromStatus = "0"; //请求来源 默认 0:app 1:pc
}
