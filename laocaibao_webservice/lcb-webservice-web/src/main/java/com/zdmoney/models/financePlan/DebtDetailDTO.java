package com.zdmoney.models.financePlan;

import com.zdmoney.constant.ParamConstant;
import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by gaol on 2017/6/6
 **/
@Data
public class DebtDetailDTO implements Serializable {


    private Date borrowerDate;
    /**
     * 主产品ID
     */
    private Long mainProductId;

    /**
     * 债权编号
     */
    private String debtNo;

    /**
     * 状态
     * 1-待撮合 2-暂停撮合 3-已撮合 4-提前结清 5-异常 6-结标
     */
    private Integer status;

    /**
     * 还款方式
     */
    private String paymentManner;

    /**
     * 是否是理财人转让
     * 0: 否   1: 是
     */
    private String isTransfer;

    /**
     * 最小债权价值
     */
    private BigDecimal minDebtPrice;

    /**
     * 最大债权价值
     */
    private BigDecimal maxDebtPrice;

    /**
     * 开始日期
     */
    private Date launchStartDate;

    /**
     * 结束日期
     */
    private Date launchEndDate;

    /**
     * 还款日 开始日期
     */
    private Date paymentStartDate;


    /**
     * 原标的编号
     */
    private String initSubjectNo;

    /**
     * 剩余期限
     */
    private Long restDays;

    /**
     * 下一个还款日
     */
    private Date nextPayDate;


    /**
     * 类型 1-新标的 2-退出转让
     */
    private String debtType;

    /**
     * 优先级
     */
    private String priority;

    /**
     * 债权价值
     */
    private BigDecimal debtPrice;

    /**
     * 原始债权利率
     */
    private double initRate;

    /**
     * 当前持有人
     */
    private String currHolder;

    /**
     * 原持有人
     */
    private String initHolder;

    /**
     * 借款人编号
     */
    private String borrowerNumber;

    /**
     * 借款人身份证号
     */
    private String borrowerIdNum;


    /**
     * 还款日 结束日期
     */
    private Date paymentEndDate;

    private int pageSize = ParamConstant.PAGESIZE;

    private int pageNo = 1;
}
