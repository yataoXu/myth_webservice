package websvc.servant;

import com.zdmoney.common.Result;
import websvc.req.ReqMain;
import websvc.servant.base.FunctionService;


public interface PaymentPlanFunctionService extends FunctionService {


    /*
     * 500028 回款计划列表
     *
     */
    Result getPaymentPlanList(ReqMain reqMain) throws Exception;

    /**
     * 500029
     * 获取资产日历信息
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result getAssetCalendar(ReqMain reqMain) throws Exception;

    /**
     * 500030
     * 资产详情
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result getAssetDetailInfo(ReqMain reqMain) throws Exception;
}
