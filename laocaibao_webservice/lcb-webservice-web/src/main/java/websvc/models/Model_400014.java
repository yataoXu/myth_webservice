package websvc.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import websvc.req.ReqParam;

@Getter
@Setter
public class Model_400014 extends ReqParam {

    @NotBlank(message = "手机号不能为空")
    private String cellphone;

}
