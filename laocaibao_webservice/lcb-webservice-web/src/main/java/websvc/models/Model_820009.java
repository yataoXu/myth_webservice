package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class Model_820009 extends ReqParam {

    @NotNull(message = "队长编号不能为空")
    private Long customerId;

    @NotNull(message = "申请编号不能为空")
    private Long applyId;

    @NotNull(message = "操作类型不能为空")
    private int type; //0-审核通过 1-审核拒绝
}
