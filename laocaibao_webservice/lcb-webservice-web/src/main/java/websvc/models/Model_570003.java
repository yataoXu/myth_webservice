package websvc.models;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

/**
 * Created by silence.cheng on 2019/1/7.
 */

@Data
public class Model_570003 extends ReqParam {

    @NotEmpty(message = "sessionToken不能为空")
    private String  sessionToken;

}
