package websvc.models;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotNull;
import websvc.req.ReqParam;

/**
 * Created by 00245337 on 2016/9/21.
 */
@Getter
@Setter
public class Model_800019 extends ReqParam {
    private static final long serialVersionUID = 1466335760388180510L;
    @NotNull(message = "customerId不能为空")
    private Long customerId;

    private String validateCode;

    @NotNull(message = "idNumber不能为空")
    private String idNumber;

    /** 校验方式: 1-手机验证码 2-交易密码 */
    private String validateType;

    private String payPassword;
}
