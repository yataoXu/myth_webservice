package com.zdmoney.webservice.api.facade;

import com.zdmoney.webservice.api.common.dto.ResultDto;
import com.zdmoney.webservice.api.dto.credit.wacai.*;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 挖财
 * Created by qinz on 2019/3/7.
 */
public interface IWaCaiFacadeService {


    /**
     * 开户/授权
     *
     * @param requestDto
     * @return
     */
    ResultDto<String> openAccount(OpenAuthRequestDto requestDto);

    /**
     * 绑定银行卡
     *
     * @param requestDto
     * @return
     */
    ResultDto<String> bindBankCard(CashierRequestDto requestDto);

    /**
     * 解绑银行卡
     *
     * @param requestDto
     * @return
     */
    ResultDto<String> unBindBankCard(CashierRequestDto requestDto);

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
     * 用身份证获取用户信息
     * @param idNo
     * @param channelCode
     * @return
     */
    ResultDto<UserInfoDto> getUserInfoWithIdCard (String idNo, String channelCode);

    /**
     * 提现结果查询接口
     * @param
     * @param
     * @return
     */
    ResultDto<List<WithdrawDto>> getWithdrawFlows(GetInfoRequestDto requestDto);

    /**
     * @title
     * @description 挖财兜底金额预警定时任务
     * @author weiNian
     * @updateTime 2019/3/13 10:22
     * @throws
     */
    void alarmAmountWacaiTask();
}
