package websvc.servant;

import com.zdmoney.common.Result;
import com.zdmoney.webservice.api.dto.fl.FlwReqDto;
import websvc.servant.base.FunctionService;

/**
 * @ Author : Evan.
 * @ Description :
 * @ Date : Crreate in 2018/10/26 15:42
 * @Mail : xuyt@zendaimoney.com
 */
public interface FanliService extends FunctionService {

    Result getOrderList(FlwReqDto flwReqDto) throws Exception;
}
