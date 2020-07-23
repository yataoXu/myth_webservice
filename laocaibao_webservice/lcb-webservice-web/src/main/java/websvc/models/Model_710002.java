package websvc.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;


@Setter
@Getter
public class Model_710002 extends ReqParam{
	/**
	 * 用户编号
	 */
	@NotBlank(message="手机号不能为空")
	private String cmCellphone;

	@NotNull(message = "渠道号号不能为空")
	private String channelCode;


}
