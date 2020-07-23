package websvc.models;

import lombok.Data;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

/**
 * Created by 00250968 on 2017/11/29
 **/
@Data
public class Model_500030 extends ReqParam {

    @NotNull(message = "用户编号不能为空")
    private Long customerId;
}
