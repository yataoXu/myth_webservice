package com.zdmoney.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.zdmoney.models.product.BusiProduct;
import com.zdmoney.utils.CoreUtil;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.math.BigDecimal;
import java.util.Date;

public class BusiFinancePlanVO extends BusiProduct{
	
	private BigDecimal yearRate;
	private BigDecimal remaindAmt;
	private BigDecimal nowProportion;
	private int investPeriod;
	private String sellOut;
	private BigDecimal remaindTime;
	private BigDecimal remaindSaleStartTime;
	private BigDecimal addInterest;//活动加息
	private String isTogether;
	private String agreementUrl;
	private String productDetailUrl;
	private String imgUrl;
	private String contractTypeDict;
	private String contractDesc;
	private int surplusInvestPeriod;//剩余投资期限
	private BigDecimal transferPrice = new BigDecimal(0);//转让金额
	private String fundTypeName;//资产类型
	@JSONField(format = "yyyy-MM-dd")
	private Date transferDate;//转让日期

	@JSONField(format = "yyyy-MM-dd")
	private Date transferEndDate;//到期日期

	public Date getTransferEndDate() {
		return transferEndDate;
	}

	public void setTransferEndDate(Date transferEndDate) {
		this.transferEndDate = transferEndDate;
	}

	public BigDecimal getTransferPrice() {
		return transferPrice;
	}

	public void setTransferPrice(BigDecimal transferPrice) {
		this.transferPrice = transferPrice;
	}


	public Date getTransferDate() {
		return transferDate;
	}

	public void setTransferDate(Date transferDate) {
		this.transferDate = transferDate;
	}

	public String getFundTypeName() {
		return fundTypeName;
	}
	public void setFundTypeName(String fundTypeName) {
		this.fundTypeName = fundTypeName;
	}

	public int getSurplusInvestPeriod() {
		return surplusInvestPeriod;
	}

	public void setSurplusInvestPeriod(int surplusInvestPeriod) {
		this.surplusInvestPeriod = surplusInvestPeriod;
	}

	public BigDecimal getRemaindAmt() {
		if(remaindAmt==null){
			return remaindAmt;
		}
		return new BigDecimal(CoreUtil.BigDecimalAccurate(this.remaindAmt,2));
	}
	public void setRemaindAmt(BigDecimal remaindAmt) {
		this.remaindAmt = remaindAmt;
	}
	public int getInvestPeriod() {
		return investPeriod;
	}
	public void setInvestPeriod(int investPeriod) {
		this.investPeriod = investPeriod;
	}
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
	public String getSellOut() {
		return sellOut;
	}
	public void setSellOut(String sellOut) {
		this.sellOut = sellOut;
	}
	public BigDecimal getRemaindTime() {
		if(remaindTime==null){
			return remaindTime;
		}
		return new BigDecimal(CoreUtil.BigDecimalAccurate(this.remaindTime,0));
	}
	public void setRemaindTime(BigDecimal remaindTime) {
		this.remaindTime = remaindTime;
	}
	public BigDecimal getNowProportion() {
		if(nowProportion==null){
			return nowProportion;
		}
		BigDecimal big100 = new BigDecimal(100);
		BigDecimal tmpnowProportion = nowProportion.multiply(big100);
		return tmpnowProportion.setScale(0, BigDecimal.ROUND_DOWN);
	}
	public void setNowProportion(BigDecimal nowProportion) {
		this.nowProportion = nowProportion;
	}
	
	public BigDecimal getYearRate() {
		if(yearRate==null){
			return yearRate;
		}
		BigDecimal big100 = new BigDecimal(100);
		return yearRate.multiply(big100);
	}
	public BigDecimal getYearRateProportion() {
		if (yearRate == null) {
			return null;
		}
		BigDecimal big100 = new BigDecimal(100);
		return yearRate.multiply(big100);
	}
	public void setYearRate(BigDecimal yearRate) {
		this.yearRate = yearRate;
	}
	public BigDecimal getRemaindSaleStartTime() {
		return remaindSaleStartTime;
	}
	public void setRemaindSaleStartTime(BigDecimal remaindSaleStartTime) {
		this.remaindSaleStartTime = remaindSaleStartTime;
	}

	public BigDecimal getAddInterest() {
		return addInterest;
	}

	@Override
	public void setAddInterest(BigDecimal addInterest) {
		this.addInterest = addInterest;
	}

	public String getIsTogether() {
		return isTogether;
	}

	public void setIsTogether(String isTogether) {
		this.isTogether = isTogether;
	}

	public String getAgreementUrl() {
		return agreementUrl;
	}

	public void setAgreementUrl(String agreementUrl) {
		this.agreementUrl = agreementUrl;
	}


	public String getProductDetailUrl() {
		return productDetailUrl;
	}

	public void setProductDetailUrl(String productDetailUrl) {
		this.productDetailUrl = productDetailUrl;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public String getContractDesc() {
		return contractDesc;
	}

	public void setContractDesc(String contractDesc) {
		this.contractDesc = contractDesc;
	}

	public String getContractTypeDict() {
		return contractTypeDict;
	}

	public void setContractTypeDict(String contractTypeDict) {
		this.contractTypeDict = contractTypeDict;
	}

}
