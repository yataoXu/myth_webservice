package com.zdmoney.webservice.api.dto.plan;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gaol on 2017/7/28
 **/
@Data
public class BuyBackDTO implements Serializable{

    private String subjectNo;

    private String specialCmNumber;

    private List<BuyBackOrder> buyBackOrderList;

}

