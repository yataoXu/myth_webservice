package com.zdmoney.web.dto.euroCup;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * Created by 00225181 on 2016/6/5.
 */
@Getter
@Setter
public class OneDayWinner {
    private String cellPhone;
    private String reward;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date guessDate;
    private boolean myself;
}
