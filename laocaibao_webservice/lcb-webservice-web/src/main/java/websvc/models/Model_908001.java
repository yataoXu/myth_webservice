package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

/**
 * Created on 2018/12/05
 */
@Getter
@Setter
public class Model_908001 extends ReqParam {

    @NotNull(message = "用户id不能为空")
    private Long cmCustomerId;

    @NotNull(message = "每页大小不能为空")
    private Integer pageSize;

    @NotNull(message = "页号不能为空")
    private Integer pageNo;

}
