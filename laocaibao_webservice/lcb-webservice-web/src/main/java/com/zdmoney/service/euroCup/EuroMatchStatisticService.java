package com.zdmoney.service.euroCup;

import com.google.common.collect.Maps;
import com.zdmoney.common.service.BaseService;
import com.zdmoney.mapper.euroCup.EuroMatchStatisticMapper;
import com.zdmoney.models.euroCup.EuroMatchStatistic;
import com.zdmoney.web.dto.euroCup.EuroGuessResultDTO;
import com.zdmoney.web.dto.euroCup.OneDayWinner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class EuroMatchStatisticService extends BaseService<EuroMatchStatistic, Long> {


    @Autowired
    private EuroMatchScheduleService euroMatchScheduleService;

    private EuroMatchStatisticMapper getEuroMatchStatisticMapper() {
        return (EuroMatchStatisticMapper) baseMapper;
    }

    /**
     * 查询竞猜记录
     * @param cmId
     * @param matchIds
     * @return
     */
    public List<EuroGuessResultDTO> getGuessResultList(Long  cmId,List<Long> matchIds) {
        Map map = Maps.newTreeMap();
        map.put("cmNumber",cmId);
        map.put("matchIds",matchIds);
        return getEuroMatchStatisticMapper().getGuessList(map);
    }

    public List<OneDayWinner> previousGameWinner(Date lastMatchDate,String cellPhone){
        DateTime lastMatchDateTime = new DateTime(lastMatchDate);
        List<OneDayWinner> winners = getEuroMatchStatisticMapper().findWinner(lastMatchDateTime.toString("yyyyMMdd"));
        if(!winners.isEmpty()){
            for(OneDayWinner winner : winners){
                if(!StringUtils.isEmpty(cellPhone) && cellPhone.equals(winner.getCellPhone())){
                    winner.setMyself(true);
                }
                String phone = winner.getCellPhone();
                if(!StringUtils.isEmpty(phone)){
                    winner.setCellPhone(phone.substring(0, 3) + "****" + phone.substring(7));
                }
            }
        }
        return winners;
    }

    public int getGuessNumByCellphone(String cellphone){
        Example example = new Example(EuroMatchStatistic.class);
        example.createCriteria().andCondition("customer_mobile="+cellphone).andCondition("is_guess_right = 0");
        return baseMapper.selectByExample(example).size();
    }

}
