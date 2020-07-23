package websvc.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import websvc.req.ReqParam;

/**
 * Created by 00225181 on 2016/5/9.
 */
@Getter
@Setter
public class Model_700008 extends ReqParam {
    private static final long serialVersionUID = -7158307106352720808L;
    @NotEmpty(message = "手机号码不能为空")
    private String cellPhone;
    @NotEmpty(message = "验证码不能为空")
    private String validCode;
    @NotEmpty(message = "商户号不能为空")
    private String merchantCode;
    @NotEmpty(message = "密码不能为空")
    private String password;
    @NotEmpty(message = "微信openId不能为空")
    private String openId;

    private String ip;
}
