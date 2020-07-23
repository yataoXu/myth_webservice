package websvc.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class Model_500009 extends ReqParam{

    @NotBlank(message = "查询参数不能为空")
	private String flowNum;
}
