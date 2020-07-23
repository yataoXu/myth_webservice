package websvc.models;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Model_430018 extends ReqParam {

    @NotNull(message="loginId不能为空")
    private String loginId;

}
