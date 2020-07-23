package websvc.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;


/**
 * 动态借款协议DTO
 */
@Data
public class Model_500039 extends ReqParam {

    @NotNull(message = "标的编号不能为空")
    private String subjectNo;
}
