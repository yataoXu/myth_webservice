package websvc.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import websvc.req.ReqParam;

/**
 * Created by jb sun on 2016/3/16.
 */
@Setter
@Getter
public class Model_500020 extends ReqParam {

    @NotBlank(message = "用户编号不能为空")
    private String customerId;
    @NotBlank(message = "产品编号不能为空")
    private String productNo;
    @NotBlank(message = "手机号不能为空")
    private String mobilePhone;
}
