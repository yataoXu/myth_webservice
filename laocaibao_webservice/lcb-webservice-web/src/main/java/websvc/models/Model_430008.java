package websvc.models;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

@Data
public class Model_430008 extends ReqParam {

    @NotNull(message = "用户ID不能为空")
    private Long customerId;

    /**
     * 支付渠道：0:快捷支付；1：网银支付
     */
    @NotNull(message = "支付渠道不能为空")
    private Integer channelType;

    /**
     * 充值金额
     */
    @NotEmpty(message = "充值金额不能为空")
    private String payAmt;

    @NotEmpty(message = "返回地址不能为空")
    private String pageUrl;

}
