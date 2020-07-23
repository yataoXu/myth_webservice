package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqMain;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

/**
 * Created by 00225181 on 2016/4/13.
 */
@Getter
@Setter
public class Model_400021 extends ReqParam {
    private static final long serialVersionUID = -3423643270265139498L;
    @NotNull(message =  "用户编号不能为空")
    private Long customerId;
}
