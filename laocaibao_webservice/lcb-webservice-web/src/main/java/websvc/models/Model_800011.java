package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;

@Setter
@Getter
public class Model_800011 extends ReqParam {

    private int pageNo = 1;

    private int pageSize = 10;

}
