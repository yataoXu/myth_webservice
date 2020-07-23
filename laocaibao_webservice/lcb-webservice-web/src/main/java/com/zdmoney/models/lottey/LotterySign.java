package com.zdmoney.models.lottey;

import com.zdmoney.common.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "T_LOT_LOTTERY_SIGN")
@Setter
@Getter
public class LotterySign extends AbstractEntity<Long> {

    @Id
    private Long id;

    /**连续(累计)签到天数 */
    private Integer signDays;

    /**1:红包 2：加息券 3:预约券 */
    private String awardType;

    /**最大红包金额*/
    private BigDecimal maxAmt;

    /**最小红包金额*/
    private BigDecimal minAmt;

    /**最大加息利率*/
    private BigDecimal maxRate;

    /**最小加息利率*/
    private BigDecimal minRate;

    /**有效天数*/
    private Integer validDays;

    /**规则编号*/
    private Integer ruldId;

    /**创建时间*/
    private Date createDate;

    /**更新时间*/
    private Date updateDate;

    /**签到类型 0:连续签到 1:累计签到*/
    private String signType;

    /**活动开始时间*/
    private Date startTime;

    /**活动结束时间*/
    private Date endTime;

    /**累计签到状态 0-停用 1-启用*/
    private String status;
}