package com.zdmoney.web.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
public class EstimateDto {

   /*转让日期*/
   @JSONField(format = "yyyy-MM-dd")
   private Date date;

    /*产品估值*/
   private String estimatePrice;

    private String minpv; //允许转让价格范围 下限

    private String maxpv; //允许转让价格范围 上限
}
