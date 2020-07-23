package com.zdmoney.webservice.api.facade;

import com.zdmoney.webservice.api.common.dto.ResultDto;
import com.zdmoney.webservice.api.dto.channel.*;

import java.util.List;

/**
 * Created by qinz on 2019/2/13.
 */
public interface IZdqqFacadeService {

    /**
     * 注册
     *
     * @param requestDto
     * @return
     */
    ResultDto<String> register(RegisterRequestDto requestDto);

    /**
     * 获取验证码
     *
     * @param requestDto
     * @return
     */
    ResultDto<String> getVerificationCode(GetCodeRequestDto requestDto);

    /**
     * 实名认证
     *
     * @param requestDto
     * @return
     */
    ResultDto realNameAuth(RealNameAuthRequestDto requestDto);

    /**
     * 开户
     *
     * @param requestDto
     * @return
     */
    ResultDto<String> openAccount(InteractionRequestDto requestDto);

    /**
     * 授权
     *
     * @param requestDto
     * @return
     */
    ResultDto<String> authorize(InteractionRequestDto requestDto);

    /**
     * 绑定银行卡
     *
     * @param requestDto
     * @return
     */
    ResultDto<String> bindBankCard(InteractionRequestDto requestDto);

    /**
     * 解绑银行卡
     *
     * @param requestDto
     * @return
     */
    ResultDto<String> unBindBankCard(InteractionRequestDto requestDto);

    /**
     * 提现
     *
     * @param requestDto
     * @return
     */
    ResultDto<String> withdraw(WithdrawRequestDto requestDto);

    /**
     * 获取用户信息
     *
     * @param requestDto
     * @return
     */
    ResultDto<UserInfoDto> getUserInfo(GetInfoRequestDto requestDto);

    /**
     * 提现结果查询接口
     * @param
     * @param
     * @return
     */
    ResultDto<List<WithdrawFlowsDto>> getWithdrawFlows(GetInfoRequestDto requestDto);
}
