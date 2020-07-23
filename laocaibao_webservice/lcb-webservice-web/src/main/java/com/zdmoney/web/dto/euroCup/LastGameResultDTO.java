package com.zdmoney.web.dto.euroCup;

import com.zdmoney.models.euroCup.EuroMatchSchedule;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * Created by 00225181 on 2016/6/5.
 */
@Getter
@Setter
public class LastGameResultDTO {
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date gameDate;
    private List<OneDayWinner> winners;
    private List<EuroMatchSchedule> schedules;

}
