package com.zdmoney.webservice.api.dto.product;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 描述 :推标信息
 *
 * @author : weiNian
 * @create : 2019-03-01 15:57
 * @Mail: wein@zendaimoney.com
 **/
@Data
public class BusiSubjectDto implements Serializable {

    /**
     * 标的编号
     */
    @NotNull(message = "标的编号不能为空")
    private String subjectNo;

    /**
     * 借款人姓名
     */
    @NotNull(message = "借款人姓名不能为空")
    private String borrowerName;

    /**
     * PERSONAL-个人 ORGANIZATION -组织
     */
    @NotNull(message = "借款人类型不能为空")
    private String borrowerType;

    /**
     * 借款人身份证号
     */
    @NotNull(message = "借款人身份证号不能为空")
    private String idNo;

    /**
     * 还款方式:AVERAGE_CAPITAL_INTEREST-等额本息 ONE_CAPTITAL_INTEREST-一次性还本付息 BEFORE_INTEREST_AFTER_CAPTITAL-先息后本
     */
    @NotNull(message = "还款方式不能为空")
    private String repayType;

    /**
     * 募集金额
     */
    @NotNull(message = "募集金额不能为空")
    private BigDecimal collectAmount;

    /**
     * 创建时间
     */
    @NotNull(message = "创建时间不能为空")
    private Date createDate;

    /**
     * 用户编号
     */
//    @NotNull(message = "用户编号不能为空")
    private String cmNumber;

    /**
     * 起息日
     */
    @NotNull(message = "起息日不能为空")
    private Date interestStartDate;

    /**
     * 结息日
     */
    @NotNull(message = "结息日不能为空")
    private Date interestEndDate;

    /**
     * 起售日
     */
    @NotNull(message = "起售日不能为空")
    private Date saleStartDate;

    /**
     * 结售日
     */
    @NotNull(message = "结售日不能为空")
    private Date saleEndDate;

    /**
     * 年化收益
     */
    @NotNull(message = "年化收益不能为空")
    private BigDecimal yearRate;

    /**
     * 还款期数
     */
    @NotNull(message = "还款期数不能为空")
    private Integer repaymentTerms;


    /**
     * 借款人负债比
     */
    @NotNull(message = "借款人负债比不能为空")
    private BigDecimal liabilitiesRate;


    /**
     * 标的本息和
     */
    @NotNull(message = "标的本息和不能为空")
    private BigDecimal productInterest;

    /**
     * 借款用途
     */
    @NotNull(message = "借款用途不能为空")
    private String borrowUse;



    /**
     * 还款日期
     */
    private Date lastExpire;

    /**
     * 还款截止时间
     */
    private String payEndTime;

    /**
     * 借款目的
     */
    private String borrowPurpose;

    /**
     * 转让人名称
     */
    private String transferorName;

    /**
     * 转让人编号
     */
    private String transferoridNo;

    /**
     * 借款组织机构代码
     */
    private String organizationNo;

    /**
     * 借款组织机构名称

     */
    private String organizationName;

    /**
     * 户名
     */
    private String bankCardUser;

    /**
     * 账号
     */
    private String bankCardNo;

    /**
     * 开户行
     */
    private String bankName;

    /**
     * 开户支行

     */
    private String bankBranch;

    /**
     * 标的结果 0-初始 1-正常 2-流标
     */
    private String status;

    /**
     * 放款结果流水
     */
    private String serno;

    /**
     * 流标退款 1-已退款 0-未退款 2-处理中
     */
    private String refundStatus;

    /**
     * 是否满标 1-是 0-否
     */
    private String isFinish;

    /**
     * 是否通知满标 1-已通知 0-未通知
     */
    private String isNotify;

    /**
     * 已募集天数
     */
    private Long collectDay;



    /**
     * 标的放款时间
     */
    private Date loanDate;

    /**
     * 备注
     */
    private String remark;


    /**
     * 提前还款时间
     */
    private Date aheadPayDate;

    /**
     * 借款人手机号
     */
    private String borrowerPhone;

    /**
     * 性别
     */
    private String sex;

    /**
     * 婚姻状况
     */
    private String maritalStatus;

    /**
     * 出生年月
     */
    private String brithday;

    /**
     * 所在城市
     */
    private String city;

    /**
     * 有车
     */
    private String hasCar;

    /**
     * 有房
     */
    private String hasHourse;

    /**
     * 行业信息
     */
    private String tradeInfo;

    /**
     * 岗位信息
     */
    private String postInfo;

    /**
     * 借款人单位性质
     */
    private String companyNature;

    /**
     * 月收入信息
     */
    private String monthIncome;

    /**
     * 借款人信用卡数
     */
    private String creditNums;

    /**
     * 借款人贷款笔数

     */
    private String loanNums;

    /**
     * 是否有车贷
     */
    private String hasCarLoan;

    /**
     * 是否有房贷
     */
    private String hasHourseLoan;

    /**
     * 是否理财计划 0-否 1-是
     */
    private String isPlan;

    /**
     * 是否华瑞建标  0-否 1-是
     */
    private String isBuild;

    /**
     * 产品星标 10-五星 9-四星半 8-四星 7-三星半 6-三星
     */
    private Short productRank;


    /**
     * 借款人所属行业
     */
    private String burrowIndustry;

    /**
     * 工作性质
     */
    private String workNature;

}
