package com.zdmoney.web.dto;

/**
 * Created by 00232385 on 2015/10/30.
 */
public class PacketDTO {

    private String packetId; //���ID

    private String amount; //������

    private String investAmount; //Ͷ�ʽ��

    private String investPrice; //Ͷ������

    private String startTime; //��Ч��ʼʱ��

    private String endTime; //��Ч����ʱ��

    private String status;

    private String condition;

    private String packetTime;

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getPacketTime() {
        return packetTime;
    }

    public void setPacketTime(String packetTime) {
        this.packetTime = packetTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPacketId() {
        return packetId;
    }

    public void setPacketId(String packetId) {
        this.packetId = packetId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getInvestAmount() {
        return investAmount;
    }

    public void setInvestAmount(String investAmount) {
        this.investAmount = investAmount;
    }

    public String getInvestPrice() {
        return investPrice;
    }

    public void setInvestPrice(String investPrice) {
        this.investPrice = investPrice;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
