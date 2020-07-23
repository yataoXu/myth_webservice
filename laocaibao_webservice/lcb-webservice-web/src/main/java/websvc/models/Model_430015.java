package websvc.models;

import lombok.Data;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class Model_430015 extends ReqParam {

    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /**
     * 响应码，0000为成功
     */
    private String code;

    /**
     * 错误原因
     */
    private String msg;

    /**
     * 授权状态
     */
    private String authStatus;

}
