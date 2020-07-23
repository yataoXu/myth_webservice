package websvc.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import websvc.req.ReqParam;

import java.math.BigDecimal;

@Setter
@Getter
public class Model_530003 extends ReqParam {

    @NotBlank(message="划扣金额不能为空")
    private BigDecimal payCash;
    @NotBlank(message="银行代码不能为空")
    private String bankCode;
    @NotBlank(message="客户号不能为空!")
    private Long orderId;
    @NotBlank(message="银行卡不能为空")
    private String bankAccount;

}
