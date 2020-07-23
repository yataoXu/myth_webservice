package com.zdmoney.webservice.api.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created by rui on 15/8/24.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BaseDto extends SerialDto {

    protected String id;

    protected Date createTime;

    protected Date modifyTime;

    protected String operator;
}
