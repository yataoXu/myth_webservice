package com.zdmoney.web.dto;

import java.util.List;

/**
 * Created by 00225181 on 2015/11/12.
 */
public class ShareRecordDTO {
    List<CouponShareDto> detail;

    public List<CouponShareDto> getDetail() {
        return detail;
    }

    public void setDetail(List<CouponShareDto> detail) {
        this.detail = detail;
    }
}
