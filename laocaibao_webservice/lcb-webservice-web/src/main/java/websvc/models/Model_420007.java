package websvc.models;

import lombok.Data;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

/**
 * Created by qinzhuang on 2018/6/13.
 */
@Data
public class Model_420007 extends ReqParam {
    @NotNull(message = "用户编号不能为空")
    private String customerId;

    @NotNull(message = "邮箱不能为空")
    private String email;
}
