package websvc.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import websvc.req.ReqParam;


@Setter
@Getter
public class Model_750001 extends ReqParam{
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

	@NotEmpty(message = "返利网会员标识不能为空")
	private String uid;

	@NotEmpty(message = "返利网订单跟踪信息不能为空")
	private String tc;

	@NotEmpty(message = "trackingId不能为空")
	private String trackingId;

	@NotEmpty(message = "actionTime不能为空")
	private String actionTime;

	@NotEmpty(message = "code不能为空")
	private String code;

}
