package com.zdmoney.models.zdpay;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.Date;

public class UserGrantBO extends BaseBO {

    private Long id;

    /**
     * 用户Id
     */
    private Long customerId;

    /**
     * 手机号
     */
    private String cellPhone;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 格式： 010000000000
     * 0-未授权、 1-已授权；
     * 权限数值占位说明：
     * 第 1 位：自动出借
     * 第 2 位： 自动还款
     * 第 3 位： 自动代偿还款
     * 第 4 位： 自动缴费
     * 第 5-12 位预留占位
     */
    private String authStatus;

    /**
     * 自动出借授权期限
     */
    private String autoLendTerm;

    /**
     * 自动出借授权额度
     */
    private Long autoLendAmt = 0L;

    /**
     * 已使用授权出借额度
     */
    private Long usedLendAmt = 0L;

    /**
     * 剩余授权出借额度
     */
    private Long leaveLendAmt = 0L;

    /**
     * 自动还款授权期限
     */
    private String autoRepayTerm;

    /**
     * 自动还款授权额度
     */
    private Long autoRepayAmt = 0L;

    /**
     * 自动代偿还款授权期限
     */
    private String autoCompenTerm;

    /**
     * 自动代偿还款授权额度
     */
    private Long autoCompenAmt = 0L;

    /**
     * 缴费授权期限
     */
    private String autoFeeTerm;

    /**
     * 缴费授权额度
     */
    private Long autoFeeAmt = 0L;

    /**
     * 用户属性
     * 1.出借人2.借款人3.营销户 4.手续费户5.代偿户6.消费金融商家7.平台自有资金账户 9.担保方手续费户
     */
    private String userAttr;

    /**
     * 授权状态 0:充足 1:不足
     */
    private Integer status;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

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

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(String authStatus) {
        this.authStatus = authStatus;
    }

    public String getAutoLendTerm() {
        return autoLendTerm;
    }

    public void setAutoLendTerm(String autoLendTerm) {
        this.autoLendTerm = autoLendTerm;
    }

    public Long getAutoLendAmt() {
        return autoLendAmt;
    }

    public void setAutoLendAmt(Long autoLendAmt) {
        this.autoLendAmt = autoLendAmt;
    }

    public Long getUsedLendAmt() {
        return usedLendAmt;
    }

    public void setUsedLendAmt(Long usedLendAmt) {
        this.usedLendAmt = usedLendAmt;
    }

    public Long getLeaveLendAmt() {
        return leaveLendAmt;
    }

    public void setLeaveLendAmt(Long leaveLendAmt) {
        this.leaveLendAmt = leaveLendAmt;
    }

    public String getAutoRepayTerm() {
        return autoRepayTerm;
    }

    public void setAutoRepayTerm(String autoRepayTerm) {
        this.autoRepayTerm = autoRepayTerm;
    }

    public Long getAutoRepayAmt() {
        return autoRepayAmt;
    }

    public void setAutoRepayAmt(Long autoRepayAmt) {
        this.autoRepayAmt = autoRepayAmt;
    }

    public String getAutoCompenTerm() {
        return autoCompenTerm;
    }

    public void setAutoCompenTerm(String autoCompenTerm) {
        this.autoCompenTerm = autoCompenTerm;
    }

    public Long getAutoCompenAmt() {
        return autoCompenAmt;
    }

    public void setAutoCompenAmt(Long autoCompenAmt) {
        this.autoCompenAmt = autoCompenAmt;
    }

    public String getAutoFeeTerm() {
        return autoFeeTerm;
    }

    public void setAutoFeeTerm(String autoFeeTerm) {
        this.autoFeeTerm = autoFeeTerm;
    }

    public Long getAutoFeeAmt() {
        return autoFeeAmt;
    }

    public void setAutoFeeAmt(Long autoFeeAmt) {
        this.autoFeeAmt = autoFeeAmt;
    }

    public String getUserAttr() {
        return userAttr;
    }

    public void setUserAttr(String userAttr) {
        this.userAttr = userAttr;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
