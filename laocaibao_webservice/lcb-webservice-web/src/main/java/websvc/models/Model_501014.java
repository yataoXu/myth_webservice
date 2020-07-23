package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;

@Getter
@Setter
public class Model_501014 extends ReqParam {

//    @NotBlank(message="用户编号不能为空")
    private String customerId;

}
