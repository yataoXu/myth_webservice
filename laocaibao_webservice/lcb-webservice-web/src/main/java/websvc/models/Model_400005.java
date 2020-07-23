package websvc.models;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import websvc.req.ReqParam;

@Setter
@Getter
public class Model_400005 extends ReqParam{

	@NotNull(message="手机号不能为空")
	private String cmCellphone;
	
	
	@NotNull(message="密码不能为空")
	private String cmPassword;

	@NotEmpty(message="校验码不能为空")
	@Length(max=40, message="校验码长度不能超过40")
	private String sign;
	
}
