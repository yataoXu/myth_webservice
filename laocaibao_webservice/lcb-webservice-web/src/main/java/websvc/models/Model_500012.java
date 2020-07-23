package websvc.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class Model_500012 {

	@NotBlank(message="客户ID不能为空")
	private String customerId;
	
	private Integer pageNo;//页码
	
	private Integer pageSize;//每页记录数

}
