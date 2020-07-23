package websvc.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import websvc.req.ReqParam;

@Getter
@Setter
public class Model_500050 extends ReqParam {

    @NotBlank(message = "产品类型不能为空")
    private String productType; //0-优选  1智选  0 转让

    @NotBlank(message = "协议编号")
    private String agreementNo;

}
