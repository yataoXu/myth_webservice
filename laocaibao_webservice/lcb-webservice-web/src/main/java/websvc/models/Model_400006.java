package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class Model_400006 extends ReqParam{
	@NotNull(message="客户编号不能为空")
	private Long cmCustomerId;

	@NotNull(message="身份证不能为空")
	private String cmIdnum;
	
	@NotNull(message="姓名不能为空")
	private String cmRealName;

}
