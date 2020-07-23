package websvc.models;

import lombok.Data;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

/**
 * Created by user on 2016/11/11.
 */
@Data
public class Model_550005 extends ReqParam {

    @NotNull(message = "客户ID不能为空")
    private Long customerId;

}
