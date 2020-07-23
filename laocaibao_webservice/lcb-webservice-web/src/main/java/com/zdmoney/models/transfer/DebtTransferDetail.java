package com.zdmoney.models.transfer;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class DebtTransferDetail implements Serializable {

    //转让人姓名
    private String transferName;
    //转让人idNum
    private String transferIdNum;
    //购买人姓名
    private String buyName;
    //购买人idNum
    private String buyIdNum;
    //借款人Id
    private String debtorId;
    //借款人Num
    private String debtorNum;
    //借款人name
    private String debtorName;
    //借款人Phone
    private String debtorPhone;
    //转让金额
    private String transferPrice;
    //转让时间
    private Date tradeDate;

}
