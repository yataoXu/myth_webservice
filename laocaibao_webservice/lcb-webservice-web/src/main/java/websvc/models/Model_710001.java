package websvc.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import websvc.req.ReqParam;


@Setter
@Getter
public class Model_710001 extends ReqParam{
	/**
	 * 手机号
	 */
	@NotEmpty(message = "手机号不能为空")
	private String cmCellphone;
	
	/**
	 * 密码
	 */
	@NotEmpty(message = "密码不能为空")
	private String cmPassword;
	
	/**
	 * 验证码
	 */
	@NotEmpty(message = "验证码不能为空")
	private String validateCode;

	/**
	 * 渠道号
	 */
	@NotEmpty(message = "渠道号不能为空")
	private String channelCode;

	private String ip;

	/**
	 * 邀请码
	 */
	private  String  inviteCode;

}
