package com.zdmoney.web.dto;

import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.constant.AppConstants;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.webservice.api.common.CustomerAccountType;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.ContextLoader;

import java.io.Serializable;

@Setter
@Getter
public class CustomerDTO implements Serializable {

    /**
     * 会员姓名
     */
    private String cmRealName = "";

    /**
     * 会员ID
     */
    private String cmCustomerId = "";

    /**
     * 身份证号码
     */
    private String cmIdnum = "";
    /**
     * 手机号码
     */
    private String cmCellphone = "";

    private String cmStatus = "";

    private String cmNumber;

    /**
     * 是否是员工
     * 0-否  1-是
     */
    private String cmEmployee = "";

    /**
     * 邀请码
     */
    private String CmInviteCode = "";

    /**
     * 介绍人码
     */
    private String CmIntroduceCode = "";

    /*
    * 会话令牌
    */
    private String sessionToken = "";

    /*
     * 推送设备号
     */
    private String token = "";

    /*
    * 设备号
    */
    private String deviceId = "";

    /*
     *分享红包url
     */
    private String shareRedUrl = "";

    private String qrCode = "";

    private int integral;

    private String cmNumToken = "";

    /*
     *是否设置交易密码
     */
    private int isSetPwd;//0:否，1:是

    /*
     *是否实名认证
     */
    private int isAuth;//0:否，1:是

    private String cmAccount;

    //商户验证码
    private String validaCode;

    //是否开户
    private int isOpen;//0否，1是

    //是否绑卡
    private int isBindCard = 0;//0否，1是

    //是否存管版本 0否 1是
    private int isDepositVersion;

    private int buyingPermitted;//1:允许购买产品 0：不允许购买

    private String customerType;//客户类型 0:个人 ，1：组织机构

    /**
     * 返回给前端页面的提示语
     */
    private String msg;

    private String pwdTips;

    private String fuiouLoginId;

    private String userLevel;

    private int authStatus = 0; //0认证成功，1认证失败

    private static ConfigParamBean configParamBean = ContextLoader.getCurrentWebApplicationContext().getBean(ConfigParamBean.class);

    public static CustomerDTO fromCustomerMainInfo(CustomerMainInfo mainInfo){
        CustomerDTO dto = new CustomerDTO();
        dto.setCmAccount(StringUtils.defaultString(mainInfo.getCmAccount()));
        dto.setCmCellphone(StringUtils.defaultString(mainInfo.getCmCellphone()));
        dto.setCmCustomerId(StringUtils.defaultString(mainInfo.getId().toString()));
        dto.setCmRealName(StringUtils.defaultString(mainInfo.getCmRealName()));
        dto.setCmStatus(mainInfo.getCmStatus().toString());
        dto.setCustomerType(mainInfo.getCustomerType());
        if(3 == mainInfo.getCmStatus()){
            dto.setCmIdnum(StringUtils.defaultString(mainInfo.getCmIdnum()));
            dto.setIsAuth(1);
        }else{
            dto.setIsAuth(0);
        }
        if(AppConstants.CmOpenAccountFlag.OPEN.equals(mainInfo.getCmOpenAccountFlag())){
            dto.setIsOpen(1);
        }
        else {
            dto.setIsOpen(0);
        }
        dto.setIsDepositVersion(Integer.valueOf(configParamBean.getDepositVersion()));
        dto.setCmInviteCode(StringUtils.defaultString(mainInfo.getCmInviteCode()));
        dto.setCmIntroduceCode(StringUtils.defaultString(mainInfo.getCmIntroduceCode()));
        dto.setIsSetPwd(mainInfo.getIsSetPwd());
        if (StringUtils.isNotEmpty(mainInfo.getFuiouLoginId()) && mainInfo.getIsSetPwd() == 0) {
            // 未设置
            dto.setIsSetPwd(0);
        } else {
            dto.setIsSetPwd(1);
        }
        if(StringUtils.isEmpty(mainInfo.getAccountType()) || CustomerAccountType.LENDER.getValue().equals(mainInfo.getAccountType())){//出借人允许购买
            dto.setBuyingPermitted(1);
        }else{
            dto.setBuyingPermitted(0);
        }
        if (StringUtils.isNotEmpty(mainInfo.getBankAccountId())) {
            dto.setIsBindCard(1);
        }
        dto.setFuiouLoginId(mainInfo.getFuiouLoginId());
        dto.setUserLevel(mainInfo.getUserLevel());
        return dto;
    }
}
