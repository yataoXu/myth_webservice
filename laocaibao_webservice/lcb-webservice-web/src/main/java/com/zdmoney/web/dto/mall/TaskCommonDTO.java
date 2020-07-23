package com.zdmoney.web.dto.mall;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by 00225181 on 2016/2/26.
 */
@Getter
@Setter
public class TaskCommonDTO {
    private String taskType = "";
    private String taskName = "";
    private Long lcbAmt = 0l;
    private String actionType = "";
    private String flowId = "";
}