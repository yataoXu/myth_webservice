package websvc.servant;

import com.zdmoney.common.Result;
import websvc.req.ReqMain;
import websvc.servant.base.FunctionService;

/**
 * Created by 00225181 on 2016/3/30.
 */
public interface WechatFunctionService extends FunctionService {
    /*
     * 400018 微信注册
     *
     */
    Result wechatRegist(ReqMain reqMain)throws Exception;


    /*
     * 700003 检查微信是否绑定
     *
     */
    Result checkWxBind(ReqMain reqMain) throws Exception;

    /*
     * 700004 微信绑定
     *
     */
    Result wxBind(ReqMain reqMain) throws Exception;

    /**
     * 700008 商户用户注册
     */
    Result merchantBind(ReqMain reqMain) throws Exception;


    /**
     * 710002 判断用户注册渠道来源
     */
    Result checkUserChannel(ReqMain reqMain) throws Exception;


    /*
     * 800014 判断微信是否绑定app
     *
     */
    Result checkLoginOpenIdBind(ReqMain reqMain) throws Exception;
}
