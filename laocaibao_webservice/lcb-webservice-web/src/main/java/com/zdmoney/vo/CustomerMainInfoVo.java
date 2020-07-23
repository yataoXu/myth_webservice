package com.zdmoney.vo;

import java.math.BigDecimal;
import java.util.Date;

public class CustomerMainInfoVo {
	 private Long id;

	 private String cmNumber;

	 private String cmCellphone;

	 private String cmAccount;//注册账号

	 private String cmRealName;

	 private String cmPassword;

	 private Short cmValid;

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

	 private String cmFirstConsumeDate;//首次购买日期

	 private Short cmEmployee;//是否员工
	 
	 private String cmEmployeeStr;//是否员工
	 
	 private String renzheng;//是否认证

	 private Short cmStatus;

	 private String cmIdnum;

	 private Short cmIdnumType;

	 private BigDecimal cmAuthenCount;

	 private String cmToken;

	 private BigDecimal cmDevice;
	 
	 private String cmRecommend;//来源
	 
	 private String cmRecommendStr;//来源
	 
	 private String introducer;//介绍人

	 private String cmOrigin;//类型

	 private String isConsumed;//是否注资

	 private String introducerIdnum;//介绍人证件号码

	 private String cbAccount;//客户银行卡账号
	 
	 private String cbBranchName;//客户银行卡分行名称
	 
	 private BigDecimal balancePub;//余额明文
	 
	private Long customerBankAccountId;//银行Id

	private String merchantCode;//商户码

	private String channelCode;//渠道码;

	private String staffCode;//会员编号;

	private String memberType;//客户类型;

	private String openAccountFlag;//客户类型;

	private String registerSource;//注册来源

	private String cmOpenPlatform;//开户平台

	private Integer buyTimes; //购买次数

	private String riskTestType;//风险测评类型

	private Date riskTestTime;//测试时间

	private String customerType;//用户类型

	public void setBalancePub(BigDecimal balancePub) {
		this.balancePub = balancePub;
	}

	public BigDecimal getBalancePub() {
		return balancePub;
	}

	public String getCmRecommendStr() {
		return cmRecommendStr;
	}

	public void setCmRecommendStr(String cmRecommendStr) {
		this.cmRecommendStr = cmRecommendStr;
	}

	public String getCmEmployeeStr() {
		return cmEmployeeStr;
	}

	public void setCmEmployeeStr(String cmEmployeeStr) {
		this.cmEmployeeStr = cmEmployeeStr;
	}

	public Long getCustomerBankAccountId() {
		return customerBankAccountId;
	}

	public void setCustomerBankAccountId(Long customerBankAccountId) {
		this.customerBankAccountId = customerBankAccountId;
	}

	public String getRenzheng() {
		return renzheng;
	}

	public void setRenzheng(String renzheng) {
		this.renzheng = renzheng;
	}

	public String getIntroducerIdnum() {
		return introducerIdnum;
	}

	public void setIntroducerIdnum(String introducerIdnum) {
		this.introducerIdnum = introducerIdnum;
	}

	public String getCbAccount() {
		return cbAccount;
	}

	public void setCbAccount(String cbAccount) {
		this.cbAccount = cbAccount;
	}

	public String getCbBranchName() {
		return cbBranchName;
	}

	public void setCbBranchName(String cbBranchName) {
		this.cbBranchName = cbBranchName;
	}

	public String getCmRecommend() {
		return cmRecommend;
	}

	public void setCmRecommend(String cmRecommend) {
		this.cmRecommend = cmRecommend;
	}

	public String getIntroducer() {
		return introducer;
	}

	public void setIntroducer(String introducer) {
		this.introducer = introducer;
	}

	public Long getId() {
		 return id;
	 }

	 public void setId(Long id) {
		 this.id = id;
	 }

	 public String getCmNumber() {
		 return cmNumber;
	 }

	 public void setCmNumber(String cmNumber) {
		 this.cmNumber = cmNumber == null ? null : cmNumber.trim();
	 }

	 public String getCmCellphone() {
		 return cmCellphone;
	 }

	 public void setCmCellphone(String cmCellphone) {
		 this.cmCellphone = cmCellphone == null ? null : cmCellphone.trim();
	 }

	 public String getCmAccount() {
		 return cmAccount;
	 }

	 public void setCmAccount(String cmAccount) {
		 this.cmAccount = cmAccount == null ? null : cmAccount.trim();
	 }

	 public String getCmRealName() {
		 return cmRealName;
	 }

	 public void setCmRealName(String cmRealName) {
		 this.cmRealName = cmRealName == null ? null : cmRealName.trim();
	 }

	 public String getCmPassword() {
		 return cmPassword;
	 }

	 public void setCmPassword(String cmPassword) {
		 this.cmPassword = cmPassword == null ? null : cmPassword.trim();
	 }

	 public Short getCmValid() {
		 return cmValid;
	 }

	 public void setCmValid(Short cmValid) {
		 this.cmValid = cmValid;
	 }

	 public String getCmInviteCode() {
		 return cmInviteCode;
	 }

	 public void setCmInviteCode(String cmInviteCode) {
		 this.cmInviteCode = cmInviteCode == null ? null : cmInviteCode.trim();
	 }

	 public String getCmIntroduceCode() {
		 return cmIntroduceCode;
	 }

	 public void setCmIntroduceCode(String cmIntroduceCode) {
		 this.cmIntroduceCode = cmIntroduceCode == null ? null : cmIntroduceCode.trim();
	 }

	 public Long getCmInputId() {
		 return cmInputId;
	 }

	 public void setCmInputId(Long cmInputId) {
		 this.cmInputId = cmInputId;
	 }

	 public Date getCmInputDate() {
		 return cmInputDate;
	 }

	 public void setCmInputDate(Date cmInputDate) {
		 this.cmInputDate = cmInputDate;
	 }

	 public Long getCmModifyId() {
		 return cmModifyId;
	 }

	 public void setCmModifyId(Long cmModifyId) {
		 this.cmModifyId = cmModifyId;
	 }

	 public Date getCmModifyDate() {
		 return cmModifyDate;
	 }

	 public void setCmModifyDate(Date cmModifyDate) {
		 this.cmModifyDate = cmModifyDate;
	 }

	 public Short getCmEmployee() {
		 return cmEmployee;
	 }

	 public void setCmEmployee(Short cmEmployee) {
		 this.cmEmployee = cmEmployee;
	 }

	 public Short getCmStatus() {
		 return cmStatus;
	 }

	 public void setCmStatus(Short cmStatus) {
		 this.cmStatus = cmStatus;
	 }

	 public String getCmIdnum() {
		 return cmIdnum;
	 }

	 public void setCmIdnum(String cmIdnum) {
		 this.cmIdnum = cmIdnum == null ? null : cmIdnum.trim();
	 }

	 public Short getCmIdnumType() {
		 return cmIdnumType;
	 }

	 public void setCmIdnumType(Short cmIdnumType) {
		 this.cmIdnumType = cmIdnumType;
	 }

	 public BigDecimal getCmAuthenCount() {
		 return cmAuthenCount;
	 }

	 public void setCmAuthenCount(BigDecimal cmAuthenCount) {
		 this.cmAuthenCount = cmAuthenCount;
	 }

	 public String getCmToken() {
		 return cmToken;
	 }

	 public void setCmToken(String cmToken) {
		 this.cmToken = cmToken;
	 }

	 public BigDecimal getCmDevice() {
		 return cmDevice;
	 }

	 public void setCmDevice(BigDecimal cmDevice) {
		 this.cmDevice = cmDevice;
	 }

	public String getCmOrigin() {
		return cmOrigin;
	}

	public void setCmOrigin(String cmOrigin) {
		this.cmOrigin = cmOrigin;
	}

	public String getIsConsumed() {
		return isConsumed;
	}

	public void setIsConsumed(String isConsumed) {
		this.isConsumed = isConsumed;
	}

	public String getCmFirstConsumeDate() {
		return cmFirstConsumeDate;
	}

	public void setCmFirstConsumeDate(String cmFirstConsumeDate) {
		this.cmFirstConsumeDate = cmFirstConsumeDate;
	}

	public String getMerchantCode() {
		return merchantCode;
	}

	public void setMerchantCode(String merchantCode) {
		this.merchantCode = merchantCode;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getStaffCode() {
		return staffCode;
	}

	public void setStaffCode(String staffCode) {
		this.staffCode = staffCode;
	}

	public String getMemberType() {
		return memberType;
	}

	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}

	public Integer getBuyTimes() {
		return buyTimes;
	}

	public void setBuyTimes(Integer buyTimes) {
		this.buyTimes = buyTimes;
	}

	public String getOpenAccountFlag() {
		return openAccountFlag;
	}

	public void setOpenAccountFlag(String openAccountFlag) {
		this.openAccountFlag = openAccountFlag;
	}

	public String getCmOpenPlatform() {
		return cmOpenPlatform;
	}

	public void setCmOpenPlatform(String cmOpenPlatform) {
		this.cmOpenPlatform = cmOpenPlatform;
	}

	public String getRegisterSource() {
		return registerSource;
	}

	public void setRegisterSource(String registerSource) {
		this.registerSource = registerSource;
	}

	public String getRiskTestType() {
		return riskTestType;
	}

	public void setRiskTestType(String riskTestType) {
		this.riskTestType = riskTestType;
	}

	public Date getRiskTestTime() {
		return riskTestTime;
	}

	public void setRiskTestTime(Date riskTestTime) {
		this.riskTestTime = riskTestTime;
	}

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}
}



