package websvc.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class Model_820006 extends ReqParam {

    @NotNull(message = "用户编号不能为空")
    private Long customerId;

    @NotBlank(message = "手机号不能为空")
    private String cellphone;

}
