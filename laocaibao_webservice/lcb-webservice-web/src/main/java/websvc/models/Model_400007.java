package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class Model_400007 extends ReqParam{

	@NotNull(message="邀请码不能为空")
	private String cmIntroduceCode;
	
}
