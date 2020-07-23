package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;

/**
 * Created by 00225181 on 2016/1/5.
 */
@Setter
@Getter
public class Model_500015 extends ReqParam {

    private Integer pageNo;

    private Integer pageSize;

}
