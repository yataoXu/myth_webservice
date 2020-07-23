package websvc.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import websvc.req.ReqParam;

@Setter
@Getter
public class Model_400012 extends ReqParam {

    @NotBlank(message = "最新消息时间不能为空")
    private String msgDate;

}
