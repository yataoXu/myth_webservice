package com.zdmoney.models.product;

import com.zdmoney.common.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "BUSI_PRODUCT_RULE")
@Getter
@Setter
public class BusiProductRule extends AbstractEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select SEQ_BUSI_PRODUCT_RULE.nextval from dual")
    private Long id;

    private String ruleName;

    private String platform;

    private String memberType;

    private String channel;

    private String welfare;

    private String status;

    private String productType;

    private Date createTime;

    private Date auditTime;


}