package com.zdmoney.service.euroCup;

import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.zdmoney.common.ConfigParamBean;
import com.zdmoney.common.service.BaseService;
import com.zdmoney.exception.BusinessException;
import com.zdmoney.mapper.euroCup.EuroMatchScheduleMapper;
import com.zdmoney.models.customer.CustomerMainInfo;
import com.zdmoney.models.euroCup.EuroMatchSchedule;
import com.zdmoney.models.euroCup.EuroMatchStatistic;
import com.zdmoney.service.CustomerMainInfoService;
import com.zdmoney.utils.DateUtil;
import com.zdmoney.utils.LaocaiUtil;
import com.zdmoney.web.dto.euroCup.EuroGuessResultDTO;
import com.zdmoney.web.dto.euroCup.EuroMatchGuessDTO;
import com.zdmoney.web.dto.euroCup.EuroMatchScheduleDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class EuroMatchScheduleService extends BaseService<EuroMatchSchedule, Long> {


    @Autowired
    private ConfigParamBean configParamBean;

    @Autowired
    private CustomerMainInfoService customerMainInfoService;

    @Autowired
    private EuroMatchStatisticService euroMatchStatisticService;

    private EuroMatchScheduleMapper getEuroMatchScheduleMapper() {
        return (EuroMatchScheduleMapper) baseMapper;
    }

    /**
     * 竞猜页面展示
     */
    public EuroMatchGuessDTO guessMatch(String sessionToken) {
        EuroMatchGuessDTO dto = new EuroMatchGuessDTO();
        CustomerMainInfo mainInfo = null;
        if (StringUtils.isNotBlank(sessionToken)) {
            String cmNumber = LaocaiUtil.sessionToken2CmNumber(sessionToken, configParamBean.getUserTokenKey());
            mainInfo = customerMainInfoService.findOneByCmNumber(cmNumber);
            if (mainInfo != null) {
                dto.setLogin(true);
                String cellphone = mainInfo.getCmCellphone();
                if (StringUtils.isNotBlank(cellphone) && cellphone.length() >= 7) {
                    dto.setCellphone(cellphone.substring(0, 3) + "****" + cellphone.substring(7));
                }
            }
        }
        Date currentDate = new Date();
        //获取上一天比赛日期
        Date lastMatchDate = getEuroMatchScheduleMapper().getLastMatchDate(currentDate);
        if (lastMatchDate != null) {
            dto.setLastMatchDate(DateUtil.getDateFormatString(lastMatchDate, "M月d日"));
        }
        //获取下一天比赛日期
        Date nextMatchDate = getEuroMatchScheduleMapper().getNextMatchDate(currentDate);
        if (nextMatchDate != null) {
            dto.setNextMatchDate(DateUtil.getDateFormatString(nextMatchDate, "M月d日"));
            dto.setExpireDate(DateUtil.getDateFormatString(DateUtil.getDateBefore(nextMatchDate, 1), "M月d日") + "23:59");
            //获取下一天比赛记录
            List<EuroMatchScheduleDTO> euroMatchScheduleDTOs = getEuroMatchScheduleMapper().getNextScheduleList(DateUtil.getDateFormatString(nextMatchDate, "yyyyMMdd"));
            dto.setScheduleDTOs(euroMatchScheduleDTOs);
            //获取竞猜记录
            if (dto.isLogin()) {
                List matchIds = Lists.newArrayList();
                for (EuroMatchScheduleDTO euroMatchScheduleDTO : euroMatchScheduleDTOs) {
                    matchIds.add(euroMatchScheduleDTO.getId());
                }
                if (!CollectionUtils.isEmpty(matchIds)) {
                    List<EuroGuessResultDTO> euroGuessResultDTOs = euroMatchStatisticService.getGuessResultList(mainInfo.getId(), matchIds);
                    if(!CollectionUtils.isEmpty(euroGuessResultDTOs)){
                        dto.setGuessed(true);
                        dto.setEuroGuessResultDTOs(euroGuessResultDTOs);
                    }
                }
            }
        }
        return dto;
    }

    @Transactional
    public void commitMatch(String sessionToken, String guessRecord) throws Exception {
        String cmNumber = LaocaiUtil.sessionToken2CmNumber(sessionToken, configParamBean.getUserTokenKey());
        CustomerMainInfo mainInfo = customerMainInfoService.findOneByCmNumber(cmNumber);
        if (mainInfo == null) {
            throw new BusinessException("customer.not.exist");
        }
        List<EuroGuessResultDTO> list = JSONArray.parseArray(guessRecord, EuroGuessResultDTO.class);
        List<Long> matchIds = Lists.newArrayList();
        for (EuroGuessResultDTO euroGuessResultDTO : list) {
            matchIds.add(euroGuessResultDTO.getId());
        }
        List<EuroGuessResultDTO> euroGuessResultDTOs = euroMatchStatisticService.getGuessResultList(mainInfo.getId(), matchIds);
        if (!CollectionUtils.isEmpty(euroGuessResultDTOs)) {
            throw new BusinessException("euroCup.guessRecord.exist");
        }
        //验证场次的有效性
        Date currentDate = new Date();
        for (Long id : matchIds) {
            EuroMatchSchedule schedule = getEuroMatchScheduleMapper().selectByPrimaryKey(id);
            if (schedule == null) {
                throw new BusinessException("euroSchedule.info.not.exist");
            }
            if (DateUtil.getDateFormatString(currentDate, "yyyyMMdd").compareTo(DateUtil.getDateFormatString(schedule.getMatchDate(), "yyyyMMdd")) >= 0) {
                throw new BusinessException("euroSchedule.info.expire");
            }
        }
        //新增竞猜记录
        for (EuroGuessResultDTO guessResultDTO : list) {
            EuroMatchStatistic statistic = new EuroMatchStatistic();
            statistic.setCreateDate(currentDate);
            statistic.setUpdateDate(currentDate);
            statistic.setCustomerId(mainInfo.getId());
            statistic.setCustomerMobile(mainInfo.getCmCellphone());
            statistic.setGuessResult(guessResultDTO.getResult());
            statistic.setMatchId(guessResultDTO.getId());
            euroMatchStatisticService.save(statistic);
        }
    }

    public Date findLastMatchDate() {
        Date currentDate = new Date();
        //获取上一天比赛日期
        Date lastMatchDate = getEuroMatchScheduleMapper().getLastMatchDate(currentDate);
        return lastMatchDate;
    }

    public List<EuroMatchSchedule> findLastMatch(Date lastMatchDate) {
        DateTime lastMatchDateTime = new DateTime(lastMatchDate);
        Example example = new Example(EuroMatchSchedule.class);
        example.createCriteria().andCondition("to_char(MATCH_DATE,'yyyyMMdd')=", lastMatchDateTime.toString("yyyyMMdd")).andCondition("MATCH_RESULT is not null");
        example.setOrderByClause("MATCH_DATE desc");
        return baseMapper.selectByExample(example);
    }
}
