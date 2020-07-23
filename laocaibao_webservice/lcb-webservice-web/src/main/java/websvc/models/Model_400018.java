package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class Model_400018  extends ReqParam{
    @NotNull(message="手机号不能为空")
    private String cellPhone;//手机号

    @NotNull(message="验证码不能为空")
    private String validateCode;//验证码

    @NotNull(message="密码不能为空")
    private String password;//登录密码

    @NotNull(message="微信号不能为空")
    private String openId;//微信openId

    private String deviceId;

    private String clientId;

    private String ip;
}
