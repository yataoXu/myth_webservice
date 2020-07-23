package websvc.servant;

import com.zdmoney.common.Result;
import websvc.req.ReqMain;
import websvc.servant.base.FunctionService;

/**
 * Created by user on 2016/11/10.
 */
public interface CustomerAddressFunctionService extends FunctionService {

    /**
     * 获取用户地址列表
     * @param reqMain
     * @return
     */
    Result queryCustomerAddressList(ReqMain reqMain);

    /**
     * 保存或修改收获地址
     * @param reqMain
     * @return
     */
    Result saveOrUpdateAddress(ReqMain reqMain);

    /**
     * 删除收获地址
     * @param reqMain
     * @return
     */
    Result deleteCustomerAddressById(ReqMain reqMain);

    /**
     * 获取各个银行限额数据
     * @return
     */
    Result getBankQuota(ReqMain reqMain);

    /**
     * 安全退出
     * @param reqMain
     * @return
     */
    Result unBind(ReqMain reqMain);

    /**
     * 查询借款人信息
     * @param reqMain
     * @return
     */
    Result queryBorrowerInfo(ReqMain reqMain) throws Exception;
}
