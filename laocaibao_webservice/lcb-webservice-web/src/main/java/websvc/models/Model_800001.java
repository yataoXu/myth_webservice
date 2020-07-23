package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

/**
 * Created by zhou on 2015/2/26.
 * 商城首页
 */
@Setter
@Getter
public class Model_800001 extends ReqParam {

    @NotNull(message = "用户编号不能为空")
    private Long customerId;

}
