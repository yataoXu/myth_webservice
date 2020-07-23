package websvc.models;

import lombok.Data;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Model_430017 extends ReqParam {

    @NotNull(message="用户编号不能为空")
    private Long customerId;

    @NotNull(message="签约时间不能为空")
    private Date signDate;

    @NotNull(message="借款金额不能为空")
    private BigDecimal borrowAmt;
}
