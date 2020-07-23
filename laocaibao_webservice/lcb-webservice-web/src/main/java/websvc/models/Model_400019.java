package websvc.models;

import lombok.Getter;
import lombok.Setter;
import websvc.req.ReqParam;

import javax.validation.constraints.NotNull;

/**
 * Created by 00225181 on 2016/3/15.
 */
@Setter
@Getter
public class Model_400019 extends ReqParam {

    @NotNull(message = "手机号不能为空")
    private String cellPhone;//手机号

    @NotNull(message = "验证码类型不能为空")
    private Integer type;//验证码类型0,捞财宝注册，1:找回密码 3--微信注册和绑定 5--设置交易密码 6--重置交易密码，7:商户用户注册，8:欧洲杯英雄榜投票，10：充值 11.绑卡,13:访问白名单校验

    private String busiMsg;

    private Integer codeType = 0;//0:短信，1:语音

}
