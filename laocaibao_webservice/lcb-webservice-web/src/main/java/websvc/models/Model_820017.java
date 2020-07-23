package websvc.models;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import websvc.req.ReqParam;

/**
 * Created by user on 2016/12/29.
 */
@Data
public class Model_820017 extends ReqParam {

    /**
     * 客户编号
     */
    @NotEmpty(message = "sessionToken不能为空")
    private String  sessionToken;


}
