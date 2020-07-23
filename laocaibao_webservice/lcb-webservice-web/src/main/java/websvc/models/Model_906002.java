package websvc.models;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import websvc.req.ReqParam;

/**
 * Created by gaol on 2017/5/15
 **/
@Data
public class Model_906002 extends ReqParam {

    @NotEmpty(message = "cmNumber不能为空")
    private String cmNumber;

}
