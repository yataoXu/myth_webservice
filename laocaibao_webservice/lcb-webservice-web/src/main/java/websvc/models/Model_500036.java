package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;

import javax.validation.constraints.Pattern;

@Getter
@Setter
public class Model_500036 extends ReqParam{

	private Long id;

	private Integer pageNo;

	private Integer pageSize;

	private String topFlag;

	private Long customerId;

	private String isLoan;

	@Pattern(regexp = "^[0-9]*$",message = "非法参数")
	private String productId;

	private String upLowFlag;//判断上下架标记
}

