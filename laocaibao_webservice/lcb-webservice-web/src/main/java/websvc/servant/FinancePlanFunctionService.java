package websvc.servant;

import com.zdmoney.common.Result;
import websvc.req.ReqMain;
import websvc.servant.base.FunctionService;


/**
 * 理财计划接口
 * Author: silence.cheng
 * Date: 2017-06-03 11:50
 */

public interface FinancePlanFunctionService extends FunctionService{

    /**
     * 理财计划产品详情--借款人明细列表（app）
     */
    Result creditorRightsInfo(ReqMain reqMain) throws Exception;


    /**
     * 理财计划订单产品详情--借款人明细列表（app）
     */
    Result orderCreditorRightsInfo(ReqMain reqMain) throws Exception;

    /**
     * 理财计划订单提前退出初始化
     */
    Result initExitFinPlanOrder(ReqMain reqMain) throws Exception;

    /**
     * 理财计划订单提前退出
     */
    Result exitFinPlanOrder(ReqMain reqMain) throws Exception;

}
