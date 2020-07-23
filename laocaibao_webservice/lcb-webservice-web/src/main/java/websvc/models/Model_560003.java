package websvc.models;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

/**
 * Created by silence.cheng on 2019/1/7.
 */

@Data
public class Model_560003 extends ReqParam {

    @NotNull(message = "用户Id不能为空")
    private Long customerId;

}
