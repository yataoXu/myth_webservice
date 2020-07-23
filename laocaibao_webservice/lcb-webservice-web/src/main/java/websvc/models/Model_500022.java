package websvc.models;

import websvc.req.ReqParam;

/**
 * Created by 00225181 on 2016/4/6.
 */
public class Model_500022 extends ReqParam {
    private static final long serialVersionUID = 2318334615984227893L;

    private Long customerId;

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }
}
