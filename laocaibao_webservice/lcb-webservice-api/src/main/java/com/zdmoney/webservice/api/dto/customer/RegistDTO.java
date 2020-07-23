package com.zdmoney.webservice.api.dto.customer;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class RegistDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message="手机号不能为空")
    private String cellPhone;

    @NotNull(message="验证码不能为空")
    private String validateCode;

    @NotNull(message="密码不能为空")
    private String password;

    @NotNull(message="微信openId不能为空")
    private String openId;

    private String ip;

    private String platform;

    private String system;

    private String openChannel;

    private String mechanism;

    private String togatherType;

    private String version;

    /**
     * 营业执照
     */
    private String businessLicense;

    /**
     * 组织机构代码
     */
    private String orgCode;

    /**
     * 企业名称
     */
    private String enterpriseName;

    /**
     * 客户类型
     */
    private String customerType;

    /**
     * 用户属性：1.出借人2.借款人3.营销户 4.手续费户5.代偿户6.消费金融商家7.平台自有资金账户 9.担保方手续费户
     */
    private String accountType;

    /**
     * 返利网会员标识
     */
    private String uid;

    /**
     * 返利网订单跟踪信息
     */
    private String tc;

}
