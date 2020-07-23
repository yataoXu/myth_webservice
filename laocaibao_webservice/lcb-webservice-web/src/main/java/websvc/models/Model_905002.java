package websvc.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import websvc.req.ReqParam;


@Setter
@Getter
public class Model_905002 extends ReqParam{

	/**
	 * 客户编号
	 */
	@NotEmpty(message = "sessionToken不能为空")
	private String  sessionToken;


}
