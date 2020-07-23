package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

/**
 * Created by zhou on 2016/2/27.
 */
@Setter
@Getter
public class Model_800003 extends ReqParam {

    @NotNull(message = "用户编号不能为空")
    private Long customerId;

    @NotNull(message = "捞财币数量不能为空")
    private Long coin;

    @NotNull(message = "描述信息不能为空")
    private String tips;

}
