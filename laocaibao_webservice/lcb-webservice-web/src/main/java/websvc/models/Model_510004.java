package websvc.models;

import lombok.Data;
import websvc.req.ReqParam;


@Data
public class Model_510004 extends ReqParam {

    /**
     * * 7: 专区产品
     */
    private String productType = "7";

    private Long customerId;

    private String ipStr;

    /**
     * 页码
     */
    private Integer pageNo = 1;

}

