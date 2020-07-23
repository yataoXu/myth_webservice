package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

/**
 * Created by 00225181 on 2015/11/11.
 */
@Setter
@Getter
public class Model_400015 extends ReqParam {

    @NotNull(message = "用户编号不能为空")
    private Long customerId;

    private Integer pageNo;

    private Integer pageSize;

}
