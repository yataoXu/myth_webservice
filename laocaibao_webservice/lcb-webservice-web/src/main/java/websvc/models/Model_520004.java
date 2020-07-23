package websvc.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import websvc.req.ReqParam;

@Setter
@Getter
public class Model_520004 extends ReqParam {

    @NotBlank(message="客户号不能为空!")
    private String customerId;
    private String isHold;//0:持有资产，1:历史资产
    private String pageNo = "1";
    private String pageSize = "20";
    private Long orderId;
}
