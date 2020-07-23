package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

/**
 * Created by 00225181 on 2016/4/13.
 */
@Getter
@Setter
public class Model_800015 extends ReqParam{
    private static final long serialVersionUID = -7935225147670918001L;

    @NotNull(message = "咨询id不能为空")
    private Long id;
}
