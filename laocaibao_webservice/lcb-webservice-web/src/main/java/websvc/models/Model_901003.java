package websvc.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import websvc.req.ReqParam;

@Getter
@Setter
public class Model_901003 extends ReqParam {

    @NotBlank(message = "heroSessionToken不能为空")
    private String heroSessionToken; //竞选人sessionToken

    private String voterSessionToken;  //投票人sessionToken

    private String voterCellPhone;  //投票人手机号

    private String voterValidateCode;  //投票人手机验证码

    private String ip;

}

