package websvc.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import websvc.req.ReqParam;

@Setter
@Getter
public class Model_530002 extends ReqParam{
    @NotBlank(message="客户号不能为空!")
    private Long customerId;

}
