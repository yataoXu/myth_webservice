package com.zdmoney.service.euroCup;

import com.google.common.collect.Lists;
import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.common.service.BaseService;
import com.zdmoney.mapper.euroCup.EuroHeroListMapper;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.euroCup.EuroHeroList;
import com.zdmoney.service.CustomerMainInfoService;
import com.zdmoney.utils.LaocaiUtil;
import com.zdmoney.web.dto.euroCup.EuroHeroListDTO;
import com.zdmoney.web.dto.euroCup.Hero;
import com.zdmoney.web.dto.euroCup.MyHero;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

/**
 * Created by 00231247 on 2016/6/13.
 */
@Service
@Slf4j
public class EuroHeroListService extends BaseService<EuroHeroList, Long> {

    @Autowired
    private ConfigParamBean configParamBean;

    @Autowired
    private CustomerMainInfoService customerMainInfoService;

    @Autowired
    private EuroMatchStatisticService euroMatchStatisticService;

    private EuroHeroListMapper getEuroHeroListMapper() {
        return (EuroHeroListMapper) baseMapper;
    }

    public EuroHeroListDTO getEuroHeroList(String sessionToken){
        EuroHeroListDTO euroHeroListDTO = new EuroHeroListDTO();
        //英雄榜
        Example listExample = new Example(EuroHeroList.class);
        listExample.setOrderByClause("vote_num desc,GUESS_NUM desc,update_date asc");
        List<EuroHeroList> heroList = baseMapper.selectByExample(listExample);
        List<Hero> heros = Lists.newArrayList(); //英雄榜
        for (EuroHeroList hero:heroList) {
            Hero he = new Hero();
            String cellphone = hero.getCustomerMobile().toString();
            he.setCustomerMobile(cellphone.substring(0, 3) + "****" + cellphone.substring(7));
            he.setGuessNum(hero.getGuessNum());
            he.setVoteNum(hero.getVoteNum());
            heros.add(he);
        }
        euroHeroListDTO.setHeroList(heros);

        //我的英雄榜详情
        MyHero myHero = new MyHero();
        CustomerMainInfo mainInfo = null;
        if (StringUtils.isNotBlank(sessionToken)) {
            String cmNumber = LaocaiUtil.sessionToken2CmNumber(sessionToken, configParamBean.getUserTokenKey());
            mainInfo = customerMainInfoService.findOneByCmNumber(cmNumber);
            if (mainInfo != null) {
                myHero.setLogin(true);
                String cellphone = mainInfo.getCmCellphone();
                myHero.setCellPhone(cellphone);
                Example myExample = new Example(EuroHeroList.class);
                myExample.createCriteria().andCondition("customer_mobile=",cellphone);
                List<EuroHeroList> list= baseMapper.selectByExample(myExample);
                if(list.size()>0){
                    myHero.setHero(true);
                    EuroHeroList myHeroList = list.get(0);
                    myHero.setGuessNum(myHeroList.getGuessNum());
                    myHero.setVoteNum(myHeroList.getVoteNum());
                    for (int i=0;i<heroList.size();i++){
                        if(myHeroList.getCustomerMobile().equals(heroList.get(i).getCustomerMobile())){
                            myHero.setRanking(i+1);
                            break;
                        }
                    }
                    EuroHeroList hero1 = heroList.get(0);
                    EuroHeroList hero14 = heroList.get(13);
                    myHero.setDistanceAwardNum(hero14.getVoteNum()>myHeroList.getVoteNum()?hero14.getVoteNum()-myHeroList.getVoteNum():0);
                    myHero.setDistanceEuropeanTourNum(hero1.getVoteNum()>myHeroList.getVoteNum()?hero1.getVoteNum()-myHeroList.getVoteNum():0);
                }else{ //
                    myHero.setGuessNum(euroMatchStatisticService.getGuessNumByCellphone(cellphone));
                }
            }
        }
        euroHeroListDTO.setMyHero(myHero);
        return euroHeroListDTO;
    }

    public void addVote(String heroPhone){
        Example heroExample = new Example(EuroHeroList.class);
        heroExample.createCriteria().andCondition("customer_mobile=",heroPhone);
        List<EuroHeroList> list= baseMapper.selectByExample(heroExample);
        if(list.size()>0){
            EuroHeroList hero = list.get(0);
            hero.setVoteNum(hero.getVoteNum()+1);
            hero.setUpdateDate(new Date());
            baseMapper.updateByPrimaryKey(hero);
        }
    }

}
