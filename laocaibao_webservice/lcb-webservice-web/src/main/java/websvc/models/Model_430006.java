package websvc.models;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

@Data
public class Model_430006 extends ReqParam {

    @NotNull(message = "用户ID不能为空")
    private Long customerId;

    @NotEmpty(message = "返回地址不能为空")
    private String pageUrl;
}
