package com.zdmoney.models.team;

import com.zdmoney.common.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Table(name = "TEAM_MEMBER_MONTH_INVEST_FLOW")
@Setter
@Getter
public class TeamMemberMonthInvestFlow extends AbstractEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select SEQ_TEAM_MEMBER_MONTH_INV_FLOW.nextval from dual")
    private Long id;

    private Long memberId;

    private Long teamId;

    private String investTime;

    private BigDecimal investAmt;

    private Long receiveLcbAmt;

    private Long receiveAddInterestcouponNum;


}