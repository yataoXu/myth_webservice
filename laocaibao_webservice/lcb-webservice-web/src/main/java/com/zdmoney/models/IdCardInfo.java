package com.zdmoney.models;

import java.io.Serializable;
import java.util.Date;

public class IdCardInfo implements Serializable {

	/**
	 * 页面展示domain层级
	 */
	private static final long serialVersionUID = 1L;

	/**
	 *自增id
	 */
	private Integer id;
	
	/**
	 * 身份证地址
	 */
	private String address;
	
	/**
	 * 身份证头像
	 */
	private String avatar;
	
	/**
	 * 生日
	 */
	private Date birth;
	
	/**
	 * 性别（MALE:男；FEMALE:女）
	 */
	private String gender;
	
	/**
	 * 身份证号码
	 */
	private String idno;
	
	/**
	 * 真实姓名	
	 */
	private String name;
	
	/**
	 * 所属用户	
	 */
	private Integer userId;
	
	/**
	 * 认证时间	
	 */
	private Date validTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public Date getBirth() {
		return birth;
	}

	public void setBirth(Date birth) {
		this.birth = birth;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getIdno() {
		return idno;
	}

	public void setIdno(String idno) {
		this.idno = idno;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Date getValidTime() {
		return validTime;
	}

	public void setValidTime(Date validTime) {
		this.validTime = validTime;
	}

	
}