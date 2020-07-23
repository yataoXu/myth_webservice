package com.zdmoney.models.financePlan;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by gaol on 2017/6/30
 **/
@Data
public class BusiDebtQueue implements Serializable{

    private static final long serialVersionUID = -539012794199391450L;

    private Long id;

    private Long productId;

    private String debtNo;

    private String cmNumber;

    private String holderType;

    private String debtType;

    private String diskNo;

    private Integer pushFlag;

    private Date createTime;

    private Date modifyTime;

    private String orderNo;
}
