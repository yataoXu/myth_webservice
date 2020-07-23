package com.zdmoney.vo;


/**
 * Created by 00245337 on 2016/9/26.
 */
public class PayBespeakDTO {
    private String bespeakNo;       //加息券编号
    private String conditionString;     //优惠券使用描述
    private String bespeakTitle;        //加息券标题
    private String bespeakMemo;         //使用规则
    private Integer validDate;     //有效日期
    private Integer flag= -1;  //1可使用, 2已过期，3已使用

    public String getBespeakNo() {
        return bespeakNo;
    }

    public void setBespeakNo(String bespeakNo) {
        this.bespeakNo = bespeakNo;
    }

    public String getConditionString() {
        return conditionString;
    }

    public void setConditionString(String conditionString) {
        this.conditionString = conditionString;
    }

    public String getBespeakTitle() {
        return bespeakTitle;
    }

    public void setBespeakTitle(String bespeakTitle) {
        this.bespeakTitle = bespeakTitle;
    }

    public String getBespeakMemo() {
        return bespeakMemo;
    }

    public void setBespeakMemo(String bespeakMemo) {
        this.bespeakMemo = bespeakMemo;
    }

    public Integer getValidDate() {
        return validDate;
    }

    public void setValidDate(Integer validDate) {
        this.validDate = validDate;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }
}
