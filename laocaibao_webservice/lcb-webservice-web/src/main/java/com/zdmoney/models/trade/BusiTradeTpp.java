package com.zdmoney.models.trade;

import javax.persistence.*;

@Table(name = "BUSI_TRADE_TPP")
public class BusiTradeTpp {
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select SEQ_BUSI_TRADE_TPP.nextval from dual")
//    @SequenceGenerator(name="",sequenceName="SEQ_BUSI_TRADE_TPP")
    private Long id;

    @Column(name = "TPP_AGREEMENT")
    private String tppAgreement;

    @Column(name = "TRADE_FLOW_ID")
    private Long tradeFlowId;

    /**
     * @return ID
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return TPP_AGREEMENT
     */
    public String getTppAgreement() {
        return tppAgreement;
    }

    /**
     * @param tppAgreement
     */
    public void setTppAgreement(String tppAgreement) {
        this.tppAgreement = tppAgreement;
    }

    /**
     * @return TRADE_FLOW_ID
     */
    public Long getTradeFlowId() {
        return tradeFlowId;
    }

    /**
     * @param tradeFlowId
     */
    public void setTradeFlowId(Long tradeFlowId) {
        this.tradeFlowId = tradeFlowId;
    }
}