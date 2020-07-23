package websvc.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import websvc.req.ReqParam;

@Setter
@Getter
public class Model_520077 extends ReqParam {

    private static final long serialVersionUID = -7888954229619650336L;

    /*客户号*/
    @NotBlank(message="客户号不能为空!")
    private String customerId;

    /*充值金额*/
    @NotBlank(message="充值金额不能为空!")
    private String amount;

    /*银行名称*/
    private String bankName;

    /*银行代码*/
    @NotBlank(message="银行代码不能为空!")
    private String bankCode;

    /*银行卡号*/
    @NotBlank(message="银行卡号不能为空!")
    private String bankCard;

}
