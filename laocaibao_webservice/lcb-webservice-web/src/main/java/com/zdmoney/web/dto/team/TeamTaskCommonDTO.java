package com.zdmoney.web.dto.team;

import lombok.Data;

@Data
public class TeamTaskCommonDTO {
    private String taskType = "";
    private String taskName = "";
    private String actionType = "";
    private String flowId = "";
    private String awardType="";
}