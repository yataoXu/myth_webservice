package com.zdmoney.webservice.api.dto.finance;

import com.zdmoney.webservice.api.common.dto.PageSearchDto;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by user on 2019/3/4.
 */
@Data
public class QueryOrderReqDto extends PageSearchDto implements Serializable{

    private String creditSource;

    private String parentNo;

    private String orderNum;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;//下单开始时间

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;//下单结束时间

    private Long productId;

    private String tenderStatus;

    private String productType;

    private String subjectNo;

    private List<String> statusList;
}
