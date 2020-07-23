package websvc.models;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
public class Model_521001 extends ReqParam{

	@NotNull(message="客户号不能为空!")
	private Long customerId;

	@NotNull(message = "产品ID不能为空")
	private Long productId;


}

