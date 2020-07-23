package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;



/**
 * 系统消息实体类  传进来的json参数
 * @author CJ
 *
 */
@Setter
@Getter
public class Model_600002 extends ReqParam{

	private Integer pageNo;//页码
	
	private Integer pageSize;//每页多少条记录

}
