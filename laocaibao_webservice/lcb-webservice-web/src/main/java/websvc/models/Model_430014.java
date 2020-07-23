package websvc.models;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

@Data
public class Model_430014 extends ReqParam {

    @NotNull(message = "用户ID不能为空")
    private Long customerId;

}
