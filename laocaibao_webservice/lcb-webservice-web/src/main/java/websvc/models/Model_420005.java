package websvc.models;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * Created by silence.cheng on 2018/3/12
 **/
@Data
public class Model_420005 extends ReqParam {

    @NotNull(message = "客户ID不能为空")
    private Long cmId;

    @NotNull(message = "借款金额不能为空")
    private BigDecimal borrowAmt;//借款意向-借款金额

    @NotBlank(message = "借款用途不能为空")
    private String borrowPurpose;//借款意向-借款用途

    @NotBlank(message = "借款期限不能为空")
    private String borrowPeriod;//借款意向-借款期限

}
