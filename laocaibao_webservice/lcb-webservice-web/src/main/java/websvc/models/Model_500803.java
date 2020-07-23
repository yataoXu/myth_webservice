package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class Model_500803 extends ReqParam{


	private Integer pageNo;

	private Integer pageSize;

	/*产品id*/
	private String productId;

	/*订单id*/
	@NotNull(message = "orderId不能为空")
	private String orderId;

}

