package websvc.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import websvc.req.ReqParam;


/**
 * Created by 00225181 on 2016/8/16.
 */
@Getter
@Setter
public class Model_500023 extends ReqParam {
    private static final long serialVersionUID = -5085395263019922410L;
    @NotBlank(message = "标的标号不能为空")
    private String subjectNo;//标的编号
}
