package com.zdmoney.web.dto.euroCup;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by 00231247 on 2016/6/15.
 */
@Getter
@Setter
public class Hero {
    private String customerMobile;
    private int guessNum; //猜中次数
    private int voteNum;  //票数
}
