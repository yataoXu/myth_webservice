package com.zdmoney.vo;

import com.zdmoney.models.customer.CustRatingChangingRecord;
import lombok.Data;

/**
 * Created by user on 2019/1/8.
 */
@Data
public class CustRatingChangingVo extends CustRatingChangingRecord {

    private String customerName;

    private String cellphone;

    private String openId;
}
