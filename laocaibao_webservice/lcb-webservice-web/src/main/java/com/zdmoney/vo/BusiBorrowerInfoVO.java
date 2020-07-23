package com.zdmoney.vo;

import lombok.Data;

/**
 * Created by user on 2016/11/15.
 */
@Data
public class BusiBorrowerInfoVO {

    /**
     * 借款人身份证号
     */
    private String idNo;

    /**
     * 借款人手机号
     */
    private String borrowerPhone;

    /**
     * 标的编号
     */
    private String subjectNo;
}
