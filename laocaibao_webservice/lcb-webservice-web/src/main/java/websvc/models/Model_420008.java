package websvc.models;

import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;


/**
 * Created by 46186 on 2018/6/22.
 */
@Data
public class Model_420008 extends ReqParam {
    @NotNull(message = "邮箱不能为空")
    private String email;

    @NotEmpty(message = "校验码不能为空")
    @Length(max = 10, message = "校验码长度不能超过10")
    private String sign;
}
