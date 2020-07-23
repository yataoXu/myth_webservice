package com.zdmoney.mapper.customer;

import com.zdmoney.common.mybatis.mapper.JdMapper;
import com.zdmoney.models.customer.CustomerAuthChannel;
import com.zdmoney.models.customer.CustomerInvestingInfo;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.webservice.api.dto.customer.CustomerGrantInfoDTO;
import com.zdmoney.webservice.api.dto.customer.CustomerInfoVO;
import com.zdmoney.webservice.api.dto.customer.CustomerMainInfoVo;
import com.zdmoney.webservice.api.dto.customer.ExpandedCustomerInfoVO;
import com.zdmoney.webservice.api.dto.customer.*;
import com.zdmoney.webservice.api.dto.plan.SpecialFinancialPlannerVO;
import com.zdmoney.webservice.api.dto.ym.CustomerMainInfoDto;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface CustomerMainInfoMapper extends JdMapper<CustomerMainInfo, Long> {

    List<CustomerMainInfo> getCustomerBySearchParam(Map<String, Object> params);

    long getCustMainCountByParam(Map<String, Object> params);

    CustomerMainInfo queryCustMainInfoByIntroduceCode(Map<String, Object> params);

    CustomerMainInfo selectByPhone(@Param("cmCellphone") String cellphone);

    int updatepassword(@Param("cmCellphone") String cellphone, @Param("cmPassword") String cmPassword, @Param("cmLoginPassword") String cmLoginPassword);

    int updateTradePassword(@Param("cmCellphone") String cellphone, @Param("tradePassword") String tradePassword);

    int updateResetLockInfos(@Param("cmCellphone") String cellphone, @Param("resetErrorTime") Integer resetErrorTime, @Param("resetLockTime") Date resetLockTime);

    int updateByCustomerId(CustomerMainInfo record);

    CustomerMainInfo selectByCustomerId(@Param("cmCustomerId") Long cmCustomerId);

    int updateCmStatusById(@Param("cmStatus") int cmStatus, @Param("cmCustomerId") Long cmCustomerId);

    int updateCmAuthenCountById(@Param("cmAuthenCount") BigDecimal cmAuthenCount, @Param("cmCustomerId") Long cmCustomerId);

    CustomerMainInfo selectByIdNum(@Param("cmIdnum") String cmIdnum);

    CustomerMainInfo selecCustomerByInivteCode(@Param("cmInviteCode") String cmInviteCode);

    String selectRecommendByInivteCode(@Param("cmInviteCode") String cmInviteCode);

    CustomerMainInfo validateIntroduceCode(String introduceCode);

    String selectTokenBycustId(@Param("id") String id);

    int updateTokenBycustId(@Param("cmToken") String cmToken, @Param("cmDevice") String cmDevice, @Param("id") Long id);

    int clearToken(@Param("cmToken") String cmToken, @Param("cmDevice") String cmDevice);

    List<CustomerMainInfo> selectByToken(@Param("cmToken") String cmToken);

    int selectInviteCodeNum(String inviteCode);

    int selectByThird(CustomerMainInfo customer);

    List<CustomerMainInfo> selectInviteCodeByIntroduceCode(String introduceCode);

    int validateIntroduceCodeByDay(String introduceCode);

    CustomerMainInfo selectBycmNumber(String cmNumber);

    List<CustomerMainInfo> selectAllCustomer();

    CustomerMainInfo checkBind(String openId);

    CustomerMainInfo checkLoginOpenIdBind(String loginOpenId);

    int bindWeichat(Map<String, Object> map);

    int unBind(Long customerId);

    CustomerAuthChannel queryAuthChannelInfo();

    int updateCmStatusFailure(@Param("id") Long id);

    int updateCardInfo(@Param("currentDate") Date date, @Param("accountId") Long accountId, @Param("id") Long customerId);

    int updateCustomerRiskTestType(@Param("riskTestType") String riskTestType, @Param("customerId") Long customerId);

    SpecialFinancialPlannerVO querySpecialFinancialPlannerInfo(String cmNumber);

    List<CustomerMainInfoVo> getCustomerBySearchParamNew(Map<String, Object> params);//返回客户列表（改造）

    int getCountBySearchParamNew(Map<String, Object> params);//返回客户列表（改造）

    List<CustomerInfoVO> getCustomerByParams(Map<String, Object> map);

    List<ExpandedCustomerInfoVO> selectCustomerWithPlannerInfo(Map<String, Object> map);

    /**
     * 根据邀请码更新客户的会员类型为普通会员，同时更新会员的理财师邀请码为空
     *
     * @param introduceCode
     * @return
     */
    int modifyInviteeMemberTypeByIntroduceCode(String introduceCode);

    List<ExpandedCustomerInfoVO> selectCustomersBankAccountInfo(Map<String, Object> map);

    ExpandedCustomerInfoVO selectOneCustomerBankAccount(Map<String, Object> map);

    ExpandedCustomerInfoVO selectBankAccountDetail(Long id);

    /**
     * 查询用户信息
     *
     * @param customerMainInfoDto
     * @return
     */
    List<com.zdmoney.vo.CustomerMainInfoVo> getCustomerInfo(CustomerMainInfoDto customerMainInfoDto);

    CustomerMainInfoVo getUserInfo(String cmNumber);

    List<CustomerMainInfoVo> getUserIdByInvideCode(String invideCode);

    int updateUserLevel(CustomerMainInfoVo customerMainInfoVo);

    CustomerMainInfoVo selectCustomerByIdNum(String idNum);

    List<CustomerInfoVO> getCustomerByNameAndPhone(Map<String, Object> map);

    List<SpecialFinancialPlannerVO> querySpecialFinancialPlannerInfoList(Map<String, Object> map);

    List<RegisterNotifyDto> queryRegisterNotify(Map<String, Object> map);

    List<RecordDto> queryWeChatBindNotify(Map<String, Object> map);

    List<RecordDto> queryCloseAccount(Map<String, Object> map);

    List<NameCertificateNotifyDto> queryNameCertificateNotify(Map<String, Object> map);

    List<BusiUnbindRecordDto> queryPhoneBindModify(Map<String, Object> map);

    CustomerMainInfo queryCustomerInfoByLoginId(String loginId);

    List<CustomerGrantInfoDTO> searchCustomerGrantInfo(Map<String,Object> map);

    List<UserInfoDTO> listUserInfos(List<Long> userIds);

    List<CustomerInvestingInfo> selectCustomerInvestingInfo(Map<String,Object> map);

    int updateByMap(Map<String,Object> map);

    CustomerMainInfo selectByIdNumAndPhone(@Param("cmCellphone") String cmCellphone);

    CustomerMainInfo findByIdNoAndPhone(@Param("cmCellphone") String cmCellphone, @Param("cmIdnum") String idNum);

    List<CustomerMainInfo> getCustomerForDebtTransfer(Map<String,Object> map);

    //根据订单编号获得借款人信息
    CustomerMainInfo getCustomerForOrderNo(String orderNo);

}