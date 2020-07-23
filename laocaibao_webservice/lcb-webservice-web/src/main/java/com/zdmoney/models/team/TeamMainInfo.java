package com.zdmoney.models.team;

import com.zdmoney.common.entity.AbstractEntity;
import com.zdmoney.common.handler.SecurityString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "TEAM_MAIN_INFO")
public class TeamMainInfo extends AbstractEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select SEQ_TEAM_MAIN_INFO.nextval from dual")
    private Long id;

    private String teamName;

    private Long teamNum;

    private String slogan;

    private Long captainId;

    private SecurityString captainCellphone = SecurityString.valueof(null);

    private String captainName;

    private BigDecimal wealthValue;

    private Short teamStatus;

    private String remark;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date dissolveTime;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Long getTeamNum() {
        return teamNum;
    }

    public void setTeamNum(Long teamNum) {
        this.teamNum = teamNum;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public Long getCaptainId() {
        return captainId;
    }

    public void setCaptainId(Long captainId) {
        this.captainId = captainId;
    }

    public String getCaptainCellphone() {
        return captainCellphone.getValue();
    }

    public void setCaptainCellphone(String captainCellphone) {
        this.captainCellphone.setValue(captainCellphone);
    }

    public String getCaptainName() {
        return captainName;
    }

    public void setCaptainName(String captainName) {
        this.captainName = captainName;
    }

    public BigDecimal getWealthValue() {
        return wealthValue;
    }

    public void setWealthValue(BigDecimal wealthValue) {
        this.wealthValue = wealthValue;
    }

    public Short getTeamStatus() {
        return teamStatus;
    }

    public void setTeamStatus(Short teamStatus) {
        this.teamStatus = teamStatus;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getDissolveTime() {
        return dissolveTime;
    }

    public void setDissolveTime(Date dissolveTime) {
        this.dissolveTime = dissolveTime;
    }
}