package websvc.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import websvc.req.ReqParam;


/**
 * 意见反馈实体类  对应页面输入框中的name
 *
 * @author CJ
 */
@Setter
@Getter
public class Model_600001 extends ReqParam {

    @NotBlank(message = "反馈内容不能为空")
    @Length(max = 500, message = "反馈内容长度不能超过500")
    private String content;

    private String contactWay;

    private Long customerId;

    private String feedbackType;//1,账号 2,绑卡/充值/提现 ,3 活动 4 收银台 5,产品 ,6 功能故障 7 福利 8 其他建议
}
