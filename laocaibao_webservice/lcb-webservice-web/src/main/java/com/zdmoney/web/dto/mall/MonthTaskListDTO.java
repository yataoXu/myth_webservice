package com.zdmoney.web.dto.mall;

/**
 * Created by 00225181 on 2016/3/2.
 */
public class MonthTaskListDTO {
    private Long investAmt;
    private Long lcbAmt;
    private Integer chestNum;

    public Long getInvestAmt() {
        return investAmt;
    }

    public void setInvestAmt(Long investAmt) {
        this.investAmt = investAmt;
    }

    public Long getLcbAmt() {
        return lcbAmt;
    }

    public void setLcbAmt(Long lcbAmt) {
        this.lcbAmt = lcbAmt;
    }

    public Integer getChestNum() {
        return chestNum;
    }

    public void setChestNum(Integer chestNum) {
        this.chestNum = chestNum;
    }
}
