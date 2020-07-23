package websvc.models;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

/**
 * Created by gaol on 2017/5/16
 **/
@Data
public class Model_400073 extends ReqParam {

    @NotEmpty(message = "sessionToken不能为空")
    private String sessionToken;

}
