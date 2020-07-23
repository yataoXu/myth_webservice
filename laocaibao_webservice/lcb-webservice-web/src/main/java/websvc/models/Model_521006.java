package websvc.models;

import lombok.Data;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;


@Data
public class Model_521006 extends ReqParam {

    @NotNull(message="客户号不能为空!")
    private  Long customerId;
}

