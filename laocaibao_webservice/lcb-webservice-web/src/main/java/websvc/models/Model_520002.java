package websvc.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import websvc.req.ReqParam;

@Setter
@Getter
public class Model_520002 extends ReqParam {
    private static final long serialVersionUID = -3535479724980891297L;

    @NotBlank(message="客户号不能为空!")
    private String customerId;

    @NotBlank(message="订单号编不能为空!")
    private String orderId;
}
