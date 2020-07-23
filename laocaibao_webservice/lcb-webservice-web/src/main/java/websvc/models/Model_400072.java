package websvc.models;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by gaol on 2017/5/16
 **/
@Data
public class Model_400072 extends ReqParam {

    @NotEmpty(message = "sessionToken不能为空")
    private String sessionToken;

    @NotEmpty(message = "测试结果不能为空")
    private String answerResult;

}
