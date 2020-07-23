package websvc.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class Model_400031 extends ReqParam {

    @NotNull(message = "customerId不能为空")
    private Long customerId;

}
