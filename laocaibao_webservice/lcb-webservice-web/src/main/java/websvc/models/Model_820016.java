package websvc.models;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import websvc.req.ReqParam;

/**
 * Created by Gosling on 2016/12/20.
 */
@Data
public class Model_820016 extends ReqParam {

    @NotBlank(message = "用户编号不能为空")
    private String cmNumber;

    @NotBlank(message = "开始时间不能为空")
    private String startDate;

    @NotBlank(message = "结束时间不能为空")
    private String endDate;

    /**
     * 接口标识  1:带有投资总额 0:没有  2:排除同性
     */
    private Integer type;
}
