package com.zdmoney.models.product;

import com.zdmoney.common.entity.AbstractEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "BUSI_SUBJECT_INFO")
public class BusiSubjectInfo extends AbstractEntity<Long> implements Serializable {
    /**
     * 编号
     */
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select SEQ_BUSI_SUBJECT_INFO.nextval from dual")
    @Id
    private Long id;

    /**
     * 标的编号
     */
    @Column(name = "SUBJECT_NO")
    private String subjectNo;

    /**
     * 还款日期
     */
    @Column(name = "LAST_EXPIRE")
    private Date lastExpire;

    /**
     * 还款截止时间
     */
    @Column(name = "PAY_END_TIME")
    private String payEndTime;

    /**
     * 借款人姓名

     */
    @Column(name = "BORROWER_NAME")
    private String borrowerName;

    /**
     * PERSONAL-个人 ORGANIZATION -组织
     */
    @Column(name = "BORROWER_TYPE")
    private String borrowerType;

    /**
     * 借款人身份证号
     */
    @Column(name = "ID_NO")
    private String idNo;

    /**
     * 借款目的
     */
    @Column(name = "BORROW_PURPOSE")
    private String borrowPurpose;

    /**
     * 转让人名称
     */
    @Column(name = "TRANSFEROR_NAME")
    private String transferorName;

    /**
     * 转让人编号
     */
    @Column(name = "TRANSFERORID_NO")
    private String transferoridNo;

    /**
     * 借款组织机构代码
     */
    @Column(name = "ORGANIZATION_NO")
    private String organizationNo;

    /**
     * 借款组织机构名称

     */
    @Column(name = "ORGANIZATION_NAME")
    private String organizationName;

    /**
     * 还款方式:AVERAGE_CAPITAL_INTEREST-等额本息 ONE_CAPTITAL_INTEREST-一次性还本付息 BEFORE_INTEREST_AFTER_CAPTITAL-先息后本
     */
    @Column(name = "REPAY_TYPE")
    private String repayType;

    /**
     * 户名
     */
    @Column(name = "BANK_CARD_USER")
    private String bankCardUser;

    /**
     * 账号
     */
    @Column(name = "BANK_CARD_NO")
    private String bankCardNo;

    /**
     * 开户行
     */
    @Column(name = "BANK_NAME")
    private String bankName;

    /**
     * 开户支行

     */
    @Column(name = "BANK_BRANCH")
    private String bankBranch;

    /**
     * 标的结果 0-初始 1-正常 2-流标
     */
    @Column(name = "STATUS")
    private String status;

    /**
     * 放款结果流水
     */
    @Column(name = "SERNO")
    private String serno;

    /**
     * 流标退款 1-已退款 0-未退款 2-处理中
     */
    @Column(name = "REFUND_STATUS")
    private String refundStatus;

    /**
     * 是否满标 1-是 0-否
     */
    @Column(name = "IS_FINISH")
    private String isFinish;

    /**
     * 是否通知满标 1-已通知 0-未通知
     */
    @Column(name = "IS_NOTIFY")
    private String isNotify;

    /**
     * 已募集天数
     */
    @Column(name = "COLLECT_DAY")
    private Long collectDay;

    /**
     * 募集金额
     */
    @Column(name = "COLLECT_AMOUNT")
    private BigDecimal collectAmount;

    /**
     * 创建时间
     */
    @Column(name = "CREATE_DATE")
    private Date createDate;

    /**
     * 标的放款时间
     */
    @Column(name = "LOAN_DATE")
    private Date loanDate;

    /**
     * 备注
     */
    @Column(name = "REMARK")
    private String remark;

    /**
     * 用户编号
     */
    @Column(name = "CM_NUMBER")
    private String cmNumber;

    /**
     * 提前还款时间
     */
    @Column(name = "AHEAD_PAY_DATE")
    private Date aheadPayDate;

    /**
     * 借款人手机号
     */
    @Column(name = "BORROWER_PHONE")
    private String borrowerPhone;

    /**
     * 性别
     */
    @Column(name = "SEX")
    private String sex;

    /**
     * 婚姻状况
     */
    @Column(name = "MARITAL_STATUS")
    private String maritalStatus;

    /**
     * 出生年月
     */
    @Column(name = "BRITHDAY")
    private String brithday;

    /**
     * 所在城市
     */
    @Column(name = "CITY")
    private String city;

    /**
     * 有车
     */
    @Column(name = "HAS_CAR")
    private String hasCar;

    /**
     * 有房
     */
    @Column(name = "HAS_HOURSE")
    private String hasHourse;

    /**
     * 行业信息
     */
    @Column(name = "TRADE_INFO")
    private String tradeInfo;

    /**
     * 岗位信息
     */
    @Column(name = "POST_INFO")
    private String postInfo;

    /**
     * 借款人单位性质
     */
    @Column(name = "COMPANY_NATURE")
    private String companyNature;

    /**
     * 月收入信息
     */
    @Column(name = "MONTH_INCOME")
    private String monthIncome;

    /**
     * 借款人信用卡数
     */
    @Column(name = "CREDIT_NUMS")
    private String creditNums;

    /**
     * 借款人贷款笔数

     */
    @Column(name = "LOAN_NUMS")
    private String loanNums;

    /**
     * 是否有车贷
     */
    @Column(name = "HAS_CAR_LOAN")
    private String hasCarLoan;

    /**
     * 是否有房贷
     */
    @Column(name = "HAS_HOURSE_LOAN")
    private String hasHourseLoan;

    /**
     * 是否理财计划 0-否 1-是
     */
    @Column(name = "IS_PLAN")
    private String isPlan;

    /**
     * 是否华瑞建标  0-否 1-是
     */
    @Column(name = "IS_BUILD")
    private String isBuild;

    /**
     * 起息日
     */
    @Column(name = "INTEREST_START_DATE")
    private Date interestStartDate;

    /**
     * 结息日
     */
    @Column(name = "INTEREST_END_DATE")
    private Date interestEndDate;

    /**
     * 起售日
     */
    @Column(name = "SALE_START_DATE")
    private Date saleStartDate;

    /**
     * 结售日
     */
    @Column(name = "SALE_END_DATE")
    private Date saleEndDate;

    /**
     * 年化收益
     */
    @Column(name = "YEAR_RATE")
    private BigDecimal yearRate;

    /**
     * 产品星标 10-五星 9-四星半 8-四星 7-三星半 6-三星
     */
    @Column(name = "PRODUCT_RANK")
    private Short productRank;

    /**
     * 借款人负债比
     */
    @Column(name = "LIABILITIES_RATE")
    private BigDecimal liabilitiesRate;

    /**
     * 借款用途
     */
    @Column(name = "BORROW_USE")
    private String borrowUse;

    /**
     * 借款人所属行业
     */
    @Column(name = "BURROW_INDUSTRY")
    private String burrowIndustry;

    /**
     * 工作性质
     */
    @Column(name = "WORK_NATURE")
    private String workNature;

    /**
     * 还款期数
     */
    @Column(name = "REPAYMENT_TERMS")
    private Integer repaymentTerms;

    /**
     * 推标来源；WACAI:挖财
     */
    @Column(name = "CREDIT_SOURCE")
    private String creditSource;

    /**
     * 是否打包到产品；0：未打包，1：已打包
     */
    @Column(name = "IS_PACKED")
    private Integer isPacked;

    /**
     * 推送时间
     */
    @Column(name = "PUSH_TIME")
    private Date pushTime;


    /**
     * 产品本息和
     */
    @Column(name = "PRODUCT_INTEREST")
    private BigDecimal productInterest;


    public BigDecimal getProductInterest() {
        return productInterest;
    }

    public void setProductInterest(BigDecimal productInterest) {
        this.productInterest = productInterest;
    }

    /**
     * 获取编号
     *
     * @return ID - 编号
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置编号
     *
     * @param id 编号
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取标的编号
     *
     * @return SUBJECT_NO - 标的编号
     */
    public String getSubjectNo() {
        return subjectNo;
    }

    /**
     * 设置标的编号
     *
     * @param subjectNo 标的编号
     */
    public void setSubjectNo(String subjectNo) {
        this.subjectNo = subjectNo;
    }

    /**
     * 获取还款日期
     *
     * @return LAST_EXPIRE - 还款日期
     */
    public Date getLastExpire() {
        return lastExpire;
    }

    /**
     * 设置还款日期
     *
     * @param lastExpire 还款日期
     */
    public void setLastExpire(Date lastExpire) {
        this.lastExpire = lastExpire;
    }

    /**
     * 获取还款截止时间
     *
     * @return PAY_END_TIME - 还款截止时间
     */
    public String getPayEndTime() {
        return payEndTime;
    }

    /**
     * 设置还款截止时间
     *
     * @param payEndTime 还款截止时间
     */
    public void setPayEndTime(String payEndTime) {
        this.payEndTime = payEndTime;
    }

    /**
     * 获取借款人姓名

     *
     * @return BORROWER_NAME - 借款人姓名

     */
    public String getBorrowerName() {
        return borrowerName;
    }

    /**
     * 设置借款人姓名

     *
     * @param borrowerName 借款人姓名

     */
    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    /**
     * 获取PERSONAL-个人 ORGANIZATION -组织
     *
     * @return BORROWER_TYPE - PERSONAL-个人 ORGANIZATION -组织
     */
    public String getBorrowerType() {
        return borrowerType;
    }

    /**
     * 设置PERSONAL-个人 ORGANIZATION -组织
     *
     * @param borrowerType PERSONAL-个人 ORGANIZATION -组织
     */
    public void setBorrowerType(String borrowerType) {
        this.borrowerType = borrowerType;
    }

    /**
     * 获取借款人身份证号
     *
     * @return ID_NO - 借款人身份证号
     */
    public String getIdNo() {
        return idNo;
    }

    /**
     * 设置借款人身份证号
     *
     * @param idNo 借款人身份证号
     */
    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    /**
     * 获取借款目的
     *
     * @return BORROW_PURPOSE - 借款目的
     */
    public String getBorrowPurpose() {
        return borrowPurpose;
    }

    /**
     * 设置借款目的
     *
     * @param borrowPurpose 借款目的
     */
    public void setBorrowPurpose(String borrowPurpose) {
        this.borrowPurpose = borrowPurpose;
    }

    /**
     * 获取转让人名称
     *
     * @return TRANSFEROR_NAME - 转让人名称
     */
    public String getTransferorName() {
        return transferorName;
    }

    /**
     * 设置转让人名称
     *
     * @param transferorName 转让人名称
     */
    public void setTransferorName(String transferorName) {
        this.transferorName = transferorName;
    }

    /**
     * 获取转让人编号
     *
     * @return TRANSFERORID_NO - 转让人编号
     */
    public String getTransferoridNo() {
        return transferoridNo;
    }

    /**
     * 设置转让人编号
     *
     * @param transferoridNo 转让人编号
     */
    public void setTransferoridNo(String transferoridNo) {
        this.transferoridNo = transferoridNo;
    }

    /**
     * 获取借款组织机构代码
     *
     * @return ORGANIZATION_NO - 借款组织机构代码
     */
    public String getOrganizationNo() {
        return organizationNo;
    }

    /**
     * 设置借款组织机构代码
     *
     * @param organizationNo 借款组织机构代码
     */
    public void setOrganizationNo(String organizationNo) {
        this.organizationNo = organizationNo;
    }

    /**
     * 获取借款组织机构名称

     *
     * @return ORGANIZATION_NAME - 借款组织机构名称

     */
    public String getOrganizationName() {
        return organizationName;
    }

    /**
     * 设置借款组织机构名称

     *
     * @param organizationName 借款组织机构名称

     */
    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    /**
     * 获取还款方式:AVERAGE_CAPITAL_INTEREST-等额本息 ONE_CAPTITAL_INTEREST-一次性还本付息 BEFORE_INTEREST_AFTER_CAPTITAL-先息后本
     *
     * @return REPAY_TYPE - 还款方式:AVERAGE_CAPITAL_INTEREST-等额本息 ONE_CAPTITAL_INTEREST-一次性还本付息 BEFORE_INTEREST_AFTER_CAPTITAL-先息后本
     */
    public String getRepayType() {
        return repayType;
    }

    /**
     * 设置还款方式:AVERAGE_CAPITAL_INTEREST-等额本息 ONE_CAPTITAL_INTEREST-一次性还本付息 BEFORE_INTEREST_AFTER_CAPTITAL-先息后本
     *
     * @param repayType 还款方式:AVERAGE_CAPITAL_INTEREST-等额本息 ONE_CAPTITAL_INTEREST-一次性还本付息 BEFORE_INTEREST_AFTER_CAPTITAL-先息后本
     */
    public void setRepayType(String repayType) {
        this.repayType = repayType;
    }

    /**
     * 获取户名
     *
     * @return BANK_CARD_USER - 户名
     */
    public String getBankCardUser() {
        return bankCardUser;
    }

    /**
     * 设置户名
     *
     * @param bankCardUser 户名
     */
    public void setBankCardUser(String bankCardUser) {
        this.bankCardUser = bankCardUser;
    }

    /**
     * 获取账号
     *
     * @return BANK_CARD_NO - 账号
     */
    public String getBankCardNo() {
        return bankCardNo;
    }

    /**
     * 设置账号
     *
     * @param bankCardNo 账号
     */
    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    /**
     * 获取开户行
     *
     * @return BANK_NAME - 开户行
     */
    public String getBankName() {
        return bankName;
    }

    /**
     * 设置开户行
     *
     * @param bankName 开户行
     */
    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    /**
     * 获取开户支行

     *
     * @return BANK_BRANCH - 开户支行

     */
    public String getBankBranch() {
        return bankBranch;
    }

    /**
     * 设置开户支行

     *
     * @param bankBranch 开户支行

     */
    public void setBankBranch(String bankBranch) {
        this.bankBranch = bankBranch;
    }

    /**
     * 获取标的结果 0-初始 1-正常 2-流标
     *
     * @return STATUS - 标的结果 0-初始 1-正常 2-流标
     */
    public String getStatus() {
        return status;
    }

    /**
     * 设置标的结果 0-初始 1-正常 2-流标
     *
     * @param status 标的结果 0-初始 1-正常 2-流标
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 获取放款结果流水
     *
     * @return SERNO - 放款结果流水
     */
    public String getSerno() {
        return serno;
    }

    /**
     * 设置放款结果流水
     *
     * @param serno 放款结果流水
     */
    public void setSerno(String serno) {
        this.serno = serno;
    }

    /**
     * 获取流标退款 1-已退款 0-未退款 2-处理中
     *
     * @return REFUND_STATUS - 流标退款 1-已退款 0-未退款 2-处理中
     */
    public String getRefundStatus() {
        return refundStatus;
    }

    /**
     * 设置流标退款 1-已退款 0-未退款 2-处理中
     *
     * @param refundStatus 流标退款 1-已退款 0-未退款 2-处理中
     */
    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    /**
     * 获取是否满标 1-是 0-否
     *
     * @return IS_FINISH - 是否满标 1-是 0-否
     */
    public String getIsFinish() {
        return isFinish;
    }

    /**
     * 设置是否满标 1-是 0-否
     *
     * @param isFinish 是否满标 1-是 0-否
     */
    public void setIsFinish(String isFinish) {
        this.isFinish = isFinish;
    }

    /**
     * 获取是否通知满标 1-已通知 0-未通知
     *
     * @return IS_NOTIFY - 是否通知满标 1-已通知 0-未通知
     */
    public String getIsNotify() {
        return isNotify;
    }

    /**
     * 设置是否通知满标 1-已通知 0-未通知
     *
     * @param isNotify 是否通知满标 1-已通知 0-未通知
     */
    public void setIsNotify(String isNotify) {
        this.isNotify = isNotify;
    }

    /**
     * 获取已募集天数
     *
     * @return COLLECT_DAY - 已募集天数
     */
    public Long getCollectDay() {
        return collectDay;
    }

    /**
     * 设置已募集天数
     *
     * @param collectDay 已募集天数
     */
    public void setCollectDay(Long collectDay) {
        this.collectDay = collectDay;
    }

    /**
     * 获取募集金额
     *
     * @return COLLECT_AMOUNT - 募集金额
     */
    public BigDecimal getCollectAmount() {
        return collectAmount;
    }

    /**
     * 设置募集金额
     *
     * @param collectAmount 募集金额
     */
    public void setCollectAmount(BigDecimal collectAmount) {
        this.collectAmount = collectAmount;
    }

    /**
     * 获取创建时间
     *
     * @return CREATE_DATE - 创建时间
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * 设置创建时间
     *
     * @param createDate 创建时间
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * 获取标的放款时间
     *
     * @return LOAN_DATE - 标的放款时间
     */
    public Date getLoanDate() {
        return loanDate;
    }

    /**
     * 设置标的放款时间
     *
     * @param loanDate 标的放款时间
     */
    public void setLoanDate(Date loanDate) {
        this.loanDate = loanDate;
    }

    /**
     * 获取备注
     *
     * @return REMARK - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取用户编号
     *
     * @return CM_NUMBER - 用户编号
     */
    public String getCmNumber() {
        return cmNumber;
    }

    /**
     * 设置用户编号
     *
     * @param cmNumber 用户编号
     */
    public void setCmNumber(String cmNumber) {
        this.cmNumber = cmNumber;
    }

    /**
     * 获取提前还款时间
     *
     * @return AHEAD_PAY_DATE - 提前还款时间
     */
    public Date getAheadPayDate() {
        return aheadPayDate;
    }

    /**
     * 设置提前还款时间
     *
     * @param aheadPayDate 提前还款时间
     */
    public void setAheadPayDate(Date aheadPayDate) {
        this.aheadPayDate = aheadPayDate;
    }

    /**
     * 获取借款人手机号
     *
     * @return BORROWER_PHONE - 借款人手机号
     */
    public String getBorrowerPhone() {
        return borrowerPhone;
    }

    /**
     * 设置借款人手机号
     *
     * @param borrowerPhone 借款人手机号
     */
    public void setBorrowerPhone(String borrowerPhone) {
        this.borrowerPhone = borrowerPhone;
    }

    /**
     * 获取性别
     *
     * @return SEX - 性别
     */
    public String getSex() {
        return sex;
    }

    /**
     * 设置性别
     *
     * @param sex 性别
     */
    public void setSex(String sex) {
        this.sex = sex;
    }

    /**
     * 获取婚姻状况
     *
     * @return MARITAL_STATUS - 婚姻状况
     */
    public String getMaritalStatus() {
        return maritalStatus;
    }

    /**
     * 设置婚姻状况
     *
     * @param maritalStatus 婚姻状况
     */
    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    /**
     * 获取出生年月
     *
     * @return BRITHDAY - 出生年月
     */
    public String getBrithday() {
        return brithday;
    }

    /**
     * 设置出生年月
     *
     * @param brithday 出生年月
     */
    public void setBrithday(String brithday) {
        this.brithday = brithday;
    }

    /**
     * 获取所在城市
     *
     * @return CITY - 所在城市
     */
    public String getCity() {
        return city;
    }

    /**
     * 设置所在城市
     *
     * @param city 所在城市
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * 获取有车
     *
     * @return HAS_CAR - 有车
     */
    public String getHasCar() {
        return hasCar;
    }

    /**
     * 设置有车
     *
     * @param hasCar 有车
     */
    public void setHasCar(String hasCar) {
        this.hasCar = hasCar;
    }

    /**
     * 获取有房
     *
     * @return HAS_HOURSE - 有房
     */
    public String getHasHourse() {
        return hasHourse;
    }

    /**
     * 设置有房
     *
     * @param hasHourse 有房
     */
    public void setHasHourse(String hasHourse) {
        this.hasHourse = hasHourse;
    }

    /**
     * 获取行业信息
     *
     * @return TRADE_INFO - 行业信息
     */
    public String getTradeInfo() {
        return tradeInfo;
    }

    /**
     * 设置行业信息
     *
     * @param tradeInfo 行业信息
     */
    public void setTradeInfo(String tradeInfo) {
        this.tradeInfo = tradeInfo;
    }

    /**
     * 获取岗位信息
     *
     * @return POST_INFO - 岗位信息
     */
    public String getPostInfo() {
        return postInfo;
    }

    /**
     * 设置岗位信息
     *
     * @param postInfo 岗位信息
     */
    public void setPostInfo(String postInfo) {
        this.postInfo = postInfo;
    }

    /**
     * 获取借款人单位性质
     *
     * @return COMPANY_NATURE - 借款人单位性质
     */
    public String getCompanyNature() {
        return companyNature;
    }

    /**
     * 设置借款人单位性质
     *
     * @param companyNature 借款人单位性质
     */
    public void setCompanyNature(String companyNature) {
        this.companyNature = companyNature;
    }

    /**
     * 获取月收入信息
     *
     * @return MONTH_INCOME - 月收入信息
     */
    public String getMonthIncome() {
        return monthIncome;
    }

    /**
     * 设置月收入信息
     *
     * @param monthIncome 月收入信息
     */
    public void setMonthIncome(String monthIncome) {
        this.monthIncome = monthIncome;
    }

    /**
     * 获取借款人信用卡数
     *
     * @return CREDIT_NUMS - 借款人信用卡数
     */
    public String getCreditNums() {
        return creditNums;
    }

    /**
     * 设置借款人信用卡数
     *
     * @param creditNums 借款人信用卡数
     */
    public void setCreditNums(String creditNums) {
        this.creditNums = creditNums;
    }

    /**
     * 获取借款人贷款笔数

     *
     * @return LOAN_NUMS - 借款人贷款笔数

     */
    public String getLoanNums() {
        return loanNums;
    }

    /**
     * 设置借款人贷款笔数

     *
     * @param loanNums 借款人贷款笔数

     */
    public void setLoanNums(String loanNums) {
        this.loanNums = loanNums;
    }

    /**
     * 获取是否有车贷
     *
     * @return HAS_CAR_LOAN - 是否有车贷
     */
    public String getHasCarLoan() {
        return hasCarLoan;
    }

    /**
     * 设置是否有车贷
     *
     * @param hasCarLoan 是否有车贷
     */
    public void setHasCarLoan(String hasCarLoan) {
        this.hasCarLoan = hasCarLoan;
    }

    /**
     * 获取是否有房贷
     *
     * @return HAS_HOURSE_LOAN - 是否有房贷
     */
    public String getHasHourseLoan() {
        return hasHourseLoan;
    }

    /**
     * 设置是否有房贷
     *
     * @param hasHourseLoan 是否有房贷
     */
    public void setHasHourseLoan(String hasHourseLoan) {
        this.hasHourseLoan = hasHourseLoan;
    }

    /**
     * 获取是否理财计划 0-否 1-是
     *
     * @return IS_PLAN - 是否理财计划 0-否 1-是
     */
    public String getIsPlan() {
        return isPlan;
    }

    /**
     * 设置是否理财计划 0-否 1-是
     *
     * @param isPlan 是否理财计划 0-否 1-是
     */
    public void setIsPlan(String isPlan) {
        this.isPlan = isPlan;
    }

    /**
     * 获取是否华瑞建标  0-否 1-是
     *
     * @return IS_BUILD - 是否华瑞建标  0-否 1-是
     */
    public String getIsBuild() {
        return isBuild;
    }

    /**
     * 设置是否华瑞建标  0-否 1-是
     *
     * @param isBuild 是否华瑞建标  0-否 1-是
     */
    public void setIsBuild(String isBuild) {
        this.isBuild = isBuild;
    }

    /**
     * 获取起息日
     *
     * @return INTEREST_START_DATE - 起息日
     */
    public Date getInterestStartDate() {
        return interestStartDate;
    }

    /**
     * 设置起息日
     *
     * @param interestStartDate 起息日
     */
    public void setInterestStartDate(Date interestStartDate) {
        this.interestStartDate = interestStartDate;
    }

    /**
     * 获取结息日
     *
     * @return INTEREST_END_DATE - 结息日
     */
    public Date getInterestEndDate() {
        return interestEndDate;
    }

    /**
     * 设置结息日
     *
     * @param interestEndDate 结息日
     */
    public void setInterestEndDate(Date interestEndDate) {
        this.interestEndDate = interestEndDate;
    }

    /**
     * 获取起售日
     *
     * @return SALE_START_DATE - 起售日
     */
    public Date getSaleStartDate() {
        return saleStartDate;
    }

    /**
     * 设置起售日
     *
     * @param saleStartDate 起售日
     */
    public void setSaleStartDate(Date saleStartDate) {
        this.saleStartDate = saleStartDate;
    }

    /**
     * 获取结售日
     *
     * @return SALE_END_DATE - 结售日
     */
    public Date getSaleEndDate() {
        return saleEndDate;
    }

    /**
     * 设置结售日
     *
     * @param saleEndDate 结售日
     */
    public void setSaleEndDate(Date saleEndDate) {
        this.saleEndDate = saleEndDate;
    }

    /**
     * 获取年化收益
     *
     * @return YEAR_RATE - 年化收益
     */
    public BigDecimal getYearRate() {
        return yearRate;
    }

    /**
     * 设置年化收益
     *
     * @param yearRate 年化收益
     */
    public void setYearRate(BigDecimal yearRate) {
        this.yearRate = yearRate;
    }

    /**
     * 获取产品星标 10-五星 9-四星半 8-四星 7-三星半 6-三星
     *
     * @return PRODUCT_RANK - 产品星标 10-五星 9-四星半 8-四星 7-三星半 6-三星
     */
    public Short getProductRank() {
        return productRank;
    }

    /**
     * 设置产品星标 10-五星 9-四星半 8-四星 7-三星半 6-三星
     *
     * @param productRank 产品星标 10-五星 9-四星半 8-四星 7-三星半 6-三星
     */
    public void setProductRank(Short productRank) {
        this.productRank = productRank;
    }

    /**
     * 获取借款人负债比
     *
     * @return LIABILITIES_RATE - 借款人负债比
     */
    public BigDecimal getLiabilitiesRate() {
        return liabilitiesRate;
    }

    /**
     * 设置借款人负债比
     *
     * @param liabilitiesRate 借款人负债比
     */
    public void setLiabilitiesRate(BigDecimal liabilitiesRate) {
        this.liabilitiesRate = liabilitiesRate;
    }

    /**
     * 获取借款用途
     *
     * @return BORROW_USE - 借款用途
     */
    public String getBorrowUse() {
        return borrowUse;
    }

    /**
     * 设置借款用途
     *
     * @param borrowUse 借款用途
     */
    public void setBorrowUse(String borrowUse) {
        this.borrowUse = borrowUse;
    }

    /**
     * 获取借款人所属行业
     *
     * @return BURROW_INDUSTRY - 借款人所属行业
     */
    public String getBurrowIndustry() {
        return burrowIndustry;
    }

    /**
     * 设置借款人所属行业
     *
     * @param burrowIndustry 借款人所属行业
     */
    public void setBurrowIndustry(String burrowIndustry) {
        this.burrowIndustry = burrowIndustry;
    }

    /**
     * 获取工作性质
     *
     * @return WORK_NATURE - 工作性质
     */
    public String getWorkNature() {
        return workNature;
    }

    /**
     * 设置工作性质
     *
     * @param workNature 工作性质
     */
    public void setWorkNature(String workNature) {
        this.workNature = workNature;
    }

    /**
     * 获取还款期数
     *
     * @return REPAYMENT_TERMS - 还款期数
     */
    public Integer getRepaymentTerms() {
        return repaymentTerms;
    }

    /**
     * 设置还款期数
     *
     * @param repaymentTerms 还款期数
     */
    public void setRepaymentTerms(Integer repaymentTerms) {
        this.repaymentTerms = repaymentTerms;
    }

    /**
     * 获取推标来源；WACAI:挖财
     *
     * @return CREDIT_SOURCE - 推标来源；WACAI:挖财
     */
    public String getCreditSource() {
        return creditSource;
    }

    /**
     * 设置推标来源；WACAI:挖财
     *
     * @param creditSource 推标来源；WACAI:挖财
     */
    public void setCreditSource(String creditSource) {
        this.creditSource = creditSource;
    }

    /**
     * 获取是否打包到产品；0：未打包，1：已打包
     *
     * @return IS_PACKED - 是否打包到产品；0：未打包，1：已打包
     */
    public Integer getIsPacked() {
        return isPacked;
    }

    /**
     * 设置是否打包到产品；0：未打包，1：已打包
     *
     * @param isPacked 是否打包到产品；0：未打包，1：已打包
     */
    public void setIsPacked(Integer isPacked) {
        this.isPacked = isPacked;
    }

    /**
     * 获取推送时间
     *
     * @return PUSH_TIME - 推送时间
     */
    public Date getPushTime() {
        return pushTime;
    }

    /**
     * 设置推送时间
     *
     * @param pushTime 推送时间
     */
    public void setPushTime(Date pushTime) {
        this.pushTime = pushTime;
    }
}