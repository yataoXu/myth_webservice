package websvc.models;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class Model_521000 extends ReqParam{

	@NotNull(message="客户号不能为空!")
	private Long customerId;

	@NotNull(message = "产品ID不能为空")
	private Long productId;

	@NotNull(message = "订单金额不能为空")
	private BigDecimal orderAmt;

    //系统下单订单号
	private String orderId ="" ;

	/*使用积分金额*/
	private String integralAmt;

	/*使用红包消费的红包id*/
	private String redId;

	/*使用加息券的编号*/
	private String voucherId;

	/*支付密码*/
	private String payPassword;
}

