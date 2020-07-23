package websvc.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import websvc.req.ReqParam;

@Getter
@Setter
public class Model_400020 extends ReqParam {
    @NotEmpty(message = "手机号不能为空")
    private String cellPhone;
    @NotEmpty(message = "密码不能为空")
    private String password;

}