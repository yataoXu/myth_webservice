package websvc.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import websvc.req.ReqParam;

@Setter
@Getter
public class Model_800005 extends ReqParam {
    private static final long serialVersionUID = 4660141579652741436L;

    @NotEmpty(message = "用户编号不能为空")
    private String customerId;

}
