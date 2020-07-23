package websvc.models;

import lombok.Data;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

/**
 * Created by Gosling on 2016/12/7.
 */
@Data
public class Model_550007 extends ReqParam {

    @NotNull(message = "用户Id不能为空")
    private Long customerId;

}
