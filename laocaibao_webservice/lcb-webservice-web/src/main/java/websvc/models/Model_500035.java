package websvc.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;


@Setter
@Getter
public class Model_500035 extends ReqParam{
	
	
	private static final long serialVersionUID = -671602774002651994L;

	@NotNull(message = "订单号不能为空")
	private String orderId;

	@NotBlank(message="客户号不能为空!")
	private String customerId;

}
