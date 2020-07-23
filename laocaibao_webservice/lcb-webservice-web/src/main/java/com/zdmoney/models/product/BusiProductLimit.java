package com.zdmoney.models.product;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "BUSI_PRODUCT_LIMIT")
@Setter
@Getter
public class BusiProductLimit {
    @Id
    @SequenceGenerator(name="",sequenceName="Oracle")
    private Long id;

    private String name;

    private Date startDate;

    private Date endDate;

    private String remark;

    private Long type;
}