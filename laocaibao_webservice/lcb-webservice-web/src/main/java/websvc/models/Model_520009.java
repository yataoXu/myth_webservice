package websvc.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import websvc.req.ReqParam;

@Setter
@Getter
public class Model_520009 extends ReqParam {

    private static final long serialVersionUID = 2747420109870720224L;

    @NotEmpty(message = "客户编号不能为空！")
    private String customerId;
    @NotEmpty(message = "提现金额不能为空！")
    private String withDrawAmount;
    private String subBankCode;
    private String subBankName;
    @NotEmpty(message = "提现密码不能为空！")
    private String payPassword;

}
