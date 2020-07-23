package com.zdmoney.models;

import com.zdmoney.integral.api.dto.cash.CashSearchResDto;
import com.zdmoney.utils.DateUtil;
import lombok.Data;
import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by 46186 on 2018/12/7.
 */
@Data
public class CashSearchResInfo {
    private String cashNo;
    private String accountNo;
    private String amount;
    private String investMin;
    private String investMax;
    private String investPeriodMin;
    private String investPeriodMax;
    private String period;
    private String openEndTime;
    private String getEndTime;
    private String status;
    private String orderNo;
    private String publishSource;
    private String createTime;
    private String modifyTime;
    private String dateString;
    private String conditionString;

    public CashSearchResInfo(CashSearchResDto dto){
        this.cashNo = dto.getCashNo();
        this.accountNo = dto.getAccountNo();
        this.amount = dto.getAmount()==null?"0":dto.getAmount().toString();
        this.investMin = dto.getInvestMin()==null?"":dto.getInvestMin().toString();
        this.investMax = dto.getInvestMax()==null?"":dto.getInvestMax().toString();
        this.investPeriodMin = dto.getInvestPeriodMin()==null?"0":dto.getInvestPeriodMin().toString();
        this.investPeriodMax = dto.getInvestPeriodMax()==null?"0":dto.getInvestPeriodMax().toString();
        this.period = dto.getPeriod()==null?"0":dto.getPeriod().toString();
        this.openEndTime = dto.getOpenEndTime() == null?"":DateUtil.getDateFormatString(dto.getOpenEndTime(),DateUtil.fullFormat);
        this.getEndTime =  dto.getGetEndTime() == null?"":DateUtil.getDateFormatString(dto.getGetEndTime(),DateUtil.fullFormat);
        this.status = dto.getStatus();
        this.orderNo = dto.getOrderNo();
        this.publishSource = dto.getPublishSource();
        this.createTime = dto.getCreateTime() == null?"":DateUtil.getDateFormatString(dto.getCreateTime(),DateUtil.fullFormat);
        this.modifyTime = dto.getModifyTime() == null?"":DateUtil.getDateFormatString(dto.getModifyTime(),DateUtil.fullFormat);
        this.dateString = dto.getDateString();
        this.conditionString = dto.getConditionString();
    }
    public CashSearchResInfo(){

    }

}
