package websvc.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class Model_410003 extends ReqParam{


	@NotBlank(message="手机号不能为空")
	private String cmCellphone;
		
	@NotBlank(message="交易密码不能为空")
	private String tradePassword;

	@NotBlank(message="身份证号不能为空")
	private String idCard;

	@NotBlank(message="验证码不能为空")
	private String validateCode;

}
