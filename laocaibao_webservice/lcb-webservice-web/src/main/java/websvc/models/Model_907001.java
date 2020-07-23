package websvc.models;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import websvc.req.ReqParam;

/**
 * Created by qinz on 2018/10/22.
 */
@Data
public class Model_907001 extends ReqParam {

    @NotEmpty(message = "sessionToken不能为空")
    private String sessionToken;

}
