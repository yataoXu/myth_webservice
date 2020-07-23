package com.zdmoney.models;

import com.zdmoney.common.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "BUSI_BANNER")
@Getter
@Setter
public class BusiBanner extends AbstractEntity<Long> {

    @Id
    private Long id;

    private Object maker;

    private Date makeDate;

    private Object bannerImgUrl;

    private Object bannerUrl;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    private Integer issue;

    private Object title;

    private String type;
}