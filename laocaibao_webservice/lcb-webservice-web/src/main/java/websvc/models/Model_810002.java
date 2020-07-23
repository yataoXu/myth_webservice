package websvc.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import websvc.req.ReqParam;

/**
 * Created by 00225181 on 2016/2/27.
 */
@Setter
@Getter
public class Model_810002 extends ReqParam {

    @NotBlank(message = "sessionToken不能为空")
    private String cmToken;

    @NotBlank(message = "任务编号不能为空")
    private String flowId;
}
