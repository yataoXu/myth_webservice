package websvc.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import websvc.req.ReqParam;

@Getter
@Setter
public class Model_400009 extends ReqParam{

	@NotEmpty(message = "用户编号不能为空")
	private String sessionToken;
}
