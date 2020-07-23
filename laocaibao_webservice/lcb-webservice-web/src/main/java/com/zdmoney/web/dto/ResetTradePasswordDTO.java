package com.zdmoney.web.dto;

/**
 * Created by 00225181 on 2016/3/25.
 */
public class ResetTradePasswordDTO {
    private int errorTime = 0;
    private boolean isLock = false;
    private String msg = "";

    public int getErrorTime() {
        return errorTime;
    }

    public void setErrorTime(int errorTime) {
        this.errorTime = errorTime;
    }

    public boolean isLock() {
        return isLock;
    }

    public void setIsLock(boolean isLock) {
        this.isLock = isLock;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
