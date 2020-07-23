package com.zdmoney.models.lottey;/**
 * Created by pc05 on 2017/4/1.
 */

import com.zdmoney.common.entity.AbstractEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 描述 : 投资到期弹窗表 实体类
 *
 * @author : huangcy
 * @create : 2017-04-01 10:25
 * @email : huangcy01@zendaimoney.com
 **/
@Table(name = "T_LOT_INVEST_ALERT")
@Setter
@Getter
public class LotteryInvestAlert extends AbstractEntity<Long> {
    @Id
    private Long id;

    private String cmNumber;

    private String cmName;

    private Date alertStartDate;

    private Date alertEndDate;

    private Date shareStartDate;

    private Date shareEndDate;

    private String isAlert;

    private Date createDate;

    private Date modifyDate;

    private String createBy;
}
