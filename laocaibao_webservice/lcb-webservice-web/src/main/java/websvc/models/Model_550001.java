package websvc.models;

import lombok.Data;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

/**
 * Created by user on 2016/11/3.
 */
@Data
public class Model_550001 extends ReqParam {

    @NotNull(message = "用户Id不能为空")
    private Long customerId;

}
