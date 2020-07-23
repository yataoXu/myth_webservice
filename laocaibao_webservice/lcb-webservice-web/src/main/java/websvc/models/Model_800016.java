package websvc.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import websvc.req.ReqParam;

/**
 * Created by 00225181 on 2016/4/27.
 */
@Getter
@Setter
public class Model_800016 extends ReqParam {
    private static final long serialVersionUID = 1466335760388180510L;
    @NotEmpty(message = "userToken不能为空")
    private String userToken;

}