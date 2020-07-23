package websvc.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class Model_820011 extends ReqParam {

    private String seachStr;

    @NotBlank(message = "查询类型不能为空")
    private String seachType = "1";  //1 全部,2 可加入 默认查询全部

    private int pageNo = 1;

    private int pageSize = 10;

    private Long customerId;

}
