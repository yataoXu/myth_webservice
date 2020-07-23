package websvc.models;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import websvc.req.ReqParam;

/**
 * Created by gaol on 2017/5/15
 **/
@Data
public class Model_906003 extends ReqParam {

    @NotEmpty(message = "sessionToken不能为空")
    private String sessionToken;

    @NotEmpty(message = "typeNo不能为空")
    private String typeNo;

    @NotEmpty(message = "shareType不能为空")
    private String shareType;

}
