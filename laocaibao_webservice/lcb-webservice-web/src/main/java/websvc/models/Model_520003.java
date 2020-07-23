package websvc.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import websvc.req.ReqMain;
import websvc.req.ReqParam;

@Setter
@Getter
public class Model_520003 extends ReqParam {
    @NotBlank(message="客户号不能为空!")
    private String customerId;
}
