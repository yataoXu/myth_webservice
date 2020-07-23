package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

/**
 * Created by 00225181 on 2016/4/27.
 */
@Getter
@Setter
public class Model_820014 extends ReqParam {
    private static final long serialVersionUID = 1740686536220204086L;
    @NotNull(message = "用户编号不能为空")
    private Long customerId;
}
