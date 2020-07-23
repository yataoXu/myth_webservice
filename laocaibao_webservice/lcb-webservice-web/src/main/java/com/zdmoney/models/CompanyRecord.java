package com.zdmoney.models;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 对应公司流水表
 * @author yangp
 *
 */
public class CompanyRecord {
    private Long id;//

    private Long companyId;//用户Id

    private String paySeq;//支付流水号

    private String type;//类型
    
    private String flag;//正负标志
    
    private String orderNum;//订单号

    private Date tradeDate;//交易时间

    private BigDecimal tradeAmt;//交易金额

    public String getPaySeq() {
		return paySeq;
	}

	public void setPaySeq(String paySeq) {
		this.paySeq = paySeq;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	public BigDecimal getTradeAmt() {
		return tradeAmt;
	}

	public void setTradeAmt(BigDecimal tradeAmt) {
		this.tradeAmt = tradeAmt;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public Date getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(Date tradeDate) {
		this.tradeDate = tradeDate;
	}


}