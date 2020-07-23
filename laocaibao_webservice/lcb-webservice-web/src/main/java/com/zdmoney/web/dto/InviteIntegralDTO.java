package com.zdmoney.web.dto;

public class InviteIntegralDTO {
	/*
	 * 邀请好友当日积分
	 */
	private Long inviteTodayIntegral;
	/*
	 * 邀请好友累计积分
	 */
	private Long inviteAllIntegral;
	/**
	 * 邀请好友总人数
	 */
	private Integer inviteNum;

	/**
	 * 邀请码
	 */
	private String cmInviteCode;

	/**
	 * 提示
	 */
	private String tips;

	/**
	 * 当月邀请数量
	 */
	private Integer curMonthInviteNum;


	public Long getInviteTodayIntegral() {
		return inviteTodayIntegral;
	}

	public void setInviteTodayIntegral(Long inviteTodayIntegral) {
		this.inviteTodayIntegral = inviteTodayIntegral;
	}

	public Long getInviteAllIntegral() {
		return inviteAllIntegral;
	}

	public void setInviteAllIntegral(Long inviteAllIntegral) {
		this.inviteAllIntegral = inviteAllIntegral;
	}

	public Integer getInviteNum() {
		return inviteNum;
	}

	public void setInviteNum(Integer inviteNum) {
		this.inviteNum = inviteNum;
	}

	public String getCmInviteCode() {
		return cmInviteCode;
	}

	public void setCmInviteCode(String cmInviteCode) {
		this.cmInviteCode = cmInviteCode;
	}

	public String getTips() {
		return tips;
	}

	public void setTips(String tips) {
		this.tips = tips;
	}

	public Integer getCurMonthInviteNum() {
		return curMonthInviteNum;
	}

	public void setCurMonthInviteNum(Integer curMonthInviteNum) {
		this.curMonthInviteNum = curMonthInviteNum;
	}
}
