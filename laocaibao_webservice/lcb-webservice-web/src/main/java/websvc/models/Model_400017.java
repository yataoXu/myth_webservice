package websvc.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import websvc.req.ReqParam;

@Getter
@Setter
public class Model_400017 extends ReqParam {

    @NotBlank(message = "sessionToken不能为空")
    private String sessionToken;

}

