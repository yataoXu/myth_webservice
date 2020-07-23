package com.zdmoney.models.lottey;/**
 * Created by pc05 on 2017/4/1.
 */

import com.zdmoney.common.handler.SecurityString;

import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 描述 :
 *
 * @author : huangcy
 * @create : 2017-04-01 17:11
 * @email : huangcy01@zendaimoney.com
 **/
@Table(name = "T_LOT_INVITATION_RECORD")
public class LotteryInvitationRecord {

    private Long id;

    private String typeNo;

    private String cmNumber;

    private String cmName;

    private SecurityString cmCellPhone = SecurityString.valueof(null);

    private String investName;

    private SecurityString investPhone = SecurityString.valueof(null);

    private BigDecimal amount;

    private String couponType;

    private Long investId;

    private Date createDate;

    private Date modifyDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTypeNo() {
        return typeNo;
    }

    public void setTypeNo(String typeNo) {
        this.typeNo = typeNo;
    }

    public String getCmNumber() {
        return cmNumber;
    }

    public void setCmNumber(String cmNumber) {
        this.cmNumber = cmNumber;
    }

    public String getCmName() {
        return cmName;
    }

    public void setCmName(String cmName) {
        this.cmName = cmName;
    }

    public String getCmCellPhone() {
        return cmCellPhone.getValue();
    }

    public void setCmCellPhone(String cmCellPhone) {
        this.cmCellPhone.setValue(cmCellPhone);
    }

    public String getInvestName() {
        return investName;
    }

    public void setInvestName(String investName) {
        this.investName = investName;
    }

    public String getInvestPhone() {
        return investPhone.getValue();
    }

    public void setInvestPhone(String investPhone) {
        this.investPhone.setValue(investPhone);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCouponType() {
        return couponType;
    }

    public void setCouponType(String couponType) {
        this.couponType = couponType;
    }

    public Long getInvestId() {
        return investId;
    }

    public void setInvestId(Long investId) {
        this.investId = investId;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Date getModifyDate() {
        return modifyDate;
    }

    public void setModifyDate(Date modifyDate) {
        this.modifyDate = modifyDate;
    }
}
