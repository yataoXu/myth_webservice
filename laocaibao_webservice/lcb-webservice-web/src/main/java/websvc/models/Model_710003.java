package websvc.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import websvc.req.ReqParam;


@Setter
@Getter
public class Model_710003 extends ReqParam{
	/**
	 * 渠道码
	 */
	@NotEmpty(message = "渠道码不能为空")
	private String channelCode;


}
