package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
public class Model_500038 extends ReqParam {

    @NotNull(message = "转让编号不能为空")
    private String transferNo;


}
