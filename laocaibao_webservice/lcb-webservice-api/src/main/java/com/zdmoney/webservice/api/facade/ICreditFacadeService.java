package com.zdmoney.webservice.api.facade;

import com.zdmoney.webservice.api.common.dto.ResultDto;
import com.zdmoney.webservice.api.dto.Asset.BankCardInfoDTO;
import com.zdmoney.webservice.api.dto.Asset.BankCardInfoListDTO;
import com.zdmoney.webservice.api.dto.Asset.BusiTradeFlowDTO;
import com.zdmoney.webservice.api.dto.Asset.UserBindCardDTO;
import com.zdmoney.webservice.api.dto.credit.GettingVCRequestDto;
import com.zdmoney.webservice.api.dto.credit.RegisteringRequestDto;
import com.zdmoney.webservice.api.dto.customer.CustomerMainInfoVo;

import java.util.List;

/**
 * Created by user on 2017/10/19.
 */
public interface ICreditFacadeService {

    ResultDto<String> register(RegisteringRequestDto requestDto) throws Exception;

    ResultDto<String> getVerificationCode (GettingVCRequestDto requestDto);

    /**
     * 绑定银行卡
     * @param userBindCardDTO
     * @return
     */
    ResultDto BindCard(UserBindCardDTO userBindCardDTO);

    /**
     * 获取银行卡信息
     * @param customerId
     * @return
     */
    ResultDto<BankCardInfoListDTO> getCustomerBankInfo(Long customerId);

    /**
     * 实名认证
     * @param customerId
     * @param idNum
     * @param realName
     * @return
     */
    ResultDto realNameAuth(Long customerId, String idNum, String realName);

    /**
     * 登录
     * @param Cellphone
     * @param validateCode
     * @return
     */
    ResultDto login(String Cellphone, String validateCode);

    /**
     * 根据身份证好获取手机号
     * @param idNum
     * @return
     */
    ResultDto<String> getUserInfoByIdNum(String idNum);

    /**
     * 根据身份证号获取用户信息
     * @param idNum
     * @return
     */
    ResultDto<CustomerMainInfoVo> getCustomerInfoByIdNum(String idNum);

    /**
     * 注册
     * @param cellphone
     * @param validateCode
     * @return
     */
    ResultDto<Long> validateCodeRegister(String cellphone, String validateCode);

    /**
     * 获取验证码
     * @param cellPhone
     * @return
     */
    ResultDto getLoginValidCode(String cellPhone);

    /**
     * 授权
     *
     * @param customerId
     * @return
     */
    ResultDto customerAuthorize(long customerId);

    /**
     *  查询借款人银行卡信息
     * @param customerId
     * @return
     */
    ResultDto<BankCardInfoDTO> getBankCardInfo(long customerId);

    /**
     *  查询提现流水
     * @param customerId
     * @return
     */

    ResultDto<List<BusiTradeFlowDTO>>queryRecentTradeFlow(long customerId, String trdDate);

    /**
     * 提现
     * @param customerNo
     * @param payAmt
     * @return
     * @throws Exception
     */
    ResultDto withdraw(String customerNo, String payAmt, String pageUrl) throws Exception;

    /**
     * 充值
     * @param customerNo
     * @param channelType
     * @param payAmt
     * @return
     */
    ResultDto recharge(String customerNo, Integer channelType, String payAmt, String pageUrl);
}
