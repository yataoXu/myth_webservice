package com.zdmoney.web.dto.euroCup;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EuroMatchScheduleDTO {
    private Long id; //ID
    private String matchType; //比赛类型
    private String homeTeam; //主队
    private String pinHome;  //主队拼音
    private String awayTeam; //客队
    private String pinAway; //客队拼音
    private String matchReward; //比赛奖品
}
