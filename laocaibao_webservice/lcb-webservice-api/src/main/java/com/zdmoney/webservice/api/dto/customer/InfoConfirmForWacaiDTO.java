package com.zdmoney.webservice.api.dto.customer;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hanxn
 * @date 2019/2/28
 */
@Setter
@Getter
public class InfoConfirmForWacaiDTO implements Serializable{

    /**
     * 借款金额
     */
    private BigDecimal borrowAmt;

    /**
     * 期限
     */
    private String borrowPeriod;

    /**
     * 利率
     */
    private String yearRate;

    /**
     * 还款方式
     */
    private String payType;

    /**
     *综合资金成本年化
     * 综合资金成本年化=（第一期综合还款金额+第二期综合还款金额+第三期综合还款金额—合同金额）/合同金额/期限*12  %
     */
    private String synthesizeCapitalCostRate;

    /**
     * 服务费金额
     */
    private BigDecimal serviceCharge;

    /**
     * 姓名
     */
    private String name;

    /**
     * 性别
     */
    private String sex;

    /**
     * 身份证号
     */
    private String cmIdnum;

    /**
     * 手机号
     */
    private String cmCellphone;

    /**
     * 出生年月
     */
    private String birthday;

    /**
     * 教育程度
     */
//    private String educationalStatus;

    /**
     * 婚姻状况
     */
//    private String maritalStatus;

    /**
     * 有无子女
     */
//    private String childrenStatus;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 居住地址
     */
    private String residentialAddr;

    /**
     * 户籍地址
     */
    private String censusregisterAddr;

//    /**
//     * 其他贷款金额
//     */
//    private BigDecimal restBorrowAmt;

    /**
     * 单位名称
     */
    private String companyName;

    /**
     * 单位性质
     */
//    private String companyNature;

    /**
     * 所属行业
     */
//    private String burrowIndustry;

    /**
     * 职位
     */
//    private String position;

    /**
     * 借款用途
     */
    private String borrowUse;

    /**
     * 所在地区
     */
    private String companyArea;

    /**
     * 单位电话
     */
//    private String companyPhone;

    /**
     * 月工资收入
     */
    private BigDecimal monthlySalary;

    /**
     * 其他收入
     */
//    private BigDecimal restIncome;

    /**
     * 联系人
     */
    private List<BorrowerContactsDTO> contactsDtos = new ArrayList();


    /**
     * 贷款用途
     */
    private String loansUse;

    /**
     * 挖财确认url
     */
    private String wacaiConfirmUrl;

    /**
     * 挖财编辑url
     */
    private String wacaiEditorUrl;

    /**
     * 系统时间
     */
    private String sysDateStr;

    /**
     * 单位地址
     */
//    private String companyAddr;
    /**
     * 就业状态
      */
    private String employment;

    /**
     * 银行卡号
     */
    private String bankCardNo;


}
