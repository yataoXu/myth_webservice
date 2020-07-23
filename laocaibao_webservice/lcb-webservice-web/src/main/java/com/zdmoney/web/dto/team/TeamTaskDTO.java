package com.zdmoney.web.dto.team;

import com.google.common.collect.Lists;
import lombok.Data;
import java.util.List;

/**
 * Created by wu.hg on 2016/4/6.
 */
@Data
public class TeamTaskDTO {
    private Long teamNum =0L;
    private Long isFulfil=0L;//是否满足团队组队要求 0-不满足 1-满足
    private Long isCaptain=0L;//是否为队长 0-非队长 1-对长
    private String teamName="";//队伍名称
    private String teamSlogan="";//队伍口号
    private Long teamId=0L;//队伍编号
    private List<TeamNewHandTaskDTO> teamNewHandTask = Lists.newArrayList();
    private List<TeamNewHandTaskDTO> teamNewHandTaskDone= Lists.newArrayList();
    private List<TeamDailyTaskDTO> teamDailyTask= Lists.newArrayList();
    private List<TeamMonthlyTaskDTO> teamMonthlyTask= Lists.newArrayList();

    private List<Long> teamMemberHeadNum = Lists.newArrayList();

}
