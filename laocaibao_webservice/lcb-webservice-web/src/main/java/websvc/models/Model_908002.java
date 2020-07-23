package websvc.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

/**
 * Created on 2018/12/05
 */
@Data
public class Model_908002 extends ReqParam {

    @NotBlank(message = "现金券编号不能为空")
    private String cashNo;

    @NotNull(message="客户号不能为空!")
    private Long customerId;

    @NotBlank(message = "现金券来源不能为空")
    private String publishSource;

}
