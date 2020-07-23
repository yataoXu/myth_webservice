package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class Model_400010 extends ReqParam {

    @NotNull(message = "用户编号不能为空")
    private Long accountNo;

    private Integer pageNo;

    private Integer pageSize;


}
