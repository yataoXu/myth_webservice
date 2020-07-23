package websvc.models;

import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;
import websvc.req.ReqParam;

import javax.validation.constraints.*;

/**
 * Created by user on 2016/11/3.
 */
@Data
public class Model_550002  extends ReqParam {

    private Long id;

    @NotNull(message = "用户Id不能为空")
    private Long customerId;

    @NotBlank(message = "联系人姓名不能为空")
    private String contactName;

    @NotBlank(message = "联系方式不能为空")
    private String cellPhone;

    @NotBlank(message = "省不能为空")
    private String province;

    @NotBlank(message = "市不能为空")
    private String city;

    private String area;

    @NotBlank(message = "街道地址不能为空")
    private String street;
}
