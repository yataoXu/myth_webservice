package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

/**
 * Created by zhou on 2016/2/26.
 */
@Setter
@Getter
public class Model_800004 extends ReqParam {


    @NotNull(message = "用户编号不能为空")
    private Long customerId;

    private Integer pageNo;

    private Integer pageSize;


}
