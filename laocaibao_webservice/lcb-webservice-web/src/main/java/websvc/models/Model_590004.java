package websvc.models;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import websvc.req.ReqParam;

/**
 * Created by silence.cheng on 2019/1/7.
 */

@Data
public class Model_590004 extends ReqParam {

    @NotEmpty(message = "subjectNo不能为空")
    private String  subjectNo;

    @NotEmpty(message = "cellPhone不能为空")
    private String  cellPhone;

    @NotEmpty(message = "idNo不能为空")
    private String  idNo;
}
