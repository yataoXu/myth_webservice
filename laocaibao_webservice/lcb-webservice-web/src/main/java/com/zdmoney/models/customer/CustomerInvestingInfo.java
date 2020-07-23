package com.zdmoney.models.customer;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by user on 2019/1/3.
 */
@Data
public class CustomerInvestingInfo {
    private BigDecimal amt;
    private Long id;
    private String cmNumber;
    private Integer memberLevel;
    private String realName;
    private String cellphone;
    private String openId;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date ratingChangingDate;
}
