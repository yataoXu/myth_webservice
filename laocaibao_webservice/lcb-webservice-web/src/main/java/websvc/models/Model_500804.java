package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class Model_500804 extends ReqParam{

	@NotNull(message = "订单号不能为空")
	private Long orderId;

}

