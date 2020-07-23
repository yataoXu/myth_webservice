package com.zdmoney.web;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Created by user on 2018/9/25.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransferResultDTO {

    private String transferSerialNo;

    private String feeInSerialNo;

    private String feeOutSerialNo;
}
