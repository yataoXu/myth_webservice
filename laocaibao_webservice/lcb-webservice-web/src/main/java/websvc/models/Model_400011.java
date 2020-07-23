package websvc.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import websvc.req.ReqParam;

@Setter
@Getter
public class Model_400011 extends ReqParam {

    @NotBlank(message = "userToken不能为空")
    private String userToken;

}
