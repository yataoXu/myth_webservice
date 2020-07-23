package com.zdmoney.webservice.api.dto.busi;

import com.zdmoney.webservice.api.common.dto.PageSearchDto;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by 46186 on 2018/6/20.
 */
@Data
public class BusiBorrowCertificateSearchDto extends PageSearchDto {
    private String cmName;
    private String cellphone;
    private String startTime;
    private String endTime;
    private String origin;
}
