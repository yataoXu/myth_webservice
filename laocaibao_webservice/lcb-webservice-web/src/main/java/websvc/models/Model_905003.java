package websvc.models;/**
 * Created by pc05 on 2017/3/31.
 */

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotEmpty;
import websvc.req.ReqParam;

/**
 * 描述 :
 *
 * @author : huangcy
 * @create : 2017-03-31 11:45
 * @email : huangcy01@zendaimoney.com
 **/
@Setter
@Getter
public class Model_905003 extends ReqParam {

    /**
     * 客户编号
     */
    @NotEmpty(message = "customerId不能为空")
    private String  customerId;
}
