package websvc.servant;

import com.zdmoney.common.Result;
import websvc.req.ReqMain;
import websvc.servant.base.FunctionService;

/**
 * Created by user on 2018/1/18.
 */
public interface SundryFunctionService extends FunctionService {
    Result queryOperationsStatisticsList(ReqMain reqMain) throws Exception;

    Result queryOperationsStatisticsDetail(ReqMain reqMain) throws Exception;

    Result queryArchiveList(ReqMain reqMain) throws Exception;
}
