package websvc.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import websvc.req.ReqParam;

@Setter
@Getter
public class Model_410002 extends ReqParam {

    @NotBlank(message = "手机号不能为空")
    private String cmCellphone;

    @NotBlank(message = "旧交易密码不能为空")
    private String oldTradePassword;

    @NotBlank(message = "新交易密码不能为空")
    private String newTradePassword;

}
