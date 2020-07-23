package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;


@Setter
@Getter
public class Model_900000 extends ReqParam{
	
	
	private static final long serialVersionUID = -671602774002651994L;

	private String traceId = "";

	private String systemNo = "";

	private String featureNo = "";

	private String notifyNo = "";

	private String notifyContent = "";
	
}
