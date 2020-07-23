package websvc.servant;

import com.zdmoney.common.Result;
import websvc.req.ReqMain;
import websvc.servant.base.FunctionService;

/**
 * 富友存管接口
 *
 * Author: gosling
 * Date: 2018年8月15日 16:48:07
 */
public interface FuiouFunctionService extends FunctionService {

    /**
     * 开户
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result openAccount(ReqMain reqMain) throws Exception;

    /**
     * 开户返回参数encode
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result openAccountWithParamEncode(ReqMain reqMain) throws Exception;

    /**
     * 绑卡
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result bankCardBind(ReqMain reqMain) throws Exception;

    /**
     * 解绑卡
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result bankCardUnBind(ReqMain reqMain) throws  Exception;

    /**
     * 更换手机号
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result mobileChange(ReqMain reqMain)throws Exception;

    /**
     * 密码重置
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result passwordModify(ReqMain reqMain) throws Exception;

    /**
     * 充值
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result recharge(ReqMain reqMain) throws Exception;

    /**
     * 用户授权
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result userGrant(ReqMain reqMain) throws Exception;

    /**
     * 用户授权返回参数encode
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result userGrantWithParamEncode(ReqMain reqMain) throws Exception;

    /**
     * 短信通知配置
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result smsNofity(ReqMain reqMain) throws Exception;

    /**
     * 提现
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result withdraw(ReqMain reqMain) throws Exception;

    /**
     * 提现返回url参数encode
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result withdrawWithParamEncode(ReqMain reqMain) throws Exception;


    /**
     * 校验用户能否提现
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result checkWithdraw(ReqMain reqMain) throws Exception;

    /**
     * 查询用户 实名, 开户, 绑卡, 授权状态
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result userStatus(ReqMain reqMain) throws Exception;

    /**
     * 查询用户授权额度是否充足(下单)
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result userAuthJudge(ReqMain reqMain) throws Exception;

    /**
     * 线下转账充值
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result offlineRecharge(ReqMain reqMain) throws Exception;

    /**
     * 判断用户授权成功-开户场景
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result webAuthInfo(ReqMain reqMain) throws Exception;

    /**
     * 获取用户银行卡信息
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result bankCardInfo(ReqMain reqMain) throws Exception;

    /**
     * 签约时判断授权是否充足
     *
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result contractGrant(ReqMain reqMain) throws Exception;

    /**
     * 用户是否成功设置交易密码
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result setHrPwd(ReqMain reqMain) throws Exception;

    /**
     * 是否超出限额
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result isExceedLimit(ReqMain reqMain) throws Exception;

    /**
     * 提现授权额度是否充足
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result withdrawAuth(ReqMain reqMain) throws Exception;
}
