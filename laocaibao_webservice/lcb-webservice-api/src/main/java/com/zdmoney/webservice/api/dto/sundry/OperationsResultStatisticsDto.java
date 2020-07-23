package com.zdmoney.webservice.api.dto.sundry;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by user on 2018/1/17.
 */
@Data
public class OperationsResultStatisticsDto implements Serializable {
    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM")
    private Date analysisTime;//数据统计时间
    private String cumulativeLoanAmount;//累计交易金额
    private String cumulativeLoanCount;//累计借贷笔数
    private String creditBalance;//借贷余额
    private String creditBalanceCount;//借贷余额笔数
    private String intrestBalance;//利息余额
    private String cumulativeBorrowersAmount;//累计借款人数量
    private String currentBorrowersAmount;//当前借款人数量
    private String topTenBorrowersDebtAmount;//前十大借款人待还金额占比
    private String biggestLoanDebtAmount;//最大单一借款人待还金额占比
    private String relatedCreditBalance;//关联关系借款余额
    private String relatedCreditBalanceCount;//关联关系借款笔数
    private String cumulativeLendersAmount;//累计出借人数量
    private String currentLendersAmount;//当前出借人数量
    private String perCapitaLendAmount;//人均累计出借金额
    private String biggestLoanBalanceAmount;//最大单户出借余额占比
    private String topTenBalanceAmount;//最大十户出借余额占比
    private String overdueAmount;//逾期金额
    private String overdueDealsCount;//逾期笔数
    private String overdueProjectsPercent;//项目逾期率
    private String overduePercent;//金额逾期率
    private String over90daysOverdueAmount;//逾期90天（不含）以上金额
    private String over90daysOverdueCount;//逾期90天（不含）以上笔数
    private String cumulativeSubstituteAmount;//累计代偿金额
    private String cumulativeSubstituteCount;//累计代偿笔数
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createDate;//创建时间
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date modifyDate;//修改时间
    private String serviceCharge;//借款人平台服务费
    private String transferServiceCharge;//出借人债权转让服务费
    private String customerReg;//用户注册
    private String recharge;//充值
    private String depositAccount;//开通存管账户
    private String repayment;//回款到账
    private String withdraw;//提现
    private String lenderProductServiceCharge;//出借人产品服务费
    private String accumulativeLenderNum;//累计出借笔数
    private String accumulatedLoanAmount; //累计借贷金额
}
