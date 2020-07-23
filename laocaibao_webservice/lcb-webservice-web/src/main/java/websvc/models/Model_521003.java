package websvc.models;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

@Data
public class Model_521003 extends ReqParam {

    @NotEmpty(message = "userToken不能为空")
    private String userToken;


}