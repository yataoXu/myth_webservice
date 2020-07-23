package websvc.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import websvc.req.ReqParam;

/**
 * Created by jb sun on 2016/3/16.
 */
@Setter
@Getter
public class Model_500019 extends ReqParam {

    //电话号码
    private String mobilePhone;

    //类型 0：手机充值 1： 流量充值
    @NotBlank(message = "充值类型不能为空")
    private String type;

    //用户编号
    @NotBlank(message = "客户编号不能为空")
    private String customerId;
}
