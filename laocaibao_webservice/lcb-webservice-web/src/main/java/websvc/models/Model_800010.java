package websvc.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import websvc.req.ReqParam;

@Setter
@Getter
public class Model_800010 extends ReqParam {
    private static final long serialVersionUID = -2957233219800845668L;

    @NotEmpty(message = "userToken不能为空")
    private String userToken;

}
