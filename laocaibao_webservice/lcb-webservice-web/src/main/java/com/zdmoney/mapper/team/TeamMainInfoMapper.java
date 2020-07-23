package com.zdmoney.mapper.team;

import com.zdmoney.common.mybatis.mapper.JdMapper;
import com.zdmoney.models.customer.AnnualBill;
import com.zdmoney.models.team.TeamMainInfo;
import com.zdmoney.web.dto.team.TeamListDTO;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface TeamMainInfoMapper extends JdMapper<TeamMainInfo, Long> {
    List<TeamListDTO> getTeamList(Map<String,Object> map);

    List<TeamListDTO> getTeamRankList(Map<String,Object> map);

    AnnualBill queryAnnualBill2016(Long customerId);

    List<TeamMainInfo> findTeamMainInfo(Map<String,Object> map);
}