package com.zdmoney.models.customer;

import com.zdmoney.common.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


@Table(name = "CUSTOMER_SIGN")
@Setter
@Getter
public class CustomerSign extends AbstractEntity<Long>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select SEQ_CUSTOMER_SIGN.nextval from dual")
    private Long id;

    private String cmNumber;

    private Date signDate;
    
    private Integer integral;

    private Long    coin;

    private Integer signTime;

}