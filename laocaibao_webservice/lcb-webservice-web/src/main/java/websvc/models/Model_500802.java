package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class Model_500802 extends ReqParam{

	private Long id;

	private Integer pageNo;

	private Integer pageSize;

	@NotNull(message = "产品id不能为空")
	private String productId;

}

