package com.zdmoney.models.financePlan;

import com.zdmoney.models.order.BusiOrder;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by gaol on 2017/6/22
 **/
@Data
public class BusiOrderSub extends BusiOrder implements Serializable {

    //private Integer exitCheckStatus; // 提前退出审核状态 0：默认不提前退出 1：提前退出

    //private Date exitCheckDate;//提前退出审核时间

    //private Date exitClickDate;//提前退出申请时间（app，pc）

    /**
     * 标的编号
     */
    private String subjectNo;

    //private Date modifyDate;

    private String transferNo;

    private Date borrowerDate;

    private Long parentId;

    private String debtType;

    private String debtNo;

    private String cmNumber;

    private String subOrderStatus;

    private String productName;

    private String parentNo;

    private Long planId;

    /*private String inviteCode;*/

    private String transferSerialNo;

    private String refundSerialNo;

    private String loginId;

    /*private Integer productType;

    private BigDecimal couponAmount;

    private BigDecimal integralAmount;

    private BigDecimal cashAmount;*/

    private String customerCmNumber;

}
