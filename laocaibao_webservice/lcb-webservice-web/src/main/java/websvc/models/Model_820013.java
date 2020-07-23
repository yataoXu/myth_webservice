package websvc.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class Model_820013 extends ReqParam {

    @NotNull(message = "邀请用户编号不能为空")
    private Long customerId;

    @NotBlank(message = "手机号不能为空")
    private String cellphone;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "验证码不能为空")
    private String verificationCode;

    private String ip;

}
