package com.zdmoney.models;

import java.math.BigDecimal;
import java.util.Date;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.zdmoney.service.JsonDateYMDHMSSerializer;

/**
 * 对应流水表
 * @author yangp
 *
 */
public class RecordDetail {
    private Long id;//

    private Long customerId;//用户Id

    private String paySeq;//支付流水号

    private String type;//类型
    
    private String flag;//正负标志
    
    private String orderNum;//订单号

    private Date createDate;//创建时间

    private BigDecimal tradeAmt;//交易金额
    
    private String remark; //备注
    

    public String getRemark() {
    
		return remark;
    	
	}

	public void setRemark(String remark) {
		this.remark = remark;
		
	}

	public String getPaySeq() {
		return paySeq;
	}

	public void setPaySeq(String paySeq) {
		this.paySeq = paySeq;
	}

	public String getType() {
		return type;
	}
	

	public String getTypeName() {
		
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

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    @JsonSerialize(using=JsonDateYMDHMSSerializer.class)
	public Date getCreateDate() {
		return createDate;
	}
	
	

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	

}