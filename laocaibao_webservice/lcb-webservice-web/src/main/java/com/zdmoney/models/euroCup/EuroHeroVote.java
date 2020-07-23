package com.zdmoney.models.euroCup;

import com.zdmoney.common.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import javax.persistence.*;

@Table(name = "EURO_HERO_VOTE")
@Getter
@Setter
public class EuroHeroVote extends AbstractEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select SEQ_EURO_HERO_VOTE.nextval from dual")
    private Long id;

    @Column(name = "VOTE_MOBILE")
    private Long voteMobile;

    @Column(name = "GUESS_MOBILE")
    private Long guessMobile;

    @Column(name = "VOTE_DATE")
    private Date voteDate;

}