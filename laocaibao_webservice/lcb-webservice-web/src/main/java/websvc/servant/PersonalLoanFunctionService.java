package websvc.servant;

import com.zdmoney.common.Result;
import websvc.req.ReqMain;
import websvc.servant.base.FunctionService;

/**
 * Created by user on 2016/10/21.
 */
public interface PersonalLoanFunctionService extends FunctionService {

    /**
     * 借款首页初始化
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result myBorrows(ReqMain reqMain) throws Exception;

    /**
     * 借款详情
     * @param reqMain
     * @return
     */
    Result queryMyBorrowDetail(ReqMain reqMain);

    /**
     * 个贷-还款详情
     * @param reqMain
     * @return
     */
    Result queryRepayDetail(ReqMain reqMain) throws Exception;

    /**
     * 个贷-签约
     * @param reqMain
     * @return
     */
    Result signBorrow(ReqMain reqMain) throws Exception;


    /**
     * 个贷-查询协议
     * @param reqMain
     * @return
     */
    Result queryBorrowAgreement(ReqMain reqMain) throws Exception;

    /**
     * 个贷-委托协议
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result queryAuthAgreement(ReqMain reqMain) throws Exception;


    /**
     * 个贷-查询模板协议
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result queryAgreementsTemplete(ReqMain reqMain) throws Exception;


    /**
     * 个贷-查询借款协议
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result queryInvestBorrowAgreement(ReqMain reqMain) throws Exception;

    /**
     * 查询理财计划服务协议
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result queryFinPlanAgreement(ReqMain reqMain) throws Exception;

    /**
     * 智投宝续投服务协议
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result queryFinPlanReInvest(ReqMain reqMain) throws Exception;

}
