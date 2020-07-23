package com.zdmoney.webservice.api.dto.plan;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by yangp on 2017/11/17
 **/
@Data
public class MatchFlowVO implements Serializable {

    private Long id;

    private String capitalCode;

    private BigDecimal capitalAmount;

    private String capitalType;

    private String ledgerId;

    private String priority;

    private String financeId;

    private String status;

    private String totalTerm;

    private Date createTime;

    private BigDecimal earningsRate;

    private String productCode;

    private String subjectNo;

    private String subjectAmt;

    private String loanCustomerNo;

    private String loanCustomerName;

    private String debtType;

    private String initOrderNum;

    private String matchOrderCode;

    private String manFinanceId;

    private Date borrowerDate;

    private BigDecimal debtWorth;

    private BigDecimal interest;

    private Long masterId;

    private String operStatus;

    private int financeNum;

    private int transferNum;

    private String batchNo;
}
