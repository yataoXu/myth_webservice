package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

/**
 * 加息券查询参数
 * Created by zhou on 2016/2/23.
 */
@Setter
@Getter
public class Model_800008 extends ReqParam {

    @NotNull(message = "客户编号不能为空")
    private Long customerId;

    @NotNull(message = "页号不能为空")
    private Integer pageNo;

    @NotNull(message = "每页大小不能为空")
    private Integer pageSize;

}
