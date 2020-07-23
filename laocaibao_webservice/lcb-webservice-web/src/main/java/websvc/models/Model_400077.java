package websvc.models;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;


@Data
public class Model_400077 extends ReqParam {

    @NotNull(message = "用户编号不能为空")
    private Long customerId;
}
