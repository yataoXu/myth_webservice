package websvc.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import websvc.req.ReqParam;

@Setter
@Getter
public class Model_600004 extends ReqParam {

    @NotEmpty(message = "banner类型不能为空")
    private String type;
}
