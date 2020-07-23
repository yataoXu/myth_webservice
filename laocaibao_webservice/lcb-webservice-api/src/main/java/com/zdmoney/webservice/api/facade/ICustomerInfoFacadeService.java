package com.zdmoney.webservice.api.facade;

import com.zdmoney.webservice.api.common.dto.PageResultDto;
import com.zdmoney.webservice.api.common.dto.ResultDto;
import com.zdmoney.webservice.api.dto.customer.*;

import java.util.List;

import java.util.Map;

public interface ICustomerInfoFacadeService{

    PageResultDto<CustomerMainInfoVo> getCustomerBySearchParamNew(CustomerInfoDto customerMainInfoVo);

    ResultDto<CustomerAddress> getCustomerAddressInfo(Long customerId);

    ResultDto<CustomerInfoVO> selectOneCustomerInfo(String cmNumber);

    /**
     * 实名认证
     * @param realName
     * @param idCard
     * @return
     */
    ResultDto realNameAuth(String realName, String idCard);
    PageResultDto<BusiUnbindRecordVO> searchUnbindingRecords(UnbindingRecordSearchDTO searchDto);

    PageResultDto<CustomerInfoVO> searchUnauthedCustomers(CustomerSearchDTO searchDto);

    ResultDto<Integer> clearTryingAuthedTimes(Long id);

    PageResultDto<ExpandedCustomerInfoVO> searchCustomersWithPlannerInfo(CustomerSearchDTO searchDto);

    ResultDto<Integer> modifyCustomerMemberType(Long id);

    PageResultDto<ExpandedCustomerInfoVO> searchCustomersBankAccountInfo(CustomerSearchDTO searchDto);

    ResultDto<ExpandedCustomerInfoVO> selectOneCustomerBankAccount(CustomerSearchDTO searchDto);

    ResultDto<ExpandedCustomerInfoVO> searchBankAccountDetail(Long id);

    ResultDto<Integer> updateCustomerBankAccount(CustomerBankAccountVO model);

    ResultDto unbindingByOperationalStaff(UnbindingReqDto reqDto);

    /**
     * 下单hessian接口
     * @param orderingDto
     * @return
     */
    ResultDto<Long> order(OrderingDto orderingDto);

    /**
     * 付款hessian接口
     * @param payingDto
     * @return
     */
    ResultDto pay(PayingDto payingDto);




    /**
     * 获取一条债权转让估值
     * @param orderId
     * @return
     */
    ResultDto<TransferEstimatedPricesVo> getOneEstimatedPrice(Long orderId);


    /**
     * 发起转让
     * @param transferingDto
     * @return
     */
    ResultDto requestTransfering(TransferingDto transferingDto);

    /**
     * 更新借款人意向
     * @param borrowInfoDtoDto
     * @return
     */
    ResultDto updateBorrowInfo(CustomerBorrowInfoDto borrowInfoDtoDto);

    /**
     * 法大大签约确认
     */
    ResultDto customerSignContract(CustomerSignContractDto signContractDto);

    /**
     * 根据身份证获取最新绑定银行卡号
     * @param idNo
     * @return
     */
    ResultDto<String> getBankCardByIdNum(String idNo);

    /**
     * 变更手机号
     * @param currentPhoneNo  当前手机号
     * @param newPhoneNo 变更后手机号
     * @return
     */
    ResultDto<String> changePhoneNo(String currentPhoneNo,String newPhoneNo);

    /**
     * 通过手机注销账户
     * @param phoneNo
     * @return
     */
    ResultDto<String> deactivateAccountByPhone(String phoneNo);

    PageResultDto<CustomerInfoVO> searchCustomerByNameAndPhone(CustomerSearchDTO searchDto);


    /**
     * 提现解冻定时任务
     * @return
     */
    void withdrawThawJob();

    /**
     * 批量查询用户属性
     * @param userIds
     * @return
     */
    ResultDto<List<UserInfoDTO>> listUserInfos(List<Long> userIds);

    /**
     * 获取用户在投资金
     * @return
     */
    ResultDto<Map<String,String>> getHoldAssest(String cmNumber);

    /**
     * 根据code获取用户授权信息
     *
     * @param code code作为换取access_token的票据, 每次用户授权带上的code将不一样
     *             code只能使用一次，5分钟未被使用自动过期。
     * @return
     */
    ResultDto<WxAuthUserInfoDTO> getWxAuthUserInfo(String code);

    /**
     * 获取微信分享签名
     *
     * @return
     */
    ResultDto<WxTicketInfoDTO> getWxTicket(String url);
}
