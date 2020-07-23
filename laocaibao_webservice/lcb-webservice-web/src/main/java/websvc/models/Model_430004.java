package websvc.models;


import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

@Data
public class Model_430004 extends ReqParam {

    @NotNull(message = "用户ID不能为空")
    private Long customerId;

    /**
     * 类型：1：修改；2：忘记
     */
    @NotNull(message = "类型不能为空")
    private Integer changeType = 2;

    @NotEmpty(message = "返回地址不能为空")
    private String pageUrl;
}
