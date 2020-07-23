package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

@Setter
@Getter
public class Model_700001 extends ReqParam {

//    @NotNull(message = "用户编号不能为空")
    private Long accountNo;
    public void setCustomerId(Long customerId)
    {
        this.setAccountNo(customerId);
    }

    public Long getCustomerId()
    {
        return this.getAccountNo();
    }

    private Integer pageNo;

    private Integer pageSize;

    private String sortParam;

}
