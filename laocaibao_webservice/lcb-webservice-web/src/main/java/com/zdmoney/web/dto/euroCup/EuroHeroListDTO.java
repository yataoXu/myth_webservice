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
public class EuroHeroListDTO {
    private MyHero myHero; //我的英雄榜详情
    private List<Hero> heroList; //英雄榜
}
