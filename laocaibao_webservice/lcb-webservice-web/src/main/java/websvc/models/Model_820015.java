package websvc.models;

import lombok.Data;
import websvc.req.ReqParam;

@Data
public class Model_820015 extends ReqParam {

    private String searchStr;

    private int pageNo = 1;

    private int pageSize = 10;

}
