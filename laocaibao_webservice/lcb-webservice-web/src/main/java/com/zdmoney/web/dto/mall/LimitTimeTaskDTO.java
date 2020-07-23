package com.zdmoney.web.dto.mall;

import com.alibaba.fastjson.annotation.JSONField;
import com.zdmoney.service.JsonDateYMDSerializer2;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by 00225181 on 2016/2/26.
 */
@Getter
@Setter
public class LimitTimeTaskDTO extends TaskCommonDTO {
    private BigDecimal investAmt;//单笔投资金额
    private Long investPeriod;//单笔投资期限下限
    private Long investUpperPeriod;//单笔投资期限上限
    @JSONField(format = "yyyy-MM-dd")
    private Date taskStartDate;//任务开始时间
    @JSONField(format = "yyyy-MM-dd")
    private Date taskEndDate;//任务结束时间
    private int limitType = 1;//1:单笔，2:累计
}
