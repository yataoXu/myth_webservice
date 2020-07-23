package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

/**
 * Created by 00232385 on 2015/10/30.
 */
@Getter
@Setter
public class Model_400013 extends ReqParam {

    @NotNull(message = "用户编号不能为空")
    private Long cmCustomerId;

    @NotNull(message = "每页大小不能为空")
    private Integer pageSize;

    @NotNull(message = "页号不能为空")
    private Integer pageNo;

}
