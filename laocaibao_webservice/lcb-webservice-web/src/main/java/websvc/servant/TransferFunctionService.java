package websvc.servant;

import com.zdmoney.common.Result;
import websvc.req.ReqMain;
import websvc.servant.base.FunctionService;

public interface TransferFunctionService  extends FunctionService{

    /**
     * 转让初始化
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result transferInit(ReqMain reqMain) throws  Exception;

    /**
     * 转让初始化v4.6
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result initTransfer(ReqMain reqMain) throws  Exception;

    /**
     * 查询转让日利率
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result queryTransferRate(ReqMain reqMain) throws Exception;

    /**
     * 查询转让日利率v4.8
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result transferRateInfo(ReqMain reqMain) throws Exception;

    /**
     * 转让
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result transfer(ReqMain reqMain) throws Exception;

    /**
     * 转让v4.6
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result transfers(ReqMain reqMain) throws Exception;

    /**
     * 转让产品详情
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result transferInfo(ReqMain reqMain) throws Exception;

    /**
     * 撤销转让
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result cancelTransfer(ReqMain reqMain) throws Exception;


    /**
     * 查询转让前协议内容
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result queryBeforeTransferProtocol(ReqMain reqMain) throws Exception;

    /**
     * 查询转让后协议内容
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result queryAfterTransferProtocol(ReqMain reqMain) throws Exception;


    /**
     *转让授权是否充足
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result transferAuth(ReqMain reqMain) throws Exception;

}
