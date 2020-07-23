package websvc.models;

import lombok.Data;
import websvc.req.ReqParam;


@Data
public class Model_510003 extends ReqParam {

    /**
     * * 7: 专区产品
     */
    private String productType;

    private Long customerId;

    /**
     * 页码
     */
    private Integer pageNo = 1;

}

