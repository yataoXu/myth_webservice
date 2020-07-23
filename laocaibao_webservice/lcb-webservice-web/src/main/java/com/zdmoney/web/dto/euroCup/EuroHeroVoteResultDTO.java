package com.zdmoney.web.dto.euroCup;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by 00231247 on 2016/6/13.
 */
@Getter
@Setter
public class EuroHeroVoteResultDTO {
    private boolean register = true;  //投票人是否已注册
    private String voterSessionToken;  //投票人sessionToken
}
