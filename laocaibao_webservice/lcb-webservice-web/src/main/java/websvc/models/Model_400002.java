package websvc.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import websvc.req.ReqParam;

@Setter
@Getter
public class Model_400002 extends ReqParam{

	@NotBlank(message="手机号不能为空")
	private String cmCellphone;

	@NotBlank(message="密码不能为空")
	private String cmPassword;

	@NotBlank(message="设备ID不能为空")
	private String deviceId;

	private String clientId;
}

