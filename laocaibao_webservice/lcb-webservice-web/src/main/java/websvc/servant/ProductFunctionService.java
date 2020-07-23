package websvc.servant;

import com.zdmoney.common.Result;
import websvc.req.ReqMain;
import websvc.servant.base.FunctionService;


/**
 * 产品模块
 */
public interface ProductFunctionService extends FunctionService {

    /**
     * 510001 查询产品列表
     *
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result queryProductList(ReqMain reqMain) throws Exception;

    /**
     * 510002 产品列表 - 更多
     *
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result queryProductListByType(ReqMain reqMain) throws Exception;

    /**
     * 710003 查询渠道产品
     *
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result getChannelProduct(ReqMain reqMain) throws Exception;

    /**
     * 500016 查询产品详情
     *
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result productDetail(ReqMain reqMain) throws Exception;

    /**
     * 501016 查询产品详情(PC)
     *
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result productDetailPC(ReqMain reqMain) throws Exception;

    /**
     * 500036 转让产品列表
     *
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result transferList(ReqMain reqMain) throws Exception;

}
