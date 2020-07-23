package com.zdmoney.models.customer;

import com.zdmoney.common.entity.AbstractEntity;
import com.zdmoney.common.handler.SecurityFieldTypeHandler;
import lombok.Getter;
import lombok.Setter;
import tk.mybatis.mapper.annotation.ColumnType;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


@Table(name = "CUSTOMER_MAIN_INFO")
@Getter
@Setter
public class  CustomerMainInfo extends AbstractEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select CM_SEQ.nextval from dual")
    private Long id;

    private String cmNumber;

    @Column(name = "CM_CELLPHONE")
    @ColumnType(typeHandler = SecurityFieldTypeHandler.class)
    private String cmCellphone;

    private String cmAccount;

    private String cmRealName;

    private String cmPassword;

    private Integer cmValid;

    private String cmToken;

    private String cmDevice;

    private String cmRecommend;

    private Date cmBindcardDate;

    private Date cmValidcardDate;

    private String cmOpenMechanism;

    private String cmOpenPlatform;

    private String cmTogatherType;

    private String cmOpenChannel;

    private String cmRegisterVersion;

    @Column(name = "CUSTOMER_BANK_ACCOUNT_ID")
    private String bankAccountId;

    private String cmSalt;

    private String cmLoginPassword;

    @Column(name = "CM_FIRST_CONSUME_DATE")
    private Date firstConsumeDate;

    private Long isConsumed;

    private String openId;

    private String loginOpenId;

    private String payPassword;

    private Integer payErrorTime;

    private Date payLockTime;

    private Integer resetErrorTime;

    private Date resetLockTime;

    private Long limitType;

    private Date limitLastDate;

    private Date resetErrorLastTime;

    private Date payErrorLastTime;

    /**
     * 邀请码(自己的)
     */
    private String cmInviteCode;

    /**
     * 介绍人码（介绍该客户注册）
     */
    private String cmIntroduceCode;

    private Long cmInputId;

    private Date cmInputDate;

    private Long cmModifyId;

    private Date cmModifyDate;

    private Integer cmEmployee;

    private Integer cmStatus;

    @Column(name = "CM_IDNUM")
    @ColumnType(typeHandler = SecurityFieldTypeHandler.class)
    private String cmIdnum;

    private Integer cmIdnumType;

    private Integer cmAuthenCount;

    private String cmOrigin;

    private BigDecimal wealthValue;

    private Integer buyWechat;

    private Date realNameTime;

    private String channelCode;

    private Integer memberType;

    private String plannerInviteCode;

    private Integer registerSource;

    @Column(name = "MOBILE_LOCK_TIME")
    private Date mobileLockTime;
    @Column(name = "MOBILE_ERROR_TIMES")
    private Integer mobileErrorTimes;
    @Column(name = "MOBILE_ERROR_LAST_TIME")
    private Date mobileLastTime;

    private String cmOpenAccountFlag;

    private Date cmOpenAccountDate;

    private  Long oldBankAccountId; // 旧的银行卡ID

    private String introducer;

    @Column(name = "INTRODUCER_IDNUM")
    @ColumnType(typeHandler = SecurityFieldTypeHandler.class)
    private String introducerIdNum;

    @Column(name = "RISK_TEST_TYPE")
    private String riskTestType;

    @Column(name = "RISK_TEST_TIME")
    private Date riskTestTime;

    private String accountType;//账户类型

    private String ip;

    private String userLabel;

    private String userLevel;

    private Date riskExpireTime;//风险测评过期时间

    private  Integer signContract; // 电子合同签约 0-未签约  1-已签约

    /*
	 *营业执照
	 */
    private String businessLicense;

    /*
     *组织机构代码
     */
    private String orgCode;

    /*
     *企业名称
     */
    private String enterpriseName;

    /*
     *客户类型
     */
    private String customerType;

    /**
     * 微信绑定时间
     */
    @Column(name = "WX_BIND_TIME")
    private Date wxBindTime;

    /**
     * 富友loginId
     */
    @Column(name = "FUIOU_LOGIN_ID")
    private String fuiouLoginId;

    /**
     * 是否设置华瑞密码
     */
    @Column(name = "IS_SET_PWD")
    private Integer isSetPwd;
    private String fanliUid;//返利网会员标识

    private String fanliTc; //返利网订单跟踪信息

    /**
     * 会员等级
     * 1:铁象 3:铜象 5:银象 7:金象 9:白金象 11:钻石象 13:无极象
     */
    @Column(name = "MEMBER_LEVEL")
    private Integer memberLevel;

    /**
     * 会员等级切换时间
     */
    @Column(name = "RATING_CHANGING_DATE")
    private Date ratingChangingDate;

    /**
     * 邀请人会员等级
     */
    @Column(name = "INVITER_MEMBER_LEVEL")
    private Integer inviterMemberLevel;

    /**
     * 邀请人身份
     * 0-理财师 1-理财师1级客户 2-理财师2级客户 3-互联网客户 4-互联网员工
     */
    @Column(name = "INVITER_USER_LEVEL")
    private String inviterUserLevel;
}