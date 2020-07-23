package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;

/**
 * Created by 00225181 on 2016/6/5.
 */
@Getter
@Setter
public class Model_900103 extends ReqParam{
    private static final long serialVersionUID = 1554469728946910692L;
    private String sessionToken;
}
