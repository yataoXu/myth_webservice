package websvc.servant;

import com.zdmoney.common.Result;
import websvc.req.ReqMain;
import websvc.servant.base.FunctionService;

/**
 * 用户模块
 * UserFunctionService
 */
public interface UserFunctionService extends FunctionService {

    /**
     * 400031 根据用户ID查询用户信息（前端，收银台调用）
     * @param reqMain 请求参数
     * @return
     * @throws Exception
     */
    Result queryCustomerById(ReqMain reqMain) throws Exception;


    /**
     * 400002 用户登录（登录密码）
     * @param reqMain 请求参数
     * @return
     * @throws Exception
     */
    Result login(ReqMain reqMain) throws Exception;

    /**
     * 400022 用户登录（手机校验码）
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result validateCodeLogin(ReqMain reqMain) throws Exception;

    /**
     * 400008 用户登出（安全退出）
     * @param reqMain 请求参数
     * @return
     * @throws Exception
     */
    Result logout(ReqMain reqMain) throws Exception;


    /**
     * 400003 用户注册
     * @param reqMain 请求参数
     * @return
     * @throws Exception
     */
    Result register(ReqMain reqMain) throws Exception;

    /**
     * 710001 用户渠道注册
     * @param reqMain 请求参数
     * @return
     * @throws Exception
     */
    Result channelRegister(ReqMain reqMain) throws Exception;

    /**
     * 400006 用户实名认证
     * @param reqMain 请求参数
     * @return
     * @throws Exception
     */
    Result realNameAuth(ReqMain reqMain) throws Exception;

    /**
     * 400004 用户登录密码修改
     * @param reqMain 请求参数
     * @return
     * @throws Exception
     */
    Result changePassword(ReqMain reqMain) throws Exception;


    /**
     * 400005 用户登录密码重置
     * @param reqMain 请求参数
     * @return
     * @throws Exception
     */
    Result resetPassword(ReqMain reqMain) throws Exception;

    /**
     * 550007 用户签到
     * @param reqMain 请求参数
     * @return
     * @throws Exception
     */
    Result customerSign(ReqMain reqMain) throws Exception;

    /**
     * 550008 查询用户签到次数
     * @param reqMain 请求参数
     * @return
     * @throws Exception
     */
    Result queryCustomerSignCount(ReqMain reqMain) throws Exception;

    /**
     * 400072 风险测试
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result riskTest(ReqMain reqMain) throws Exception;

    /**
     * 400073 查询用户风险测试类型
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result queryCustomerRiskTestType(ReqMain reqMain) throws Exception;

    /**
     * 400077 个人中心风险测试
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result userCenterRisk(ReqMain reqMain) throws Exception;

    /*
     * 400055 检查验证码
     *
     */
    Result checkValidateCode(ReqMain reqMain) throws Exception;


    /*
     * 400007 校验验证码
     */
    Result checkIntroduceCode(ReqMain reqMain) throws Exception;



    /*
     * 400009 签到   已弃用，新的签到接口550007
     *
     */
    @Deprecated
    Result sign(ReqMain reqMain) throws Exception;


    /*
     * 400011 token转换number
     *
     */
    Result cmToken2CmNumber(ReqMain reqMain) throws Exception;

    /*
     * 400014 判断手机是否注册
     *
     */
    Result isPhoneRegistered(ReqMain reqMain) throws Exception;

    /*
         * 400020 校验密码
         *
         */
    Result validatePassword(ReqMain reqMain) throws Exception;

    /*
     * 410001 设置交易密码 新存管弃用
     *
     */
    @Deprecated
    Result setTradePassword(ReqMain reqMain) throws Exception;

    /*
     * 410002 修改交易密码  新存管弃用
     *
     */
    @Deprecated
    Result changeTradePassword(ReqMain reqMain) throws Exception;

    /*
     * 410003 重置交易密码 新存管弃用
     *
     */
    @Deprecated
    Result resetTradePassword(ReqMain reqMain) throws Exception;




    /*
     * 800010 获取用户Id
     *
     */
    Result cmToken2CmId(ReqMain reqMain) throws Exception;

    /*
     * 800016 根据sessionToken获取cmNumber
     *
     */
    Result sessionToken2CmNumber(ReqMain reqMain) throws Exception;

    /*
     * 800019 修改手机号之前校验信息
     *
     */
    Result checkCustomerInfo(ReqMain reqMain) throws Exception;

    /*
     * 800020 修改手机号
     *
     */
    Result changeCellphone(ReqMain reqMain) throws Exception;

    /**
     * 用户借款意向信息
     *
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result customerBorrowInfo(ReqMain reqMain) throws Exception;




    /**
     * 授权: 产品交割日
     *
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result customerAuthorize(ReqMain reqMain) throws Exception;

    /**
     * 开具出借凭证
     * @param reqMain
     * @return
     * @throws Exception
     */
    Result borrowCertificate(ReqMain reqMain) throws Exception;

    Result checkEmailValidateCode(ReqMain reqMain) throws Exception;

    /*
     * 750001 返利网注册
     *
     */
    Result fanLiRegister(ReqMain reqMain) throws Exception;

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
     * 会员中心
     */
    Result userCenter(ReqMain reqMain);

    /**
     * 月度礼包初始化
     */
    Result initMonthGift(ReqMain reqMain);

    /**
     * 月度礼包领取
     */
    Result gainMonthGift(ReqMain reqMain);

    /**
     * 会员等级信息
     */
    Result memberLevelInfo(ReqMain reqMain);

    /**
     * 获取借款人信息
     */
    Result getBorrowerInfo(ReqMain reqMain);
}