package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

/**
 * 预约券参数
 * Created by 00245337 on 2016/9/20.
 */
@Setter
@Getter
public class Model_800018 extends ReqParam {

    @NotNull(message = "客户编号不能为空")
    private Long customerId;

    @NotNull(message = "页号不能为空")
    private Integer pageNo;

    @NotNull(message = "每页大小不能为空")
    private Integer pageSize;
}
