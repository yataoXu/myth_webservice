package websvc.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import websvc.req.ReqParam;

@Setter
@Getter
public class Model_520044 extends ReqParam {

    @NotBlank(message="客户号不能为空!")
    private String customerId;
    private String isHold;//0:持有资产，1:历史资产
    private String pageNo = "1";
    private String pageSize = "20";
    private Long orderId;
    private String orderType="0";//订单类型 0：全部，1：智投计划，2：散标，3:定期，4:转让
}
