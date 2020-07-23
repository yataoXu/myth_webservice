package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


/**
 * 系统消息实体类  传进来的json参数
 * @author CJ
 *
 */
@Setter
@Getter
public class Model_600014 extends ReqParam{

	private String userId;

	private Integer pageNo;//页码
	
	private Integer pageSize;//每页多少条记录

    @NotNull(message = "消息类型不能为空")
    @Min(1)@Max(2)
    private Integer type;// 消息类型 (1:消息 2:公告)

}
