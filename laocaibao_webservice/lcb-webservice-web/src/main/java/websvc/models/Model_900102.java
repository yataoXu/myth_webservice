package websvc.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import websvc.req.ReqParam;

@Getter
@Setter
public class Model_900102 extends ReqParam {

    @NotBlank(message = "sessionToken不能为空")
    private String sessionToken;

    @NotBlank(message = "guessRecord不能为空")
    private String guessRecord;

}

