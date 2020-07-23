package com.zdmoney.web.dto.euroCup;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class EuroMatchGuessDTO {
    List<EuroMatchScheduleDTO> scheduleDTOs = Lists.newArrayList();//场次信息
    List<EuroGuessResultDTO> euroGuessResultDTOs = Lists.newArrayList();//竞猜信息
    private boolean login = false;
    private String lastMatchDate;
    private String nextMatchDate;
    private String expireDate;
    private boolean guessed = false;
    private String cellphone;
}
