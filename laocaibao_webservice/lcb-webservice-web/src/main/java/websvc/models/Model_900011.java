package websvc.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class Model_900011 extends ReqParam {

    @NotBlank(message = "手机号不能为空")
    private String cvMobile;

    @NotNull(message = "操作类型不能为空")
    private Integer cvType;

    @NotNull(message = "短信类型不能为空")
    private Integer codeType;

}
