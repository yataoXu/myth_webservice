package com.zdmoney.models.team;

import com.zdmoney.common.entity.AbstractEntity;
import com.zdmoney.common.handler.SecurityString;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "TEAM_MEMBER_INFO")
public class TeamMemberInfo extends AbstractEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select SEQ_TEAM_MEMBER_INFO.nextval from dual")
    private Long id;

    private Long teamId;

    private Long memberId;

    private SecurityString memberCellphone = SecurityString.valueof(null);

    private String memberName;

    private String memberStatus;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date enqueueTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date leaveTime;

    private Long inviteId;

    private String remark;

    private String memberNumber;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getMemberCellphone() {
        return memberCellphone.getValue();
    }

    public void setMemberCellphone(String memberCellphone) {
        this.memberCellphone.setValue(memberCellphone);
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public String getMemberStatus() {
        return memberStatus;
    }

    public void setMemberStatus(String memberStatus) {
        this.memberStatus = memberStatus;
    }

    public Date getEnqueueTime() {
        return enqueueTime;
    }

    public void setEnqueueTime(Date enqueueTime) {
        this.enqueueTime = enqueueTime;
    }

    public Date getLeaveTime() {
        return leaveTime;
    }

    public void setLeaveTime(Date leaveTime) {
        this.leaveTime = leaveTime;
    }

    public Long getInviteId() {
        return inviteId;
    }

    public void setInviteId(Long inviteId) {
        this.inviteId = inviteId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getMemberNumber() {
        return memberNumber;
    }

    public void setMemberNumber(String memberNumber) {
        this.memberNumber = memberNumber;
    }
}