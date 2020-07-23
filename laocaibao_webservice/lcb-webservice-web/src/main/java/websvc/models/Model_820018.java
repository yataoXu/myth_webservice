package websvc.models;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

/**
 * Created by gaol on 2017/01/09.
 */
@Data
public class Model_820018 extends ReqParam {

    /**
     * 客户编号
     */
    @NotEmpty(message = "sessionToken不能为空")
    private String  sessionToken;

}
