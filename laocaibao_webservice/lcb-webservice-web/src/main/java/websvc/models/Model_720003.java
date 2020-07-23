package websvc.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class Model_720003 extends ReqParam {

    @NotBlank(message = "用户编号不能为空")
    private String sessionToken;

    @NotNull(message = "购买期限不能为空")
    private Integer investPeriod;

}
