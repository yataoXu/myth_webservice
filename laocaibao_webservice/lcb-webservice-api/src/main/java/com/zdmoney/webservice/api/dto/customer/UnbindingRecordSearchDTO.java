package com.zdmoney.webservice.api.dto.customer;

import com.zdmoney.webservice.api.common.dto.PageSearchDto;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by user on 2017/9/5.
 */
@Getter
@Setter
public class UnbindingRecordSearchDTO extends PageSearchDto{

    private String cmCellphone;

    private String cmRealName;

    private String cmIdnum;

    private String startDate;

    private String endDate;

    private String cbBankCode;

}
