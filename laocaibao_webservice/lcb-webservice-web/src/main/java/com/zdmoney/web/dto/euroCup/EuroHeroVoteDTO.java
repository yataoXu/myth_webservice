package com.zdmoney.web.dto.euroCup;

import com.zdmoney.models.euroCup.EuroHeroList;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Created by 00231247 on 2016/6/13.
 */
@Getter
@Setter
public class EuroHeroVoteDTO {
    private String heroCellphone; //竞猜者手机号
    private boolean voted = false; //今日是否已投票
}
