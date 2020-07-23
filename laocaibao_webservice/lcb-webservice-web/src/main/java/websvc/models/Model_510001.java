package websvc.models;

import lombok.Data;
import websvc.req.ReqParam;

@Data
public class Model_510001 extends ReqParam{

	/**
	 * 产品类型
	 *
	 * 0: 所有
	 * 1: 定期
	 * 2: 个贷
	 * 3: 理财计划
	 */
	private String productType = "0";

	private Long customerId;

}

