package com.zdmoney.models;

import com.alibaba.fastjson.annotation.JSONField;
import com.zdmoney.common.entity.AbstractEntity;
import lombok.Data;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "BUSI_ORDER_TO_PAY")
@Data
public class BusiOrderToPay extends AbstractEntity<Long>{

    @Id
    private Long id;

    private String orderNo;//订单编号

    private Long productId;//产品编号

    private BigDecimal principalInterest;//本息和（待结算）

    @JSONField(format = "yyyy-MM-dd")
    private Date exitDate;


}