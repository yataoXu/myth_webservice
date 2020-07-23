package websvc.models;

import lombok.Data;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

/**
 * Created by user on 2016/10/24.
 */
@Data
public class Model_540002 extends ReqParam {

    @NotNull(message="用户ID不能为空")
    private Long customerId;

    @NotNull(message="借款标的编号不能为空")
    private String subjectNo;

}
