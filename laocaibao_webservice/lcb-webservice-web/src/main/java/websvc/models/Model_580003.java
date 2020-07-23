package websvc.models;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import websvc.req.ReqParam;

/**
 * Created by silence.cheng on 2019/1/7.
 */

@Data
public class Model_580003 extends ReqParam {

    @NotEmpty(message = "sessionToken不能为空")
    private String  sessionToken;

}
