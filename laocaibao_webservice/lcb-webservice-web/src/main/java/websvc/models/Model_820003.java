package websvc.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class Model_820003 extends ReqParam {

    @NotNull(message = "用户编号不能为空")
    private Long customerId;

    @NotBlank(message = "小队ID不能为空")
    private String teamId;

    @NotBlank(message = "小队口号不能为空")
    private String slogan;



}
