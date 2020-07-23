package com.zdmoney.web.dto.euroCup;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by 00231247 on 2016/6/13.
 */
@Getter
@Setter
public class MyHero {
    private String cellPhone;
    private int guessNum; //猜中次数
    private int voteNum;  //票数
    private int ranking;  //排名
    private boolean login = false;  //是否登陆
    private boolean hero = false;  //是否入围英雄榜
    private int distanceAwardNum;  //距离获奖票数
    private int distanceEuropeanTourNum;  //距离欧洲游票数

}
