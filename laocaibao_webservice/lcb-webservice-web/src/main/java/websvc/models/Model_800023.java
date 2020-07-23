package websvc.models;

import lombok.Data;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

@Data
public class Model_800023 extends ReqParam {

    @NotNull(message = "id不能为空")
    private Integer id;
}
