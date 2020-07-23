package websvc.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

/**
 * Created by 00245337 on 2016/8/23.
 */

@Setter
@Getter
public class Model_600015 extends ReqParam {

    private Integer msgId;
    @NotEmpty(message = "用户id不能为空")
    private String userId;
    private Integer isAllRead;
    @NotNull(message = "消息类型不能为空")
    private Integer msgType;
}