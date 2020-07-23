package com.zdmoney.models.product;

import com.zdmoney.common.handler.SecurityFieldTypeHandler;
import lombok.Getter;
import lombok.Setter;
import tk.mybatis.mapper.annotation.ColumnType;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "BUSI_PRODUCT_CONTRACT")
@Getter
@Setter
public class BusiProductContract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select SEQ_BUSI_PRODUCT_CONTRACT.nextval from dual")
    private Long id;

    @Column(name = "SUBJECT_NO")
    private String subjectNo;

    @Column(name = "LAST_EXPIRE")
    private Date lastExpire;

    @Column(name = "PAY_END_TIME")
    private String payEndTime;

    @Column(name = "BORROWER_NAME")
    private String borrowerName;

    @Column(name = "BORROWER_TYPE")
    private String borrowerType;

    @Column(name = "ID_NO")
    @ColumnType(typeHandler = SecurityFieldTypeHandler.class)
    private String idNo;

    @Column(name = "BORROW_PURPOSE")
    private String borrowPurpose;

    @Column(name = "TRANSFEROR_NAME")
    private String transferorName;

    @Column(name = "TRANSFERORID_NO")
    @ColumnType(typeHandler = SecurityFieldTypeHandler.class)
    private String transferoridNo;

    @Column(name = "ORGANIZATION_NO")
    private String organizationNo;

    @Column(name = "ORGANIZATION_NAME")
    private String organizationName;

    @Column(name = "REPAY_TYPE")
    private String repayType;

    @Column(name = "BANK_CARD_USER")
    private String bankCardUser;

    @Column(name = "BANK_CARD_NO")
    @ColumnType(typeHandler = SecurityFieldTypeHandler.class)
    private String bankCardNo;

    @Column(name = "BANK_NAME")
    private String bankName;

    @Column(name = "BANK_BRANCH")
    private String bankBranch;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "SERNO")
    private String serno;

    @Column(name = "REFUND_STATUS")
    private String refundStatus;

    @Column(name = "IS_NOTIFY")
    private String isNotify;

    @Column(name = "COLLECT_AMOUNT")
    private BigDecimal collectAmount;

    @Column(name = "CREATE_DATE")
    private Date createDate;

    @Column(name = "REMARK")
    private String remark;

    @Column(name = "IS_FINISH")
    private String isFinish;

    @Column(name="LOAN_DATE")
    private Date loanDate;

    @Column(name="CM_NUMBER")
    private String cmNumber;

    @Column(name="ahead_pay_date")
    private Date aheadPayDate;

    @Column(name="borrower_phone")
    @ColumnType(typeHandler = SecurityFieldTypeHandler.class)
    private String borrowerPhone;

    @Column(name="sex")
    private String sex; //性别

    @Column(name="marital_status")
    private String maritalStatus; //婚姻状况

    @Column(name="brithday")
    private String brithday; //出生年份

    @Column(name="city")
    private String city; //所在城市
    @Column(name="has_car")
    private String hasCar; //是否有车
    @Column(name="HAS_HOURSE")
    private String hasHourse; //是否房产
    @Column(name="TRADE_INFO")
    private String tradeInfo; //行业信息

    @Column(name="POST_INFO")
    private String postInfo; //岗位信息

    @Column(name="COMPANY_NATURE")
    private String companyNature; //单位性质

    @Column(name="MONTH_INCOME")
    private String monthIncome; //月收入信息

    @Column(name="CREDIT_NUMS")
    private String creditNums; //信用卡张数

    @Column(name="LOAN_NUMS")
    private String loanNums; //贷款笔数

    @Column(name="HAS_CAR_LOAN")
    private String hasCarLoan; //有无车贷

    @Column(name="HAS_HOURSE_LOAN")
    private String hasHourseLoan; //有无房贷

    @Column(name="is_plan")
    private String isPlan; //理财计划 0-否 1-是

    @Column(name="IS_BUILD")
    private String isBuild; //华瑞建标 0-否 1-是

    @Column(name="INTEREST_START_DATE")
    private Date interestStartDate; //起息日

    @Column(name="INTEREST_END_DATE")
    private Date interestEndDate; //结息日

    @Column(name="SALE_START_DATE")
    private Date saleStartDate; //起售日

    @Column(name="SALE_END_DATE")
    private Date saleEndDate; //结售日

    @Column(name="YEAR_RATE")
    private BigDecimal yearRate;

    @Column(name="PRODUCT_RANK")
    private BigDecimal productRank;//产品星标 10-五星 9-四星半 8-四星 7-三星半 6-三星

    @Column(name="LIABILITIES_RATE")
    private BigDecimal liabilitiesRate;//借款人负债比

    @Column(name="BORROW_USE")
    private String borrowUse; //借款用途

    @Column(name="BURROW_INDUSTRY")
    private String burrowIndustry; //借款人所属行业

    @Column(name="WORK_NATURE")
    private String workNature; //借款人所属行业

    @Column(name="repayment_terms")
    private Integer repaymentTerms; //还款期数

    @Column(name = "PRODUCT_INTEREST")
    private BigDecimal productInterest;

    @Column(name = "CREDIT_SOURCE")
    private String creditSource;

}