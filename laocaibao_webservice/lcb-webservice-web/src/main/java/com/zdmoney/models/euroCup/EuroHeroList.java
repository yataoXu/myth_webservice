package com.zdmoney.models.euroCup;

import com.zdmoney.common.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Table(name = "EURO_HERO_LIST")
@Getter
@Setter
public class EuroHeroList  extends AbstractEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select SEQ_EURO_HERO_LIST.nextval from dual")
    private Long id;

    @Column(name = "CUSTOMER_MOBILE")
    private Long customerMobile;

    @Column(name = "GUESS_NUM")
    private Short guessNum;

    @Column(name = "VOTE_NUM")
    private Integer voteNum;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "UPDATE_DATE")
    private Date updateDate;

}