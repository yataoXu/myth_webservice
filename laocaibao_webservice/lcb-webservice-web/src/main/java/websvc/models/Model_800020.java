package websvc.models;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotNull;
import websvc.req.ReqParam;

/**
 * Created by 00245337 on 2016/9/21.
 */
@Getter
@Setter
public class Model_800020 extends ReqParam {
    private static final long serialVersionUID = 1466335760388180510L;
    @NotNull(message = "customerId不能为空")
    private Long customerId;
    @NotNull(message = "validateCode不能为空")
    private String validateCode;
    @NotNull(message = "cellphone不能为空")
    private String cellphone;

}