package com.zdmoney.models.lottey;/**
 * Created by pc05 on 2017/4/5.
 */

import com.zdmoney.common.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 描述 :
 *
 * @author : huangcy
 * @create : 2017-04-05 15:37
 * @email : huangcy01@zendaimoney.com
 **/
@Table(name = "T_LOT_COUPON_RULE")
@Setter
@Getter
public class LotteryCouponRule extends AbstractEntity<Long> {
    @Id
    private Long id;

    /* 红包金额 */
    private Integer amount;

    /* 有效天数 */
    private Integer period;

    /* 投资金额 */
    private Integer investAmount;

    /* 投资期限 */
    private Integer investPeriod;

    private BigDecimal rate;

    private Integer days;

    private String type;

    private Integer investMaxAmount;

    private Integer investMaxPeriod;

    private Date createDate;

    private String createBy;

    private Date modifyDate;

    private String modifyBy;
}
