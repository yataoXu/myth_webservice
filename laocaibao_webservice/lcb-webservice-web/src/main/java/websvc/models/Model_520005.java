package websvc.models;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import websvc.req.ReqParam;

@Setter
@Getter
public class Model_520005 extends ReqParam {

    @NotBlank(message="客户号不能为空!")
    private String customerId;
    private String pageNo = "1";
    private String pageSize = "20";
    /**
     * 交易类型 充值 "0" ，
     * 回款"1"， 提现"3"   购买"4"
     * 转让"6"    受让"7"  其他 "-1"
     * 全部 不传 或 ""
     */
    private String recordType="";

}
