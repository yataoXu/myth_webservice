package websvc.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class Model_400055 extends ReqParam{

	@NotNull(message="手机号不能为空")
	private String cmCellphone;

	@NotEmpty(message="校验码不能为空")
	@Length(max=40, message="校验码长度不能超过40")
	private String sign;
	
}
