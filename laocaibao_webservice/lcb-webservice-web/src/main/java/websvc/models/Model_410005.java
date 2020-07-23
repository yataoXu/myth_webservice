package websvc.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import websvc.req.ReqParam;

/**
 * @author hanxn
 * @date 2019/2/28
 */
@Setter
@Getter
public class Model_410005 extends ReqParam {

    /**
     * 手机号
     */
    @NotEmpty(message = "手机号不能为空")
    private String cmCellphone;

    /**
     * 身份证号
     */
    @NotEmpty(message = "身份证号不能为空")
    private String cmIdnum;

    /**
     * 真实姓名
     */
    @NotEmpty(message = "真实姓名不能为空")
    private String realName;

}
