package websvc.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import websvc.req.ReqParam;

@Getter
@Setter
public class Model_500028 extends ReqParam {

     @NotBlank(message="订单编号不能为空")
      private String orderNum;
}
