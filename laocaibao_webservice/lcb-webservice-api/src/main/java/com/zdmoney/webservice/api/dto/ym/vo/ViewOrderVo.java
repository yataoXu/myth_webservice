package com.zdmoney.webservice.api.dto.ym.vo;/**
 * Created by pc05 on 2017/11/22.
 */

import com.zdmoney.webservice.api.common.dto.BaseDto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 描述 :
 *
 * @author : huangcy
 * @create : 2017-11-22 10:14
 * @email : huangcy01@zendaimoney.com
 **/
@Data
public class ViewOrderVo extends BaseDto{
    private Long orderId;

    private String orderNo;

    private String customerCode;

    private BigDecimal orderAmt;

    private Integer productExpiry;

    private String orderStatus;

    private Date interestStart;

    private Date expireDate;

    private String inviteCode;

    private Date orderTime;

    private String productType;

    private Long firstOrder;

    private String productName;

    private String assignFlag;

    private Date assignDate;

    private Date settleDate;

    private Integer closePeriod;

    private Date updateTime;

    private String initOrderNo;

    private BigDecimal couponIncreaseRevenueAmt;

    private BigDecimal couponAmount;

    private BigDecimal integralRmbAmount;

    /**
     * 0-非转让订单 1-转让订单
     */
    private String transferType;

    /**
     * 订单退出时间
     */
    private Date exitDate;

	/**
	 * 续投次数
	 */
	private Integer reInvestCount;

	/**
	 * 续投申请日期
	 */
	private String reCreateTime;

	/**
	 * 新的封闭期结束日
	 */
	private String reCurrentEndDate;

	/**
	 * 续期订单原订单本息和（续期金额）
	 */
	private BigDecimal reReinvestAmt;

	/**
	 * 续期天数
	 */
	private Integer reinvestDays;

	/**
	 * 新的封闭期起始日
	 */
	private String reCurrentStartDate;

}
