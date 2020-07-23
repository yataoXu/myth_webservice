package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

/**
 * Created by 00231247 on 2016/3/16.
 */
@Setter
@Getter
public class Model_800012 extends ReqParam {

    @NotNull(message="资讯编号不能为空")
    private Long sid;

}
