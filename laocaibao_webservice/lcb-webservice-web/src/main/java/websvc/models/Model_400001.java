package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;


@Setter
@Getter
public class Model_400001 extends ReqParam{
	
	
	private static final long serialVersionUID = -671602774002651994L;

	@NotNull(message="操作类型不能为空")
	private Integer cvType;
	
	@NotNull(message="手机号不能为空")
	private String cvMobile;
	
}
