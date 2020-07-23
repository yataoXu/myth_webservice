package websvc.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import websvc.req.ReqParam;

/**
 * Created by 00225181 on 2016/1/6.
 */
@Setter
@Getter
public class Model_700003 extends ReqParam {

    @NotBlank(message="openId不能为空")
    private String openId;

}
