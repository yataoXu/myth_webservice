package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class Model_530001 extends ReqParam {
    @NotNull(message = "银行编号不能为空")
    private String bankCode;
    @NotNull(message = "省份编号不能为空")
    private String provinceCode;
    @NotNull(message = "城市编号不能为空")
    private String cityCode;
    @NotNull(message = "支付渠道不能为空")
    private String payChannel;

}
