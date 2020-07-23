package websvc.models;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

/**
 * Model_431009
 *
 * @Author: wein
 * @Description: 校验用户能否提现
 * @Date: Created in 2018/8/20 16:56
 * @Mail: wein@zendaimoney.com
 */
@Data
public class Model_431009 extends ReqParam {

    @NotNull(message = "用户ID不能为空")
    private Long customerId;

    /**
     * 充值金额
     */
    @NotEmpty(message = "充值金额不能为空")
    private String payAmt;

    private String pageUrl;

    /**
     * 1: 普通提现
     * 2: 快速提现
     */
    @NotNull(message = "提现类型不能为空")
    private Integer type;
}
