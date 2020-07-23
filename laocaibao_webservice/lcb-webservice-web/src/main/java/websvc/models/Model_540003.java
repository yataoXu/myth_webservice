package websvc.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

/**
 * 个贷-还款详情
 */
@Getter
@Setter
public class Model_540003 extends ReqParam {

    @NotNull(message = "用户编号不能为空")
    private Long customerId;

    @NotBlank(message = "标的编号不能为空")
    private String subjectNo;

}
