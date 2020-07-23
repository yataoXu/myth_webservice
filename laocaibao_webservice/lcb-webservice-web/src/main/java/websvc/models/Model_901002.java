package websvc.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import websvc.req.ReqParam;

@Getter
@Setter
public class Model_901002 extends ReqParam {

    @NotBlank(message = "heroSessionToken不能为空")
    private String heroSessionToken; //竞选人sessionToken

    private String voterSessionToken;  //投票人sessionToken

}

