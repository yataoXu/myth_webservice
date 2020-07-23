package websvc.models;

import lombok.Data;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

/**
 * Created by user on 2016/11/15.
 */
@Data
public class Model_550006 extends ReqParam {

    @NotNull(message = "产品编号不能为空")
    private Long productId;

    private int status=0;//0:普通产品 1：理财计划产品  2 理财计划订单产品

    private String id;

}
