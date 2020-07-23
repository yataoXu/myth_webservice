package com.zdmoney.webservice.api.dto.customer;


import lombok.Data;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.io.Serializable;


@Data
public class CustomerBorrowInfoDto implements Serializable {

    @NotBlank(message="用户编号不能为空!")
    private String cmNumber;//用户编号

    @NotNull(message = "其它网贷平台借款情况不能为空")
    private Long thdFlag;//其他网贷平台借款情况 0-无 1-有

    private String thdInfo="无";//其他借款平台借款详情

}